package springboot_todo.todo.middleware;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.AllArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;
import springboot_todo.todo.entity.UserEntity;
import springboot_todo.todo.exception.CustomException;
import springboot_todo.todo.repository.UserRepository;
import springboot_todo.todo.utils.JwtUtils;

import java.io.IOException;
import java.util.Collections;
import java.util.Map;
import java.util.UUID;

@Component
@AllArgsConstructor
public class JwtMiddleware extends OncePerRequestFilter {
    private final JwtUtils jwtUtils;
    private final HandlerExceptionResolver handlerExceptionResolver;
    private final UserRepository userRepository;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            // ✅ Skip JWT validation for public routes
            String path = request.getServletPath();
            if (path.startsWith("/api/auth/")) {
                filterChain.doFilter(request, response);
                return;
            }

            // ✅ Get Authorization header & validate
            final String authHeader = request.getHeader("Authorization");
            if (authHeader == null || !authHeader.startsWith("Bearer ")) {
                throw new CustomException("Missing or invalid Authorization header", HttpStatus.UNAUTHORIZED);
            }
            final String jwt = authHeader.substring(7);

            final Map<String, String> userData = jwtUtils.decodeJwt(jwt);
            final String userId = userData.get("userId");
            if (userId == null) {
                throw new CustomException("Invalid JWT token: user ID is missing", HttpStatus.UNAUTHORIZED);
            } else if (userId != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                UserEntity user = this.userRepository.findById(UUID.fromString(userId)).orElse(null);
                if (user == null) {
                    throw new CustomException("User not found", HttpStatus.UNAUTHORIZED);
                }
                UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                        user,
                        null,
                        Collections.singletonList(new SimpleGrantedAuthority("ROLE_" + user.getRole().name())));
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
            filterChain.doFilter(request, response);
        } catch (Exception e) {
            this.handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}
