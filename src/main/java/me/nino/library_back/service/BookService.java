package me.nino.library_back.service;

import me.nino.library_back.dto.BookResponseDTO;
import me.nino.library_back.dto.LoanResponseDTO;
import me.nino.library_back.model.Book;
import me.nino.library_back.model.Loan;
import me.nino.library_back.repository.BookRepository;
import me.nino.library_back.repository.LoanRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class BookService {
    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private LoanRepository loanRepository;
    public List<BookResponseDTO> getAllBooks() {
        List<Book> books = bookRepository.findAll();
        return books.stream()
                .map(this::mapToBookResponseDTO)
                .collect(Collectors.toList());
    }
    private BookResponseDTO mapToBookResponseDTO(Book book) {
        // Check if the book is borrowed
        boolean isBorrowed = loanRepository.existsByBookIdAndReturnDateIsNull(book.getId());
        return new BookResponseDTO(
                book.getId(),
                book.getTitle(),
                book.getAuthor(),
                book.getGenre(),
                isBorrowed
        );
    }
    public List<BookResponseDTO> searchBooksByTitle(String title) {
        List<Book> books = bookRepository.findByTitleContaining(title);

        return books.stream()
                .map(this::mapToBookResponseDTO)
                .collect(Collectors.toList());
    }

    public Book addBook(Book book){
        return bookRepository.save(book);
    }
    public void deleteBookById(Long id) {
        // Perform deletion
        bookRepository.deleteById(id);
    }

    public void updateBook(Book existingBook, Book updatedBook) {
        // Update only the fields that are not null
        if (updatedBook.getTitle() != null) {
            existingBook.setTitle(updatedBook.getTitle());
        }
        if (updatedBook.getAuthor() != null) {
            existingBook.setAuthor(updatedBook.getAuthor());
        }
        if (updatedBook.getGenre() != null) {
            existingBook.setGenre(updatedBook.getGenre());
        }

        // Save changes
        bookRepository.save(existingBook);
    }
}
