package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

import javafx.application.Application;
import javafx.concurrent.Task;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.ProgressIndicator;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.layout.*;



public class mainUI extends Application {
	   public static String selectedCharacterName = null;  // CharacterName Variable, it's accessible across all methods
	   
	   static double appVersion = 3.4;
	   static String applastUpdate = "12/23/2024";
	   
	   
	   static ProgressBar healthBar;
	   static ProgressBar strengthBar;
	   static ProgressBar speedBar;
	   static ProgressBar defenseBar;
	   
	   static TextField nameTextField;
	   static TextArea infoTextArea;
	   
	   static Image iconImage;
	   static  ImageView iconImageView;
	   static Button characterRenderButton;
	   static Button loadCSVFile;
	   static Slider volumeSlider;
	   static   ComboBox<String> characterComboBox;
	   
	   static VBox buttonContainer;
	   @Override
	public void start(Stage primaryStage) {
		  
		   
	  
	        primaryStage.setTitle("Character Info Library");
	        Image appIcon = new Image(getClass().getResource("/icons/appIcon.png").toExternalForm());
	      

	        primaryStage.getIcons().add(appIcon);

	        // Root layout
	        BorderPane root = new BorderPane();

	        // Left Panel (Pink) - Character Selection
	        VBox leftPanel = new VBox(10);
	        leftPanel.setPadding(new Insets(20));
	        leftPanel.setStyle("-fx-background-image: url('imageSamples/BorderPink.png'); " +
	                           "-fx-background-repeat: no-repeat; " +
	                           "-fx-background-size: 100% 100%; " +
	                           "-fx-border-width: 5; " +                   // Border width
	                           "-fx-border-color: #000000; ");           // Border color (Black)

	        buttonContainer = new VBox(10); // Create a VBox to hold the dynamic buttons
	        buttonContainer.setStyle("-fx-background-color: #f4f4f4;"); // Optional styling for the VBox
	        buttonContainer.setPadding(new Insets(10));
	     
	        buttonContainer.setSpacing(10);
	        buttonContainer.setAlignment(Pos.CENTER);
	        
	        
	        ScrollPane charScrollPane = new ScrollPane(buttonContainer);
	        charScrollPane.setFitToWidth(true); // Ensures the VBox is resized to fit the ScrollPane width
	        charScrollPane.setFitToHeight(false); // Ensures the ScrollPane adjusts to the VBox's height
	        characterComboBox = new ComboBox<>();
	        characterComboBox.getItems().add("-Click Me-"); // Default option

	        // Load categories dynamically
	        characterComboBox.getItems().addAll(characterInfo.getCharacterCategories().keySet());

	        characterComboBox.setOnAction(e -> {
	            appMethods.playButtonSFX();
	            characterInfo.updateCharacterButtons(characterComboBox, buttonContainer, characterRenderButton);
	        });
	        characterComboBox.setValue("-Click Me-");
	        
	        Button helpButton = new Button("Help");
	        helpButton.getStyleClass().add("pink");
	        helpButton.setMaxWidth(Double.MAX_VALUE); // Ensure the button stretches horizontally
	        helpButton.setOnAction(e -> { appMethods.playButtonSFX(); customWindows.showHelp(); });

	        characterRenderButton = new Button("Character Render");
	        characterRenderButton.setOnAction(e -> {
	            appMethods.playButtonSFX();  // Play the button sound effect

	            // Only show the render if a character has been selected
	            if (selectedCharacterName != null) {
	                appMethods.showCharacterRender(selectedCharacterName);  // Pass the selected characterName
	            } else {
	                System.out.println("No character selected.");
	            }
	        });

	        characterRenderButton.getStyleClass().add("pink");
	        characterRenderButton.setMaxWidth(Double.MAX_VALUE); // Ensure the button stretches horizontally
	        
	        loadCSVFile = new Button("Load New");
	        loadCSVFile.getStyleClass().add("pink");
	        loadCSVFile.setMaxWidth(Double.MAX_VALUE);
	        loadCSVFile.setOnAction(e -> {
	            // Play the button sound effect
	            appMethods.playButtonSFX();

	            // Open a FileChooser to select a new CSV file
	            FileChooser fileChooser = new FileChooser();
	            fileChooser.setTitle("Select New Character CSV File");
	            fileChooser.getExtensionFilters().add(new FileChooser.ExtensionFilter("CSV Files", "*.csv"));
	            
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



	        
	        // VBox for dynamic buttons
	        VBox dynamicButtonsBox = new VBox(10);
	        ScrollPane scrollPaneForButtons = new ScrollPane(dynamicButtonsBox);
	        scrollPaneForButtons.setFitToWidth(true); // Ensure scrollable content fits the width
	        
	        // Add buttons to the left panel
	        leftPanel.getChildren().addAll(characterComboBox, charScrollPane, characterRenderButton, helpButton, loadCSVFile);
	        root.setLeft(leftPanel);

	        // Center Panel (Cyan) - Main Content Display
	        VBox centerPanel = new VBox(20);
	        centerPanel.setPadding(new Insets(20));
	        centerPanel.setStyle("-fx-background-image: url('imageSamples/BorderCyan.png'); " +
	                             "-fx-background-repeat: no-repeat; " +
	                             "-fx-background-size: 100% 100%; " +
	                             "-fx-border-width: 5; " +                   // Border width
	                             "-fx-border-color: #000000; ");            // Border color (Black)

	        // Create a new VBox for holding the profile header and information
	        VBox profileContainer = new VBox(10);
	        profileContainer.setPadding(new Insets(10));
	        VBox.setVgrow(profileContainer, Priority.ALWAYS);

	        // Profile Header (Character Name)
	        nameTextField = new TextField("Character Information Library V" + appVersion);
	        nameTextField.setEditable(false);

	        // Information (Text Area)
	        infoTextArea = new TextArea("WELCOME TO THE CHARACTER INFORMATION LIBRARY: \r\nPlease start from the drop down menu at the top left to show different categories. Character buttons will be shown that can display information.");
	        infoTextArea.setEditable(false);
	        infoTextArea.setWrapText(true);

	        VBox.setVgrow(infoTextArea, Priority.ALWAYS);

	     // Create the icon image with specific size (200x200)
	        try {
	            iconImage = new Image(getClass().getResource("/icons/staricon.png").toExternalForm());  // Use getResource()
	            iconImageView = new ImageView(iconImage);
	            iconImageView.setFitWidth(200);  // Set the desired width
	            iconImageView.setFitHeight(200); // Set the desired height
	            iconImageView.setPreserveRatio(true); // Keep the aspect ratio
	        } catch (NullPointerException e) {
	            System.out.println("Error: Image not found at the specified path.");
	            e.printStackTrace();
	        }

	     // Create a StackPane to hold the ImageView
	        StackPane imageContainer = new StackPane(iconImageView);
	        imageContainer.setStyle("-fx-border-color: #4f95b8; -fx-border-width: 6px; -fx-border-radius: 1px;");

	    
	        
	     // Create VBox for the progress bars stacked vertically
	        VBox progressBarContainer = new VBox(0);

	        // Create the ProgressBars
	        healthBar = new ProgressBar(1);  // 100% progress (Full health)
	        healthBar.getStyleClass().add("progress-bar-health");

	        strengthBar = new ProgressBar(1);  // 100% progress (Full strength)
	        strengthBar.getStyleClass().add("progress-bar-strength");

	        speedBar = new ProgressBar(1);  // 100% progress (Full speed)
	        speedBar.getStyleClass().add("progress-bar-speed");

	        defenseBar = new ProgressBar(1);  // 100% progress (Full defense)
	        defenseBar.getStyleClass().add("progress-bar-defense");

	        // Create ImageView for the icons, using getResource() to access images in the imageSamples directory
	        ImageView healthIcon = new ImageView(new Image(getClass().getResource("/imageSamples/HPIcon.png").toExternalForm()));
	        healthIcon.setFitWidth(25);  // Set size for the icon
	        healthIcon.setFitHeight(25);

	        ImageView strengthIcon = new ImageView(new Image(getClass().getResource("/imageSamples/PWIcon.png").toExternalForm()));
	        strengthIcon.setFitWidth(25);
	        strengthIcon.setFitHeight(25);

	        ImageView speedIcon = new ImageView(new Image(getClass().getResource("/imageSamples/SPIcon.png").toExternalForm()));
	        speedIcon.setFitWidth(25);
	        speedIcon.setFitHeight(25);

	        ImageView defenseIcon = new ImageView(new Image(getClass().getResource("/imageSamples/DFIcon.png").toExternalForm()));
	        defenseIcon.setFitWidth(25);
	        defenseIcon.setFitHeight(25);

	        // Creating HBoxes to combine each icon with its corresponding progress bar
	        HBox healthHBox = new HBox(5, healthIcon, healthBar);
	        HBox strengthHBox = new HBox(5, strengthIcon, strengthBar);
	        HBox speedHBox = new HBox(5, speedIcon, speedBar);
	        HBox defenseHBox = new HBox(5, defenseIcon, defenseBar);
	        healthHBox.setAlignment(Pos.CENTER_LEFT);
	        strengthHBox.setAlignment(Pos.CENTER_LEFT);
	        speedHBox.setAlignment(Pos.CENTER_LEFT);
	        defenseHBox.setAlignment(Pos.CENTER_LEFT);
	        // Add the HBoxes to the progressBarContainer
	        progressBarContainer.getChildren().addAll(healthHBox, strengthHBox, speedHBox, defenseHBox);

	        // Setting the tooltips/hints of the status bars
	        appMethods.setToolTips();


	        // Create an HBox to place the icon and the VBox (with the progress bars) side by side
	        HBox iconAndBars = new HBox(10);  // 10 pixels spacing between the icon and bars
	        iconAndBars.setAlignment(Pos.CENTER_LEFT);  // Align items to the left in the HBox
	        iconAndBars.getChildren().addAll(imageContainer, progressBarContainer);

	  
	      
	        // Add the nameTextField and infoTextArea to the profileContainer
	        profileContainer.getChildren().addAll(iconAndBars, nameTextField, infoTextArea);

	        // Add profileContainer to the center panel
	        centerPanel.getChildren().add(profileContainer);

	     
	        
	     

	        // Add the center panel to the root layout
	        root.setCenter(centerPanel);

	        // Bottom Panel (Orange) - Audio Controls
	        HBox bottomPanel = new HBox(10);
	        bottomPanel.setPadding(new Insets(20));
	        bottomPanel.setStyle("-fx-background-image: url('imageSamples/BorderOrange.png'); " +
	                             "-fx-background-repeat: no-repeat; " +
	                             "-fx-background-size: 100% 100%; " +
	                             "-fx-border-width: 5; " +                   // Border width
	                             "-fx-border-color: #000000; ");             // Border color (Black)

	        Button playAudioButton = new Button("Play Audio");
	        playAudioButton.getStyleClass().add("orange");
	        Button resetAudioButton = new Button("Reset Audio");
	        resetAudioButton.getStyleClass().add("orange");
	        ComboBox<String> audioComboBox = new ComboBox<>();
	        audioComboBox.getStyleClass().add("combo-box");
	        audioComboBox.getItems().addAll("ThematicHeroes", "DranixionsRising", "COME BACK LATER :)");
	        audioComboBox.setOnAction(e -> {appMethods.playButtonSFX();});
	        audioComboBox.setValue("ThematicHeroes");
	       // The event handler for Play Audio button
	        playAudioButton.setOnAction(event -> {
	        	appMethods.playButtonSFX();
	            String selectedAudio = audioComboBox.getValue(); // Get the selected audio path
	            if (selectedAudio != null) {
	                appMethods.playSelectedAudio(selectedAudio);
	            } else {
	                System.out.println("No audio selected.");
	            }
	        });
	        
	        resetAudioButton.setOnAction(event -> {
	        	appMethods.playButtonSFX();
	        	appMethods.stopMusic();
	                System.out.println("Audio playback reset.");
	            });
	        
	        volumeSlider = new Slider(0, 100, 50); // MIN 0, MAX 100, DEFAULT 50
	        volumeSlider.getStyleClass().add("custom-slider");
	        volumeSlider.setShowTickLabels(true);
	        volumeSlider.setShowTickMarks(true);
	        volumeSlider.setMajorTickUnit(25);
	        volumeSlider.setBlockIncrement(1);

	        volumeSlider.valueProperty().addListener((obs, oldVal, newVal) -> {
	            appMethods.setVolume(newVal.doubleValue());
	        });

	        
	        bottomPanel.getChildren().addAll(playAudioButton, resetAudioButton, audioComboBox, volumeSlider);
	        root.setBottom(bottomPanel);

	        // Wrap the BorderPane in a ScrollPane
	        ScrollPane scrollPane = new ScrollPane(root);
	        scrollPane.setFitToWidth(true);
	        scrollPane.setFitToHeight(true);
	        scrollPane.getStylesheets().add(getClass().getResource("applicationUISheet.css").toExternalForm());

	        
	        // Create a loading screen
	        VBox loadingScreen = new VBox(10);
	        loadingScreen.setAlignment(Pos.CENTER);
	        loadingScreen.setStyle("-fx-background-color: rgba(0, 0, 0, 1);"); // Semi-transparent background
	        Label loadingLabel = new Label("Character Library V" + appVersion);
	        loadingLabel.setStyle("-fx-text-fill: white; -fx-font-size: 20px;");
	        ProgressIndicator progressIndicator = new ProgressIndicator();
	        loadingScreen.getChildren().addAll(progressIndicator, loadingLabel);
	        loadingScreen.setVisible(false); // Initially hidden

	        // Wrap the BorderPane in a StackPane to overlay the loading screen
	        StackPane stackPane = new StackPane();
	        stackPane.getChildren().addAll(scrollPane, loadingScreen);
	        
	        // Set the scene with the ScrollPane
	        primaryStage.setScene(new Scene(stackPane, 1200, 900));

	        // Show the stage
	        primaryStage.show();
	        Task<Void> loadingTask = customWindows.simulateLoading(loadingScreen, primaryStage);
	        primaryStage.setOnCloseRequest(event -> {
	            loadingTask.cancel(); // Cancel the task when window is closed
	            System.out.println("Window closed, task cancelled.");
	            primaryStage.close();  // Close the window
	        });
	}


    public static void main(String[] args) {
    	
        System.out.println("Application starting...");
        try {
        	  Thread.sleep(2000);
        	  characterInfo.loadCharactersFromCSV("/characterCSV/characterData.csv");

            launch(args);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println("Application ended.");
        
    }
}
