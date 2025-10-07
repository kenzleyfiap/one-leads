package com.luan.kenzley.one_leads.domain.validator;

public class CnpjValidator {

    public static boolean isValid(String cnpj) {
        if (cnpj == null || cnpj.length() != 14 || !cnpj.matches("\\d{14}")) {
            return false;
        }

        int[] pesos1 = {5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};
        int[] pesos2 = {6, 5, 4, 3, 2, 9, 8, 7, 6, 5, 4, 3, 2};

        String base = cnpj.substring(0, 12);
        String digitos = cnpj.substring(12);

        int soma1 = 0;
        for (int i = 0; i < pesos1.length; i++) {
            soma1 += Character.getNumericValue(base.charAt(i)) * pesos1[i];
        }
        int dv1 = soma1 % 11 < 2 ? 0 : 11 - (soma1 % 11);

        int soma2 = 0;
        for (int i = 0; i < pesos2.length; i++) {
            soma2 += Character.getNumericValue(base.charAt(i)) * pesos2[i];
        }
        soma2 += dv1 * pesos2[12];
        int dv2 = soma2 % 11 < 2 ? 0 : 11 - (soma2 % 11);

        return digitos.equals("" + dv1 + dv2);
    }
}
