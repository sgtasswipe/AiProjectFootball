const teamForm = document.getElementById("input-form");
const baseUrl = "http://localhost:8080";

// Event listener for form submission
teamForm.addEventListener('submit', async (event) => {
    event.preventDefault();
    const league = document.getElementById("leagues").value;
    const region = document.getElementById("regions").value;
    const teamName = document.getElementById("team").value;

    const oddsJsonOutput = await fetchTeamOdds2(league, region, teamName);
    console.log(oddsJsonOutput);

    displayMatchCards(oddsJsonOutput); // Assuming `oddsJsonOutput` contains the matches array
});

async function fetchTeamOdds2(league, region, team) {
    const oddsUrl = `${baseUrl}/football/odds/h2h`;
    const requestData = { league, region, team };

    // Send the request with POST and the requestData in the body
    return await fetch(oddsUrl, {
        method: 'POST',
        headers: {
            'Content-Type': 'application/json',
        },
        body: JSON.stringify(requestData),
    }).then(response => response.json());
}

// Function to dynamically display match cards
function displayMatchCards(matches) {
    const matchContainer = document.getElementById('match-container');
    matchContainer.innerHTML = ''; // Clear existing cards

    matches.forEach(match => {
        const card = document.createElement('div');
        card.className = 'match-card';

        // Initialize objects to store best odds
        let bestHome = { price: 0, bookmaker: '' };
        let bestAway = { price: 0, bookmaker: '' };
        let bestDraw = { price: 0, bookmaker: '' };

        // Debugging: Log the match data
        console.log('Processing match:', match);

        // Iterate through all bookmakers to find the best odds
        if (match.Bookmakers && Array.isArray(match.Bookmakers)) {
            match.Bookmakers.forEach(bookmaker => {
                if (bookmaker.markets && Array.isArray(bookmaker.markets)) {
                    bookmaker.markets.forEach(market => {
                        if (market.outcomes && Array.isArray(market.outcomes)) {
                            market.outcomes.forEach(outcome => {
                                const outcomeName = outcome.name.toLowerCase();
                                const outcomePrice = parseFloat(outcome.price);

                                // Handle different naming conventions
                                if (outcomeName === match.HomeTeam.toLowerCase() || outcomeName === 'home') {
                                    if (outcomePrice > bestHome.price) {
                                        bestHome = { price: outcomePrice, bookmaker: bookmaker.title };
                                    }
                                } else if (outcomeName === match.AwayTeam.toLowerCase() || outcomeName === 'away') {
                                    if (outcomePrice > bestAway.price) {
                                        bestAway = { price: outcomePrice, bookmaker: bookmaker.title };
                                    }
                                } else if (outcomeName === 'draw') {
                                    if (outcomePrice > bestDraw.price) {
                                        bestDraw = { price: outcomePrice, bookmaker: bookmaker.title };
                                    }
                                }
                            });
                        }
                    });
                }
            });
        } else {
            console.warn('No bookmakers found for match:', match);
        }

        // Generate HTML content for the match card
        card.innerHTML = `
            <h3>${match.HomeTeam} vs ${match.AwayTeam}</h3>
            <p>Date: ${new Date(match.DateTime).toLocaleString()}</p>
            <p>Best Odds:</p>
            <ul>
                <li>Home (${match.HomeTeam}): ${bestHome.price} by ${bestHome.bookmaker}</li>
                <li>Away (${match.AwayTeam}): ${bestAway.price} by ${bestAway.bookmaker}</li>
                <li>Draw: ${bestDraw.price} by ${bestDraw.bookmaker}</li>
            </ul>
        `;

        matchContainer.appendChild(card);
    });
}