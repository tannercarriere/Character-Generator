package model;

import java.util.Arrays;

public class CharClass {
	private String name;
	private int numProficiencies;
	private String[] savingThrows;
	private String[] proficiencies;
	
	public CharClass(String name, int numProficiencies, String[] savingThrows, String[] proficiencies) {
		this.name = name;
		this.numProficiencies = numProficiencies;
		this.savingThrows = savingThrows;
		this.proficiencies = proficiencies;
	}
	
	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNumProficiencies() {
		return numProficiencies;
	}

	public void setNumProficiencies(int numProficiencies) {
		this.numProficiencies = numProficiencies;
	}

	public String getSavingThrows() {
		return Arrays.toString(savingThrows);
	}

	public void setSavingThrows(String[] savingThrows) {
		this.savingThrows = savingThrows;
	}

	public String getProficiencies() {
		return Arrays.toString(proficiencies);
	}

	public void setProficiencies(String[] proficiencies) {
		this.proficiencies = proficiencies;
	}
	
	public String toString() {
		return name + " " + Arrays.toString(savingThrows) + " " + Arrays.toString(proficiencies);
	}
	
	public String toJSON() {
		return "\"" + name + "\"";
	}

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
