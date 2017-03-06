package com.topoffers.topoffers.common.helpers;

import android.content.Context;
import android.content.Intent;

import com.google.gson.Gson;
import com.orm.SugarRecord;
import com.topoffers.data.models.Header;
import com.topoffers.data.models.Headers;
import com.topoffers.topoffers.common.models.AuthenticationCookie;
import com.topoffers.topoffers.common.models.LoginResult;
import com.topoffers.topoffers.login.LoginActivity;

import java.util.ArrayList;
import java.util.List;

public class AuthenticationHelpers {
    public static Intent checkAuthentication(Context context) {
        AuthenticationCookie cookie = getAuthenticationCookie();
        return checkAuthentication(context, cookie);
    }

    public static Intent checkAuthentication(Context context, AuthenticationCookie cookie) {
        Intent intent;
        if (cookie == null) {
            intent = new Intent(context, LoginActivity.class);
        } else {
            intent = null;
        }

        return intent;
    }

    public static AuthenticationCookie getAuthenticationCookie() {
        List<AuthenticationCookie> cookies  = SugarRecord.listAll(AuthenticationCookie.class);
        if (cookies.size() == 0) {
            return null;
        } else {
            return cookies.get(0);
        }
    }

    public static void logout(Context context) {
        // Remove authentication cookies
        SugarRecord.deleteAll(AuthenticationCookie.class);

        // Redirect go login screen
        Intent intent = new Intent(context, LoginActivity.class);
        context.startActivity(intent);
    }

    public static LoginResult getLoginResult() {
        List<LoginResult> loginResults = SugarRecord.listAll(LoginResult.class);
        if (loginResults.size() == 0) {
            return new LoginResult();
        } else {
            return loginResults.get(0);
        }
    }

    public static Headers getAuthenticationHeaders(AuthenticationCookie authenticationCookie) {
        Gson gson = new Gson();
        String cookieStringValue = gson.toJson(authenticationCookie, AuthenticationCookie.class);
        Header header = new Header("x-cookie", cookieStringValue);

        List<Header> headersList =  new ArrayList<Header>();
        headersList.add(header);
        Headers headers = new Headers(headersList);
        return headers;
    }
}
