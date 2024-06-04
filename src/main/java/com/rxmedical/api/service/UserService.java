package com.rxmedical.api.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import com.rxmedical.api.model.dto.*;
import com.rxmedical.api.model.po.History;
import com.rxmedical.api.model.po.Record;
import com.rxmedical.api.repository.HistoryRepository;
import com.rxmedical.api.repository.ProductRepository;
import com.rxmedical.api.repository.RecordRepository;
import com.rxmedical.api.util.EmailUtil;
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

	@Autowired
	private RecordRepository recordRepository;

	@Autowired
	private HistoryRepository historyRepository;

	/**
	 * [前台 - 檢測] 檢驗使用者登入資料
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
     * [前台 - 增加] 使用者資料
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
	 * [前台 - 搜索] 取得使用者個人帳戶資料
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
	 * [前台 - 更新] 更新使用者個人帳戶資料
	 * @param userEditInfoDto 使用者更新後的個人資訊
	 * @return UserInfoDto 更新後的個人資訊
	 */
	public UserInfoDto updateUserInfo(UserEditInfoDto userEditInfoDto) {

		// root 的資料不可被更改
		if (userEditInfoDto == null || userEditInfoDto.userId().equals(0)) {
			return null;
		}

		// 更新資料
		Optional<User> optionalUser = userRepository.findById(userEditInfoDto.userId());
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
	 * [前台 - 搜索] 取得使用者個人帳戶資料
	 * @param userId 使用者id
	 * @return List<PurchaseHistoryDto> 使用者歷史申請資料
	 */
	public List<PurchaseHistoryDto> getUserPurchaseHistoryList(Integer userId) {
		Optional<User> optionalUser = userRepository.findById(userId);
		if (optionalUser.isEmpty()) {
			return null;
		}
		List<Record> recordList = recordRepository.findByDemander(optionalUser.get());
		return recordList.stream()
				.map(r -> new PurchaseHistoryDto(
										r.getId(),
										r.getCode(),
										historyRepository.countByRecord(r),
										r.getChineseStatus()
										))
				.toList();

	}

	/**
	 * [前台] 取得訂單明細
	 * @param recordDto 查詢訂單資料，誰查詢、查哪筆
	 * @return (null 代表發生錯誤)，List為明細資料
	 */
	public List<OrderDetailDto> getPurchaseDetails(RecordDto recordDto) {
		Optional<Record> optionalRecord = recordRepository.findById(recordDto.recordId());
		if (optionalRecord.isEmpty()){
			return null;
		}

		// 查詢人應該要跟訂購人一樣
		Record r = optionalRecord.get();
		if (!recordDto.userId().equals(r.getDemander().getId())) {
			return null;
		}
		// 給資料
		List<History> recordDetails = historyRepository.findByRecord(r);
		return recordDetails.stream()
				.map(history -> new OrderDetailDto(history.getProduct().getName(), history.getQuantity(), null))
				.toList();
	}

	/**
	 * [前台 - 更新] 使用者確定訂單完成
	 * @param recordDto 操作者和要完成的訂單
	 * @return String 錯誤信息
	 */
	public String finishOrder(RecordDto recordDto) {
		Optional<Record> optionalRecord = recordRepository.findById(recordDto.recordId());
		if (optionalRecord.isEmpty()){
			return "找不到訂單";
		}
		Record r = optionalRecord.get();
		if (r.getStatus().equals("finish")) {
			return "訂單已經完成";
		}
		if (!r.getStatus().equals("transporting")) {
			return "錯誤訂單狀態";
		}
		if (!recordDto.userId().equals(r.getDemander().getId())) {
			return "錯誤操作人員";
		}
		r.setStatus("finish");
		recordRepository.save(r);
		return null;
	}

	/**
	 * [後台 - 查詢] 後台查詢所有使用者的資料
	 * @return List<MemberInfoDto> 會員資訊
	 */
	public List<MemberInfoDto> getMemberList() {
		return userRepository.findAll().stream()
				.map(user -> new MemberInfoDto(user.getId(), user.getEmpCode(), user.getName(),
						user.getDept(), user.getTitle(), user.getAuthLevel(), user.getCreateDate()))
				.toList();
	}

	/**
	 * [後台 root - 調整] 調整會員權限
	 * @param memberAuthDto	欲調整的資訊，誰被調整成什麼等級
	 * @return Boolean 是否調整成功
	 */
	public Boolean updateMemberAuthLevel(ChangeMemberAuthDto memberAuthDto) {
		// 不能修改root權限
		if (memberAuthDto.memberId().equals(1)) {
			return false;
		}
		// 修改權限不可為空
		if (memberAuthDto.authLevel() == null) {
			return false;
		}
		// 不能將任何人調成root權限，或是主動調成註冊狀態
		if (memberAuthDto.authLevel().equals("root") || memberAuthDto.authLevel().equals("register")) {
			return false;
		}

		// 更新資料
		Optional<User> optionalUser = userRepository.findById(memberAuthDto.memberId());
		if (optionalUser.isPresent()) {
			User u = optionalUser.get();
			if (u.getAuthLevel().equals("register")) {
				// 寄一封通知信
				ExecutorService executorService = Executors.newSingleThreadExecutor();
				executorService.execute(() -> {
					EmailUtil.prepareAndSendEmail(u.getEmail());
				});
			}
			u.setAuthLevel(memberAuthDto.authLevel());
			userRepository.save(u);
			return true;
		}
		return false;
	}

	/**
	 * [輔助、後台 - 搜索] 提供"待運送"清單狀態的運送人員
	 * @return List 運送人員(admin)
	 */
	public List<TransporterDto> getTransporterList() {
		return userRepository.findByAuthLevel("admin").stream()
				.map(user -> new TransporterDto(user.getId(), user.getEmpCode(), user.getName()))
				.toList();
	}

}
