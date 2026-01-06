package com.saju.service;

import com.saju.domain.User;
import com.saju.dto.AuthResponse;
import com.saju.dto.KakaoTokenResponse;
import com.saju.dto.KakaoUserInfo;
import com.saju.mapper.UserMapper;
import com.saju.util.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;


@Service
public class KakaoAuthService {

    @Value("${kakao.client-id}")
    private String clientId;

    @Value("${kakao.client-secret}")
    private String clientSecret;

    @Value("${kakao.redirect-uri}")
    private String redirectUri;

    private final RestTemplate restTemplate = new RestTemplate();
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    public KakaoAuthService(UserMapper userMapper, JwtUtil jwtUtil) {
        this.userMapper = userMapper;
        this.jwtUtil = jwtUtil;
    }

    public AuthResponse authenticate(String code) {
        // 1. 카카오 인가 코드로 액세스 토큰 받기
        String accessToken = getKakaoAccessToken(code);

        // 2. 액세스 토큰으로 사용자 정보 가져오기
        KakaoUserInfo userInfo = getKakaoUserInfo(accessToken);

        // 3. 사용자 저장 또는 업데이트
        User user = saveOrUpdateUser(userInfo);

        // 4. JWT 토큰 생성
        String jwtToken = jwtUtil.generateToken(user.getId());

        return new AuthResponse(jwtToken, user.getId(), user.getNickname());
    }

    private String getKakaoAccessToken(String code) {
        String tokenUrl = "https://kauth.kakao.com/oauth/token";

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);

        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
        params.add("grant_type", "authorization_code");
        params.add("client_id", clientId);
        params.add("client_secret", clientSecret);
        params.add("redirect_uri", redirectUri);
        params.add("code", code);

        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);

        try {
            ResponseEntity<KakaoTokenResponse> response = restTemplate.postForEntity(
                    tokenUrl, request, KakaoTokenResponse.class);
            
            if (response.getBody() == null || response.getBody().getAccessToken() == null) {
                throw new RuntimeException("카카오 액세스 토큰을 받을 수 없습니다.");
            }
            
            return response.getBody().getAccessToken();
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            throw new RuntimeException("카카오 인증 실패: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("카카오 토큰 요청 중 오류 발생: " + e.getMessage(), e);
        }
    }

    private KakaoUserInfo getKakaoUserInfo(String accessToken) {
        String userInfoUrl = "https://kapi.kakao.com/v2/user/me";

        HttpHeaders headers = new HttpHeaders();
        headers.set("Authorization", "Bearer " + accessToken);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<String> request = new HttpEntity<>(headers);

        try {
            ResponseEntity<KakaoUserInfo> response = restTemplate.exchange(
                    userInfoUrl, HttpMethod.GET, request, KakaoUserInfo.class);
            
            if (response.getBody() == null) {
                throw new RuntimeException("카카오 사용자 정보를 가져올 수 없습니다.");
            }
            
            return response.getBody();
        } catch (org.springframework.web.client.HttpClientErrorException e) {
            throw new RuntimeException("카카오 사용자 정보 조회 실패: " + e.getStatusCode() + " - " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new RuntimeException("카카오 사용자 정보 조회 중 오류 발생: " + e.getMessage(), e);
        }
    }

    private User saveOrUpdateUser(KakaoUserInfo userInfo) {
        if (userInfo == null || userInfo.getId() == null) {
            throw new RuntimeException("카카오 사용자 정보를 가져올 수 없습니다.");
        }

        String provider = "KAKAO";
        String userId = String.valueOf(userInfo.getId());
        User existingUser = userMapper.findByProviderAndUserId(provider, userId);

        // 닉네임 가져오기 (properties 또는 kakao_account.profile에서)
        String nickname = "사용자"; // 기본값
        if (userInfo.getProperties() != null && userInfo.getProperties().getNickname() != null) {
            nickname = userInfo.getProperties().getNickname();
        } else if (userInfo.getKakaoAccount() != null 
                && userInfo.getKakaoAccount().getProfile() != null 
                && userInfo.getKakaoAccount().getProfile().getNickname() != null) {
            nickname = userInfo.getKakaoAccount().getProfile().getNickname();
        }

        if (existingUser != null) {
            // 기존 사용자 정보 업데이트
            existingUser.setNickname(nickname);
            userMapper.update(existingUser);
            return existingUser;
        } else {
            // 새 사용자 생성
            User newUser = new User();
            newUser.setProvider(provider);
            newUser.setUserId(userId);
            newUser.setNickname(nickname);
            userMapper.insert(newUser);
            return newUser;
        }
    }
}
