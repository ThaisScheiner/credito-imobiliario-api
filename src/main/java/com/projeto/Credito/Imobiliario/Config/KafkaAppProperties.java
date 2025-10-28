package com.projeto.Credito.Imobiliario.Config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "app.kafka") // Mapeia tudo que começa com "app.kafka" no .yml
public class KafkaAppProperties {

    private String topicName;

    // Getters e Setters são necessários para o @ConfigurationProperties funcionar
    public String getTopicName() {
        return topicName;
    }

    public void setTopicName(String topicName) {
        this.topicName = topicName;
    }
}
