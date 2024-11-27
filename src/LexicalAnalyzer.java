import java.io.*;

public class LexicalAnalyzer {
    /* Global declarations */
    static int charClass;
    static String lexeme = "";
    static char nextChar;
    static int lexLen;
    static int token;
    static int nextToken;
    static BufferedReader inFp;

    static final int LETTER = 0;
    static final int DIGIT = 1;
    static final int UNKNOWN = 99;

    static final int INT_LIT = 10;
    static final int IDENT = 11;
    static final int ASSIGN_OP = 20;
    static final int ADD_OP = 21;
    static final int SUB_OP = 22;
    static final int MULT_OP = 23;
    static final int DIV_OP = 24;
    static final int LEFT_PAREN = 25;
    static final int RIGHT_PAREN = 26;

    public static void main(String[] args) {
        try {
            inFp = new BufferedReader(new FileReader("front.txt"));
            getChar();
            do {
                lex();
            } while (nextToken != -1); // EOF in Java is represented by -1
        } catch (IOException e) {
            System.out.println("ERROR - cannot open front.in");
        }
    }

    static int lookup(char ch) {
        switch (ch) {
            case '(':
                addChar();
                nextToken = LEFT_PAREN;
                break;
            case ')':
                addChar();
                nextToken = RIGHT_PAREN;
                break;
            case '+':
                addChar();
                nextToken = ADD_OP;
                break;
            case '-':
                addChar();
                nextToken = SUB_OP;
                break;
            case '*':
                addChar();
                nextToken = MULT_OP;
                break;
            case '/':
                addChar();
                nextToken = DIV_OP;
                break;
            case '=':
                addChar();
                nextToken = ASSIGN_OP;
                break;
            default:
                addChar();
                nextToken = -1; // EOF or unknown
                break;
        }
        return nextToken;
    }

    static void getChar() {
        try {
            int charInt = inFp.read();
            if (charInt != -1) {
                nextChar = (char) charInt;
                if (Character.isLetter(nextChar)) {
                    charClass = LETTER;
                } else if (Character.isDigit(nextChar)) {
                    charClass = DIGIT;
                } else {
                    charClass = UNKNOWN; // Handle operators and other characters
                }
            } else {
                charClass = -1; // EOF
            }
        } catch (IOException e) {
            charClass = -1; // EOF
        }
    }

    static void addChar() {
        if (lexLen <= 98) {
            lexeme += nextChar;
            lexLen++;
        } else {
            System.out.println("Error - lexeme is too long");
        }
    }

    static void getNonBlank() {
        while (Character.isWhitespace(nextChar))
            getChar();
    }

    static int lex() {
        lexLen = 0;
        lexeme = "";
        getNonBlank();
        switch (charClass) {
            case LETTER:
                addChar();
                getChar();
                while (charClass == LETTER || charClass == DIGIT) {
                    addChar();
                    getChar();
                }
                nextToken = IDENT;
                break;
            case DIGIT:
                addChar();
                getChar();
                while (charClass == DIGIT) {
                    addChar();
                    getChar();
                }
                nextToken = INT_LIT;
                break;
            case UNKNOWN:
                lookup(nextChar);
                getChar();
                break;
            case -1:
                nextToken = -1; // EOF
                lexeme = "EOF";
                break;
        }
        System.out.printf("Next token is: %d, Next lexeme is: %s%n", nextToken, lexeme);
        return nextToken;
    }
}
