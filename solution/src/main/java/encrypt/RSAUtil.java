package encrypt;

import org.apache.commons.io.FileUtils;

import javax.crypto.Cipher;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;

public class RSAUtil {

    /**
     * 生成密钥对并保存在文件中
     *
     * @param algorithm：算法
     * @param pubPath：公钥保存路径
     * @param priPath：密钥保存路径
     */
    public static void generateKeyToFile(String algorithm, String pubPath, String priPath) throws NoSuchAlgorithmException, IOException {
        // 获取密钥对生成器
        KeyPairGenerator keyPairGenerator = KeyPairGenerator.getInstance(algorithm);
        // 获取密钥对
        KeyPair keyPair = keyPairGenerator.generateKeyPair();
        // 获取公钥
        PublicKey publicKey = keyPair.getPublic();
        // 获取私钥
        PrivateKey privateKey = keyPair.getPrivate();
        // 获取byte数组
        byte[] publicKeyEncoded = publicKey.getEncoded();
        byte[] privateKeyEncoded = privateKey.getEncoded();
        // 进行base64编码
        String publicKeyString = Base64.getEncoder().encodeToString(publicKeyEncoded);
        String privateKeyString = Base64.getEncoder().encodeToString(privateKeyEncoded);
        // 保存文件
        FileUtils.writeStringToFile(new File(pubPath), publicKeyString, StandardCharsets.UTF_8);
        FileUtils.writeStringToFile(new File(priPath), privateKeyString, StandardCharsets.UTF_8);
    }

    /**
     * 从文件中加载公钥
     *
     * @param algorithm：算法
     * @param filePath：文件路径
     * @return 公钥
     * @throws Exception
     */
    public static PublicKey loadPublicKeyFromFile(String algorithm, String filePath) throws Exception {
        String keyString = FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);
        return loadPublicKeyFromString(algorithm, keyString);
    }

    /**
     * 从字符串中加载公钥
     *
     * @param algorithm：算法
     * @param keyString：公钥字符串
     * @return 公钥
     * @throws NoSuchAlgorithmException
     * @throws InvalidKeySpecException
     */
    public static PublicKey loadPublicKeyFromString(String algorithm, String keyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 进行base64 解码
        byte[] decode = Base64.getDecoder().decode(keyString);
        // 获取密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        // 构建密钥规范
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decode);
        // 获取公钥
        return keyFactory.generatePublic(keySpec);
    }

    /**
     * 从文件中加载私钥
     *
     * @param algorithm：算法
     * @param filePath：文件路径
     * @return 私钥
     * @throws Exception
     */
    public static PrivateKey loadPrivateKeyFromFile(String algorithm, String filePath) throws Exception {
        String keyString = FileUtils.readFileToString(new File(filePath), StandardCharsets.UTF_8);
        return loadPrivateKeyFromString(algorithm, keyString);
    }

    /**
     * 从字符串中加载私钥
     *
     * @param algorithm:算法
     * @param keyString:私钥字符串
     * @return 私钥
     * @throws Exception
     */
    public static PrivateKey loadPrivateKeyFromString(String algorithm, String keyString) throws Exception {
        // 进行base64 解码
        byte[] decode = Base64.getDecoder().decode(keyString);
        // 获取密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        // 构建密钥规范
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decode);
        // 获取公钥
        return keyFactory.generatePrivate(keySpec);
    }

    /**
     * 使用密钥加密数据
     *
     * @param algorithm：算法
     * @param input：原文
     * @param key：密钥
     * @param maxEncryptSize：最大加密长度
     * @return 密文
     * @throws Exception
     */
    public static String encrypt(String algorithm, String input, Key key, int maxEncryptSize) throws Exception {
        //获取Cipher对象
        Cipher cipher = Cipher.getInstance(algorithm);
        // 初始化模式(加密)和密钥
        cipher.init(Cipher.ENCRYPT_MODE, key);
        // 将原文转换为byte数组
        byte[] data = input.getBytes();
        // 总数据长度
        int total = data.length;
        // 输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        decodeByte(maxEncryptSize, cipher, data, total, out);
        // 将密文进行base64 编码
        return Base64.getEncoder().encodeToString(out.toByteArray());
    }

    /**
     * 解密数据
     *
     * @param algorithm：算法
     * @param encrypt:              密文
     * @param key：密钥
     * @param maxDecryptSize：最大解密长度
     * @return 原文
     * @throws Exception
     */
    public static String decrypt(String algorithm, String encrypt, Key key, int maxDecryptSize) throws Exception {
        //获取Cipher对象
        Cipher cipher = Cipher.getInstance(algorithm);
        // 初始化模式(解密)和密钥
        cipher.init(Cipher.DECRYPT_MODE, key);
        // 由于密文进行了base64编码，这里先进行解码
        byte[] data = Base64.getDecoder().decode(encrypt);
        // 总数据长度
        int total = data.length;
        // 输出流
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        decodeByte(maxDecryptSize, cipher, data, total, out);
        // 输出原文
        return out.toString();
    }

    /**
     * 分段处理数据
     *
     * @param maxSize：最大处理能力
     * @param cipher：Cipher对象
     * @param data：要处理的byte数组
     * @param total：总数据长度
     * @param out：输出流
     * @throws Exception
     */
    private static void decodeByte(int maxSize, Cipher cipher, byte[] data, int total, ByteArrayOutputStream out) throws Exception {
        // 偏移量
        int offset = 0;
        // 缓冲区
        byte[] buffer;
        while (total - offset > 0) {
            // 如果剩余量>=最大处理能力，按照最大处理能力来加密数据
            if (total - offset >= maxSize) {
                // 加密数据
                buffer = cipher.doFinal(data, offset, maxSize);
                // 偏移量向右偏移最大数据能力个
                offset += maxSize;
            } else {
                // 如果剩余数量<最大处理能力，按照剩余个数加密数据
                buffer = cipher.doFinal(data, offset, total - offset);
                // 偏移量设置为总数据长度,跳出循环
                offset = total;
            }
            // 向输出流写入数据
            out.write(buffer);
        }
    }
}
