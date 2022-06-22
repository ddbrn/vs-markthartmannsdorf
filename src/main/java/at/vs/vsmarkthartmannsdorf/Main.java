package at.vs.vsmarkthartmannsdorf;

public class Main {
    public static void main(String[] args) {
        System.setProperty("log4j.configurationFile","./path_to_the_log4j2_config_file/log4j2.xml");


        MainApplication.launch(MainApplication.class);
    }
}
