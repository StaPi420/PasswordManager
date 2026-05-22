package org.example.passwordmanager.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.example.passwordmanager.services.JWTService;
import org.example.passwordmanager.services.UserService;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JWTService jwtService;
    private final UserService userService;

    public JwtAuthFilter(
            JWTService jwtService,
            UserService userService
    ){
        this.jwtService = jwtService;
        this.userService = userService;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain
    ) throws IOException, ServletException {
        String authHeader = request.getHeader("Authorization");

        if (authHeader != null && authHeader.startsWith("Bearer ")){
            String token = authHeader.substring(7);

            if (jwtService.isTokenValid(token)){
                String username = jwtService.extractUserName(token);
                UserDetails userDetails = userService.loadUserByUsername(username);

                UsernamePasswordAuthenticationToken authenticationToken =
                        new UsernamePasswordAuthenticationToken(
                                userDetails,
                                null,
                                userDetails.getAuthorities()
                        );
                if (
                        username != null && SecurityContextHolder.getContext().getAuthentication() == null
                )
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
            }
        }

        filterChain.doFilter(request, response);
    }
}
