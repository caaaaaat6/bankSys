package com.example.banksys.presentationlayer;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SimpleUrlLogoutSuccessHandler;
import org.springframework.stereotype.Component;

import java.io.IOException;

//public class MyLogoutSuccessHandler implements LogoutSuccessHandler {
//
//    @Override
//    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication) throws IOException, ServletException {
//        String requestUrl = request.getRequestURI();
//        if (checkLogOutUrl(requestUrl, "/deposit")) {
//            try {
//                response.sendRedirect("/logout");
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
//
//    private boolean checkLogOutUrl(String requestUrl, String... endsWithUrl) {
//        boolean ret = false;
//        for (String s : endsWithUrl) {
//            ret |= requestUrl.endsWith(s);
//        }
//        return ret;
//    }
//}
@Component
public class MyLogoutSuccessHandler extends
        SimpleUrlLogoutSuccessHandler implements LogoutSuccessHandler {

    @Override
    public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse
            response, Authentication authentication)
            throws IOException, ServletException {
        if (authentication != null) {
            System.out.println(authentication.getName());
        }
        response.setStatus(HttpStatus.OK.value());
        response.sendRedirect(request.getHeader("referer"));
    }
}