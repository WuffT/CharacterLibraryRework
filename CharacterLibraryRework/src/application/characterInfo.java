package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceDialog;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.HBox;
import javafx.scene.layout.StackPane;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.util.Duration;





//Character class to store the character's details
public class characterInfo extends appMethods{
	private String name;
    private double health;
    private double strength;
    private double speed;
    private double defense;
    private String description;
    private String imagePath; // New field to store the character image path
    private String renderPath;
    private static Timeline currentTypingTimeline = null; 
    static Map<String, characterInfo> characterMap = new HashMap<>();

    // Constructor
    public characterInfo(String name, double health, double strength, double speed, double defense, String descriptionPath, String imagePath, String renderPath) {
        this.name = name;
        this.health = health;
        this.strength = strength;
        this.speed = speed;
        this.defense = defense;
        this.description = loadDescriptionFromFile(descriptionPath);;
        this.imagePath = imagePath;
        this.renderPath = renderPath;
    }

 public String getName() {
     return name;
 }

 public double getHealth() {
     return health;
 }

 public double getStrength() {
     return strength;
 }

 public double getSpeed() {
     return speed;
 }

 public double getDefense() {
     return defense;
 }

 public String getDescription() {
     return description;
 }
 
 public String getImagePath() {
     return imagePath;
 }
 
 public String getRenderPath() {
	 return renderPath;
 }
 
 
 public String loadDescriptionFromFile(String descriptionPath) {
     try (BufferedReader reader = new BufferedReader(
             new InputStreamReader(characterInfo.class.getResourceAsStream(descriptionPath)))) {
         return reader.lines().collect(Collectors.joining("\n"));
     } catch (Exception e) {
         System.err.println("Error loading description from file: " + descriptionPath);
         e.printStackTrace();
         return "Description not available.";
     }
 }
 

 public static Map<String, List<String>> characterCategories = new HashMap<>();

 //THIS is the method that allows the whole file with character data to be loaded and set.
 public static void loadCharactersFromCSV(String filePath) {
     try (BufferedReader reader = getBufferedReader(filePath)) {
         String line;
         boolean isFirstLine = true; // Skip header row

         while ((line = reader.readLine()) != null) {
             if (isFirstLine) {
                 isFirstLine = false;
                 continue; // Skip the header
             }

             try {
                 // Split the line by commas
                 String[] values = line.split(",");

                 // Ensure at least 9 columns: Category + Name + 7 data fields
                 if (values.length < 6) {
                     System.err.println("Invalid line: Not enough data -> " + line);
                     continue; // Skip this line
                 }

                 // Extract data
                 String category = values[0].trim(); // First column is category
                 String name = values[1].trim();    // Second column is name
                 double health = parseDoubleWithDefault(values[2]);
                 double strength = parseDoubleWithDefault(values[3]);
                 double speed = parseDoubleWithDefault(values[4]);
                 double defense = parseDoubleWithDefault(values[5]);
                 
                 
                 // Handle paths with fallbacks if empty
                 String descriptionPath = (values.length > 6 && !values[6].trim().isEmpty()) 
                         ? values[6].trim() 
                         : "/characterDescriptions/WIPCharacter.txt"; // Default path if empty

                 String imagePath = (values.length > 7 && !values[7].trim().isEmpty()) 
                         ? values[7].trim() 
                         : "/icons/WIPIcon.png"; // Default path if empty

                 String renderPath = (values.length > 8 && !values[8].trim().isEmpty()) 
                         ? values[8].trim() 
                         : "/CharacterRenders/NoRender.png"; // Default path if empty


                 // Create a new characterInfo object
                 characterInfo character = new characterInfo(name, health, strength, speed, defense,
                                                             descriptionPath, imagePath, renderPath);

                 // Add character to characterMap
                 characterMap.put(name, character);

                 // Add character to its category
                 characterCategories.computeIfAbsent(category, k -> new ArrayList<>()).add(name);

             } catch (NumberFormatException e) {
                 System.err.println("Error parsing numeric values in line: " + line);
                 e.printStackTrace();
             } catch (Exception e) {
                 System.err.println("Error processing line: " + line);
                 e.printStackTrace();
             }
         }
     } catch (IOException e) {
         System.err.println("Error reading the file: " + filePath);
         e.printStackTrace();
     }
 }
 


 // Custom Base paths for different file types
 final static String BASE_DESCRIPTION_PATH = "/customCharacterDirectory/customCharacterDescription/"; 
 final static String BASE_ICON_PATH = "/customCharacterDirectory/customCharacterIcon/";
 final static String BASE_RENDER_PATH = "/customCharacterDirectory/customCharacterRender/";

 // Custom Base paths for different file types
 final static String NO_DESCRIPTION_PATH = "/characterDescriptions/WIPCharacter.txt"; 
 final static String NO_ICON_PATH = "/icons/WIPIcon.png";
 final static String NO_RENDER_PATH = "/CharacterRenders/NoRender.png";

 public static void loadAndEditCSVFile(File selectedFile, StackPane rootPane) {
	    if (selectedFile != null) {
	        try {
	            List<String> lines = Files.readAllLines(selectedFile.toPath());

	            if (lines.isEmpty()) {
	                System.err.println("The CSV file is empty.");
	                return;
	            }

	            String[] headers = lines.get(0).split(",");
	            lines.remove(0);

	            ObservableList<ObservableList<String>> newTableData = FXCollections.observableArrayList();
	            Map<String, List<ObservableList<String>>> newCategories = new HashMap<>();

	            for (String line : lines) {
	                String[] values = line.split(",");
	                if (line.trim().isEmpty() || values.length < 6) continue;

	                String descriptionPath = values.length > 6 ? values[6].replace(BASE_DESCRIPTION_PATH, "").trim() : "";
	                String imagePath = values.length > 7 ? values[7].replace(BASE_ICON_PATH, "").trim() : "";
	                String renderPath = values.length > 8 ? values[8].replace(BASE_RENDER_PATH, "").trim() : "";

	                while (values.length < 9) {
	                    values = Arrays.copyOf(values, values.length + 1);
	                }
	                values[6] = descriptionPath;
	                values[7] = imagePath;
	                values[8] = renderPath;

	                ObservableList<String> row = FXCollections.observableArrayList(values);
	                newTableData.add(row);

	                String category = values[0].trim();
	                newCategories.putIfAbsent(category, new ArrayList<>());
	                newCategories.get(category).add(row);
	            }

	            TableView<ObservableList<String>> newTableView = new TableView<>();
	            for (int i = 0; i < headers.length; i++) {
	                TableColumn<ObservableList<String>, String> column = new TableColumn<>(headers[i]);
	                int colIndex = i;
	                column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(colIndex)));
	                column.setCellFactory(TextFieldTableCell.forTableColumn());
	                column.setOnEditCommit(event -> event.getRowValue().set(colIndex, event.getNewValue()));
	                newTableView.getColumns().add(column);
	                
	            }

	            newTableView.setItems(newTableData);
	            newTableView.setEditable(true);
	          
	         // Create the label with the instructions
	            Label instructionLabel = new Label("CSV loaded! Please edit the table as needed.\n"
	                    + "Double-click on any column to edit and press Enter to confirm your changes.\n"
	                    + "When you're ready, press the 'Confirm' button to save your customizations.\n"
	                    + "NOTE: Ensure that your custom character files are correctly placed in their respective folders.\n"
	                    + "Make sure the file name matches what you type in the columns. If your description file is named 'mycharacter.txt' then type that");

	            
	            
	            instructionLabel.setStyle("-fx-text-fill: white; -fx-font-size: 18px; -fx-padding: 10; -fx-alignment: center; -fx-background-color: black;");
	            instructionLabel.setMaxWidth(Double.MAX_VALUE); // Stretch to the width of the container
	            instructionLabel.setMaxHeight(200); // Set a fixed maximum height for the Label
	            
	            // Confirmation & Cancel Buttons
	            Button confirmButton = new Button("Confirm");
	            Button cancelButton = new Button("Cancel");

	            confirmButton.setOnAction(e -> {
	            	appMethods.playButtonSFX();
	           	 // Clear existing character categories and UI components before loading new data
		            characterCategories.clear(); // Clear the old categories
		            buttonContainer.getChildren().clear(); // Clear any existing buttons
	                
	                for (ObservableList<String> row : newTableData) {
	                    row.set(6, BASE_DESCRIPTION_PATH + row.get(6).trim());
	                    row.set(7, BASE_ICON_PATH + row.get(7).trim());
	                    row.set(8, BASE_RENDER_PATH + row.get(8).trim());
	                }

	                StringBuilder updatedCsv = new StringBuilder();
	                updatedCsv.append(String.join(",", headers)).append("\n");
	                for (ObservableList<String> row : newTableData) {
	                    updatedCsv.append(String.join(",", row)).append("\n");
	                }

	                try {
	                    Files.write(Paths.get(selectedFile.toURI()), updatedCsv.toString().getBytes());
	                    characterInfo.loadCharactersFromCSV(selectedFile.getAbsolutePath());
	                    refreshUI();
	                    System.out.println("CSV file updated and UI refreshed successfully!");
	                } catch (IOException ex) {
	                    System.err.println("Error saving the file.");
	                    ex.printStackTrace();
	                }

	                // Remove the editing UI after confirming
	                rootPane.getChildren().removeIf(node -> node instanceof VBox);
	            });
	            confirmButton.getStyleClass().add("pink");
	            cancelButton.setOnAction(e -> {
	            	appMethods.playButtonSFX();
	                // Remove the editing UI without saving
	                rootPane.getChildren().removeIf(node -> node instanceof VBox);
	            });

	            cancelButton.getStyleClass().add("close");
	            
	            HBox buttonContainer = new HBox(10, confirmButton, cancelButton);
	            buttonContainer.setAlignment(Pos.CENTER);

	            VBox editContainer = new VBox(10,instructionLabel, newTableView, buttonContainer);
	            editContainer.setAlignment(Pos.CENTER);
	            editContainer.setPadding(new Insets(10));
	            editContainer.setStyle("-fx-padding: 10; -fx-background-color: rgba(0, 0, 0, 0.7);");

	            // Remove any previous table UI before adding a new one
	            rootPane.getChildren().removeIf(node -> node instanceof VBox);
	            rootPane.getChildren().add(editContainer);
	            rootPane.getStylesheets().add(mainUI.class.getResource("applicationUISheet.css").toExternalForm());
	        } catch (IOException e) {
	            System.err.println("Error reading the file.");
	            e.printStackTrace();
	        }
	    }
	}

 

 // Helper method to get character categories map
 public static Map<String, List<String>> getCharacterCategories() {
     return characterCategories;
 }
 
 public static void updateCharacterButtons(ComboBox<String> characterComboBox, VBox buttonContainer, Button characterRenderButton) {
     buttonContainer.getChildren().clear();

     // Get the selected category
     String selectedCategory = characterComboBox.getValue();

     // Retrieve characters dynamically
     if (selectedCategory != null && characterCategories.containsKey(selectedCategory)) {
         List<String> characters = characterCategories.get(selectedCategory);
         for (String character : characters) {
             buttonContainer.getChildren().add(createCharacterButton(character, characterRenderButton));
         }
     }
 }
 public static void refreshUI() {
	    // Clear and refresh the ComboBox items (categories)
	    characterComboBox.getItems().clear();
	    characterComboBox.getItems().addAll(characterCategories.keySet());

	    // Set the default category (first one, if any)
	    if (!characterComboBox.getItems().isEmpty()) {
	        characterComboBox.setValue(characterComboBox.getItems().get(0)); // Default to the first category
	    }

	    // Refresh the character buttons for the selected category
	    updateCharacterButtons(characterComboBox, buttonContainer, characterRenderButton);
	}

 
//Helper method to parse a double and provide a default value in case of error
public static double parseDoubleWithDefault(String value) {
  try {
      // Replace comma with period to handle different locales
      value = value.replace(",", ".");
      
      return value.isEmpty() ? 0.0 : Double.parseDouble(value);
  } catch (NumberFormatException e) {
      return 0.0; // Return default value if invalid or empty
  }
}


public static void filterCharacterButtons(ComboBox<String> characterComboBox, VBox buttonContainer, Button characterRenderButton, String query) {
    buttonContainer.getChildren().clear();

    String selectedCategory = characterComboBox.getValue();
    if (selectedCategory != null && characterCategories.containsKey(selectedCategory)) {
        List<String> characters = characterCategories.get(selectedCategory);
        
        // Filter characters based on search query (ignoring case)
        List<String> filteredCharacters = characters.stream()
            .filter(character -> character.toLowerCase().contains(query.toLowerCase()))
            .collect(Collectors.toList());

        // Create buttons only for filtered characters
        for (String character : filteredCharacters) {
            buttonContainer.getChildren().add(createCharacterButton(character, characterRenderButton));
        }
    }
}


	// Helper method to get a BufferedReader for the file or resource
	private static BufferedReader getBufferedReader(String filePath) throws IOException {
	    InputStream inputStream;

	    // Check if the path is a classpath resource (starts with "/")
	    if (filePath.startsWith("/")) {
	        // Loading resource from the classpath (like in a JAR)
	        inputStream = characterInfo.class.getResourceAsStream(filePath);
	    } else {
	        // Loading from the file system
	        inputStream = new FileInputStream(filePath);
	    }

	    // If the resource/inputStream is null, print error and return
	    if (inputStream == null) {
	        throw new FileNotFoundException("File not found: " + filePath);
	    }

	    return new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8));
	}

 
 
 public static void setCharacter(String name, double health, double strength, double speed, double defense, String description, String imagePath, String renderPath) {
	    System.out.println("Setting character: " + name);
	    System.out.println("Using image path: " + imagePath);
	    System.out.println("Using render path: " + renderPath);
	    
	    // Loading the image
	    try {
	        Image characterImage = new Image(characterInfo.class.getResource(imagePath).toExternalForm());
	        iconImageView.setImage(characterImage);
	        System.out.println("Image loaded successfully for: " + name);
	    } catch (Exception e) {
	        System.out.println("Error loading image for: " + name);
	        e.printStackTrace();
	    }

	    // Animate the progress bars
	    animateProgressBar(healthBar, health/100);
	    animateProgressBar(strengthBar, strength/100);
	    animateProgressBar(speedBar, speed/100);
	    animateProgressBar(defenseBar, defense/100);

	    // Setting other character info
	    nameTextField.setText(name);
	    applyTypingEffect(infoTextArea, description, Duration.millis(1));
	}

	// method to animate the progress bars
	private static void animateProgressBar(ProgressBar progressBar, double targetValue) {
	    // Store the current value of the progress bar
	    double startValue = progressBar.getProgress();

	    // Duration of the animation (in seconds)
	    Duration duration = Duration.seconds(0.5);  // Animation Speed Value

	    // Timeline to animate the progress bar
	    Timeline timeline = new Timeline(
	        new KeyFrame(Duration.ZERO, new KeyValue(progressBar.progressProperty(), startValue)),
	        new KeyFrame(duration, new KeyValue(progressBar.progressProperty(), targetValue))
	    );

	    // Play the animation
	    timeline.play();
	}
	
	
	private static void applyTypingEffect(TextArea textArea, String fullText, Duration typingSpeed) {
	    if (currentTypingTimeline != null) {
	        currentTypingTimeline.stop();
	    }

	    textArea.clear(); // Clear the text area before typing starts
	    StringBuilder displayedText = new StringBuilder();

	    currentTypingTimeline = new Timeline();
	    int batchSize = 20; // Number of characters to show at once per update
	    for (int i = 0; i < fullText.length(); i += batchSize) {
	        int end = Math.min(i + batchSize, fullText.length());
	        String textBatch = fullText.substring(i, end);
	        
	        int index = i; // Use an effectively final variable for lambda
	        currentTypingTimeline.getKeyFrames().add(
	            new KeyFrame(typingSpeed.multiply(index), event -> {
	                displayedText.append(textBatch); // Add the next batch of characters
	                textArea.setText(displayedText.toString());
	            })
	        );
	    }

	    // Action when typing is completed
	    currentTypingTimeline.setOnFinished(event -> System.out.println("Typing animation completed!"));

	    // Add event listener to skip the typing effect on click
	    EventHandler<MouseEvent> skipTypingEffectHandler = event -> {
	        if (event.getButton() == MouseButton.PRIMARY) { // Left click
	            currentTypingTimeline.stop();
	            textArea.setText(fullText); // Set the full text immediately
	        }
	    };

	    textArea.setOnMouseClicked(skipTypingEffectHandler);

	    currentTypingTimeline.play();
	}

	
	

}







