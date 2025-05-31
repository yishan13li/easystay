package com.easystay.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.easystay.entity.User;
import com.easystay.projection.UserLoginProjection;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

	boolean existsByUsername(String username);
	boolean existsByEmail(String email);
	Optional<UserLoginProjection> findProjectedByUsernameOrEmail(String username, String email);
	Optional<User> findEntityByUsernameOrEmail(String username, String email);
}
