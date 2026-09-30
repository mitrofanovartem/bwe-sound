package com.bwesound.identity.auth;

import com.bwesound.identity.auth.api.LoginRequest;
import com.bwesound.identity.auth.api.LoginResponse;
import com.bwesound.identity.auth.token.AccessToken;
import com.bwesound.identity.auth.token.JwtService;
import com.bwesound.identity.user.User;
import com.bwesound.identity.user.UserRepository;
import com.bwesound.identity.user.UserStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class LoginService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public LoginService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService
    ){
        this.userRepository = userRepository;
        this.jwtService = jwtService;
        this.passwordEncoder = passwordEncoder;
    }

    @Transactional(readOnly = true)
    public LoginResponse login(LoginRequest request){
        String email = request.email().trim().toLowerCase(Locale.ROOT);
        User user = userRepository.findByEmail(email).orElseThrow(EmailAlreadyInUseException::new);

        boolean PasswordMatches = passwordEncoder.matches(
                request.password(),
                user.getPasswordHash()
        );

        if (!PasswordMatches || user.getStatus() != UserStatus.ACTIVE) {
            throw new InvalidCredentialsException();
        }

        AccessToken accessToken = jwtService.issueAccessToken(user);

        return new LoginResponse(
                accessToken.value(),
                "Bearer",
                accessToken.expiresAt()
        );
    }
}
