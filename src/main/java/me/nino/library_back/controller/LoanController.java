package me.nino.library_back.controller;

import me.nino.library_back.model.Loan;
import me.nino.library_back.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
