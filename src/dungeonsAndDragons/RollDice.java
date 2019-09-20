package dungeonsAndDragons;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

enum Dice{
	D4,D6,D8,D10,D12,D20,D100;
	
	/**
	 * Gets the number of sides a given dice has. As all dice names start with a "D" followed
	 * by an integer value we can just substring the value after the "D" and find the number
	 * of sides.
	 * 
	 * @return sides is the number of sides a given dice has.
	 */
	public int sides() {
		int sides = Integer.parseInt(this.toString().substring(1));
		return sides;
	}
}
public class RollDice {
	private final int D6 = Dice.D6.sides();
	
	/**
	 * Will roll a dice x times, x being defined by the user. As well as printing
	 * each roll to the console.
	 * 
	 * @param dice is the type of dice we're rolling
	 * 
	 * @param input is an object that gets an integer from the user
	 */
	public void rollX(Dice dice, GetInput input) {
		int num = input.getInteger("How many times would you like to roll the dice?");
		for(int i = 0;i<num;i++) {
			System.out.println(rollDice(dice));
		}
	}
	
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
	public int rollX(Dice dice, int x) {
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
			stat.add(rollDice(Dice.D6));
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
	public int rollDice(Dice dice) {
		//String dice=diceName.toUpperCase();
		return getRandBetween(1, dice.sides());
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
	public int getRandBetween(int low, int high) {
		return (int)((Math.random() * ((high + 1) - low)) + low);
	}

	
}
