package com.rxmedical.api.aop;

import com.rxmedical.api.model.po.User;
import com.rxmedical.api.model.response.ApiResponse;
import com.rxmedical.api.repository.UserRepository;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

// 檢查前台使用者登入狀態
@Component
@Aspect
public class CheckUserLoginAOP {

	@Autowired
	private UserRepository userRepository;

	// 設定切點
	// ---------------- User ----------------------
	@Pointcut(value = "execution(* com.rxmedical.api.controller.UserController.getUserInfo(..))")
	public void getUserInfo() {}

	@Pointcut(value = "execution(* com.rxmedical.api.controller.UserController.editUserInfo(..))")
	public void editUserInfo() {}

	// ---------------- Product -------------------
	@Pointcut(value = "execution(* com.rxmedical.api.controller.ProductController.getProductList(..))")
	public void getProductList() {}

	@Pointcut(value = "execution(* com.rxmedical.api.controller.ProductController.getProductItemInfo(..))")
	public void getProductItemInfo() {}

	// ---------------- Sale ----------------------
	@Pointcut(value = "execution(* com.rxmedical.api.controller.SaleController.orderGenerate(..))")
	public void orderGenerate() {}



	@Around(value = "getUserInfo() || editUserInfo() ||" +
				"getProductList() || getProductItemInfo() ||" +
				"orderGenerate()")
	public Object aroundCheckLogin(ProceedingJoinPoint joinPoint) {

		Object result = null;
		Optional<User> optionalUser;

		try {
			System.out.println("測試前置");
			// 前置：檢查用戶登入狀態
			Object[] args = joinPoint.getArgs();
			if (args.length > 1) { // 非DTO，而是用多個參數傳入的，則 currUserId 為第一個參數
				optionalUser = userRepository.findById((Integer) args[0]);
			} else { 
				// DTO 裡面應該包含 currUserId
				// 使用反射取得 currUserId
				Integer currUserId = (Integer) args[0].getClass().getDeclaredMethod("userId").invoke(args[0]);
				optionalUser = userRepository.findById(currUserId);
			}

			if (optionalUser.isPresent()){
				User u = optionalUser.get();
				if (u.getAuthLevel().equals("staff") || u.getAuthLevel().equals("admin") ||
						u.getAuthLevel().equals("root")){
					result = joinPoint.proceed();
				} else {
					result = ResponseEntity.ok(new ApiResponse<>(false, "權限有誤", null));
				}
			} else {
				result = ResponseEntity.ok(new ApiResponse<>(false, "LoginFirst", null));
			}
		} catch (Throwable e) {
			e.printStackTrace();
			result = ResponseEntity.ok(new ApiResponse<>(false, "伺服器發生錯誤", null));
		}
		return result;
	}
}
