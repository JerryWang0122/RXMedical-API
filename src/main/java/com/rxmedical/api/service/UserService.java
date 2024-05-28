package com.rxmedical.api.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;

import com.rxmedical.api.model.dto.*;
import com.rxmedical.api.util.KeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.rxmedical.api.model.po.User;
import com.rxmedical.api.repository.UserRepository;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

	/**
	 * [檢測] 檢驗使用者登入資料
	 *
	 * @param userLoginDto 使用者登入資料
	 * @return UserInfoDto 使用者資料
	 * @throws NoSuchAlgorithmException
	 */
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
     * @param userRegisterDto 註冊人註冊資料
     * @return Boolean 註冊是否成功
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

	/**
	 * [搜索] 取得使用者個人帳戶資料
	 * @param userId 使用者id
	 * @return UserInfoDto 個人帳戶資料
	 */
    public UserInfoDto getUserInfo(Integer userId) {
		Optional<User> optionalUser = userRepository.findById(userId);
		if (optionalUser.isPresent()) {
			User u = optionalUser.get();
			return new UserInfoDto(u.getId(), u.getEmpCode(), u.getName(),
								   u.getDept(), u.getTitle(), u.getEmail(),
								   u.getAuthLevel());
		}
		return null;
    }

	/**
	 * [更新] 更新使用者個人帳戶資料
	 * @param userEditInfoDto 使用者更新後的個人資訊
	 * @return UserInfoDto 更新後的個人資訊
	 */
	public UserInfoDto updateUserInfo(UserEditInfoDto userEditInfoDto) {

		// root 的資料不可被更改
		if (userEditInfoDto == null || userEditInfoDto.id().equals(0)) {
			return null;
		}

		// 更新資料
		Optional<User> optionalUser = userRepository.findById(userEditInfoDto.id());
		if (optionalUser.isPresent()) {
			User u = optionalUser.get();
			u.setName(userEditInfoDto.name());
			u.setDept(userEditInfoDto.dept());
			u.setTitle(userEditInfoDto.title());
			u.setEmail(userEditInfoDto.email());
			userRepository.save(u);
			return new UserInfoDto(u.getId(), u.getEmpCode(), u.getName(),
								   u.getDept(), u.getTitle(), u.getEmail(),
								   u.getAuthLevel());
		}
		return null;
	}

	/**
	 * [後台 - 查詢] 後台查詢所有使用者的資料
	 * @param userId 查詢者的id，要先做權限驗證
	 * @return List<MemberInfoDto> 會員資訊
	 */
	public List<MemberInfoDto> getMemberList(Integer userId) {
		// userId 查詢者的ID，先做權限驗證
		Optional<User> optionalUser = userRepository.findById(userId);
		if (optionalUser.isPresent()) {
			User u = optionalUser.get();
			// 如果是 admin 或 root，可以查詢所有使用者的資料
			if (u.getAuthLevel().equals("admin") || u.getAuthLevel().equals("root")) {
				return userRepository.findAll().stream()
						.map(user -> new MemberInfoDto(user.getId(), user.getEmpCode(), user.getName(),
									user.getDept(), user.getTitle(), user.getAuthLevel(), user.getCreateDate()))
						.toList();
			}
		}
		return null;
	}
}
