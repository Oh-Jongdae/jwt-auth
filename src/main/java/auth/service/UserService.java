package auth.service;

import auth.model.UserEntity;
import auth.repository.UserRepository;
import auth.security.TokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import javax.servlet.http.HttpServletRequest;
import auth.exception.AuthenticationException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
  private final UserRepository userRepository;
  private final PasswordEncoder passwordEncoder;
  private final TokenProvider tokenProvider;
  private final AuthenticationManager authenticationManager;

  public String signIn(String username, String password) {
    try {
      authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(username, password));
      return tokenProvider.generateToken(username, userRepository.findByUsername(username).getUserEntityRoles());
    } catch (org.springframework.security.core.AuthenticationException e) {
      throw new AuthenticationException("Invalid username/password supplied!", HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  public String signUp(UserEntity userEntity) {
    if (!userRepository.existsByUsername(userEntity.getUsername())) {
      userEntity.setPassword(passwordEncoder.encode(userEntity.getPassword()));
      userRepository.save(userEntity);
      return tokenProvider.generateToken(userEntity.getUsername(), userEntity.getUserEntityRoles());
    } else {
      throw new AuthenticationException("Username is already in use!", HttpStatus.UNPROCESSABLE_ENTITY);
    }
  }

  public void delete(String username) {
    userRepository.deleteByUsername(username);
  }

  public UserEntity search(String username) {
    UserEntity appUserEntity = userRepository.findByUsername(username);
    if (appUserEntity == null) {
      throw new AuthenticationException("The user doesn't exist!", HttpStatus.NOT_FOUND);
    }
    return appUserEntity;
  }

  public UserEntity whoAmI(HttpServletRequest req) {
    return userRepository.findByUsername(tokenProvider.getUsername(tokenProvider.resolveToken(req)));
  }

  public String refresh(String username) {
    return tokenProvider.generateToken(username, userRepository.findByUsername(username).getUserEntityRoles());
  }
}