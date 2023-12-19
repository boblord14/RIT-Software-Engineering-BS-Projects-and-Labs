package model.Save;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class fileWriterCsv {
    public fileWriterCsv(){}
    public boolean fileExists(String fileName){
        File file = new File(fileName+".csv");
        return file.exists();
    }
    public void writeFile(String csvData, String fileName) throws IOException {
        File file = new File(fileName+".csv");
        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        BufferedWriter bw = new BufferedWriter(osw);
        try (PrintWriter pw = new PrintWriter(bw, true)) {
            pw.write(csvData);
        }
    }

    public String readFile(String fileName) throws IOException {
        File file = new File(fileName+".csv");
        return Files.readString(file.toPath());
    }

}
