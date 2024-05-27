package com.rxmedical.api.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Date;
import java.util.Optional;

import com.rxmedical.api.util.KeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
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
	public UserInfoDto checkUserLogin(UserLoginDto userLoginDto) throws NoSuchAlgorithmException {

		if (userLoginDto.email() == null || userLoginDto.password() == null) {
			return null;
		}
		// 查找email對應使用者
		User u = new User();
		u.setEmail(userLoginDto.email());
		Example<User> example = Example.of(u);
		Optional<User> optionalUser = userRepository.findOne(example);

		// 帳號存在
		if (optionalUser.isPresent()) {
			u = optionalUser.get();

			// hash 傳入的密碼
			MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
			messageDigest.update(KeyUtil.hexStringToByteArray(u.getSalt()));
			byte[] hashedPassword = messageDigest.digest(userLoginDto.password().getBytes());

			// 比對密碼
			// 且密碼正確
			if (u.getPassword().equals(KeyUtil.bytesToHex(hashedPassword))){
				return new UserInfoDto(u.getId(), u.getEmpCode(), u.getName(),
									   u.getDept(), u.getTitle(), u.getEmail(),
									   u.getAuthLevel());
			}
		}
		return null;
	}

    /**
     * [增加] 使用者資料
     * 
     * @param userRegisterDto
     * @return
     */
    public Boolean registerUserInfo(UserRegisterDto userRegisterDto) throws NoSuchAlgorithmException {
    	
    	User user = new User();
    	boolean result;
        
    	// 轉成 PO
    	user.setEmpCode(userRegisterDto.empCode());
    	user.setName(userRegisterDto.name());
    	user.setDept(userRegisterDto.dept());
    	user.setTitle(userRegisterDto.title());
    	user.setEmail(userRegisterDto.email());
		user.setAuthLevel("register");

		// ----- 密碼加密 --------
		// 產生鹽
		byte[] salt = new byte[16];
		SecureRandom secureRandom = new SecureRandom();
		secureRandom.nextBytes(salt);
		user.setSalt(KeyUtil.bytesToHex(salt));
		// hash 密碼
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.update(salt);
		byte[] hashedPassword = messageDigest.digest(userRegisterDto.password().getBytes());
		user.setPassword(KeyUtil.bytesToHex(hashedPassword));
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
