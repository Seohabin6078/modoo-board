package com.modooboard.exception;

import com.google.gson.Gson;
import com.modooboard.member.dto.MemberDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest
public class MemberExceptionTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private Gson gson;

    // 아래의 테스트처럼 메서드 인자에 @Valid 애너테이션을 사용하여 유효성 검사를 하고 실패하는 경우에
    @Test
    void 이메일_필드에러_예외처리_테스트() throws Exception {
        //given
        MemberDto.Post post =
                MemberDto.Post.builder()
                        .email("hgd")
                        .password("test1234!")
                        .displayName("홍길동")
                        .build();
        String postJson = gson.toJson(post);

        //when
        ResultActions actions = mockMvc.perform(
                post("/members")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postJson)
        );

        //then
        actions
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.fieldErrors").isArray())
                .andExpect(jsonPath("$.fieldErrors[0].field").value("email"));
    }

    @Test
    void 중복회원가입_예외처리_테스트() throws Exception {
        //given
        MemberDto.Post post =
                MemberDto.Post.builder()
                        .email("hgd@gmail.com")
                        .password("test1234!")
                        .displayName("홍길동")
                        .build();
        String postJson = gson.toJson(post);

        mockMvc.perform(
                post("/members")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postJson)
        );

        //when
        ResultActions actions = mockMvc.perform(
                post("/members")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postJson)
        );

        //then
        actions
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.message").value(ExceptionCode.MEMBER_EXISTS.getMessage()));
    }

    @Test
    @WithMockUser // 여기 아래에서 patch를 사용하기 때문
    void HTTP_METHOD_NOT_ALLOWED_예외처리_테스트() throws Exception {
        //given
        MemberDto.Post post =
                MemberDto.Post.builder()
                        .email("hgd@gmail.com")
                        .password("test1234!")
                        .displayName("홍길동")
                        .build();
        String postJson = gson.toJson(post);

        //when
        ResultActions actions = mockMvc.perform(
                patch("/members")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(postJson)
        );

        //then
        actions
                .andExpect(status().isMethodNotAllowed())
                .andExpect(jsonPath("$.status").value(HttpStatus.METHOD_NOT_ALLOWED.value()))
                .andExpect(jsonPath("$.message").value(HttpStatus.METHOD_NOT_ALLOWED.getReasonPhrase()));
    }
}
