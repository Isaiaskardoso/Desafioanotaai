package com.isaiasKardoso.desafioanotaai.services;

import com.isaiasKardoso.desafioanotaai.domain.category.Category;
import com.isaiasKardoso.desafioanotaai.domain.category.exceptions.CategoryNotFoundException;
import com.isaiasKardoso.desafioanotaai.domain.product.Product;
import com.isaiasKardoso.desafioanotaai.domain.product.ProductDTO;
import com.isaiasKardoso.desafioanotaai.domain.product.exceptions.ProductNotFoundException;
import com.isaiasKardoso.desafioanotaai.repositories.ProductRepository;
import com.isaiasKardoso.desafioanotaai.services.aws.AwsSnsService;
import com.isaiasKardoso.desafioanotaai.services.aws.MessageDTO;
import org.springframework.stereotype.Service;

import java.util.List;
@Service
public class ProductService { // instâncias
    private final CategoryService categoryService;
    private final ProductRepository repository;
    private final AwsSnsService snsService;
    public ProductService(CategoryService categoryService, ProductRepository productRepository, AwsSnsService snsService){
        this.categoryService = categoryService;
        this.repository = productRepository;
        this.snsService = snsService;
    }
    public Product insert(ProductDTO productData){
        Category category = this.categoryService.getById(productData.categoryId())
                .orElseThrow(CategoryNotFoundException::new);
        Product newProduct = new Product(productData);
        newProduct.setCategory(category);

        this.repository.save(newProduct);

        this.snsService.publish(new MessageDTO(newProduct.getOwnerId()));

        return newProduct;
    }
    public Product update(String id, ProductDTO productData) {
        Product product = this.repository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        if (productData.categoryId() != null) {
            this.categoryService.getById(productData.categoryId())
                    .ifPresent(product::setCategory);
        }



        if (!productData.title().isEmpty()) product.setTitle(productData.title());
        if (!productData.description().isEmpty()) product.setDescription(productData.description());
        if (!(productData.price() == null)) product.setPrice(productData.price());

        this.repository.save(product);
        this.snsService.publish(new MessageDTO(product.getOwnerId()));

        return product;
    }

    public void delete(String id) {
        Product product = this.repository.findById(id)
                .orElseThrow(CategoryNotFoundException::new);

        this.repository.delete(product);


    }
    public List<Product> getAll(){
        return this.repository.findAll();
    }
}