import java.util.*;
import edu.duke.*;
import java.io.*;

public class VigenereBreaker {
    public String sliceString(String message, int whichSlice, int totalSlices) {
        //a String message, representing the encrypted message, an integer whichSlice, indicating 
        //the index the slice should start from, and an integer totalSlices, indicating the length of 
        //the key. 
        //This method returns a String consisting of every totalSlices-th character from message, 
        //starting at the whichSlice-th character.
        
        // slicedString will be the final string returned, intialized with ""
        // iterate throught letters in message, every "totalSlices" number of letter, starting from "whichSlice"
            //get the letter at iterated position from message, add it to "slicedString"
        // slicedString is the answer
        
        String slicedString = "";
        for (int dex=whichSlice; dex<message.length(); dex+=totalSlices) {
            slicedString += message.charAt(dex);
        }
        return slicedString;
    }
    //This returns an Array of keys for each slice for one keylength:
    public int[] tryKeyLength(String encrypted, int klength, char mostCommon) {
        int[] key = new int[klength];
        // This method make use of the CaesarCracker class, as well as the sliceString method, 
        //to find the shift for each index in the key.
        //fill in the key (which is an array of integers) and return it.
        
        //klength will be a int like 4 or 5 (letters key, or slice)
        CaesarCracker cc = new CaesarCracker(mostCommon);
        for (int whichSlice=0; whichSlice<klength; whichSlice++) {
            String slicedString = sliceString(encrypted,whichSlice, klength);
            key[whichSlice] = cc.getKey(slicedString);
        }
        return key;
    }


    public HashSet<String> readDictionary (FileResource fr) {
        //This method first make a new HashSet of Strings, 
        //then read each line in fr 
        //(which should contain exactly one word per line), 
        //convert that line to lowercase, 
        //and put that line into the HashSet that created. The method then return the 
        //HashSet representing the words in a dictionary. All the dictionary files, including the 
        //English dictionary file, are included in the starter program you download. They are 
        //inside the folder called ‘dictionaries’.
        
        HashSet<String> hsDict = new HashSet<String>();
        for (String line : fr.lines() ) {
            line = line.toLowerCase();
            hsDict.add(line);
        }
        return hsDict;
    }
    
    public int countWords(String message, HashSet<String> dictionary) {
        //This method split the message into words (use .split(“\\W+”), 
        //which returns a String array), 
        //iterate over those words, and see how many of them are “real words”—that is, 
        //how many appear in the dictionary. Recall that the words in dictionary are lowercase. 
        //This method return the integer count of how many valid words it found.
        
        String[] words = message.split("\\W+");
        int wordCount = 0;
        for (String word : words) {
            if (dictionary.contains(word.toLowerCase()) ) {
                wordCount += 1;
            }
        }
        return wordCount;
    }
    
    public String breakForLanguage( String encrypted,HashSet<String> dictionary) {
        //Call mostCommonCharIn method to find the most common character in the language, 
        //and pass that to tryKeyLength instead of ‘e’.

        //-Call mostCommonCharIn method, get languageLabel, convert to char
        //-Iterate all key lengths from 1 to 100 
            //Call tryKeyLength method to get Array of keys (encrypted, klength, languageLabel)
            //Call object VigenereCipher decrypt method to decrypt the message  
            //Call countWords method to see how many words recognizable 
            //if wordCount greater than recorded maxWords
                //update maxWords record
                //update "decrypted" to hold current decrypted message
                //clear "keys" in record
                //update "keys" with current keys
                //update "valideWords" with current wordCount
        //Iterate elements in ArrayList "int[]keys"
            //Iterate integer numbers (key) in int[]
                //print current number (key)
        //Split all the words from complete message, ready to count its number
        //print "valideWords" and the count of all words
        //and return that String decryption.
       
        int maxWords = 0;
        String decrypted = "";
        ArrayList<int[]> keys = new ArrayList<int[]> ();
        //int validWords = 0;
        char languageLabel = mostCommonCharIn (dictionary);
        for (int klength=1; klength<101; klength++) {
            int[] keyTemp = tryKeyLength(encrypted, klength, languageLabel);
            VigenereCipher vc = new VigenereCipher(keyTemp);
            String messageTry = vc.decrypt(encrypted); 
            int wordCount = countWords(messageTry, dictionary);
            if (maxWords < wordCount) {
                maxWords = wordCount;
                decrypted = messageTry;
                keys.clear();
                keys.add( keyTemp);
                //validWords = wordCount;
            }
        }
                
        for (int[] key : keys ) {
            for (int i=0; i<key.length; i++) {
                System.out.print("key"+i + ": " + key[i] +"\t");
            }
        }           
        String[] words = encrypted.split("\\W+");
        System.out.println("This file contains "+maxWords+" valid words out of "+words.length);
        return decrypted;
    }
    public char mostCommonCharIn(HashSet<String> dictionary) {
        //This method find out which character, of the letters in the English alphabet, 
        //appears most often in the words in dictionary. It return this most commonly 
        //occurring character. can iterate over a HashSet of Strings with a 
        //for-each style for loop.
        
        //Build a HashMap<String, Integer> letterVsCounts
        //              < letter, count > 
        //int countMax = 0;
        //char mostOften = '';
        //-Iterate each word in HashSet key 
            //-Iterate each char in current word
                //-If currnt char not in key of HashMap letterVsCounts
                    //count = 1
                //-Else If current char in HashMap letterVsCounts
                    //get count at current char
                    //Add 1 to this count
                //-Put (current char, count) into HashMap letterVsCounts
                //-if count > countMax
                    //countMax = count
                    //mostOften = current char
        //mostOften is the answer
        
        HashMap<String,Integer> letterVsCounts = new  HashMap<String,Integer>();
        int countMax = 0;
        int count = 0;
        char mostOften = ' ';
        for (String word : dictionary) {
            for (char c : word.toCharArray()) {
                if ( ! letterVsCounts.containsKey(Character.toString(c))) {
                    //System.out.println("HashMap contains NO key " + c);
                    count = 1;
                }
                else {
                    count = letterVsCounts.get(Character.toString(c)) + 1;
                }
                letterVsCounts.put(Character.toString(c), count);
                if (count > countMax) {
                    countMax = count;
                    mostOften = c;
                }
            }
        }
        System.out.println("countMax " + countMax);
        return mostOften;
    }
    
    public String breakForAllLangs(String encrypted, HashMap<String, HashSet<String>> languages){
        //HashMap- languages, mapping a String representing the name of a language 
        //to a HashSet of Strings containing the words in that language.
        //use the breakForLanguage and countWords methods 
        //print out the decrypted message as well as the language that you identified for the message.
        //return decrypted message
        
        //HashMap<String,   HashSet<String>> languages
        //       <langName, dictOfLang     >
        
        //-Iterate over each key(langName) of HashMap
            //-Extract HashSet dict at current key, give it to HashSet "dictionary"
            
            //-Call breakForLanguage method, (take encrypted, HashSet)
            //-------------xxxxxxxxxxxxxx------------------Call tryKeyLength method (take char languageLabel)
            //-------------xxxxxxxxxxxxxx------------------Call object vc.decrypt
            //-Call countWords
            //-Get validWordCount
            //-Track/update highest validWordCount
            //-Track/update language label at this highest validWordCount
            //-Track/update decrypted message at this highest validWordCount
        //decrypted is the answer

        int wordCountMax = 0;
        String validLanguage = "";
        String decrypted = "";
        for (String languageName: languages.keySet()) {
            System.out.println("\n\nFor language "+languageName+": --------------------------------" );
            HashSet<String> dictionary = languages.get(languageName);
            
            String decryptedTry = breakForLanguage( encrypted, dictionary);  
            int wordCount = countWords(decryptedTry, dictionary);
            if (wordCount > wordCountMax) {
                wordCountMax = wordCount;
                validLanguage = languageName;
                decrypted = decryptedTry;
            }
        }
        String[] words = encrypted.split("\\W+");
        System.out.println("\n\n==============================================");
        System.out.println("The best out of all of above, this file contains "
                                +wordCountMax+" valid words out of "+words.length);
        System.out.println("Valid language is: "+ validLanguage);       
        System.out.println();
        return decrypted;
    }
    
    public void breakVigenere () {
        //Build HashMap< String ,HashSet<String>> languages
              //       <langName,   dictWords   >
        //--Create a new FileResource using its default constructor 
        //--read the file message into a String.
        //--make new DirectoryResource to read multiple dictionary files. 
        //--call readDictionary method to read dictionary data, store into HashMap
        //---------------add some print statements to reassure that program is making progress    
        //--Call breakForAllLangs method to decrypt the 
        //print out the decrypted message!
        
        HashMap< String ,HashSet<String>> languages = new HashMap< String ,HashSet<String>>();
        FileResource fr = new FileResource();
        String encrypted = fr.asString();
        DirectoryResource dr = new DirectoryResource();
        for (File f : dr.selectedFiles() ) {
            String languageName = f.getName();
            System.out.println("loading data from file " + languageName);
            FileResource fr2 = new FileResource(f);
            System.out.println("loading dictionary HashSet from file " + languageName);
            HashSet<String> dict = readDictionary (fr2);     
            System.out.println("Building HashMap from file " + languageName);
            languages.put(languageName, dict);
            System.out.println("Completed building HashMap from file " + languageName );
        }
        String message = breakForAllLangs( encrypted,languages);
        System.out.println(message.substring(0));        
        
        //System.out.println("Most frequent letter in this language: "+ mostCommonCharIn(dict));
        
        // int[] key = tryKeyLength(encrypted, 4, 'e');
        // VigenereCipher vc = new VigenereCipher(key);
        // vc.decrypt(encrypted);
        // System.out.println("vc.decrypt (encrypted): " + vc.decrypt (encrypted)); 
        

        //print out the key
        // for (int i =0; i< key.length; i++) {
            // System.out.println("key"+i + ": " + key[i]);
        // }
    }
        
    
    
    
    
    
    
    
    
    
    public HashSet<String> readDictionaryObsolete (FileResource fr) {
        //This method first make a new HashSet of Strings, 
        //then read each line in fr 
        //(which contain exactly one word per line), 
        //convert that line to lowercase, 
        //and put that line into the HashSet that created. The method then return the 
        //HashSet representing the words in a dictionary. All the dictionary files, including the 
        //English dictionary file, are included in the starter program download. They are 
        //inside the folder called ‘dictionaries’.
        
        HashSet<String> hsDict = new HashSet<String>();
        for (String line : fr.lines() ) {
            line = line.toLowerCase();
            hsDict.add(line);
        }
        return hsDict;
    }    
    
    public String breakForLanguageObsolete( String encrypted,HashSet<String> dictionary) {
        //-Iterate all key lengths from 1 to 100 
            //Call tryKeyLength method to get Array of keys
            //Call object VigenereCipher decrypt method to decrypt the message  
            //Call countWords method to see how many words recognizable 
            //if wordCount greater than recorded maxWords
                //update maxWords record
                //update "decrypted" to hold current decrypted message
                //clear "keys" in record
                //update "keys" with current keys
                //update "valideWords" with current wordCount
            //Iterate elements in ArrayList "int[]keys"
                //Iterate integer numbers (key) in int[]
                    //print current number (key)
            //Split all the words from complete message, ready to count its number
            //print "valideWords" and the count of all words
            //and return that String decryption.
        int maxWords = 0;
        String decrypted = "";
        ArrayList<int[]> keys = new ArrayList<int[]> ();
        int validWords = 0;
        for (int klength=1; klength<101; klength++) {
            int[] keyTemp = tryKeyLength(encrypted, klength, 'e');
            VigenereCipher vc = new VigenereCipher(keyTemp);
            String messageTry = vc.decrypt(encrypted); 
            int wordCount = countWords(messageTry, dictionary);
            if (maxWords < wordCount) {
                maxWords = wordCount;
                decrypted = messageTry;
                keys.clear();
                keys.add( keyTemp);
                validWords = wordCount;
            }
        }
                
        for (int[] key : keys ) {
            for (int i=0; i<key.length; i++) {
                System.out.println("key"+i + ": " + key[i]);
            }
        }           
        String[] words = encrypted.split("\\W+");
        System.out.println("This file contains "+validWords+" valid words out of "+words.length);
        return decrypted;
    }    

    public void breakVigenereObsolete () {
        //--Create a new FileResource using its default constructor 
        //--read the file message into a String.
        //--make a new FileResource to read from the English dictionary file 
        //--call readDictionary method to read dictionary data 
        //--Call breakForLanguage method to decrypt the 
        //print out the decrypted message!
        
        FileResource fr = new FileResource();
        String encrypted = fr.asString();
        FileResource fr2 = new FileResource("dictionaries/English");
        //call method to read dictionary data
        HashSet<String> dict = readDictionary (fr2);
        String message = breakForLanguage( encrypted,dict);
        System.out.println(message.substring(0, 5000));
        
        // int[] key = tryKeyLength(encrypted, 4, 'e');
        // VigenereCipher vc = new VigenereCipher(key);
        // vc.decrypt(encrypted);
        // System.out.println("vc.decrypt (encrypted): " + vc.decrypt (encrypted)); 
        

        //print out the key
        // for (int i =0; i< key.length; i++) {
            // System.out.println("key"+i + ": " + key[i]);
        // }
    }    
}
