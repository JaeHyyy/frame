package com.saju;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.saju.mapper")
public class SajuBackApplication {

	public static void main(String[] args) {
		SpringApplication.run(SajuBackApplication.class, args);
	}

}
