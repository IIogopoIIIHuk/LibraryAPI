package com.example.LibraryAPI;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
@Schema(description = "Запрос на аутентификацию")
public class SignUpRequest {

    @Schema(description = "Имя пользователя", example = "Olesya")
    @Size(min = 5, max = 50, message = "Имя пользователя должно содержать от 5 до 50 символов")
    @NotBlank(message = "Имя пользователя не может быть пустыми")
    private String username;

    @Schema(description = "Пароль", example = "designer12345")
    @Size(max = 255, message = "Длина пароля должна быть не более 255 символов")
    private String password;

}
