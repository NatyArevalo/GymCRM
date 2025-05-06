package com.gymcrm.gymcrm.Configurations;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.gymcrm.gymcrm.DTO.TrainingBillingDTO;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jms.support.converter.MappingJackson2MessageConverter;
import org.springframework.jms.support.converter.MessageConverter;
import org.springframework.jms.support.converter.MessageType;

import java.util.Map;

@Configuration
public class JmsConfiguration {
    @Bean
    public MessageConverter jacksonJmsMessageConverter(ObjectMapper objectMapper) {
        MappingJackson2MessageConverter converter = new MappingJackson2MessageConverter();
        converter.setTargetType(MessageType.TEXT);
        converter.setTypeIdPropertyName("_typeId");
        converter.setTypeIdMappings(Map.of(
                "TrainingBillingDTO", TrainingBillingDTO.class
        ));
        converter.setObjectMapper(objectMapper);
        return converter;
    }
}
