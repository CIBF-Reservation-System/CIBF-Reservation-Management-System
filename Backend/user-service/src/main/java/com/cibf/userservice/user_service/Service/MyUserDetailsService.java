package com.cibf.userservice.user_service.Service;

import com.cibf.userservice.user_service.Entity.UserEntity;
import com.cibf.userservice.user_service.Repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User;
import org.springframework.stereotype.Service;

@Service
public class MyUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    public MyUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    // load user with username ( email )
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        UserEntity userData = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("User not Found"));

        return User.builder()
                .username(userData.getEmail())
                .password(userData.getPassword())
                .build();
    }
}
