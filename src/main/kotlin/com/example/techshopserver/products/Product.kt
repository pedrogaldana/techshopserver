package com.example.techshopserver.products

import com.example.techshopserver.orders.Order
import com.example.techshopserver.products.requests.ProductRequest
import com.example.techshopserver.products.responses.ProductResponse
import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "TblProduct")
class Product(
    @Id
    @GeneratedValue
    var id: Long? = null,

    @Column(nullable = false)
    var name: String = "",

    @Column(nullable = false)
    var description: String = "",

    @Column(nullable = false)
    var price: BigDecimal = BigDecimal.ZERO,

    @Column(nullable = false, length = 20)
    var category: String = "",

    @ManyToMany(mappedBy = "products")
    val orders: MutableSet<Order> = mutableSetOf()

) {

    constructor(req: ProductRequest) : this() {
        this.id = req.id
        this.name = req.name ?: ""
        this.description = req.description ?: ""
        this.price = req.price ?: BigDecimal.ZERO
        this.category = req.category ?: ""
    }

    fun toResponse() = ProductResponse(
        id = this.id!!,
        name = this.name,
        description = this.description,
        price = this.price,
        category = this.category
    )

    companion object Category {
        const val PERIFERICO = "periferico"
        const val HARDWARE = "hardware"
        const val SOFTWARE = "software"
        const val GAMER = "gamer"
    }
}