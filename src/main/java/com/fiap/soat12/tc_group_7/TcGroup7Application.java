package com.fiap.soat12.tc_group_7;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

@SpringBootApplication
@EnableTransactionManagement
public class TcGroup7Application {

	public static void main(String[] args) {
		SpringApplication.run(TcGroup7Application.class, args);
	}

}
