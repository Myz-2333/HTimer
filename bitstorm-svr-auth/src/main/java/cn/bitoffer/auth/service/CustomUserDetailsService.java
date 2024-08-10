package cn.bitoffer.auth.service;

import cn.bitoffer.auth.model.UserCredential;
import cn.bitoffer.auth.model.LoginUser;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private AuthService authService;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserCredential credential = authService.findUserByUsername(username);
        if(credential == null){
            log.error("user not found");
            return null;
        }
        return new LoginUser(credential);
    }
}

