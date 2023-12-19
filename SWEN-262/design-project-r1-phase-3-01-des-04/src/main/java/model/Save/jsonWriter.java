package model.Save;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.IOException;

public class jsonWriter implements fileWriter{
    private ObjectMapper objectMapper;

    private fileWriterJson FileWriterJson;

    public jsonWriter() {

        objectMapper = new ObjectMapper();
        FileWriterJson = new fileWriterJson();
    }
    @Override
    public boolean saveMapData(MapProfile gameProgress) {
        try {

            String fileName;
            if(!gameProgress.getMap().isEndless()){
                fileName = gameProgress.getMap().getPlayerCharacter().getName() + "Map";
            } else { fileName="endless";}

            String jsonData = objectMapper.writeValueAsString(gameProgress);
            FileWriterJson.writeFile(jsonData, fileName);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean saveUserProfile(UserProfile userAccount) {
        try {
            String jsonData = objectMapper.writeValueAsString(userAccount);
            FileWriterJson.writeFile(jsonData, userAccount.getName());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }


    @Override
    public UserProfile loadUserProfile(String userName) {
        try {
            if (FileWriterJson.fileExists(userName)) {
                return objectMapper.readValue(FileWriterJson.readFile(userName), UserProfile.class);
            } else {
                System.err.println("JSON file does not exist: " + userName + ".json");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public MapProfile loadMapData(String mapName) {
        try {
            if (FileWriterJson.fileExists(mapName)) {
                return objectMapper.readValue(FileWriterJson.readFile(mapName), MapProfile.class);
            } else {
                System.err.println("JSON file does not exist: " + mapName + ".json");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getFileType() {
        return "json";
    }

    @Override
    public Boolean fileExists(String fileName){return FileWriterJson.fileExists(fileName);}
}
