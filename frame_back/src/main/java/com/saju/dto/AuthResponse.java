package com.saju.dto;

public class AuthResponse {
    private String accessToken;
    private Long userId;
    private String nickname;

    public AuthResponse() {
    }

    public AuthResponse(String accessToken, Long userId, String nickname) {
        this.accessToken = accessToken;
        this.userId = userId;
        this.nickname = nickname;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }
}
