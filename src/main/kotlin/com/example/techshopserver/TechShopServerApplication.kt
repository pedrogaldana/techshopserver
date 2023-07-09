package com.example.techshopserver

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class TechShopServerApplication

fun main(args: Array<String>) {
	runApplication<TechShopServerApplication>(*args)
}
