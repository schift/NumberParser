package com.surowka;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Optional;

/*
   @author Lech Surowka
   @version 1.1
 */
public class NumberParser {
    private Map<String, Integer> countryCodes = new HashMap<>();
    private final String PLUS_SIGN = "+";
    private final String ERR_MISSING_COUNTRY_CODE = "couldn't find country code";
    private final String ERR_EMPTY_PHONE_NR = "phone number is empty";

    public NumberParser(Map<String, Integer> callingCodes) {
        countryCodes = Optional.ofNullable(callingCodes).orElse(new HashMap<String, Integer>());
    }

    public String parse(String dialledNumber, String userNumber) {
        StringBuilder phoneNumberInternationalFormat = new StringBuilder();
        validateInputData(dialledNumber, userNumber);

        String dialledNumberUnified = removeAllNonDigitCharsInPhoneNumberAndZeroPrefix(dialledNumber.toCharArray());
        String userNumberUnified = removeAllNonDigitCharsInPhoneNumberAndZeroPrefix(userNumber.toCharArray());
        String userNumberValidPrefix = "";

        if (dialledNumberUnified.length() == 0) throw new IllegalArgumentException(ERR_EMPTY_PHONE_NR);

        for (Integer code : countryCodes.values()) {
            userNumberValidPrefix = userNumberUnified.substring(0, (int) (Math.log10(code) + 1));
            if (userNumberValidPrefix.equals(code.toString())) {
                phoneNumberInternationalFormat.append(dialledNumberUnified);
                phoneNumberInternationalFormat.insert(0, PLUS_SIGN);
                phoneNumberInternationalFormat.insert(1, code);
                return phoneNumberInternationalFormat.toString();
            }
        }
        throw new NoSuchElementException(ERR_MISSING_COUNTRY_CODE);
    }

    private void validateInputData(String iDialledNumber, String iUserNumber) {
        Optional.ofNullable(iDialledNumber).orElseThrow(IllegalArgumentException::new);
        Optional.ofNullable(iUserNumber).orElseThrow(IllegalArgumentException::new);
        if (!iUserNumber.startsWith("+")) throw new IllegalArgumentException(ERR_MISSING_COUNTRY_CODE);
    }

    private String removeAllNonDigitCharsInPhoneNumberAndZeroPrefix(char[] inputPhoneNumber) {
        int index = 0;
        StringBuilder outputPhoneNumber = new StringBuilder();

        while (inputPhoneNumber.length != index) {
            char currentCh = inputPhoneNumber[index];
            if (Character.isDigit(currentCh) && ((currentCh != '0' && index == 0) || (index > 0)))
                outputPhoneNumber.append(currentCh);
            index++;
        }
        return outputPhoneNumber.toString();
    }
}
