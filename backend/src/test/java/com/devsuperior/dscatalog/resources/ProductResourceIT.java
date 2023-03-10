package com.devsuperior.dscatalog.resources;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.tests.Factory;
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
public class ProductResourceIT {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    private Long existingId;
    private Long nonExistingId;
    private Long countTotalProducts;
    private ProductDTO productDTO;

    @BeforeEach
    void setUp() {

        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;
        productDTO = Factory.createProductDTO();
    }

    @Test
    public void findAllShouldReturnPagedSortedWhenSortByName() throws Exception {

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
    public void updateShouldReturnProductDTOWhenIdExists() throws Exception {

        String expectedName = productDTO.getName();
        String expectedDescription = productDTO.getDescription();

        String jsonBody = objectMapper.writeValueAsString(productDTO);


        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", existingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isOk());
        result.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(existingId));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.name").value(expectedName));
        result.andExpect(MockMvcResultMatchers.jsonPath("$.description").value(expectedDescription));
    }

    @Test
    public void updateShouldReturnResourceNotFoundExceptionWhenIdDoesNotExist() throws Exception {

        String jsonBody = objectMapper.writeValueAsString(productDTO);

        ResultActions result = mockMvc.perform(MockMvcRequestBuilders.put("/products/{id}", nonExistingId)
                .content(jsonBody)
                .contentType(MediaType.APPLICATION_JSON)
                .accept(MediaType.APPLICATION_JSON));

        result.andExpect(MockMvcResultMatchers.status().isNotFound());
    }

}
