package br.com.challange.util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class FormatUtil {

    public static Date getDateFromLocalDateTime(){
        LocalDateTime dateTimeNow = LocalDateTime.now();
        return Date.from(dateTimeNow.atZone(ZoneId.systemDefault()).toInstant());
    }

    public static Date getOnlyDate() {
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        try {
            return formatter.parse(formatter.format(new Date()));
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

}
