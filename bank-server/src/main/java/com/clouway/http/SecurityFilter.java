package com.clouway.http;

import com.clouway.core.SessionRepository;
import com.google.inject.Inject;
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

    @Inject
    public SecurityFilter(SessionRepository sessionRepository) {

        this.sessionRepository = sessionRepository;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletResponse response = (HttpServletResponse) servletResponse;

        HttpServletRequest request = (HttpServletRequest) servletRequest;

        Cookie[] cookies = request.getCookies();

        String sessionId = null;

        if(cookies != null) {
            for(Cookie cookie: cookies) {
                if("sid".equals(cookie.getName())) {
                    sessionId = cookie.getValue();
                }
            }
        }

        if(!sessionRepository.authenticate(sessionId)) {
            response.setStatus(401);
        }

        filterChain.doFilter(request, response);

    }


    @Override
    public void destroy() {

    }
}
