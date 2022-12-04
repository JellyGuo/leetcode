package encrypt;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHAUtil {
    private static String KEY_SHA = "SHA";
    private static String ALGORITHM = "SHA-256";

    public static byte[] encryptSHA(byte[] data) throws Exception {
        MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
        sha.update(data);
        return sha.digest();
    }

    public static String SHAEncrypt(final String content) {
        try {
            MessageDigest sha = MessageDigest.getInstance(KEY_SHA);
            byte[] sha_byte = sha.digest(content.getBytes());
            StringBuffer hexValue = new StringBuffer();
            for (byte b : sha_byte) {
                // 将其中的每个字节转成十六进制字符串：byte类型的数据最高位是符号位，通过和0xff进行与操作，转换为int类型的正整数
                String toHexString = Integer.toHexString(b & 0xff);
                hexValue.append(toHexString.length() == 1 ? "0" + toHexString : toHexString);
            }
            return hexValue.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }
    //SHA-256加密
    public static String SHA256Encrypt(String sourceStr){
        MessageDigest md = null;
        try {
            md = MessageDigest.getInstance(ALGORITHM);
        }catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if(md!=null){
            md.update(sourceStr.getBytes());
            return getDigestStr(md.digest());
        }
        return null;
    }

    private static String getDigestStr(byte[] originBytes){
        String tmpStr;
        StringBuilder sb = new StringBuilder();
        for(byte b:originBytes){
            tmpStr = Integer.toHexString(b&0xff);
            if(tmpStr.length() == 1){
                sb.append("0");
            }
            sb.append(tmpStr);
        }
        return sb.toString();
    }
}
