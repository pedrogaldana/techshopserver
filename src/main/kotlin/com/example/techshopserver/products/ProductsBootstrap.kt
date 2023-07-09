package com.example.techshopserver.products

import com.example.techshopserver.products.Product.Category.GAMER
import com.example.techshopserver.products.Product.Category.HARDWARE
import com.example.techshopserver.products.Product.Category.PERIFERICO
import com.example.techshopserver.products.Product.Category.SOFTWARE
import org.springframework.context.ApplicationListener
import org.springframework.context.event.ContextRefreshedEvent
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class ProductsBootstrap(val productsRepository: ProductsRepository) : ApplicationListener<ContextRefreshedEvent> {
    override fun onApplicationEvent(event: ContextRefreshedEvent) {
        if (productsRepository.count() == 0L) {
            val exPeriferico = Product(
                name = "Mouse Sem Fio",
                description = "Desfrute da confiabilidade de um mouse com fio com a conveniência do sem fio.",
                price = BigDecimal(58.79),
                category = PERIFERICO,
            )

            val exHardware = Product(
                name = "SSD M.2 NVMe 1TB",
                description = "O SSD M.2 NVMe é uma solução substancial de armazenamento de última geração.",
                price = BigDecimal(299.99),
                category = HARDWARE,
            )

            val exSoftware = Product(
                name = "Pacote Office Professional 32/64 Bits",
                description = "Office Professional ajuda a gerenciar o seu trabalho com ferramentas de produtividade e database",
                price = BigDecimal(1999.99),
                category = SOFTWARE,
            )

            val exGamer = Product(
                name = "Cadeira Gamer Reclinável RGB",
                description = "Trabalhe, jogue ou descanse com todo o conforto que a belíssima e ergonômica Cadeira Gamer.",
                price = BigDecimal(1072.85),
                category = GAMER,
            )

            productsRepository.save(exPeriferico)
            productsRepository.save(exHardware)
            productsRepository.save(exSoftware)
            productsRepository.save(exGamer)
        }
    }

}