/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mrsystem;

import java.security.*;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;

public class AsymmtricCrypto {
    
    //algorithm
    public static final String ALGORITHM = "RSA";
    
    private Cipher cipher;
            
    public AsymmtricCrypto() throws Exception {
        this.cipher = Cipher.getInstance(ALGORITHM);
        
    }
    
    //encrypt
    public String encrypt (String data, PublicKey key) throws Exception{
        cipher.init(Cipher.ENCRYPT_MODE, key);
        byte[] cipherBytes = cipher.doFinal(data.getBytes()); //encrypted text
        return Base64.getEncoder().encodeToString(cipherBytes);
    }
    
    //decrypt
    public String decrypt(String ciphertext, PrivateKey key) throws Exception{
        cipher.init(Cipher.DECRYPT_MODE, key);
        byte[] cipherBytes = Base64.getDecoder().decode(ciphertext);
        byte[] originalBytes = cipher.doFinal(cipherBytes);
        return new String (originalBytes);
        
    }
    
    
}