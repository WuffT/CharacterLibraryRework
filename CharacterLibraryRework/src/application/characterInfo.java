package application;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javafx.animation.KeyFrame;
import javafx.animation.KeyValue;
import javafx.animation.Timeline;
import javafx.scene.control.ProgressBar;
import javafx.scene.control.TextArea;
import javafx.scene.image.Image;
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
 
//THIS METHOD INITIALIZES THE CHARACTER DETAILS BY TAKING THEIR NAME, STATS AND DESCRIPTION INTO A DATA MAP/TABLE
 
 /* Code Example. This goes inside the initiliazeCharacterData method
 
 characterMap.put("Character Name", new characterInfo(
        "Character Name",
        0.55,  // Health
        0.88,  // Strength
        0.35,  // Speed
        0.20,  // Defense
        //all values above are from 0-1 || Example: 0.55 is really just 55% while 1 is 100%
        "/characterDescriptions/character.txt",
        "/icons/charactericon.png",
        "/CharacterRenders/characterrender.png"
    ));

*/
 
 public static void initializeCharacterData() {
	    characterMap.put("Wolftical Triglowsticus", new characterInfo(
	        "WolfTical Triglowsticus",
	        0.80,  // Health
	        0.40,  // Strength
	        0.61,  // Speed
	        0.80,  // Defense
	        "/characterDescriptions/WuffT.txt",
            "/icons/wuffpix.png",
            "/CharacterRenders/WolfTicalT.png"
	    ));

	    characterMap.put("Viraxe Eleviac", new characterInfo(
	        "Viraxe Eleviac",
	        0.75,  // Health
	        0.65,  // Strength
	        0.70,  // Speed
	        0.60,  // Defense
	        "/characterDescriptions/ViraxeEleviac.txt",
	        "/icons/birtpix.png",
            "/CharacterRenders/ViraxeE.png"
	    ));
	    
	    characterMap.put("Wren Ryzen", new characterInfo(
		        "Wren Ryzen",
		        0.55,  // Health
		        0.88,  // Strength
		        0.35,  // Speed
		        0.20,  // Defense
		        "/characterDescriptions/WrenRyzen.txt",
		        "/icons/wrenpix.png",
	            "/CharacterRenders/WrenR.png"
		    ));
	  
	    characterMap.put("Doxyn Larchiux", new characterInfo(
		        "Doxyn Larchiux",
		        0.45,  // Health
		        0.56,  // Strength
		        1,  // Speed
		        0.50,  // Defense
		        "/characterDescriptions/DoxynLarchiux.txt",
		        "/icons/doxpix.png",
	            "/CharacterRenders/NoRender.png"
		    ));
	    
	    characterMap.put("Archie Larchiux", new characterInfo(
		        "Archie Larchiux",
		        0.63,  // Health
		        0.20,  // Strength
		        0.72,  // Speed
		        0.05,  // Defense
		        "/characterDescriptions/ArchieLarchiux.txt",
		        "/icons/archiepix.png",
	            "/CharacterRenders/NoRender.png"
		    ));

	    characterMap.put("Drex Dixton", new characterInfo(
		        "Drex Dixton",
		        1,  // Health
		        0.89,  // Strength
		        0.20,  // Speed
		        1,  // Defense
		        "/characterDescriptions/DrexDixton.txt",
		        "/icons/drexpix.png",
	            "/CharacterRenders/NoRender.png"
		    ));
	    
	    characterMap.put("Drax Dixton", new characterInfo(
		        "Drax Dixton",
		        1,  // Health
		        0.20,  // Strength
		        0.90,  // Speed
		        1,  // Defense
		        "/characterDescriptions/DraxDixton.txt",
		        "/icons/draxpix.png",
	            "/CharacterRenders/NoRender.png"
		    ));
	    
	    characterMap.put("Dr. Stenfort", new characterInfo(
		        "Dr. Stenfort",
		        0.99,  // Health
		        0.99,  // Strength
		        0.60,  // Speed
		        0.05,  // Defense
		        "/characterDescriptions/BillStenfort.txt",
		        "/icons/stepix.png",
	            "/CharacterRenders/NoRender.png"
		    ));
	    
	    characterMap.put("Zalfor Tylox", new characterInfo(
	            "Zalfor Tylox",
	            0.95,  // Health
	            0.85,  // Strength
	            0.36,  // Speed
	            0.27,  // Defense
	            "/characterDescriptions/WIPCharacter.txt",
	            "/icons/zalpix.png",
	            "/CharacterRenders/NoRender.png"
	        ));
	    
	    characterMap.put("Teforel Vaxin", new characterInfo(
	            "Teforel Vaxin",
	            0.85,  // Health
	            0.35,  // Strength
	            0.58,  // Speed
	            0.60,  // Defense
	            "/characterDescriptions/WIPCharacter.txt",
	            "/icons/tefpix.png",
	            "/CharacterRenders/NoRender.png"
	        ));
	    
	    
	    characterMap.put("Incident K-5520", new characterInfo(
	            "Incident K-5520",
	            0.75,  // Health
	            1,  // Strength
	            0.44,  // Speed
	            0.35,  // Defense
	            "/incidentDescriptions/INC_K5520.txt",
	            "/icons/keetpix.png",
	            "/incidentRenders/INC_K5520.png"
	        ));
	    
	    
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
	    animateProgressBar(healthBar, health);
	    animateProgressBar(strengthBar, strength);
	    animateProgressBar(speedBar, speed);
	    animateProgressBar(defenseBar, defense);

	    // Setting other character info
	    nameTextField.setText(name);
	    applyTypingEffect(infoTextArea, description, Duration.millis(5));
	}

	// Helper method to animate the progress bars
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

	    // Optional: Add an action to perform when typing is complete
	    currentTypingTimeline.setOnFinished(event -> System.out.println("Typing animation completed!"));
	    currentTypingTimeline.play();
	}

	
	

}







