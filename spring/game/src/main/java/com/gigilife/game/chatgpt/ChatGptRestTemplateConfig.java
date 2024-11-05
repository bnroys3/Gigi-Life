package com.gigilife.game.chatgpt;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

@Configuration
public class ChatGptRestTemplateConfig {
    
    @Value("#{environment.OPENAI_API_KEY}")
    private String openAiApiKey;

    @Bean
    @Qualifier("GptTemplate")
    RestTemplate openAiRestTemplate() {
        RestTemplate restTemplate = new RestTemplate();
        restTemplate.getInterceptors().add((request, body, execution) -> {
            request.getHeaders().add("Authorization", "Bearer " + openAiApiKey);
            return execution.execute(request, body);
        });
        return restTemplate;
    }
}
