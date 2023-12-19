package model.Save;


import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;

public class fileWriterXml {


    public fileWriterXml(){}


    public boolean fileExists(String fileName){
        File file = new File(fileName+".xml");
        return file.exists();
    }

    public void writeFile(String xmlData, String fileName) throws IOException {
        File file = new File(fileName+".xml");
        FileOutputStream fos = new FileOutputStream(file);
        OutputStreamWriter osw = new OutputStreamWriter(fos, StandardCharsets.UTF_8);
        BufferedWriter bw = new BufferedWriter(osw);
        try (PrintWriter pw = new PrintWriter(bw, true)) {
            pw.write(xmlData);
        }

    }

    public String readFile(String fileName) throws IOException {
        File file = new File(fileName+".xml");
        return Files.readString(file.toPath());
    }

}
