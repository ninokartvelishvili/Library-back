package me.nino.library_back.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
public class LoanSummaryDTO {
    private String bookTitle;
    private LocalDate loanDate;
    private LocalDate returnDate;
}
