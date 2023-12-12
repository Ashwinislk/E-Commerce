package com.electronicstore.repository;

import com.electronicstore.entity.Category;
import com.electronicstore.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product,String> {

    public Page<Product> findByLiveTrue(Pageable pageable);

    public Page<Product> findByTitleContaining(Pageable pageable,String keyword);

   Page<Product> findByCategory(Category category,Pageable pageable);
}
