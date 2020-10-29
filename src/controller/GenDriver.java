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

import model.CharacterSheet;

public class GenDriver {
	String filePath;
	int amount;
	final String NAMES_FILE = "Names.txt";
	final String RACES_FILE = "Races.txt";
	final String CLASSES_FILE = "Classes.txt";
	final String BACKGROUNDS_FILE = "Backgrounds.txt";
	final String SKILLS_FILE = "Skills.txt";
	final static String DEBUG_PATH = "C:\\Users\\Tanner\\Documents\\D&D\\Prog Sheets\\";
	final int CLASS = 0;
	final int BACKGROUND = 1;
	RollDice rd = new RollDice();
	GetInput input = new GetInput();
	List<String> NAMES;
	
	public GenDriver(String filePath, int amount){
		this.filePath = filePath;
		for(int c = 0; c < amount-1; c++) {
			create();
		}
	}
	
	/**
	 * create
	 * This is effectively the main() of the program, it calls all the needed things populate
	 * the maps and lists. As well as calling all methods need to use the stored info.
	 */
	public void create() {
		Map<String, HashMap<String, Integer>> races = new HashMap<String, HashMap<String,Integer>>();
		Map<String, ArrayList<String>> classesSave = new HashMap<String, ArrayList<String>>();
		Map<String, ArrayList<String>> classesSkills= new HashMap<String, ArrayList<String>>();
		Map<String, ArrayList<String>> backgrounds = new HashMap<String, ArrayList<String>>();
		Map<String, Integer> names = new HashMap<String, Integer>();
		Map<String, String> skills = new HashMap<String, String>();

		CharacterSheet playerCharacter = new CharacterSheet();
		racesInit(races);
		classInit(classesSave, classesSkills);
		backgroundInit(backgrounds);
		skillsInit(skills);
		namesInit(names);
		
		rollStats(playerCharacter);
		pickName(playerCharacter, names);
		pickRace(playerCharacter, races);
		pickClassOptions(playerCharacter, classesSave, classesSkills);
		pickPlayerOptions(playerCharacter, backgrounds);
		
		try {
			writeSheet(playerCharacter, names);
		}catch(IOException e) {
			System.out.println("Something went wrong, ending the program.");
			e.printStackTrace();
			System.exit(0);
		}
		
	}

	private void classInit(Map<String, ArrayList<String>> classes, Map<String, ArrayList<String>> classesSkills) {
		Scanner fileIn = openFileAsScanner(CLASSES_FILE);
		List<String> skills = new ArrayList<String>();
		String lineString;
		String[] line;
		String key;
		String[] modifiers;
		String name;
		int numChoices;
		while(fileIn.hasNextLine()) { // loop through whole file
			lineString = fileIn.nextLine(); // turn the line into something we can use
			if(lineString.charAt(0) != '#') { //ignore any line with a '#' as they are comments in file
				line = lineString.split(":"); 
				key = line[0]; //grab name of class
				modifiers = line[1].trim().split(","); //grab stat modifiers
				classes.put(key, new ArrayList<String>());
				classesSkills.put(key, new ArrayList<String>());
				
				for(int i = 0; i < modifiers.length; i++) { // add the modifiers to the class map
					name = modifiers[i];
					classes.get(key).add(name.trim());
				}
				
				modifiers = line[2].trim().split(";");
				numChoices = Integer.parseInt(modifiers[0]); // number of choices we have for skills
				Collections.addAll(skills, modifiers[1].trim().split(",")); // turn array into ArrayList
				for(int i = 0; i < numChoices; i++) { // do this a number of times equal to the number of skills the class has
					int index =rd.getRandBetween(0, skills.size()-1);
					String skill = skills.remove(index); //pick a skill
					classesSkills.get(key).add(skill.trim()); // add it to the skills
				}
			}
			skills.clear();// wipe skills  for next class
		}
		fileIn.close();
	}

	private void skillsInit(Map<String, String> skills) {
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
	private void namesInit(Map<String, Integer> names) {
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
	private void backgroundInit(Map<String, ArrayList<String>> map) {
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
					map.get(key).add(name.trim());
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
	private void racesInit(Map<String, HashMap<String, Integer>> races) {
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
						int val = rd.getRandBetween(-1, 1);
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
	private void pickPlayerOptions(CharacterSheet playerCharacter, 
			Map<String, ArrayList<String>> options) {
		int optVal = rd.getRandBetween(0,options.size()-1);
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
	private void pickClassOptions(CharacterSheet playerCharacter, 
			Map<String, ArrayList<String>> options, Map<String, ArrayList<String>> classesSkills) {
		int optVal = rd.getRandBetween(0,options.size()-1);
		String selectedOption = "";
		int i = 0;
		for(String s: options.keySet()) {
			if(i == optVal) {
				selectedOption = s;
				playerCharacter.setClass(selectedOption, options.get(selectedOption));
				playerCharacter.setSkills(classesSkills.get(s));
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
	private void pickName(CharacterSheet playerCharacter, Map<String, Integer> names) {
		int namePos = rd.getRandBetween(0, NAMES.size()-1);
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
	private void pickRace(CharacterSheet playerCharacter, Map<String, HashMap<String, Integer>> races) {
		int raceVal = rd.getRandBetween(0, races.size()-1); //selects the index of the race
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
    private Scanner openFileAsScanner(String fName) {
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
	public void rollStats(CharacterSheet player) {
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
	public void writeSheet(CharacterSheet playerCharacter, Map<String, Integer> names) throws IOException {
		String charName = playerCharacter.getName();
		String fileName = charName + " " +names.get(charName);
		File sheet = new File(filePath+ fileName +".txt");
		if(sheet.createNewFile()) {
			System.out.println("Creating character sheet...");
			PrintWriter pw = new PrintWriter(sheet);
			pw.println(playerCharacter);
			pw.close();
		}else {
			System.out.println("A character of that name already exsists, please input a different name");
		}
	}
}
