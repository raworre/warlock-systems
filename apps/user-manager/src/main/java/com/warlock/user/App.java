package com.warlock.user;

import io.jsonwebtoken.JwtBuilder;
import io.jsonwebtoken.JwtParser;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class App {

	public static void main(String[] args) {
		SpringApplication.run(App.class, args);
	}

	@Bean
	public JwtBuilder jwtBuilder() {
		return Jwts.builder().signWith(SignatureAlgorithm.HS512, "secretKey");
	}

	@Bean
	public JwtParser jwtParser() {
		return Jwts.parser().setSigningKey("secretKey");
	}
}
