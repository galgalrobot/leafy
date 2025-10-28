package com.leafy.domain.user;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

/**
 * User 엔티티를 위한 Spring Data JPA 리포지토리
 */
// JpaRepository<User, Long>의 User는 같은 패키지의 User.java를 참조하게 된다.
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByKakaoId(String kakaoId);
}