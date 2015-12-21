package org.emmanet.util;

/*
 * #%L
 * InfraFrontier
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2015 EMBL-European Bioinformatics Institute
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.io.UnsupportedEncodingException;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import org.apache.commons.codec.binary.Base64;

public class Encrypter {

    private Cipher ecipher;
    private Cipher dcipher;
    /*
     
     * Need to find a better more secure way to store keys.
     * tried java keystore which didn't work without error
     * tried plain text file which didn't work
     * this method semed fine.
     * 
     */
    final static String KEY = "Yp&_LL80ul0o8HN3";//must be 16 long 
    final static String IV = "ZH5q0%amzsN5UTGt";//must be 16 long

    public Encrypter() {
        try {
            ecipher = Cipher.getInstance("AES/CBC/PKCS5Padding");

            dcipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            //System.out.println("byte size of key == " + KEY.getBytes().length);
            //System.out.println("byte size of ivparam == " + IV.getBytes().length);
            SecretKeySpec keySpec = new SecretKeySpec(KEY.getBytes(), "AES");
            IvParameterSpec ivSpec = new IvParameterSpec(IV.getBytes());
            ecipher.init(Cipher.ENCRYPT_MODE, keySpec, ivSpec);
            //ecipher.init(Cipher.ENCRYPT_MODE, key);
            //dcipher.init(Cipher.DECRYPT_MODE, key);
            dcipher.init(Cipher.DECRYPT_MODE, keySpec, ivSpec);

        } catch (InvalidKeyException ex) {
            Logger.getLogger(Encrypter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(Encrypter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(Encrypter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(Encrypter.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public String encrypt(String str) {
        try {
            // Encode the string into bytes using utf-8
            byte[] utf8 = str.getBytes("UTF8");
            // Encrypt
            byte[] enc = ecipher.doFinal(utf8);

            // Encode bytes to base64 to get a string
            // return new sun.misc.BASE64Encoder().encode(enc);
            byte[] encodedBytes = Base64.encodeBase64URLSafe(enc);
            System.out.println("NEW COMMONS CODEC ENCRYPTED STRING IS :: " + new String(encodedBytes) + " AND STRING WAS ::  " + str);
            return new String(encodedBytes);

        } catch (BadPaddingException ex) {
            Logger.getLogger(Encrypter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (java.io.IOException e) {
        }
        return null;
    }

    public String decrypt(String str) {
        System.out.println("todecrypt==" + str);
        try {
            // Decode base64 to get bytes
         //   byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);
            byte[] decodedBytes = Base64.decodeBase64(str.getBytes("UTF8"));
            //System.out.println("bystestring==" + dec.toString());
            // Decrypt
           // byte[] utf8 = dcipher.doFinal(dec);
             byte[] utf8 = dcipher.doFinal(decodedBytes);
            //System.out.println("decrypted string==" + new String(utf8, "UTF8"));
            // Decode using utf-8
            //return new String(utf8, "UTF8");
            System.out.println("NEW COMMONS CODEC DECRYPTED STRING IS :: " + new String(utf8));
            return new String(utf8);

        } catch (BadPaddingException ex) {
            Logger.getLogger(Encrypter.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IllegalBlockSizeException e) {
        } catch (UnsupportedEncodingException e) {
        } catch (java.io.IOException e) {
        }
        return null;
    }
}