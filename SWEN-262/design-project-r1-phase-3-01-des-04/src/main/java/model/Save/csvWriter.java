package model.Save;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;

import java.io.IOException;

public class csvWriter implements fileWriter{
    private ObjectMapper objectMapper;
    private fileWriterCsv FileWriterCsv;

    public csvWriter() {


        FileWriterCsv = new fileWriterCsv();
        objectMapper = new ObjectMapper();
    }
    @Override
    public boolean saveMapData(MapProfile gameProgress) {
        try {
            String fileName;
            if(!gameProgress.getMap().isEndless()){
                fileName = gameProgress.getMap().getPlayerCharacter().getName() + "Map";
            } else { fileName="endless";}

            String csvData = objectMapper.writeValueAsString(gameProgress);
            FileWriterCsv.writeFile(csvData, fileName);
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public boolean saveUserProfile(UserProfile userAccount) {
        try {
            String csvData = objectMapper.writeValueAsString(userAccount);
            FileWriterCsv.writeFile(csvData, userAccount.getName());
            return true;
        } catch (IOException e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public UserProfile loadUserProfile(String userName) {
        try {
            if (FileWriterCsv.fileExists(userName)) {
                return objectMapper.readValue(FileWriterCsv.readFile(userName), UserProfile.class);
            } else {
                System.err.println("CSV file does not exist: " + userName + ".csv");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public MapProfile loadMapData(String mapName) {
        try {
            if (FileWriterCsv.fileExists(mapName)) {
                return objectMapper.readValue(FileWriterCsv.readFile(mapName), MapProfile.class);
            } else {
                System.err.println("CSV file does not exist: " + mapName + ".csv");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getFileType() {
        return "csv";
    }

    @Override
    public Boolean fileExists(String fileName){return FileWriterCsv.fileExists(fileName);}
}
