package auth.security;

import auth.exception.AuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class TokenFilter extends OncePerRequestFilter {

  private final TokenProvider tokenProvider;

  public TokenFilter(TokenProvider tokenProvider) {
    this.tokenProvider = tokenProvider;
  }

  @Override
  protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
    String token = tokenProvider.resolveToken(request);
    try {
      if (token != null && tokenProvider.validateToken(token)) {
        Authentication auth = tokenProvider.getAuthentication(token);
        SecurityContextHolder.getContext().setAuthentication(auth);
      }
    } catch (AuthenticationException authenticationException) {
      SecurityContextHolder.clearContext();
      response.sendError(authenticationException.getHttpStatus().value(), authenticationException.getMessage());
      return;
    }

    filterChain.doFilter(request, response);
  }
}