public class TerminalNonTerminal {
    // Reserved words (1-11)
    public static final int PROG = 1;             // program
    public static final int BEGIN = 2;            // begin
    public static final int END = 3;              // end
    public static final int INT_DECL = 4;         // int
    public static final int IF = 5;               // if
    public static final int THEN = 6;             // then
    public static final int ELSE = 7;             // else
    public static final int WHILE = 8;            // while
    public static final int LOOP = 9;             // loop
    public static final int INPUT = 10;           // read
    public static final int OUTPUT = 11;          // write

    // Special symbols (12-30)
    public static final int SEMICOLON = 12;       // ";"
    public static final int COMMA = 13;           // ","
    public static final int ASSIGN_OP = 14;       // "="
    public static final int NOT = 15;             // "!"
    public static final int LBRACKET = 16;        // "["
    public static final int RBRACKET = 17;        // "]"
    public static final int AND = 18;             // "&&"
    public static final int OR = 19;              // "||"
    public static final int LPAREN = 20;          // "("
    public static final int RPAREN = 21;          // ")"
    public static final int PLUS = 22;            // "+"
    public static final int MINUS = 23;           // "-"
    public static final int TIMES = 24;           // "*"
    public static final int NOT_EQUAL = 25;       // "!="
    public static final int EQUAL = 26;           // "=="
    public static final int LESS_THAN = 27;       // "<"
    public static final int GREATER_THAN = 28;    // ">"
    public static final int LESS_THAN_EQUAL = 29; // "<="
    public static final int GREATER_THAN_EQUAL = 30; // ">="

    // Other tokens
    public static final int CONST = 31;           // Integer constants
    public static final int ID = 32;              // Identifiers
    public static final int EOF = 33;             // End of file
    public static final int ERROR = 34;           // Error token

    // Non-terminals (35 and up)
    public static final int DECL_SEQ = 35;        // <decl seq>
    public static final int STMT_SEQ = 36;        // <stmt seq>
    public static final int DECL = 37;            // <decl>
    public static final int ID_LIST = 38;         // <id list>
    public static final int STMT = 39;            // <stmt>
    public static final int ASSIGN = 40;          // <assign>
    public static final int COND = 41;            // <cond>
    public static final int COMP = 42;            // <comp>
    public static final int EXP = 43;             // <exp>
    public static final int FAC = 44;             // <fac>
    public static final int OP = 45;              // <op>
    public static final int COMP_OP = 46;         // <comp op>
    public static final int ID_TERM = 47;         //
    }