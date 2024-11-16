package org.example.chatgptspring08.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.example.chatgptspring08.dto.odds.OddsResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class OddsService {
    @Value("${odds.api.key}")
    private String apiKey;

    // Create head-to-head API call to the odds webclient and return the response
    // - League: football league
    // - Region: of betting websites

    //@Cacheable(value = "oddsCache", key = "'fullOddsResponse'")  // value= cache name, key= cache key

    @Cacheable(value = "oddsCache", key = "'oddsData_' + #league", unless = "#result == null")
    public List<OddsResponse> getOddsFromLeagueAndRegion(WebClient webClient, String league, String region) {
        System.out.println("Fetching data from external API...");
        return webClient
                .get()
                .uri("/v4/sports/" + league + "/odds?includeLinks=true&regions=" + region + "&markets=h2h")

                .header("x-rapidapi-key", apiKey)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<OddsResponse>>() {})
                .block();
    }

    // Return a list of matches involving specified team
    public List<Map<String, Object>> getMatchesForTeam(List<OddsResponse> oddsResponseList, String team) {
        List<Map<String, Object>> filteredTeamList = new ArrayList<>();

        for (OddsResponse response : oddsResponseList) {
            String homeTeam = response.getHome_team().toLowerCase();
            String awayTeam = response.getAway_team().toLowerCase();

            if (homeTeam.contains(team.toLowerCase()) || awayTeam.contains(team.toLowerCase())) {
                Map<String, Object> map = new HashMap<>();
                // Include all bookmakers
                map.put("Bookmakers", response.getBookmakers());
                map.put("HomeTeam", response.getHome_team());
                map.put("AwayTeam", response.getAway_team());
                map.put("DateTime", response.getCommence_time());
                filteredTeamList.add(map);
            }
        }
        return filteredTeamList;
    }
}
