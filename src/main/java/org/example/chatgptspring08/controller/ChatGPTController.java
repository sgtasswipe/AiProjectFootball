package org.example.chatgptspring08.controller;

<<<<<<< HEAD
import org.example.chatgptspring08.dto.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
=======
import org.example.chatgptspring08.dto.chatgpt.ChatRequest;
import org.example.chatgptspring08.dto.chatgpt.ChatResponse;
import org.example.chatgptspring08.dto.chatgpt.Choice;
import org.example.chatgptspring08.dto.chatgpt.Usage;
import org.example.chatgptspring08.service.ChatGPTService;
>>>>>>> 7485697c6acb859561298f7924802c2f966388d0
import org.springframework.web.bind.annotation.*;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
@CrossOrigin("*")
@RestController
public class ChatGPTController {
    private final WebClient webClient;
    private final ChatGPTService chatGPTService;

<<<<<<< HEAD

    public ChatGPTController(WebClient.Builder webClientBuilder) {
        this.webClient = webClientBuilder.baseUrl("https://api.openai.com/v1/chat/completions").build();

    }

    @Value("${openai.api.key}")
    private String apiKey;

=======
    public ChatGPTController(WebClient.Builder webClientBuilder, ChatGPTService chatGPTService) {
        this.webClient = webClientBuilder.baseUrl("https://api.openai.com/v1/chat/completions").build();
        this.chatGPTService = chatGPTService;
    }

    // Return usage and choices for ChatGPT from sent odds data
>>>>>>> 7485697c6acb859561298f7924802c2f966388d0
    @PostMapping("/chat")
    public Map<String, Object> chatWithGPT(@RequestBody Map<String, Object> requestData) {
        String stringOddsResponse = requestData.get("message").toString();

        ChatRequest chatRequest = chatGPTService.fetchOddsSummary(stringOddsResponse);
        ChatResponse chatResponse = chatGPTService.promptChat(webClient, chatRequest);

<<<<<<< HEAD
                "2. Identify and select the highest odds for Team1, Draw, and Team2 outcomes across all bookmakers.\n" +

                "3. Calculate and select the highest for each outcome, and indicate the respective site.\n" +

                "4. Format the output precisely as follows:\n" +
                "Team1 - Team2 (DD/MM/YYYY HH:MM)\n" +
                "* Prediction: (Predicted Winner or Draw based on all of the options with the lowest odds)\n" +
                "* Team1: (Odds as a decimal) (Site with highest odds)\n" +
                "* Draw: (Odds as a decimal) (Site with highest draw odds)\n" +
                "* Team2: (Odds as a decimal) (Site with highest odds)\n\n" +

                "Example (Only use this as an example):\n" +
                "Ipswich Town - Manchester United (12/11/2024 21:00)\n" +
                "* Prediction: Manchester United\n" +
                "* Ipswich Town: 5.0 (Betfair)\n" +
                "* Draw: 4.4 (Betfair)\n" +
                "* Manchester United: 1.59 (Betclic)\n\n" +

                "Guidelines:\n" +
                "- Do not add extra commentary or format changes.\n" +
                "- Odds must be formatted in decimal (e.g., '4.0').\n" +
                "- Dates and times should be converted accurately from UK to Danish format.\n" +
                "- Accurately choose the odds for the specific site and if there are multiple sites list them all separated by commas.\n";

        ChatRequest chatRequest = new ChatRequest(); //ChatRequest objekt har jeg dannet med https://www.jsonschema2pojo.org/ værktøj
        chatRequest.setModel("gpt-4o"); //vælg rigtig model. se powerpoint
        List<Message> lstMessages = new ArrayList<>(); //en liste af messages med roller
        lstMessages.add(new Message("system", "You are a helpful odds."));
        lstMessages.add(new Message("user", promptManuscript + "\"" + message + "\""));
        chatRequest.setMessages(lstMessages);
        //chatRequest.setN(1); //n er antal svar fra chatgpt
        //chatRequest.setTemperature(1); //jo højere jo mere fantasifuldt svar (se powerpoint)
        //chatRequest.setMaxTokens(1000); //længde af svar
        chatRequest.setStream(false); //stream = true, er for viderekomne, der kommer flere svar asynkront
        chatRequest.setPresencePenalty(1); //noget med ikke at gentage sig. se powerpoint

        ChatResponse response = webClient.post()
                .contentType(MediaType.APPLICATION_JSON)
                .headers(h -> h.setBearerAuth(apiKey))
                .bodyValue(chatRequest)
                .retrieve()
                .bodyToMono(ChatResponse.class)
                .block();

        List<Choice> lst = response.getChoices();
        Usage usg = response.getUsage();
=======
        List<Choice> lst = chatResponse.getChoices();
        Usage usg = chatResponse.getUsage();
>>>>>>> 7485697c6acb859561298f7924802c2f966388d0

        Map<String, Object> map = new HashMap<>();
        map.put("Usage", usg);
        map.put("Choices", lst);

        return map;
    }
}



