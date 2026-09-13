package com.bwesound.identity.auth;

import com.bwesound.identity.auth.api.RegisterRequest;
import com.bwesound.identity.auth.api.RegisterResponse;
import com.bwesound.identity.user.User;
import com.bwesound.identity.user.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Locale;

@Service
public class RegistrationService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public RegistrationService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder
    ){
        this.passwordEncoder = passwordEncoder;
        this.userRepository = userRepository;
    }

    @Transactional
    public RegisterResponse register(RegisterRequest request){
        String email = request.email().trim().toLowerCase(Locale.ROOT);

        if(userRepository.existsByEmail(email)){
            throw new EmailAlreadyInUseException();
        }

        String passwordHash = passwordEncoder.encode(request.password());

        User user = new User(email,passwordHash);

        try{
            User savedUser = userRepository.saveAndFlush(user);

            return new RegisterResponse(
                    savedUser.getId(),
                    savedUser.getEmail(),
                    savedUser.getRole().name()
            );
        }
        catch (DataIntegrityViolationException exception){
            throw new EmailAlreadyInUseException();
        }
    }
}
