package com.ssm.demo.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

@Component
public class StringToByteArrayConverter implements Converter<String, byte[]> {

    @Override
    public byte[] convert(String source) {
        return source.getBytes(StandardCharsets.UTF_8);
    }
}

