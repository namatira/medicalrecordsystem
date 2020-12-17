/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mrsystem;

/**
 *
 * @author niata
 */
import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.security.*;
import javax.crypto.*;

/**
 *
 * @author niata
 */
public class KeyMaker {
    
    KeyPairGenerator Keygen;
    KeyPair keypair;
    private final int keysize = 1024;
    public final String PUBLICKEY_FILE;
    public final String PRIVATEkEY_FILE;
    //algorithm
    public static final String ALGORITHM = "RSA";
    
    
    public KeyMaker(String PUBLICKEY_FILE,String PRIVATEkEY_FILE) throws Exception{
        this.PUBLICKEY_FILE = PUBLICKEY_FILE;
        this.PRIVATEkEY_FILE = PRIVATEkEY_FILE;
        this.Keygen = KeyPairGenerator.getInstance( ALGORITHM);
        this.Keygen.initialize(keysize);
    }
    
    public static void mkKeypair(String PUBLICKEY_FILE,String PRIVATEkEY_FILE){
        try{
            //generate keypair
            KeyMaker km = new KeyMaker(PUBLICKEY_FILE,PRIVATEkEY_FILE);
            km.keypair = km.Keygen.generateKeyPair();
            //public key
            PublicKey pukey = km.keypair.getPublic();
            
            //pivatekey
            PrivateKey pvkey = km.keypair.getPrivate();
            
            //show keys- if u wanna see them
            
            //store keypair
            store( PUBLICKEY_FILE, pukey.getEncoded());
            store( PRIVATEkEY_FILE, pvkey.getEncoded());
            
        }catch(Exception e){
            e.printStackTrace();
        }
                  
    }
    
    //store keypair 
    private static void store ( String path, byte[] key){
        File file = new File(path);
        file.getParentFile().mkdirs();
        try{
            Files.write(Paths.get(path), key, StandardOpenOption.CREATE);
        
        }catch(Exception e){
            e.printStackTrace();
        }
        
        
    }

}
