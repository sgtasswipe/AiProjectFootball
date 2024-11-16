package org.example.chatgptspring08.controller;

import java.util.List;
import java.util.Map;

import org.example.chatgptspring08.dto.odds.OddsRequest;
import org.example.chatgptspring08.dto.odds.OddsResponse;
import org.example.chatgptspring08.service.DebugCache;
import org.example.chatgptspring08.service.OddsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;

@CrossOrigin("*")
@RestController
public class OddsApiController {


    @Autowired
    private OddsService oddsService;

    @Autowired
    DebugCache debugCache;

    private final WebClient oddsWebClient;

    public OddsApiController(WebClient.Builder webClientBuilder, OddsService oddsService) {
        this.oddsWebClient = webClientBuilder.baseUrl("https://odds.p.rapidapi.com").build();
        this.oddsService = oddsService;
    }

    // Receive head-to-head matches using OddsRequest
    // - Receive data through @RequestBody instead of @PathVariable
    @PostMapping("/football/odds/h2h")
    public ResponseEntity<List<Map<String, Object>>> getOdds2(@RequestBody OddsRequest oddsRequest) {
        String league = oddsRequest.getLeague();
        String region = oddsRequest.getRegion();
        String team = oddsRequest.getTeam();

        List<OddsResponse> responseList = oddsService.getOddsFromLeagueAndRegion(oddsWebClient, league, region);
        debugCache.printCacheContent();  // For debugging purposes

        List<Map<String, Object>> filteredMatchList = oddsService.getMatchesForTeam(responseList, team);
        return ResponseEntity.ok(filteredMatchList);
    }
}
