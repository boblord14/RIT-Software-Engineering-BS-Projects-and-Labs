package model.Save;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class fileWriterJson {


    public fileWriterJson(){}


    public boolean fileExists(String fileName){
        File file = new File(fileName+".json");
        return file.exists();
    }

    public void writeFile(String jsonData, String fileName) throws FileNotFoundException {
        File file = new File(fileName+".json");
        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        BufferedWriter bw = new BufferedWriter(osw);
        try (PrintWriter pw = new PrintWriter(bw, true)) {
            pw.write(jsonData);
        }
    }

    public String readFile(String fileName) throws IOException {
        File file = new File(fileName+".json");
        return Files.readString(file.toPath());
    }

}
