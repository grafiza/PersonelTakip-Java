package com.personel.PersonelTakip.payload.helper;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

public class LeaveCalculator {
    private static final List<LocalDate> HOLIDAYS = Arrays.asList(
            LocalDate.of(2024, 8, 30), // 30 Ağustos Zafer Bayramı
            LocalDate.of(2024, 10, 29), // 29 Ekim Cumhuriyet Bayramı
            LocalDate.of(2024, 4, 10), // Ramazan Bayramı 1. Gün (örnek tarih)
            LocalDate.of(2024, 4, 11), // Ramazan Bayramı 2. Gün (örnek tarih)
            LocalDate.of(2024, 4, 12), // Ramazan Bayramı 3. Gün (örnek tarih)
            LocalDate.of(2024, 6, 15), // Kurban Bayramı 1. Gün (örnek tarih)
            LocalDate.of(2024, 6, 16), // Kurban Bayramı 2. Gün (örnek tarih)
            LocalDate.of(2024, 6, 17), // Kurban Bayramı 3. Gün (örnek tarih)
            LocalDate.of(2024, 6, 18)  // Kurban Bayramı 4. Gün (örnek tarih)
    );
}
