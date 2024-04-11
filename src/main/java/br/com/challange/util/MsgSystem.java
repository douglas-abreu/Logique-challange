package br.com.challange.util;

public class MsgSystem {

    public static String errDate(){
        return "Formato de 'Data' incorreto";
    }

    public static String errDateTime(){
        return "Formato de 'Data Hora' incorreto";
    }

    public static String errTime(){
        return "Formato de 'Hora' incorreto";
    }

    public static String sucCreate(String objName){
        return String.format("%s criado(a) com sucesso", objName);
    }

    public static String sucUpdate(String objName){
        return String.format("%s atualizado(a) com sucesso", objName);
    }

    public static String sucGet(String objName){
        return String.format("%s encontrado(a) com sucesso", objName);
    }

    public static String errGet(String objName){
        return String.format("%s não encontrado(a) com o id informado", objName);
    }

    public static String errLogin() { return "Usuário ou senha inválido"; }


}
