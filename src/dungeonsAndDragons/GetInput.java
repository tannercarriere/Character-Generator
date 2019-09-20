package dungeonsAndDragons;

import java.util.Scanner;

public class GetInput {
	private Scanner keyboard = new Scanner(System.in);
	
	GetInput(){}

	/**
	 * Prompts the user for an integer. if the given input isn't an integer will prompt
	 * for re-entry
	 * 
	 * @param msg is a message we wish to display to the user
	 * 
	 * @return num is the integer we've gotten from the user
	 */
	public int getInteger(String msg){
		int num;
		System.out.println(msg);
		while(!keyboard.hasNextInt()) {
			System.err.println("No integer detected, please try again.");
			keyboard.nextLine();
		}
		num = keyboard.nextInt();
		return num;
	}
	

	/**
	 * Prompts the user for a String. if the given input is blank will prompt
	 * for re-entry
	 * 
	 * @param msg is a message we wish to display to the user
	 * 
	 * @return num is the String we've gotten from the user
	 */
	public String getString(String msg) {
		String hold;
		System.out.println(msg);
		while(!keyboard.hasNext()) {
			System.err.println("No input detected, please try again.");
			hold = keyboard.next();
		}
		hold = keyboard.next();
		return hold;
	}
	
}
