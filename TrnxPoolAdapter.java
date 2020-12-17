/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mrsystem;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;


public class TrnxPoolAdapter {

    
 public static List<Transaction> getTransactions(){
//        List<String> trnxLst = Transaction.getAll
        List<String> trnxLst = Transaction.readAll(Transaction.FILENAME);
        return trnxLst.stream()
                .map( record -> record.split("\\|") )
                //.filter( arr -> !arr[0].equals("") )
                .map(arr -> new Transaction( arr[0], arr[1], arr[2], arr[3], arr[4], arr[5],arr[6] ))
                .collect( Collectors.toList() );
    }
    
    public static List<List<String>> getTransactionsHashes(){
        List<Transaction> trnxPool = TrnxPoolAdapter.getTransactions();
        //generate hash value of each data in Transaction
            //collect using javatuple
        List<List<String>> hashLstAll = new ArrayList();
        for (Transaction trnx : trnxPool) {
            List<String> hashLst = new ArrayList();
            hashLst.add( HashingUtils.newhash(trnx.getPID(), "SHA-256") );
            hashLst.add( HashingUtils.newhash(trnx.getDgID_ProID(), "SHA-256") );
            hashLst.add( HashingUtils.newhash(trnx.getDID(), "SHA-256") );
            hashLst.add( HashingUtils.newhash(trnx.getPat_ICNo(), "SHA-256") );
            hashLst.add( HashingUtils.newhash(trnx.getDate(), "SHA-256") );
            hashLst.add( HashingUtils.newhash(trnx.getRes_type(), "SHA-256") );
            hashLst.add( HashingUtils.newhash(trnx.getDiagnosis_procedure(), "SHA-256") );
            hashLstAll.add(hashLst);
        }
        return hashLstAll;
    }
}