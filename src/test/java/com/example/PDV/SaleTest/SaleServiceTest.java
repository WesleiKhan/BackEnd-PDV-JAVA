package com.example.PDV.SaleTest;

import com.example.PDV.Agreement.AgreementEntity;
import com.example.PDV.Agreement.AgreementRepository;
import com.example.PDV.Agreement.Dtos.AgreementEntryDto;
import com.example.PDV.Agreement.Exceptions.AgreementNotFound;
import com.example.PDV.BoxCore.BoxEntity;
import com.example.PDV.BoxCore.BoxRepository;
import com.example.PDV.BoxCore.BoxService;
import com.example.PDV.EmployeeCore.EmployeeDtos.EmployeeEntryDto;
import com.example.PDV.EmployeeCore.EmployeeEntity;
import com.example.PDV.EmployeeCore.Enums.Roles;
import com.example.PDV.Exceptions.BoxNotFound;
import com.example.PDV.Exceptions.DuplicateSaleException;
import com.example.PDV.Exceptions.PriceIsNotEqual;
import com.example.PDV.Exceptions.ProductNotFound;
import com.example.PDV.LogsCore.ActivityLogsService;
import com.example.PDV.ProductsCore.ProductEntity;
import com.example.PDV.ProductsCore.ProductRepository;
import com.example.PDV.ProductsCore.ProductService;
import com.example.PDV.SaleCore.Entities.SaleEntity;
import com.example.PDV.SaleCore.Repositories.ItemsForSaleRepository;
import com.example.PDV.SaleCore.Repositories.PaymentOfSaleRepository;
import com.example.PDV.SaleCore.Repositories.SaleRepository;
import com.example.PDV.SaleCore.SaleDtos.InfoPaymentDto;
import com.example.PDV.SaleCore.SaleDtos.ItemsForSaleDto;
import com.example.PDV.SaleCore.SaleDtos.SaleEntryDto;
import com.example.PDV.SaleCore.SaleDtos.ValueAndInstallments;
import com.example.PDV.SaleCore.SaleEnums.Installments;
import com.example.PDV.SaleCore.SaleEnums.KindOfPayment;
import com.example.PDV.SaleCore.SaleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.*;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.*;

@ExtendWith(MockitoExtension.class)
public class SaleServiceTest {

    @Mock
    ItemsForSaleRepository itemsForSaleRepository;

    @Mock
    SaleRepository saleRepository;

    @Mock
    PaymentOfSaleRepository paymentOfSaleRepository;

    @Mock
    AgreementRepository agreementRepository;

    @Mock
    BoxRepository boxRepository;

    @Mock
    BoxService boxService;

    @Mock
    ProductRepository productRepository;

    @Mock
    ProductService productService;

    @Mock
    ActivityLogsService activityLogsService;

    @InjectMocks
    SaleService saleService;

    private SaleEntryDto dtoEntry;

    private AgreementEntity agreement;

    private ProductEntity product1;

    private ProductEntity product2;

    private EmployeeEntity operator;

    private List<Integer> products;

    private Set<Integer> uniqsIds;

    private BoxEntity box;

    private Map<ProductEntity, Integer> productAndQuantity;

    private SaleEntity sale;

    @BeforeEach
    void setUp() {

        product1 = new ProductEntity();
        product1.setId(5);
        product1.setValue(new BigDecimal("20.99"));
        product1.setQuantity(10);

        product2 = new ProductEntity();
        product2.setId(6);
        product2.setValue(new BigDecimal("31.90"));
        product2.setQuantity(10);

        products = List.of(product1.getId(),
                product1.getId(), product1.getId(), product1.getId(),
                product1.getId() , product2.getId(), product2.getId());

        ItemsForSaleDto items = new ItemsForSaleDto();
        items.setProducts(products);

        ValueAndInstallments valueAndInstallments1 =
                new ValueAndInstallments();
        valueAndInstallments1.setValuePayment(new BigDecimal("68.75"));

        ValueAndInstallments valueAndInstallments2 =
                new ValueAndInstallments();
        valueAndInstallments2.setValuePayment(new BigDecimal("100"));
        valueAndInstallments2.setInstallments(Installments.DUAS);

        Map<KindOfPayment, ValueAndInstallments> payment = new HashMap<>();
        payment.put(KindOfPayment.DINHEIRO, valueAndInstallments1);
        payment.put(KindOfPayment.CREDITO, valueAndInstallments2);

        InfoPaymentDto payments = new InfoPaymentDto();
        payments.setInfoPayment(payment);

        AgreementEntryDto entryAgreement = new AgreementEntryDto();
        entryAgreement.setPercentage(new BigDecimal(30));

        agreement = new AgreementEntity(entryAgreement);
        agreement.setId(2);

        EmployeeEntryDto entryEmployee = new EmployeeEntryDto();
        entryEmployee.setName("operator-test");
        entryEmployee.setPassword("123");
        entryEmployee.setRole(Roles.ROLE_OPERATOR_BOX);

        operator = new EmployeeEntity(entryEmployee);
        operator.setId(1);

        box = new BoxEntity(operator);
        box.setId(1);

        dtoEntry = new SaleEntryDto();
        dtoEntry.setExternalId("uuid-test");
        dtoEntry.setBoxId(box.getId());
        dtoEntry.setAgreementId(agreement.getId());
        dtoEntry.setItems(items);
        dtoEntry.setPayment(payments);

        uniqsIds = new HashSet<>(dtoEntry.getItems().getProducts());

        sale = new SaleEntity(box, agreement);

    }

    @Test
    void shouldMakeSale() {

        when(saleRepository.existsByExternalId(dtoEntry.getExternalId()))
                .thenReturn(false);

        when(agreementRepository.findById(dtoEntry.getAgreementId()))
                .thenReturn(Optional.of(agreement));

        when(boxRepository.findById(dtoEntry.getBoxId())).thenReturn(Optional.of(box));

        when(productRepository.findAllById(uniqsIds)).thenReturn(List.of(product1, product2));

        saleService.makeSale(dtoEntry);

        // validações
        verify(saleRepository).save(argThat(sale ->
                sale.getExternalId().equals("uuid-test") &&
                        sale.getQuantity() == 7 &&
                        sale.getTotalValueSale().compareTo(new BigDecimal("219.38")) == 0
        ));

        verify(paymentOfSaleRepository).saveAll(any());
        verify(itemsForSaleRepository).saveAll(argThat(items -> ((List<?>) items).size() == 7));
        verify(boxRepository).save(any());

        verify(boxService).evictCacheBoxOpenedForCurrentUser();
        verify(productService).evichProducts();
        verify(activityLogsService).createActivityLogs(any());

    }

    @Test
    void shouldMakeSaleWhenExternalIdEntryEmptyOrNull() {

        dtoEntry.setExternalId(" ");

        when(saleRepository.existsByExternalId(anyString()))
                .thenReturn(false);

        when(agreementRepository.findById(dtoEntry.getAgreementId()))
                .thenReturn(Optional.of(agreement));

        when(boxRepository.findById(dtoEntry.getBoxId())).thenReturn(Optional.of(box));

        when(productRepository.findAllById(uniqsIds)).thenReturn(List.of(product1, product2));

        saleService.makeSale(dtoEntry);

        // validações
        verify(saleRepository).save(argThat(sale ->
                !sale.getExternalId().trim().isEmpty() &&
                        sale.getQuantity() == 7 &&
                        sale.getTotalValueSale().compareTo(new BigDecimal("219.38")) == 0
        ));

        verify(paymentOfSaleRepository).saveAll(any());
        verify(itemsForSaleRepository).saveAll(argThat(items -> ((List<?>) items).size() == 7));
        verify(boxRepository).save(any());

        verify(boxService).evictCacheBoxOpenedForCurrentUser();
        verify(productService).evichProducts();
        verify(activityLogsService).createActivityLogs(any());
    }

    @Test
    void shouldThrowDuplicateSaleExceptionWhenExternalIdExists() {

        when(saleRepository.existsByExternalId(dtoEntry.getExternalId()))
                .thenReturn(true);

        assertThrows(DuplicateSaleException.class, () -> {
            saleService.makeSale(dtoEntry);
        });

        verify(paymentOfSaleRepository, never()).saveAll(any());
        verify(itemsForSaleRepository, never()).saveAll(any());
        verify(boxRepository, never()).save(any());
    }

    @Test
    void shouldThrowAgreementNotFound() {

        when(saleRepository.existsByExternalId(anyString()))
                .thenReturn(false);

        when(agreementRepository.findById(dtoEntry.getAgreementId()))
                .thenReturn(Optional.empty());

        assertThrows(AgreementNotFound.class, () -> {
            saleService.makeSale(dtoEntry);
        });

        verify(paymentOfSaleRepository, never()).saveAll(any());
        verify(itemsForSaleRepository, never()).saveAll(any());
        verify(boxRepository, never()).save(any());
    }

    @Test
    void shouldBoxNotFound() {

        when(saleRepository.existsByExternalId(anyString()))
                .thenReturn(false);

        when(agreementRepository.findById(dtoEntry.getAgreementId()))
                .thenReturn(Optional.of(agreement));

        when(boxRepository.findById(dtoEntry.getBoxId())).thenReturn(Optional.empty());

        assertThrows(BoxNotFound.class, () -> {
            saleService.makeSale(dtoEntry);
        });

        verify(paymentOfSaleRepository, never()).saveAll(any());
        verify(itemsForSaleRepository, never()).saveAll(any());
        verify(boxRepository, never()).save(any());
    }

    @Test
    void shouldProductNotFound() {

        when(saleRepository.existsByExternalId(anyString()))
                .thenReturn(false);

        when(agreementRepository.findById(dtoEntry.getAgreementId()))
                .thenReturn(Optional.of(agreement));

        when(boxRepository.findById(dtoEntry.getBoxId())).thenReturn(Optional.of(box));

        when(productRepository.findAllById(uniqsIds)).thenReturn(List.of(product1));

        assertThrows(ProductNotFound.class, () -> {
            saleService.makeSale(dtoEntry);
        });

        verify(paymentOfSaleRepository, never()).saveAll(any());
        verify(itemsForSaleRepository, never()).saveAll(any());
        verify(boxRepository, never()).save(any());
    }

    @Test
    void shouldThrowPriceIsNotEqual() {

        ValueAndInstallments wrongValue = new ValueAndInstallments();
        wrongValue.setValuePayment(new BigDecimal("50")); // errado

        ValueAndInstallments value2 = new ValueAndInstallments();
        value2.setValuePayment(new BigDecimal("100"));
        value2.setInstallments(Installments.DUAS);

        Map<KindOfPayment, ValueAndInstallments> payment = new HashMap<>();
        payment.put(KindOfPayment.DINHEIRO, wrongValue);
        payment.put(KindOfPayment.CREDITO, value2);

        InfoPaymentDto payments = new InfoPaymentDto();
        payments.setInfoPayment(payment);

        dtoEntry.setPayment(payments);

        when(saleRepository.existsByExternalId(anyString()))
                .thenReturn(false);

        when(agreementRepository.findById(dtoEntry.getAgreementId()))
                .thenReturn(Optional.of(agreement));

        when(boxRepository.findById(dtoEntry.getBoxId()))
                .thenReturn(Optional.of(box));

        when(productRepository.findAllById(uniqsIds))
                .thenReturn(List.of(product1, product2));

        assertThrows(PriceIsNotEqual.class, () -> {
            saleService.makeSale(dtoEntry);
        });

        verify(paymentOfSaleRepository, never()).saveAll(any());
        verify(itemsForSaleRepository, never()).saveAll(any());
        verify(boxRepository, never()).save(any());
    }
}
