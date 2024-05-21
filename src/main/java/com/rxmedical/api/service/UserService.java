package com.rxmedical.api.service;

import com.rxmedical.api.model.dto.UserInfoDto;
import com.rxmedical.api.model.dto.UserLoginDto;
import com.rxmedical.api.model.dto.UserRegisterDto;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    // 檢測使用者登入資料
    public UserInfoDto checkUserLogin(UserLoginDto userLoginDto) {
        System.out.println(userLoginDto);
        if (userLoginDto.getEmail() == null || userLoginDto.getPassword() == null) {
            return null;
        }

        // TODO: 用DAO抓使用者資料
        if (userLoginDto.getEmail().equals("school832@gmail.com") && userLoginDto.getPassword().equals("123")) {
            return new UserInfoDto(1, "73174", "王俊傑", "秘書室", "替代役", "school832@gmail.com", "staff");
        }
        if (userLoginDto.getEmail().equals("b06502168@ntu.edu.tw") && userLoginDto.getPassword().equals("123")) {
            return new UserInfoDto(2, "12345", "Test", "TestDept", "TestTitle", "b06502168@ntu.edu.tw", "register");
        }
        return null;

    }

    // 使用者資料註冊
    public Boolean registerUserInfo(UserRegisterDto userRegisterDto) {
        // TODO: 用DAO寫入使用者資料
        System.out.printf("寫入使用者資料%n%s%n%s%n%s%n%s%n%s%n%s",
                          userRegisterDto.getEmpId(),
                          userRegisterDto.getName(),
                          userRegisterDto.getDept(),
                          userRegisterDto.getTitle(),
                          userRegisterDto.getEmail(),
                          userRegisterDto.getPassword());
        return true;
    }

    // 取得使用者個人帳戶資料
    public UserInfoDto getUserInfo(Integer userId) {
        // TODO: 用DAO抓使用者資料
        if (userId == 1) {
            return new UserInfoDto(1, "73174", "王俊傑", "秘書室", "替代役", "school832@gmail.com", "staff");
        }
        if (userId == 2) {
            return new UserInfoDto(2, "12345", "Test", "TestDept", "TestTitle", "b06502168@ntu.edu.tw", "register");
        }
        return null;
    }
}
