package auth.repository;

import auth.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import javax.transaction.Transactional;

public interface UserRepository extends JpaRepository<UserEntity, Integer> {

  boolean existsByUsername(String username);

  UserEntity findByUsername(String username);

  @Transactional
  void deleteByUsername(String username);

}
