package application;

import java.io.*;
import java.net.*;
import java.nio.file.*;
import java.util.*;

public class characterFileManager {

    // Directory where the custom character data will be stored
    private static final String CHARACTER_DIRECTORY = getAppDirectory() + "/customCharacterDirectory/";

    // Get the directory where the executable or JAR file is located
    private static String getAppDirectory() {
        try {
            // For JAR application
            File file = new File(characterFileManager.class.getProtectionDomain().getCodeSource().getLocation().toURI());
            String path = file.getParent(); // Get the parent directory of the JAR

            // If running from an executable, path should point to the executable's directory
            if (path != null) {
                return path; // Return the directory where the .exe or .jar is located
            }
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }

        // Default fallback if it fails
        return System.getProperty("user.dir");
    }

    // Copy resources from JAR to the custom character directory
    public static void copyResourcesFromJarToCharacterDirectory() {
        try {
            // Get the path where the EXE or JAR is launched from
            String appDirectory = getAppDirectory();
            File customCharacterDir = new File(appDirectory, "customCharacterDirectory");

            // Check if the main folder exists, if not, create it
            if (!customCharacterDir.exists()) {
                if (customCharacterDir.mkdirs()) {
                    System.out.println("Created main folder: " + customCharacterDir.getPath());
                } else {
                    System.err.println("Failed to create main folder: " + customCharacterDir.getPath());
                    return;
                }
            }

            // Create subdirectories if they don't exist
            String[] subdirectories = { "customCharacterDescription", "customCharacterIcon", "customCharacterRender" };
            for (String subDir : subdirectories) {
                File subdirectory = new File(customCharacterDir, subDir);
                if (!subdirectory.exists()) {
                    if (subdirectory.mkdirs()) {
                        System.out.println("Created subdirectory: " + subdirectory.getPath());
                    } else {
                        System.err.println("Failed to create subdirectory: " + subdirectory.getPath());
                    }
                }
            }

            String folderPathInJar = "/customCharacterDescription/"; // Modify to match your folder structure
            URL url = characterFileManager.class.getResource(folderPathInJar);
            if (url == null) {
                System.err.println("Folder not found in JAR: " + folderPathInJar);
                return;
            }

            if (url.getProtocol().equals("jar")) {
                URI jarUri = new URI(url.toString().split("!")[0]);
                FileSystem fs = FileSystems.newFileSystem(jarUri, Collections.emptyMap());
                Path jarFolder = fs.getPath(folderPathInJar);

                Files.walk(jarFolder).forEach(jarFile -> {
                    try {
                        Path relativePath = jarFolder.relativize(jarFile);
                        File userFile = new File(customCharacterDir, relativePath.toString());

                        if (Files.isDirectory(jarFile)) {
                            if (!userFile.exists()) {
                                userFile.mkdirs();
                            }
                        } else {
                            if (!userFile.exists()) {
                                copyFileFromJar(jarFile, userFile);
                            }
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                fs.close();
            } else {
                System.err.println("Resources are not inside a JAR.");
            }
        } catch (Exception e) {
            System.err.println("Error copying resources from JAR to custom character directory: " + e.getMessage());
        }
    }

    // Helper method to copy a file from the JAR to the custom character directory
    public static void copyFileFromJar(Path jarFile, File userFile) throws IOException {
        try (InputStream in = Files.newInputStream(jarFile);
             OutputStream out = new FileOutputStream(userFile)) {
            byte[] buffer = new byte[1024];
            int bytesRead;
            while ((bytesRead = in.read(buffer)) != -1) {
                out.write(buffer, 0, bytesRead);
            }
        }
    }

    // Method to watch the custom character directory for changes and reload the files
    public static void watchForFileChanges() {
        try {
            // Get the path to the custom character directory
            Path path = Paths.get(CHARACTER_DIRECTORY);
            
            // Create a WatchService to watch for changes
            WatchService watchService = FileSystems.getDefault().newWatchService();
            
            // Register for the file events we want to track
            path.register(watchService, StandardWatchEventKinds.ENTRY_MODIFY, 
                          StandardWatchEventKinds.ENTRY_CREATE, 
                          StandardWatchEventKinds.ENTRY_DELETE);
            
            while (true) {
                // Take the next watch key (blocking)
                WatchKey key = watchService.take();
                
                // Process each event
                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path filename = ev.context();
                    
                    System.out.println("Event kind: " + kind + ", File: " + filename);
                    
                    // If a file is modified, reload it
                    if (kind == StandardWatchEventKinds.ENTRY_MODIFY) {
                        File updatedFile = new File(path.toFile(), filename.toString());
                        reloadFile(updatedFile);
                    }
                    // Handle other event types (create, delete) as necessary
                }
                
                // Reset the key so that we can continue watching
                boolean valid = key.reset();
                if (!valid) {
                    break; // Exit the loop if the key is no longer valid
                }
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    // Reload the file content (this method can be customized to update data in your app)
    public static void reloadFile(File file) {
        try {
            System.out.println("Reloading file: " + file.getName());
            // Read the updated content from the file and update the application
            BufferedReader reader = new BufferedReader(new FileReader(file));
            String line;
            while ((line = reader.readLine()) != null) {
                System.out.println(line); // Print content (or update your app state accordingly)
            }
            reader.close();
        } catch (IOException e) {
            System.err.println("Error reading updated file: " + file.getName());
        }
    }


}


