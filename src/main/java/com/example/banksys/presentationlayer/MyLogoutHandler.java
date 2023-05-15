package com.example.banksys.presentationlayer;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
public class MyLogoutHandler implements LogoutHandler {
    @Override
    public void logout(HttpServletRequest request, HttpServletResponse response, Authentication authentication) {
        String requestUrl = request.getRequestURI();
        if (checkLogOutUrl(requestUrl, "/deposit")) {
            try {
                response.sendRedirect("/logout");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private boolean checkLogOutUrl(String requestUrl, String... endsWithUrl) {
        boolean ret = false;
        for (String s : endsWithUrl) {
            ret |= requestUrl.endsWith(s);
        }
        return ret;
    }
}
