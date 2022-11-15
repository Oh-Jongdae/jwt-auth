package auth.model;

import org.springframework.security.core.GrantedAuthority;

public enum UserEntityRole implements GrantedAuthority {
  ROLE_ADMIN, ROLE_CLIENT;

  public String getAuthority() {
    return name();
  }

}
