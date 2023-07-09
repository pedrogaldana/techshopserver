package com.example.techshopserver.orders

import com.example.techshopserver.orders.Order.Status.ABERTO
import com.example.techshopserver.orders.requests.OrderRequest
import com.example.techshopserver.products.ProductsRepository
import org.slf4j.LoggerFactory
import org.springframework.data.repository.findByIdOrNull
import org.springframework.stereotype.Service
import java.util.*

@Service
class OrdersService(val ordersRepository: OrdersRepository, val productsRepository: ProductsRepository) {

    fun save(req: OrderRequest): Order {
        val product =
            productsRepository.findByIdOrNull(req.idProduct) ?: throw IllegalStateException("Product not found.")
        val date = Date()
        val order = Order(date = date, amount = product.price, status = ABERTO)
        order.addProduct(product)
        return ordersRepository.save(order)
    }

    fun getById(id: Long) = ordersRepository.findById(id)

    fun findAll(status: String?) =
        if (status == null) ordersRepository.findAll().sortedBy { it.date }
        else ordersRepository.findAllByStatus(status)

    fun addProduct(req: OrderRequest): Boolean {
        val order = ordersRepository.findByIdOrNull(req.id) ?: return false
        val product = productsRepository.findByIdOrNull(req.idProduct) ?: return false
        order.addProduct(product)
        log.warn("Order updated. id={} date={} amount{}", order.id, order.date, order.amount)
        ordersRepository.save(order)
        return true
    }

    fun removeProduct(req: OrderRequest): Boolean {
        val order = ordersRepository.findByIdOrNull(req.id) ?: return false
        val product = productsRepository.findByIdOrNull(req.idProduct) ?: return false
        if (!order.products.contains(product)) return false
        order.removeProduct(product)
        log.warn("Order updated. id={} date={} amount{}", order.id, order.date, order.amount)
        ordersRepository.save(order)
        return true
    }

    fun conclude(req: OrderRequest): Boolean {
        val order = ordersRepository.findByIdOrNull(req.id) ?: return false
        log.warn("Order finished. id={} date={} amount{}", order.id, order.date, order.amount)
        order.status = Order.FINALIZADO
        ordersRepository.save(order)
        return true
    }

    companion object {
        val log = LoggerFactory.getLogger(OrdersService::class.java)
    }
}