package br.com.challange.util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FormatUtil {
    public static String dateTimeNow(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        LocalDateTime dateTimeNow = LocalDateTime.now();
        return dtf.format(dateTimeNow);
    }

    public static String dateNow(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDateTime dateTimeNow = LocalDateTime.now();
        return dtf.format(dateTimeNow);
    }

    public static String timeNow(){
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("HH:mm:ss");
        LocalDateTime dateTimeNow = LocalDateTime.now();
        return dtf.format(dateTimeNow);
    }
}
