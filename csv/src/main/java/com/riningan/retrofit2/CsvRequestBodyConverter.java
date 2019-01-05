package com.riningan.retrofit2;

import java.io.IOException;

import okhttp3.MediaType;
import okhttp3.RequestBody;
import retrofit2.Converter;


public class CsvRequestBodyConverter<T> implements Converter<T, RequestBody> {
    private static final MediaType MEDIA_TYPE = MediaType.parse("text/csv; charset=UTF-8");


    CsvRequestBodyConverter() {
    }


    @Override
    public RequestBody convert(T value) throws IOException {
        StringBuilder csv;
        if (value instanceof Iterable) {
            Iterable iterable = (Iterable) value;
            csv = null;
            for (Object item : iterable) {
                if (csv == null) {
                    csv = new StringBuilder(Utils.getTitles(item));
                }
                try {
                    csv.append("\n").append(Utils.getRow(item));
                } catch (IllegalAccessException e) {
                    throw new IOException("Can't cast to csv: " + value.toString());
                }
            }
            if (csv == null) {
                throw new IOException("Can't cast to csv: " + value.toString());
            }
        } else {
            csv = new StringBuilder(Utils.getTitles(value));
            try {
                csv.append("\n").append(Utils.getRow(value));
            } catch (IllegalAccessException e) {
                throw new IOException("Can't cast to csv: " + value.toString());
            }
        }
        return RequestBody.create(MEDIA_TYPE, csv.toString());
    }
}