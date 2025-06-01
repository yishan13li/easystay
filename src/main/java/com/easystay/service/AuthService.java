package com.easystay.service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.easystay.dto.UserLoggedInfo;
import com.easystay.dto.UserRegisterDto;
import com.easystay.entity.User;
import com.easystay.projection.UserLoginProjection;
import com.easystay.repository.UserRepository;
import com.easystay.security.JwtUtil;

@Service
public class AuthService implements UserDetailsService{

	@Autowired
	private UserRepository userRepository;
	
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtUtil jwtUtil;
    
    //Register
    public void register(UserRegisterDto dto) {
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new RuntimeException("Email has already existed");
        }

        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new RuntimeException("Username has already existed");
        }

        String encodedPassword = passwordEncoder.encode(dto.getPassword());

        User user = new User();
        user.setUsername(dto.getUsername());
        user.setRole(dto.getRole());
        user.setEmail(dto.getEmail());
        user.setPassword(encodedPassword);

        userRepository.save(user);
    }
    
    //Login
    public String login(String usernameOrEmail, String password) {
    	//login by username or account
    	Optional<UserLoginProjection> optionalUser = userRepository.findProjectedByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
    	
    	if(optionalUser.isEmpty()) {
    		throw new RuntimeException("Username or email does not exist");
    	}
    	
    	UserLoginProjection  user = optionalUser.get();
    	
    	if(user.getStatus() == false) {
    	    throw new RuntimeException("User account is disabled");
    	}
    	
    	//check password
    	if(!passwordEncoder.matches(password, user.getPassword())) {
    		throw new RuntimeException("Incorrect password");
    	}
    	
    	//Login successfully and then generate Token
    	return jwtUtil.generateToken(user.getUsername(),user.getRole(),user.getId());
    }
    
    // share with Spring Security and JWT
    private UserLoggedInfo buildUserLoggedInDetails(User user) {
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_" + user.getRole().toUpperCase());

        UserLoggedInfo userDetails = new UserLoggedInfo();
        userDetails.setUserId(user.getId());
        userDetails.setUsername(user.getUsername());
        userDetails.setPassword(user.getPassword());
        userDetails.setAuthorities(List.of(authority));

        return userDetails;
    }

    // crucial for Spring Security 
    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
        User user = userRepository.findEntityByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
                .orElseThrow(() -> new UsernameNotFoundException("User not found"));
        return buildUserLoggedInDetails(user);
    }

    // validate for jwt
    public UserDetails loadUserById(Long userId) throws UsernameNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id: " + userId));
        return buildUserLoggedInDetails(user);
    }

}
