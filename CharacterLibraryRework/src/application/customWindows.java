package application;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Random;

import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

public class customWindows extends mainUI{

	public static void showOptions() {
	    // Create a new stage for the Options window
	    Stage popupStage = new Stage();
	    popupStage.setTitle("Options");
	    popupStage.initModality(Modality.APPLICATION_MODAL);  // Make the popup modal

	    // Title label
	    Label titleLabel = new Label("Options and Features");
	    titleLabel.getStyleClass().add("menuLabel");
	    titleLabel.setAlignment(Pos.CENTER);

	    // Create layout
	    VBox layout = new VBox(15);
	    layout.setAlignment(Pos.CENTER);
	    layout.setPadding(new Insets(20));
	    layout.setStyle(("-fx-background-image: url('imageSamples/BorderPink.png'); " +
	            "-fx-background-repeat: no-repeat; " +
	            "-fx-background-size: 100% 100%; " +
	            "-fx-border-width: 5; " +                   // Border width
	            "-fx-border-color: #000000; "));

	    // Button for "Manage" (for loading and editing)
	    Button manageButton = new Button("Manage");
	    manageButton.getStyleClass().add("pink");
	    manageButton.setOnAction(e -> {
	        // Play the button sound effect
	        appMethods.playButtonSFX();
	        // Open a FileChooser to select a new CSV file
	        FileChooser fileChooser = new FileChooser();
	        fileChooser.setTitle("Select New Character CSV File");
	        fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV File", "*.csv"));
	        
	        // Show the FileChooser dialog and get the selected file
	        File selectedFile = fileChooser.showOpenDialog(null); // Replace `null` with your primary stage if available

	        // Check if a file was selected (user didn't cancel the dialog)
	        if (selectedFile != null) {
	            // Call the method to load and edit the CSV file (custom method to handle file editing)
	            characterInfo.loadAndEditCSVFile(selectedFile);

	            // Optionally, provide feedback to the user (e.g., display success message)
	            System.out.println("CSV file selected: " + selectedFile.getAbsolutePath());
	        } else {
	            // If no file was selected, you can provide feedback or just do nothing
	            System.out.println("No file selected");
	        }
	    });

	    // Text Area (Initially empty, hidden)
	    TextArea infoTextArea = new TextArea();
	    infoTextArea.getStyleClass().add("vbox-background");
	    infoTextArea.setWrapText(true);
	    infoTextArea.setEditable(true); // Initially editable, but hidden
	    infoTextArea.setMaxWidth(Double.MAX_VALUE);
	    infoTextArea.setMaxHeight(Double.MAX_VALUE);
	    infoTextArea.setVisible(false); // Initially hidden

	    // Button for "Create Text File"
	    Button createTextFileButton = new Button("Create Text File");
	    createTextFileButton.getStyleClass().add("pink");
	    createTextFileButton.setOnAction(e -> {
	        appMethods.playButtonSFX();
	        infoTextArea.setVisible(true); // Make the text area visible when this button is clicked
	        infoTextArea.setPromptText("Type character info here to save it as a text file.");	    });

	    // Button to save the content as a .txt file
	    Button saveButton = new Button("Save Text File");
	    saveButton.getStyleClass().add("pink");
	    saveButton.setOnAction(saveEvent -> {
	        String content = infoTextArea.getText();
	        if (!content.isEmpty()) {
	            // Save the text to a file
	            FileChooser fileChooser = new FileChooser();
	            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("Text Files", "*.txt"));
	            File file = fileChooser.showSaveDialog(popupStage);

	            if (file != null) {
	                try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
	                    writer.write(content);
	                    System.out.println("File saved: " + file.getAbsolutePath());
	                } catch (IOException ioException) {
	                    System.err.println("Error saving file: " + ioException.getMessage());
	                }
	            }
	        } else {
	            infoTextArea.setText("Please enter some text to save.");
	        }
	    });

	    // Close button to close the options window
	    Button closeButton = new Button("Close");
	    closeButton.getStyleClass().add("close");
	    closeButton.setOnAction(e -> {
	        appMethods.playButtonSFX();
	        popupStage.close();
	    });

	    // Organize the buttons in a VBox
	    VBox buttonBox = new VBox(10);
	    buttonBox.setAlignment(Pos.CENTER);
	    buttonBox.getChildren().addAll(manageButton, createTextFileButton, saveButton);

	    // Add the text area and button box to the layout
	    layout.getChildren().addAll(titleLabel, buttonBox,infoTextArea,  closeButton);

	    // Create the scene for the popup
	    Scene scene = new Scene(layout, 600, 800); // Adjust size as needed
	    scene.getStylesheets().add(mainUI.class.getResource("applicationUISheet.css").toExternalForm());

	    titleLabel.prefWidthProperty().bind(layout.widthProperty());
	    infoTextArea.prefWidthProperty().bind(scene.widthProperty().multiply(0.8));
	    infoTextArea.prefHeightProperty().bind(scene.heightProperty().multiply(0.5));
	    popupStage.setScene(scene);
	    popupStage.showAndWait(); // Show the popup and wait for it to be closed
	}
	
    public static void showHelp() {
        // Create a new stage for the Help window
        Stage popupStage = new Stage();
        popupStage.setTitle("Help");
        popupStage.initModality(Modality.APPLICATION_MODAL);  // Make the popup modal

   
      

        // Title label
        Label titleLabel = new Label("Help and Instructions");
        titleLabel.getStyleClass().add("menuLabel");
        titleLabel.setAlignment(Pos.CENTER);

        
        
     // Text Area that displays the text 
        TextArea helpTextArea = new TextArea("Select a topic from the buttons below.");
        helpTextArea.getStyleClass().add("vbox-background");
        helpTextArea.setWrapText(true);
        helpTextArea.setEditable(false);

        // Allow the TextArea to scale with the window size
        VBox.setVgrow(helpTextArea, Priority.ALWAYS);
        helpTextArea.setMaxWidth(Double.MAX_VALUE);
        helpTextArea.setMaxHeight(Double.MAX_VALUE);

     


        // Button for "how to use"
        Button inputHelpButton = new Button("How to use");
        inputHelpButton.getStyleClass().add("pink");
        
        inputHelpButton.setOnAction(e -> {appMethods.playButtonSFX();
            helpTextArea.setText("Navigate each character with the top left bar, when each one is clicked it will display information of said character");
        });

        Button updateNotesButton = new Button("Update Notes");
        updateNotesButton.getStyleClass().add("pink");
        updateNotesButton.setOnAction(e -> {
            appMethods.playButtonSFX();
            helpTextArea.setText("Update Changes and Notes as of " + applastUpdate + "\r\n"
                    + "Version " + appVersion + " REWORKED:\r\n"
                    + "- Application is in its reworking stage and early development\r\n"
                    + "- New Character Data Loading and editing Feature using a CSV file.\r\n"
                    + "- Custom characters can be made now, Tutorial on it coming soon");
        });

        // Button for "App Information"
        Button appInfoButton = new Button("App Information");
        appInfoButton.getStyleClass().add("pink");
        
        appInfoButton.setOnAction(e -> {appMethods.playButtonSFX();
            helpTextArea.setText("CHARACTER INFO LIBRARY\n"
                    + "About: The application showcases a variety of characters by description, statistics, and full image references.\n"
                    + "Developed By: WolfTical\n"
                    + "Current App Version: " + appVersion + " REWORKED\n"
                    + "Copyright © 2023 Owned by WolfTical and MegaNekaii. All Rights Reserved.");
        });

       

        // Close button to close the help window
        Button closeButton = new Button("Close");
        closeButton.getStyleClass().add("close");
        
        closeButton.setOnAction(e -> {appMethods.playButtonSFX(); popupStage.close();});

        // Organize the buttons in a VBox
        VBox buttonBox = new VBox(10);
        buttonBox.setAlignment(Pos.CENTER);
        buttonBox.getChildren().addAll(inputHelpButton, updateNotesButton, appInfoButton);

        // Main layout containing the icon, title, buttons, and help content
        VBox layout = new VBox(15);
        layout.getChildren().addAll(titleLabel, buttonBox, helpTextArea, closeButton);
        layout.setAlignment(Pos.CENTER);
        layout.setPadding(new Insets(20));
        layout.setStyle(("-fx-background-image: url('imageSamples/BorderPink.png'); " +
                "-fx-background-repeat: no-repeat; " +
                "-fx-background-size: 100% 100%; " +
                "-fx-border-width: 5; " +                   // Border width
                "-fx-border-color: #000000; "));

        // Create the scene for the popup
        Scene scene = new Scene(layout, 600, 800); // Adjust size as needed
        scene.getStylesheets().add(mainUI.class.getResource("applicationUISheet.css").toExternalForm());
        
        
        titleLabel.prefWidthProperty().bind(layout.widthProperty());
        helpTextArea.prefWidthProperty().bind(scene.widthProperty().multiply(0.8));
        helpTextArea.prefHeightProperty().bind(scene.heightProperty().multiply(0.5));
        popupStage.setScene(scene);
        popupStage.showAndWait(); // Show the popup and wait for it to be closed
    }
    
    
    public static void showCharacterRender(String characterName) {
        characterInfo character = characterInfo.characterMap.get(characterName);
        
        if (character != null) {
            // Create a new window to display the character render
            Stage renderStage = new Stage();
            renderStage.setTitle(character.getName() + " - Character Render");

            // Create an ImageView for the character's render
            ImageView renderImageView = new ImageView();
            renderImageView.setImage(new Image(character.getRenderPath()));
            
            // Set properties to maintain aspect ratio and fill the window
            renderImageView.setPreserveRatio(true);  // Preserve the aspect ratio
            renderImageView.setFitWidth(600);  // Set the width to fill the window
            renderImageView.setFitHeight(600);  // Set the height to fill the window

            // Set up the layout for the render window
            VBox renderLayout = new VBox(10);
            renderLayout.setAlignment(Pos.CENTER);
            renderLayout.getChildren().addAll(renderImageView);

            // Set up the scene and show the render window
            Scene renderScene = new Scene(renderLayout, 600, 600);
            renderStage.setScene(renderScene);
            renderStage.show();
          
            // Optionally, you can add an event listener to resize the image as the window size changes
            renderStage.widthProperty().addListener((observable, oldWidth, newWidth) -> {
                renderImageView.setFitWidth(newWidth.doubleValue());
            });
            
            renderStage.heightProperty().addListener((observable, oldHeight, newHeight) -> {
                renderImageView.setFitHeight(newHeight.doubleValue());
            });
        } else {
            System.out.println("Character not found: " + characterName);
        }
    }
    
    

   

    public static Task<Void> simulateLoading(VBox loadingScreen, Stage stage) {
        // Set up the loading screen
        loadingScreen.setVisible(true);
        loadingScreen.getStylesheets().add(mainUI.class.getResource("applicationUISheet.css").toExternalForm());

        ProgressBar progressBar = new ProgressBar(0);
        Label loadingLabel = new Label("");
        loadingLabel.getStyleClass().add("loading");
        loadingScreen.getChildren().addAll(progressBar, loadingLabel);

        Button enterDatabaseButton = new Button("Enter The Database");
        enterDatabaseButton.setVisible(false);
        enterDatabaseButton.getStyleClass().add("close");
        enterDatabaseButton.setOnAction(event -> {
            appMethods.playButtonSFX();
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), loadingScreen);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> loadingScreen.setVisible(false));
            fadeOut.play();
            appMethods.playSelectedAudio("ThematicHeroes");
        });
        loadingScreen.getChildren().add(enterDatabaseButton);

        Button skipButton = new Button("Skip");
        skipButton.setVisible(true);
        skipButton.getStyleClass().add("close");
        loadingScreen.getChildren().add(skipButton);

        // Define the loading task
        Task<Void> loadingTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                appMethods.playSelectedAudio("loadingBeep");

                // Count the number of rows (excluding the header) in the CSV file
                int totalItems = 0;
                try (BufferedReader br = new BufferedReader(
                        new InputStreamReader(mainUI.class.getResourceAsStream("/characterCSV/characterData.csv")))) {
                    String line;
                    boolean isFirstLine = true;
                    while ((line = br.readLine()) != null) {
                        if (isFirstLine) {
                            isFirstLine = false; // Skip the header row
                            continue;
                        }
                        totalItems++;
                    }
                }

                for (int i = 0; i < totalItems; i++) {
                    if (isCancelled()) break;

                    // Update progress as numbers only
                    updateMessage("Loading Characters: " + (i + 1) + "/" + totalItems);
                    updateProgress(i + 1, totalItems);

                    // Simulate a delay for each item
                    Thread.sleep(300);
                }

                // Update after loading completes
                skipButton.setVisible(false);
                updateMessage("Loading Success!");
                Thread.sleep(1000);
                enterDatabaseButton.setVisible(true);

                return null;
            }
        };

        // Bind progress and message properties
        progressBar.progressProperty().bind(loadingTask.progressProperty());
        loadingLabel.textProperty().bind(loadingTask.messageProperty());

        // Define the skip button behavior
        skipButton.setOnAction(event -> {
            loadingTask.cancel();
            appMethods.playButtonSFX();
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), loadingScreen);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> loadingScreen.setVisible(false));
            fadeOut.play();
            appMethods.playSelectedAudio("ThematicHeroes");
        });

        // Start the loading task in a new thread
        new Thread(loadingTask).start();

        return loadingTask;
    }


    
}