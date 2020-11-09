package model;

import java.util.Arrays;

import exceptions.ArraySizeMismatchException;

public class CharClass {
	private String name;
	private int numProficiencies = 0;
	private String[] savingThrows;
	private String[] proficiencies;
	
	public CharClass(String name, int numProficiencies, String[] savingThrows, String[] proficiencies) {
		this.name = name;
		this.numProficiencies = numProficiencies;
		this.savingThrows = savingThrows;
		this.proficiencies = proficiencies;
	}
	
	/**
	 * Name of the CharClass
	 * @return String of the name of the CharClass
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * Sets the name of the CharClass
	 * @param name The name used in the CharClass
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * Returns total number of proficiencies that the CharClass can have
	 * @return integer of the total proficiencies
	 */
	public int getNumProficiencies() {
		return numProficiencies;
	}

	/**
	 * Sets the max amount of proficiencies the CharClass can have
	 * @param numProficiencies max number of proficiencies the CharClass will have
	 */
	public void setNumProficiencies(int numProficiencies) {
		this.numProficiencies = numProficiencies;
	}

	/**
	 * Gets the saving throws as a String
	 * 
	 * This has been done to support legacy code that I wrote years ago.
	 * That is why it doesn't just return the Array.
	 * @return String of the saving throws array
	 */
	public String getSavingThrows() {
		return Arrays.toString(savingThrows);
	}

	/**
	 * Sets the array of saving throws to the passed in array
	 * @param savingThrows String array
	 */
	public void setSavingThrows(String[] savingThrows) {
		this.savingThrows = savingThrows;
	}
	
	/**
	 * Gets the proficiencies as a String
	 * 
	 * This has been done to support legacy code that I wrote years ago.
	 * That is why it doesn't just return the Array.
	 * @return String of the proficiencies array
	 */
	public String getProficiencies() {
		return Arrays.toString(proficiencies);
	}

	/**
	 * Sets passed in array equal to proficiencies array
	 * @param proficiencies String array of proficiencies
	 * @throws ArraySizeMismatchException if array size doesn't match up to expected value (e.g. too large or too small)
	 */
	public void setProficiencies(String[] proficiencies) throws ArraySizeMismatchException {
		if(numProficiencies > 0 && proficiencies.length == numProficiencies) {
			this.proficiencies = proficiencies;
		}else {
			throw new ArraySizeMismatchException("Array passed in is not of expected size");
		}
	}
	
	/**
	 * Returns the character class as a String
	 * @return CharClass as a String
	 */
	@Override
	public String toString() {
		return name + " " + Arrays.toString(savingThrows) + " " + Arrays.toString(proficiencies);
	}
	
	/**
	 * Returns the 'name' of the class as a String in quotes
	 * @return name of the CharClass in double quotes
	 */
	public String toJSON() {
		return "\"" + name + "\"";
	}

	/**
	 * Converts the saving throws array into a JSON String for use in server side processing
	 * @return JSON array of the saving throws
	 */
	public String getSavingThrowsJSON() {
		String stJSON = "[";
		for(int i = 0; i < savingThrows.length; i++) {
			stJSON += "\"" + savingThrows[i].trim() + "\"";
			if(i+1 != savingThrows.length) {
				stJSON += ",";
			}
		}
		stJSON += "]";
		return stJSON;
	}

	/**
	 * Converts the proficiencies array into a JSON String for use in server side processing
	 * @return JSON array of the proficiencies
	 */
	public String getProficienciesJSON() {
		String pJSON = "[";
		for(int i = 0; i < proficiencies.length; i++) {
			pJSON += "\"" + proficiencies[i].trim() + "\"";
			if(i+1 != proficiencies.length) {
				pJSON += ",";
			}
		}
		pJSON += "]";
		return pJSON;
	}
}
