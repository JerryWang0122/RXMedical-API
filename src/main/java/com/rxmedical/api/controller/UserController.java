package com.rxmedical.api.controller;

import com.rxmedical.api.model.dto.UserRegisterDto;
import com.rxmedical.api.model.response.ApiResponse;
import com.rxmedical.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import com.rxmedical.api.model.dto.UserInfoDto;
import com.rxmedical.api.model.dto.UserLoginDto;

@RestController
@CrossOrigin
@RequestMapping("/api/users")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/test")
	public String getTest() {
		return "User API 連接成功";
	}
		
	// 判斷使用者登入
	@PostMapping("/user/login")
	public ResponseEntity<ApiResponse<UserInfoDto>> postUserLogin(@RequestBody UserLoginDto userLoginDto) {
		UserInfoDto userInfoDto = userService.checkUserLogin(userLoginDto);
		ApiResponse<UserInfoDto> response = new ApiResponse<>();
		if (userInfoDto == null) {
			response.setState(false);
			response.setMessage("帳號或密碼不正確!");
		} else {
			response.setState(true);
			response.setMessage("使用者存在");
			response.setData(userInfoDto);
		}
		return ResponseEntity.ok(response);
	}
	
	// 登出
	public void getLogout() {
		// TODO: 請實作
	}
	
	// 註冊資料寫入DB
	@PostMapping("/user/register")
	public ResponseEntity<ApiResponse<Object>> registerUserInfo(@RequestBody UserRegisterDto userRegisterDto) {

		Boolean registerSuccess = userService.registerUserInfo(userRegisterDto);
		if (!registerSuccess) {
			return ResponseEntity.ok(new ApiResponse<>(false, "註冊失敗", null));
		}
		return ResponseEntity.ok(new ApiResponse<>(true, "註冊成功", null));

	}
	
	// 取得個人資訊
	@PostMapping("/user/profile")
	public ResponseEntity<ApiResponse<UserInfoDto>> getUserInfo(@RequestBody Integer userId) {
		System.out.println(userId);
		UserInfoDto info = userService.getUserInfo(userId);
		if (info == null) {
			return ResponseEntity.ok(new ApiResponse<>(false, "使用者資訊不存在", null));
		}
		return ResponseEntity.ok(new ApiResponse<>(true, "使用者資訊", info));
	}
	
	// 修改個人資料
	public void editUserInfo(UserInfoDto userInfoDto) {
		// TODO: 請實作
	}
}
