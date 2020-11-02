package controller;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import model.CharClass;
import model.CharacterSheet;

public class GenDriver {
	final static String filePath = "./Characters/";
	int amount;
	final static String NAMES_FILE = "Names.txt";
	final static String RACES_FILE = "Races.txt";
	final static String CLASSES_FILE = "Classes.txt";
	final static String BACKGROUNDS_FILE = "Backgrounds.txt";
	final static String SKILLS_FILE = "Skills.txt";
	final int CLASS = 0;
	final int BACKGROUND = 1;
	static RollDice rd = new RollDice();
	GetInput input = new GetInput();
	static List<String> NAMES;
	private static Map<String, HashMap<String, Integer>> races = new HashMap<String, HashMap<String,Integer>>();
	private static List<CharClass> classes = new LinkedList<>();
	private static Map<String, ArrayList<String>> backgrounds = new HashMap<String, ArrayList<String>>();
	private static Map<String, Integer> names = new HashMap<String, Integer>();
	private static Map<String, String> skills = new HashMap<String, String>();
	private static boolean instanced = false;
	
	/**
	 * create
	 * This is effectively the main() of the program, it calls all the needed things populate
	 * the maps and lists. As well as calling all methods need to use the stored info.
	 */
	public static void create() {
		if(!instanced) {
			racesInit(races);
			classInit(classes);
			backgroundInit(backgrounds);
			skillsInit(skills);
			namesInit(names);
			instanced = true;
		}
		
		CharacterSheet playerCharacter = new CharacterSheet();
		
		rollStats(playerCharacter);
		pickName(playerCharacter, names);
		pickRace(playerCharacter, races);
		pickClassOptions(playerCharacter, classes);
		pickPlayerOptions(playerCharacter, backgrounds);
		
		try {
			writeSheet(playerCharacter, names);
		}catch(IOException e) {
			System.out.println("Something went wrong, ending the program.");
			e.printStackTrace();
			System.exit(0);
		}
		
	}

	private static void classInit(List<CharClass> classes) {
		Scanner fileIn = openFileAsScanner(CLASSES_FILE);
		List<String> skills = new ArrayList<String>();
		String lineString;
		String[] line;
		String className;
		String[] savingThrowNames;
		String[] proficienciesNames;
		String[] proficiencies;
		CharClass c;
		int numProficiencies;
		
		while(fileIn.hasNextLine()) { // loop through whole file
			lineString = fileIn.nextLine(); // turn the line into something we can use
			if(lineString.charAt(0) != '#') { //ignore any line with a '#' as they are comments in file
				line = lineString.split(":"); 
				
				className = line[0]; //grab name of class
				savingThrowNames = line[1].trim().split(","); //grab names of the saving throws
				
				proficienciesNames = line[2].trim().split(";");
				numProficiencies = Integer.parseInt(proficienciesNames[0]); // number of choices we have for skills
				proficiencies = new String[numProficiencies];
				
				/*
				 * Turn the array into an ArrayList so that we can remove from it.
				 * 
				 * Could in theory be more efficient by doing a swap of the selected element
				 * and last element and narrowing the search area by one. However that's complicated
				 * and the program isn't time critical.
				 */
				Collections.addAll(skills, proficienciesNames[1].trim().split(",")); // turn array into ArrayList
				
				for(int i = 0; i < numProficiencies; i++) { // do this a number of times equal to the number of skills the class has
					int index = RollDice.getRandBetween(0, skills.size()-1);
					proficiencies[i] = skills.remove(index).trim(); //pick a skill
				}
				c = new CharClass(className, numProficiencies, savingThrowNames, proficiencies);
				classes.add(c);
			}
			skills.clear();// wipe skills for next class
		}
		fileIn.close();
	}

	private static void skillsInit(Map<String, String> skills) {
		Scanner fileIn = openFileAsScanner(SKILLS_FILE);
		String line;
		String[] temp;
		while(fileIn.hasNextLine()) {
			line = fileIn.nextLine();
			if(line.charAt(0) != '#') {
				temp = line.split(",");
				skills.put(temp[0].trim(), temp[1].trim());
			}
		}
		fileIn.close();
		
	}

	/**
	 * namesInit
	 * Goes through a list of names and stores them as an ArrayList of easy access later.
	 * Then store it into a HashMap to keep track of each names number of appearances.
	 * 
	 * @param names names is the HashMap we want to store the names and their appearances in.
	 */
	private static void namesInit(Map<String, Integer> names) {
		Scanner fileIn = openFileAsScanner(NAMES_FILE);
		String line;
		while(fileIn.hasNextLine()) {
			line = fileIn.nextLine();
			if(line.charAt(0) != '#') {
				names.put(line, 0);
			}
		}
		NAMES = new ArrayList<String>(names.keySet());
		fileIn.close();
	}

	/**
	 * mapInit
	 * Goes through a file and parses the file into a format the program can easily read.
	 * Storing names as a key and the info relevant to the name as an ArrayList of Strings
	 * 
	 * @param map map is the HashMap we want to store the info in
	 * 
	 * @param fileName fileName is the name of the file we want to store
	 */
	private static void backgroundInit(Map<String, ArrayList<String>> map) {
		Scanner fileIn = openFileAsScanner(BACKGROUNDS_FILE);
		String lineString;
		String[] line;
		String key;
		String[] modifiers;
		String name;
		while(fileIn.hasNextLine()) {
			lineString = fileIn.nextLine();
			if(lineString.charAt(0) != '#') {
				line = lineString.split(":");
				key = line[0];
				modifiers = line[1].trim().split(",");
				map.put(key, new ArrayList<String>());
				for(int i = 0; i < modifiers.length; i++) {
					name = modifiers[i];
					map.get(key).add("\""+name.trim()+"\"");
				}
			}
		}
		fileIn.close();
	}
	
	/**
	 * racesInit
	 * This takes a file full of information on D&D races and stores them in a HashMap.
	 * First taking the race's name and storing that as a key, then finding their stat
	 * modifiers and storing those in a different HashMap that the name points to.
	 * 
	 * @param races races is the HashMap we want to store the info in.
	 */
	private static void racesInit(Map<String, HashMap<String, Integer>> races) {
		Scanner fileIn = openFileAsScanner(RACES_FILE);
		String lineString;
		String[] line;
		String race;
		String[] modifiers;
		String[] hold;
		String name;
		int value = 0;
		while(fileIn.hasNextLine()) {
			lineString = fileIn.nextLine();
			if(lineString.charAt(0) != '#') {
				line = lineString.split(":");
				race = line[0];
				modifiers = line[1].trim().split(",");
				races.put(race, new HashMap<String, Integer>());
				
				for(int i = 0; i < modifiers.length; i++) {
					
					hold = modifiers[i].split(" ");
					if(hold[1].equals("or")) { //some classes have a choice of either or of two stats
											   //this is in that cases
						int val = RollDice.getRandBetween(-1, 1);
						if(val == 0) {
							name = hold[0];
						}else {
							name = hold[2];
						}
						value = Integer.parseInt(hold[hold.length-1]);
					}else {
						name = hold[0];
						value = Integer.parseInt(hold[1]);
					}
					races.get(race).put(name.trim(), value);
				}
			}
		}
		fileIn.close();
	}
	
	/**
	 * pickPlayerOptions
	 * This picks from one of two options, one being class the other being background. Since the
	 * files are similarly structured I've used this for both, it selects a position in a HashMap
	 * find the pos by looping through a the set of keys then writes the needed info then returns
	 * 
	 * @param playerCharacter playerCharacter is a CharacterSheet object that we write the info to
	 * 
	 * @param options is the HashMap of options we're choosing from
	 */
	private static void pickPlayerOptions(CharacterSheet playerCharacter, 
			Map<String, ArrayList<String>> options) {
		int optVal = RollDice.getRandBetween(0,options.size()-1);
		String selectedOption = "";
		int i = 0;
		for(String s: options.keySet()) {
			if(i == optVal) {
				selectedOption = s;
				playerCharacter.setBackground(selectedOption, options.get(selectedOption));
				return;
			}
			i++;
		}
	}
	
	/**
	 * pickClassOptions
	 * This picks from one of two options, one being class the other being background. Since the
	 * files are similarly structured I've used this for both, it selects a position in a HashMap
	 * find the pos by looping through a the set of keys then writes the needed info then returns
	 * 
	 * @param playerCharacter playerCharacter is a CharacterSheet object that we write the info to
	 * 
	 * @param options is the HashMap of options we're choosing from
	 * 
	 * @param classesSkills 
	 */
	private static void pickClassOptions(CharacterSheet playerCharacter,  List<CharClass> classes) {
		int optVal = RollDice.getRandBetween(0,classes.size()-1);
		int i = 0;
		for(CharClass c: classes) {
			if(i == optVal) {
				playerCharacter.setClass(c);
				return;
			}
			i++;
		}
	}
	
	/**
	 * pickName
	 * Picks an index in an ArrayList then takes the index sets the name and updates a HashMap
	 * to reflect the name has been used with it's current amount of uses.
	 * 
	 * @param playerCharacter playerCharacter is a CharacterSheet object that we write the name to
	 * 
	 * @param names names is a HashMap of names and their number of appearances 
	 */
	private static void pickName(CharacterSheet playerCharacter, Map<String, Integer> names) {
		int namePos = RollDice.getRandBetween(0, NAMES.size()-1);
		String name = NAMES.get(namePos);
		names.replace(name, 1+names.get(name));
		playerCharacter.setName(name);
	}
	
	/**
	 * pickRace
	 * Goes through a Map of Strings and Maps of more strings and Integers to pick a race based
	 * off of the index in the array. Loops through the the key set until it finds the selected
	 * race. Once found it sets the race and returns.
	 * 
	 * @param playerCharacter is the CharacterSheet object we're going to write the race to
	 * 
	 * @param races is the HashMap that contains all the race names and any modifiers they have
	 */
	private static void pickRace(CharacterSheet playerCharacter, Map<String, HashMap<String, Integer>> races) {
		int raceVal = RollDice.getRandBetween(0, races.size()-1); //selects the index of the race
		String race = "";
		int i = 0;
		for(String s: races.keySet()) { //loops through keys to find the given index
			if(i == raceVal) {
				race = s;
				playerCharacter.setRace(race, races.get(race));
				return; //returns once found
			}
			i++;
		}
	}
	
    /**
     * openFileAsScanner
     * Tries to open the file and returns it should the file exist
     * returns it as a Scanner to process the data
     * 
     * @param fName
     *            is the name of the file that will be opened
     * 
     * @return returns the opened file, should always return a file or exit the
     *         program
     */
    private static Scanner openFileAsScanner(String fName) {
        try {
            Scanner file = new Scanner(new File(fName));
            return file;
        } catch (Exception IOException) {
            System.err.println("File not found.");
            System.exit(-1);
        }

        return null;
    }
	
	/**
	 * rollStats
	 * rolls the stats for a character. It rolls 6 "d6" or six sided dies. In this case
	 * it runs a random number generator that can choose from one to 6 six times.
	 * 
	 * @param player 
	 * 			is a CharacterSheet object that holds the stats
	 */
	public static void rollStats(CharacterSheet player) {
		List<Integer> stats = new LinkedList<Integer>();
		
		for(int i = 0; i < player.getNumStats();i++) {
			stats.add(rd.rollStat());
		}
		player.setAllStats(stats);
	}
	
	/**
	 * writeSheet
	 * This writes a "Character Sheet." It takes a CharacterSheet object which contains all
	 * relevant information needed for the sheet and a names Map. The names map holds all possible
	 * names for the character. If a name appears more than once it will prevent file name clashes
	 * from incrementing a number attached to the file. 
	 * 
	 * @param playerCharacter 
	 * 			playerCharacter is the CharacterSheet object we're writing to a file.
	 * 
	 * @param names 
	 * 			names is a HashMap of names and the number of times each has appeared in this run of the program.
	 * 
	 * @throws IOException 
	 */
	public static void writeSheet(CharacterSheet playerCharacter, Map<String, Integer> names) throws IOException {
		String charName = playerCharacter.getName();
		String fileName = charName + " " +names.get(charName);
		File sheet = new File(filePath+ fileName +".txt");
		if(sheet.createNewFile()) {
			System.out.println("Creating character sheet...");
			PrintWriter pw = new PrintWriter(sheet);
			pw.println(playerCharacter.toJSON());
			pw.close();
		}else {
			System.out.println("A character of that name already exsists, please input a different name");
		}
	}
}
