package com.rslakra.springservices.thymeleaflayout.framework;

import java.util.regex.Pattern;

public enum TextUtils {
    
    INSTANCE;
    
    /**
     * @param source
     * @param target
     * @return
     */
    public static boolean containsIgnoreCase(String source, String target) {
        if (source == null || target == null) {
            return false;
        }
        
        Pattern pattern = Pattern.compile(Pattern.quote(target), Pattern.CASE_INSENSITIVE);
        return pattern.matcher(source).find();
    }
}
