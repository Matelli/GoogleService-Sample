package fr.matelli.GoogleService.googleService.service;

import com.google.api.client.auth.oauth2.Credential;
import com.google.api.services.oauth2.Oauth2;
import com.google.api.services.oauth2.model.Userinfoplus;
import fr.matelli.GoogleService.googleService.GoogleAuthHelper;

import java.io.IOException;
import java.util.List;

public class UserInfo extends GoogleAuthHelper {

    public UserInfo(String redirectUri) throws Exception {
        super(redirectUri);
    }

    public UserInfo(String redirectUri, List<String> scopes) throws Exception {
        super(redirectUri);
        this.scopes.addAll(scopes);
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
        Userinfoplus userInfo = null;
        userInfo = userInfoService.userinfo().get().execute();
        if (userInfo != null && userInfo.getId() != null) {
            return userInfo;
        }
        return userInfo;
    }
}