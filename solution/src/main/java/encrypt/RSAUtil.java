package encrypt;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.Charset;
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
        FileUtils.writeStringToFile(new File(pubPath),publicKeyString,Charset.forName("UTF-8"));
    }

    public static PublicKey loadPublicKeyFromFile(String algorithm,String filePath){
        return null;
    }

    public static PublicKey loadPublicKeyFromString(String algorithm,String keyString) throws NoSuchAlgorithmException, InvalidKeySpecException {
        // 进行base64 解码
        byte[] decode = Base64.getDecoder().decode(keyString);
        // 获取密钥工厂
        KeyFactory keyFactory = KeyFactory.getInstance(algorithm);
        // 构建密钥规范
        X509EncodedKeySpec keySpec = new X509EncodedKeySpec(decode);
        // 获取公钥
        return keyFactory.generatePublic(keySpec);
    }
}
