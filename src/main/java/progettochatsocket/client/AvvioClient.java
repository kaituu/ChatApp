package progettochatsocket.client;

import java.util.Scanner;

public class AvvioClient {
    public static void main(String[] args) {
        int scelta = -1;
        Scanner sc = new Scanner(System.in);
        do {

            System.out.println("MENU SCELTA CLIENT");
            System.out.println("1 ) \t GUI ");
            System.out.println("2 ) \t CLI ");     //L'input si inserira dall'ide, mentre l'output si vedrà da powershell
            System.out.print("decidi : ");

            scelta = sc.nextInt();

        } while (scelta != 1 && scelta != 2);

        if (scelta == 1)
            ChatApplication.main(null);
        else
            Client.main(null);

    }
}
