package com.kingtan.store.product;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;

@SpringBootApplication	//(exclude = {DataSourceAutoConfiguration.class})
@EnableJpaRepositories(basePackages = "com.kingtan.store.product.repository.jpa")
@EnableMongoRepositories(basePackages = "com.kingtan.store.product.repository.mongo")
@EntityScan("com.kingtan.store.product.model.entity")
public class ProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(ProductApplication.class, args);
	}

}
