package me.nino.library_back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data

public class UserResponseDTO {
    private Long id;
    private String username;
    private String role;
}
