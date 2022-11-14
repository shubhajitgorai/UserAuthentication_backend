package com.newtglobal.userauthentication.Repository;

import com.newtglobal.userauthentication.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

import javax.transaction.Transactional;




public interface UserRepository extends JpaRepository<User, Long> {
    User findByEmail(String email);
    Optional<User> findByUsernameOrEmail(String username, String email);
    Optional<User> findByUsernameOrEmailAndPassword(String username, String email, String password);
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
    Boolean existsByEmail(String email);
    
    String passwordPassword = "UPDATE userauthentication.users SET password = ?1 WHERE email= ?2";
    
    @Transactional
    @Modifying(clearAutomatically = true)
    @Query(value = passwordPassword, nativeQuery = true)
    void updateUserPass(String pass, String oldEmail);
      
}
