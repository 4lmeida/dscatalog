package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@SpringBootTest
@Transactional
class ProductServiceIT {

    @Autowired
    private ProductService service;

    @Autowired
    private ProductRepository repository;

    private Long  existingId;
    private Long nonExistingId;
    private Long countTotalProducts;

    @BeforeEach
    void setUp() {

        existingId = 1L;
        nonExistingId = 1000L;
        countTotalProducts = 25L;

    }

    @Test
    void deleteShouldDeleteProductWhenIdExists() {

        service.delete(existingId);

        assertEquals(countTotalProducts - 1, repository.count());
    }

    @Test
    void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

        assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });
    }

    @Test
    void findAllPagedShouldReturnPagedWhenPage0size10() {
        PageRequest pageRequest = PageRequest.of(0, 10);

        Page<ProductDTO> result = service.findAllPaged(0L, "", pageRequest);

        assertFalse(result.isEmpty());
        assertEquals(0, pageRequest.getPageNumber());
        assertEquals(10, pageRequest.getPageSize());
        assertEquals(countTotalProducts, result.getTotalElements());
    }

    @Test
    void findAllPagedShouldReturnPageEmpty() {

        PageRequest pageRequest = PageRequest.of(50, 10);

        Page<ProductDTO> result = service.findAllPaged(0L, "",pageRequest);

        assertTrue(result.isEmpty());
    }

    @Test
    void findAllPagedShouldReturnSortedWhenSortByName() {

        PageRequest pageRequest = PageRequest.of(0, 10, Sort.by("name"));

        Page<ProductDTO> result = service.findAllPaged(0L, "",pageRequest);

        assertFalse(result.isEmpty());
        assertEquals("Macbook Pro", result.getContent().get(0).getName());
        assertEquals("PC Gamer", result.getContent().get(1).getName());
        assertEquals("PC Gamer Alfa", result.getContent().get(2).getName());

    }

}
