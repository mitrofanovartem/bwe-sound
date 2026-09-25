package com.bwesound.identity.auth;

import com.bwesound.identity.auth.api.RegisterRequest;
import com.bwesound.identity.auth.api.RegisterResponse;
import com.bwesound.identity.user.User;
import com.bwesound.identity.user.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class RegisterServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private RegistrationService registrationService;

    @Test
    void shouldRegisterUserWithNormalizedEmailAndHashedPassword() {
        String rawPassword = "super-secure-password";
        String passwordHash = "$2a$12$hashed-password";
        RegisterRequest request = new RegisterRequest(
                " Artem@Example.com ",
                rawPassword
        );

        when(userRepository.existsByEmail("artem@example.com"))
                .thenReturn(false);

        when(passwordEncoder.encode(rawPassword))
                .thenReturn(passwordHash);

        when(userRepository.saveAndFlush(any(User.class)))
                .thenAnswer(invocation -> invocation.getArgument(0, User.class));

        RegisterResponse response = registrationService.register(request);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);

        verify(userRepository).saveAndFlush(userCaptor.capture());

        User savedUser = userCaptor.getValue();

        assertThat(savedUser.getEmail()).isEqualTo("artem@example.com");
        assertThat(savedUser.getPasswordHash()).isEqualTo(passwordHash);
        assertThat(savedUser.getPasswordHash()).isNotEqualTo(rawPassword);

        assertThat(response.email()).isEqualTo("artem@example.com");
        assertThat(response.role()).isEqualTo("USER");

        verify(passwordEncoder).encode(rawPassword);
    }

    @Test
    void shouldRejectRegistrationWhenEmailAlreadyExists() {
        RegisterRequest request = new RegisterRequest(
                "artem@example.com",
                "super-secure-password"
        );

        when(userRepository.existsByEmail("artem@example.com"))
                .thenReturn(true);

        assertThatThrownBy(() -> registrationService.register(request))
                .isInstanceOf(EmailAlreadyInUseException.class)
                .hasMessage("Пользователь с таким email уже существует");

        verifyNoInteractions(passwordEncoder);
        verify(userRepository, never()).saveAndFlush(any());
    }
}