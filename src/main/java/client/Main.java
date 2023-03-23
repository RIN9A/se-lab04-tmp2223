package client;

import java.io.IOException;
import java.util.Scanner;

public class Main {
    static Scanner scanner = new Scanner(System.in);
    public static void main(String[] args) throws IOException {
        ClientServer clientServer = new ClientServer();


        while (true) {
            String action = enterAction();
            //clientServer.upDateInputOutput();
            if ("1".equals(action)) {
                getFile(clientServer);
            } else if ("2".equals(action)) {
                putFile(clientServer);
            } else if ("3".equals(action)) {
                deleteFile(clientServer);
            } else if ("exit".equals(action)) {
                clientServer.close();
                break;
            }
        }
    }

    public static String enterAction() {
        System.out.println("Введите действие (1 - получить файл с сервера, 2 - загрузить новый файл на сервер, 3 - удалить файл с сервера, exit - завершить сеанс): ");
        return scanner.nextLine();
    }

    public static void getFile(ClientServer clientServer) throws IOException {
        System.out.println("Вы хотите получить файл по имени или по id (1 - имя, 2 - id):");
        String str = scanner.nextLine();
        String searchBy = str.equals("1") ? "имя" : "id";

        System.out.println("Введите " + searchBy + " файла: ");
        String nameId = scanner.nextLine();

        clientServer.getFile(searchBy, nameId);
    }

    public static void putFile(ClientServer clientServer) throws IOException {
        System.out.println("Введите имя файла: ");
        String name = scanner.nextLine();
        clientServer.putFile(name);
    }

    public static void deleteFile(ClientServer clientServer) throws IOException {
        System.out.println("Вы хотите удалить файл по имени или по id (1 - имя, 2 - id):");
        String str = scanner.nextLine();
        String searchBy = str.equals("1") ? "имя" : "id";

        System.out.println("Введите " + searchBy + "файла: ");
        String nameId = scanner.nextLine();

        clientServer.deleteFile(searchBy, nameId);
    }
}


