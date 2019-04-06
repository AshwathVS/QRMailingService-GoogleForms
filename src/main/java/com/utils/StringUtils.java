package com.utils;

import org.apache.commons.lang3.RandomStringUtils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    private static final int RANDOM_STRING_LENGTH = 15;

    private static final boolean useLetters = true;

    private static final boolean useNumbers = false;

    private static final String EMAIL_ID_REGEX = "(?:[a-z0-9!#$%&'*+/=?^_`{|}~-]+(?:\\.[a-z0-9!#$%&'*+/=?^_`{|}~-]+)*|\"(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21\\x23-\\x5b\\x5d-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])*\")@(?:(?:[a-z0-9](?:[a-z0-9-]*[a-z0-9])?\\.)+[a-z0-9](?:[a-z0-9-]*[a-z0-9])?|\\[(?:(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?)\\.){3}(?:25[0-5]|2[0-4][0-9]|[01]?[0-9][0-9]?|[a-z0-9-]*[a-z0-9]:(?:[\\x01-\\x08\\x0b\\x0c\\x0e-\\x1f\\x21-\\x5a\\x53-\\x7f]|\\\\[\\x01-\\x09\\x0b\\x0c\\x0e-\\x7f])+)\\])";

    private static final String GMAIL_ID_REGEX = "^[\\w.+\\-]+@gmail\\.com$\n";

    public static String getRandomString() {
        return RandomStringUtils.random(RANDOM_STRING_LENGTH, useLetters, useNumbers);
    }

    public static boolean isValidMail(String email, boolean isGmail) {
        Pattern pattern;
        if(isGmail) {
            pattern = Pattern.compile(GMAIL_ID_REGEX);
        }
        else {
            pattern = Pattern.compile(EMAIL_ID_REGEX);
        }
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
