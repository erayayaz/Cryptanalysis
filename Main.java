/*
 * Eray Ayaz - 150116053
 *
 */

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) throws Exception, FileNotFoundException {
        FileReader file = new FileReader("C:\\Users\\eray7\\Desktop\\Discrete\\Proje\\inputfile1.txt");
        FileReader file2 = new FileReader("C:\\Users\\eray7\\Desktop\\Discrete\\Proje\\inputfile2.txt");
        FileReader dictionaryFile = new FileReader("C:\\Users\\eray7\\Desktop\\Discrete\\Proje\\dictionary.txt");

        ArrayList<String> input1text = new ArrayList<String>();
        ArrayList<String> input2text = new ArrayList<String>();
        ArrayList<String> dictionaryWord = new ArrayList<String>();


        Scanner input = new Scanner(file);
        Scanner input2 = new Scanner(file2);
        Scanner dictionaryİnput = new Scanner(dictionaryFile);


        PrintWriter encrypt = new PrintWriter("encrypt.txt", "UTF-8");
        PrintWriter encrypt2 = new PrintWriter("encrypt2.txt", "UTF-8");

        while (input.hasNext()) {
            String word  = input.next();
            input1text.add(word);
        }

        while (input2.hasNext()) {
            String input2words  = input2.next();
            input2text.add(input2words);
        }
        while (dictionaryİnput.hasNext()) {
            String dictionary  = dictionaryİnput.next();
            dictionaryWord.add(dictionary);
        }

        int i,x;
        int firstKey = randomWithRange(1,26);
        int secondKey = randomWithRange2(1,26);

        ArrayList<String> encryptVocab = new ArrayList<String>();
        ArrayList<String> encrypt2Vocab = new ArrayList<String>();

        //Encryption part
        for(i=0;i<input1text.size();i++){
            encryptVocab.add(encrypt(input1text.get(i),firstKey,secondKey));
            encrypt.print(encryptVocab.get(i));
            encrypt.print(" ");
        }
        for(x=0;x<input2text.size();x++){
             encrypt2Vocab.add(encrypt(input2text.get(x),firstKey,secondKey));
             encrypt2.print(encrypt2Vocab.get(x));
             encrypt2.print(" ");
        }
        bruteForce(encryptVocab,dictionaryWord);

        encrypt.close();
        encrypt2.close();

    }
    //this method find the great common divisor
    private static int gcd(int a, int b) {
        int t;
        while(b != 0){
            t = a;
            a = b;
            b = t%b;
        }
        return a;
    }
    //this method check if given number relatively prime with 26
    private static boolean relativelyPrime(int a, int b) {
        return gcd(a,b) == 1;
    }
    //this method return the random numbers between 0-25 but this number relatively prime with 26
    static int randomWithRange(int min, int max)
    {
        int range = (max - min) + 1;
        int random =(int)(Math.random() * range) + min;
        if(relativelyPrime(random,26))
            return random;
        else{
            return randomWithRange(1,26);
        }
    }
    //this method return the random numbers between 0-25
    static int randomWithRange2(int min, int max)
    {
        int range = (max - min) + 1;
        return (int)(Math.random() * range) + min;
    }
    //this method decrypt the text with given numbers
    static String encrypt(String input,int firstKey,int secondKey) {
        StringBuilder builder = new StringBuilder();
        for(int y=0;y<input.length();y++){
            input.toLowerCase();
        }
        for (int in = 0; in < input.length(); in++) {
            char character = input.charAt(in);
            char empty=' ';
            if(Character.isLetter(character)){
                if (Character.isLowerCase(character)) {
                    character = (char) ((firstKey * (character - 'a') + secondKey) % 26 + 'a');
                }else if(Character.isUpperCase(character)){
                    character = (char) ((firstKey * (character - 'A') + secondKey) % 26 + 'A');
                }
            }else if(input.charAt(in)=='.' || input.charAt(in)==',' || input.charAt(in)=='?' || input.charAt(in)=='�'){
                character=empty;
            }
            builder.append(character);
        }
        return builder.toString();
    }
    //this method decrypt the text with given numbers
    public static String decrypt(String input,int firstKey,int secondKey) {
        StringBuilder builder = new StringBuilder();
        BigInteger inverse = BigInteger.valueOf(firstKey).modInverse(BigInteger.valueOf(26));
        for(int y=0;y<input.length();y++){
            input.toLowerCase();
        }
        for (int in = 0; in < input.length(); in++) {
            char character = input.charAt(in);
            if(Character.isLetter(character)){
                if (Character.isLowerCase(character)) {
                    int decoded = inverse.intValue() * (character - 'a' - secondKey + 26);
                    character = (char) (decoded % 26 + 'a');
                }else if(Character.isUpperCase(character)){
                    int decoded = inverse.intValue() * (character - 'A' - secondKey + 26);
                    character = (char) (decoded % 26 + 'A');
                }
            }
            builder.append(character);
        }
        return builder.toString();
    }
    //This method try to find encrypt numbers then decrypt the text
    public static void bruteForce(ArrayList<String> encryptWord,ArrayList<String> dictionaryWord) throws FileNotFoundException, UnsupportedEncodingException {
        int [] firstKey={1,3,5,7,9,11,15,17,19,21,23,25};
        int [] secondKey={0,1,2,3,4,5,6,7,8,9,10,11,12,13,14,15,16,17,18,19,20,21,22,23,24,25};

        PrintWriter decrypt = new PrintWriter("decrypt.txt", "UTF-8");
        ArrayList<String> text = new ArrayList<String>();

        int t,y,s;
        double counter;
        counter =0;

        for(t=0;t<firstKey.length;t++){
            for(y=0;y<secondKey.length;y++){
                for(String u:encryptWord){
                    String solvedWord = decrypt(u,firstKey[t],secondKey[y]);
                    for(s=0;s<dictionaryWord.size();s++){
                        if(solvedWord.toLowerCase().compareTo(dictionaryWord.get(s).toLowerCase())==0){
                            counter++;
                        }
                    }
                }
                if(counter > 300){
                    double lot=counter*100;
                    double percentage = lot / encryptWord.size();
                    System.out.printf("The percentage is: "+" %.2f",percentage);
                    for(int i=0;i<encryptWord.size();i++){
                        text.add(decrypt(encryptWord.get(i),firstKey[t],secondKey[y]));
                        decrypt.print(text.get(i));
                        decrypt.print(" ");
                    }
                    decrypt.close();
                }
                counter =0;
            }
        }
    }
}
