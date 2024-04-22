package com.simpleblogapi.simpleblogapi.filters;

import com.simpleblogapi.simpleblogapi.components.JwtTokenUtil;
import com.simpleblogapi.simpleblogapi.exceptions.UnauthorizedException;
import com.simpleblogapi.simpleblogapi.models.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.data.util.Pair;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtTokenFilter extends OncePerRequestFilter {
    private final UserDetailsService userDetailsService;
    private final JwtTokenUtil jwtTokenUtil;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain)
            throws ServletException, IOException {


        try {
            if (isPublicEndpoint(request)) {
                filterChain.doFilter(request, response);
                return;
            }
            final String token = getTokenFromRequest(request);

            final String email = jwtTokenUtil.getEmailFromToken(token);
            if (email != null
                    && SecurityContextHolder.getContext().getAuthentication() == null) {
                User userDetails = (User) userDetailsService.loadUserByUsername(email);
                if (jwtTokenUtil.validateToken(token, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );
                    authenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                }
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, e.getMessage());
        }
    }

    private boolean isPublicEndpoint(HttpServletRequest request) {
        final List<Pair<String, String>> publicEndpoints = Arrays.asList(
                Pair.of("/api/v1/register", "POST"),
                Pair.of("/api/v1/login", "POST"),
                Pair.of("/api/v1/posts", "GET"),
                Pair.of("/api/v1/categories", "GET")
        );
        for (Pair<String, String> endpoint : publicEndpoints) {
            if (request.getRequestURI().equals(endpoint.getFirst())
                    && request.getMethod().equals(endpoint.getSecond())) {
                return true;
            }
        }
        return false;
    }

    private String getTokenFromRequest(HttpServletRequest request) throws UnauthorizedException {
        String bearerToken = request.getHeader("Authorization");
        if(bearerToken == null || !bearerToken.startsWith("Bearer ")) {
            throw new UnauthorizedException("Token is required");
        }
        return bearerToken.substring(7);
    }
}
