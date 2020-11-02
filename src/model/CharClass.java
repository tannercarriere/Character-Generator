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

	public String[] getSavingThrows() {
		return savingThrows;
	}

	public void setSavingThrows(String[] savingThrows) {
		this.savingThrows = savingThrows;
	}

	public String[] getProficiencies() {
		return proficiencies;
	}

	public void setProficiencies(String[] proficiencies) {
		this.proficiencies = proficiencies;
	}
	
	public String toString() {
		return name + " " + Arrays.toString(savingThrows) + " " + Arrays.toString(proficiencies);
	}
	
	public String toJSON() {
		return "name: " + Arrays.toString(savingThrows) + " " + Arrays.toString(proficiencies);
	}
}
