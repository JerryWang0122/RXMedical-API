package com.rxmedical.api.aop;

import com.rxmedical.api.model.po.User;
import com.rxmedical.api.model.response.ApiResponse;
import com.rxmedical.api.repository.UserRepository;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import java.util.Optional;

// 檢查使用者是否有後台權限
@Component
@Aspect
public class BackendAuthCheckAOP {

    @Autowired
    private UserRepository userRepository;

    // 設定切點
    // ---------------- User ----------------------
    @Pointcut(value = "execution(* com.rxmedical.api.controller.UserController.getMemberList(..))")
    public void getMemberList(){};


    // 環繞通知(不包括getTest、登入、註冊)
    @Around(value = "getMemberList() ")
    public Object aroundCheckAuth(ProceedingJoinPoint joinPoint) {

        Object result = null;
        Optional<User> optionalUser;

        try {
            System.out.println("後台測試前置");
            // 前置：檢查用戶登入狀態
            Object[] args = joinPoint.getArgs();
            if (args.length > 1) { // 非DTO，而是用多個參數傳入的，則 currUserId 為第一個參數
                optionalUser = userRepository.findById((Integer) args[0]);
            } else { // DTO 裡面應該包含 currUserId
                // 使用反射取得 currUserId
                Integer currUserId = (Integer) args[0].getClass().getDeclaredMethod("userId").invoke(args[0]);
                optionalUser = userRepository.findById(currUserId);
            }

            if (optionalUser.isPresent()){
                User u = optionalUser.get();
                if (u.getAuthLevel().equals("admin") || u.getAuthLevel().equals("root")){
                    result = joinPoint.proceed();
                } else {
                    result = ResponseEntity.ok(new ApiResponse<>(false, "NoAuth", null));
                }
            } else {
                result = ResponseEntity.ok(new ApiResponse<>(false, "LoginFirst", null));
            }

        } catch (Throwable e) {
            e.printStackTrace();
            result = ResponseEntity.ok(new ApiResponse<>(false, "伺服器發生錯誤", null));
        }
        return result;
    }


    @Around(value = "execution(* com.rxmedical.api.controller.UserController.changeMemberAuthLevel(..))")
    public Object changeMemberAuthLevel(ProceedingJoinPoint joinPoint) {

        Object result = null;
        Optional<User> optionalUser;

        try {
            System.out.println("修改會員權限前置");
            // 前置：檢查用戶登入狀態
            Object[] args = joinPoint.getArgs();
            if (args.length > 1) { // 非DTO，而是用多個參數傳入的，則 currUserId 為第一個參數
                optionalUser = userRepository.findById((Integer) args[0]);
            } else { // DTO 裡面應該包含 currUserId
                // 使用反射取得 currUserId
                Integer currUserId = (Integer) args[0].getClass().getDeclaredMethod("userId").invoke(args[0]);
                optionalUser = userRepository.findById(currUserId);
            }

            if (optionalUser.isPresent()){
                User u = optionalUser.get();
                if (u.getAuthLevel().equals("root")){
                    result = joinPoint.proceed();
                } else {
                    result = ResponseEntity.ok(new ApiResponse<>(false, "NoAuth", null));
                }
            } else {
                result = ResponseEntity.ok(new ApiResponse<>(false, "LoginFirst", null));
            }

        } catch (Throwable e) {
            e.printStackTrace();
            result = ResponseEntity.ok(new ApiResponse<>(false, "伺服器發生錯誤", null));
        }
        return result;
    }

}