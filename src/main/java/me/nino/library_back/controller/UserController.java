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
import java.util.Optional;

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

    @PutMapping("/update/{id}")
    public ResponseEntity<String> updateUser(@PathVariable Long id, @RequestBody User updatedUser) {
        // Check if the user exists
        Optional<User> optionalUser = userRepository.findById(id);
        if (optionalUser.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("User with ID " + id + " does not exist.");
        }

        // Delegate update logic to the service
        userService.updateUser(optionalUser.get(), updatedUser);

        return ResponseEntity.ok("User updated successfully.");
    }

}
