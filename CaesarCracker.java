import edu.duke.*;

public class CaesarCracker {
    char mostCommon;
    
    public CaesarCracker() {
        mostCommon = 'e';
    }
    
    public CaesarCracker(char c) {
        mostCommon = c;
    }
    
    public int[] countLetters(String message){
        String alph = "abcdefghijklmnopqrstuvwxyz";
        int[] counts = new int[26]; //intialize for 26 storage locations, each with value 0. Represent the count at each char of 26 letters.
        for(int k=0; k < message.length(); k++){  //iterate over message, char by char
            int dex = alph.indexOf(Character.toLowerCase(message.charAt(k)));  // charAt() can be used to String, not only StringBuilder. See Oracle doc.
            if (dex != -1){   // if found char in alph
                counts[dex] += 1;  // add 1 count to the counts at dex position.
            }
        }
        return counts;
    }
    //Identify which letter has the max occurance
    public int maxIndex(int[] vals){
        int maxDex = 0;
        for(int k=0; k < vals.length; k++){
            if (vals[k] > vals[maxDex]){  // Smart!!!
                maxDex = k;
            }
        }
        return maxDex;
    }
    //getKey is actually get the offset position
    public int getKey(String encrypted){
        int[] freqs = countLetters(encrypted);
        int maxDex = maxIndex(freqs);  //identify the max index position of most frequently occurred letter
        int mostCommonPos = mostCommon - 'a';
        int dkey = maxDex - mostCommonPos;
        if (maxDex < mostCommonPos) {
            dkey = 26 - (mostCommonPos-maxDex);
        }
        return dkey;
    }
    
    public String decrypt(String encrypted){
        int key = getKey(encrypted);
        CaesarCipher cc = new CaesarCipher(key);
        return cc.decrypt(encrypted);
        
    }
   
}
