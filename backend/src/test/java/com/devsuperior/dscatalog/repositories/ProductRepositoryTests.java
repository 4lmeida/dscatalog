package com.devsuperior.dscatalog.repositories;

import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.EmptyResultDataAccessException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class ProductRepositoryTests {
    private long existingId;
    private long nonExistingId;
    private long countTotalId;

    @BeforeEach
    void setUp() throws Exception {
        existingId = 1L;
        nonExistingId = 1000L;
        countTotalId = 25L;
    }

    @Autowired
    ProductRepository repository;

    @Test
    void findByIdShouldReturnProductNotNullWhenIdExist() {

        Product product  = Factory.createProduct();

        Optional<Product> obj = repository.findById(product.getId());

        assertNotNull(obj);
    }

    @Test
    void findByIdShouldReturnProductNullWhenIdNotExist() {

        Optional<Product> obj = repository.findById(countTotalId + 1);

        assertTrue(obj.isEmpty());
    }

    @Test
    void saveShouldPersistWithAutoincrementWhenIdIsNull() {

        Product product = Factory.createProduct();
        product.setId(null);

        product = repository.save(product);

        assertNotNull(product);
        assertEquals(countTotalId + 1, product.getId());

    }

    @Test
    void deleteShouldDeleteObjectWhenExists() {

        repository.deleteById(existingId);

        Optional<Product> result = repository.findById(existingId);
        assertFalse(result.isPresent());
    }

    @Test
    void deleteShouldThrowEmptyResultDataAccessExceptionWhenIdDoesNotExists() {

        assertThrows(EmptyResultDataAccessException.class, () -> {
            repository.deleteById(nonExistingId);
        });
    }

}
