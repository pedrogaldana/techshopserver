package com.example.techshopserver.orders

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.stereotype.Repository

@Repository
interface OrdersRepository : JpaRepository<Order, Long> {
    @Query(
        value = "select distinct o from Order o" +
                " where o.status = :status" +
                " order by o.date"
    )
    fun findAllByStatus(status: String): List<Order>
}