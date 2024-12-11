package application;

import javax.sound.sampled.*;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.binding.Bindings;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Tooltip;
import javafx.scene.layout.VBox;
import javafx.util.Duration;

import java.net.URL;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class appMethods extends customWindows {
	   public static Clip selectedClip;
	   public static Clip buttonClick;
	 
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
	        strengthTooltip.textProperty().bind(Bindings.format("Power: %.0f Damage Points", strengthBar.progressProperty().multiply(250)));
	        strengthBar.setTooltip(strengthTooltip);

	        // Speed Tooltip - Speed: value * 100% movement speed ||| MAX DISPLAY VALUE IS 100%
	        Tooltip speedTooltip = new Tooltip();
	        speedTooltip.textProperty().bind(Bindings.format("Speed: %.0f%% movement speed", speedBar.progressProperty().multiply(100)));
	        speedBar.setTooltip(speedTooltip);

	        // Defense Tooltip - Defense: value * 30% Resistant To Damage ||| MAX DISPLAY VALUE IS 30%
	        Tooltip defenseTooltip = new Tooltip();
	        defenseTooltip.textProperty().bind(Bindings.format("Defense: %.0f%% Resistant To Damage", defenseBar.progressProperty().multiply(30)));
	        defenseBar.setTooltip(defenseTooltip);
    }
    
    public static void updateCharacterButtons(ComboBox<String> characterComboBox, VBox buttonContainer, Button characterRenderButton) {
        // Clear the existing buttons
        buttonContainer.getChildren().clear();

        // Define a map with character categories and their respective characters
        Map<String, List<String>> characterCategories = new HashMap<>();
        characterCategories.put("The Heroes", Arrays.asList("Wolftical Triglowsticus", "Viraxe Eleviac", "Wren Ryzen", "Doxyn Larchiux", "Archie Larchiux","Drex Dixton", "Drax Dixton"));
        characterCategories.put("The Antagonists", Arrays.asList("Dr. Stenfort", "Zalfor Tylox", "Teforel Vaxin"));
        // Define specific names for incidents
        List<String> incidentCharacters = Arrays.asList(
            "Incident K-5520", //set 1
            "Incident L-152012",
            "Incident P-51420",
            "Incident C-11514",
            "Incident N-1115", //set 2
            "Incident U-3114",
            "Incident Z-1184",
            "Incident P-201518",
            "Incident H-1125", //set 3
            "Incident T-9512",
            "Incident G-12518",
            "Incident L-51514",
            "Incident B-91212", //FIRST LEVEL 5 INCIDENT
            "Incident D-91415", //set 4
            "Incident M-15208",
            "Incident E-512",
            "Incident C-202512",
            "Incident P-8914", //set 5
            "Incident R-18520",
            "Incident R-20125",
            "Incident H-5516",
            "Incident E-31115", //set 6
            "Incident S-12217",
            "Incident I-4518",
            "Incident A-71514",
            "Incident I-14715",// SECOND LEVEL 5 INCIDENT
            "Incident F-9198", //set 7
            "Incident B-55",
            "Incident G-21914",
            "Incident G-18125");
        characterCategories.put("The Incidents", incidentCharacters);
        characterCategories.put("Miscellaneous", Arrays.asList("UNSPECIFIED", "UNSPECIFIED", "UNSPECIFIED"));
        // Get the selected character category from the ComboBox
        String selectedCharacter = characterComboBox.getValue();

        // Add buttons based on selection
        if (selectedCharacter != null && characterCategories.containsKey(selectedCharacter)) {
            // Get the list of characters for the selected category
            List<String> characters = characterCategories.get(selectedCharacter);

            // Loop through the character list and create a button for each one
            for (String character : characters) {
                buttonContainer.getChildren().add(createCharacterButton(character, characterRenderButton));
            }
        }
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
    
    
    
   

}