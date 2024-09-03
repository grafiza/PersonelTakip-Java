package com.personel.PersonelTakip.entity;


public enum Duty {
    BEKCI("Bekçi"),
    KASIYER("Kasiyer"),
    SATIS("Satış Sorumlusu"),
    MUHASEBE("Muhasebe Personeli"),
    SEF("Şef"),
    DEPO("Depo Personeli"),
    TEMIZLIK("Temizlik Personeli"),
    ASCI("Aşçı"),
    SATINALMA("Satınalma Sorumlusu"),
    MUDUR("Müdür")



    ;


    private String name;

    Duty(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}
