package auth.service;

import auth.model.UserEntity;

public interface UserService {

  String signIn(String username, String password);

  String signUp(UserEntity userEntity);

  void delete(String username);

  UserEntity search(String username);

  String refresh(String username);
}