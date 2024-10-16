package me.nino.library_back.repository;

import me.nino.library_back.model.Loan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LoanRepository extends JpaRepository<Loan,Long> {
    List<Loan> findByUserId(Long userId);
    List<Loan> findByBookId(Long bookId);
}
