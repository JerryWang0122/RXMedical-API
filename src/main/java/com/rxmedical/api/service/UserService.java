package com.rxmedical.api.service;

import com.rxmedical.api.model.dto.UserInfoDto;
import com.rxmedical.api.model.dto.UserLoginDto;
import org.springframework.stereotype.Service;

@Service
public class UserService {
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
            return new UserInfoDto(1, "12345", "Test", "TestDept", "TestTitle", "b06502168@ntu.edu.tw", "register");
        }
        return null;

    }
}
