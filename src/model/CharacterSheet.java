package model;
/**
 *This is a character sheet object for the Table top role playing game Dungeons and Dragons. This
 * class holds all given values needed to create a character. 
 * 
 * By: Tanner Carriere
 */
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

enum Stats{
	/**
	 * The stats enum holds each of the six individual stats. 
	 * randStat chooses a random name from the enum.
	 */
	Str, Dex, Con, Int, Wis, Cha;
	
	/**
	 * randStats
	 * takes a number between 0 and the number of enums in the list. then returns a random enum
	 * name.
	 * 
	 *  @return returns the name of a random enum.
	 */
	public static String randStat() {
		Random gen = new Random();
		int size = Stats.values().length;
		return Stats.values()[gen.nextInt(size)].toString();
	}
}

public class CharacterSheet {
	private final int NUM_STATS = 6; //total number of stats a character can have
	private final Stats[] STAT_NAMES = Stats.values(); //an array of all the enum names
	private Map<Stats, Integer> stats = new HashMap<Stats, Integer>(); //a map of stat names and their scores
	private List<String> savingThrows;//a list of all the saving throws the character is proficient in.
	private List<String> backgroundProficencies;//a list of skills the character is proficient in given their background
	private String name;//character's name
	private String race;//character's race
	private String charClass;//character's class
	private int lvl = 1;//starting level for all characters
	private String background;//character's background
	private List<String> proficiencientSkills;//list of skills the character is proficient in
	
	/**
	 * Constructor for the character. Sets all stats to zero.
	 */
	public CharacterSheet() {
		for(Stats stat: STAT_NAMES) {
			stats.put(stat, 0);
		}
	}
	
	/**
	 * addModifiers
	 * Every stat is given a base value 3-18, certain things can augement the stat (i.e. the character's
	 * race). This is called to update the given stat with the given bonus.
	 * 
	 * @param statName the name of the stat we wish to update.
	 * 
	 * @param statBonus the value we want to augment the stat by.
	 */
	public void addModifiers(String statName, int statBonus) {
		int statVal;
		statVal = stats.get(Stats.valueOf(statName));
		stats.replace(Stats.valueOf(statName), statVal+statBonus);
	}
	
	/**
	 * statBonus
	 * Every stat is gives a certain bonus to skill checks. for every multiple of two this bonus
	 * modifies by one. For example 10 will give a +0, 12 will give a +1, and 8 will give a -1.
	 * This finds that modifies recursively.
	 * 
	 * @param stat is the value of the stat we want to know the modifier of
	 * 
	 * @return it returns the modifier of the stat we send in
	 */
	private int statBonus(int stat) {
		if(stat == 1) {
			return -5;
		}else if (stat%2 != 0){
			return statBonus(stat-1);
		}else {
			return statBonus(stat-1) + 1;
		}
	}
	
	/**
	 * @return name of character
	 */
	public String getName() {
		return name;
	}
	
	/**
	 * @return the level of the character
	 */
	public int getLevel() {
		return lvl;
	}
	
	/**
	 * @return the characters class
	 */
	public String getCharClass() {
		return charClass;
	}
	
	/**
	 * @return the list of saving throws
	 */
	public String getSavingThrows() {
		return savingThrows.toString();
	}
	
	/**
	 * @return the characters race
	 */
	public String getRace() {
		return race;
	}
	
	/**
	 * @return the number of stats a sheet has
	 */
	public int getNumStats() {
		return NUM_STATS;
	}
	
	/**
	 * AC is a value a character must meet to do damage against another character. Without armor
	 * this value is 10+ the dex modifier.
	 * 
	 * @return the AC of a given character
	 */
	public int getAC() {
		return 10 + getInitative();
	}
	
	/**
	 * @return the background of the character
	 */
	public String getBackground() {
		return background + ", background proficiencies: " + backgroundProficencies;
	}
	
	/**
	 * initative is the order characters go in combat, this gets the bonus added to an initative roll.
	 * Which is just the dex modifier.
	 */
	public int getInitative() {
		int dex = stats.get(Stats.valueOf("Dex"));
		return statBonus(dex);
	}
	
	/**
	 * @param name the name we wish to give this character
	 */
	public void setName(String name) {
		this.name = name;
	}
	
	/**
	 * sets the race of the character and modifies any associated stats that come with that race.
	 * 
	 * @param race is the name of the race we want to give the character
	 * 
	 * @param statBonus is the map of stats so we can modify the associated stats of the character
	 */
	public void setRace(String race, Map<String, Integer> statBonus) {
		this.race = race;
		for(String stat: statBonus.keySet()) {
			if(stat.equals("Any")) {
				for(int i = 0; i < statBonus.get(stat); i++) {
					addModifiers(Stats.randStat(), statBonus.get(stat));
				}
			}else {
				addModifiers(stat, statBonus.get(stat));
			}
		}
	}
	
	/**
	 * @param charClass is the class we want to give this character
	 * 
	 * @param savingThrows is a list of associated saving throws to the given class
	 */
	public void setClass(String charClass, ArrayList<String> savingThrows) {
		this.charClass = charClass;
		setSavingThrows(savingThrows);
	}
	
	/**
	 * @param savingThrows is a list of associated saving throws to a given class
	 */
	private void setSavingThrows(ArrayList<String> savingThrows) {
		this.savingThrows = new ArrayList<String>(savingThrows);
	}
	
	/**
	 * @param skills are the skills we've chosen for this character
	 */
	public void setSkills(List<String> skills) {
		this.proficiencientSkills = new ArrayList<String>(skills);
	}
	
	/**
	 * @param background is the background we want to set for this character
	 * 
	 * @param backProficiencies is a list of associated proficiencies to the given background
	 */
	public void setBackground(String background, ArrayList<String> backProficiencies) {
		this.background = background;
		setBackgroundProficienies(backProficiencies);
	}
	
	/**
	 * @param backProficiencies is a list of associated proficiencies to a given background
	 */
	private void setBackgroundProficienies(ArrayList<String> backProficiencies) {
		this.backgroundProficencies = new ArrayList<String>(backProficiencies);
	}
	
	/**
	 * @param stats is a list of randomly generated stats 3-18
	 */
	public void setAllStats(List<Integer> stats) {
		LinkedList<Integer> hold = (LinkedList<Integer>) stats;
		for(Stats stat: STAT_NAMES) {
			this.stats.replace(stat, hold.pop());
		}
	}
	
	/**
	 * toString
	 * returns the character sheet represented as a string
	 * 
	 * @return ret is the object turned into a String
	 */
	public String toString() {
		String ret = "";
		ret += "Name: " + getName() + "\n";
		ret += "Race: " + getRace() + "\n";
		ret += "Class: " + getCharClass() + "\n";
		ret += "lvl: " + getLevel() + "\n";
		ret += "Background: " + getBackground() + "\n";
		ret += "Saving Throws: " + getSavingThrows() + "\n";
		ret += "Proficiency Bonus: " + "+2" + "\n";
		ret += "AC: " + getAC() + "\n";
		ret += "Initiative: " + getInitative() + "\n";
		for(int i = 0; i < STAT_NAMES.length; i++) {
			ret += STAT_NAMES[i] + ": " + stats.get(STAT_NAMES[i])+"\n";
		}
		ret += "Proficient Skills: ";
		System.out.println(proficiencientSkills);
		for(String skill : proficiencientSkills) {
			ret += "\n  " + skill;
		}
		return ret;
	}





}
