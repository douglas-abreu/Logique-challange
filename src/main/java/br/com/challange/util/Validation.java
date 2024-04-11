package br.com.challange.util;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

public class Validation {


    public static boolean isValidDate(String date){
        try {
            if(date.contains(" "))
                LocalDateTime.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            else
                LocalDate.parse(date, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public static boolean isValidTime(String time){
        try {
            LocalTime.parse(time, DateTimeFormatter.ofPattern("HH:mm:ss"));
        } catch (Exception e) {
            return false;
        }
        return true;
    }


    public static boolean isEmptyOrNull(Object obj){
        if(obj == null) return true;
        String objType = obj.getClass().getTypeName();
        if(objType.equals("java.lang.String"))  return String.valueOf(obj).isEmpty();
        return false;
    }


    public static String isEmptyFields(Object[][] args){
        String msgErr = "";
        for (int i = 0; i < args.length; i++) {
            if(isEmptyOrNull(args[i][0])){
                msgErr = String.format("Campo '%s' deve ser preenchido", args[i][1]);
                return msgErr;
            }
        }
        return msgErr;
    }

}
