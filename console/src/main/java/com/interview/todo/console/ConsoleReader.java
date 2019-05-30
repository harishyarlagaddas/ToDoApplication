package com.interview.todo.console;

import org.springframework.stereotype.Component;

import java.util.InputMismatchException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

@Component("ConsoleReader")
public class ConsoleReader {
    private Logger mLogger = Logger.getLogger("ConsoleReader");

    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_RESET = "\u001B[0m";

    public int displayOptionsAndGetInput(List<String> options) {
        Scanner reader = new Scanner(System.in);

        while (true) {
            System.out.println(ANSI_BLUE+"*********************"+ANSI_RESET);
            for (int i = 0; i < options.size(); i++) {
                System.out.println(ANSI_BLUE+i + ". " + options.get(i)+ANSI_RESET);
            }
            System.out.println(ANSI_BLUE+"*********************\n"+ANSI_RESET);
            System.out.print("Please Select One Option: ");
            int selectedOption = -1;
            if (reader.hasNextInt()) {
                selectedOption = reader.nextInt() ;
            } else {
                displayString("Invalid input...", true);
                reader = new Scanner(System.in);
                continue;
            }

            if (0 > selectedOption || selectedOption >= options.size()) {
                displayString("Invalid selection....", true);
                reader = new Scanner(System.in);
                continue;
            }
            return selectedOption;
        }
    }

    public String displayStringAndGetString(String display) {
        Scanner reader = new Scanner(System.in);
        while (true) {
            System.out.print(ANSI_BLUE+display+" : "+ANSI_RESET);
            String userInput = reader.nextLine();
            if (null == userInput || userInput.length() <= 0) {
                displayString("Invalid Input...", true);
                continue;
            }
            return userInput;
        }
    }

    public int displayStringAndGetInt(String display) {
        Scanner reader = new Scanner(System.in);
        while (true) {
            System.out.print(ANSI_BLUE+display+" : "+ANSI_RESET);
            if (reader.hasNextInt()) {
                int userInput = reader.nextInt() ;
                return userInput;
            } else {
                displayString("Invalid input...", true);
                reader = new Scanner(System.in);
                continue;
            }
        }
    }

    public void displayString(String displayString, boolean error) {
        if (error) {
            System.out.println(ANSI_RED+"\n"+displayString+"\n"+ANSI_RESET);
        } else {
            System.out.println("\n" + displayString + "\n");
        }
    }

    public void displayStringList(List<String> stringList) {
        System.out.println("*********************");
        for (String displayString: stringList) {
            System.out.println(displayString);
        }
        System.out.println("*********************\n");
    }
}
