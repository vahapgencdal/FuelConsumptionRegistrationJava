package com.fuel.consumption.config;

import com.fuel.consumption.intercepter.AddQueryParameterInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.util.CollectionUtils;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.List;

@Configuration
public class RestConfig {

    @Autowired
    private AddQueryParameterInterceptor addQueryParameterInterceptor;


    @Bean
    public RestTemplate ipStackRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();

        List<ClientHttpRequestInterceptor> interceptors = restTemplate.getInterceptors();
        if (CollectionUtils.isEmpty(interceptors)) {
            interceptors = new ArrayList<>();
        }
        interceptors.add(addQueryParameterInterceptor);
        restTemplate.setInterceptors(interceptors);
        return restTemplate;
    }


}
