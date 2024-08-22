package com.challenge.gft.utils;

public class DocumentUtils {
    public static String normalizeDocument(String document) {
        if (document == null) {
            return null;
        }

        return document.replaceAll("\\D", "");
    }
}
