package com.credit.userms.filter;


import com.credit.userms.config.JwtUtil;
import com.credit.userms.service.CustomUserDetailsService;
import jakarta.servlet.*;
import jakarta.servlet.http.*;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.*;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final CustomUserDetailsService userDetailsService;

    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain)
            throws ServletException, IOException {

        System.out.println("Request intercepted by JwtAuthFilter....");
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            System.out.println("JWT token generated....");
            String username = jwtUtil.extractUsername(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtUtil.validateToken(jwt, userDetails.getUsername())) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,//credential is null since the user is already authenticated.
                                    userDetails.getAuthorities()
                            );
                    //This line sets additional meta data like "Client IP address", "Session ID(if present)", "Request origin" etc.
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    //This will set the UsernamePasswordAuthenticationToken authentication object in Security context.
                    //Other more details are in notes.
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    System.out.println("JWT token is validated....");
                    System.out.println("Security Context is now holding the current user details...");
                }
                System.out.println("JWT Filter triggered for: " + request.getRequestURI());
                System.out.println("Auth Header: " + authHeader);
                System.out.println("Extracted Username: " + username);
                System.out.println("username: " + userDetails.getUsername());
                System.out.println("Authorities: " + userDetails.getAuthorities());

            }
        }
        if(authHeader==null){
            System.out.println("The incoming request is a request without an Authorization header! so it is an unauthenticated user");
        }
        filterChain.doFilter(request, response);
    }
}
