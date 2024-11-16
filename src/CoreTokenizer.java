import java.io.*;
import java.util.*;

public class CoreTokenizer {

    private BufferedReader reader;
    private List<Token> tokens;  // Stores the Token objects
    private int cursor;  // Cursor to track the current token index
    private Map<String, Integer> tokenMap;  // Combined map for reserved words and special symbols

    // Constructor to initialize the BufferedReader and tokenize the file
    public CoreTokenizer(String inputFileName) {
        try {
            reader = new BufferedReader(new FileReader(inputFileName));
            tokens = new ArrayList<>();
            cursor = 0;
            initializeMap();  // Initialize the token map
            tokenizeFile();  // Tokenizes the file
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void tokenizeFile() {
        try {
            String line = reader.readLine(); // The first read

            while (line != null) {
                line = line.trim();  // Remove leading and trailing whitespaces

                if (line.isEmpty()) {  // Skip empty lines
                    line = reader.readLine();  // Read the next line before continuing
                    continue;
                }

                // Process the line character by character
                StringBuilder currentToken = new StringBuilder();
                for (int i = 0; i < line.length(); i++) {
                    char ch = line.charAt(i);

                    // Check for two-character symbols
                    if (i < line.length() - 1) {  // Ensure there's a next character
                        String twoCharSymbol = "" + ch + line.charAt(i + 1);
                        if (tokenMap.containsKey(twoCharSymbol)) {
                            // Process any existing token first
                            if (currentToken.length() > 0) {
                                processToken(currentToken.toString());
                                currentToken.setLength(0);  // Clear the current token
                            }
                            // Add the two-character symbol as a token
                            processToken(twoCharSymbol);
                            i++;  // Skip the next character
                            continue;
                        }
                    }

                    // Check for whitespace
                    if (Character.isWhitespace(ch)) {
                        if (currentToken.length() > 0) {
                            processToken(currentToken.toString());
                            currentToken.setLength(0); // Clear the current token
                        }
                        continue; // Skip whitespace
                    }

                    // Check if the character is a special symbol
                    if (tokenMap.containsKey(String.valueOf(ch))) {
                        // Process the current token if it exists
                        if (currentToken.length() > 0) {
                            processToken(currentToken.toString());
                            currentToken.setLength(0); // Clear the current token
                        }
                        // Add the special symbol as a token
                        processToken(String.valueOf(ch));
                        continue; // Move to the next character
                    }

                    // Accumulate characters for reserved words, integers, or identifiers
                    currentToken.append(ch);
                }

                // Process any remaining token after the loop
                if (currentToken.length() > 0) {
                    processToken(currentToken.toString());
                }

                // Read the next line
                line = reader.readLine();
            }

            // Add EOF token at the end of tokenization
            tokens.add(new Token(33, "EOF"));

        } catch (IOException e) {
            System.out.println("Error reading from file: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public int getToken() {
        return tokens.get(cursor).getTokenType();
    }

    public Token getTokenType(){
        return  tokens.get(cursor);
    }

    // Method to skip to the next token
    public void skipToken() {
        if (cursor < tokens.size()) {
            cursor++;
        } else {
            tokenizeFile();
            cursor++;
        }
    }

    public static void main(String[] args) {
        if (args.length == 0) {
            System.out.println("Please provide an input file.");
            return;
        }

        CoreTokenizer tokenizer = new CoreTokenizer(args[0]);

        // Print Token Numbers Until EOF
        while (tokenizer.getToken() != 33) {
            System.out.println(tokenizer.getToken());
            tokenizer.skipToken();
        }
        System.out.println(33);
    }

    // Initialize the combined map for reserved words and special symbols
    private void initializeMap() {
        tokenMap = new HashMap<>();

        // Reserved words with their token numbers (1-11)
        tokenMap.put("program", 1);
        tokenMap.put("begin", 2);
        tokenMap.put("end", 3);
        tokenMap.put("int", 4);
        tokenMap.put("if", 5);
        tokenMap.put("then", 6);
        tokenMap.put("else", 7);
        tokenMap.put("while", 8);
        tokenMap.put("loop", 9);
        tokenMap.put("read", 10);
        tokenMap.put("write", 11);

        // Special symbols with their token numbers (12-30)
        tokenMap.put(";", 12);
        tokenMap.put(",", 13);
        tokenMap.put("=", 14);
        tokenMap.put("!", 15);
        tokenMap.put("[", 16);
        tokenMap.put("]", 17);
        tokenMap.put("&&", 18);
        tokenMap.put("||", 19);
        tokenMap.put("(", 20);
        tokenMap.put(")", 21);
        tokenMap.put("+", 22);
        tokenMap.put("-", 23);
        tokenMap.put("*", 24);
        tokenMap.put("!=", 25);
        tokenMap.put("==", 26);
        tokenMap.put("<", 27);
        tokenMap.put(">", 28);
        tokenMap.put("<=", 29);
        tokenMap.put(">=", 30);
    }

    // Method to check if a string represents an integer
    private boolean isInteger(String str) {
        for (char ch : str.toCharArray()) {
            if (!Character.isDigit(ch)) {
                return false;  // Not an integer
            }
        }
        return true;
    }

    private boolean isIdentifier(String str) {
        // Check if the string is empty or null (not a valid identifier)
        if (str == null || str.length() == 0) {
            return false;
        }

        // Check if the first character is an uppercase letter
        if (!Character.isUpperCase(str.charAt(0))) {
            return false;
        }

        // Loop through the remaining characters (if any)
        for (int i = 1; i < str.length(); i++) {
            char ch = str.charAt(i);

            // Check if each character is either an uppercase letter or a digit
            if (!Character.isUpperCase(ch) && !Character.isDigit(ch)) {
                return false;  // Invalid character for an identifier (e.g., lowercase letters)
            }
        }

        // If all checks passed, the string is a valid identifier
        return true;
    }

    // Method to get the integer value of the current token
    public int intVal() {
        return tokens.get(cursor).getVal();
    }

    // Method to get the identifier name of the current token
    public String idName() {
        return tokens.get(cursor).getIDName();
    }

    // Method to process a token after it has been fully constructed
    private void processToken(String token) {
        if (tokenMap.containsKey(token)) { // Check for reserved words or special symbols
            tokens.add(new Token(tokenMap.get(token), token));
        } else if (isInteger(token)) { // Check for integers
            tokens.add(new Token(31, token));
        } else if (isIdentifier(token)) { // Check for identifiers
            tokens.add(new Token(32, token));
        } else {
            System.out.println("Illegal Token: " + token);
        }
    }
}
