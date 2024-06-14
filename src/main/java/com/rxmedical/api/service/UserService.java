package com.rxmedical.api.service;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Example;
import org.springframework.stereotype.Service;

import com.nimbusds.jose.JOSEException;
import com.rxmedical.api.model.dto.ChangeMemberAuthDto;
import com.rxmedical.api.model.dto.MemberInfoDto;
import com.rxmedical.api.model.dto.OrderDetailDto;
import com.rxmedical.api.model.dto.PurchaseHistoryDto;
import com.rxmedical.api.model.dto.RecordDto;
import com.rxmedical.api.model.dto.TransporterDto;
import com.rxmedical.api.model.dto.UserEditInfoDto;
import com.rxmedical.api.model.dto.UserInfoDto;
import com.rxmedical.api.model.dto.UserLoginDto;
import com.rxmedical.api.model.dto.UserRegisterDto;
import com.rxmedical.api.model.dto.UserUsageDto;
import com.rxmedical.api.model.po.History;
import com.rxmedical.api.model.po.Record;
import com.rxmedical.api.model.po.User;
import com.rxmedical.api.repository.HistoryRepository;
import com.rxmedical.api.repository.RecordRepository;
import com.rxmedical.api.repository.UserRepository;
import com.rxmedical.api.util.EmailUtil;
import com.rxmedical.api.util.KeyUtil;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

	@Autowired
	private RecordRepository recordRepository;

	@Autowired
	private HistoryRepository historyRepository;

	@Autowired
	private JWTService jwtService;


	/**
	 * [前台 - 檢測] 檢驗使用者登入資料
	 * @param userLoginDto 使用者登入資料
	 * @return UserUsageDto 使用者資料
	 * @throws NoSuchAlgorithmException
	 */
	public UserUsageDto checkUserLogin(UserLoginDto userLoginDto) throws NoSuchAlgorithmException, JOSEException {

		if (userLoginDto.email() == null || userLoginDto.password() == null) {
			return null;
		}

		// 查找email對應使用者
		User user = new User();
		user.setEmail(userLoginDto.email());
		Example<User> example = Example.of(user);
		Optional<User> optionalUser = userRepository.findOne(example);

		// 帳號不存在
		if (optionalUser.isEmpty()) {
			return null;
		}

		// 帳號存在
		user = optionalUser.get();

		// hash 傳入的密碼
		MessageDigest messageDigest = MessageDigest.getInstance("SHA-256");
		messageDigest.update(KeyUtil.hexStringToByteArray(user.getSalt()));
		byte[] hashedPassword = messageDigest.digest(userLoginDto.password().getBytes());

		// 比對密碼
		if (!user.getPassword().equals(KeyUtil.bytesToHex(hashedPassword))){
			return null;
		}

		// 且密碼正確
		String usageJWT = jwtService.getUserUsageJWT(user);
		return new UserUsageDto(user.getDept(), user.getName(), user.getAuthLevel(), usageJWT);

	}

    /**
     * [前台 - 增加] 使用者資料
     * @param userRegisterDto 註冊人註冊資料
     * @return Boolean 註冊是否成功
     */
    public Boolean registerUserInfo(UserRegisterDto userRegisterDto) throws NoSuchAlgorithmException {

		// Register User
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
		User user = findUserById(userId);
		if (user != null) {
			return new UserInfoDto(
					user.getId(),
					user.getEmpCode(),
					user.getName(),
				    user.getDept(),
					user.getTitle(),
					user.getEmail(),
				    user.getAuthLevel()
			);
		}
		return null;
    }

	/**
	 * [前台 - 更新] 更新使用者個人帳戶資料
	 * @param userEditInfoDto 使用者更新後的個人資訊
	 * @return 更新後的部分個人資訊
	 */
	public UserUsageDto updateUserInfo(UserEditInfoDto userEditInfoDto) {

		// root 的資料不可被更改
		if (userEditInfoDto == null || userEditInfoDto.getUserId().equals(0)) {
			return null;
		}

		// 更新資料
		User user = findUserById(userEditInfoDto.getUserId());
		if (user != null) {
			user.setName(userEditInfoDto.getName());
			user.setDept(userEditInfoDto.getDept());
			user.setTitle(userEditInfoDto.getTitle());
			user.setEmail(userEditInfoDto.getEmail());
			userRepository.save(user);
			return new UserUsageDto(user.getDept(), user.getName(), user.getAuthLevel(), null);
		}
		return null;
	}

	/**
	 * [前台 - 搜索] 取得使用者個人帳戶資料
	 * @param userId 使用者id
	 * @return List<PurchaseHistoryDto> 使用者歷史申請資料
	 */
	public List<PurchaseHistoryDto> getUserPurchaseHistoryList(Integer userId) {
		User user = findUserById(userId);
		if (user == null) {
			return null;
		}

		List<Record> recordList = recordRepository.findByDemander(user);
		return recordList.stream()
				.map(record -> new PurchaseHistoryDto(
										record.getId(),
										record.getCode(),
										historyRepository.countByRecord(record),
										record.getChineseStatus()
										))
				.toList();

	}

	/**
	 * [前台] 取得訂單明細
	 * @param recordDto 查詢訂單資料，誰查詢、查哪筆
	 * @return (null 代表發生錯誤)，List為明細資料
	 */
	public List<OrderDetailDto> getPurchaseDetails(RecordDto recordDto) {
		Optional<Record> optionalRecord = recordRepository.findById(recordDto.getRecordId());
		if (optionalRecord.isEmpty()){
			return null;
		}

		// 查詢人應該要跟訂購人一樣
		Record record = optionalRecord.get();
		if (!recordDto.getUserId().equals(record.getDemander().getId())) {
			return null;
		}
		// 給資料
		List<History> recordDetails = historyRepository.findByRecord(record);
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
		Optional<Record> optionalRecord = recordRepository.findById(recordDto.getRecordId());
		if (optionalRecord.isEmpty()){
			return "找不到訂單";
		}
		Record record = optionalRecord.get();
		if (record.getStatus().equals("finish")) {
			return "訂單已經完成";
		}
		if (!record.getStatus().equals("transporting")) {
			return "錯誤訂單狀態";
		}
		if (!recordDto.getUserId().equals(record.getDemander().getId())) {
			return "錯誤操作人員";
		}
		record.setStatus("finish");
		recordRepository.save(record);
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
		if (memberAuthDto.getMemberId().equals(1)) {
			return false;
		}
		// 修改權限不可為空
		if (memberAuthDto.getAuthLevel() == null) {
			return false;
		}
		// 不能將任何人調成root權限，或是主動調成註冊狀態
		if (memberAuthDto.getAuthLevel().equals("root") || memberAuthDto.getAuthLevel().equals("register")) {
			return false;
		}
		User user = findUserById(memberAuthDto.getMemberId());

		if (user != null) {
			if (user.getAuthLevel().equals("register")) {
				// 寄一封通知信
				ExecutorService executorService = Executors.newSingleThreadExecutor();
				executorService.execute(() -> EmailUtil.prepareAndSendEmail(user.getEmail()));
			}
			user.setAuthLevel(memberAuthDto.getAuthLevel());
			userRepository.save(user);
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

	/**
	 * [輔助] 利用id找到使用者
	 * @param id userId
	 * @return 使用者資料PO
	 */
	public User findUserById(Integer id) {
		Optional<User> optionalUser = userRepository.findById(id);
        return optionalUser.orElse(null);
    }

}
