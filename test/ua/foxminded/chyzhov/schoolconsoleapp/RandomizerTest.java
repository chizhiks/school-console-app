package ua.foxminded.chyzhov.schoolconsoleapp;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

class RandomizerTest {

	Randomizer randomizer = new Randomizer();

	@Test
	void getRandomInteger_shouldReturnDigitBetween0And9_whenCalled() {
		int randomNumber = randomizer.getRandomInteger();
		assertTrue(randomNumber >= 0 && randomNumber <= 9, "Number should be between 0 and 9");
	}

	@Test
	void getRandomLetter_shouldReturnUpperCaseLetter_whenCalled() {
		char randomLetter = randomizer.getRandomLetter();
		assertTrue(randomLetter >= 'A' && randomLetter <= 'Z', "Number should be between A and Z");
	}

	@Test
	void getRandomGroupName_shouldReturnCorrectResult_whenCalled() {
		String groupName = randomizer.getRandomGroupName();

		assertEquals(5, groupName.length(), "Group name should be 5 characters long");
		assertTrue(Character.isUpperCase(groupName.charAt(0)), "First character should be between A and Z");
		assertTrue(Character.isUpperCase(groupName.charAt(1)), "Second character should be between A and Z");
		assertEquals('-', groupName.charAt(2), "Third character should be '-'");
		assertTrue(Character.isDigit(groupName.charAt(3)), "Fourth character should be a digit");
		assertTrue(Character.isDigit(groupName.charAt(4)), "Fifth character should be a digit");
	}
}
