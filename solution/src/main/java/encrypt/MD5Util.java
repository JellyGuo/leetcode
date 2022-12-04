package encrypt;

import java.security.MessageDigest;

public class MD5Util {
    public static final String KEY_MD5 = "MD5";

    /**
     * MD5加密(生成唯一的MD5值)
     * @param data
     * @return
     * @throws Exception
     */
    public static byte[] encryptMD5(byte[] data) throws Exception {
        MessageDigest md5 = MessageDigest.getInstance(KEY_MD5);
        md5.update(data);
        return md5.digest();
    }
}
