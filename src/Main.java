import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

public class Main {

    public static void main(String[] args) {

        // Check if the right number of arguments is provided
        if (args.length < 2) {
            System.err.println("Usage: java Main <program_file> <data_file>");
            return;
        }

        try {
            // Open the data file.
            Scanner fileInput = new Scanner(new File(args[1]));

            // Read the entire program file into a string.
            String programCode = new String(Files.readAllBytes(Paths.get(args[0])));

            // Create the Tokenizer with the program code string.
            CoreTokenizer tokenizer = new CoreTokenizer(programCode);

            // Create the Parser.
            Parser parser = new Parser(tokenizer);

            // Parse the program and get the parse tree.
            ParseTree parseTree = parser.parse();

            // Optional: Set to true if you want to pretty-print the program.
            boolean iShouldPrettyPrint = true;

            // Create the Printer and print the parse tree if needed.
            Printer printer = new Printer(System.out, parseTree);
            if (iShouldPrettyPrint) {
                printer.printCoreProgram();
            }

            // Create the Executor with the parse tree and input data.
            Executor executor = new Executor(parseTree, fileInput);

            // Execute the parsed program.
            executor.ExecuteProgram();

        } catch (FileNotFoundException e) {
            System.err.println("File not found: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Error: " + e.getMessage());
        }
    }
}
