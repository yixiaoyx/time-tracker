package model;

import java.util.Scanner;

public class TempDriver {
    public static void main(String[] args) throws Exception {
        System.out.println("This is the TRACKATTACK Test Driver\n");
        System.out.println("i: clock in, o: clock out, r: see current runtime:  ");


        Scanner scanner = new Scanner(System.in);
        Task t = new Task("Dummy Task");
        while(true) {
            String input = scanner.next();
            if (input.equals("i")) {
                t.clockIn();
            } else if (input.equals("o")) {
                t.clockOut();
                //grab total time of task 
                System.out.println("total time of task " + t.getLengthOfLastClockInOut() + " in seconds = " + t.durationInSeconds() );

            } else if (input.equals("r")) {
                System.out.println(t.getActiveRunTimeString());
            }
        }

    }

}
