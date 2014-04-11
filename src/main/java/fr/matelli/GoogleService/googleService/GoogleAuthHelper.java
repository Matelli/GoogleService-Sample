package fr.matelli.GoogleService.googleService;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.client.googleapis.auth.oauth2.*;
import com.google.api.client.http.HttpTransport;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.JsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.drive.DriveScopes;
import com.google.api.services.oauth2.Oauth2Scopes;
import org.apache.log4j.Logger;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.security.SecureRandom;
import java.util.Arrays;
import java.util.List;

/**
 * A helper class for Google's OAuth2 authentication API.
 */
public class GoogleAuthHelper {

    // =========================================================================
    // ATTRIBUTES
    // =========================================================================

    protected static Logger logger = Logger.getLogger(GoogleAuthHelper.class);
    protected String clientSecretsLocation = "/client_secret.json";
    protected JsonFactory jsonFactory = new JacksonFactory();
    protected HttpTransport httpTransport = new NetHttpTransport();

    protected List<String> scopes = Arrays.asList(Oauth2Scopes.USERINFO_EMAIL,
            Oauth2Scopes.USERINFO_PROFILE, DriveScopes.DRIVE, CalendarScopes.CALENDAR);
    protected String stateToken = null;
    protected String refreshToken = null;

    /**
     * Authorization code to exchange for OAuth 2.0 credentials.
     */
    protected String authorizationCode = null;
    protected GoogleAuthorizationCodeFlow flow = null;
    protected GoogleClientSecrets googleClientSecrets = this.importClientSecrets();
    protected String applicationName = "Matelli";
    protected String redirectUri;
    protected String baseRedirectUri = "http://localhost:8080/GoogleService";

    // =========================================================================
    // CONSTRUCTORS
    // =========================================================================

    /**
     * Contructeur par defaut
     *
     * @param redirectUri CallBalk
     * @throws Exception
     */
    public GoogleAuthHelper(String redirectUri) throws Exception {
        if (redirectUri != null) {
//            if (!googleClientSecrets.getWeb().getRedirectUris().contains(new String(baseRedirectUri + redirectUri)))
//                throw new Exception("Redirect URI not found");
            this.redirectUri = baseRedirectUri + redirectUri;
        }
        getFlow();
    }

    // =========================================================================
    // METHODS
    // =========================================================================

    /**
     * Import le credential Json de Google
     *
     * @return
     * @throws IOException
     */
    private GoogleClientSecrets importClientSecrets() throws IOException {
        Reader reader = new InputStreamReader(GoogleAuthHelper.class.getResourceAsStream(clientSecretsLocation));
        return GoogleClientSecrets.load(jsonFactory, reader);
    }


    /**
     * Initialise le GoogleAuthorizationCodeFlow avec le googleClientSecrets et
     * les scopes
     *
     * @return Le flow de GoogleAuthorization
     */
    public GoogleAuthorizationCodeFlow getFlow() {
        if (flow == null) {
            jsonFactory = new JacksonFactory();
            httpTransport = new NetHttpTransport();

            String approvalPrompt = "force";
            if (refreshToken != null) {
                approvalPrompt = "auto";
            }

            flow = new GoogleAuthorizationCodeFlow
                    .Builder(httpTransport, jsonFactory, googleClientSecrets, scopes)
                    .setApprovalPrompt(approvalPrompt)
                    .setAccessType("offline").build();
            generateStateToken();
        }
        return flow;
    }

    /**
     * Exchange an authorization code for OAuth 2.0 credentials.
     *
     * @return OAuth 2.0 credentials
     * @throws java.io.IOException
     */
    public Credential exchangeCode() throws IOException {
        Credential credential = null;

        if (refreshToken == null) {
            GoogleTokenResponse response = flow.newTokenRequest(authorizationCode)
                    .setRedirectUri(redirectUri)
                    .setGrantType("authorization_code")
                    .execute();

            credential = new GoogleCredential.Builder()
                    .setTransport(httpTransport)
                    .setJsonFactory(jsonFactory)
                    .setClientSecrets(googleClientSecrets)
                    .build();
            credential.setFromTokenResponse(response);
        } else {
            credential = new GoogleCredential.Builder()
                    .setJsonFactory(jsonFactory)
                    .setTransport(httpTransport)
                    .setClientSecrets(googleClientSecrets)
                    .build();
            credential.setRefreshToken(refreshToken);
        }
        logger.info("RefreshToken : " + credential.getRefreshToken());
        logger.info("AccessToken : " + credential.getAccessToken());
        logger.info("ExpiresInSeconds : " + credential.getExpiresInSeconds());
        return credential;
    }

    /**
     * Retrieve the authorization URL
     *
     * @return L'url d'authorisation
     */
    public String getAuthorizationUrl() {
        GoogleAuthorizationCodeRequestUrl urlBuilder = flow.newAuthorizationUrl()
                .setRedirectUri(redirectUri)
                .setState(stateToken);
        return urlBuilder.build();
    }

    /**
     * Generates a secure state token
     */
    private void generateStateToken() {
        SecureRandom sr1 = new SecureRandom();
        stateToken = "google;" + sr1.nextInt();
    }

    /**
     * Ajoute des scopes supplementaires
     *
     * @param scopes
     */
    private void addScopes(List<String> scopes) {
        this.scopes.addAll(scopes);
    }

    // =========================================================================
    // OVERRIDES
    // =========================================================================

    // =========================================================================
    // GETTERS & SETTERS
    // =========================================================================


    public void setRefreshToken(String refreshToken) {
        this.refreshToken = refreshToken;
    }

    public void setAuthorizationCode(String authorizationCode) {
        this.authorizationCode = authorizationCode;
    }

    public String getRefreshToken() {
        return refreshToken;
    }
}
