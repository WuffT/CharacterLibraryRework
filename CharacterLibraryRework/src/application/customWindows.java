package application;

import java.awt.Desktop;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Random;

import javafx.animation.FadeTransition;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Duration;

public class customWindows extends mainUI{
	public static void showOptions(Stage primaryStage, StackPane rootPane) {
	    // Main layout for the options overlay (using VBox)
	    VBox optionsLayout = new VBox(15); // Use VBox for vertical stacking of elements
	    optionsLayout.setStyle("-fx-background-color: rgba(0, 0, 0, 0.7);"); // Semi-transparent background
	    optionsLayout.prefWidthProperty().bind(rootPane.widthProperty());
	    optionsLayout.prefHeightProperty().bind(rootPane.heightProperty());

	    // Title Bar with category buttons
	    HBox titleBar = new HBox(10); // Horizontal box for title and close button
	    titleBar.setAlignment(Pos.CENTER); // Center the title bar content
	    titleBar.setStyle("-fx-padding: 10; -fx-background-color: rgba(0, 0, 0, 0.7);"); // Title bar background

	    // Label for displaying content
	    Label helpLabel = new Label("Select a topic from the buttons above.");
	    helpLabel.setWrapText(true); // Allow text wrapping
	    helpLabel.setAlignment(Pos.TOP_LEFT); // Align text to the top-left corner
	    helpLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-padding: 10; -fx-alignment: center; -fx-background-color: black; -fx-background-radius: 15px;");

	    helpLabel.setMaxWidth(Double.MAX_VALUE); // Stretch to the width of the container
	    helpLabel.setMaxHeight(200); // Set a fixed maximum height for the Label

	    // Option Category Button
	    Button optionCategory = new Button("Options and Features");
	    optionCategory.getStyleClass().add("close");
	    optionCategory.setOnAction(e -> {
	        appMethods.playButtonSFX(); // Play button sound
	        // Add category-specific buttons
	        optionsLayout.getChildren().clear();
	        optionsLayout.getChildren().add(titleBar); // Re-add title bar

	        VBox optionsButtons = new VBox(10); // VBox to hold buttons vertically
	        optionsButtons.setAlignment(Pos.CENTER); // Center the buttons within the VBox

	        Button manageButton = new Button("Import CSV Data");
	        manageButton.getStyleClass().add("pink");
	        manageButton.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.5)); // 50% of the rootPane's width
	        manageButton.setOnAction(e1 -> {
	            appMethods.playButtonSFX(); // Play sound effect
	            FileChooser fileChooser = new FileChooser();
	            fileChooser.setTitle("Select New Character CSV File");
	            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV File", "*.csv"));
	            File selectedFile = fileChooser.showOpenDialog(rootPane.getScene().getWindow());
	            if (selectedFile != null) {
	                characterInfo.loadAndEditCSVFile(selectedFile);
	                System.out.println("CSV file selected: " + selectedFile.getAbsolutePath());
	            } else {
	                System.out.println("No file selected");
	            }
	        });
	        Button templateLink = new Button("Download CSV Template");
	        templateLink.getStyleClass().add("pink");
	        templateLink.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.5)); // 50% of the rootPane's width
	        templateLink.setOnAction(e2 -> {
	            appMethods.playButtonSFX(); // Play the sound effect

	            // Show a confirmation alert
	            Alert alert = new Alert(AlertType.CONFIRMATION);
	            alert.setTitle("Redirect Warning");
	            alert.setHeaderText("You are about to leave the application");
	            alert.setContentText("This will redirect you to a website to download the CSV template. Do you want to continue?");
	            
	            Optional<ButtonType> result = alert.showAndWait();
	            if (result.isPresent() && result.get() == ButtonType.OK) {
	                // Open the Google Sheets link in the default web browser
	                try {
	                    URI uri = new URI("https://docs.google.com/spreadsheets/d/1jdrQYk9TVaHQLFxEZyOv-7rGCXCYYlfTxMTyVLkGY6o/edit?usp=sharing");
	                    if (Desktop.isDesktopSupported() && Desktop.getDesktop().isSupported(Desktop.Action.BROWSE)) {
	                        Desktop.getDesktop().browse(uri);
	                    } else {
	                        System.out.println("Desktop browsing is not supported on this system.");
	                    }
	                } catch (Exception ex) {
	                    ex.printStackTrace();
	                }
	            } else {
	                // User clicked "No" or closed the dialog
	                System.out.println("Redirection canceled by the user.");
	            }
	        });
	        
	        Button resetDefault = new Button("Reset Default");
	        resetDefault.getStyleClass().add("pink");
	        resetDefault.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.5)); // 50% of the rootPane's width
	        resetDefault.setOnAction(e2 -> {
	            appMethods.playButtonSFX();
	            characterInfo.characterCategories.clear();
	            buttonContainer.getChildren().clear();
	            characterInfo.loadCharactersFromCSV("/characterCSV/characterData.csv");
	            characterInfo.refreshUI();
	        });

	        optionsButtons.getChildren().addAll(manageButton,templateLink, resetDefault);

	        optionsLayout.getChildren().add(optionsButtons);
	    });

	    // Help Category Button
	    Button helpCategory = new Button("Help");
	    helpCategory.getStyleClass().add("close");
	    helpCategory.setOnAction(e -> {
	        appMethods.playButtonSFX(); // Play button sound
	        optionsLayout.getChildren().clear();
	        optionsLayout.getChildren().add(titleBar); // Re-add title bar
	        optionsLayout.getChildren().add(helpLabel); // Add the Label for help content

	        VBox helpButtons = new VBox(10); // VBox to hold buttons vertically
	        helpButtons.setAlignment(Pos.CENTER); // Center the buttons within the VBox

	        Button howToButton = new Button("How to use");
	        howToButton.getStyleClass().add("pink");
	        howToButton.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.5));
	        howToButton.setOnAction(e1 -> {
	            appMethods.playButtonSFX();
	            helpLabel.setText("Navigate each character with the top left bar. When each one is clicked, it will display information of said character.");
	        });

	        Button updateNotesButton = new Button("Update Notes");
	        updateNotesButton.getStyleClass().add("pink");
	        updateNotesButton.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.5));
	        updateNotesButton.setOnAction(e2 -> {
	            appMethods.playButtonSFX();
	            helpLabel.setText("Update Changes and Notes as of " + applastUpdate + "\n"
	                    + "Version " + appVersion + " REWORKED:\n"
	                    + "- Application is in its testing phase of development\n"
	                    + "- Small improvements made to the UI.\n"
	                    + "- Custom characters can be made now, Tutorial on it coming soon");
	        });

	        Button appInfoButton = new Button("App Information");
	        appInfoButton.getStyleClass().add("pink");
	        appInfoButton.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.5));
	        appInfoButton.setOnAction(e3 -> {
	            appMethods.playButtonSFX();
	            helpLabel.setText("CHARACTER INFO LIBRARY\n"
	                    + "About: The application showcases a variety of characters by description, statistics, and full image references.\n"
	                    + "Developed By: WolfTical\n"
	                    + "Current App Version: " + appVersion + " REWORKED\n"
	                    + "Copyright � 2023 Owned by WolfTical and MegaNekaii. All Rights Reserved.");
	        });

	        helpButtons.getChildren().addAll(howToButton, updateNotesButton, appInfoButton);

	        // Create a new VBox to stack buttons and the label
	        VBox helpSection = new VBox(15);
	        helpSection.setAlignment(Pos.TOP_CENTER); // Align to the top center
	      
	        helpSection.getChildren().addAll(helpButtons, helpLabel); // Add buttons and label to the section

	        optionsLayout.getChildren().add(helpSection);
	    });

	    // Close Button
	    Button closeButton = new Button("Close");
	    closeButton.getStyleClass().add("close");
	    closeButton.setOnAction(e -> {
	        appMethods.playButtonSFX(); // Play close button sound
	        rootPane.getChildren().remove(optionsLayout); // Remove the overlay
	    });

	    titleBar.getChildren().addAll(optionCategory, helpCategory, closeButton);

	    optionsLayout.getChildren().add(titleBar);

	    rootPane.getChildren().add(optionsLayout);

	    optionsLayout.getStylesheets().add(mainUI.class.getResource("applicationUISheet.css").toExternalForm());
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