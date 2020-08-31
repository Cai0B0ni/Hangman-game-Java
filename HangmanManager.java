// Caio Boni
// 2/7/2019
// CSE 143 - TA: Yael Goldin
// Assignment #4

// Class to manage an altered version of Hangman. The program chooses a number 
//  of words based off of the length of the specified word, then as the user guesses
//  it will keep trying to alternate the word so that it matches the pattern but
//  at the same time makes it so that the hangman words is guessed as late as
//  possible

import java.util.*;

public class HangmanManager {

   private TreeSet<String> dictionaryWord;
   private TreeSet<Character> letterGuessed;
   private int guessesLeft;
   private String pattern;
   
   // Word length must be greater then or equal to 1 and max number of guessing 
   //  attempts is greater then or equal to 0 or else IllegalArgumentException is thrown.
   // If pre-conditions are met it then contructs the HangManager to start of the game
   //  given the dictionary of words the length of the word desired and the maximum
   //  guesses allowed. 
   public HangmanManager(Collection<String> dictionary, int length, int max){
      if(length < 1 || max < 0){
         throw new IllegalArgumentException("Words length less then 1 or max guess" +
                                             " is less then 0");
      }
      
      dictionaryWord = new TreeSet<String>();
      letterGuessed = new TreeSet<Character>(); 
      guessesLeft = max;
      pattern = "-";
      
      for(String word : dictionary){
         if(word.length() == length){
            dictionaryWord.add(word); 
         }
      }
      
     for(int i = 0; i < length - 1; i++){
         pattern += " -";
      }
   }
   
   // Returns possible set of words that can show up.
   public Set<String> words(){
      return dictionaryWord;
   }
   
   // Returns number of guesses left
   public int guessesLeft(){
      return guessesLeft;
   }
   
   // Returns the guesses the user has made so far
   public Set<Character> guesses(){
      return letterGuessed;
   }
   
   // Throws IllegalStateException if there are no words in which the program
   //  is considering. Otherwise is will return the pattern of the choosen word.
   public String pattern(){
      if(dictionaryWord.isEmpty()){
         throw new IllegalStateException("There is no word");
      } 
      return pattern; 
   }
   
   // Throws IllegalStateException if guesses left are less then 1 and size of word
   //  is zero. Throws IllegalArgumentException if letter has already been guessed.
   // After pre-conditions are met checks if word in it still has the same patterns
   //  but if possible one that does not contain the guessed character, thus making 
   //  it harder to win.
   public int record(char guess){
      if(guessesLeft < 1 || words().size() == 0){
         throw new IllegalStateException("Word size is 0 or no more guesses left");
      }
      if(letterGuessed.contains(guess)){
         throw new IllegalArgumentException("Letter already guessed");
      }
      
      letterGuessed.add(guess);
      Map<String, TreeSet<String>> wordGroup = new TreeMap<String, TreeSet<String>>();
      
      for(String words : dictionaryWord){
         wordPattern(words, guess);
         if(!wordGroup.containsKey(pattern)){
            wordGroup.put(pattern, new TreeSet<String>());
         }
         wordGroup.get(pattern).add(words);
      }
      return wordChoice(wordGroup, guess);
   }
   
   // Takes in a string "word" and char "guess" in order to create a new patter
   // that contains the previous and current guessed letters, with unguessed 
   // letters being represented as dashes.
   private void wordPattern(String word, char guesses){
      pattern = "";
      for(int i = 0; i < word.length(); i++){
         char specificChar = word.charAt(i);
         
         if(specificChar == guesses){
            pattern += guesses + " ";
         }else if(letterGuessed.contains(specificChar)){
            pattern += specificChar + " ";
         }else{
            pattern += "- ";
         }
      }
   }
   
   // Given a wordGroup and character guess the method chooses a new word that
   //  based on the the words from allt he pattern. Also if guessed letter has
   //  not been guessed yet it will lower the number of guesses left and returns
   //  the number of guesses left
   private int wordChoice (Map<String, TreeSet<String>> wordGroup, char guess){
      int numberOfGuesses = 0;
      int size = 0;
      String largestWord = "";
      
      for(String key : wordGroup.keySet()){
         if(wordGroup.get(key).size() > size){
            largestWord = key;
            size = wordGroup.get(key).size();
         }
      }

      dictionaryWord = wordGroup.get(largestWord);
      pattern = largestWord;      
      
      for(int i = 0; i < pattern.length(); i++){
         if(pattern.charAt(i) == guess){
            numberOfGuesses++;
         }
      }
      if(numberOfGuesses == 0){
         this.guessesLeft--;
      }
      return numberOfGuesses;
   }
}