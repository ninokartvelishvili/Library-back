package me.nino.library_back.controller;

import me.nino.library_back.dto.LoanResponseDTO;
import me.nino.library_back.model.Book;
import me.nino.library_back.model.Loan;
import me.nino.library_back.model.User;
import me.nino.library_back.repository.BookRepository;
import me.nino.library_back.repository.LoanRepository;
import me.nino.library_back.repository.UserRepository;
import me.nino.library_back.service.LoanService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/loans")
public class LoanController {
    @Autowired
    private LoanService loanService;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private BookRepository bookRepository;
    @GetMapping("/{userId}")
    public List<LoanResponseDTO> getLoansByUser(@PathVariable Long userId) {
        return loanService.getLoansByUser(userId);
    }
    @GetMapping
    public List<LoanResponseDTO> getAllLoans() {
        return loanService.getAllLoans();
    }

    //TODO logic here not in service
    @PostMapping
    public ResponseEntity<LoanResponseDTO> createLoan(@RequestBody LoanResponseDTO loanRequest) {
        User user = userRepository.findById(loanRequest.getUserId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));
        Book book = bookRepository.findById(loanRequest.getBookId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Book not found"));

       boolean isBorrowed = loanRepository.existsByBookIdAndReturnDateIsNull(loanRequest.getBookId());
       if(isBorrowed){
           throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Book is already loaned and not returned.");
       }
        LocalDate date =loanRequest.getLoanDate();

        LoanResponseDTO loanResponse = loanService.createLoan(user, book, date);

        return new ResponseEntity<>(loanResponse, HttpStatus.CREATED);
    }

}
