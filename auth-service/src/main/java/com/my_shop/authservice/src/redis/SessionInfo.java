package com.my_shop.authservice.src.redis;

import lombok.Data;

import java.io.Serializable;

@Data
public class SessionInfo implements Serializable {
    private String sessionId;
    private String userId;
    private String refreshToken;

    private String ipAddress;
    private String userAgent;

    private Long issuedAt;
    private Long expiresAt;

    private boolean used;
    private boolean revoked;

    private String location;
    private String deviceId;

    public SessionInfo() {}

    public SessionInfo(String sessionId,
                       String userId,
                       String refreshToken,
                       String ipAddress,
                       String userAgent, Long issuedAt,
                       Long expiresAt, boolean used,
                       boolean revoked,
                       String location,
                       String deviceId) {
        this.sessionId = sessionId;
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.ipAddress = ipAddress;
        this.userAgent = userAgent;
        this.issuedAt = issuedAt;
        this.expiresAt = expiresAt;
        this.used = used;
        this.revoked = revoked;
        this.location = location;
        this.deviceId = deviceId;
    }
}
