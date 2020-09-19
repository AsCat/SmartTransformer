package com.ascat;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class StringUtils {

    static String regexMiddleStr(String src, String beg, String end) {
        String regex = beg + "(.*)" + end;
        System.out.println(regex);
        Pattern p = Pattern.compile(regex);
        Matcher m = p.matcher(src);
        if (m.find()) {
            return m.group(1);
        }

        return null;
    }

    static String middle(String src, String beg, String end){
        int left = src.indexOf(beg) + beg.length();
        int right  = src.lastIndexOf(end);
        return src.substring(left, right);
    }

    static String middle(String src, String beg, String[] ends){
        int left = src.indexOf(beg) + beg.length();
        String end = "";
        for(String s: ends){
            if(src.contains(s)){
                end = s;
            };
        }
        int right = src.lastIndexOf(end);
        return src.substring(left, right);
    }

    static String left(String src, String beg){
        int left = src.indexOf(beg);
        return src.substring(0, left);
    }

    static String right(String src, String end){
        int left = src.indexOf(end) + end.length();
        return src.substring(left, src.length());
    }
}
