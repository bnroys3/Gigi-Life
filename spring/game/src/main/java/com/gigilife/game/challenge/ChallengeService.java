package com.gigilife.game.challenge;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.gigilife.game.chatgpt.ChatGptRequest;
import com.gigilife.game.chatgpt.ChatGptResponse;
import com.gigilife.game.chatgpt.ChatGptService;
import com.gigilife.game.game.GameService;
import com.gigilife.game.player.PlayerService;

@Service
public class ChallengeService {

    @Autowired
    ChatGptService chatGptService;

    @Autowired
    PlayerService playerService;

    @Autowired
    GameService gameService;

    @Autowired
    ChallengeRepository challengeRepository;

    final String challengePrompt = "You create challenges. You will be prompted with a user profile and will create 1 easy challenge, 1 medium challenge, and 1 hard challenge for the user to complete within 1 week. And be creative! Respond in the format: easy: [1]. med: [2]. hard: [3]. Where [1] , [2], & [3] are replaced with the challenges, with [1] being the easiest and [3] being the hardest, with each challenge being less than 250 characters. Also, don't include any of the previous challenges, which will be provided - make them unique.";

    // returns map of easy: medium: hard: challenges
    public Map<String, String> getWeekly(Integer gameId){
        ChatGptRequest chatGptRequest = new ChatGptRequest();

        //determines randomness/creativity
        chatGptRequest.setTemperature(1);
        chatGptRequest.addMessage("system", challengePrompt);

        Integer playerId = gameService.findGameByGameId(gameId).getPlayerId();

        //create a message to chatgpt using the player's profile as well as the goals provided by the game admin
        String playerProfile = playerService.getPlayerProfile(playerId);
        String gameGoals = gameService.getGamePlayerGoals(gameId);
        String gptPrompt = playerProfile.substring(0,playerProfile.length()-1);
        gptPrompt += "Other's goals for them: " + gameGoals + "} ";
        gptPrompt += "Previous challenges: " + getPreviousChallenges(gameId);



        chatGptRequest.addMessage("user", gptPrompt);
        
        //receive the response from chatgpt
        ChatGptResponse chatGptResponse = chatGptService.chat(chatGptRequest);
        String messageContent = chatGptResponse.getChoices().get(0).getMessage().getContent();

        Map<String, String> challenges = new HashMap<>();

        String[] levels = {"easy", "med", "hard"};
        
        //iterate through the response to extract the challenges
        for(int i = levels.length-1; i >= 0; i--) {

            //extract the rightmost challenge from the response ("hard: ")
            int headerLength = levels[i].length() + 2;
            int challengeIndex = messageContent.indexOf(levels[i] + ": ") + headerLength;
            String challengeText = messageContent.substring(challengeIndex);

            //create the challenge entity to be stored in the repository
            ChallengeEntity challengeEntity = new ChallengeEntity();
            challengeEntity.setGameId(gameId);
            challengeEntity.setChallenge(challengeText);
            challengeEntity.setDifficulty(levels[i]);
            challengeEntity.setComplete(false);
            challengeEntity.setExpired(false);

            //set the expiration date to be one week from the current time
            Date utilDate = new Date();
            challengeEntity.setExpiryDate(new java.sql.Date(utilDate.getTime()));

            //save the entity
            challengeRepository.save(challengeEntity);
            
            //add the challenge to the hashmap with the difficulty level as the key
            challenges.put(levels[i], messageContent.substring(challengeIndex));


            //trim the message to remove the handled challenge
            messageContent = messageContent.substring(0,challengeIndex - headerLength).trim();

        }

        return challenges;
    }

    public Map<String, Object> getCurrentChallenges(Integer gameId) {

        Map<String, Object> currentChallenges = new HashMap<>();

        //find all challenges associated with the game
        List<ChallengeEntity> challenges = new ArrayList<ChallengeEntity>();
        challenges.addAll(challengeRepository.findByGameId(gameId));

        //remove any that are expired
        for(Iterator<ChallengeEntity> iterator = challenges.iterator(); iterator.hasNext(); ) {
            ChallengeEntity challenge = iterator.next();
            if (challenge.isExpired() == true) {
                iterator.remove();
            } else {
                //add the challenge to the map with its difficulty as the key and its details as the value
                currentChallenges.put(challenge.getDifficulty(), getChallengeDetails(challenge));
            }
        }

        return currentChallenges;

    }

    public Map<String, String> getChallengeDetails(ChallengeEntity challengeEntity) {

        Map<String, String> details = new HashMap<>();

        String status;
        if(challengeEntity.isComplete()) {
            status = "complete";
        } else {
            Date utilDate = new Date();
            if(challengeEntity.getExpiryDate().after(new java.sql.Date(utilDate.getTime()))){
                status = "expired";
            } else {
                status = "ongoing";
            }
        }
        details.put("challenge", challengeEntity.getChallenge());
        details.put("status", status);

        //convert the java.sql.date to a java.sql.timestamp to access the hh:mm:ss.fff via toString() instead of just the date
        Timestamp timestamp = new Timestamp(challengeEntity.getExpiryDate().getTime());
        details.put("expiry", timestamp.toString());

        return details;
    }

    public String getPreviousChallenges(Integer gameId) {

        String previousChallenges = "{";

        //find all challenges related to the game
        List<ChallengeEntity> challengeEntities = new ArrayList<ChallengeEntity>();
        challengeEntities.addAll(challengeRepository.findByGameId(gameId));

        for(Iterator<ChallengeEntity> iterator = challengeEntities.iterator(); iterator.hasNext(); ) {
            ChallengeEntity challengeEntity = iterator.next();
            previousChallenges += challengeEntity.getChallenge() + ". ";
        }
        previousChallenges += "}";
        
        return previousChallenges;
    }
}
