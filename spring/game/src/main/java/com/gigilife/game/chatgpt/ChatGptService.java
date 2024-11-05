package com.gigilife.game.chatgpt;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
@ComponentScan("com.gigilife.game.chatgpt")
public class ChatGptService {

    @Qualifier("GptTemplate")
    @Autowired
    private RestTemplate restTemplate;


    public ChatGptResponse chat(ChatGptRequest request) {

        return restTemplate.postForObject("https://api.openai.com/v1/chat/completions", request, ChatGptResponse.class);

    }
}
