package com.sess.core.api.rest;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.sess.core.TestUtils;
import com.sess.core.api.rest.handlers.exceptions.HttpStatusOperationException;
import com.sess.core.api.rest.handlers.UserRestApiHandler;
import com.sess.core.dto.DTOUser;
import com.sess.core.exceptions.Error;
import com.sess.core.exceptions.ErrorBuilder;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest
class UserRestControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private UserRestApiHandler registrationManager;

    @Test
    public void shouldReturnDTOUser() throws Exception {
        DTOUser dtoUser = TestUtils.createDTOUser(null);
        String json = objectMapper.writeValueAsString(dtoUser);
        when(registrationManager.register(any(DTOUser.class))).thenReturn(dtoUser);

        MvcResult mvcResult = mockMvc
                .perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().isCreated())
                .andReturn();

        verify(registrationManager).register(any(DTOUser.class));
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(json);
    }

    @Test
    public void shouldReturnError() throws Exception {
        Error expectedError = ErrorBuilder.newBuilder(false)
                .addMessage("1", "message 1")
                .addMessage("2", "message 2")
                .build();
        HttpStatus expectedStatus = HttpStatus.BAD_REQUEST;

        DTOUser dtoUser = TestUtils.createDTOUser(null);
        String json = objectMapper.writeValueAsString(dtoUser);
        when(registrationManager.register(any(DTOUser.class)))
                .thenThrow(new HttpStatusOperationException(expectedError, expectedStatus));

        MvcResult mvcResult = mockMvc
                .perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json)
                )
                .andExpect(status().is(expectedStatus.value()))
                .andReturn();

        verify(registrationManager).register(any(DTOUser.class));
        assertThat(mvcResult.getResponse().getContentAsString()).isEqualTo(objectMapper.writeValueAsString(expectedError));
    }


}