package com.gigilife.game.chatgpt;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatGptResponse {

    private List<Choice> choices;


    @Getter
    @Setter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class Choice {

        private int index;
        private ChatGptMessage message;
        private String finish_reason;

    }
    

}


