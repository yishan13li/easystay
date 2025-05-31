package com.easystay.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.easystay.dto.UserDto;
import com.easystay.dto.UserLoggedInfo;
import com.easystay.entity.User;
import com.easystay.repository.UserRepository;
import com.easystay.service.UserService;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;

@RestController
@RequestMapping("/users")
public class UserController {

	@Autowired
	private UserRepository userRepository;
	
	@Autowired
	private UserService userService;
	
	@SecurityRequirement(name = "bearerAuth")
	@GetMapping("/{id}")
	public ResponseEntity<UserDto> getUserById(@PathVariable Long id) {
			Optional<User> opionalUser = userRepository.findById(id);
			
			if(opionalUser.isPresent()) {
				User user = opionalUser.get();
				UserDto dto = new UserDto(
						user.getId(),
						user.getUsername(),
						user.getEmail(),
						user.getPhone(),
						user.getRole(),
						user.getAvatar(),
						user.getStatus(),
						user.getCreatedAt()
						);
            return ResponseEntity.ok(dto);
        } else {
            return ResponseEntity.notFound().build();
        }
    }
	
    @GetMapping("/me")
    public ResponseEntity<UserDto> getCurrentUser() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
        }

        UserLoggedInfo userDetails = (UserLoggedInfo) auth.getPrincipal();
        UserDto dto = userService.getUserDtoById(userDetails.getUserId());

        return ResponseEntity.ok(dto);
    }
	
}
