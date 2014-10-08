package com.clouway.http;

import com.clouway.core.Session;
import com.clouway.core.SessionRepository;
import com.google.inject.Inject;
import com.google.inject.Provider;
import com.google.inject.Singleton;

import javax.servlet.*;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author Emil Georgiev <emogeorgiev88@gmail.com>.
 */
@Singleton
public class SecurityFilter implements Filter {


    private final SessionRepository sessionRepository;
    private final Provider<Session> sessionProvider;

    @Inject
    public SecurityFilter(SessionRepository sessionRepository, Provider<Session> sessionProvider) {

        this.sessionRepository = sessionRepository;
        this.sessionProvider = sessionProvider;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        Session session = sessionProvider.get();

        if (session == null || !sessionRepository.authenticate(session.getSessionId())) {
            response.setStatus(401);
            return;
        }
        filterChain.doFilter(request, response);
    }

    @Override
    public void destroy() {

    }
}
