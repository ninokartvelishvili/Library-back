package me.nino.library_back.service;

import me.nino.library_back.dto.LoanResponseDTO;
import me.nino.library_back.dto.LoanSummaryDTO;
import me.nino.library_back.dto.UserResponseDTO;
import me.nino.library_back.model.Book;
import me.nino.library_back.model.User;
import me.nino.library_back.repository.LoanRepository;
import me.nino.library_back.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private LoanRepository loanRepository;

    public List<UserResponseDTO> getAllUsers() {
        List<User> users = userRepository.findAll();
        return users.stream()
                .map(this::mapToUserResponseDTO)
                .collect(Collectors.toList());
    }

    public UserResponseDTO mapToUserResponseDTO(User user){
        List<LoanSummaryDTO> LoanSummaryDTO = user.getLoans().stream()
                .map(loan -> new LoanSummaryDTO(
                  loan.getBook().getTitle(),
                  loan.getLoanDate(),
                  loan.getReturnDate()
        )).collect(Collectors.toList());

        return new UserResponseDTO(
                user.getId(),
                user.getUsername(),
                user.getRole(),
                LoanSummaryDTO
        );
    }

    public User addUser(User user){
        return userRepository.save(user);
    }

    public void deleteUserById(Long id) {
        // Perform deletion
        userRepository.deleteById(id);
    }

    public void updateUser(User existingUser, User updatedUser) {
        if (updatedUser.getUsername() != null) {
            existingUser.setUsername(updatedUser.getUsername());
        }
        if (updatedUser.getRole() != null) {
            existingUser.setRole(updatedUser.getRole());
        }
        if (updatedUser.getPassword() != null) {
            existingUser.setPassword(updatedUser.getPassword());
        }

        // Save changes
        userRepository.save(existingUser);
    }

}
