package com.ws101.bido.EcommerceApi.service;

import com.ws101.bido.EcommerceApi.model.Product;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * @author Zanjoe M. Bido
 */
@Service
public class ProductService {

    private final List<Product> productList = new ArrayList<>();
    private final AtomicLong idGenerator = new AtomicLong(1);

    public ProductService() {
        productList.add(new Product(idGenerator.getAndIncrement(), "Premium Leather Jacket", "High-quality genuine leather jacket", 149.99, "Apparel", 15, "jacket.png"));
        productList.add(new Product(idGenerator.getAndIncrement(), "Wireless Headphones", "Noise-cancelling over-ear headphones", 89.50, "Electronics", 30, "headphones.png"));
        productList.add(new Product(idGenerator.getAndIncrement(), "Smart Sports Watch", "Waterproof fitness tracker with GPS tracking", 199.99, "Electronics", 12, "watch.png"));
        productList.add(new Product(idGenerator.getAndIncrement(), "Ergonomic Blender", "High-speed multi-function kitchen blender", 45.00, "Home Appliances", 8, "blender.png"));
        productList.add(new Product(idGenerator.getAndIncrement(), "Digital Rice Cooker", "Smart programmable rice cooker and food warmer", 65.25, "Home Appliances", 20, "ricecooker.png"));
        productList.add(new Product(idGenerator.getAndIncrement(), "Watermelon Safe Container", "Sleek airtight produce freshness saver", 12.99, "Kitchenware", 50, "watermelon.png"));
        productList.add(new Product(idGenerator.getAndIncrement(), "Classic Basketball", "Official size and weight durable indoor/outdoor ball", 29.99, "Sports", 25, "basketball.png"));
        productList.add(new Product(idGenerator.getAndIncrement(), "Organic Baby Oil", "Hypoallergenic skin nourishment fluid for toddlers", 8.50, "Baby Care", 40, "baby-oil.png"));
        productList.add(new Product(idGenerator.getAndIncrement(), "Sublime Audio Pods", "True wireless deep-bass sound bluetooth ear buds", 74.99, "Electronics", 18, "pot.png"));
        productList.add(new Product(idGenerator.getAndIncrement(), "Minimalist Dress", "Comfortable, versatile, and elegant casual dress", 55.00, "Apparel", 14, "dress.png"));
    }

    public List<Product> getAllProducts() {
        return new ArrayList<>(productList);
    }

    public Optional<Product> getProductById(Long id) {
        return productList.stream()
                .filter(product -> product.getId().equals(id))
                .findFirst();
    }

    public Product createProduct(Product product) {
        product.setId(idGenerator.getAndIncrement());
        productList.add(product);
        return product;
    }

    public Optional<Product> updateProduct(Long id, Product updatedProduct) {
        return getProductById(id).map(existingProduct -> {
            existingProduct.setName(updatedProduct.getName());
            existingProduct.setDescription(updatedProduct.getDescription());
            existingProduct.setPrice(updatedProduct.getPrice());
            existingProduct.setCategory(updatedProduct.getCategory());
            existingProduct.setStockQuantity(updatedProduct.getStockQuantity());
            existingProduct.setImageUrl(updatedProduct.getImageUrl());
            return existingProduct;
        });
    }

    public Optional<Product> patchProduct(Long id, Product partialProduct) {
        return getProductById(id).map(existingProduct -> {
            if (partialProduct.getName() != null) existingProduct.setName(partialProduct.getName());
            if (partialProduct.getDescription() != null) existingProduct.setDescription(partialProduct.getDescription());
            if (partialProduct.getPrice() != null) existingProduct.setPrice(partialProduct.getPrice());
            if (partialProduct.getCategory() != null) existingProduct.setCategory(partialProduct.getCategory());
            if (partialProduct.getStockQuantity() != null) existingProduct.setStockQuantity(partialProduct.getStockQuantity());
            if (partialProduct.getImageUrl() != null) existingProduct.setImageUrl(partialProduct.getImageUrl());
            return existingProduct;
        });
    }

    public boolean deleteProduct(Long id) {
        return productList.removeIf(product -> product.getId().equals(id));
    }

    public List<Product> filterProducts(String filterType, String filterValue) {
        if (filterType == null || filterValue == null) return getAllProducts();
        return productList.stream().filter(product -> {
            switch (filterType.toLowerCase()) {
                case "name":
                    return product.getName().toLowerCase().contains(filterValue.toLowerCase());
                case "category":
                    return product.getCategory().toLowerCase().equalsIgnoreCase(filterValue.trim());
                case "price":
                    try { return product.getPrice() <= Double.parseDouble(filterValue); } 
                    catch (NumberFormatException e) { return false; }
                default:
                    return true;
            }
        }).collect(Collectors.toList());
    }
}