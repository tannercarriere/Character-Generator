package controller;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class RollDice {
	//support for legacy code that used enum
	private final int D6 = 6;
	//Overloaded method for when dice rolling is done by the program rather than the user
	/**
	 * Will roll a dice x times, and adds up the total rolls.
	 * 
	 * @param dice is a dice we're rolling
	 * 
	 * @param x is the number of times we're rolling a dice
	 * 
	 * @return results is the final number we get from rolling the dice
	 */
	public static int rollX(int dice, int x) {
		int results = 0;
		for(int i = 0;i<x;i++) {
			results+= rollDice(dice);
		}
		return results;
	}
	
	/**
	 * Rolls the stats from a Dungeons and Dragons character. It rolls 4d6 (4 six sided die)
	 * then drops the lowest number rolled. After that it adds up each number rolled and returns
	 * it as an integer between the values 3-18.
	 * 
	 * @return ret is an integer whose value lies between 3-18
	 */
	public int rollStat() {
		List<Integer> stat = new ArrayList<Integer>();
		int ret = 0;
		for(int i = 0; i < 4; i++) {
			stat.add(rollDice(D6));
		}
		stat.remove(stat.indexOf(Collections.min(stat)));
		for(int i : stat) {
			ret += i;
		}
		return ret;
	}
	
	/**
	 * Gets a number between one and the number of sides of the dice passed down.
	 * 
	 * @param dice is a dice who we want to "roll"
	 * 
	 * @return is the value rolled by the dice
	 */
	public static int rollDice(int dice) {
		//String dice=diceName.toUpperCase();
		return getRandBetween(1, dice);
	}
	
	/**
	 * Gets a random value between two given values.
	 * 
	 * @param low is the lowest value we want
	 * 
	 * @param high is the highest value we want
	 * 
	 * @return is the value we found
	 */
	public static int getRandBetween(int low, int high) {
		return (int)((Math.random() * ((high + 1) - low)) + low);
	}

	
}
