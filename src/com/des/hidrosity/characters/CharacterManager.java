package com.des.hidrosity.characters;

public class CharacterManager {

	private static Character currentCharacter;

	public static void setCharacter(Character character) {
		currentCharacter = character;
	}

	public static Character getCharacter() {
		return currentCharacter;
	}
}
