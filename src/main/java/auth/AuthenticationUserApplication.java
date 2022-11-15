package auth;

import auth.model.UserEntity;
import auth.model.UserEntityRole;
import auth.service.UserService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

import java.util.ArrayList;
import java.util.Collections;

@SpringBootApplication
@RequiredArgsConstructor
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
    UserEntity admin = new UserEntity();
    admin.setUsername("spadeAdm");
    admin.setPassword("6575");
    admin.setEmail("gusttaa00@gmail.com");
    admin.setUserEntityRoles(new ArrayList<UserEntityRole>(Collections.singletonList(UserEntityRole.ROLE_ADMIN)));

    userService.signUp(admin);

    UserEntity client = new UserEntity();
    client.setUsername("spadeClient");
    client.setPassword("6575");
    client.setEmail("gusttaa00@gmail.com");
    client.setUserEntityRoles(new ArrayList<UserEntityRole>(Collections.singletonList(UserEntityRole.ROLE_CLIENT)));

    userService.signUp(client);
  }
}
