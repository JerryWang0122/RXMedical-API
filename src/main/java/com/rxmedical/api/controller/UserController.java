package com.rxmedical.api.controller;

import com.nimbusds.jose.JOSEException;
import com.rxmedical.api.model.dto.*;
import com.rxmedical.api.model.response.ApiResponse;
import com.rxmedical.api.service.JWTService;
import com.rxmedical.api.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.text.ParseException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;

	@Autowired
	private JWTService jwtService;
	
	@GetMapping("/test")
	public String getTest() {
		return "User API 連接成功";
	}

	// 登入防止CSRF

	@PostMapping("/user/CSRFToken")
	public ResponseEntity<ApiResponse<List<String>>> getUserToken() throws JOSEException {
		return ResponseEntity.ok(new ApiResponse<>(true, "CSRF令牌", jwtService.getCSRFTokenJWT()));
	}


	// 判斷使用者登入
	@PostMapping("/user/login")
	public ResponseEntity<ApiResponse<Map<String,String>>> postUserLogin(@RequestBody UserLoginDto userLoginDto, HttpServletRequest request) throws NoSuchAlgorithmException, ParseException, JOSEException {
		if (!jwtService.checkCSRFTokenJWT(userLoginDto.token(), request)) {
			return ResponseEntity.ok(new ApiResponse<>(false, "CSRF驗證失敗", null));
		}

		Map<String,String> userInfo = userService.checkUserLogin(userLoginDto);

		if (userInfo == null) {
			return ResponseEntity.ok(new ApiResponse<>(false, "帳號或密碼不正確!", null));
		}
		
		return ResponseEntity.ok(new ApiResponse<>(true, "使用者資訊", userInfo));
	}
	
	// 登出
	public void getLogout() {
		// TODO: 請實作
	}
	
	// 註冊
	@PostMapping("/user/register")
	public ResponseEntity<ApiResponse<Object>> registerUserInfo(@RequestBody UserRegisterDto userRegisterDto, HttpServletRequest request) throws NoSuchAlgorithmException, ParseException {

		if (!jwtService.checkCSRFTokenJWT(userRegisterDto.token(), request)) {
			return ResponseEntity.ok(new ApiResponse<>(false, "CSRF驗證失敗", null));
		}
		Boolean registerSuccess = userService.registerUserInfo(userRegisterDto);

		if (!registerSuccess) {
			return ResponseEntity.ok(new ApiResponse<>(false, "註冊失敗", null));
		}
		return ResponseEntity.ok(new ApiResponse<>(true, "註冊成功", null));
	}
	
	// 取得個人資訊
	@PostMapping("/user/profile")
	public ResponseEntity<ApiResponse<UserInfoDto>> getUserInfo(@RequestBody CurrUserDto currUserDto) {
		
		UserInfoDto info = userService.getUserInfo(currUserDto.userId());
		
		if (info == null) {
			return ResponseEntity.ok(new ApiResponse<>(false, "使用者資訊不存在", null));
		}
		return ResponseEntity.ok(new ApiResponse<>(true, "使用者資訊", info));
	}
	
	// 修改個人資料
	@PutMapping("/user/profile")
	public ResponseEntity<ApiResponse<UserInfoDto>> editUserInfo(@RequestBody UserEditInfoDto userEditInfoDto) {
		
		UserInfoDto updateInfo = userService.updateUserInfo(userEditInfoDto);
		
		if (updateInfo == null) {
			return ResponseEntity.ok(new ApiResponse<>(false, "使用者資訊更新失敗", null));
		}
		return ResponseEntity.ok(new ApiResponse<>(true, "使用者資訊", updateInfo));
		// DataIntegrityViolationException 問題 (先不要處理這個 Error)
		// return ResponseEntity.ok(new ApiResponse<>(false, "信箱(帳號)重複", null));
	}


	// 取得個人衛材清單歷史
	@PostMapping("/user/purchase")
	public ResponseEntity<ApiResponse<List<PurchaseHistoryDto>>> getPurchaseHistoryList(@RequestBody CurrUserDto currUserDto) {
		List<PurchaseHistoryDto> userPurchaseHistoryList = userService.getUserPurchaseHistoryList(currUserDto.userId());
		if (userPurchaseHistoryList == null) {
			return ResponseEntity.ok(new ApiResponse<>(false, "無此人員", null));
		}
		return ResponseEntity.ok(new ApiResponse<>(true, "歷史衛材申請資料", userPurchaseHistoryList));
	}

	// 前台使用者，取得訂單明細
	@PostMapping("/user/purchase/detail")
	public ResponseEntity<ApiResponse<List<OrderDetailDto>>> getPurchaseDetails(@RequestBody RecordDto recordDto) {
		List<OrderDetailDto> purchaseDetails = userService.getPurchaseDetails(recordDto);
		if (purchaseDetails == null) {
			return ResponseEntity.ok(new ApiResponse<>(false, "存在問題", null));
		}
		return ResponseEntity.ok(new ApiResponse<>(true, "訂單明細", purchaseDetails));
	}

	// 前台使用者完成訂單流程
	@PostMapping("/user/purchase/finish")
	public ResponseEntity<ApiResponse<String>> finishOrder(@RequestBody RecordDto recordDto) {
		String errorMsg = userService.finishOrder(recordDto);
		if (errorMsg == null) {
			return ResponseEntity.ok(new ApiResponse<>(true, "訂單完成", null));
		}
		return ResponseEntity.ok(new ApiResponse<>(false, errorMsg, null));
	}

	// 後台查詢所有使用者
	@PostMapping("/admin/member")
	public ResponseEntity<ApiResponse<List<MemberInfoDto>>> getMemberList(@RequestBody CurrUserDto currUserDto) {
		
		List<MemberInfoDto> memberList = userService.getMemberList();
		
		return ResponseEntity.ok(new ApiResponse<>(true, "員工權限資訊", memberList));
	}

	// root 使用者調整會員權限
	@PutMapping("/root/member")
	public ResponseEntity<ApiResponse<Boolean>> changeMemberAuthLevel(@RequestBody ChangeMemberAuthDto memberAuthDto) {
		
		Boolean success = userService.updateMemberAuthLevel(memberAuthDto);
		
		if (success) {
			return ResponseEntity.ok(new ApiResponse<>(true, "權限更新成功", true));
		}
		return ResponseEntity.ok(new ApiResponse<>(false, "權限更新失敗", false));
	}

	// 取得運送人員清單
	@PostMapping("/admin/transporter")
	public ResponseEntity<ApiResponse<List<TransporterDto>>> getTransporterList(@RequestBody CurrUserDto currUserDto) {
		List<TransporterDto> transporterList = userService.getTransporterList();
		return ResponseEntity.ok(new ApiResponse<>(true, "運送人員資訊", transporterList));
	}

}
