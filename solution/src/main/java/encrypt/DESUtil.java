package encrypt;

import sun.misc.BASE64Decoder;
import sun.misc.BASE64Encoder;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import java.security.Key;
import java.security.SecureRandom;

public class DESUtil {
    private static Key key;
    private static String KEY_STR = "myKey";
    private static String CHARSET_NAME = "UTF-8";
    private static String ALGORITHM = "DES";

    static {
        try {
            // 生成DES算法对象
            KeyGenerator generator = KeyGenerator.getInstance(ALGORITHM);
            // 运用SHA1 安全策略
            SecureRandom secureRandom = SecureRandom.getInstance("SHA1PRNG");
            // 设置上密钥种子
            secureRandom.setSeed(KEY_STR.getBytes());
            // 初始化基于SHA1的算法对象
            generator.init(secureRandom);
            // 生成密钥对象
            key = generator.generateKey();
            generator = null;
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * 加密
     *
     * @param str
     * @return
     */
    public static String getEncryptString(String str) {
        BASE64Encoder encoder = new BASE64Encoder();
        try {
            // 按utf8编码
            byte[] bytes = str.getBytes(CHARSET_NAME);
            // 获取加密对象
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            // 初始化密码信息
            cipher.init(Cipher.ENCRYPT_MODE, key);
            // 加密
            byte[] doFinal = cipher.doFinal(bytes);
            return encoder.encode(doFinal);
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    /**
     * 解密
     *
     * @param str
     * @return
     */
    public static String getDecryptString(String str) {
        BASE64Decoder decoder = new BASE64Decoder();
        try {
            byte[] bytes = decoder.decodeBuffer(str);
            Cipher cipher = Cipher.getInstance(ALGORITHM);
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] doFinal = cipher.doFinal(bytes);
            return new String(doFinal, CHARSET_NAME);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
