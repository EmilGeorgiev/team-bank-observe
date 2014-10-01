package com.clouway.http;

import com.clouway.core.CurrentUser;
import com.clouway.core.SessionRepository;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.sitebricks.SitebricksModule;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by emil on 14-9-24.
 */
public class Sitbricks extends SitebricksModule {

    @Override
    protected void configureSitebricks() {
        super.configureSitebricks();
        scan(LoginCtrl.class.getPackage());
        
        at("/bankService").serve(BankService.class);
    }

    @Provides
    public CurrentUser getCurrentUserName(Provider<HttpServletRequest> request, SessionRepository sessionRepository) {

        Cookie[] cookies = request.get().getCookies();

        if(cookies != null) {
            for(Cookie cookie : cookies) {
                if("sid".equals(cookie.getName())) {
                    return sessionRepository.getClientName(cookie.getValue());
                }
            }
        }

        return null;
    }
}
