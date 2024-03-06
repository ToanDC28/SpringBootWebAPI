package com.team2.trainingprogramrepo.config;

import com.fasterxml.jackson.databind.util.StdConverter;
import java.sql.Date;
import java.text.SimpleDateFormat;


public class SqlDateConverter extends StdConverter<String, Date> {
    @Override
    public Date convert(String value) {
        try {
            SimpleDateFormat inputFormat = new SimpleDateFormat("yyyy/MM/dd");
            SimpleDateFormat outputFormat = new SimpleDateFormat("yyyy-MM-dd");
            java.util.Date parsedDate = inputFormat.parse(value);
            String formattedDate = outputFormat.format(parsedDate);
            return Date.valueOf(formattedDate);
        } catch (Exception e) {
            return null;
        }
    }
}