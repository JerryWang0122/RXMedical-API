package org.rxmedical.api.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user/api")
public class UserController {
	
	@GetMapping("/test")
	public String getTest() {
		return "User API 連接成功";
	}
		
	// 判斷使用者登入
	public void getLogin() {
		// TODO: 請實作
	}
	
	// 登出
	public void getLoguot() {
		// TODO: 請實作
	}
	
	// 註冊資料寫入DB
	public void addUserInfo() {
		// TODO: 請實作
	}
	
	// 取得個人資訊
	public void getUserInfo() {
		// TODO: 請實作
	}
	
	// 修改個人資料
	public void editUserInfo() {
		// TODO: 請實作
	}
}
