package com.example.techshopserver.products

import com.example.techshopserver.products.requests.ProductRequest
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service

@Service
class ProductsService(val productsRepository: ProductsRepository) {

    fun save(req: ProductRequest): Product {
        return productsRepository.save(Product(req))
    }

    fun getById(id: Long) = productsRepository.findById(id)

    fun findAll(category: String?) =
        if (category == null) productsRepository.findAll().sortedBy { it.name }
        else productsRepository.findAllByCategory(category)


    fun delete(id: Long): Boolean {
        val user = productsRepository.findByIdOrNull(id) ?: return false
        log.warn("Product deleted. id={} name={} category{}", user.id, user.name, user.category)
        productsRepository.delete(user)
        return true
    }

    fun update(req: ProductRequest): Boolean {
        productsRepository.findByIdOrNull(req.id) ?: return false
        log.warn("Product updated. id={} name={} category{}", req.id, req.name, req.category)
        productsRepository.save(Product(req))
        return true
    }

    companion object {
        val log = LoggerFactory.getLogger(ProductsService::class.java)
    }
}