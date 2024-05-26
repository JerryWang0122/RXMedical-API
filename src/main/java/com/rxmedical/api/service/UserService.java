package com.rxmedical.api.service;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.rxmedical.api.model.dto.UserInfoDto;
import com.rxmedical.api.model.dto.UserLoginDto;
import com.rxmedical.api.model.dto.UserRegisterDto;
import com.rxmedical.api.model.po.User;
import com.rxmedical.api.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    // 檢測使用者登入資料
	public UserInfoDto checkUserLogin(UserLoginDto userLoginDto) {

		if (userLoginDto.email() == null || userLoginDto.password() == null) {
			return null;
		}

		// 用DAO抓使用者資料
		if (userLoginDto.email().equals("school832@gmail.com") && userLoginDto.password().equals("123")) {
			return new UserInfoDto(1, "73174", "王俊傑", "秘書室", "替代役", "school832@gmail.com", "staff");
		}
		if (userLoginDto.email().equals("b06502168@ntu.edu.tw") && userLoginDto.password().equals("123")) {
			return new UserInfoDto(2, "12345", "Test", "TestDept", "TestTitle", "b06502168@ntu.edu.tw", "register");
		}
		return null;

	}

    /**
     * [增加] 使用者資料
     * 
     * @param userRegisterDto
     * @return
     */
    public Boolean registerUserInfo(UserRegisterDto userRegisterDto) {
    	
    	User user = new User();
    	boolean result;
        
    	// 轉成 PO
    	user.setEmpCode(userRegisterDto.empId());
    	user.setName(userRegisterDto.name());
    	user.setDept(userRegisterDto.dept());
    	user.setTitle(userRegisterDto.title());
    	user.setEmail(userRegisterDto.email());
    	user.setPassword(userRegisterDto.password());
    	user.setCreateDate(new Date());
    	// 存檔
    	result = userRepository.save(user) != null;
    	
        return result;
    }

    // 取得使用者個人帳戶資料
    public UserInfoDto getUserInfo(Integer userId) {
        // 用DAO抓使用者資料
        if (userId == 1) {
            return new UserInfoDto(1, "73174", "王俊傑", "秘書室", "替代役", "school832@gmail.com", "staff");
        }
        if (userId == 2) {
            return new UserInfoDto(2, "12345", "Test", "TestDept", "TestTitle", "b06502168@ntu.edu.tw", "register");
        }
        return null;
    }
}
