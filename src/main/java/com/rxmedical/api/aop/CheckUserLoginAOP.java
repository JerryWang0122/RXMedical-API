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

import java.lang.reflect.Field;
import java.lang.reflect.Method;
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
				if (args.length != 0){
					System.out.println(args[0]);
					Method setUserId = args[0].getClass().getDeclaredMethod("setUserId", Integer.class);
					setUserId.invoke(args[0], userInfoMap.get("userId"));
				}

				result = joinPoint.proceed();
			}

		} catch (Throwable e) {
			e.printStackTrace();
			result = ResponseEntity.ok(new ApiResponse<>(false, "伺服器發生錯誤", null));
		}
		return result;
	}
}
