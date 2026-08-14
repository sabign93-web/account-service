package com.banking.accountservice.util;

import org.springframework.stereotype.Component;

import java.security.SecureRandom;

@Component
public class IbanGenerator {

    private static final String COUNTRY_CODE = "FI";
    private static final int BBAN_LENGTH = 14;
    private static final SecureRandom RANDOM = new SecureRandom();

    public String generateIban() {

        String bban = generateBban();

        String checkDigits = calculateCheckDigits(
                COUNTRY_CODE,
                bban
        );

        return COUNTRY_CODE + checkDigits + bban;
    }

    private String generateBban() {

        StringBuilder bban = new StringBuilder();

        for (int i = 0; i < BBAN_LENGTH; i++) {
            bban.append(RANDOM.nextInt(10));
        }

        return bban.toString();
    }

    private String calculateCheckDigits(
            String countryCode,
            String bban) {

        String rearranged =
                bban
                        + convertLettersToNumbers(countryCode)
                        + "00";

        int remainder = mod97(rearranged);

        int checkDigits = 98 - remainder;

        return String.format("%02d", checkDigits);
    }

    private String convertLettersToNumbers(String letters) {

        StringBuilder result = new StringBuilder();

        for (char c : letters.toCharArray()) {
            result.append(c - 'A' + 10);
        }

        return result.toString();
    }

    private int mod97(String number) {

        int remainder = 0;

        for (char digit : number.toCharArray()) {

            remainder =
                    (remainder * 10 + (digit - '0')) % 97;
        }

        return remainder;
    }
}