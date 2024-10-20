package me.nino.library_back.controller;

import me.nino.library_back.dto.LoanResponseDTO;
import me.nino.library_back.model.Loan;
import me.nino.library_back.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/loans")
public class LoanController {
    @Autowired
    private LoanService loanService;

    @GetMapping("/user/{userId}")
    public List<Loan> getLoansByUser(@PathVariable Long userId) {
        return loanService.getLoansByUser(userId);
    }
    @GetMapping
    public List<LoanResponseDTO> getAllLoans() {
        return loanService.getAllLoans();
    }

    @PostMapping
    public ResponseEntity<LoanResponseDTO> createLoan(@RequestBody LoanResponseDTO loanRequest) {
        Long userId = loanRequest.getUserId();
        Long bookId = loanRequest.getBookId();
        LocalDate loanDate = loanRequest.getLoanDate();

        LoanResponseDTO loanResponseDTO = loanService.createLoan(userId, bookId, loanDate);
        return ResponseEntity.ok(loanResponseDTO);
    }
}
