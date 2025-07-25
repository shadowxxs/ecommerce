package com.example.ecommerce.service;

import com.example.ecommerce.entity.Product;
import com.example.ecommerce.exception.ResourceNotFoundExecption;
import com.example.ecommerce.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;



@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public Page<Product> getAllProduct(Pageable pageable){
        return productRepository.findAll(pageable);
    }

    public Product getProductById(Long id) {
      return productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundExecption("Product: " + id +" tidak ditemukan"));
    }

    public Page<Product> getProductByName(String name, Pageable pageable) {
        Page<Product> productName = productRepository.findByNameContainingIgnoreCase(name, pageable);
        if (productName.isEmpty()){
            throw new ResourceNotFoundExecption("Tidak ada product dengan nama: " + name);
        }
        return productName;

    }

    public Page<Product> getProductByType(String type, Pageable pageable) {
        Page<Product> productType = productRepository.findByType(type, pageable);
        if (productType.isEmpty()) {
            throw new ResourceNotFoundExecption("Tidak ada product dengan type: " + type);
        }
        return productType;
    }

    public Product createProduct(Product product) {
        return productRepository.save(product);
    }

    public Product updateProduct(Long id, Product updateProduct) {
        Product existingProduct = productRepository.findById(id).orElseThrow(() -> new ResourceNotFoundExecption("Product dengan ID: " + id + "tidak ditemukan"));

            existingProduct.setName(updateProduct.getName());
            existingProduct.setType(updateProduct.getType());
            existingProduct.setPrice(updateProduct.getPrice());
            existingProduct.setDescription(updateProduct.getDescription());
            existingProduct.setImageUrl(updateProduct.getImageUrl());
            existingProduct.setStock(updateProduct.getStock());
            existingProduct.setIsActive(updateProduct.getIsActive());

            return productRepository.save(existingProduct);
    }

    public void deleteProduct(Long id){
        if (!productRepository.existsById(id)){
            throw new ResourceNotFoundExecption("Product tidak ada");
        }
        productRepository.deleteById(id);
    }
}
