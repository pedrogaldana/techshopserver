package com.example.techshopserver.products

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface ProductsRepository : JpaRepository<Product, Long> {
        @Query(
            value = "select distinct p from Product p" +
                    " where p.category = :category" +
                    " order by p.name"
        )
        fun findAllByCategory(category: String): List<Product>
}