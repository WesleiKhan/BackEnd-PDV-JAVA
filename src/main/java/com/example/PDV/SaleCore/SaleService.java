package com.example.PDV.SaleCore;

import com.example.PDV.Agreement.AgreementEntity;
import com.example.PDV.Agreement.AgreementRepository;
import com.example.PDV.Agreement.Exceptions.AgreementNotFound;
import com.example.PDV.BoxCore.BoxEntity;
import com.example.PDV.BoxCore.BoxRepository;
import com.example.PDV.BoxCore.BoxService;
import com.example.PDV.Exceptions.BoxNotFound;
import com.example.PDV.Exceptions.PriceIsNotEqual;
import com.example.PDV.Exceptions.ProductNotFound;
import com.example.PDV.Exceptions.SaleNotFound;
import com.example.PDV.LogsCore.ActivityLogsService;
import com.example.PDV.LogsCore.Dtos.ActivityLogsEntryDto;
import com.example.PDV.LogsCore.Enums.EntityType;
import com.example.PDV.LogsCore.Enums.TypeAction;
import com.example.PDV.ProductsCore.ProductEntity;
import com.example.PDV.ProductsCore.ProductRepository;
import com.example.PDV.ProductsCore.ProductService;
import com.example.PDV.SaleCore.Entities.ItemsForSaleEntity;
import com.example.PDV.SaleCore.Entities.PaymentOfSaleEntity;
import com.example.PDV.SaleCore.Entities.SaleEntity;
import com.example.PDV.SaleCore.Repositories.ItemsForSaleRepository;
import com.example.PDV.SaleCore.Repositories.PaymentOfSaleRepository;
import com.example.PDV.SaleCore.Repositories.SaleRepository;
import com.example.PDV.SaleCore.SaleDtos.InfoOfProductsSaleDto;
import com.example.PDV.SaleCore.SaleDtos.SaleEntryDto;
import com.example.PDV.SaleCore.SaleDtos.ValueAndInstallments;
import com.example.PDV.SaleCore.SaleEnums.Installments;
import com.example.PDV.SaleCore.SaleEnums.KindOfPayment;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
public class SaleService {

    private final ItemsForSaleRepository itemsForSaleRepository;

    private final SaleRepository saleRepository;

    private final PaymentOfSaleRepository paymentOfSaleRepository;

    private final AgreementRepository agreementRepository;

    private final BoxRepository boxRepository;

    private final BoxService boxService;

    private final ProductRepository productRepository;

    private final ProductService productService;

    private final ActivityLogsService activityLogsService;

    public SaleService(ItemsForSaleRepository itemsForSaleRepository,
                       SaleRepository saleRepository,
                       BoxRepository boxRepository,
                       AgreementRepository agreementRepository,
                       BoxService boxService,
                       PaymentOfSaleRepository paymentOfSaleRepository,
                       ProductRepository productRepository,
                       ProductService productService,
                       ActivityLogsService activityLogsService) {

        this.itemsForSaleRepository = itemsForSaleRepository;
        this.saleRepository = saleRepository;
        this.paymentOfSaleRepository = paymentOfSaleRepository;
        this.agreementRepository = agreementRepository;
        this.boxService = boxService;
        this.boxRepository = boxRepository;
        this.productRepository = productRepository;
        this.productService = productService;
        this.activityLogsService = activityLogsService;
    }

    @Transactional
    public void makeSale(SaleEntryDto saleEntry, Integer id) {

        AgreementEntity agreement = agreementRepository.findById(id)
                .orElseThrow(AgreementNotFound::new);

        BoxEntity box = boxRepository.findById(saleEntry.getBoxId())
                .orElseThrow(BoxNotFound::new);

        SaleEntity sale = new SaleEntity(box, agreement);

        List<ItemsForSaleEntity> sales = new ArrayList<>();

        List<PaymentOfSaleEntity> payments = new ArrayList<>();

        BigDecimal valueTotal = BigDecimal.ZERO;

        BigDecimal totalPricePayment = BigDecimal.ZERO;

        Integer quantity = 0;

        for (int i = 0; i < saleEntry.getItems().getProducts().size(); i++) {

            ProductEntity product = productRepository.findById(saleEntry.getItems()
                    .getProducts().get(i)).orElseThrow(ProductNotFound::new);

            ItemsForSaleEntity items = new ItemsForSaleEntity(product, sale);

            valueTotal = valueTotal.add(product.getValue());

            quantity++;

            product.decreaseQuantity(1);

            sales.add(items);
        }

        BigDecimal percentageToBeAdded = valueTotal
                .multiply(agreement.getPercentage())
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        BigDecimal finalValue = valueTotal.add(percentageToBeAdded);

        for (Map.Entry<KindOfPayment, ValueAndInstallments> entry : saleEntry.getPayment()
                        .getInfoPayment().entrySet()) {

            BigDecimal paymentValue = entry.getValue().getValuePayment();

            BigDecimal proportion = paymentValue.divide(valueTotal, 6,
                    RoundingMode.HALF_UP);

            BigDecimal feePart = percentageToBeAdded
                    .multiply(proportion)
                    .setScale(2, RoundingMode.HALF_UP);

            BigDecimal finalPaymentValue = paymentValue.add(feePart);

            PaymentOfSaleEntity payment = new PaymentOfSaleEntity(entry.getKey(),
                    finalPaymentValue, sale);

            totalPricePayment = totalPricePayment.add(finalPaymentValue);

            if (entry.getKey() == KindOfPayment.CREDITO) {

                Installments installments = entry.getValue().getInstallments();

                BigDecimal installmentValue = finalPaymentValue
                                .divide(new BigDecimal(installments.getQuantity()), 2,
                                        RoundingMode.HALF_UP);

                payment.setInstallment(installments);
                payment.setInstallment_value(installments.getQuantity() + "x" +
                        " R$ " + installmentValue);
            }

            payments.add(payment);
        }

        if (totalPricePayment == null || finalValue == null
                || totalPricePayment.compareTo(finalValue) != 0) {

            System.out.println("Valor total Payment: " + totalPricePayment + " valor final: " + finalValue);
            throw new PriceIsNotEqual();
        }

        activityLogsService.createActivityLogs(new ActivityLogsEntryDto(EntityType.SALE,
                sale.getId(), TypeAction.SALE));


        sale.setQuantity(quantity);
        sale.setTotalValueSale(finalValue);
        saleRepository.save(sale);

        paymentOfSaleRepository.saveAll(payments);

        box.setTotalValue(finalValue);
        boxRepository.save(box);

        boxService.evictCacheBoxOpenedForCurrentUser();

        itemsForSaleRepository.saveAll(sales);

        productService.evichProducts();

    }

    public void cancelSele(Integer saleId) {

        ItemsForSaleEntity sale = itemsForSaleRepository.findById(saleId)
                .orElseThrow(SaleNotFound::new);

        activityLogsService.createActivityLogs(new ActivityLogsEntryDto(EntityType.SALE,
                sale.getId(), TypeAction.SALE_CANCEL));


        itemsForSaleRepository.delete(sale);
    }

    @CachePut(
            value = "Info_Of_Products_Sale",
            key = "'Info_Of_Products_Sale_user_' + T(com.example.PDV" +
                    ".EmployeeCore.EmployeeService).currentEmployeeId()"
    )
    public InfoOfProductsSaleDto toWriteInfosInDataRedisOfProductsSale (InfoOfProductsSaleDto info) {
        return info;
    }

    @Cacheable(
            value = "Info_Of_Products_Sale",
            key = "'Info_Of_Products_Sale_user_' + T(com.example.PDV" +
                    ".EmployeeCore.EmployeeService).currentEmployeeId()",
            unless = "#result == null"
    )
    public InfoOfProductsSaleDto readInfosInDataRedisOfProductsSale () {
        return null;
    }

    @CacheEvict(
            value = "Info_Of_Products_Sale",
            key = "'Info_Of_Products_Sale_user_' + T(com.example.PDV" +
                    ".EmployeeCore.EmployeeService).currentEmployeeId()"
    )
    public void evictCacheInfosInDataRedisOfProductsSale () {
        // só para evict, nada dentro
    }

}
