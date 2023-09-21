package hashTable;

import java.util.Random;

public class GenerateRandomString {
    public static String genRandStr() {
        String preset = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz1234567890";
        Random rand=new Random();
        StringBuilder strBld=new StringBuilder();
        for (int i = 0; i < 10; i++) {
            int index=rand.nextInt(preset.length());
            strBld.append(preset.charAt(index));
        }
        return strBld.toString();
    }
}
