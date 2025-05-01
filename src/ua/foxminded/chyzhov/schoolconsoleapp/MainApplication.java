package ua.foxminded.chyzhov.schoolconsoleapp;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class MainApplication {

	public static void main(String[] args) {

		var context = SpringApplication.run(MainApplication.class, args);

		UserInputService userInputService = context.getBean(UserInputService.class);
		userInputService.start();

	}

}
