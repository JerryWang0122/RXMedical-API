package com.rxmedical.api.aop;

import com.rxmedical.api.model.po.User;
import com.rxmedical.api.model.response.ApiResponse;
import com.rxmedical.api.repository.UserRepository;
import com.rxmedical.api.service.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import java.util.Map;
import java.util.Optional;

// 檢查前台使用者登入狀態
@Component
@Aspect
public class CheckUserLoginAOP {

	@Autowired
	private UserRepository userRepository;

	@Autowired
	private JWTService jwtService;

	// 設定切點
	// ---------------- User ----------------------
	@Pointcut(value = "execution(* com.rxmedical.api.controller.UserController.getUserInfo(..))")
	public void getUserInfo() {}

	@Pointcut(value = "execution(* com.rxmedical.api.controller.UserController.editUserInfo(..))")
	public void editUserInfo() {}

	@Pointcut(value = "execution(* com.rxmedical.api.controller.UserController.getPurchaseHistoryList(..))")
	public void getPurchaseHistoryList() {}

	@Pointcut(value = "execution(* com.rxmedical.api.controller.UserController.getPurchaseDetails(..))")
	public void getPurchaseDetails() {}

	@Pointcut(value = "execution(* com.rxmedical.api.controller.UserController.finishOrder(..))")
	public void finishOrder() {}

	// ---------------- Product -------------------
	@Pointcut(value = "execution(* com.rxmedical.api.controller.ProductController.getProductList(..))")
	public void getProductList() {}

	@Pointcut(value = "execution(* com.rxmedical.api.controller.ProductController.getProductItemInfo(..))")
	public void getProductItemInfo() {}

	// ---------------- Sale ----------------------
	@Pointcut(value = "execution(* com.rxmedical.api.controller.SaleController.orderGenerate(..))")
	public void orderGenerate() {}



	@Around(value = "getUserInfo() || editUserInfo() || getPurchaseHistoryList() || getPurchaseDetails() || " +
				"finishOrder() ||" +
				"getProductList() || getProductItemInfo() ||" +
				"orderGenerate()")
	public Object aroundCheckLogin(ProceedingJoinPoint joinPoint) {

		Object result = null;
		Optional<User> optionalUser;
		String verifyToken = null;

		try {
			System.out.println("測試前置");

			// 驗證JWT
			HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
			Map<String, Object> userInfoMap = jwtService.verifyUserUsageJWT(request);
			if (userInfoMap == null) {  // 驗證失敗
				result = ResponseEntity.ok(new ApiResponse<>(false, "JWT Verify Error", null));
			} else if ("register".equals(userInfoMap.get("authLevel")) || "off".equals(userInfoMap.get("authLevel"))) { // 權限不足
				result = ResponseEntity.ok(new ApiResponse<>(false, "權限錯誤", null));
			} else {  // 驗證成功
				Object[] args = joinPoint.getArgs();
				if (args.length == 0){
					result = joinPoint.proceed();
				} else {

				}
			}


//			if (args.length > 1) { // 非DTO，而是用多個參數傳入的，則 currUserId 為第一個參數
//				optionalUser = userRepository.findById((Integer) args[0]);
//			} else {
//				// DTO 裡面應該包含 currUserId
//				// 使用反射取得 currUserId
//				Integer currUserId = (Integer) args[0].getClass().getDeclaredMethod("userId").invoke(args[0]);
//				verifyToken = (String) args[0].getClass().getDeclaredMethod("verifyToken").invoke(args[0]);
//				optionalUser = userRepository.findById(currUserId);
//			}
//
//			if (optionalUser.isEmpty() || verifyToken == null) { // 若找不到使用者，或是根本沒有token
//				result = ResponseEntity.ok(new ApiResponse<>(false, "LoginFirst", null));
//			} else {
//				User u = optionalUser.get();
//				if (!verifyToken.equals(null)) {
//					result = ResponseEntity.ok(new ApiResponse<>(false, "TokenError", null));
//				} else if (u.getAuthLevel().equals("staff") || u.getAuthLevel().equals("admin") ||
//						u.getAuthLevel().equals("root")){
//					result = joinPoint.proceed();
//				} else {
//					result = ResponseEntity.ok(new ApiResponse<>(false, "權限有誤", null));
//				}
//			}

		} catch (Throwable e) {
			e.printStackTrace();
			result = ResponseEntity.ok(new ApiResponse<>(false, "伺服器發生錯誤", null));
		}
		return result;
	}
}
