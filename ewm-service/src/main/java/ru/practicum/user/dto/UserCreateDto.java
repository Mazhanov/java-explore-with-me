package ru.practicum.user.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserCreateDto {

    @NotBlank(message = "name cannot be missing")
    @Size(min = 2, max = 250)
    private String name;

    @NotBlank(message = "email cannot be missing")
    @Email(message = "email is incorrect")
    @Size(min = 6, max = 254)
    private String email;
}
