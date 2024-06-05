//package com.nam.repository;
//
//import com.nam.model.RefreshToken;
//import com.nam.model.User;
//import org.springframework.data.jpa.repository.JpaRepository;
//import org.springframework.data.jpa.repository.Modifying;
//
//import java.util.Optional;
//
//public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {
//    Optional<RefreshToken> findByToken(String token);
//
//    Optional<RefreshToken> findByUser(Optional<User> user);
//
//    @Modifying
//    int deleteByUser(User user);
//}