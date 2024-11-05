package com.gigilife.game;

import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class GameApplication {

	public static void main(String[] args) {
		java.security.Security.addProvider(new BouncyCastleProvider());
		SpringApplication.run(GameApplication.class, args);
	}

}
