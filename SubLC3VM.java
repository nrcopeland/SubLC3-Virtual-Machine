/*
Nathaniel Copeland
Programming Languages 4200
Dr. Salimi
Virtual Machine
 */
package vmPackage;
import java.io.*;
import java.text.*;
import java.util.*;

public class SubLC3VM {

    //simple scanner
    static 	Scanner tempScanner = new Scanner(System.in);
    //fmt fixed a bug I having earlier with the decimal places
    static DecimalFormat fmt = new DecimalFormat("0.#########");
    //same as parser holds temp string val
    static final int MAX_MEMORY_SIZE = 500;
    //array for program memomry
    static String nextLine;
    //max memory size
    static String[] commandStorage = new String[MAX_MEMORY_SIZE];
    static int progLength = 0;
    //counter
    static int line = 0;
    //stores the integers and labels them with their line number
    static HashMap<String, Double> main = new HashMap<>();
    //token arry
    static String[] tokens = {"NULL"};
    //temp operand variables
    static double tempOperand1 = 0;
    static double tempOperand2 = 0;
    //temp storage
    static double result = 0;
//method I ended up not using
    /*public void printInstruction() throws IOException {
        InputStream input = new BufferedInputStream(new FileInputStream("src\\mySubLC3_Prog.txt"));
        byte[] buffer = new byte[8192];

        try {
            for (int length = 0; (length = input.read(buffer)) != -1;) {
                System.out.write(buffer, 0, length);
            }
        } finally {
            input.close();
        }*/

    public static void execute() {
        //states program at 0 and and keeps incrementing until end
        exe: for (line = 0; line < progLength; line++) {

            //takes each line and breaks it into tokens
            tokens = commandStorage[line].split(" ");

            //switch statement for each token and then which method is called
            switch (tokens[0]) {
                case "ADD":
                    add();
                    break;
                case "SUB":
                    sub();
                    break;
                case "MUL":
                    mul();
                    break;
                case "DIV":
                    div();
                    break;
                case "IN":
                    in(tokens[1]);
                    break;
                case "PUT": 
    				//example
                case "OUT":
                    out(commandStorage[line]);
                    break;
                case "STO":
                    sto();
                    break;
                case "BRn":
                    brn();
                    break;
                case "BRz":
                    brz();
                    break;
                case "BRp":
                    brp();
                    break;
                case "BRzp":
                    brzp();
                    break;
                case "BRzn":
                    brzn();
                    break;
                case "JMP":
                    jmp();
                    break;
                case "HALT":
                    /*On HALT, break execution*/
                    break exe;
            }
        }
    }
    public static void assign(){
        //checking for '-' goes with negative numbers
        tempOperand1 = tokens[2].charAt(0) == '-' || Character.isDigit(tokens[2].charAt(0)) ? Double.parseDouble(tokens[2]) : main.get(tokens[2]);
        tempOperand2 = tokens[3].charAt(0) == '-' || Character.isDigit(tokens[3].charAt(0)) ? Double.parseDouble(tokens[3]) : main.get(tokens[3]);
    }
    //accepts two integer values of tempOperand1 and tempOperand2 then stores the result of the addition to tokens
    private static void add() {
        // assign operands
        assign();

        // perform addition
        result = tempOperand1 + tempOperand2;

        // send to sto
        tokens[2] = Double.toString(result);
        sto();
    }
    //accepts two integer values of tempOperand1 and tempOperand2 then stores the result of the subtraction to tokens
    private static void sub() {
        // assign operands
        assign();

        // perform subtraction
        result = tempOperand1 - tempOperand2;

        // send to sto
        tokens[2] = Double.toString(result);
        sto();

    }

    //accepts two integer values of tempOperand1 and tempOperand2 then stores the result of the multiplication to tokens
    private static void mul() {
        // assign operands
        assign();

        // perform multiplication
        result = tempOperand1 * tempOperand2;

        // send to sto
        tokens[2] = Double.toString(result);
        sto();

    }
    //accepts two integer values of tempOperand1 and tempOperand2 then stores the result of the division to tokens
    private static void div() {
        // assign operands
        assign();

        // perform division
        result = tempOperand1 / tempOperand2;

        // send to sto
        tokens[2] = Double.toString(result);
        sto();

    }


    public static void out(String output) {
        if (tokens[1].contains("\""))
            System.out.println(output.substring(5, output.length() - 1));
        else
            System.out.println(fmt.format(main.get(tokens[1])));
    }

    //inputs an integer value and stores it
    private static void in(String varName) {
        try{

            main.put(varName, (double)tempScanner.nextInt());

        }catch(InputMismatchException e){
            e.printStackTrace();
        }
    }
    //stores the value of the source
    private static void sto() {

        if (tokens[2].charAt(0) == '-' || Character.isDigit(tokens[2].charAt(0)))
            main.put(tokens[1], Double.parseDouble(tokens[2]));
        else
            main.put(tokens[1], main.get(tokens[2])); // else copy variable
    }
    //if the value of variable is negative, jump
    private static void brn() {
        if (main.get(tokens[1]) < 0) {
            tokens[1] = tokens[2];
            jmp();
        }
    }
    //if the value of variable is 0, jump
    private static void brz() {
        if (main.get(tokens[1]) == 0) {
            tokens[1] = tokens[2];
            jmp();
        }
    }
    //if the value of variable is positive, jump
    private static void brp() {
        if (main.get(tokens[1]) > 0) {
            tokens[1] = tokens[2];
            jmp();
        }
    }
    //if the value of variable is zero or positive, jump
    private static void brzp() {
        if (main.get(tokens[1]) >= 0) {
            tokens[1] = tokens[2];
            jmp();
        }
    }
    //if the value of Variable is zero or negative, jump
    private static void brzn() {
        if (main.get(tokens[1]) <= 0) {
            tokens[1] = tokens[2];
            jmp();
        }
    }
    //jump
    private static void jmp() {
        line = (int) (main.get(tokens[1])-1);
    }



    public static void main(String[] args) throws FileNotFoundException {
        String filename = "mySubLC3_Prog.txt";
        /* Name and Class information */
        System.out.println("Name: Nathaniel Copeland, Class: CSCI4200-Programming Languages, Instructor: Dr. Abi Salimi\n");
        System.out.println("**********************************************************************\n");
        System.out.println("File name: "+filename);

        //printInstruction();
        //System.out.println("----------------------------------------------------------------------\n");

        //accesses file
        try {
            Scanner scan = new Scanner(new File(filename));
            //read from file
            int i = 0;

            //default increment 2.0
            main.put("increment", 2.0);

            //scanning
            while (scan.hasNextLine()) {
                nextLine = scan.nextLine();
                System.out.println(nextLine);

                //ignores comment lines
                if (nextLine.length() != 0 && nextLine.charAt(0) != ';') {
                    commandStorage[i] = nextLine;
                    i++;
                    progLength++;
 
                    tokens = nextLine.split(" ");
                    if (tokens.length == 1) {               
                        tokens = new String[3];
                        tokens[0] = "Nothing";
                        tokens[1] = nextLine;
                        line = i;                           
                        tokens[2] = Integer.toString(line);
                        sto();
                    }

                }

            } // end while
            scan.close();
            System.out.println("\n**********************************************************************\n");
            /* Begin Fetch-Execute Cycle */
            execute();

        } catch (IOException e) {
            e.printStackTrace();
        }
        tempScanner.close();

        //END PROGRAM
    }
}