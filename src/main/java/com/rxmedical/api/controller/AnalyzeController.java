package com.rxmedical.api.controller;

import com.rxmedical.api.model.dto.CurrUserDto;
import com.rxmedical.api.model.dto.SafetyRatioDto;
import com.rxmedical.api.model.response.ApiResponse;
import com.rxmedical.api.service.AnalyzeService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@CrossOrigin
@RequestMapping("/api/analyze")
public class AnalyzeController {

    @Autowired
    private AnalyzeService analyzeService;

    @GetMapping("/test")
    public String getTest() {
        return "Analyze API 連接成功";
    }

    // 後台取得勞動積分
    @PostMapping("/laborScore")
    public ResponseEntity<ApiResponse<Map<String, Integer>>> getLaborScore(@RequestBody CurrUserDto user) {
        return ResponseEntity.ok(new ApiResponse<>(true, "勞動積分", analyzeService.getLaborScore()));
    }

    // 後台取得庫存safety threshold ratio
    @PostMapping("/materialSafetyRatio")
    public ResponseEntity<ApiResponse<List<SafetyRatioDto>>> getMaterialSafetyRatio(@RequestBody CurrUserDto user) {
        return ResponseEntity.ok(new ApiResponse<>(true, "庫存安全比例", analyzeService.getMaterialSafetyRatio()));
    }
}
