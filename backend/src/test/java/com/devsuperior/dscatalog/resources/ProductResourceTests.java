package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.services.ProductService;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import com.devsuperior.dscatalog.tests.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class ProductResourceTests {

    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private ProductService service;
    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenUtil tokenUtil;


    private ProductDTO productDTO;
    private PageImpl<ProductDTO> page;
    private Long existingId;
    private Long nonExistingId;
    private Long dependentId;

    private String username;
    private String password;

    @BeforeEach
    void setUp() {
        existingId = 1L;
        nonExistingId = 2L;
        dependentId = 3L;

        username = "maria@gmail.com";
        password = "123456";

        productDTO = Factory.createProductDTO();
        page = new PageImpl<>(List.of(productDTO));

        Mockito.when(service.findAllPaged(any(), any(), any())).thenReturn(page);

        Mockito.when(service.findById(existingId)).thenReturn(productDTO);
        Mockito.when(service.findById(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        Mockito.when(service.update(eq(existingId), ArgumentMatchers.any())).thenReturn(productDTO);
        Mockito.when(service.update(eq(nonExistingId), ArgumentMatchers.any())).thenThrow(ResourceNotFoundException.class);

        Mockito.when(service.insert(ArgumentMatchers.any())).thenReturn(productDTO);

        Mockito.doNothing().when(service).delete(existingId);
        Mockito.doThrow(ResourceNotFoundException.class).when(service).delete(nonExistingId);
        Mockito.doThrow(DatabaseException.class).when(service).delete(dependentId);
    }

    @Test
    void insertShouldReturnCreatedAndProductDTO() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(post("/products")
                .header("Authorization", "Bearer " + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isCreated());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.price").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    void deleteShouldReturnNoContentWhenIdExists() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        ResultActions result = mockMvc.perform(delete("/products/{id}", existingId)
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNoContent());
    }

    @Test
    void deleteShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        ResultActions result = mockMvc.perform(delete("/products/{id}", nonExistingId)
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    void deleteShouldReturnBadRequestWhenDependentId() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        ResultActions result = mockMvc.perform(delete("/products/{id}", dependentId)
                .header("Authorization", "Bearer " + accessToken)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isBadRequest());
    }


    @Test
    void updateShouldReturnProductDTOWhenIdExists() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(put("/products/{id}", existingId)
                .header("Authorization", "Bearer " + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    void updateShouldReturnNotFoundWhenIdDoesNotExist() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result  = mockMvc.perform(put("/products/{id}", nonExistingId)
                .header("Authorization", "Bearer " + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

    @Test
    void findAllPagedShouldReturnPageProductDTO() throws Exception {
        ResultActions result = mockMvc.perform(get("/products")
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());

    }

    @Test
    void findByIdShouldReturnProductDTOWhenIdExists() throws Exception {

        ResultActions result = mockMvc.perform(get("/products/{id}", existingId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isOk());
        result.andExpect(jsonPath("$.id").exists());
        result.andExpect(jsonPath("$.name").exists());
        result.andExpect(jsonPath("$.description").exists());
    }

    @Test
    void findByIdShouldReturnNotFoundWhenIdDoesNotExists() throws Exception  {

        ResultActions  result = mockMvc.perform(get("/products/{id}", nonExistingId)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(status().isNotFound());
    }

}
