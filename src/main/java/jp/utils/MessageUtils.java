package jp.utils;

import java.text.MessageFormat;

public class MessageUtils {

    public static String message(String msg, String... param) {

        String result = MessageFormat.format(msg, param);
        return result;
    }
}
