package com.alexmisko.util;

import org.apache.commons.codec.binary.Base64;

import javax.crypto.Cipher;
import java.nio.charset.StandardCharsets;
import java.security.*;
import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

/**
 * RSA加密
 * 非对称加密，有公钥和私钥之分，公钥用于数据加密，私钥用于数据解密。加密结果可逆
 * 公钥一般提供给外部进行使用，私钥需要放置在服务器端保证安全性。
 * 特点：加密安全性很高，但是加密速度较慢
 */
public class RSAUtil {

    private static final String PUBLIC_KEY = "MIGfMA0GCSqGSIb3DQEBAQUAA4GNADC" +
            "BiQKBgQDSYU360kAcqgoPUkuEZSE10JMr\n" +
            "OM2xrbWW2MjldjwpcmR0dc3ZaGO3KNADzcXq1iUr7jyCuchzjSAYi+SmzsvgICbl\n" +
            "fcGb21dokZQ5RLYQw3ucQqzDUy9/AHA7XYHFXjM1EleheqBpTz09ErtWlkL+4Ka4\n" +
            "acPzQVtS3W8rSemyCQIDAQAB";

    private static final String PRIVATE_KEY = "MIICeAIBADANBgkqhkiG9w0BAQEFAA" +
            "SCAmIwggJeAgEAAoGBANJhTfrSQByqCg9S\n" +
            "S4RlITXQkys4zbGttZbYyOV2PClyZHR1zdloY7co0APNxerWJSvuPIK5yHONIBiL\n" +
            "5KbOy+AgJuV9wZvbV2iRlDlEthDDe5xCrMNTL38AcDtdgcVeMzUSV6F6oGlPPT0S\n" +
            "u1aWQv7gprhpw/NBW1LdbytJ6bIJAgMBAAECgYEAtgnEc/hhyWvI8KEA/zqSlgI4\n" +
            "lfm84rWwnypOC19Xh3WYPr2AtVKClgYWmHQBgcZyu6SVb3haXsIKt7K00zG1blKg\n" +
            "3/mrrJTMI998Dj1m0fCMpASmvOe3S+FqeZP7SLoaa3KH0nm109moK8IwH1Tq8ItB\n" +
            "nstqvlS7RwryiZHyxEECQQD+A/CNf5qRhiDZpqI/tyiY+xvQ3Dbmd1uwprUWFE8G\n" +
            "e1T2J6PvF/1oJbSI+27jkAYNg3I7D1Ij/9dbMO75nPetAkEA1AYW213yn6QiMmTQ\n" +
            "tNWFs6eKEDhMpyJV3oU8ZSDOKuvyLmkjOVWL9PpdPmokvtdsXT/LrR5dnlDwHQ9t\n" +
            "rjJfTQJBAIJsn2zn4gNqbR+UEabJcozqEiQxjfbTvj57ums1zkWzubISNHglRzXc\n" +
            "dlAHWa56WsFBhruQaEqXqInoHCwKTpkCQAWQg1kOhieabytTagbU88VbSExUre5V\n" +
            "AFvusz8UNVT7DEgbikke7aVoTLounvhDPxzTZ80LVvmotiSNGxvPRqECQQC8hCTJ\n" +
            "/xrjgcYb3pD4f9nnWJN47y5Kogla6pU9IPZkyKnfaR5dJFgILqjNpEe/vXB2bxyf\n" +
            "cO93AkysehHuaDMi";

    public static void main(String[] args) throws Exception {
        String str = RSAUtil.decrypt("NKxlvB+DHStXtM8OKS6KOkA16m6tWTm5bVO2GfWEmfFo0TPyoh/KYyFvW/2H4rkIXPXoP7QGSeUxoBh4xmfQJVW7Z0Lo+3Cn3HD9ITkl+qZIoP49EGnfvQMp+m/6q6H8yGBuVS4JK9jifHqJdrvnjAaVZYXRVtI/Wojn/h4K9fY=");
        System.out.println(str);
    }

    /**
     * 得到公钥str
     *
     * @return {@link String}
     */
    public static String getPublicKeyStr() {
        return PUBLIC_KEY;
    }

    /**
     * 得到公钥
     *
     * @return {@link RSAPublicKey}
     * @throws Exception 异常
     */
    public static RSAPublicKey getPublicKey() throws Exception {
        byte[] decoded = Base64.decodeBase64(PUBLIC_KEY);
        return (RSAPublicKey) KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(decoded));
    }

    /**
     * 获得私钥
     *
     * @return {@link RSAPrivateKey}
     * @throws Exception 异常
     */
    public static RSAPrivateKey getPrivateKey() throws Exception {
        byte[] decoded = Base64.decodeBase64(PRIVATE_KEY);
        return (RSAPrivateKey) KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(decoded));
    }

    /**
     * 生成密钥对
     *
     * @return {@link RSAKey}
     * @throws NoSuchAlgorithmException 没有这样算法异常
     */
    public static RSAKey generateKeyPair() throws NoSuchAlgorithmException {
        KeyPairGenerator keyPairGen = KeyPairGenerator.getInstance("RSA");
        keyPairGen.initialize(1024, new SecureRandom());
        KeyPair keyPair = keyPairGen.generateKeyPair();
        RSAPrivateKey privateKey = (RSAPrivateKey) keyPair.getPrivate();
        RSAPublicKey publicKey = (RSAPublicKey) keyPair.getPublic();
        String publicKeyString = new String(Base64.encodeBase64(publicKey.getEncoded()));
        String privateKeyString = new String(Base64.encodeBase64(privateKey.getEncoded()));
        return new RSAKey(privateKey, privateKeyString, publicKey, publicKeyString);
    }

    /**
     * 加密
     *
     * @param source 源
     * @return {@link String}
     * @throws Exception 异常
     */
    public static String encrypt(String source) throws Exception {
        byte[] decoded = Base64.decodeBase64(PUBLIC_KEY);
        RSAPublicKey rsaPublicKey = (RSAPublicKey) KeyFactory.getInstance("RSA")
                .generatePublic(new X509EncodedKeySpec(decoded));
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(1, rsaPublicKey);
        return Base64.encodeBase64String(cipher.doFinal(source.getBytes(StandardCharsets.UTF_8)));
    }

    /**
     * 得到密码
     *
     * @return {@link Cipher}
     * @throws Exception 异常
     */
    public static Cipher getCipher() throws Exception {
        byte[] decoded = Base64.decodeBase64(PRIVATE_KEY);
        RSAPrivateKey rsaPrivateKey = (RSAPrivateKey) KeyFactory.getInstance("RSA")
                .generatePrivate(new PKCS8EncodedKeySpec(decoded));
        Cipher cipher = Cipher.getInstance("RSA");
        cipher.init(2, rsaPrivateKey);
        return cipher;
    }

    /**
     * 解密
     *
     * @param text 文本
     * @return {@link String}
     * @throws Exception 异常
     */
    public static String decrypt(String text) throws Exception {
        Cipher cipher = getCipher();
        byte[] inputByte = Base64.decodeBase64(text.getBytes(StandardCharsets.UTF_8));
        return new String(cipher.doFinal(inputByte));
    }

    /**
     * rsakey
     *
     * @author Guo
     * @date 2022/07/02
     */
    public static class RSAKey {
        private RSAPrivateKey privateKey;
        private String privateKeyString;
        private RSAPublicKey publicKey;
        public String publicKeyString;


        /**
         * rsakey
         *
         * @param privateKey       私钥
         * @param privateKeyString 私钥字符串
         * @param publicKey        公钥
         * @param publicKeyString  公钥字符串
         */
        public RSAKey(RSAPrivateKey privateKey, String privateKeyString, RSAPublicKey publicKey, String publicKeyString) {
            this.privateKey = privateKey;
            this.privateKeyString = privateKeyString;
            this.publicKey = publicKey;
            this.publicKeyString = publicKeyString;
        }

        /**
         * 获得私钥
         *
         * @return {@link RSAPrivateKey}
         */
        public RSAPrivateKey getPrivateKey() {
            return this.privateKey;
        }

        /**
         * 设置私钥
         *
         * @param privateKey 私钥
         */
        public void setPrivateKey(RSAPrivateKey privateKey) {
            this.privateKey = privateKey;
        }

        /**
         * 获得私钥字符串
         *
         * @return {@link String}
         */
        public String getPrivateKeyString() {
            return this.privateKeyString;
        }

        /**
         * 设置私钥字符串
         *
         * @param privateKeyString 私钥字符串
         */
        public void setPrivateKeyString(String privateKeyString) {
            this.privateKeyString = privateKeyString;
        }

        /**
         * 得到公钥
         *
         * @return {@link RSAPublicKey}
         */
        public RSAPublicKey getPublicKey() {
            return this.publicKey;
        }

        /**
         * 设置公钥
         *
         * @param publicKey 公钥
         */
        public void setPublicKey(RSAPublicKey publicKey) {
            this.publicKey = publicKey;
        }

        /**
         * 得到公钥字符串
         *
         * @return {@link String}
         */
        public String getPublicKeyString() {
            return this.publicKeyString;
        }

        /**
         * 设置公钥字符串
         *
         * @param publicKeyString 公钥字符串
         */
        public void setPublicKeyString(String publicKeyString) {
            this.publicKeyString = publicKeyString;
        }
    }
}
