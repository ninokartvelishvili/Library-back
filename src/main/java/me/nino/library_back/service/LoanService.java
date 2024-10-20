package me.nino.library_back.service;

import me.nino.library_back.dto.LoanResponseDTO;
import me.nino.library_back.model.Book;
import me.nino.library_back.model.Loan;
import me.nino.library_back.model.User;
import me.nino.library_back.repository.BookRepository;
import me.nino.library_back.repository.LoanRepository;
import me.nino.library_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class LoanService {
    @Autowired
    private LoanRepository loanRepository;
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BookRepository bookRepository;

    public List<Loan> getLoansByUser(Long userId) {
        return loanRepository.findByUserId(userId);
    }
    public List<LoanResponseDTO> getAllLoans() {
        List<Loan> loans = loanRepository.findAll();
        return loans.stream()
                .map(this::mapToLoanResponseDTO)
                .collect(Collectors.toList());
    }
    public LoanResponseDTO mapToLoanResponseDTO(Loan loan) {
        return new LoanResponseDTO(
                loan.getId(),
                loan.getUser().getId(),
                loan.getBook().getId(),
                loan.getLoanDate(),
                loan.getReturnDate()
        );
    }
    public LoanResponseDTO createLoan(Long userId, Long bookId, LocalDate loanDate) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("User not found"));
        Book book = bookRepository.findById(bookId).orElseThrow(() -> new RuntimeException("Book not found"));

        Loan newLoan = new Loan();
        newLoan.setUser(user);
        newLoan.setBook(book);
        newLoan.setLoanDate(loanDate);
        newLoan.setReturnDate(null);  // Assuming the loan starts without a return date

        Loan savedLoan = loanRepository.save(newLoan);

        return mapToLoanResponseDTO(savedLoan); // Return the DTO instead of the entity
    }
}
