# Refactor in Progress
Things that need to be done.
1. Follow MVC design pattern. :heavy_check_mark:
2. Change view to a web based application: change to new goal, use this serverside in a web application.
3. Serialize model classes. Ideally using json files in order to better support new view idea. :heavy_check_mark:

## D&D character generator
This takes from several .txt files to create a random Dungeons and Dragons character. The 
character will consist of a race, class, background, will be given a name, and have skills selected
at random. All of these are the basics to first level Dungeons and Dragons character. The main idea
was to be able to have the building blocks to a character. Should a Dungeon Master need a random
Non Player Character or a new player joins the game and they need a character in a hurry.

Examples of character sheets can be found [here](https://dnd.wizards.com/articles/features/character_sheets)

## File Format
### config 
The first value found is the file directory seen as a default to save characters to. The second 
line found is the file path to the icon used for the program. The third thing found is the max 
number of characters that the program will be able to make at a time.
### Classes
The Classes file is formatted such that the first thing in the line is the name of the class, and 
following the ':' are the saving throws associated with the class. Following that it contains the
number of skills the class can choose from along with a list of available skills for that class.
### Backgrounds 
The Backgrounds file is formatted such that the first thing to appear is the name of the background
and everything following the ':' are the associated proficiency bonuses with the given background.
### Names
The Names file is formatted so that each line gets its own name.
### Races
The Races file is formatted such that the name of the race is the first to appear, and the sub-races
will appear in parenthesis next to it. Everything following the ':' are the stat bonuses associated
with the race. The first letter of each stat is capitalized as to match the formatting used for the
enum (if not the program crashes). After the name of the stat the amount it gets incremented is
shown next to a +/- sign. Each stat is separated by a comma. "Any" refers to any stat and will
be selected at random.


