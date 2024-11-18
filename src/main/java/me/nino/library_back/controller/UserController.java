package me.nino.library_back.controller;

import me.nino.library_back.dto.UserResponseDTO;
import me.nino.library_back.model.Book;
import me.nino.library_back.model.User;
import me.nino.library_back.repository.BookRepository;
import me.nino.library_back.repository.LoanRepository;
import me.nino.library_back.repository.UserRepository;
import me.nino.library_back.service.BookService;
import me.nino.library_back.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/users")
public class UserController {
    @Autowired
    private UserService userService;
    private final UserRepository userRepository;
    private final BookRepository bookRepository;
    private final LoanRepository loanRepository;
    public UserController(UserRepository userRepository,UserService userService, BookRepository bookRepository, LoanRepository loanRepository) {
        this.userRepository=userRepository;
        this.userService = userService;
        this.bookRepository = bookRepository;
        this.loanRepository = loanRepository;
    }

    @GetMapping
    public List<UserResponseDTO> getAllUsers() {
        return userService.getAllUsers();
    }
    @PostMapping
    public ResponseEntity<User> addUser(@RequestBody User user){
        User createdUser = userService.addUser(user);
        return new ResponseEntity<>(createdUser , HttpStatus.CREATED);
    }

    @DeleteMapping("/delete/{id}")
    public ResponseEntity<String> deleteUser(@PathVariable Long id) {

        // Check if the user exists

        if (!userRepository.existsById(id)) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("USER with ID " + id + " does not exist.");
        }
        if (loanRepository.existsByUserId(id)) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body("User with ID " + id + " cannot be deleted because it has associated loans.");
        }

        // Call the service to delete the user
        userService.deleteUserById(id);
        return ResponseEntity.ok("User deleted successfully.");
    }
}
