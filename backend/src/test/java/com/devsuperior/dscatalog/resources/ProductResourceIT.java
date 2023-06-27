package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.tests.Factory;
import com.devsuperior.dscatalog.tests.TokenUtil;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class ProductResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private TokenUtil tokenUtil;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;
    private ProductDTO productDTO;

    private String username;
    private String password;

    @BeforeEach
    void setUp() {

        username = "maria@gmail.com";
        password = "123456";
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
        productDTO = Factory.createProductDTO();
    }

    @Test
    void findAllShouldReturnPagedSortedWhenSortByName() throws Exception {

        ResultActions result = mockMvc.perform(
                MockMvcRequestBuilders.get("/products?page=0&size=12&sort=name,asc")
                        .accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.content").exists());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.totalElements").value(countTotalProducts));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name").value("Macbook Pro"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.content[1].name").value("PC Gamer"));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.content[2].name").value("PC Gamer Alfa"));

    }

    @Test
    void updateShouldReturnProductDTOWhenIdExists() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        String expectedName = productDTO.getName();
        String expectedDescription = productDTO.getDescription();

        String jsonBody = objectMapper.writeValueAsString(productDTO);


        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", existingId)
                .header("Authorization", "Bearer " + accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(existingId));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(expectedName));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.description").value(expectedDescription));
    }

    @Test
    void updateShouldReturnResourceNotFoundExceptionWhenIdDoesNotExist() throws Exception {

        String accessToken = tokenUtil.obtainAccessToken(mockMvc, username, password);

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", nonExistingId)
                .header("Authorization", "Bearer " +  accessToken)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
