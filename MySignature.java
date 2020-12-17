/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mrsystem;

import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.KeyFactory;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;


/**
 *
 * @author niata
 */
public class MySignature {
    private Signature signature;
    
    //algorithm
    public static final String ALGORITHM = "RSA";
    
    private String PUBLICKEY_FILE ;
    private String PRIVATEKEY_FILE ;
    private Doctor doc;
    
    //connstructor
    public MySignature(){
        try{
            signature = signature.getInstance("SHA256withRSA");
        
        }catch(Exception e){
            e.printStackTrace();
        
        }
    }
    
    public void setPublicKeyFile(String PUBLICKEY_FILE){
        this.PUBLICKEY_FILE = PUBLICKEY_FILE;          
    }
    
    public void setPrivateKeyFile(String PUBLICKEY_FILE){
        this.PRIVATEKEY_FILE = PUBLICKEY_FILE;          
    }
    
    
    //sign
    public String sign(String data) throws Exception{
        signature.initSign(getPrivateKey(PRIVATEKEY_FILE));
        signature.update(data.getBytes());
        return Base64.getEncoder().encodeToString(signature.sign());
    }
    
    //verify
    public boolean verify(String data, String ds) throws Exception{
        signature.initVerify(getPublicKey(PUBLICKEY_FILE));
        signature.update(data.getBytes());
        return signature.verify(Base64.getDecoder().decode(ds));
        
    
    }
    
    //acess publickey
    public static PublicKey getPublicKey(String path) throws Exception{
        byte[] keyBytes = Files.readAllBytes(Paths.get(path));
        X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
         
        return KeyFactory.getInstance(ALGORITHM).generatePublic(spec);
        
    }
    public static PrivateKey getPrivateKey(String path) throws Exception{
        byte[] keyBytes = Files.readAllBytes(Paths.get(path));
        PKCS8EncodedKeySpec spec = new PKCS8EncodedKeySpec(keyBytes);
        
        return KeyFactory.getInstance(ALGORITHM).generatePrivate(spec);
    }

 
    
}
