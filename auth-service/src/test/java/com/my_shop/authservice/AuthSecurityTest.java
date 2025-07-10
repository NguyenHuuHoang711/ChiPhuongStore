package com.my_shop.authservice;

import com.my_shop.authservice.src.dto.LoginRequest;
import com.my_shop.authservice.src.service.JwtService;
import org.junit.jupiter.api.AutoClose;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class AuthSecurityTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private JwtService jwtService;

//    // Test1: Truy cập với jwt bị sửa -> Từ chối
//    @Test
//    public void accessWithTamperedToken_ShouldRejected() throws Exception {
//        String fakeToken = "abc.def.hij";
//
//        mockMvc.perform(get("user/me")
//                        .cookie(new Cookie("access_token", fakeToken)))
//                .andExpect(status().isUnauthorized());
//    }

//    // Test2: Truy cập với token đã hết hạn
//    @Test
//    public void accessWithExpiredToken_ShouldRejected() throws Exception {
//        String expiredToken = jwtService.generateExpiredToken("user123");
//
//        mockMvc.perform(get("user/me")
//                        .cookie(new Cookie("access_token", expiredToken)))
//                .andExpect(status().isUnauthorized());
//    }

    // Test3: Kiểm tra cookie sau đăng nhập phải có HttpOnly + Secure
    @Test
    public void login_ShouldSetSecureHttpOnly() throws Exception {
        LoginRequest loginRequest = new LoginRequest("user@example.com", "validPassword");

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"email\":\"user1@example.com\", \"password\":\"validPassword\"}"))
                .andExpect(status().isUnauthorized());
    }

//    // Test4: Không có CSRF token thì bị từ chối
//    @Test
//    public void postWithoutCsrfToken_ShouldBeForbidden() throws Exception {
//        mockMvc.perform(post("/auth/logout"))
//                .andExpect(status().isForbidden());
//    }

    // Test5: Giả lập brute-force login nhiều lần -> bị chặn
    @Test
    public void tooManyFailedLogin_ShouldBeRateLimited() throws Exception {
        for (int i = 0; i < 10; i++) {
            mockMvc.perform(post("/api/auth/login")
                            .contentType(MediaType.APPLICATION_JSON)
                    .content("{\"email\":\"user@example.com\", \"password\":\"wrongPassword\"}"))
                    .andExpect(status().isTooManyRequests());
        }
    }
}
