package codelex.io.blackjack;

import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.TimeUnit;

public class BlackJack {

    private static final String NETWORK_URL = "https://deckofcardsapi.com";
    private static final String ANSI_RESET = "\u001B[0m";
    private static final String ANSI_RED = "\u001B[31m";
    private static final String ANSI_GREEN = "\u001B[32m";

    private final static String DECK_COUNT = "6";

    public static void main(String[] args) throws InterruptedException {

        while (true) {

            Scanner in = new Scanner(System.in);

            GetDeckResult yourDeck = getDeck();

            boolean isNotEnough = true;
            boolean gameOver = false;


            int playerPoints = 0;
            int computerPoints = 0;


            while (isNotEnough && !gameOver) {

                Card card = drawCard(yourDeck.getDeckId());
                int cardValue = card.getCardValue().getBlackJackValue();
                if ((cardValue == 11) && ((playerPoints + cardValue) > 21)) {
                    cardValue = 1;
                }
                playerPoints += cardValue;
                System.out.println("Your card is " + card.getCardValue() + " of " + card.getSuit());
                System.out.println("You have " + ANSI_GREEN + playerPoints + ANSI_RESET + " points!");
                if (playerPoints > 21) {
                    System.out.println(ANSI_RED + "You lost!" + ANSI_RESET);
                    gameOver = true;
                }
                if (!gameOver) {
                    System.out.println("Draw again?");
                    isNotEnough = in.nextLine().equalsIgnoreCase("y");
                }
            }
            if (!gameOver) {
                System.out.println("Computers turn!");
                isNotEnough = true;
                while (isNotEnough) {

                    Card card = drawCard(yourDeck.getDeckId());
                    int cardValue = card.getCardValue().getBlackJackValue();
                    if ((cardValue == 11) && ((computerPoints + cardValue) > 21)) {
                        cardValue = 1;
                    }
                    computerPoints += cardValue;
                    System.out.println("Computer card is " + card.getCardValue() + " of " + card.getSuit());
                    System.out.println("Computer has " + ANSI_GREEN + computerPoints + ANSI_RESET + " points!");
                    TimeUnit.SECONDS.sleep(1);

                    if (computerPoints < 18) {
                        System.out.println("Computer DRAWS again");
                    }
                    else {
                        System.out.println("Computer has enaugh points!");
                        isNotEnough = false;
                    }
                    TimeUnit.SECONDS.sleep(3);
                }

                if ((playerPoints >= computerPoints) || (computerPoints > 21)) {
                    System.out.println(ANSI_GREEN + "Player WINS!!!" + ANSI_RESET);
                }
                else {
                    System.out.println(ANSI_RED + "Computer WINS!!!" + ANSI_RESET);
                }
            }
            System.out.println("Play again?");
            String playAgain = in.nextLine();
            if (playAgain.toLowerCase().equals("n")) {
                System.exit(0);
            }
        }


    }

    private static void shuffleDeck(String deckId) {
        callApi(NETWORK_URL + "/api/deck/" + deckId + "/shuffle/");
    }

    private static GetDeckResult getDeck() {
        String result = callApi(NETWORK_URL + "/api/deck/new/shuffle/?deck_count=" + DECK_COUNT);
        JSONObject rawResult = new JSONObject(result);

        return new GetDeckResult(
                rawResult.getBoolean("success"),
                rawResult.getString("deck_id"),
                rawResult.getBoolean("shuffled"),
                rawResult.getInt("remaining")
        );
    }

    private static Card drawCard(String deckID) {
        String result = callApi(NETWORK_URL + "/api/deck/" + deckID + "/draw/?count=" + DECK_COUNT);
        JSONObject rawResult = new JSONObject(result);
        JSONObject cardData = (JSONObject) rawResult.getJSONArray("cards").get(0);
        Suit suit = Suit.valueOf(cardData.getString("suit"));
        CardValue value = CardValue.getCardValueByString(cardData.getString("value"));
        return new Card(suit, value);

    }

    private static String callApi(String theURL) {

        try {
            URL url = new URL(theURL);
            BufferedReader in = new BufferedReader(new InputStreamReader(url.openStream()));
            StringBuilder inputData = new StringBuilder();
            String inputLine;
            while ((inputLine = in.readLine()) != null) {
                inputData.append(inputLine);
            }
            return inputData.toString();
        } catch (IOException e) {
            System.out.println(ANSI_RED + "Error happened" + ANSI_RESET);
            System.out.println(e.getMessage());
            System.exit(0);
            return "";
        }

    }


}
