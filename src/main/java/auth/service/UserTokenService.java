package auth.service;

import auth.model.UserEntity;
import auth.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Log4j2
public class UserTokenService implements UserDetailsService {

  private final UserRepository repository;

  @Override
  public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
    log.info("Loading User by Username!");
    final UserEntity userEntity = repository.findByUsername(username);

    if (userEntity == null) {
      log.error("Error at Loading User by Username!");
      throw new UsernameNotFoundException("User '" + username + "' not found");
    }

    return org.springframework.security.core.userdetails.User.withUsername(username).password(userEntity.getPassword()).authorities(userEntity.getUserEntityRoles()).accountExpired(false).accountLocked(false).credentialsExpired(false).disabled(false).build();
  }
}
