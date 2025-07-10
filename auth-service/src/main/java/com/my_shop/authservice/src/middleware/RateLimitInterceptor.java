package com.my_shop.authservice.src.middleware;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.lang.NonNull;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import java.time.Instant;
import java.util.Deque;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedDeque;

@Component
@ConfigurationProperties(prefix = "rate-limit")
public class RateLimitInterceptor implements HandlerInterceptor {

    // Lưu lịch sử req theo ip
    private final ConcurrentHashMap<String, Deque<Long>> requestHistory = new ConcurrentHashMap<>();

    @Value("${rate-limit.limit}")
    private int LIMIT; // req tối đa
    @Value("${rate-limit.window-seconds}")
    private int WINDOW_SECONDS; // thời gian phản hồi tối đa

    @Override
    public boolean preHandle(@NonNull HttpServletRequest request, @NonNull HttpServletResponse response, @NonNull Object handler) throws Exception {
        String clientIp = getClientIp(request);
        long now = Instant.now().getEpochSecond();

        requestHistory.putIfAbsent(clientIp, new ConcurrentLinkedDeque<>());
        Deque<Long> timestamps = requestHistory.get(clientIp);

        synchronized (timestamps) {
            // Xóa các req quá cũ khỏi window
            while  (!timestamps.isEmpty() && (now - timestamps.peekFirst()) >= WINDOW_SECONDS) {
                timestamps.removeFirst();
            }

            // Kiểm tra giới hạn
            if (timestamps.size() >= LIMIT) {
                response.setStatus(429);
                response.getWriter().write("Too many requests - try again later");
                return false;
            }

            // Ghi nhận req mới
            timestamps.addLast(now);
        }

        return true;
    }

    private String getClientIp(HttpServletRequest request) {
        String ip = request.getHeader("X-Forwarded-For");
        return (ip == null || ip.isEmpty()) ? request.getRemoteAddr() : ip;
    }
}
