package com.ssm.demo.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.Base64;

@Component
public class ByteArrayToStringConverter implements Converter<byte[], String> {

    @Override
    public String convert(byte[] source) {


        return Base64.getEncoder().encodeToString(source);
    }

}
