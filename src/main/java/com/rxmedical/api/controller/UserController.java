package com.rxmedical.api.controller;

import com.rxmedical.api.model.dto.*;
import com.rxmedical.api.model.response.ApiResponse;
import com.rxmedical.api.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.util.List;

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
	public ResponseEntity<ApiResponse<UserInfoDto>> postUserLogin(@RequestBody UserLoginDto userLoginDto) throws NoSuchAlgorithmException {
		
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
	
	// 註冊
	@PostMapping("/user/register")
	public ResponseEntity<ApiResponse<Object>> registerUserInfo(@RequestBody UserRegisterDto userRegisterDto) throws NoSuchAlgorithmException {

		Boolean registerSuccess = userService.registerUserInfo(userRegisterDto);
		
		if (!registerSuccess) {
			return ResponseEntity.ok(new ApiResponse<>(false, "註冊失敗", null));
		}
		return ResponseEntity.ok(new ApiResponse<>(true, "註冊成功", null));
	}
	
	// 取得個人資訊
	@PostMapping("/user/profile")
	public ResponseEntity<ApiResponse<UserInfoDto>> getUserInfo(@RequestBody Integer userId) {
		UserInfoDto info = userService.getUserInfo(userId);
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
	}

	// 後台查詢所有使用者
	@PostMapping("/admin/member")
	public ResponseEntity<ApiResponse<List<MemberInfoDto>>> getMemberList(@RequestBody Integer userId) {
		List<MemberInfoDto> memberList = userService.getMemberList(userId);
		if (memberList == null) {
			return ResponseEntity.ok(new ApiResponse<>(false, "權限不足", null));
		}
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
}
