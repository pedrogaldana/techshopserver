package com.example.techshopserver.products

import com.example.techshopserver.Stubs.productStub
import io.kotest.matchers.shouldBe
import io.mockk.every
import io.mockk.justRun
import io.mockk.mockk
import org.junit.jupiter.api.Test
import org.springframework.data.repository.findByIdOrNull

internal class ProductsServiceTest {
    private val productsRepositoryMock = mockk<ProductsRepository>()
    private val service = ProductsService(productsRepositoryMock)

    @Test
    fun `Delete should return false if the product does not exists`() {
        every { productsRepositoryMock.findByIdOrNull(1) } returns null
        service.delete(1) shouldBe false
    }

    @Test
    fun `Delete must return true if the product is deleted`() {
        val product = productStub()
        every { productsRepositoryMock.findByIdOrNull(1) } returns product
        justRun { productsRepositoryMock.delete(product) }
        service.delete(1) shouldBe true
    }
}