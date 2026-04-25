package com.example.PDV.SaleCore;

import com.example.PDV.Agreement.AgreementEntity;
import com.example.PDV.Agreement.AgreementRepository;
import com.example.PDV.Agreement.Exceptions.AgreementNotFound;
import com.example.PDV.BoxCore.BoxEntity;
import com.example.PDV.BoxCore.BoxRepository;
import com.example.PDV.BoxCore.BoxService;
import com.example.PDV.Exceptions.*;
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
import com.example.PDV.SaleCore.SaleDtos.*;
import com.example.PDV.SaleCore.SaleEnums.Installments;
import com.example.PDV.SaleCore.SaleEnums.KindOfPayment;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.*;

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
    public void makeSale(SaleEntryDto saleEntry) {

        if (saleEntry.getExternalId() == null || saleEntry.getExternalId().trim().isEmpty()) {
            saleEntry.setExternalId(UUID.randomUUID().toString());
        }

        if (saleRepository.existsByExternalId(saleEntry.getExternalId())) {
            throw new DuplicateSaleException();
        }

        AgreementEntity agreement = agreementRepository.findById(saleEntry.getAgreementId())
                .orElseThrow(AgreementNotFound::new);

        BoxEntity box = boxRepository.findById(saleEntry.getBoxId())
                .orElseThrow(BoxNotFound::new);

        SaleEntity sale = new SaleEntity(box, agreement);

        Set<Integer> uniqueIds = new HashSet<>(saleEntry.getItems().getProducts());

        List<ProductEntity> products = productRepository
                .findAllById(uniqueIds);

        if (products.size() != uniqueIds.size()) {
            throw  new ProductNotFound();
        }

        Map<ProductEntity, Integer> productsAndQuantity =
                createMapProductQuantity(products,
                        saleEntry.getItems().getProducts());

        CreateItemsForSaleOutDto itemsForSale = createListOfItemsForSale(productsAndQuantity,
                sale);

        BigDecimal percentageToBeAdded = itemsForSale.valueTotal()
                .multiply(agreement.getPercentage())
                .divide(new BigDecimal("100"), 2, RoundingMode.HALF_UP);

        BigDecimal finalValue = itemsForSale.valueTotal().add(percentageToBeAdded);

        CreatePaymentSales paymentsSales =
                createPaymentOfSales(saleEntry.getPayment().getInfoPayment()
                        , itemsForSale.valueTotal(), percentageToBeAdded,
                        sale);


        if (paymentsSales.totalPrincePayment() == null || finalValue == null
                || paymentsSales.totalPrincePayment().compareTo(finalValue) != 0) {

            System.out.println("Valor total Payment: " + paymentsSales.totalPrincePayment() +
                    " valor final:" +
                    " " + finalValue);
            throw new PriceIsNotEqual();
        }

        sale.setExternalId(saleEntry.getExternalId());
        sale.setQuantity(itemsForSale.quantity());
        sale.setTotalValueSale(finalValue);
        saleRepository.save(sale);

        paymentOfSaleRepository.saveAll(paymentsSales.payments());

        box.setTotalValue(finalValue);
        boxRepository.save(box);

        boxService.evictCacheBoxOpenedForCurrentUser();

        itemsForSaleRepository.saveAll(itemsForSale.sales());

        productService.evichProducts();

        activityLogsService.createActivityLogs(new ActivityLogsEntryDto(EntityType.SALE,
                sale.getId(), TypeAction.SALE));

    }

    public void cancelSele(Integer saleId) {

        ItemsForSaleEntity sale = itemsForSaleRepository.findById(saleId)
                .orElseThrow(SaleNotFound::new);

        itemsForSaleRepository.delete(sale);

        activityLogsService.createActivityLogs(new ActivityLogsEntryDto(EntityType.SALE,
                sale.getId(), TypeAction.SALE_CANCEL));
    }

    public List<ItemsForSaleEntity> createItemsForSale(ProductEntity product,
                                                SaleEntity sale,
                                                Integer quantity) {

        List<ItemsForSaleEntity> items = new ArrayList<>();

        for (int i = 0; i < quantity; i++) {
            ItemsForSaleEntity item = new ItemsForSaleEntity(product, sale);

            items.add(item);
        }

        return items;
    }

    private CreateItemsForSaleOutDto createListOfItemsForSale(Map<ProductEntity, Integer> products,
                                                             SaleEntity sale) {

        List<ItemsForSaleEntity> sales = new ArrayList<>();

        BigDecimal valueTotal = BigDecimal.ZERO;

        Integer quantity = 0;

        for (Map.Entry<ProductEntity, Integer> product : products.entrySet()) {

            List<ItemsForSaleEntity> items = createItemsForSale(product.getKey(),
                    sale, product.getValue());

            Integer quantityProduct = product.getValue();

            BigDecimal valueFinalProduct = product.getKey().getValue()
                    .multiply(new BigDecimal(quantityProduct));

            valueTotal = valueTotal.add(valueFinalProduct);

            quantity += quantityProduct;

            if(!product.getKey().decreaseQuantity(quantityProduct))
                throw new RuntimeException("Quantidade de Invalida.");

            sales.addAll(items);
        }

        return new CreateItemsForSaleOutDto(sales, valueTotal, quantity);
    }

    private CreatePaymentSales createPaymentOfSales(Map<KindOfPayment,
                                                            ValueAndInstallments> paymentsOfSales,
                                                    BigDecimal valueTotal,
                                                    BigDecimal percentageToBeAdded,
                                                    SaleEntity sale) {

        List<PaymentOfSaleEntity> payments = new ArrayList<>();

        BigDecimal totalPricePayment = BigDecimal.ZERO;

        for (Map.Entry<KindOfPayment, ValueAndInstallments> entry :
                paymentsOfSales.entrySet()
                ) {

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

        return new CreatePaymentSales(payments, totalPricePayment);
    }

    private Map<ProductEntity, Integer> createMapProductQuantity(List<ProductEntity> products,
                                                                 List<Integer> ids) {

        Map<Integer, ProductEntity> idAndProduct = new HashMap<>();

        Map<ProductEntity, Integer> productsAndQuantity = new HashMap<>();

        for (ProductEntity product : products) {

            idAndProduct.put(product.getId(), product);

        }

        for (Integer id : ids) {

            ProductEntity product = idAndProduct.get(id);

            if (product == null) {
                throw  new ProductNotFound();
            }

            productsAndQuantity.merge(product, 1, Integer::sum);

        }

        return productsAndQuantity;
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