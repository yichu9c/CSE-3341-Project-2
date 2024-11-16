import java.util.*;
import java.util.Scanner;
public  class Executor {

    private ParseTree p;
    private Map<String, variable> theVariables;
    private Scanner s;

    private class variable {
        private int val;
        private boolean defined;

        public variable() {
            defined = false;
        }

        public variable(int x) {
            val = x;
            defined = true;
        }

        public boolean defined() {
            return defined;
        }

        public int getVal() {
            return val;
        }

        public void set(int x) {
            val = x;
            defined = true;
        }

    }

    public Executor(ParseTree tree, Scanner scanner) {
        p = tree;
        s = scanner;
        theVariables = new HashMap<String, variable>();
    }


    public void ExecuteProgram() {
        p.goAllTheWayBackUp();

        p.goDownLeftBranch();
        executeDeclSeq();
        p.goUp();

        p.goDownMiddleBranch();
        executeStmtSeq();
        p.goUp();

    }

    private void executeDeclSeq() {
        p.goDownLeftBranch();
        executeDecl();
        p.goUp();

        if (p.currentAlternative() == 2) {
            p.goDownMiddleBranch();
            executeDeclSeq();
            p.goUp();
        }
    }

    private void executeDecl() {

        p.goDownLeftBranch();
        List<String> list = executeIdList();
        p.goUp();

        for (String id : list) {
            if (theVariables.containsKey(id)) {
                printErrorAndExit("This variable has already been delcared");
            }

            theVariables.put(id, new variable());
        }
    }

    private void executeStmtSeq(){
        p.goDownLeftBranch();
        executeStmt();
        p.goUp();

        if (p.currentAlternative() == 2) {
            p.goDownMiddleBranch();
            executeStmtSeq();
            p.goUp();
        }
    }

    private List<String> executeIdList() {
        List<String> IdList = new ArrayList<String>();

        p.goDownLeftBranch();
        IdList.add(executeId());
        p.goUp();
        if (p.currentAlternative()==2) {
            p.goDownMiddleBranch();
            IdList.addAll(executeIdList());
            p.goUp();
        }
        return IdList ;
    }


    private void executeStmt(){
        switch(p.currentAlternative()) {
            case 1:
                p.goDownLeftBranch();
                executeAssign();
                p.goUp();
                break;
            case 2:
                p.goDownLeftBranch();
                executeIf();
                p.goUp();
                break;
            case 3:
                p.goDownLeftBranch();
                executeLoop();
                p.goUp();
                break;
            case 4:
                p.goDownLeftBranch();
                executeIn();
                p.goUp();
                break;
            case 5:
                p.goDownLeftBranch();
                executeOut();
                p.goUp();
                break;
        }

    }

    private void executeAssign(){//LOOK HERE FOR EXECUTE ID
        p.goDownLeftBranch();
        String id = p.getCurrentIdName();
        p.goUp();

        p.goDownMiddleBranch();
        int x= executeExp();
        p.goUp();

        if(id == null){
            printErrorAndExit("There exists no ID to assign");
        }

        if(!theVariables.containsKey(id)){
            printErrorAndExit("The ID " + id + " has not been declared");
        }
        theVariables.get(id).set(x);
    }


    private void executeIf(){
        p.goDownLeftBranch();
        boolean b = executeCond();
        p.goUp();

        if(b){
            p.goDownMiddleBranch();
            executeStmtSeq();
            p.goUp();
        }else if(p.currentAlternative() ==2 && !b){
            p.goDownRightBranch();
            executeStmtSeq();
            p.goUp();
        }
    }

    private void executeLoop(){
        p.goDownLeftBranch();
        boolean b = executeCond();
        p.goUp();

        while(b){
            p.goDownMiddleBranch();
            executeStmtSeq();
            p.goUp();


            p.goDownLeftBranch();
            b = executeCond();
            p.goUp();
        }
    }

    private void executeIn(){
        p.goDownLeftBranch();
        List<String> IdList = executeIdList();
        p.goUp();

        for (String id:IdList) {
            if (!s.hasNextInt()) {
                printErrorAndExit("Can not write the value of" + id + " because there is nothing to read");
            }

            if (!theVariables.containsKey(id)) {
                printErrorAndExit("Can not write hte value of " + id + " because it is undeclared");
            }
            theVariables.get(id).set(s.nextInt());
        }
    }

    private void executeOut(){
        p.goDownLeftBranch();
        List<String> IdList = executeIdList();
        p.goUp();

        for (String id:IdList) {
            if (!theVariables.containsKey(id)) {
                printErrorAndExit("Can not write the value of" + id + " it is undeclared");
            }

            if (!theVariables.get(id).defined) {
                printErrorAndExit("Can not write hte value of " + id + " because it is undefined");
            }
            System.out.println(id + " = " + theVariables.get(id).getVal());
        }
    }

    private boolean executeCond(){

        boolean result =false;

        if(p.currentAlternative() ==1){
            p.goDownLeftBranch();
            result = executeComp();
            p.goUp();
        }else if(p.currentAlternative() ==2){
            p.goDownLeftBranch();
            result = !executeCond();
            p.goUp();
        }else if(p.currentAlternative() ==3){
            p.goDownLeftBranch();
            boolean tempOne = executeCond();
            p.goUp();

            p.goDownMiddleBranch();
            boolean tempTwo = executeCond();
            p.goUp();
            result = tempOne&& tempTwo;
        }else if(p.currentAlternative() ==4){
            p.goDownLeftBranch();
            boolean tempOne = executeCond();
            p.goUp();

            p.goDownMiddleBranch();
            boolean tempTwo = executeCond();
            p.goUp();
            result = tempOne || tempTwo;
        }
        return result;
    }


    private boolean  executeComp(){
        boolean result = false;
        p.goDownLeftBranch();
        int tempOne = executeOp();
        p.goUp();


        p.goDownRightBranch();
        int tempTwo = executeOp();
        p.goUp();

        p.goDownMiddleBranch();

        if(p.currentAlternative()==1){
            result =tempOne!=tempTwo;
        }else if(p.currentAlternative()==2){
            result= tempOne==tempTwo;
        }else if(p.currentAlternative()==3){
            result=tempOne<tempTwo;
        } else if ((p.currentAlternative()==4)) {
            result =tempOne >tempTwo;
        }else if(p.currentAlternative()==5){
            result= tempOne<=tempTwo;
        }else if(p.currentAlternative()==6){
            result =tempOne>=tempTwo;
        }
        p.goUp();
        return result;
    }


    private int executeExp(){
        p.goDownLeftBranch();
        int result = executeFac();
        p.goUp();

        if(p.currentAlternative()==2){
            p.goDownMiddleBranch();
            result += executeExp();
            p.goUp();
        }else if(p.currentAlternative() ==3){
            p.goDownRightBranch();
            result -= executeExp();
            p.goUp();
        }
        return result;
    }

    private int executeFac(){
        p.goDownLeftBranch();
        int result = executeOp();
        p.goUp();

        if(p.currentAlternative()==2){
            p.goDownMiddleBranch();
            result *= executeFac();
            p.goUp();
        }
        return result;
    }

    private int executeOp(){
        int result = 0;
        String id;

        switch(p.currentAlternative()) {
            case 1:
                p.goDownLeftBranch();
                result=executeInt();
                p.goUp();
                break;
            case 2:
                p.goDownLeftBranch();
                id = executeId();
                p.goUp();
                if (!theVariables.containsKey(id)){
                    printErrorAndExit(id+" is undeclared");
                }


                if (!theVariables.get(id).defined()){
                    printErrorAndExit(id +" is undefined");
                }
                result = theVariables.get(id).getVal();
                break;
            case 3:
                p.goDownLeftBranch();
                result = executeExp();
                p.goUp();
                break;
        }
        return result;

    }

    private String executeId(){
        return p.getCurrentIdName();
    }

    private int executeInt(){
        return p.getCurrentIntVal();
    }

    private void printErrorAndExit(String message) {
        System.err.println("Error: " + message);
        System.exit(1);  // Stop the program with an error status
    }




}
