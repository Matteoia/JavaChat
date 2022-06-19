package documenti;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class Test {
    public static void main(String[] args) throws IOException {
        StringBuilder sb = new StringBuilder();
        File f = new File("./src/javachat/documenti/Comandi.txt");
        System.out.println(f.getAbsolutePath());
        BufferedReader br = new BufferedReader(new FileReader(f));
        String tmp;
        while((tmp = br.readLine()) != null)
            sb.append(tmp).append("\n");
        System.out.println(sb.toString());
    }
}
