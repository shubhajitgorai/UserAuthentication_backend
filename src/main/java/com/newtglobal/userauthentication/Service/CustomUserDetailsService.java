package com.newtglobal.userauthentication.Service;

import org.springframework.stereotype.Service;

import com.newtglobal.userauthentication.Entity.Role;
import com.newtglobal.userauthentication.Entity.User;
import com.newtglobal.userauthentication.Exception.UserException;
import com.newtglobal.userauthentication.Repository.UserRepository;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CustomUserDetailsService implements UserDetailsService {

    private UserRepository userRepository;

    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String usernameOrEmail) throws UsernameNotFoundException {
       User user = userRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail)
               .orElseThrow(() ->
                       new UserException("User not found with username or email:" + usernameOrEmail));
      
      // User user1 = userRepository.findByUsernameAndPassword(usernameOrEmail, user.getPassword()).orElseThrow(() ->
      // new UserException("User not found with username or email:" + usernameOrEmail));
        return new org.springframework.security.core.userdetails.User("User not found with username or email:"+user.getEmail(),
                user.getPassword(), mapRolesToAuthorities(user.getRoles()));
    }
    

    private Collection< ? extends GrantedAuthority> mapRolesToAuthorities(Set<Role> roles){
        return roles.stream().map(role -> new SimpleGrantedAuthority(role.getName())).collect(Collectors.toList());
    }
}

