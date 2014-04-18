package fr.matelli.GoogleService.googleService.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.calendar.CalendarScopes;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.Oauth2Scopes;
import com.google.api.services.oauth2.model.Userinfoplus;
import fr.matelli.GoogleService.googleService.GoogleAuthHelper;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

/**
 * Classe permettant d'interagir avec les services Google User
 *
 * @author <a href="http://www.matelli.fr">Matelli</a>
 * @see fr.matelli.GoogleService.googleService.GoogleAuthHelper
 * @see <a href"https://developers.google.com/drive/v2/reference/>https://developers.google.com/drive/v2/reference/</a>
 */
public class UserInfoService extends GoogleAuthHelper {

    {
        // Liste des autorisations
        this.scopes.addAll(Arrays.asList(Oauth2Scopes.USERINFO_EMAIL,
                Oauth2Scopes.USERINFO_PROFILE));
    }

    public UserInfoService(String redirectUri) throws Exception {
        super(redirectUri);
    }

    /**
     * Expects an Authentication Code, and makes an authenticated request for
     * the user's profile information
     *
     * @return JSON formatted user profile information
     * @param credential authentication code provided by google
     * @throws java.io.IOException
     */
    public Userinfoplus getUserInfo(Credential credential) throws IOException {
        Oauth2 userInfoService = new Oauth2.Builder(httpTransport, jsonFactory, credential).build();
        Userinfoplus userInfo = userInfoService.userinfo().get().execute();
        if (userInfo != null && userInfo.getId() != null) {
            return userInfo;
        }
        return null;
    }
}