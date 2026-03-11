package com.example.PDV.Utils;

public class FormattingPersonalDataToDisplay {

    public String formattingCPF(String cpf) {

        String cpfNull = "XXX.XXX.XXX-XX";

        if (cpf == null || cpf.trim().isEmpty()) {

            return cpfNull;
        }

        String formatToCPF = "(\\d{3})\\d{6}(\\d{2})";
        String cpfNotNull = "$1.XXX.XXX-XX";

        return cpf.replaceAll(formatToCPF, cpfNotNull);

    }

    public String formattingCNPJ(String cnpj) {

        String cnpjNull = "XX.XXX.XXX/XXXX-XX";

        if (cnpj == null || cnpj.trim().isEmpty()) {

            return cnpjNull;

        }

        String formatToCNPJ = "(\\d{2})\\d{6}(\\d{4})(\\d{2})";
        String cnpjNotNull = "XX.XXX.XXX/$2-$3";

        return cnpj.replaceAll(formatToCNPJ, cnpjNotNull);

    }
}
