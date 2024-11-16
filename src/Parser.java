import java.nio.file.StandardOpenOption;

public class Parser {
    private CoreTokenizer tokenizer;
    private ParseTree parseTree;

    public Parser(CoreTokenizer tokenizer) {
        this.tokenizer = tokenizer;
        this.parseTree = new ParseTree();  // Initialize the parse tree
    }

    // Parse the full program structure <prog>
    public ParseTree parse() {
        parseTree.setNT(TerminalNonTerminal.PROG);
        parseTree.setAltNo(1);
        parseTree.createLeftBranch();
        parseTree.createRightBranch();

       if(tokenizer.getToken() !=TerminalNonTerminal.PROG){
           printErrorAndExit("Expected 'program'");
       }

       tokenizer.skipToken();
       parseTree.goDownLeftBranch();
       parseDeclSeq();
       parseTree.goUp();

       if(tokenizer.getToken() != TerminalNonTerminal.BEGIN){
           printErrorAndExit("Expected 'begin'");
       }
       tokenizer.skipToken();
       parseTree.goDownRightBranch();
       parseStmtSeq();
       parseTree.goUp();

       if(tokenizer.getToken() != TerminalNonTerminal.END){
           printErrorAndExit("Expected 'end'");
       }
       tokenizer.skipToken();

       return parseTree;
    }

    // Parse <decl-seq> ::= <decl> | <decl> <decl-seq>
    private void parseDeclSeq() {
        parseTree.setNT(TerminalNonTerminal.DECL_SEQ);
        parseTree.createLeftBranch();

        parseTree.goDownLeftBranch();
        parseDecl();
        parseTree.goUp();

        if(tokenizer.getToken() == TerminalNonTerminal.INT_DECL || tokenizer.getToken() != TerminalNonTerminal.BEGIN){
            parseTree.setAltNo(2);
            parseTree.createMiddleBranch();
            parseTree.goDownMiddleBranch();
            parseDeclSeq();
            parseTree.goUp();
        } else {
            parseTree.setAltNo(1);
        }
    }

    // Parse <decl> ::= int <id-list> ;
    private void parseDecl() {
        parseTree.setNT(TerminalNonTerminal.DECL);

        if (tokenizer.getToken() == TerminalNonTerminal.CONST) {  // CONST
            tokenizer.skipToken();  // Skip 'int'
            parseIdList();  // Parse id list
            if (tokenizer.getToken() == TerminalNonTerminal.SEMICOLON) {  // SEMICOLON
                tokenizer.skipToken();  // Skip ';'
            } else {
                printErrorAndExit("Expected ';' after declaration");
            }
        } else {
            printErrorAndExit("Expected 'int' for declaration");
        }
    }

    // Parse <id-list> ::= id | id , <id-list>
    private void parseIdList() {
        parseTree.setNT(TerminalNonTerminal.ID_LIST);
        parseTree.createLeftBranch();

        parseTree.goDownLeftBranch();
        parseId();
        parseTree.goUp();

        if(tokenizer.getToken() == TerminalNonTerminal.COMMA){
            tokenizer.skipToken();
            parseTree.setAltNo(2);
            parseTree.createMiddleBranch();
            parseTree.goDownMiddleBranch();
            parseIdList();
            parseTree.goUp();
        }else{
            parseTree.setAltNo(1);
        }
    }

    // Parse <stmt-seq> ::= <stmt> | <stmt> <stmt-seq>
    private void parseStmtSeq() {
        parseTree.setNT(TerminalNonTerminal.STMT_SEQ);
        parseTree.createLeftBranch();

        parseTree.goDownLeftBranch();
        parseStmt();
        parseTree.goUp();

        if(tokenizer.getToken() != TerminalNonTerminal.END) {
            parseTree.setAltNo(2);
            parseTree.createMiddleBranch();
            parseTree.goDownMiddleBranch();
            parseStmtSeq();
            parseTree.goUp();

        }else{
            parseTree.setAltNo(1);
        }
    }

    // Parse <stmt> ::= <assign> | <if> | <loop> | <in> | <out>
    private void parseStmt() {
        parseTree.setNT(TerminalNonTerminal.STMT);
        parseTree.createLeftBranch();

        switch (tokenizer.getToken()) {
            case TerminalNonTerminal.ID:  // ID
                parseTree.setAltNo(1);
                parseTree.goDownLeftBranch();
                parseAssign();
                break;
            case TerminalNonTerminal.IF:   // IF
                parseTree.setAltNo(2);
                parseTree.goDownLeftBranch();
                parseIf();
                break;
            case TerminalNonTerminal.WHILE:   // WHILE
                parseTree.setAltNo(3);
                parseTree.goDownLeftBranch();
                parseLoop();
                break;
            case TerminalNonTerminal.INPUT:  // INPUT
                parseTree.setAltNo(4);
                parseTree.goDownLeftBranch();
                parseIn();
                break;
            case TerminalNonTerminal.OUTPUT:  // OUTPUT
                parseTree.setAltNo(5);
                parseTree.goDownLeftBranch();
                parseOut();
                break;
            default:
                printErrorAndExit("Unexpected statement type: " + tokenizer.getToken());
        }

        parseTree.goUp();
    }

    // Parse <assign> ::= id := <expr> ;
    private void parseAssign() {
        parseTree.setNT(TerminalNonTerminal.ASSIGN);
        parseTree.setAltNo(1);
        parseTree.createLeftBranch();


        parseTree.goDownLeftBranch();
        parseId();
        parseTree.goUp();

        if(tokenizer.getToken() != TerminalNonTerminal.ASSIGN_OP){
            printErrorAndExit("Expected '='");
        }

        tokenizer.skipToken();
        parseTree.createMiddleBranch();
        parseTree.goDownMiddleBranch();
        parseExp();
        parseTree.goUp();

        if(tokenizer.getToken() != TerminalNonTerminal.SEMICOLON){
            printErrorAndExit("Expected ';'");
        }
        tokenizer.skipToken();
    }

    // Parse <expr> ::= <term> | <term> + <expr> | <term> - <expr>
    private void parseExp() {
        parseTree.setNT(TerminalNonTerminal.EXP);
        parseTree.createLeftBranch();

        parseTree.goDownLeftBranch();
        parseFactor();
        parseTree.goUp();

        if(tokenizer.getToken() == TerminalNonTerminal.PLUS){
            parseTree.setAltNo(2);
            tokenizer.skipToken();
            parseTree.createMiddleBranch();
            parseTree.goDownMiddleBranch();
            parseExp();
            parseTree.goUp();
        }else if(tokenizer.getToken() == TerminalNonTerminal.MINUS){
            parseTree.setAltNo(3);
            tokenizer.skipToken();
            parseTree.createRightBranch();
            parseTree.goDownRightBranch();
            parseExp();
            parseTree.goUp();
        }else{
            parseTree.setAltNo(1);
        }
    }

    private void parseIf(){
        parseTree.setNT(TerminalNonTerminal.IF);

        if(tokenizer.getToken() != TerminalNonTerminal.IF){
            printErrorAndExit("Expected 'if'");
        }
        tokenizer.skipToken();

        parseTree.setAltNo(1);
        parseTree.createLeftBranch();
        parseTree.createMiddleBranch();

        parseTree.goDownLeftBranch();
        parseCond();
        parseTree.goUp();

        if(tokenizer.getToken() !=TerminalNonTerminal.THEN){
            printErrorAndExit("Expected 'then'");
        }
        tokenizer.skipToken();

        parseTree.goDownMiddleBranch();
        parseStmtSeq();
        parseTree.goUp();

        if(tokenizer.getToken() == TerminalNonTerminal.ELSE){
            tokenizer.skipToken();
            parseTree.setAltNo(2);
            parseTree.createRightBranch();
            parseTree.goDownRightBranch();
            parseStmtSeq();
            parseTree.goUp();
        }

        if(tokenizer.getToken() != TerminalNonTerminal.END){
            printErrorAndExit("Expected 'end'");
        }
        tokenizer.skipToken();
        if(tokenizer.getToken() != TerminalNonTerminal.SEMICOLON){
            printErrorAndExit("Expected ';'");
        }
        tokenizer.skipToken();
    }

    private void parseLoop(){
        parseTree.setNT(TerminalNonTerminal.LOOP);
        parseTree.setAltNo(1);
        parseTree.createLeftBranch();
        parseTree.createMiddleBranch();

        if(tokenizer.getToken()!=TerminalNonTerminal.WHILE){
            printErrorAndExit("Expected 'while'");
        }
        tokenizer.skipToken();

        parseTree.goDownLeftBranch();
        parseCond();
        parseTree.goUp();

        if(tokenizer.getToken()!=TerminalNonTerminal.LOOP){
            printErrorAndExit("Expected 'loop'");
        }
        tokenizer.skipToken();

        parseTree.goDownMiddleBranch();
        parseStmtSeq();
        parseTree.goUp();

        if(tokenizer.getToken() !=TerminalNonTerminal.END){
            printErrorAndExit("Expected 'end'");
        }
        tokenizer.skipToken();

        if(tokenizer.getToken() != TerminalNonTerminal.SEMICOLON){
            printErrorAndExit("Expected ';'");
        }
        tokenizer.skipToken();
    }

    private void parseIn(){
        parseTree.setNT(TerminalNonTerminal.INPUT);
        parseTree.setAltNo(1);


        if(tokenizer.getToken()!=TerminalNonTerminal.INPUT){
            printErrorAndExit("Expected 'read'");
        }
        tokenizer.skipToken();

        parseTree.createLeftBranch();
        parseTree.goDownLeftBranch();
        parseIdList();
        parseTree.goUp();

        if(tokenizer.getToken()!= TerminalNonTerminal.SEMICOLON){
            printErrorAndExit("Expected ';'");
        }
        tokenizer.skipToken();
    }

    private void parseOut(){
        parseTree.setNT(TerminalNonTerminal.OUTPUT);
        parseTree.setAltNo(1);

        if(tokenizer.getToken()!=TerminalNonTerminal.INPUT){
            printErrorAndExit("Expected 'write'");
        }
        tokenizer.skipToken();

        parseTree.createLeftBranch();
        parseTree.goDownLeftBranch();
        parseIdList();
        parseTree.goUp();

        if(tokenizer.getToken()!= TerminalNonTerminal.SEMICOLON){
            printErrorAndExit("Expected ';'");
        }
        tokenizer.skipToken();

    }

    private void parseCond(){
        parseTree.setNT(TerminalNonTerminal.COND);

        parseTree.createLeftBranch();

        switch(tokenizer.getToken()){
            case TerminalNonTerminal.LPAREN:
                parseTree.setAltNo(1);
                parseTree.goDownLeftBranch();
                parseComp();
                parseTree.goUp();
                break;
            case TerminalNonTerminal.NOT:
                parseTree.setAltNo(2);
                parseTree.goDownLeftBranch();
                parseCond();
                parseTree.goUp();
                break;
            case TerminalNonTerminal.LBRACKET:
                tokenizer.skipToken(); //Skip the left bracket
                parseTree.goDownLeftBranch();
                parseCond();
                parseTree.goUp();

               if(tokenizer.getToken() == TerminalNonTerminal.AND){
                   parseTree.setAltNo(3);

               }else if(tokenizer.getToken() == TerminalNonTerminal.OR){
                   parseTree.setAltNo(4);
               }else{
                   printErrorAndExit("Expected '&&' or '||'");
               }
               tokenizer.skipToken();// SKip the && or ||

                parseTree.createMiddleBranch();
                parseTree.goDownMiddleBranch();
                parseCond();
                parseTree.goUp();

                if(tokenizer.getToken()!=TerminalNonTerminal.RBRACKET){
                    printErrorAndExit("Expected ']'");
                }
                tokenizer.skipToken();
                break;
        }

    }


    private void parseComp(){
        parseTree.setNT(TerminalNonTerminal.COMP);
        parseTree.setAltNo(1);


        if(tokenizer.getToken() != TerminalNonTerminal.LPAREN){
            printErrorAndExit("Expected '('");
        }
        tokenizer.skipToken();

        parseTree.createLeftBranch();
        parseTree.createMiddleBranch();
        parseTree.createRightBranch();

        parseTree.goDownLeftBranch();
        parseOp();
        parseTree.goUp();

        parseTree.goDownMiddleBranch();
        parseCompOp();
        parseTree.goUp();

        parseTree.goDownRightBranch();
        parseOp();
        parseTree.goUp();

        if(tokenizer.getToken() != TerminalNonTerminal.RPAREN){
            printErrorAndExit("Expected ')'");
        }
        tokenizer.skipToken();

    }
    private void parseFactor(){
        parseTree.setNT(TerminalNonTerminal.FAC);
        parseTree.setAltNo(1);
        parseTree.createLeftBranch();

        parseTree.goDownLeftBranch();
        parseOp();
        parseTree.goUp();

        if(tokenizer.getToken() == TerminalNonTerminal.TIMES){
            tokenizer.skipToken();
            parseTree.setAltNo(2);

            parseTree.createMiddleBranch();
            parseTree.goDownMiddleBranch();
            parseFactor();
            parseTree.goUp();
        }
    }

    private void parseOp(){
        parseTree.setNT(TerminalNonTerminal.OP);
        parseTree.createLeftBranch();

        switch(tokenizer.getToken()) {
            case TerminalNonTerminal.CONST:
                parseTree.setAltNo(1);
                parseTree.goDownLeftBranch();
                parseInt();
                parseTree.goUp();
                break;

            case TerminalNonTerminal.ID:
                parseTree.setAltNo(2);
                parseTree.goDownLeftBranch();
                parseId();
                parseTree.goUp();
                break;

            case TerminalNonTerminal.LPAREN:
                tokenizer.skipToken();
                parseTree.setAltNo(3);

                parseTree.goDownLeftBranch();
                parseExp();
                parseTree.goUp();

                if (tokenizer.getToken() != TerminalNonTerminal.RPAREN) {
                    printErrorAndExit("Expected ')'");
                }
                tokenizer.skipToken();
                break;
        }
    }

    private void parseCompOp(){
        parseTree.setNT(TerminalNonTerminal.COMP_OP);


        switch (tokenizer.getToken()) {
            case TerminalNonTerminal.NOT_EQUAL:
                parseTree.setAltNo(1);
                break;
            case TerminalNonTerminal.EQUAL:
                parseTree.setAltNo(2);
                break;
            case TerminalNonTerminal.LESS_THAN:
                parseTree.setAltNo(3);
                break;
            case TerminalNonTerminal.GREATER_THAN:
                parseTree.setAltNo(4);
                break;
            case TerminalNonTerminal.LESS_THAN_EQUAL:
                parseTree.setAltNo(5);
                break;
            case TerminalNonTerminal.GREATER_THAN_EQUAL:
                parseTree.setAltNo(6);
                break;
            default:
                printErrorAndExit("Expected an Operator");
        }
        tokenizer.skipToken();
    }

    private void parseId(){
        parseTree.setNT(TerminalNonTerminal.ID);
        parseTree.setCurrentIdName(tokenizer.getTokenType().getIDName());
        tokenizer.skipToken();
    }

    private void parseInt(){

        parseTree.setNT(TerminalNonTerminal.CONST);
        parseTree.setCurrentIntVal(tokenizer.getTokenType().getVal());
        tokenizer.skipToken();

    }


    // Helper method to print an error message and stop the program
    private void printErrorAndExit(String message) {
        System.err.println("Error: " + message);
        System.exit(1);  // Stop the program with an error status
    }
}
