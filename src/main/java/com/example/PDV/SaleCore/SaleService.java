package com.example.PDV.SaleCore;

import com.example.PDV.BoxCore.BoxEntity;
import com.example.PDV.BoxCore.BoxRepository;
import com.example.PDV.BoxCore.BoxService;
import com.example.PDV.Exceptions.BoxNotFound;
import com.example.PDV.Exceptions.PriceIsNotEqual;
import com.example.PDV.Exceptions.ProductNotFound;
import com.example.PDV.Exceptions.SaleNotFound;
import com.example.PDV.ProductsCore.ProductEntity;
import com.example.PDV.ProductsCore.ProductRepository;
import com.example.PDV.SaleCore.Entities.ItemsForSaleEntity;
import com.example.PDV.SaleCore.Entities.PaymentOfSaleEntity;
import com.example.PDV.SaleCore.Entities.SaleEntity;
import com.example.PDV.SaleCore.Repositories.ItemsForSaleRepository;
import com.example.PDV.SaleCore.Repositories.PaymentOfSaleRepository;
import com.example.PDV.SaleCore.Repositories.SaleRepository;
import com.example.PDV.SaleCore.SaleDtos.SaleEntryDto;
import com.example.PDV.SaleCore.SaleEnums.KindOfPayment;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SaleService {

    private final ItemsForSaleRepository itemsForSaleRepository;

    private final SaleRepository saleRepository;

    private PaymentOfSaleRepository paymentOfSaleRepository;

    private final BoxRepository boxRepository;

    private final BoxService boxService;

    private final ProductRepository productRepository;

    public SaleService(ItemsForSaleRepository itemsForSaleRepository,
                       SaleRepository saleRepository,
                       BoxRepository boxRepository,
                       BoxService boxService,
                       PaymentOfSaleRepository paymentOfSaleRepository,
                       ProductRepository productRepository) {

        this.itemsForSaleRepository = itemsForSaleRepository;
        this.saleRepository = saleRepository;
        this.paymentOfSaleRepository = paymentOfSaleRepository;
        this.boxService = boxService;
        this.boxRepository = boxRepository;
        this.productRepository = productRepository;
    }

    public void makeSale(SaleEntryDto saleEntry) {

        BoxEntity box = boxRepository.findById(saleEntry.getBoxId())
                .orElseThrow(BoxNotFound::new);

        SaleEntity sale = new SaleEntity(box);

        List<ItemsForSaleEntity> sales = new ArrayList<>();

        List<PaymentOfSaleEntity> payments = new ArrayList<>();

        BigDecimal valueTotal = new BigDecimal(0);

        BigDecimal totalPricePayment = new BigDecimal(0);

        Integer quantity = 0;

        for (int i = 0; i < saleEntry.getItems().getProducts().size(); i++) {

            ProductEntity product = productRepository.findById(saleEntry.getItems()
                    .getProducts().get(i)).orElseThrow(ProductNotFound::new);

            ItemsForSaleEntity items = new ItemsForSaleEntity(product, sale);

            valueTotal = valueTotal.add(product.getValue());

            quantity++;

            sales.add(items);
        }

        for (Map.Entry<KindOfPayment, BigDecimal> entry : saleEntry.getPayment()
                        .getInfoPayment().entrySet()) {

            PaymentOfSaleEntity payment = new PaymentOfSaleEntity(entry.getKey(),
                    entry.getValue(), sale);

            totalPricePayment = totalPricePayment.add(entry.getValue());

            payments.add(payment);
        }

        if (totalPricePayment == null || valueTotal == null
                || totalPricePayment.compareTo(valueTotal) != 0) {

            throw new PriceIsNotEqual();
        }

        sale.setQuantity(quantity);
        sale.setTotalValueSale(valueTotal);
        saleRepository.save(sale);

        paymentOfSaleRepository.saveAll(payments);

        box.setTotalValue(valueTotal);
        boxRepository.save(box);

        boxService.evictCacheBoxOpenedForCurrentUser();

        itemsForSaleRepository.saveAll(sales);

    }

    public void cancelSele(Integer saleId) {

        ItemsForSaleEntity sale = itemsForSaleRepository.findById(saleId)
                .orElseThrow(SaleNotFound::new);

        itemsForSaleRepository.delete(sale);
    }

}
