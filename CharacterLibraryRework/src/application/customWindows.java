package application;

import java.io.File;
import java.net.URL;
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
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.Duration;

public class customWindows extends mainUI{


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
                    + "- New Character Data Loading Feature using a CSV file.\r\n"
                    + "- Better categorizing");
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
        loadingScreen.setVisible(true);
        loadingScreen.getStylesheets().add(mainUI.class.getResource("applicationUISheet.css").toExternalForm());
        // Add a ProgressBar
        ProgressBar progressBar = new ProgressBar(0); // Initial progress is 0
        Label loadingLabel = new Label(""); // Initial loading text
        loadingLabel.getStyleClass().add("loading");
        loadingScreen.getChildren().addAll(progressBar, loadingLabel); // Add to the loading screen
        
        // Create the "Enter The Database" button, initially hidden
        Button enterDatabaseButton = new Button("Enter The Database");
        enterDatabaseButton.setVisible(false); // Initially hidden
        enterDatabaseButton.getStyleClass().add("close");
        
        // Add action to the button when clicked
        enterDatabaseButton.setOnAction(event -> {
            // Start fade-out when button is clicked
        	appMethods.playButtonSFX();
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), loadingScreen);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> loadingScreen.setVisible(false)); // Hide after fade
            fadeOut.play(); // Start the fade-out animation
            appMethods.playSelectedAudio("ThematicHeroes");
        });

        // Add the button to the loading screen
        loadingScreen.getChildren().add(enterDatabaseButton);

        Button skipButton = new Button("Skip");
        skipButton.setVisible(true); // Show the Skip button during loading
        skipButton.getStyleClass().add("close");
        loadingScreen.getChildren().add(skipButton);
        
        Task<Void> loadingTask = new Task<Void>() {
            @Override
            protected Void call() throws Exception {
                appMethods.playSelectedAudio("loadingBeep");

                // Total number of items to "load"
                int totalItems = 78;

                for (int i = 1; i <= totalItems; i++) {
                    if (isCancelled()) break;

                    // Simulate loading each item with a delay
                    updateMessage("Loading.. " + i + "/" + totalItems);
                    Thread.sleep(150); // Simulate the loading time for each item
                    updateProgress(i, totalItems); // Update progress
                }
                // Hide the skip button and show the "Enter The Database" button
                skipButton.setVisible(false);
                // After the task is complete, update the label to show success message
                updateMessage("Loading Success!");
                Thread.sleep(1000);

               
                enterDatabaseButton.setVisible(true);

                return null;
            }
        };

        // Bind the ProgressBar's progress property to the Task's progress
        progressBar.progressProperty().bind(loadingTask.progressProperty());
        // Bind the Label's text to the Task's message property
        loadingLabel.textProperty().bind(loadingTask.messageProperty());
     
        // Skip button functionality: Cancel the loading task
        skipButton.setOnAction(event -> {
            loadingTask.cancel(); // Cancel the task
            // Start fade-out when button is clicked
        	appMethods.playButtonSFX();
            FadeTransition fadeOut = new FadeTransition(Duration.seconds(1), loadingScreen);
            fadeOut.setFromValue(1.0);
            fadeOut.setToValue(0.0);
            fadeOut.setOnFinished(e -> loadingScreen.setVisible(false)); // Hide after fade
            fadeOut.play(); // Start the fade-out animation
            appMethods.playSelectedAudio("ThematicHeroes");
   
        });
        
        // Start the Task in a separate thread
        new Thread(loadingTask).start();
        
        return loadingTask; // Return the task so it can be canceled later
    }
    
}