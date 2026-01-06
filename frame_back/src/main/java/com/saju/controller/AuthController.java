package com.saju.controller;

import com.saju.dto.AuthResponse;
import com.saju.dto.KakaoAuthRequest;
import com.saju.service.KakaoAuthService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "http://localhost:5173")
public class AuthController {

    private final KakaoAuthService kakaoAuthService;

    public AuthController(KakaoAuthService kakaoAuthService) {
        this.kakaoAuthService = kakaoAuthService;
    }

    @PostMapping("/kakao")
    public ResponseEntity<AuthResponse> kakaoLogin(@RequestBody KakaoAuthRequest request) {
        if (request.getCode() == null || request.getCode().isEmpty()) {
            throw new RuntimeException("인가 코드가 없습니다.");
        }
        AuthResponse response = kakaoAuthService.authenticate(request.getCode());
        return ResponseEntity.ok(response);
    }
}
