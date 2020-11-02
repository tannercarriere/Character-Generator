package view;

/**
 * This a GUI wrapper for the GenDriver class.
 * Consists of two main elements in a ComboBox and Button
 * The ComboBox is used to pick how many characters that want to be generated the button calls
 * the GenDriver class.
 */
import java.io.File;
import java.util.Scanner;

import controller.GenDriver;
import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class Run extends Application{
	private final String CONFIG_FILE = "config.txt";
	private final int WIDTH = 450;
	private final int HEIGHT = 190;
	private final int X_PADDING = 5;
	private final int Y_PADDING = 2;
	private final int V_GAP = 4;
	private int MAX;
	
	public static void main(String[] args) {
		launch(args);
	}
	
	@Override
	public void start(Stage primaryStage) throws Exception {
		GridPane entryHolder = new GridPane();
		Scene display = new Scene(entryHolder, WIDTH, HEIGHT);
		String superFilePath;
		Label comboBoxName = new Label("How many characters would you like to create?");
		Button create = new Button("Create");
		ComboBox<Integer> numToCreate = new ComboBox<Integer>();
		Scanner configFile = openFileAsScanner(CONFIG_FILE);
		

		setUpGrid(comboBoxName, numToCreate, create, entryHolder);
		superFilePath = next(configFile);
		setStage(primaryStage, configFile);
		MAX = Integer.parseInt(next(configFile));
		setUpComboBox(numToCreate);
		
		entryHolder.getChildren().addAll(comboBoxName, numToCreate, create);
		
		create.setOnAction((e) ->{
			int amount = numToCreate.getValue();
			while(amount > 0) {
				GenDriver.create();
				amount--;
			}
		});
		
		primaryStage.setScene(display);
		
		primaryStage.show();
	}
	
	/**
	 * setStage
	 * gives stage a title and an icon. Additionally it sets the width and height of the box.
	 */
	private void setStage(Stage stage, Scanner config) {
		String icon = "file:" + next(config);
		stage.setTitle("Character Generator");
		stage.getIcons().add(new Image(icon));
		stage.setMaxHeight(HEIGHT);
		stage.setMaxWidth(WIDTH);
	}

	/**
	 * setUpComboBox
	 * adds a number of numbers equal to the max setting denoting how many characters we want to
	 * create.
	 */
	private void setUpComboBox(ComboBox<Integer> numToCreate) {
		numToCreate.setPrefWidth(WIDTH);
		for(int i = 1; i <= MAX; i++) {
			numToCreate.getItems().add(i);
		}
		numToCreate.setValue(1);
	}

	/**
	 * setUpGrid
	 * formats the gridpane to look not have all the objects cramped together.
	 */
	private void setUpGrid(Label textAreaName, ComboBox<Integer> numToCreate, Button create, 
			GridPane entryHolder) {
		//adding setting pos of everything in grid
		GridPane.setConstraints(textAreaName, 0, 0);
		GridPane.setConstraints(numToCreate, 0, 3);
		GridPane.setConstraints(create, 0, 4);
		
		//Setting up the grid's spacing
		entryHolder.setVgap(V_GAP);
		entryHolder.setPadding(new Insets(X_PADDING, Y_PADDING, Y_PADDING, X_PADDING));
		entryHolder.setPrefHeight(HEIGHT);
		entryHolder.setPrefWidth(WIDTH);
		
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
    
    private String next(Scanner in) {
    	String out;
    	while(in.hasNextLine()) {
    		out = in.nextLine();
    		if(out.charAt(0) != '#') {
    			return out;
    		}
    	}
    	return null;
    }

}
