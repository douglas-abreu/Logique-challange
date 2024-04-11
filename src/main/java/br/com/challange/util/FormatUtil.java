package br.com.challange.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

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

    public static Date getDateFromLocalDateTime(){
        LocalDateTime dateTimeNow = LocalDateTime.now();
        return Date.from(dateTimeNow.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date getOnlyDate() {
        LocalDateTime dateTimeNow = LocalDateTime.now();
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return formatter.parse(formatter.format(new Date()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
