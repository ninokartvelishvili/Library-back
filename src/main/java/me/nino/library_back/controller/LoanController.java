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
import java.util.Map;
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

    @PostMapping
    public ResponseEntity<?> createLoan(@RequestBody LoanResponseDTO loanRequest) {
        // Check if the user exists
        User user = userRepository.findById(loanRequest.getUserId())
                .orElse(null);
        if (user == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with ID " + loanRequest.getUserId() + " does not exist.");
        }

        // Check if the book exists
        Book book = bookRepository.findById(loanRequest.getBookId())
                .orElse(null);
        if (book == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Book with ID " + loanRequest.getBookId() + " does not exist.");
        }

        // Check if the book is already borrowed
        boolean isBorrowed = loanRepository.existsByBookIdAndReturnDateIsNull(loanRequest.getBookId());
        if (isBorrowed) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body("Book with ID " + loanRequest.getBookId() + " is already loaned and not returned.");
        }

        // Proceed with loan creation
        LocalDate loanDate = loanRequest.getLoanDate();
        LoanResponseDTO loanResponse = loanService.createLoan(user, book, loanDate);

        return ResponseEntity.status(HttpStatus.CREATED).body(loanResponse);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteLoan(@PathVariable Long id) {

        // Check if the loan exists

        if (!loanRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Loan with ID " + id + " does not exist.");
        }

        // Call the service to delete the user
        loanService.deleteLoanById(id);
        return ResponseEntity.ok("Loan deleted successfully.");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<?> updateLoan(
            @PathVariable Long id,
            @RequestBody Map<String, Object> loanRequest) {
        // Validate loan exists
        Loan loan = loanRepository.findById(id).orElse(null);
        if (loan == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Loan with ID " + id + " does not exist.");
        }

        // Update user if userId is provided
        if (loanRequest.containsKey("userId")) {
            Long userId = Long.valueOf(loanRequest.get("userId").toString());
            User user = userRepository.findById(userId).orElse(null);
            if (user == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("User with ID " + userId + " does not exist.");
            }
            loan.setUser(user);
        }

        // Update book if bookId is provided
        if (loanRequest.containsKey("bookId")) {
            Long bookId = Long.valueOf(loanRequest.get("bookId").toString());
            boolean isBorrowed = loanRepository.existsByBookIdAndReturnDateIsNull(bookId);
            if (isBorrowed && !bookId.equals(loan.getBook().getId())) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                        .body("Book with ID " + bookId + " is already loaned and not returned.");
            }

            Book book = bookRepository.findById(bookId).orElse(null);
            if (book == null) {
                return ResponseEntity.status(HttpStatus.NOT_FOUND)
                        .body("Book with ID " + bookId + " does not exist.");
            }
            loan.setBook(book);
        }

        // Update book title if provided
        if (loanRequest.containsKey("bookTitle")) {
            String bookTitle = loanRequest.get("bookTitle").toString();
            if (loan.getBook() != null) {
                loan.getBook().setTitle(bookTitle); // Ensure the book entity is updated
                bookRepository.save(loan.getBook()); // Persist book changes
            }
        }

        // Update loan date
        if (loanRequest.containsKey("loanDate")) {
            LocalDate loanDate = LocalDate.parse(loanRequest.get("loanDate").toString());
            loan.setLoanDate(loanDate);
        }

        // Update return date
        if (loanRequest.containsKey("returnDate")) {
            Object returnDateValue = loanRequest.get("returnDate");
            if (returnDateValue == null) {
                loan.setReturnDate(null); // Set return date to null
            } else {
                LocalDate returnDate = LocalDate.parse(returnDateValue.toString());
                loan.setReturnDate(returnDate);
            }
        }

        // Save the updated loan
        Loan updatedLoan = loanRepository.save(loan);

        LoanResponseDTO responseDTO = new LoanResponseDTO(
                updatedLoan.getId(),
                updatedLoan.getUser().getId(),
                updatedLoan.getBook().getId(),
                updatedLoan.getBook().getTitle(),
                updatedLoan.getLoanDate(),
                updatedLoan.getReturnDate()
        );

        return ResponseEntity.ok(responseDTO);
    }


}
