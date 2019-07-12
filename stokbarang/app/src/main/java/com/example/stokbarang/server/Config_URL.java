package com.example.stokbarang.server;

//This class is for storing all URLs as a model of URLs

public class Config_URL {
    public static String base_URL = "http://172.32.1.191:8000";
    public static String login = base_URL + "/login";
    public static String register = base_URL + "/registrasi";

    public static String input                = base_URL + "/inputbarang";
    public static String dataBarang           = base_URL + "/dataBarang";
    public static String update               = base_URL + "/updatebarang";
    public static String hapus                = base_URL + "/hapusbarang";
    public static String cariBarang           = base_URL + "/cari";


}
