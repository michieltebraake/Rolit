package server;

/**
 * Protocol Klasse.
 * @author �de Symen Hoekstra
 * @version 1.0.0
 */
public final class Protocol {
	
	/*
	 * Het volgende figuur geeft de nummering van de velden aan die gebruikt wordt om zetten door 
	 * te geven tussen de server en de clients en vice versa.
	 * 
	 * +---+---+---+---+---+---+---+---+
	 * | 0 | 1 | 2 | 3 | 4 | 5 | 6 | 7 |
	 * +---+---+---+---+---+---+---+---+
 	 * | 8 | 9 | 10| 11| 12| 13| 14| 15|
 	 * +---+---+---+---+---+---+---+---+
 	 * | 16| 17| 18| 19| 20| 21| 22| 23|
 	 * +---+---+---+---+---+---+---+---+
 	 * | 24| 25| 26| 27| 28| 29| 30| 31|
 	 * +---+---+---+---+---+---+---+---+
 	 * | 32| 33| 34| 35| 36| 37| 38| 39|
 	 * +---+---+---+---+---+---+---+---+
 	 * | 40| 41| 42| 43| 44| 45| 46| 47|
 	 * +---+---+---+---+---+---+---+---+
 	 * | 48| 49| 50| 51| 52| 53| 54| 55|
 	 * +---+---+---+---+---+---+---+---+
 	 * | 56| 57| 58| 59| 60| 61| 62| 63|
 	 * +---+---+---+---+---+---+---+---+
 	 * 
	 */

    /**
     * Wordt gebruikt om de client in te loggen op de server.
     * <p>
     * client -> server
     *
     * @param clientName naam van de client
     * @param chatSupported geeft aan of de client de chat functie ondersteunt
     * @param challengeSupported geeft aan of de client de challenge functie ondersteunt
     */

    //@ require !name.contains(" ");
    //@ require chatSupported == 0 || chatSupported == 1;
    //@ require challengeSupported == 0 || challengeSupported == 1;
    public static final String AUTHENTICATE_CLIENT = "AUTHENTICATE";

    /**
     * Wordt gebruikt om aan te geven dat een client succesvol is ingelogd.
     * <p>
     * server -> client
     *
     * @param chatSupported geeft aan of de server de chat functie ondersteunt
     * @param challengeSupoorted geeft aan of de server de challenge functie ondersteunt
     */

    //@ require chatSupported == 0 || chatSupported == 1;
    //@ require challengeSupported == 0 || challengeSupported == 1;
    public static final String AUTHENTICATED = "AUTHENTICATED";

    /**
     * Wordt gebruikt om deel te nemen aan een spel.
     * <p>
     * client -> server
     *
     * @param players geeft aan hoeveel spelers aan het spel deel moeten nemen
     */

    //@ require 2 <= players && players <= 4;
    public static final String JOIN_GAME = "JOINGAME";

    /**
     * Wordt gebruikt om een spel te starten.
     * <p>
     * server -> client
     *
     * @param player1 naam van speler 1
     * @param player2 naam van speler 2
     * @param player3 naam van speler 3 (optioneel)
     * @param player4 naam van speler 4 (optioneel)
     */

    //@ require !player1.contains(" ");
    //@ require !player2.contains(" ");
    //@ require !player3.contains(" ");
    //@ require !player4.contains(" ");
    public static final String START_GAME = "STARTGAME";

    /**
     * Wordt gebruikt om aan te geven dat een client een zet moet doen.
     * <p>
     * server -> client
     */
    public static final String REQUEST_MOVE = "REQUESTMOVE";

    /**
     * Wordt gebruikt om een zet naar de server te sturen.
     * <p>
     * client -> server
     *
     * @param move de zet die de client wil doen.
     */

    //@ require 0 <= move && move <= 63;
    public static final String MAKE_MOVE = "MAKEMOVE";

    /**
     * Wordt gebruikt om een zet naar de clients te sturen.
     * <p>
     * server -> client
     *
     * @param move de zet die wordt verstuurd naar de clients
     */

    //@ require 0 <= move && move <= 63;
    public static final String BROADCAST_MOVE = "MOVE";

    /**
     * Wordt gebruikt om aan te geven dat het spel is afgelopen.
     * <p>
     * server -> client
     */
    public static final String GAME_OVER = "GAMEOVER";

    /**
     * Wordt gebruikt om een highscore op te vragen.
     * <p>
     * client -> server
     *
     * @param type het soort highscore dat opgevraagd wordt
     * @param n het aantal highscores dat opgehaald moet worden
     * @param name de naam voor welke speler de highscore opgevraagd moet worden (optioneel)
     */

    //@ require type.equals("day") || type.equals("week") || type.equals("overall");
    //@ require n > 0;
    //@ require !name.contains(" ");
    public static final String GET_HIGHSCORES = "HIGHSCORE";

    /**
     * Wordt gebruikt om een score naar de client de sturen.
     * <p>
     * server -> client
     *
     * @param score1 de eerste score die gestuurd moet worden
     * @param name1 de naam van de speler die de score gehaald heeft
     * @param score2 de tweede score die gestuurd moet worden
     * @param name2 de naam van de speler die de score gehaald heeft
     * @param score3 de tweede score die gestuurd moet worden
     * @param name3 de naam van de speler die de score gehaald heeft
     * ...
     * @param scoren de n'de score die gestuurd moet worden
     * @param namen de naam van de speler die de score gehaald heeft
     */

    //@ requires !name1.contains(" ");
    //@ requires !name2.contains(" ");
    //@ requires !name3.contains(" ");
    //@ ...
    //@ requires !namen.contains(" ");
    public static final String SEND_HIGHSCORES = "HIGHSCORE";


    /**
     * Wordt gebruikt om uit te loggen op de server.
     * <p>
     * client -> server
     */
    public static final String EXIT = "EXIT";

    /**
     * Wordt gebruikt om een informatie over een fout te geven.
     * <p>
     * server -> client
     *
     * @param code identificatienummer van de fout
     * @param message informatie over de fout
     */

    public static final String ERROR = "ERROR";
	
	/* ----------------------------------Security---------------------------------- */

    /**
     * Wordt gebruikt om een identificatie bewijs op te vragen aan een client.
     * <p>
     * server -> client
     *
     * @param token een unieke willekeurige string
     */
    public static final String TOKEN_REQUEST = "TOKENREQUEST";

    /**
     * Wordt gebruikt om een identificatie bewijs naar de server te sturen.
     * <p>
     * client -> server
     *
     * @param token de string die gegeven
     */
    public static final String TOKEN_REPLY = "TOKENREPLY";
	
	/* ---------------------------EXTRA Functionaliteit --------------------------- */

    /**
     * Wordt gebruikt om een bericht naar de server te versturen.
     * <p>
     * client -> server
     *
     * @param player de speler die het bericht verstuurt
     * @param message het bericht
     */

    //@ require message.length() > 0;
    public static final String SEND_MESSAGE = "MESSAGE";

    /**
     * Wordt gebruikt om een bericht naar de clients te sturen.
     * <p>
     * server -> client
     *
     * @param player de speler die het bericht verstuurt
     * @param message het bericht
     */

    //@ require !player.contains(" ");
    //@ require message.length() > 0;
    public static final String BROADCAST_MESSAGE = "MESSAGE";

    /**
     * Wordt gebruikt om op te vragen welke spelers er beschikbaar zijn voor een challenge.
     * <p>
     * client -> server
     */
    public static final String GET_AVAILABLE_PLAYERS = "GETAVAILABLEPLAYERS";

    /**
     * Wordt gebruikt om aan te geven welke spelers er beschikbaar zijn voor een challenge.
     * <p>
     * server -> client
     *
     * @param player1 de naam van speler 1
     * @param player2 de naam van speler 2
     * @param player3 de naam van speler 3
     * ...
     * @param playern de naam van speler n
     */

    //@ require !player1.contains(" ");
    //@ require !player2.contains(" ");
    //@ require !player3.contains(" ");
    //@ ...
    //@ require !playern.contains(" ");
    public static final String SEND_AVAILABLE_PLAYERS = "AVAILABLEPLAYERS";

    /**
     * Wordt gebruikt om aan te geven dat een speler beschikbaar is voor een challenge.
     * <p>
     * server -> client
     *
     * @param player de naam van de speler
     */

    //@ require !player.contains(" ");
    public static final String CHALLENGE_JOIN = "CHALLENGEJOIN";

    /**
     * Wordt gebruikt om aan te geven dat een speler niet meer beschikbaar is voor een challenge.
     * <p>
     * server -> client
     *
     * @param player de naam van de speler
     */

    //@ require !player.contains(" ");
    public static final String CHALLENGE_LEFT = "CHALLENGELEFT";

    /**
     * Wordt gebruikt om spelers uit te dagen voor een challenge.
     * <p>
     * client -> server
     *
     * @param player1 de naam van speler 1
     * @param player2 de naam van speler 2 (optioneel)
     * @param player3 de naam van speler 3 (optioneel)
     */

    //@ require !player1.contains(" ");
    //@ require !player2.contains(" ");
    //@ require !player3.contains(" ");
    public static final String CHALLENGE_PLAYERS = "CHALLENGEPLAYERS";

    /**
     * Wordt gebruikt om een client op de hoogte te brengen van een challenge.
     * <p>
     * server -> client
     *
     * @param player1 de naam de uitdager
     * @param player2 de naam van speler 2 (optioneel)
     * @param player3 de naam van speler 3 (optioneel)
     */

    //@ require !player1.contains(" ");
    //@ require !player2.contains(" ");
    //@ require !player3.contains(" ");
    public static final String CHALLENGE_NOTIFY = "NEWCHALLENGE";

    /**
     * Wordt gebruikt om een challenge te accepteren of te weigeren.
     * <p>
     * client -> server
     *
     * @param player de naam van de speler
     * @param challengeAccepted is de challenge geaccepteerd
     */

    //@ require !player.contain(" ");
    //@ require challengeAccepted == 0 || challengeAccepted == 1;
    public static final String CHALLENGE_RESPONSE = "CHALLENGERESPONSE";


    /**
     * Wordt gebruikt om mee te delen ov een challenge geaccepteerd is.
     * <p>
     * server -> client
     *
     * @param player de naam van de speler
     * @param challengeAccepted is de challenge geaccepteerd
     */

    //@ require !player.contain(" ");
    //@ require challengeAccepted == 0 || challengeAccepted == 1;
    public static final String CHALLENGE_RESPONSE_BROADCAST = "CHALLENGERESPONSEBROADCAST";

    private Protocol() {

    }

}