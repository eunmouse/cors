package org.example.springv3.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.example.springv3.board.BoardRequest;
import org.example.springv3.core.util.JwtUtil;
import org.example.springv3.user.User;
import org.example.springv3.user.UserRequest;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@Transactional
@AutoConfigureMockMvc
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.MOCK) // 가짜환경 MOCK 에 띄운다.
public class BoardControllerTest {

    @Autowired
    private MockMvc mvc;
    private ObjectMapper om = new ObjectMapper(); // 얘는 그냥 new 하면 된다.
    private String accessToken;

    @BeforeEach // 매번 메서드 만들기 전에 실행
    public void setUp() { // 매번 실행될 때 마다 만들기 귀찮으니
        System.out.println("나 실행돼? setUp");
        User sessionUser = User.builder().id(1).username("ssar").build();
        accessToken = JwtUtil.create(sessionUser);
    }

    @Test
    public void save_test() throws Exception { // _는 테스트 코드의 컨벤션
        // given
//        User sessionUser = User.builder().id(1).username("ssar").build();
//        String accessToken = JwtUtil.create(sessionUser);
//        System.out.println(accessToken);

        BoardRequest.SaveDTO saveDTO = new BoardRequest.SaveDTO();
        saveDTO.setTitle("title 11");
        saveDTO.setContent("content 11");

        String requestBody = om.writeValueAsString(saveDTO);
        System.out.println(requestBody);

        // when
        ResultActions actions = mvc.perform(
                post("/api/board")
                        .header("Authorization", "Bearer " + accessToken)
                        .content(requestBody)
                        .contentType(MediaType.APPLICATION_JSON)
        ); // 동작 코드

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(jsonPath("$.status").value(200));
        actions.andExpect(jsonPath("$.msg").value("성공"));
        actions.andExpect(jsonPath("$.body.title").value("title 11"));
        actions.andExpect(jsonPath("$.body.content").value("content 11"));
        actions.andExpect(jsonPath("$.body.id").isNumber());
    }

    @Test
    public void delete_test() throws Exception { // _는 테스트 코드의 컨벤션
        // given
        int id = 2;

        // when
        ResultActions actions = mvc.perform(
                delete("/api/board/"+id)
                        .header("Authorization", "Bearer " + accessToken)
        ); // 동작 코드

        // eye
        String responseBody = actions.andReturn().getResponse().getContentAsString();
        System.out.println(responseBody);

        // then
        actions.andExpect(jsonPath("$.status").value(200));
        actions.andExpect(jsonPath("$.msg").value("성공"));
        actions.andExpect(jsonPath("$.body").isEmpty());
    }


}
