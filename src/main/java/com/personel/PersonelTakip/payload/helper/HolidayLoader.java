package com.personel.PersonelTakip.payload.helper;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

public class HolidayLoader {

    private Set<LocalDate> holidays;

    public HolidayLoader(String filePath, int year) throws IOException {
        holidays = new HashSet<>();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode rootNode = mapper.readTree(Paths.get(filePath).toFile());
        JsonNode yearNode = rootNode.path(String.valueOf(year));

        for (JsonNode holiday : yearNode) {
            holidays.add(LocalDate.parse(holiday.get("date").asText()));
        }
    }

    public Set<LocalDate> getHolidays() {
        return holidays;
    }
}
