package com.bwesound.identity.auth.api;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RegisterRequest(

        @NotBlank(message = "Недопустимый Email")
        @Email(message = "Недопустимый Email")
        @Size(max = 254, message = "Email слишком длинный")
        String email,

        @NotBlank(message = "Недопустимый пароль")
        @Size(min = 12, max = 72, message ="Минимальный размер пароля - 12 символов,максимальный - 72")
        String password
) {
}
