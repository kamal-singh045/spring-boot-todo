package springboot_todo.todo.service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import springboot_todo.todo.dto.GetAllUsersDto;
import springboot_todo.todo.dto.GetAllUsersResponse;
import springboot_todo.todo.dto.LoginDto;
import springboot_todo.todo.entity.UserEntity;
import springboot_todo.todo.enums.UserStatusEnum;
import springboot_todo.todo.exception.CustomException;
import springboot_todo.todo.repository.UserRepository;
import springboot_todo.todo.specification.UserSpecification;
import springboot_todo.todo.utils.JwtUtils;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public UserService(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtils jwtUtils) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    public UserEntity getUserByEmail(String email) {
        return this.userRepository.getUserByEmail(email);
    }

    public String register(UserEntity userDetails) {
        String encodedPassword = passwordEncoder.encode(userDetails.getPassword());
        userDetails.setPassword(encodedPassword);
        UserEntity createdUser = this.userRepository.save(userDetails);
        return jwtUtils.generateJwt(createdUser.getId(), createdUser.getEmail());
    }

    public String login(LoginDto loginData) {
        UserEntity user = userRepository.getUserByEmail(loginData.getEmail());
        if (user == null) {
            throw new CustomException(
                    "Email not found",
                    HttpStatus.NOT_FOUND);
        } else if (user.getStatus() == UserStatusEnum.BLOCKED) {
            throw new CustomException(
                    "Your account is blocked. Please contact to admin",
                    HttpStatus.BAD_REQUEST);
        } else if (user.getStatus() == UserStatusEnum.PENDING) {
            throw new CustomException(
                    "Your account is in pending. Please wait for admin approval",
                    HttpStatus.BAD_REQUEST);
        }
        boolean isPasswordMatched = passwordEncoder.matches(loginData.getPassword(), user.getPassword());
        if (!isPasswordMatched) {
            throw new CustomException(
                    "Incorrect password",
                    HttpStatus.BAD_REQUEST);
        }
        return jwtUtils.generateJwt(user.getId(), user.getEmail());
    }

    public String updateUserStatus(UUID userId, UserStatusEnum status) {
        UserEntity user = this.userRepository.findById(userId).orElse(null);
        if (user == null) {
            throw new CustomException(
                    "User not found",
                    HttpStatus.NOT_FOUND);
        }
        user.setStatus(status);
        this.userRepository.save(user);
        return "User status updated to " + status;
    }

    public List<GetAllUsersResponse> getAllUsers(GetAllUsersDto input) {
        Specification<UserEntity> spec = UserSpecification.filtersUsers(input);
        List<UserEntity> users = this.userRepository.findAll(spec);
        return users.stream()
                .map(user -> new GetAllUsersResponse(user.getId(), user.getName(), user.getEmail(), user.getStatus()))
                .collect(Collectors.toList());
    }
}
