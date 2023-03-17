import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Spelling Bee
 *
 * This program accepts an input of letters. It prints to an output file
 * all English words that can be generated from those letters.
 *
 * For example: if the user inputs the letters "doggo" the program will generate:
 * do
 * dog
 * doggo
 * go
 * god
 * gog
 * gogo
 * goo
 * good
 *
 * It utilizes recursion to generate the strings, mergesort to sort them, and
 * binary search to find them in a dictionary.
 *
 * @author Zach Blick, [Eric Feng]
 *
 * Written on March 5, 2023 for CS2 @ Menlo School
 *
 * DO NOT MODIFY MAIN OR ANY OF THE METHOD HEADERS.
 *
 */
public class SpellingBee {

    private String letters;
    private ArrayList<String> words;
    public static final int DICTIONARY_SIZE = 143091;
    public static final String[] DICTIONARY = new String[DICTIONARY_SIZE];

    public SpellingBee(String letters) {
        this.letters = letters;
        words = new ArrayList<String>();
    }

    // Generates all possible words and adds them to words
    public void generate() {
        makeWords(letters, "");
    }

    public void makeWords(String makeLetters, String usedLetters){
        if (makeLetters.isEmpty())
        {
            return;
        }
        for (int i = 0; i < makeLetters.length(); i++){
            words.add(usedLetters);
            makeWords(makeLetters.substring(0, i) + makeLetters.substring(i+1),
                    usedLetters + makeLetters.substring(i, i+1));
        }
    }



    //Sorts words by calling mergesort
    public void sort() {
        // YOUR CODE HERE
        String[] merged = mergeSort(0, words.size()-1);
        for (int i = 0; i < words.size(); i++)
        {
            words.set(i, merged[i]);
        }
    }

    //Typical mergesort
    public String[] mergeSort(int low, int high){
        if (low == high){
            String[] temp = new String[1];
            temp[0] = words.get(low);
            return temp;
        }

        int mid = (low + high) / 2;
        String[] a = mergeSort(low, mid);
        String[] b = mergeSort(mid + 1, high);

        return merge(a,b);
    }

    public String[] merge(String[] i, String[] j){
        String[] sorted = new String[i.length + j.length];
        int a = 0;
        int b = 0;
        int c = 0;

        while (a < i.length && b < j.length){
            if((i[a].compareTo(j[b])) < 0){
                sorted[c] = i[a];
                a++;
            }
            else
            {
                sorted[c] = j[b];
                b++;
            }
            c++;
        }

        if(a == i.length){
            while (b < j.length){
                sorted[c] = j[b];
                b++;
                c++;
            }
        }

        if(b == j.length){
            while (a < i.length){
                sorted[c] = i[a];
                a++;
                c++;
            }
        }
        return sorted;
    }

    // Removes duplicates from the sorted list.
    public void removeDuplicates() {
        int i = 0;
        while (i < words.size() - 1) {
            String word = words.get(i);
            if (word.equals(words.get(i + 1)))
                words.remove(i + 1);
            else
                i++;
        }
    }

    // Checks if each of the words in words are in dictionary
    public void checkWords() {
        // YOUR CODE HERE
        for (int i = 0; i < words.size(); i++)
        {
            if(!search(words.get(i))) {
                words.remove(i);
                i--;
            }

        }

    }

    //Checks if a string is in the dictionary through binary search
    public boolean search(String target){
        return searchHelp(target, 0, DICTIONARY_SIZE);
    }

    public boolean searchHelp(String target, int low, int high){
        int mid = (low + high) / 2;

        if (DICTIONARY[mid].equals(target))
            return true;
        if (high <= low)
        {
            return false;
        }
        else if (DICTIONARY[mid].compareTo(target) > 0)
            return searchHelp(target, low, mid - 1);
        else
            return searchHelp(target, mid + 1, high);

    }

    // Prints all valid words to wordList.txt
    public void printWords() throws IOException {
        File wordFile = new File("Resources/wordList.txt");
        BufferedWriter writer = new BufferedWriter(new FileWriter(wordFile, false));
        for (String word : words) {
            writer.append(word);
            writer.newLine();
        }
        writer.close();
    }

    public ArrayList<String> getWords() {
        return words;
    }

    public void setWords(ArrayList<String> words) {
        this.words = words;
    }

    public SpellingBee getBee() {
        return this;
    }

    public static void loadDictionary() {
        Scanner s;
        File dictionaryFile = new File("Resources/dictionary.txt");
        try {
            s = new Scanner(dictionaryFile);
        } catch (FileNotFoundException e) {
            System.out.println("Could not open dictionary file.");
            return;
        }
        int i = 0;
        while(s.hasNextLine()) {
            DICTIONARY[i++] = s.nextLine();
        }
    }

    public static void main(String[] args) {

        // Prompt for letters until given only letters.
        Scanner s = new Scanner(System.in);
        String letters;
        do {
            System.out.print("Enter your letters: ");
            letters = s.nextLine();
        }
        while (!letters.matches("[a-zA-Z]+"));

        // Load the dictionary
        SpellingBee.loadDictionary();

        // Generate and print all valid words from those letters.
        SpellingBee sb = new SpellingBee(letters);
        sb.generate();
        sb.sort();
        sb.removeDuplicates();
        sb.checkWords();
        try {
            sb.printWords();
        } catch (IOException e) {
            System.out.println("Could not write to output file.");
        }
        s.close();
    }
}
