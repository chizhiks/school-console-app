package ua.foxminded.chyzhov.schoolconsoleapp;

import java.util.Random;

public class Randomizer {

	public String getRandomGroupName() {
		StringBuilder result = new StringBuilder();

		result.append(getRandomLetter());
		result.append(getRandomLetter());
		result.append('-');
		result.append(getRandomInteger());
		result.append(getRandomInteger());

		return result.toString();
	}

	public char getRandomLetter() {
		Random random = new Random();
		char randomLetter = (char) (random.nextInt(26) + 'A');
		return randomLetter;
	}

	public int getRandomInteger() {
		Random random = new Random();
		int randomInteger = random.nextInt(10);
		return randomInteger;
	}

}
