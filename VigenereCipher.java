import edu.duke.*;
import java.util.*;

public class VigenereCipher {
    CaesarCipher[] ciphers;  //the field ciphers, which is an array of CaesarCipher objects.
    
    public VigenereCipher(int[] key) {
        //Since ciphers is a array. 2 steps to define array. 1st define its length. 2nd define each element.
        ciphers = new CaesarCipher[key.length]; //array cipher will have same number of storage location as the length of key. Each slice will match an element in the cipher array.
        for (int i = 0; i < key.length; i++) { // iterate over each slice
            ciphers[i] = new CaesarCipher(key[i]); // build each CaesarCipher object with each key.
        }
    }
    //a method that encrypts the String passed in and returns the encrypted message:
    public String encrypt(String input) {
        //"input" will be a complete message, none sliced.
        StringBuilder answer = new StringBuilder();
        int i = 0;
        //Iterate over each char of input message. 
        for (char c : input.toCharArray()) {
            // create index: cipherIndex to keep track of slicing number, 
            int cipherIndex = i % ciphers.length; //0%5=0,1%5=1,2%5=2,3%5=3,4%5=4,5%5=0,6%5=1,7%5=2,8%5=3 9%5=4
            // create a CaesarCipher object: thisCipher, which has all encrypt features,
            // and choose ciphers' element based on slice number
            CaesarCipher thisCipher = ciphers[cipherIndex];
            // call method to encrypt current letter c, then append the encrypted letter to "answer"
            answer.append(thisCipher.encryptLetter(c));
            //move up tracker 1 step
            i++;
        }
        return answer.toString();
    }
    //a method that decrypts the String passed in and returns the decrypted message:
    public String decrypt(String input) {
        //"input" will be a complete message, none sliced.
        StringBuilder answer = new StringBuilder();
        int i = 0;
        //Iterate over each char of input message. 
        for (char c : input.toCharArray()) {  // each char in a char Array that is from String
            // create index: cipherIndex to keep track of slicing number, 
            int cipherIndex = i % ciphers.length;
            // create a CaesarCipher object: thisCipher, which has all encrypt or decrypt features,
            // and choose ciphers' element based on slice number
            CaesarCipher thisCipher = ciphers[cipherIndex];
            // call method to decrypt current letter c, then append the encrypted letter to "answer"
            answer.append(thisCipher.decryptLetter(c)); //This is the only different statement from encypt code.
            i++;
        }
        return answer.toString();
    }
    //returns a String representing the key for this cipher.
    public String toString() {
        return Arrays.toString(ciphers);   //Arrays.toString() is defined in Java Oracle
                    //"Returns a string representation of the contents of the specified array"
    }
    
}
