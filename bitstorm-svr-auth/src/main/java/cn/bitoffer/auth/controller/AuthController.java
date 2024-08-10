package cn.bitoffer.auth.controller;

import cn.bitoffer.auth.dto.AuthRequest;
import cn.bitoffer.auth.model.LoginUser;
import cn.bitoffer.auth.model.UserCredential;
import cn.bitoffer.auth.service.AuthService;
import cn.bitoffer.common.utils.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = "/auth")
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private AuthenticationManager authenticationManager;

//    @PostMapping(value = "/register")
//    public ResponseEntity createUser(@RequestBody UserCredential credential) {
//        credential.setPassword(passwordEncoder.encode(credential.getPassword()));
//        authService.save(credential);
//        return ResponseEntity.status(HttpStatus.CREATED).build();
//    }

    @PostMapping(value = "/token")
    public ResponseEntity<String> generateToken(@RequestBody AuthRequest authRequest) {
        final Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                authRequest.getUsername(), authRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            LoginUser loginUser = (LoginUser)authenticate.getPrincipal();
            final String token = JwtUtil.generateToken(authRequest.getUsername(),loginUser.getUserId());
            return ResponseEntity.status(HttpStatus.OK).body(token);
        } else {
            throw new RuntimeException("invalid access");
        }
    }

    @GetMapping(value = "/validate")
    public ResponseEntity validateToken(@RequestParam String token) {
        JwtUtil.validateToken(token);
        return ResponseEntity.status(HttpStatus.ACCEPTED).build();
    }
}

