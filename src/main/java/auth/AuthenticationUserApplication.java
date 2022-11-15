package auth;

import auth.model.UserEntity;
import auth.model.UserEntityRole;
import auth.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Collections;

@SpringBootApplication
@RequiredArgsConstructor
@Log4j2
public class AuthenticationUserApplication implements CommandLineRunner {

  final UserService userService;

  public static void main(String[] args) {
    org.springframework.boot.SpringApplication.run(AuthenticationUserApplication.class, args);
  }

  @Bean
  public ModelMapper modelMapper() {
    return new ModelMapper();
  }

  @Override
  public void run(String... params) {
    log.info("Creating testing admin and client users!");

    UserEntity admin = new UserEntity();
    admin.setUsername("admin");
    admin.setPassword("123");
    admin.setEmail("admin@email.com");
    admin.setUserEntityRoles(new ArrayList<UserEntityRole>(Collections.singletonList(UserEntityRole.ROLE_ADMIN)));

    userService.signUp(admin);

    UserEntity client = new UserEntity();
    client.setUsername("client");
    client.setPassword("123");
    client.setEmail("client@email.com");
    client.setUserEntityRoles(new ArrayList<UserEntityRole>(Collections.singletonList(UserEntityRole.ROLE_CLIENT)));

    userService.signUp(client);
  }
}
