package com.example.keycloakadminclient.config;

import feign.codec.Encoder;
import feign.form.FormEncoder;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.cloud.openfeign.support.SpringEncoder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignFormConfig {
  @Bean
  public Encoder feignFormEncoder(ObjectFactory<HttpMessageConverters> converters) {
    return new FormEncoder(new SpringEncoder(converters));
  }
}