/*
 * Copyright 2021 Shang Yehua <niceshang@outlook.com>
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use this file except
 * in compliance with the License. You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software distributed under the License
 * is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 * or implied. See the License for the specific language governing permissions and limitations under
 * the License.
 */
package io.github.deergate.serializable;

import java.util.Base64;
import java.nio.charset.Charset;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * TODO AesTool说明
 *
 * @author Shang Yehua <niceshang@outlook.com>
 * @since 2021-09-15  12:13
 *
 */
public class AesTool {
    private final static byte[] key = "TuanTuanQiQi__Go".getBytes(Charset.forName("ASCII"));

    public static byte[] encrypt(byte[] data)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"));
        byte[] result = cipher.doFinal(data);
        return result;
    }

    public static byte[] decrypt(byte[] data)
            throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException,
            IllegalBlockSizeException, BadPaddingException {
        Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"));
        byte[] result = cipher.doFinal(data);
        return result;
    }

    public static byte[] encryptGCM(byte[] data, byte[] key, byte[] iv, byte[] aad)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.ENCRYPT_MODE, new SecretKeySpec(key, "AES"),
                new GCMParameterSpec(128, iv));
        cipher.updateAAD(aad);
        byte[] result = cipher.doFinal(data);
        return result;
    }

    public static byte[] decryptGCM(byte[] data, byte[] key, byte[] iv, byte[] aad)
            throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException,
            BadPaddingException, IllegalBlockSizeException, InvalidAlgorithmParameterException {
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        cipher.init(Cipher.DECRYPT_MODE, new SecretKeySpec(key, "AES"),
                new GCMParameterSpec(128, iv));
        cipher.updateAAD(aad);
        byte[] result = cipher.doFinal(data);
        return result;
    }

    public static void main(String[] args)
            throws IllegalBlockSizeException, InvalidKeyException, BadPaddingException,
            NoSuchAlgorithmException, NoSuchPaddingException, InvalidAlgorithmParameterException {
        String data = "Hello World"; // 待加密的原文
        String key = "12345678abcdefgh"; // key 长度只能是 16、25 或 32 字节
        String iv = "iviviviviviviviv";
        String aad = "aad"; // AAD 长度无限制，可为空

        byte[] ciphertext =
                encryptGCM(data.getBytes(), key.getBytes(), iv.getBytes(), aad.getBytes());
        System.out.println("GCM 模式加密结果（Base64）：" + Base64.getEncoder().encodeToString(ciphertext));

        byte[] plaintext = decryptGCM(ciphertext, key.getBytes(), iv.getBytes(), aad.getBytes());
        System.out.println("解密结果：" + new String(plaintext));
    }
}
