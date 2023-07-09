package com.example.techshopserver.orders

import com.example.techshopserver.orders.responses.OrderResponse
import com.example.techshopserver.products.Product
import jakarta.persistence.*
import java.math.BigDecimal
import java.util.*

@Entity
@Table(name = "TblOrder")
class Order(
    @Id
    @GeneratedValue
    var id: Long? = null,

    @Column(nullable = false)
    var date: Date = Date(),

    @Column(nullable = false, length = 20)
    var status: String = "",

    @Column(nullable = false)
    var amount: BigDecimal = BigDecimal.ZERO,

    @ManyToMany
    @JoinTable(
        name = "OrderProduct",
        joinColumns = [JoinColumn(name = "idOrder")],
        inverseJoinColumns = [JoinColumn(name = "idProduct")]
    )
    val products: MutableSet<Product> = mutableSetOf()
) {
    fun toResponse() = OrderResponse(
        id = this.id!!,
        date = this.date,
        status = this.status,
        amount = this.amount,
        products = this.products.map { p -> p.toResponse() }.toMutableSet()
    )

    fun addProduct(product: Product) {
        products.add(product)
        refreshTotalAmount()
    }

    fun removeProduct(product: Product) {
        products.remove(product)
        refreshTotalAmount()
    }

    private fun refreshTotalAmount() {
        amount = products.sumOf { p -> p.price }
    }

    companion object Status {
        const val ABERTO = "aberto"
        const val FINALIZADO = "finalizado"
    }
}