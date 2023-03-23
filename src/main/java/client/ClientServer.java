package client;

import java.io.*;
import java.net.Socket;
import java.util.Scanner;

public class ClientServer {

    private static final String SERVER_ADDRESS = "127.0.0.1";
    private static final int SERVER_PORT = 34522;

    private Socket socket;
    private DataInputStream input;
    private DataOutputStream output;
    public static Scanner scanner;

    public ClientServer() throws IOException {
        socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
        input = new DataInputStream(socket.getInputStream());
        output = new DataOutputStream(socket.getOutputStream());
        scanner = new Scanner(System.in);
    }

    public void upDateInputOutput() throws IOException {
        if(this.socket.isClosed()){
            this.socket = new Socket(SERVER_ADDRESS, SERVER_PORT);
            this.input = new DataInputStream(socket.getInputStream());
            this.output = new DataOutputStream(socket.getOutputStream());
        }


    }

    public void getFile(String searchBy, String nameId) throws IOException {
        output.writeUTF("GET " + searchBy + " " + nameId);

        String otvServer = input.readUTF();
        if (otvServer.equals("200")) {
            int len = Integer.parseInt(input.readUTF());
            byte[] byteAr = new byte[len];
            input.readFully(byteAr, 0, len);
            System.out.println("Файл скачан.");
            System.out.println("Введите имя для скачанного файла:");
            FileManager.saveFile(scanner.nextLine(), byteAr);
            System.out.println("Файл сохранен...");
        } else {
            System.out.println("Ошибка! Файл с такими данными не найден....");
        }
    }

    public boolean putFile(String name) throws IOException {
        //upDateInputOutput();
        if (!FileManager.checkFileExists(name)) {
            System.out.println("Введите содержимое файла");
            String inFile = scanner.nextLine();
            FileManager.saveFile(name, inFile.getBytes());
        }

        System.out.println("Введите название файла для загрузки его на сервер: ");
        String fileServer = scanner.nextLine();
        //if (fileServer== null || fileServer.trim().equals("")) {
         //   fileServer = name;
        //}

        output.writeUTF("PUT " + fileServer);
        output.flush();



        byte[] bytes = FileManager.readFileBytes(name);


        output.writeUTF(String.valueOf(bytes.length));
        output.write(bytes);
        output.flush();


        System.out.println("Запрос отправлен...");
        String otv = input.readUTF();
        if(otv.equals("200")){
            System.out.println("Файл сохранен на сервер. ID = " + input.readUTF());
            return true;
        }else{
            System.out.println("Ошибка! Не удалось загрузить файл на сервер...");
            return false;


        }

    }

    public void deleteFile(String searchBy, String nameId) throws IOException {
        output.writeUTF("DELETE " + searchBy + " " + nameId);
        System.out.println("Запрос отправлен...");

        String otvServer = input.readUTF();
        if (otvServer.equals("200")) {
            System.out.println("Файл удален...");
        } else {
            System.out.println("Ошибка! Файл с такими данными не найден....");
        }
    }

    public void close() throws IOException {
        output.writeUTF("exit");

    }
}