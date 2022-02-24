package at.vs.vsmarkthartmannsdorf.bl;

import at.vs.vsmarkthartmannsdorf.data.PropertyName;
import javafx.beans.property.Property;

import java.io.*;
import java.nio.file.Paths;
import java.util.Properties;

public class PropertiesLoader {
    private static PropertiesLoader instance;
    private Properties properties;

    private static final File PROPERTY_FILE = Paths.get("", "data", "config.properties").toFile();

    public static PropertiesLoader getInstance(){
        if (instance == null){
            instance = new PropertiesLoader();
        }

        return instance;
    }

    private PropertiesLoader(){
        try {
            PROPERTY_FILE.getParentFile().mkdirs();
            PROPERTY_FILE.createNewFile();
        }catch (Exception e){
            e.printStackTrace();
        }

        properties = new Properties();
        try (InputStream is = new FileInputStream(PROPERTY_FILE)){
            properties.load(is);
        }catch(Exception e){
            e.printStackTrace();
        }

        // properties.setProperty(PropertyName.export_folder.toString(), "C:/");
        // properties.setProperty(PropertyName.theme.toString(), "hell");
    }

    public void addProperty(PropertyName propertyName, String property){
        properties.setProperty(propertyName.toString(), property);
        saveProperties();
    }

    public void saveProperties(){
        try(OutputStream os = new FileOutputStream(PROPERTY_FILE)){
            properties.store(os, null);
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public Properties getProperties(){
        return properties;
    }

    /* public static void main(String[] args) {
        System.out.println(PROPERTY_FILE.getAbsolutePath());
        PropertiesLoader.getInstance().addProperty(PropertyName.export_folder, "C:/");
        PropertiesLoader.getInstance().addProperty(PropertyName.theme, "dark");
        PropertiesLoader.getInstance().addProperty(PropertyName.export_folder, "C:/");
        PropertiesLoader.getInstance().saveProperties();
        System.out.println(PropertiesLoader.getInstance().properties);
    } */
}
