package org.example.util;

import java.time.LocalDate;
import java.time.Period;

public class DateUtils {

    public static boolean isOver15YearsOld(LocalDate birthDate) {
        Period period = Period.between(birthDate, LocalDate.now());
        return period.getYears() > 15;
    }
}
