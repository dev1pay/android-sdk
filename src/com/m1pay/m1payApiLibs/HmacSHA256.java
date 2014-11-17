
package com.m1pay.m1payApiLibs;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class HmacSHA256 {
    private Mac mac = null;

    public static HmacSHA256 getInstance(String secret) {
        return new HmacSHA256(secret);
    }

    public HmacSHA256(String secret) {
        SecretKeySpec signingKey = new SecretKeySpec(secret.getBytes(), "HmacSHA256");
        try {
            mac = Mac.getInstance("HmacSHA256");
            mac.init(signingKey);
        } catch (InvalidKeyException e) {
            throw new RuntimeException("Invalid key exception while converting to HMac SHA256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
    }

    private String byteArrayToString(byte[] data) {
        BigInteger bigInteger = new BigInteger(1, data);
        String hash = bigInteger.toString(16);
        // Zero pad it
        while (hash.length() < 64) {
            hash = "0" + hash;
        }
        return hash;
    }

    public String sign(String data) {
        try {
            byte[] digest = mac.doFinal(data.getBytes("UTF-8"));
            return byteArrayToString(digest);
        } catch (IllegalStateException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String hmacDigest(String msg, String keyString, String algo) {
        String digest = null;
        try {
            SecretKeySpec key = new SecretKeySpec((keyString).getBytes("UTF-8"), algo);
            Mac mac = Mac.getInstance(algo);
            mac.init(key);

            byte[] bytes = mac.doFinal(msg.getBytes("ASCII"));

            StringBuffer hash = new StringBuffer();
            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(0xFF & bytes[i]);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            digest = hash.toString();
        } catch (UnsupportedEncodingException e) {
        } catch (InvalidKeyException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        return digest;
    }
}
