package application;

import javax.sound.sampled.*;

import javafx.animation.FadeTransition;
import javafx.animation.Interpolator;
import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.PauseTransition;
import javafx.animation.Timeline;
import javafx.animation.TranslateTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.Tooltip;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class appMethods extends customWindows {
	   public static Clip selectedClip;
	   public static Clip buttonClick;
	   static String quickNotice = "This is an important section filled with a lot of lore elements. Left behind are small entries of a characters past, present and sometimes future...";
	   public static void playSelectedAudio(String audioPath) {
		    try {
		        // Stop and close the currently playing audio (if any)
		        if (selectedClip != null && selectedClip.isRunning()) {
		            selectedClip.stop();
		            selectedClip.close();
		        }

		        // Load the selected audio file THIS part loads the file by the song name only since its concatenated
		        URL audioURL = mainUI.class.getResource("/audioSamples/" + audioPath + ".wav"); 
		        if (audioURL == null) {
		            System.out.println("Audio file not found: " + audioPath);
		            return;
		        }

		        AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(audioURL);
		        selectedClip = AudioSystem.getClip();
		        selectedClip.open(audioInputStream);

		        // Start playing the audio
		        selectedClip.start();
		        selectedClip.loop(Clip.LOOP_CONTINUOUSLY); 
		        System.out.println("Playing audio: " + audioPath);

		    } catch (Exception e) {
		        e.printStackTrace();
		    }
		}

    // Method to stop the music
    public static void stopMusic() {
        if (selectedClip != null && selectedClip.isRunning()) {
        	selectedClip.stop();
        }
    }
    
    public static void setVolume(double percentage) {
        try {
            if (selectedClip != null && selectedClip.isOpen()) {
                FloatControl volumeControl = (FloatControl) selectedClip.getControl(FloatControl.Type.MASTER_GAIN);

                // THIS CONVERTS percentage (0-100) to decibels (-80 to 0)
                float dB = (float) ((Math.log10(percentage / 100.0) * 20));
                if (percentage == 0) {
                    dB = -80; // Set to minimum when at 0% to mute completely
                }
                volumeControl.setValue(dB);
                //System.out.println("Volume set to: " + percentage + "% (" + dB + " dB)");
            }
        } catch (Exception e) {
            System.out.println("Unable to set volume.");
            e.printStackTrace();
        }
    }

    
    public static void playButtonSFX() {
        try {
            URL soundURL = mainUI.class.getResource("/audioSamples/modernClick.wav"); // Ensure path is correct
            if (soundURL == null) {
                System.out.println("Sound file not found.");
                return;
            }
            
            AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundURL);
            buttonClick = AudioSystem.getClip();
            buttonClick.open(audioInputStream);

            // Adjust the volume here
            FloatControl volumeControl = (FloatControl) buttonClick.getControl(FloatControl.Type.MASTER_GAIN);
            float volume = -15.0f; // this is just the volume of the button click
            volumeControl.setValue(volume);
            
        } catch (Exception e) {
            e.printStackTrace();
        }

        if (buttonClick != null) {
        	buttonClick.setFramePosition(0); // Rewind to the beginning
        	buttonClick.start(); // Play the sound
        }
    } 
    
    public static void setToolTips() {

	     // Health Tooltip - Maximum Health: value * 1000 ||| MAX DISPLAY VALUE IS 1000
	        Tooltip healthTooltip = new Tooltip();
	        healthTooltip.textProperty().bind(Bindings.format("Maximum Health: %.0f", healthBar.progressProperty().multiply(1000)));
	        healthBar.setTooltip(healthTooltip);

	        // Strength Tooltip - Power: value * 250 Damage Points ||| MAX DISPLAY VALUE IS 250
	        Tooltip strengthTooltip = new Tooltip();
	        strengthTooltip.textProperty().bind(Bindings.format("Power: %.0f", strengthBar.progressProperty().multiply(250)));
	        strengthBar.setTooltip(strengthTooltip);

	        // Speed Tooltip - Speed: value * 100% movement speed ||| MAX DISPLAY VALUE IS 100%
	        Tooltip speedTooltip = new Tooltip();
	        speedTooltip.textProperty().bind(Bindings.format("Speed: %.0f%%", speedBar.progressProperty().multiply(100)));
	        speedBar.setTooltip(speedTooltip);

	        // Defense Tooltip - Defense: value * 30% Resistant To Damage ||| MAX DISPLAY VALUE IS 30%
	        Tooltip defenseTooltip = new Tooltip();
	        defenseTooltip.textProperty().bind(Bindings.format("Defense: %.0f%%", defenseBar.progressProperty().multiply(30)));
	        defenseBar.setTooltip(defenseTooltip);
    }
    
   

    
    public static void updateCharacterRenderButton(Button characterRenderButton, String selectedCharacter) {
        // Retrieve the character from the map to check for the render availability
        characterInfo character = characterInfo.characterMap.get(selectedCharacter);

        if (character != null) {
            String renderPath = character.getRenderPath();

            // Check if the character's render path is explicitly set to the default "NoRender.png"
            if ("/CharacterRenders/NoRender.png".equals(renderPath)) {
                // If the render is set to NoRender.png, then the "No Render Available" will be displayed on the button
                characterRenderButton.setDisable(false); // Enable the button
                characterRenderButton.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white; -fx-effect: dropshadow(gaussian, rgba(25, 35, 46, 1), 2, 3.54, 1.54, 5);");
                characterRenderButton.setText("No Render Available");
            } else if (renderPath != null && !renderPath.isEmpty()) {
                // If a valid render is available, the button is set to its normal state
                characterRenderButton.setDisable(false); // Enable the button
                characterRenderButton.setStyle("-fx-background-color: #cb5090; -fx-text-fill: white; -fx-effect: dropshadow(gaussian, rgba(112, 46, 81, 1), 2, 3.54, 1.54, 5);");
                characterRenderButton.setText("Render Available");
            } else {
                // If the render path is null or empty, default to "No Render Available"
              
                characterRenderButton.setDisable(false);
                characterRenderButton.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white; -fx-effect: dropshadow(gaussian, rgba(25, 35, 46, 1), 2, 3.54, 1.54, 5);");
                characterRenderButton.setText("No Render Available");
            }
        } else {
            // If the character does not exist it will default to "Character Not Found"
            characterRenderButton.setDisable(true); // Disable the button if the character does not exist
            characterRenderButton.setStyle("-fx-background-color: #2c3e50; -fx-text-fill: white; -fx-effect: dropshadow(gaussian, rgba(25, 35, 46, 1), 2, 3.54, 1.54, 5);");
            characterRenderButton.setText("Character Not Found");
        }
    }
    
    
    
 // Method to handle the styling of each credit
    private static String getStyledCredit(String credit) {
    	  String boldText = "-fx-text-fill: white; -fx-font-size: 40px; -fx-font-weight: bold;";
    	    String normalText = "-fx-text-fill: white; -fx-font-size: 30px;";

    	    switch (credit) {
    	        case "TESTERS":
    	        case "PROJECT TOOLS USED:":
    	        case "And a special thanks to...":
    	        case "CHARACTER INFORMATION LIBRARY":
    	        case  "CHARACTERS OWNED BY WOLFTICAL:":
    	        case  "CHARACTERS OWNED BY MEGANEKAII:":
    	            return boldText;
    	        default:
    	            return normalText; // Default style for all other labels
    	    }
    }
    public static void showCredits(Pane rootPane) {
        // Clean up existing credits UI if any
        Node existingOverlay = rootPane.lookup("#creditsOverlay");
        Node existingHeader = rootPane.lookup("#creditsHeader");
        Node existingVBox = rootPane.lookup("#creditsVBox");

        if (existingOverlay != null) rootPane.getChildren().remove(existingOverlay);
        if (existingHeader != null) rootPane.getChildren().remove(existingHeader);
        if (existingVBox != null) rootPane.getChildren().remove(existingVBox);

        String[] credits = {
            "CHARACTER INFORMATION LIBRARY",
            "App Version: " + appVersion,  
            "PROGRAMMER / DEVELOPER: WolfTical",  
            "UI DESIGN: WolfTical",  
            "ART / ASSETS: WolfTical",   
            "IN COLLABORATION WITH: MEGANEKAII",   
            "TESTERS",  
            "NeoBoudiou",  
            "DireKrow",  
            "Pilbemen",
            "Quoyi",  
            " ",  
            "PROJECT TOOLS USED:",
            "ECLIPSE IDE (JavaFX Library Integration)",
            "Launch4J (EXE app creator for Java)",
            "GitHub (Version Control and Application Updates)",
            "Krita (Drawing/Art Tool Programm)",
            " ",  
            "A special thanks to...",  
            "YOU! The user!",  
            " ",  
            "Thank you for using the application!",  
            " ",  
            "CHARACTERS OWNED BY WOLFTICAL:",  
            "Wolftical Triglowsticus",  
            "Wren Ryzen",  
            "Doxyn Larchiux",  
            "Archie Larchiux",  
            "Drex Dixton",  
            "Drax Dixton",  
            "Dr. Stenfort",  
            "Zalfor Tylox",  
            "Teforel Vaxin",  
            "The Incidents",
            "Shark Workers",
            "Chef Chompiere",
            " ",  
            "CHARACTERS OWNED BY MEGANEKAII:",  
            "Viraxe Eleviac"  
        };

        // Black overlay
        Region blackOverlay = new Region();
        blackOverlay.setId("creditsOverlay");
        blackOverlay.setStyle("-fx-background-color: rgba(0, 0, 0, 1);");
        blackOverlay.prefWidthProperty().bind(rootPane.widthProperty());
        blackOverlay.prefHeightProperty().bind(rootPane.heightProperty());

        // Header with close button
        HBox header = new HBox(10);
        header.setId("creditsHeader");
        header.setAlignment(Pos.TOP_RIGHT);
        header.setSpacing(20);

        Button closeButton = new Button("Close");
        closeButton.getStyleClass().add("close");

        final TranslateTransition[] scrollAnimation = new TranslateTransition[1];
        final PauseTransition[] delayTask = new PauseTransition[1];

        closeButton.setOnAction(e -> {
            appMethods.playButtonSFX();

            if (scrollAnimation[0] != null) scrollAnimation[0].stop();
            if (delayTask[0] != null) delayTask[0].stop();

            rootPane.getChildren().removeAll(blackOverlay, header);
            rootPane.getChildren().removeIf(node -> "creditsVBox".equals(node.getId()));
        });

        header.getChildren().add(closeButton);
        rootPane.getChildren().addAll(blackOverlay, header);

        // VBox for credits
        VBox creditsText = new VBox(10);
        creditsText.setId("creditsVBox");
        creditsText.setAlignment(Pos.BOTTOM_CENTER);
        creditsText.setSpacing(15);
        creditsText.setStyle("-fx-background-color: transparent;");
        creditsText.setMouseTransparent(true);
        rootPane.getChildren().add(creditsText);

     // Top image row (character icons)
        HBox imageRow = new HBox(15);
        imageRow.setAlignment(Pos.CENTER);

        // Load character images
        ImageView img1 = new ImageView(new Image(mainUI.class.getResource("/icons/wuffsprite_front.png").toExternalForm()));
        ImageView img2 = new ImageView(new Image(mainUI.class.getResource("/icons/viraxesprite_front.png").toExternalForm()));
        ImageView img3 = new ImageView(new Image(mainUI.class.getResource("/icons/doxynsprite_front.png").toExternalForm()));
        ImageView img4 = new ImageView(new Image(mainUI.class.getResource("/icons/archiesprite_front.png").toExternalForm()));
        ImageView img5 = new ImageView(new Image(mainUI.class.getResource("/icons/wrensprite_front.png").toExternalForm()));
        ImageView img6 = new ImageView(new Image(mainUI.class.getResource("/icons/stenfortsprite_front.png").toExternalForm()));

        // Set consistent size for all
        for (ImageView img : new ImageView[]{img1, img2, img3, img4, img5, img6}) {
            img.setFitWidth(128);  // Adjust as needed
            img.setPreserveRatio(true);
        }

        imageRow.getChildren().addAll(img1, img2, img3, img4, img5, img6);
        creditsText.getChildren().add(imageRow);


        // Now add all credit lines
        for (String credit : credits) {
            Label creditLabel = new Label(credit);
            creditLabel.setStyle(getStyledCredit(credit));
            creditsText.getChildren().add(creditLabel);
        }

        creditsText.setOpacity(0);
        creditsText.setTranslateY(rootPane.getHeight() + 100);

        delayTask[0] = new PauseTransition(Duration.seconds(0.1));
        delayTask[0].setOnFinished(event -> {
            double screenHeight = rootPane.getHeight();
            double creditsHeight = creditsText.getBoundsInParent().getHeight();
            double startYPosition = screenHeight + creditsHeight * 0.5;
            creditsText.setTranslateY(startYPosition);

            double totalScrollDistance = creditsHeight + screenHeight;
            int numLines = credits.length;
            double scrollDuration = Math.max(10, numLines * 0.75);

            scrollAnimation[0] = new TranslateTransition(Duration.seconds(scrollDuration), creditsText);
            scrollAnimation[0].setToY(-totalScrollDistance);
            scrollAnimation[0].setInterpolator(Interpolator.LINEAR);

            FadeTransition fadeIn = new FadeTransition(Duration.seconds(1), creditsText);
            fadeIn.setFromValue(0);
            fadeIn.setToValue(1);

            fadeIn.play();
            scrollAnimation[0].play();

            scrollAnimation[0].setOnFinished(finishEvent -> {
                PauseTransition finalPause = new PauseTransition(Duration.seconds(5));
                finalPause.setOnFinished(p -> rootPane.getChildren().remove(creditsText));
                finalPause.play();
            });
        });

        delayTask[0].play();
        rootPane.getStylesheets().add(mainUI.class.getResource("applicationUISheet.css").toExternalForm());
    }





    
    public static Button createCharacterButton(String characterName, Button characterRenderButton) {
        Button button = new Button(characterName);
        button.setWrapText(true); // Wrap text if it's too long
        button.setTooltip(new Tooltip(characterName)); // Show full name in tooltip
        // Set the action for the character button
        button.setOnAction(e -> {
            playButtonSFX();  // Your sound effect method
            handleCharacterSelection(characterName);  // Handle character selection logic
            selectedCharacterName = characterName;
            // Update the render button based on the selected character
            updateCharacterRenderButton(characterRenderButton, characterName);
        });

        button.getStyleClass().add("pink");
        return button;
        
        
    }

    
    public static void handleCharacterSelection(String characterName) {
        System.out.println(characterName + " selected!");

        // Retrieve the character from the map
        characterInfo character = characterInfo.characterMap.get(characterName);

        // If the character is found, update the character information
        if (character != null) {
            characterInfo.setCharacter(
                character.getName(),
                character.getHealth(),
                character.getStrength(),
                character.getSpeed(),
                character.getDefense(),
                character.getDescription(),
                character.getImagePath(),
                character.getRenderPath()
            );
        } else {
            System.out.println("Character not found! Displaying default WIP icon.");

            // Set default values with a WIP icon and render if the character is not found
            characterInfo.setCharacter(
                "WIP Character",
                0.0, // Default health
                0.0, // Default strength
                0.0, // Default speed
                0.0, // Default defense
                "This character is still a work in progress.",
                "/icons/WIPIcon.png",  // Path to your WIP icon
                "/CharacterRenders/NoRender.png"  // Path to a default "no render" image
            );
        }
    }
    
    
    public static void changeToDarkTheme() {
    	  leftPanel.setStyle("-fx-background-image: url('imageSamples/BorderPinkDARK.png'); " +
                  "-fx-background-repeat: no-repeat; " +
                  "-fx-background-size: 100% 100%; " +
                  "-fx-border-width: 5; " +                   // Border width
                  "-fx-border-color: #000000; "); 
    	  centerPanel.setStyle("-fx-background-image: url('imageSamples/BorderCyanDARK.png'); " +
                  "-fx-background-repeat: no-repeat; " +
                  "-fx-background-size: 100% 100%; " +
                  "-fx-border-width: 5; " +                   // Border width
                  "-fx-border-color: #000000; ");        
    	  
    	  bottomPanel.setStyle("-fx-background-image: url('imageSamples/BorderOrangeDARK.png'); " +
                  "-fx-background-repeat: no-repeat; " +
                  "-fx-background-size: 100% 100%; " +
                  "-fx-border-width: 5; " +                   // Border width
                  "-fx-border-color: #000000; ");  
    	  
    	  infoTextArea.setStyle("-fx-control-inner-background: black;" + "-fx-text-fill: #66ff66;");// Text color and background color
    	  nameTextField.setStyle("-fx-control-inner-background: black;" + "-fx-text-fill: #66ff66;");// Text color and background color
    	  nameTextField.setText("WELCOME TO THE SPECIAL JOURNAL ENTRIES!");
    	 
    	  characterInfo.applyTypingEffect(infoTextArea, quickNotice ,Duration.millis(1));
    }
    
   

}
