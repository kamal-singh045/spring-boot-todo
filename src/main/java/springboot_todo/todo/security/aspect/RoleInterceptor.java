package springboot_todo.todo.security.aspect;

import java.util.Arrays;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import lombok.RequiredArgsConstructor;
import springboot_todo.todo.entity.UserEntity;
import springboot_todo.todo.enums.RoleEnum;
import springboot_todo.todo.exception.CustomException;
import springboot_todo.todo.security.AllowedRoles;

@Aspect
@Component
@RequiredArgsConstructor
public class RoleInterceptor {

	@Around("@annotation(AllowedRoles)")
	public Object checkRole(ProceedingJoinPoint proceedingJoinPoint) throws Throwable {
		MethodSignature signature = (MethodSignature) proceedingJoinPoint.getSignature();
		System.out.println(signature);
		AllowedRoles allowedRoles = signature.getMethod().getAnnotation(AllowedRoles.class);

		Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		if(principal == null || !(principal instanceof UserEntity user)) {
			throw new CustomException("Invalid User", HttpStatus.UNAUTHORIZED);
		}
		RoleEnum userRole = user.getRole();

		if(!Arrays.asList(allowedRoles.value()).contains(userRole)) {
			throw new CustomException("Access Denied", HttpStatus.BAD_REQUEST);
		}

		return proceedingJoinPoint.proceed();
	}
}
