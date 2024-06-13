package com.rxmedical.api.aop;

import java.lang.reflect.Method;
import java.util.Map;

import com.rxmedical.api.service.JWTService;
import jakarta.servlet.http.HttpServletRequest;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;

import com.rxmedical.api.model.response.ApiResponse;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

// 檢查使用者是否有後台權限
@Component
@Aspect
public class BackendAuthCheckAOP {

    @Autowired
    private JWTService jwtService;

    // 設定切點
    // ---------------- User ----------------------
    @Pointcut(value = "execution(* com.rxmedical.api.controller.UserController.getMemberList(..))")
    public void getMemberList(){}

    @Pointcut(value = "execution(* com.rxmedical.api.controller.UserController.getTransporterList(..))")
    public void getTransporterList(){}

    // ---------------- Product -------------------
    @Pointcut(value = "execution(* com.rxmedical.api.controller.ProductController.materialInfoUpload(..))")
    public void materialInfoUpload(){}

    @Pointcut(value = "execution(* com.rxmedical.api.controller.ProductController.getMaterialList(..))")
    public void getMaterialList(){}

    @Pointcut(value = "execution(* com.rxmedical.api.controller.ProductController.getMaterialInfo(..))")
    public void getMaterialInfo(){}

    @Pointcut(value = "execution(* com.rxmedical.api.controller.ProductController.materialInfoUpdate(..))")
    public void materialInfoUpdate(){}

    // ---------------- Sale ----------------------
    @Pointcut(value = "execution(* com.rxmedical.api.controller.SaleController.callMaterial(..))")
    public void callMaterial(){}

    @Pointcut(value = "execution(* com.rxmedical.api.controller.SaleController.destroyMaterial(..))")
    public void destroyMaterial(){}

    @Pointcut(value = "execution(* com.rxmedical.api.controller.SaleController.getOrderDetails(..))")
    public void getOrderDetails(){}

    @Pointcut(value = "execution(* com.rxmedical.api.controller.SaleController.getUncheckedOrderList(..))")
    public void getUncheckedOrderList(){}

    @Pointcut(value = "execution(* com.rxmedical.api.controller.SaleController.getPickingOrderList(..))")
    public void getPickingOrderList(){}

    @Pointcut(value = "execution(* com.rxmedical.api.controller.SaleController.getWaitingOrderList(..))")
    public void getWaitingOrderList(){}

    @Pointcut(value = "execution(* com.rxmedical.api.controller.SaleController.getTransportingOrderList(..))")
    public void getTransportingOrderList(){}

    @Pointcut(value = "execution(* com.rxmedical.api.controller.SaleController.getFinishOrderList(..))")
    public void getFinishOrderList(){}

    @Pointcut(value = "execution(* com.rxmedical.api.controller.SaleController.getRejectedOrderList(..))")
    public void getRejectedOrderList(){}

    @Pointcut(value = "execution(* com.rxmedical.api.controller.SaleController.pushToPicking(..))")
    public void pushToPicking(){}

    @Pointcut(value = "execution(* com.rxmedical.api.controller.SaleController.pushToRejected(..))")
    public void pushToRejected(){}

    @Pointcut(value = "execution(* com.rxmedical.api.controller.SaleController.getHistoryProductList(..))")
    public void getHistoryProductList(){}

    @Pointcut(value = "execution(* com.rxmedical.api.controller.SaleController.pickUpItem(..))")
    public void pickUpItem(){}

    @Pointcut(value = "execution(* com.rxmedical.api.controller.SaleController.pushToWaiting(..))")
    public void pushToWaiting(){}

    @Pointcut(value = "execution(* com.rxmedical.api.controller.SaleController.pushToTransporting(..))")
    public void pushToTransporting(){}

    // ------------------- Analyze ------------------

    @Pointcut(value = "execution(* com.rxmedical.api.controller.AnalyzeController.getLaborScore(..))")
    public void getLaborScore(){}

    @Pointcut(value = "execution(* com.rxmedical.api.controller.AnalyzeController.getMaterialSafetyRatio(..))")
    public void getMaterialSafetyRatio(){}



    // ---------------- 開切 -------------------------
    // 環繞通知(不包括getTest、登入、註冊)
    @Around(value = "getMemberList() || getTransporterList() ||" +
            "getMaterialList() || materialInfoUpload() || getMaterialInfo() || materialInfoUpdate() ||" +
            "callMaterial() || destroyMaterial() || getOrderDetails() || getUncheckedOrderList() || pushToPicking() ||" +
            "pushToRejected() || getRejectedOrderList() || getPickingOrderList() || getHistoryProductList() ||" +
            "pickUpItem() || pushToWaiting() || getWaitingOrderList() || pushToTransporting() || getTransportingOrderList() ||" +
            "getFinishOrderList() ||" +
            "getLaborScore() || getMaterialSafetyRatio()")
    public Object aroundCheckAuth(ProceedingJoinPoint joinPoint) {

        Object result = null;

        try {
            System.out.println("測試前置");

            // 驗證JWT
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            Map<String, Object> userInfoMap = jwtService.verifyUserUsageJWT(request);
            if (userInfoMap == null) {  // 驗證失敗
                result = ResponseEntity.ok(new ApiResponse<>(false, "JWT Verify Error", null));
            } else if (!("admin".equals(userInfoMap.get("authLevel")) || "root".equals(userInfoMap.get("authLevel")))) { // 權限不足
                result = ResponseEntity.ok(new ApiResponse<>(false, "權限錯誤", null));
            } else {  // 驗證成功
                Object[] args = joinPoint.getArgs();
                if (args.length != 0){
                    System.out.println(args[0]);
                    Method setUserId = args[0].getClass().getDeclaredMethod("setUserId", Integer.class);
                    setUserId.invoke(args[0], userInfoMap.get("userId"));
                }

                result = joinPoint.proceed();
            }

        } catch (Throwable e) {
            e.printStackTrace();
            result = ResponseEntity.ok(new ApiResponse<>(false, "伺服器發生錯誤", null));
        }
        return result;
    }

    // 只有root能修改使用者權限
    @Around(value = "execution(* com.rxmedical.api.controller.UserController.changeMemberAuthLevel(..))")
    public Object changeMemberAuthLevel(ProceedingJoinPoint joinPoint) {

        Object result = null;

        try {
            System.out.println("測試前置");

            // 驗證JWT
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            Map<String, Object> userInfoMap = jwtService.verifyUserUsageJWT(request);
            if (userInfoMap == null) {  // 驗證失敗
                result = ResponseEntity.ok(new ApiResponse<>(false, "JWT Verify Error", null));
            } else if (!"root".equals(userInfoMap.get("authLevel"))) { // 權限不足
                result = ResponseEntity.ok(new ApiResponse<>(false, "權限錯誤", null));
            } else {  // 驗證成功
                Object[] args = joinPoint.getArgs();
                if (args.length != 0){
                    System.out.println(args[0]);
                    Method setUserId = args[0].getClass().getDeclaredMethod("setUserId", Integer.class);
                    setUserId.invoke(args[0], userInfoMap.get("userId"));
                }

                result = joinPoint.proceed();
            }

        } catch (Throwable e) {
            e.printStackTrace();
            result = ResponseEntity.ok(new ApiResponse<>(false, "伺服器發生錯誤", null));
        }
        return result;
    }
}
