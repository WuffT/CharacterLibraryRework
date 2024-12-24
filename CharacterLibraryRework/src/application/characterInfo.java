package application;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.image.Image;
import javafx.scene.layout.VBox;
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
 
 
 public static void loadAndEditCSVFile(File selectedFile) {
	    if (selectedFile != null) {
	        try {
	            // Clear existing character categories and UI components before loading new data
	            characterCategories.clear(); // Clear the old categories
	            buttonContainer.getChildren().clear(); // Clear any existing buttons

	            // Load characters from the selected CSV file
	            List<String> lines = Files.readAllLines(selectedFile.toPath());

	            // Create an ObservableList to store the CSV rows (this will be used for your TableView)
	            ObservableList<ObservableList<String>> newTableData = FXCollections.observableArrayList();
	            boolean missingPaths = false; // Flag to track if there are missing paths
	            Map<String, List<ObservableList<String>>> newCategories = new HashMap<>(); // Map to store categories

	            // Parse CSV lines into rows and columns
	            for (String line : lines) {
	                String[] values = line.split(","); // Assuming CSV is comma-separated

	                // Skip empty lines
	                if (line.trim().isEmpty()) continue;

	                // Ensure the CSV row has at least 6 columns before adding description, image, and render
	                if (values.length < 6) {
	                    continue; // Skip rows with insufficient data (we expect at least 6 columns for health, strength, speed, etc.)
	                }

	                // Check if there are missing paths for description, image, and render
	                String descriptionPath = values.length > 6 && !values[6].trim().isEmpty()
	                        ? values[6].trim() : "/characterDescriptions/WIPCharacter.txt"; // Default path if empty
	                String imagePath = values.length > 7 && !values[7].trim().isEmpty()
	                        ? values[7].trim() : "/icons/WIPIcon.png"; // Default path if empty
	                String renderPath = values.length > 8 && !values[8].trim().isEmpty()
	                        ? values[8].trim() : "/CharacterRenders/NoRender.png"; // Default path if empty

	                // Check if any of the paths are missing
	                if (descriptionPath.equals("/characterDescriptions/WIPCharacter.txt") ||
	                    imagePath.equals("/icons/WIPIcon.png") ||
	                    renderPath.equals("/CharacterRenders/NoRender.png")) {
	                    missingPaths = true; // Set flag if any default paths are used
	                }

	                // Add the description, image, and render paths explicitly to the row if missing
	                while (values.length < 9) {
	                    values = Arrays.copyOf(values, values.length + 1);
	                }
	                values[6] = descriptionPath;
	                values[7] = imagePath;
	                values[8] = renderPath;

	                // Create a row with values
	                ObservableList<String> row = FXCollections.observableArrayList(values);

	                // Add row data to the ObservableList (new table data)
	                newTableData.add(row);

	                // Update the categories map based on the first column (Category)
	                String category = values[0].trim(); // First column is the category
	                newCategories.putIfAbsent(category, new ArrayList<>());
	                newCategories.get(category).add(row); // Add row to the appropriate category list
	            }

	            // Clear existing TableView before loading new data
	            TableView<ObservableList<String>> newTableView = new TableView<>();

	            // Create columns based on the number of columns in the CSV file
	            for (int i = 0; i < newTableData.get(0).size(); i++) {
	                TableColumn<ObservableList<String>, String> column = new TableColumn<>("Column " + (i + 1));
	                int colIndex = i;
	                column.setCellValueFactory(cellData -> new SimpleStringProperty(cellData.getValue().get(colIndex)));

	                // Make the column editable
	                column.setCellFactory(TextFieldTableCell.forTableColumn());
	                column.setOnEditCommit(event -> {
	                    ObservableList<String> row = event.getRowValue();
	                    row.set(colIndex, event.getNewValue());
	                });
	                newTableView.getColumns().add(column);
	            }

	            // Set the new table data
	            newTableView.setItems(newTableData);
	            newTableView.setEditable(true);

	            // Create a dialog to display the TableView to the user
	            VBox vbox = new VBox(newTableView);
	            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
	            alert.setTitle("Edit CSV File");
	            alert.setHeaderText("Edit the loaded CSV content in the table below.");
	            alert.getDialogPane().setContent(vbox);

	            // If any paths are missing, show a warning
	            if (missingPaths) {
	                alert.setHeaderText("Warning: Some paths are missing, default paths will be used for missing description, image, and render paths. Leave these alone if you dont have any paths  this will be updated soon.");
	            }

	            // Add confirm and cancel buttons
	            ButtonType confirmButton = new ButtonType("Confirm", ButtonData.OK_DONE);
	            ButtonType cancelButton = new ButtonType("Cancel", ButtonData.CANCEL_CLOSE);
	            alert.getButtonTypes().setAll(confirmButton, cancelButton);

	            Optional<ButtonType> result = alert.showAndWait();

	            if (result.isPresent() && result.get() == confirmButton) {
	                // If confirmed, save the edited content back to the file
	                StringBuilder updatedCsv = new StringBuilder();
	                for (ObservableList<String> row : newTableData) {
	                    updatedCsv.append(String.join(",", row)).append("\n");
	                }
	                Files.write(Paths.get(selectedFile.toURI()), updatedCsv.toString().getBytes());

	                // After saving, trigger the loadCharactersFromCSV method to reload the data
	                characterInfo.loadCharactersFromCSV(selectedFile.getAbsolutePath()); // Load the updated data

	                // After the CSV is loaded, refresh the ComboBox and character buttons
	                refreshUI();  // Call this method to refresh the ComboBox and button containers

	                System.out.println("CSV file updated and UI refreshed successfully!");
	            }

	        } catch (IOException e) {
	            System.err.println("Error reading or saving the file.");
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

 
//We do not use the method anymore to initialize character data now. Instead all character data is loaded through a new CSV format.
	//In this case the data is now inside of a comma seperated values file which will have each character have its data inside of a table and other things like that.
 
 /* As a legacy example this is how the data is handled. AGAIN CHARACTER DATA IS NOW HANDLED IN A NEW CSV FILE.
        "Character Name",
        0.55,  // Health
        0.88,  // Strength
        0.35,  // Speed
        0.20,  // Defense    //all values above are from 0-1 || Example: 0.55 is really just 55% while 1 is 100%
        "/characterDescriptions/character.txt",
        "/icons/charactericon.png",
        "/CharacterRenders/characterrender.png"

*/
 
 
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
	    animateProgressBar(healthBar, health);
	    animateProgressBar(strengthBar, strength);
	    animateProgressBar(speedBar, speed);
	    animateProgressBar(defenseBar, defense);

	    // Setting other character info
	    nameTextField.setText(name);
	    applyTypingEffect(infoTextArea, description, Duration.millis(5));
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
	
	
	// Typing effect method
	private static void applyTypingEffect(TextArea textArea, String fullText, Duration typingSpeed) {
	    if (currentTypingTimeline != null) {
	        currentTypingTimeline.stop();
	    }
		
	    textArea.clear(); // Clear the text area before typing starts
	    StringBuilder displayedText = new StringBuilder();

	    currentTypingTimeline = new Timeline();
	    for (int i = 0; i < fullText.length(); i++) {
	        int index = i; // Use an effectively final variable for lambda
	        currentTypingTimeline.getKeyFrames().add(
	            new KeyFrame(typingSpeed.multiply(index), event -> {
	                displayedText.append(fullText.charAt(index)); // Add the next character
	                textArea.setText(displayedText.toString());
	            })
	        );
	    }

	    // Optional: Add an action to perform when typing is complete. This is nothing special it was just used for testing.
	    currentTypingTimeline.setOnFinished(event -> System.out.println("Typing animation completed!"));
	    currentTypingTimeline.play();
	}

	
	

}







