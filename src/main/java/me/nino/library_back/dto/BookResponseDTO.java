package me.nino.library_back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class BookResponseDTO {
    private Long id;
    private String title;
    private String author;
    private String genre;
    private boolean isBorrowed;
//  private String borrowedBy;
}
