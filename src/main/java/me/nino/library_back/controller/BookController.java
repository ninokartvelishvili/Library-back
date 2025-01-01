package me.nino.library_back.controller;

import me.nino.library_back.dto.BookResponseDTO;
import me.nino.library_back.model.Book;
import me.nino.library_back.repository.BookRepository;
import me.nino.library_back.repository.LoanRepository;
import me.nino.library_back.service.BookService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/books")
public class BookController {
    @Autowired
    private BookService bookService;
    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    public BookController(BookService bookService, BookRepository bookRepository,LoanRepository loanRepository) {
        this.bookService = bookService;
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
    }
    @GetMapping
    public List<BookResponseDTO> getAllBooks() {
        return bookService.getAllBooks();
    }

    @GetMapping("/search")
    public List<BookResponseDTO> searchBooks(@RequestParam String title) {
        return bookService.searchBooksByTitle(title);
    }
    @PostMapping
    public ResponseEntity<Book> addBook(@RequestBody Book book){
        Book createdBook = bookService.addBook(book);
        return new ResponseEntity<>(createdBook, HttpStatus.CREATED);
    }
    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteBook(@PathVariable Long id) {
        if (!bookRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Book with ID " + id + " does not exist.");
        }
        if (loanRepository.existsByBookId(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("Book with ID " + id + " cannot be deleted because it has associated loans.");
        }
        if (loanRepository.existsByBookIdAndReturnDateIsNull(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body("Book with ID " + id + " is currently borrowed and cannot be deleted.");
        }
        bookService.deleteBookById(id);
        return ResponseEntity.ok("Book deleted successfully.");
    }

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateBook(@PathVariable Long id, @RequestBody Book updatedBook) {
        // Check if the book exists
        Optional<Book> optionalBook = bookRepository.findById(id);
        if (optionalBook.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("Book with ID " + id + " does not exist.");
        }
        bookService.updateBook(optionalBook.get(), updatedBook);
        return ResponseEntity.ok("Book updated successfully.");
    }



}
