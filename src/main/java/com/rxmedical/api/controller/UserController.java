package com.rxmedical.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.rxmedical.api.model.dto.UserInfoDto;
import com.rxmedical.api.model.dto.UserLoginDto;

@RestController
@RequestMapping("/user/api")
public class UserController {
	
	@GetMapping("/test")
	public String getTest() {
		return "User API 連接成功";
	}
		
	// 判斷使用者登入
	public void getUserInfo(UserLoginDto userLoginDto) {
		// TODO: 請實作
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
