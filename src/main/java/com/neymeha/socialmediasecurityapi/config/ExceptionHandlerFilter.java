package com.neymeha.socialmediasecurityapi.config;

import com.neymeha.socialmediasecurityapi.customexceptions.ApiFilterException;
import com.neymeha.socialmediasecurityapi.customexceptions.SocialMediaApiException;
import com.neymeha.socialmediasecurityapi.customexceptions.jwt.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class ExceptionHandlerFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
//            if (request.getHeader("Authorization") == null || !request.getHeader("Authorization").startsWith("Bearer ")) {
//                throw new JwtException("No Token, no access!", HttpStatus.FORBIDDEN);
//            }
            filterChain.doFilter(request, response);
        } catch (SocialMediaApiException e) {
            setErrorResponse(e.getStatus(), response, e);
            e.printStackTrace();
        } catch (AccessDeniedException e) {
            e.printStackTrace();
            setErrorResponse(HttpStatus.FORBIDDEN, response, e);
        } catch (Throwable e) {
            e.printStackTrace();
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, response, e);
        }
    }

    public void setErrorResponse(HttpStatus status, HttpServletResponse response, Throwable ex){
        response.setStatus(status.value());
        response.setContentType("application/json");
        // A class used for errors
        ApiFilterException apiFilterException = new ApiFilterException(status, ex);
        try {
            String json = apiFilterException.convertToJson();
            System.out.println(json);
            response.getWriter().write(json);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
