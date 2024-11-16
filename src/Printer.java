import java.io.*;

public class Printer {

    private PrintStream out;
    private ParseTree p;

    /**
     * Default constructor if no PrintStream is specified.
     */
    public Printer(ParseTree p) {
        this(System.out, p);
    }

    /**
     * Constructor for the Printer class.
     */
    public Printer(PrintStream out, ParseTree fullyInitializedParseTree) {
        this.out = out;
        this.p = fullyInitializedParseTree;
    }

    /**
     * Print a CORE program.
     */
    public void printCoreProgram() {
        out.println("program");

        p.goDownLeftBranch();
        printDeclSeq();
        p.goUp();

        out.println("begin");

        p.goDownMiddleBranch();
        printStmtSeq();
        p.goUp();

        out.println("end");
    }

    /**
     * Print a declaration sequence.
     */
    private void printDeclSeq() {
        p.goDownLeftBranch();
        printDecl();
        p.goUp();

        if (p.currentAlternative() == 2) {
            p.goDownMiddleBranch();
            printDeclSeq();
            p.goUp();
        }
    }

    /**
     * Print a statement sequence.
     */
    private void printStmtSeq() {
        p.goDownLeftBranch();
        printStmt();
        p.goUp();

        if (p.currentAlternative() == 2) {
            p.goDownMiddleBranch();
            printStmtSeq();
            p.goUp();
        }
    }

    /**
     * Print a declaration.
     */
    private void printDecl() {
        out.print("int ");

        p.goDownLeftBranch();
        printIdList();
        p.goUp();

        out.println(";");
    }

    /**
     * Print an ID list.
     */
    private void printIdList() {
        p.goDownLeftBranch();
        printId();
        p.goUp();

        if (p.currentAlternative() == 2) {
            out.print(", ");

            p.goDownMiddleBranch();
            printIdList();
            p.goUp();
        }
    }

    /**
     * Print a statement.
     */
    private void printStmt() {
        switch (p.currentAlternative()) {
            case 1 -> {
                p.goDownLeftBranch();
                printAss();
            }
            case 2 -> {
                p.goDownLeftBranch();
                printIf();
            }
            case 3 -> {
                p.goDownLeftBranch();
                printLoop();
            }
            case 4 -> {
                p.goDownLeftBranch();
                printIn();
            }
            case 5 -> {
                p.goDownLeftBranch();
                printOut();
            }
        }
        p.goUp();
    }

    /**
     * Print an assignment.
     */
    private void printAss() {
        p.goDownLeftBranch();
        printId();
        p.goUp();

        out.print(" = ");

        p.goDownMiddleBranch();
        printExp();
        p.goUp();

        out.println(";");
    }

    /**
     * Print an if statement.
     */
    private void printIf() {
        out.print("if ");

        p.goDownLeftBranch();
        printCond();
        p.goUp();

        out.println(" then");

        p.goDownMiddleBranch();
        printStmtSeq();
        p.goUp();

        if (p.currentAlternative() == 2) {
            out.println("else");

            p.goDownRightBranch();
            printStmtSeq();
            p.goUp();
        }

        out.println("end;");
    }

    /**
     * Print a loop statement.
     */
    private void printLoop() {
        out.print("while ");

        p.goDownLeftBranch();
        printCond();
        p.goUp();

        out.println(" loop");

        p.goDownMiddleBranch();
        printStmtSeq();
        p.goUp();

        out.println("end;");
    }

    /**
     * Print an input statement.
     */
    private void printIn() {
        out.print("read ");

        p.goDownLeftBranch();
        printIdList();
        p.goUp();

        out.println(";");
    }

    /**
     * Print an output statement.
     */
    private void printOut() {
        out.print("write ");

        p.goDownLeftBranch();
        printIdList();
        p.goUp();

        out.println(";");
    }

    /**
     * Print a condition.
     */
    private void printCond() {
        p.goDownLeftBranch();
        switch (p.currentAlternative()) {
            case 1 -> printComp();
            case 2 -> {
                out.print("!");
                printCond();
            }
            case 3 -> {
                out.print("[");
                printCond();
                p.goUp();
                out.print(" && ");
                p.goDownMiddleBranch();
                printCond();
                out.print("]");
            }
            case 4 -> {
                out.print("[");
                printCond();
                p.goUp();
                out.print(" || ");
                p.goDownMiddleBranch();
                printCond();
                out.print("]");
            }
        }
        p.goUp();
    }

    /**
     * Print a comparison.
     */
    private void printComp() {
        out.print("(");

        p.goDownLeftBranch();
        printOp();
        p.goUp();

        p.goDownMiddleBranch();
        printCompOp();
        p.goUp();

        p.goDownRightBranch();
        printOp();
        p.goUp();

        out.print(")");
    }

    /**
     * Print an expression.
     */
    private void printExp() {
        p.goDownLeftBranch();
        printTrm();
        p.goUp();

        switch (p.currentAlternative()) {
            case 2 -> {
                out.print(" + ");
                p.goDownMiddleBranch();
                printExp();
                p.goUp();
            }
            case 3 -> {
                out.print(" - ");
                p.goDownMiddleBranch();
                printExp();
                p.goUp();
            }
        }
    }

    /**
     * Print a term.
     */
    private void printTrm() {
        p.goDownLeftBranch();
        printOp();
        p.goUp();

        if (p.currentAlternative() == 2) {
            out.print(" * ");
            p.goDownMiddleBranch();
            printTrm();
            p.goUp();
        }
    }

    /**
     * Print an operator.
     */
    private void printOp() {
        switch (p.currentAlternative()) {
            case 1 -> {
                p.goDownLeftBranch();
                printNo();
            }
            case 2 -> {
                p.goDownLeftBranch();
                printId();
            }
            case 3 -> {
                p.goDownLeftBranch();
                out.print("(");
                printExp();
                out.print(")");
            }
        }
        p.goUp();
    }

    /**
     * Print a comparison operator.
     */
    private void printCompOp() {
        switch (p.currentAlternative()) {
            case 1 -> out.print(" != ");
            case 2 -> out.print(" == ");
            case 3 -> out.print(" < ");
            case 4 -> out.print(" > ");
            case 5 -> out.print(" <= ");
            case 6 -> out.print(" >= ");
        }
    }

    /**
     * Print an ID.
     */
    private void printId() {
        out.print(p.getCurrentIdName());
    }

    /**
     * Print a number.
     */
    private void printNo() {
        out.print(p.getCurrentIntVal());
    }
}
