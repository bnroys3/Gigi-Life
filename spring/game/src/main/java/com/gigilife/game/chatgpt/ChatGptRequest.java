package com.gigilife.game.chatgpt;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ChatGptRequest {
    private List<ChatGptMessage> messages;
    final String model = "gpt-3.5-turbo";
    private int max_tokens;
    private double temperature;

    public ChatGptRequest(){
        this.messages = new ArrayList<>();
        this.temperature = 1;
        this.max_tokens = 100;
    }

    public void addMessage(String role, String content) {
        ChatGptMessage chatGptMessage = new ChatGptMessage(role, content);
        messages.add(chatGptMessage);
    }

}
