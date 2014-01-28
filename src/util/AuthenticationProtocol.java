package util;

public class AuthenticationProtocol {

    /**
     * Requests publickey from authentication server.
     * @param username
     */
    public static final String REQUEST_PUBLICKEY = "PUBLICKEY ";

    /**
     * Authentication reponse for REQUEST_PUBLICKEY.
     * @param publickey in base64 String.
     */
    public static final String PUBLICKEY_RESPONSE = "PUBKEY";

    /**
     * Requests privatekey from authentication server.
     * @param username
     * @param password
     */
    public static final String REQUEST_PRIVATEKEY = "IDPLAYER ";

    /**
     * Authentication reponse for REQUEST_PRIVATEKEY.
     * @param privatekey in base64 String.
     */
    public static final String PRIVATEKEY_RESPONSE = "PRIVKEY";
}
