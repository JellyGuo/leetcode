package encrypt;

import java.io.*;
import java.nio.charset.StandardCharsets;

public class MyUtil {

    public static void main(String[] args) {
        MyUtil.decryptAndUncompress();
    }
    public static void compressAndEncrypt() {
        try {
            FileInputStream in = new FileInputStream("C:\\Users\\GDJessica\\Documents\\Solution1Encode.txt");
            InputStreamReader reader = new InputStreamReader(in, "UTF-8");
            BufferedReader br = new BufferedReader(reader);

            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            br.close();
            reader.close();
            System.out.println("Done Reading");
            FileOutputStream out = new FileOutputStream("C:\\Users\\GDJessica\\Documents\\Solution1Decode.txt");
            String s = GZipUtil.compress(sb.toString());
            s = DESUtil.getEncryptString(s);
            byte[] b = s.getBytes(StandardCharsets.UTF_8);
            out.write(b);
            out.close();
            System.out.println("Done Writing");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void decryptAndUncompress() {
        try {
            FileInputStream in = new FileInputStream("C:\\Users\\GDJessica\\Documents\\Solution1Encode.txt");
            InputStreamReader reader = new InputStreamReader(in);
            BufferedReader br = new BufferedReader(reader);
            String line;
            StringBuilder sb = new StringBuilder();
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            br.close();
            reader.close();
            System.out.println("Done reading");
            FileOutputStream out = new FileOutputStream("C:\\Users\\GDJessica\\Documents\\Solution1Decode.txt");
            String s = DESUtil.getDecryptString(sb.toString());
            s = GZipUtil.uncompress(s);
            byte[] b = s.getBytes(StandardCharsets.UTF_8);
            out.write(b);
            out.close();
            System.out.println("Done writing");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
