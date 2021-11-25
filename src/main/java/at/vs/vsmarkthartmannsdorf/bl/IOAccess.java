package at.vs.vsmarkthartmannsdorf.bl;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

// 25.11.2021 Simon
public class IOAccess {

    private static File FILE_STUDENT = Paths.get(System.getProperty("user.dir"), "src", "main", "resources","data.json").toFile();

    public static boolean storeUserFiles(String jsonStr) {
        try {

            FileWriter fileWriter = new FileWriter(IOAccess.FILE_STUDENT.getAbsolutePath());
            fileWriter.write(jsonStr);
            fileWriter.close();
            System.out.println("FileWrite wrote in \"" + IOAccess.FILE_STUDENT.getName() + "\".");

            return true; //successfully wrote data

        } catch (IOException e) {
            System.out.println("Failed to write in the File: \"" + IOAccess.FILE_STUDENT.getName() + "\".");
            e.printStackTrace();
            return false; //not successfully wrote data
        }
    }

    public static String readUserFiles() {
        String result = "";
        try  {
            result = Files.readString(Paths.get(FILE_STUDENT.getAbsolutePath()));
        } catch (IOException e) {
            e.printStackTrace();
        }
        return result;
    }

}
