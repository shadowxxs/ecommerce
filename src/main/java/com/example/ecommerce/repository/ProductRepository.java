package com.example.ecommerce.repository;


import com.example.ecommerce.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;





@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

    Page<Product> findAll(Pageable pageable);
    Page<Product> findByType(String type, Pageable pageable);
    Page<Product> findByNameContainingIgnoreCase(String name, Pageable pageable);
}
