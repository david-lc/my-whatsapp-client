import java.util.Scanner;

public final class IO {
    public static final Scanner sc = new Scanner(System.in);

    public static String readMessage(String message){
        System.out.print(message);
        return sc.nextLine();
    }
}
