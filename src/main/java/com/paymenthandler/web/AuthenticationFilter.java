package com.paymenthandler.web;

import com.google.inject.Provider;
import javax.inject.Inject;
import javax.inject.Singleton;
import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Singleton
public class AuthenticationFilter implements Filter {

    private static final List<String> ALLOWED_PATHS = Arrays.asList(
        "/auth/login",
        "/auth/register",
        "/auth/logout",
        "/h2-console"
    );

    private final Provider<UserSession> userSessionProvider;

    @Inject
    public AuthenticationFilter(Provider<UserSession> userSessionProvider) {
        this.userSessionProvider = userSessionProvider;
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String uri = httpRequest.getRequestURI();
        String contextPath = httpRequest.getContextPath();
        String path = uri.substring(contextPath.length());

        if (isAllowedPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        UserSession userSession = getUserSession();

        if (userSession != null && userSession.isLoggedIn()) {
            chain.doFilter(request, response);
        } else {
            httpResponse.sendRedirect(contextPath + "/auth/login");
        }
    }

    private boolean isAllowedPath(String path) {
        for (String allowedPath : ALLOWED_PATHS) {
            if (path.startsWith(allowedPath)) {
                return true;
            }
        }
        return false;
    }

    private UserSession getUserSession() {
        try {
            return userSessionProvider.get();
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public void destroy() {
    }
}
