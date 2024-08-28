package com.personel.PersonelTakip.entity;


public enum Duty {
    BEKCI("Bekçi"),
    KASIYER("Kasiyer"),
    SATIS("Satış Sorumlusu"),
    MUHASEBE("Muhasebe"),
    SEF("Şef")


    ;


    private String name;

    Duty(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }


}
