package com.example.PDV.CustomerCore;

import com.example.PDV.CustomerCore.Dtos.CustomerEntryDto;
import com.example.PDV.CustomerCore.Dtos.InfosCustomerDto;
import com.example.PDV.CustomerCore.Enums.TypeCustomer;
import com.example.PDV.Utils.FormattingPersonalDataToDisplay;
import jakarta.persistence.*;

@Entity
@Table(name = "customers")
public class CustomerEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "cpf")
    private String cpf;

    @Column(name = "cnpj")
    private String cnpj;

    @Column(name = "phone_number")
    private String phoneNumber;

    @Column(name = "type_customer")
    @Enumerated(EnumType.STRING)
    private TypeCustomer typeCustomer;

    public CustomerEntity() {

    }

    public CustomerEntity(String name, String phoneNumber) {

        if (name != null && !name.trim().isEmpty()
                && phoneNumber != null && !phoneNumber.trim().isEmpty()) {
            this.name = name;
            this.phoneNumber = phoneNumber;
        }
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {

        if(name != null && !name.trim().isEmpty()){this.name = name;}
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        if(cpf != null && !cpf.trim().isEmpty()){this.cpf = cpf;}
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        if(cnpj != null && !cnpj.trim().isEmpty()){this.cnpj = cnpj;}
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        if(phoneNumber != null && !phoneNumber.trim().isEmpty()){this.phoneNumber =
                phoneNumber;}
    }

    public TypeCustomer getTypeCustomer() {
        return typeCustomer;
    }

    public void setTypeCustomer(TypeCustomer typeCustomer) {
        this.typeCustomer = typeCustomer;
    }

    public void endRegister(CustomerEntryDto entry) {

        if (entry.getCpf() != null && !entry.getCpf().trim().isEmpty()) {

            this.cpf = entry.getCpf();
            this.typeCustomer = TypeCustomer.PF;

        } else if (entry.getCnpj() != null && !entry.getCnpj().trim().isEmpty()) {

            this.cnpj = entry.getCnpj();
            this.typeCustomer = TypeCustomer.PJ;

        }
    }

    public void updateCustomer (CustomerEntryDto entry) {

        setName(entry.getName());
        setCpf(entry.getCpf());
        setCnpj(entry.getCnpj());
        setPhoneNumber(entry.getPhoneNumber());
    }

    public InfosCustomerDto seeInfosCustomer () {

        FormattingPersonalDataToDisplay formatter =
                new FormattingPersonalDataToDisplay();

        return new InfosCustomerDto(getName(),
                formatter.formattingCPF(getCpf()),
                formatter.formattingCNPJ(getCnpj()),
                getPhoneNumber());
    }
}
