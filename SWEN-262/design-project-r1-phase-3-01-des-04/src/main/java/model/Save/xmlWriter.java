package model.Save;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.xml.*;

import java.io.IOException;

public class xmlWriter implements fileWriter{

    private ObjectMapper objectMapper;
    private fileWriterXml FileWriterXml;

    public xmlWriter() {
        objectMapper = new ObjectMapper();
        FileWriterXml = new fileWriterXml();
    }
    @Override
    public boolean saveMapData(MapProfile gameProgress) {
        try {
            String fileName;
            if(!gameProgress.getMap().isEndless()){
                fileName = gameProgress.getMap().getPlayerCharacter().getName() + "Map";
            } else { fileName="endless";}

            String xmlData = objectMapper.writeValueAsString(gameProgress);
            FileWriterXml.writeFile(xmlData, fileName);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean saveUserProfile(UserProfile userAccount) {
        try {
            String xmlData = objectMapper.writeValueAsString(userAccount);
            FileWriterXml.writeFile(xmlData, userAccount.getName());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public UserProfile loadUserProfile(String userName) {
        try {
            if (FileWriterXml.fileExists(userName)) {
                return objectMapper.readValue(FileWriterXml.readFile(userName), UserProfile.class);
            } else {
                System.err.println("XML file does not exist: " + userName + ".xml");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    @Override
    public MapProfile loadMapData(String mapName) {
        try {
            if (FileWriterXml.fileExists(mapName)) {
                return objectMapper.readValue(FileWriterXml.readFile(mapName), MapProfile.class);
            } else {
                System.err.println("XML file does not exist: " + mapName + ".xml");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getFileType() {
        return "xml";
    }

    @Override
    public Boolean fileExists(String fileName){return FileWriterXml.fileExists(fileName);}
}
