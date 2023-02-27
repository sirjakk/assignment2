import java.util.Scanner;
import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.FileReader;
import java.lang.Thread.State;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.InputMismatchException;
import java.io.File;
import java.io.IOException;

public class PoudelSirjakA2 {
    private static final String SECRET_FILE = "secret.txt";
    private static boolean exit = false;
    private static int mapNums = 0;
    private static boolean readFolioFile = true;
    private static boolean codeIsEmpty = true;
    private static int mapIndex = -1;

    public static void main(String[] args) {

        Maps[] maps = folioReader("folio.txt");
        ArrayList<String> code = new ArrayList<String>();
        // check if the user pressed exit

        // plays the game till usur exsit
        if (mapNums > 0) {
            String userSecret = "";
            while (!exit) {
                Scanner s = new Scanner(System.in);

                startGame();
                int input = -1;
                try {
                    input = s.nextInt();
                } catch (InputMismatchException IME) {
                    System.out.println("--ENTER ONE OF THE OPTION--");
                }
                s.nextLine();
                System.out.println();
                if (input > -1 && input < 6) {
                    if (input == 0) {
                        exit = true;
                        File data = new File("secret.txt");
                        data.delete();
                    }
                    if (input == 1) {
                        for (int i = 0; i < maps.length; i++) {
                            System.out.println(maps[i]);
                        }
                    }
                    if (input == 2) {
                        System.out.println("Which map would you like to open?");
                        String userString = "EMPTY";
                        try {
                            userString = s.nextLine();
                        } catch (InputMismatchException IME) {
                            System.out.println("--ENTER A PROPER MAP NAME--");
                        }
                        code = searchMaps(userString, maps);
                        if (!codeIsEmpty) {
                            System.out.print("CODE: ");
                            for (int i = 0; i < code.size(); i++) {
                                System.out.println(code.get(i));
                            }
                        }
                    }
                    if (input == 3 && !codeIsEmpty) {
                        userSecret = decode(code, maps);
                    }
                    if (input == 4 && !codeIsEmpty) {
                        System.out.println("TYPE IN THE SECRET....");
                        userSecret = s.nextLine();
                        updateMaps(maps, mapIndex, userSecret);
                    }
                    if (input == 5) {
                        sendCode(userSecret);
                    }
                    if (codeIsEmpty && (input == 3 || input == 4 || input == 5)) {
                        System.out.println("--PLEASE OPEN A MAP--");
                    }
                } else {
                    System.out.println("--PICK AN OPTION BETWEEN 0-5--");
                }
            }
        } else if (!readFolioFile) {
            System.out.println("**COULDN'T READ FOLIO**");
        } else {
            System.out.println("**NO MAPS FOUND**");
        }
        System.out.println("End of the program");

    }

    public static void startGame() {
        try {
            FileReader start = new FileReader("Starting prompt.txt");
            Scanner in = new Scanner(start);
            while (in.hasNextLine()) {
                System.out.println(in.nextLine());
            }

        } catch (FileNotFoundException fnf) {
            System.out.println(fnf);
        }

    }

    public static Maps[] folioReader(String fileName) {
        Maps maps[] = new Maps[1];
        try {
            FileReader reader = new FileReader(fileName);
            Scanner in = new Scanner(reader);
            exit = false;
            readFolioFile = true;
            int mapAmount = in.nextInt();
            in.nextLine();
            // gives maps its needed lenght
            maps = new Maps[mapAmount];
            // makes all the different maps in the array onlt if they exsit
            for (int i = 0; i < mapAmount; i++) {
                String[] values = in.nextLine().split(",");
                File file = new File(values[2]);
                if (file.exists()) {
                    int years = Integer.parseInt(values[1]);
                    maps[i] = new Maps(values[0], years, values[2]);
                    mapNums++;
                }
            }

        } catch (FileNotFoundException fnf) {
            System.out.println(fnf);
            exit = true;
            readFolioFile = false;
        }
        return maps;
    }

    public static ArrayList<String> searchMaps(String name, Maps[] maps) {
        int i = 0;
        ArrayList<String> code = new ArrayList<String>();
        try {
            while (!name.equalsIgnoreCase(maps[i].getName())) {
                i++;
            }
        } catch (ArrayIndexOutOfBoundsException AIOB) {
            System.out.println("--NOT A PROPER MAP--");
            codeIsEmpty = true;
            return code;
        }
        try {
            FileReader reader = new FileReader(maps[i].getFileName());
            Scanner in = new Scanner(reader);

            while (in.hasNextLine()) {
                code.add(in.nextLine());
            }
            codeIsEmpty = false;
            mapIndex = i;

        } catch (FileNotFoundException fnf) {
            System.out.println(fnf);
        }

        return code;
    }

    public static String decode(ArrayList<String> code, Maps[] maps) {
        int i = 0;
        String sceret = "";
        try {
            while (i < code.size()) {
                String[] lines = code.get(i).split("\\s+");
                for (int a = 0; a < lines.length; a++) {
                    String[] lettersNumbers = lines[a].split(",");
                    for (int b = 0; b < lettersNumbers.length; b++) {
                        if (!lettersNumbers[b].isEmpty()) {
                            System.out.println(lettersNumbers[b]);
                            int num = Integer.parseInt(lettersNumbers[b]);
                            char letter = (char) num;
                            sceret += letter;
                        }
                    }
                }
                i++;
            }
        } catch (NumberFormatException nf) {
            System.out.println(nf);
        }
        System.out.println(sceret);
        updateMaps(maps, mapIndex, sceret);
        return sceret;
    }

    public static void updateMaps(Maps[] maps, int index, String sceret) {
        maps[index].updateSecret(sceret);
    }

    public static void sendCode(String secret) {
        if (!secret.isEmpty()) {
            try {
                FileWriter writer = new FileWriter(SECRET_FILE);
                BufferedWriter out = new BufferedWriter(writer);
                out.write(secret);
                out.close();

            } catch (IOException io) {
                System.out.println(io);
            }
        } else {
            System.out.println("--NO INFORMATION HAS BEEN CREATED--");
        }
    }
}
