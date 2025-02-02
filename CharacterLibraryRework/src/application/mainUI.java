package application;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import java.io.File;

import javafx.application.Application;
import javafx.beans.binding.Bindings;
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
import javafx.scene.control.Tooltip;
import javafx.scene.layout.*;



public class mainUI extends Application {
	   public static String selectedCharacterName = null;  // CharacterName Variable, it's accessible across all methods
	   
	   static double appVersion = 4.0;
	   static String applastUpdate = "1/27/2025";
	   
	   
	   static ProgressBar healthBar;
	   static ProgressBar strengthBar;
	   static ProgressBar speedBar;
	   static ProgressBar defenseBar;
	   
	   static TextField nameTextField;
	   static TextArea infoTextArea;
	   
	   static Image iconImage;
	   static  ImageView iconImageView;
	   static Button characterRenderButton;
	 
	  
	   static Slider volumeSlider;
	   static   ComboBox<String> characterComboBox;
	   
	   static VBox buttonContainer;
	   
	   static StackPane screenOverlay;
	   @Override
	public void start(Stage primaryStage) {
		  
		   
	        primaryStage.setTitle("Character Info Library V" + appVersion + " BETA");
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
	     // Create the search bar
	        TextField searchBar = new TextField();
	        searchBar.setPromptText("Search characters...");
	        searchBar.setMinHeight(30);
	        searchBar.setMaxWidth(200); // Adjust width if necessary

	        // Listener to filter character buttons as the user types
	        searchBar.textProperty().addListener((observable, oldValue, newValue) -> {
	            characterInfo.filterCharacterButtons(characterComboBox, buttonContainer, characterRenderButton, newValue);
	        });
	        
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
	      

	        Tooltip renderTip = new Tooltip("Shows a full image of a character");
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
	        characterRenderButton.setTooltip(renderTip);

	      

	        Button options = new Button("Options/Extras");
	        options.getStyleClass().add("pink");
	        options.setMaxWidth(Double.MAX_VALUE);
	        options.setOnAction(e -> { appMethods.playButtonSFX(); customWindows.showOptions(primaryStage, screenOverlay);});
	         
	      


	        
	        // VBox for dynamic buttons
	        VBox dynamicButtonsBox = new VBox(10);
	        ScrollPane scrollPaneForButtons = new ScrollPane(dynamicButtonsBox);
	        scrollPaneForButtons.setFitToWidth(true); // Ensure scrollable content fits the width
	        
	        // Add buttons to the left panel
	        leftPanel.getChildren().addAll(searchBar, characterComboBox, charScrollPane, characterRenderButton,options);
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
	        VBox progressBarContainer = new VBox(5);

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
	        healthIcon.setFitWidth(40);  // Set size for the icon
	        healthIcon.setFitHeight(40);
	     
	        ImageView strengthIcon = new ImageView(new Image(getClass().getResource("/imageSamples/PWIcon.png").toExternalForm()));
	        strengthIcon.setFitWidth(40);
	        strengthIcon.setFitHeight(40);

	        ImageView speedIcon = new ImageView(new Image(getClass().getResource("/imageSamples/SPIcon.png").toExternalForm()));
	        speedIcon.setFitWidth(40);
	        speedIcon.setFitHeight(40);

	        ImageView defenseIcon = new ImageView(new Image(getClass().getResource("/imageSamples/DFIcon.png").toExternalForm()));
	        defenseIcon.setFitWidth(40);
	        defenseIcon.setFitHeight(40);

	     // Create Labels for the dynamic values
	        Label healthValueLabel = new Label();
	        healthValueLabel.textProperty().bind(Bindings.format("Max Health: %.0f", healthBar.progressProperty().multiply(1000)));

	        Label strengthValueLabel = new Label();
	        strengthValueLabel.textProperty().bind(Bindings.format("Power: %.0f Damage", strengthBar.progressProperty().multiply(250)));

	        Label speedValueLabel = new Label();
	        speedValueLabel.textProperty().bind(Bindings.format("Speed: %.0f%% Movement Speed", speedBar.progressProperty().multiply(100)));

	        Label defenseValueLabel = new Label();
	        defenseValueLabel.textProperty().bind(Bindings.format("Defense: %.0f%% Damage Reduction", defenseBar.progressProperty().multiply(30)));
	    
	        // Style the labels for consistent alignment (optional)
	      
	        healthValueLabel.getStyleClass().add("barLabel");
	   
	        strengthValueLabel.getStyleClass().add("barLabel");
	       
	        speedValueLabel.getStyleClass().add("barLabel");
	     
	        defenseValueLabel.getStyleClass().add("barLabel");
	        // Creating HBoxes to combine the dynamic value label, icon, and progress bar
	        HBox healthHBox = new HBox(5, healthValueLabel, healthIcon, healthBar);
	        healthHBox.getStyleClass().add("hbox-customIcon");
	        HBox strengthHBox = new HBox(5, strengthValueLabel, strengthIcon, strengthBar);
	        strengthHBox.getStyleClass().add("hbox-customIcon");
	        HBox speedHBox = new HBox(5, speedValueLabel, speedIcon, speedBar);
	        speedHBox.getStyleClass().add("hbox-customIcon");
	        HBox defenseHBox = new HBox(5, defenseValueLabel, defenseIcon, defenseBar);
	        defenseHBox.getStyleClass().add("hbox-customIcon");

	        // Align the HBoxes
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
	        Button resetAudioButton = new Button("Stop Audio");
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
	        screenOverlay = new StackPane();
	        screenOverlay.getChildren().addAll(scrollPane, loadingScreen);
	        
	        // Set the scene with the ScrollPane
	        primaryStage.setScene(new Scene(screenOverlay, 1200, 900));

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
        	  characterFileManager.copyResourcesFromJarToCharacterDirectory();
        	   // Start the file watching in a separate thread
              Thread fileWatchThread = new Thread(() -> {
                  characterFileManager.watchForFileChanges();
              });
              fileWatchThread.setDaemon(true); // Allow thread to exit when the app closes
              fileWatchThread.start();
              
              
            launch(args);
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error: " + e.getMessage());
        }
        System.out.println("Application ended.");
        
    }
}
