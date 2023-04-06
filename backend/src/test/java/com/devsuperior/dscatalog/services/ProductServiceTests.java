package com.devsuperior.dscatalog.services;

import com.devsuperior.dscatalog.dto.ProductDTO;
import com.devsuperior.dscatalog.entities.Category;
import com.devsuperior.dscatalog.entities.Product;
import com.devsuperior.dscatalog.repositories.CategoryRepository;
import com.devsuperior.dscatalog.repositories.ProductRepository;
import com.devsuperior.dscatalog.services.exceptions.DatabaseException;
import com.devsuperior.dscatalog.services.exceptions.ResourceNotFoundException;
import com.devsuperior.dscatalog.tests.Factory;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;

import java.util.List;
import java.util.Optional;

@ExtendWith(SpringExtension.class)
class ProductServiceTests {

    @InjectMocks
    private ProductService service;
    @Mock
    private ProductRepository repository;

    @Mock
    private CategoryRepository categoryRepository;

    private long existingId;
    private long nonExistingId;
    private long dependentId;
    private Product product;
    private PageImpl<Product> page;
    private ProductDTO dto;
    private Category  category;

    @BeforeEach
    void setUp() {
        existingId =  1L;
        nonExistingId = 1000L;
        dependentId = 4L;
        product = Factory.createProduct();
        page = new PageImpl<>(List.of(product));
        dto = Factory.createProductDTO();
        category = Factory.createCategory();

        Mockito.when(repository.findAll((Pageable) any())).thenReturn(page);

        Mockito.when(repository.save(any())).thenReturn(product);

        Mockito.when(repository.findById(existingId)).thenReturn(Optional.of(product));
        Mockito.when(repository.findById(nonExistingId)).thenReturn(Optional.empty());

        Mockito.when(repository.find(any(), any(), any())).thenReturn(page);

		Mockito.when(repository.getOne(existingId)).thenReturn(product);
        Mockito.when(repository.getOne(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        Mockito.when(categoryRepository.getOne(existingId)).thenReturn(category);
        Mockito.when(categoryRepository.getOne(nonExistingId)).thenThrow(ResourceNotFoundException.class);

        Mockito.doNothing().when(repository).deleteById(existingId);
        Mockito.doThrow(EmptyResultDataAccessException.class).when(repository).deleteById(nonExistingId);
        Mockito.doThrow(DataIntegrityViolationException.class).when(repository).deleteById(dependentId);

    }

    @Test
    void updateShouldThrowResourceNotFoundExceptionWhenIdDoesNotExist() {

        assertThrows(ResourceNotFoundException.class, () ->  {

            service.update(nonExistingId, dto);

        });

        Mockito.verify(repository, Mockito.times(1)).getOne(nonExistingId);
    }

    @Test
    void updateShouldReturnProductDTOWhenIdExists() {

        ProductDTO productDTO = service.update(existingId, dto);

        assertNotNull(productDTO);

        Mockito.verify(repository, Mockito.times(1)).getOne(existingId);

    }

    @Test
    void findByIdShouldReturnProductDTOWhenIdExists() {

        ProductDTO result = service.findById(existingId);

        assertNotNull(result);
        Mockito.verify(repository, Mockito.times(1)).findById(existingId);
    }

    @Test
    void findByIdShouldThrowResourceNotFoundExceptionWhenIdNotExists() {

        assertThrows(ResourceNotFoundException.class, () -> {

           service.findById(nonExistingId);

        });

        Mockito.verify(repository, Mockito.times(1)).findById(nonExistingId);
    }

    @Test
    void findAllPagedShouldReturnPage() {

        Pageable pageable = PageRequest.of(0, 10);

        Page<ProductDTO> result = service.findAllPaged(0L, "",pageable);

        assertNotNull(result);


    }


    @Test
    void deleteShouldThrowDatabaseExceptionWhenDependId() {

        assertThrows(DatabaseException.class, () -> {
            service.delete(dependentId);
        });


        Mockito.verify(repository, Mockito.times(1)).deleteById(dependentId);
    }

    @Test
    void deleteShouldThrowResourceNotFoundExceptionWhenIdDoesExists() {

        assertThrows(ResourceNotFoundException.class, () -> {
            service.delete(nonExistingId);
        });


        Mockito.verify(repository, Mockito.times(1)).deleteById(nonExistingId);
    }

    @Test
    void deleteShouldDoNothingWhenIdExist() {

        assertDoesNotThrow(() -> {
            service.delete(existingId);
        });


        Mockito.verify(repository, Mockito.times(1)).deleteById(existingId);
    }

}
