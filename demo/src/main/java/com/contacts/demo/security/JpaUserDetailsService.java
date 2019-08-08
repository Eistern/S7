package com.contacts.demo.security;

import com.contacts.demo.security.data.types.User;
import com.contacts.demo.security.data.types.UserEntry;
import com.contacts.demo.security.data.JpaUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class JpaUserDetailsService implements UserDetailsService {
    private JpaUserRepository userRepository;

    @Autowired
    public JpaUserDetailsService(JpaUserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Transactional
    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        Optional<User> user = userRepository.findByUsername(s);
        if (user.isEmpty())
            throw new UsernameNotFoundException(s);
        User foundUser = user.get();

        UserEntry userDetails = new UserEntry(foundUser.getUserId(), foundUser.getUsername());
        userDetails.setPassword(foundUser.getPassword());
        userDetails.setUserAuthorities(foundUser.getStringRoles());

        return userDetails;
    }
}
