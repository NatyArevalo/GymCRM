package com.gymcrm.gymcrm.Services;

import com.gymcrm.gymcrm.DTO.UserDTO;
import com.gymcrm.gymcrm.Entities.AuthenticationResponse;
import com.gymcrm.gymcrm.Entities.Token;
import com.gymcrm.gymcrm.Entities.User;
import com.gymcrm.gymcrm.Repositories.TokenRepository;
import com.gymcrm.gymcrm.Repositories.UserRepository;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class AuthenticationService {
    private final UserRepository userRepository;
    private final JwtService jwtService;

    private final TokenRepository tokenRepository;

    private final AuthenticationManager authenticationManager;

    public AuthenticationService(UserRepository userRepository,
                                 JwtService jwtService,
                                 TokenRepository tokenRepository,
                                 AuthenticationManager authenticationManager) {
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.tokenRepository = tokenRepository;
        this.authenticationManager = authenticationManager;
    }

    public AuthenticationResponse authenticate(UserDTO request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        User user = userRepository.searchUserByUsername(request.getUsername());
        String accessToken = jwtService.generateToken(user);

        revokeAllTokenByUser(user);
        saveUserToken(accessToken, user);

        return new AuthenticationResponse(accessToken, "User login was successful");

    }
    private void saveUserToken(String accessToken, User user) {
        Token token = new Token();
        token.setToken(accessToken);
        token.setIsLoggedOut(false);
        token.setUser(user);
        tokenRepository.save(token);
    }
    private void revokeAllTokenByUser(User user) {
        List<Token> validTokens = tokenRepository.findAllTokensByUsername(user.getUsername());
        if(validTokens.isEmpty()) {
            return;
        }

        validTokens.forEach(t-> {
            t.setIsLoggedOut(true);
        });

        tokenRepository.saveAll(validTokens);
    }

}
