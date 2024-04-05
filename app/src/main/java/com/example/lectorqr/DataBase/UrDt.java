package com.example.lectorqr.DataBase;

public class UrDt {

    private static String Nombre;
    private static String Apellido_P;
    private static String Apellido_M;
    private static String Num;
    private static String Area;
    private static String Correo;
    private static String Celular;
    private static String Remail;
    private static String Semail;
    private static String pswd;

    public static String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public static String getRemail() {
        return Remail;
    }

    public void setRemail(String remail) {
        Remail = remail;
    }

    public static String getSemail() {
        return Semail;
    }

    public void setSemail(String semail) {
        Semail = semail;
    }

    public static String getPswd() {
        return pswd;
    }

    public void setPswd(String pswd) {
        this.pswd = pswd;
    }

    public static String getApellido_P() {
        return Apellido_P;
    }

    public void setApellido_P(String apellido_P) {
        Apellido_P = apellido_P;
    }

    public static String getApellido_M() {
        return Apellido_M;
    }

    public void setApellido_M(String apellido_M) {
        Apellido_M = apellido_M;
    }

    public static String getNum() {
        return Num;
    }

        public void setNum(String num) {
        Num = num;
    }

    public static String getArea() {
        return Area;
    }

    public void setArea(String area) {
        Area = area;
    }

    public static String getCorreo() {
        return Correo;
    }

    public void setCorreo(String correo) {
        Correo = correo;
    }

    public static String getCelular() {
        return Celular;
    }

    public void setCelular(String celular) {
        Celular = celular;
    }
}
