package com.rxmedical.api.controller;

import com.rxmedical.api.model.response.ApiResponse;
import com.rxmedical.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.rxmedical.api.model.dto.UserInfoDto;
import com.rxmedical.api.model.dto.UserLoginDto;

@RestController
@RequestMapping("/api/user")
public class UserController {

	@Autowired
	private UserService userService;
	
	@GetMapping("/test")
	public String getTest() {
		return "User API 連接成功";
	}
		
	// 判斷使用者登入
	@PostMapping("/login")
	public ApiResponse<UserInfoDto> postUserLogin(@RequestBody UserLoginDto userLoginDto) {
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
		return response;
	}
	
	// 登出
	public void getLogout() {
		// TODO: 請實作
	}
	
	// 註冊資料寫入DB
	public void addUserInfo(UserInfoDto userInfoDto) {
		// TODO: 請實作
	}
	
	// 取得個人資訊
	public void getUserInfo(UserInfoDto userInfoDto) {
		// TODO: 請實作
	}
	
	// 修改個人資料
	public void editUserInfo(UserInfoDto userInfoDto) {
		// TODO: 請實作
	}
}
