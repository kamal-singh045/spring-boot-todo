package springboot_todo.todo.service;

import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import springboot_todo.todo.dto.LoginDto;
import springboot_todo.todo.entity.UserEntity;
import springboot_todo.todo.exception.CustomException;
import springboot_todo.todo.repository.UserRepository;
import springboot_todo.todo.utils.JwtUtils;

@Service
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public UserService (
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtUtils jwtUtils
    ) {
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
        if(user == null) {
            throw new CustomException(
                    "Email not found",
                    HttpStatus.NOT_FOUND
            );
        }
        boolean isPasswordMatched = passwordEncoder.matches(loginData.getPassword(), user.getPassword());
        if(!isPasswordMatched) {
            throw new CustomException(
                    "Incorrect password",
                    HttpStatus.BAD_REQUEST
            );
        }
        return jwtUtils.generateJwt(user.getId(), user.getEmail());
    }
}
