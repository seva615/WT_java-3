package by.lab3.Scanner;

import java.util.Scanner;

public class ScannerUtil {
    Scanner sc = new Scanner(System.in);

    public int inputPort() {
        int port;
        try {
            port = sc.nextInt();
        } catch (NumberFormatException e) {
            throw e;
        }
        return port;
    }

    public String inputString() {
        return sc.nextLine();
    }

}
