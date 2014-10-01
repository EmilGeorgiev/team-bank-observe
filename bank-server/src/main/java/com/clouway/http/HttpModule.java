package com.clouway.http;

import com.clouway.core.*;
import com.clouway.persistent.PersistentBankRepository;
import com.google.inject.Provider;
import com.google.inject.Provides;
import com.google.inject.servlet.RequestScoped;
import com.google.inject.servlet.ServletModule;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * Created by emil on 14-9-24.
 */
public class HttpModule extends ServletModule {

    @Override
    protected void configureServlets() {
        super.configureServlets();

        bind(BankRepository.class).to(PersistentBankRepository.class);
        bind(IdGenerator.class).to(SessionIdGenerator.class);
        bind(SiteMap.class).to(LabelMap.class);
        bind(Clock.class).to(CalendarUtil.class);
    }

    @Provides
    public TransactionMessages getTransactionMessages() {
        return new TransactionMessages() {
            @Override
            public String success() {
                return "Transaction success";
            }

            @Override
            public String failed() {
                return "Transaction failed";
            }
        };
    }
    
    @Provides
    @RequestScoped
    public Session getCurrentSession(Provider<HttpServletRequest> requestProvider, SessionRepository sessionRepository, SiteMap siteMap) {
        Cookie[] cookies = requestProvider.get().getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {

                if (cookie.getName().equals(siteMap.sessionCookieName())) {
                    return sessionRepository.get(cookie.getValue());
                }
            }
        }
        return null;
    }





}
