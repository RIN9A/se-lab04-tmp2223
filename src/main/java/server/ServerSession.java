package server;

import java.io.*;
import java.net.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;
import java.util.concurrent.ExecutorService;

public class ServerSession implements Runnable {
    private ServerFiles serverFiles;
    private ExecutorService executorService;
    private ServerSocket serverSocket;
    private Socket socket;

    public ServerSession(ServerFiles serverFiles, ServerSocket serverSocket, ExecutorService executorService) throws IOException {

            this.serverFiles = serverFiles;
            this.serverSocket = serverSocket;
            this.socket = serverSocket.accept();
            this.executorService = executorService;


    }


    @Override
    public void run() {
        try (
             DataInputStream input = new DataInputStream(socket.getInputStream());
             DataOutputStream output = new DataOutputStream(socket.getOutputStream())) {

            String str = input.readUTF();
            while (!str.equals("exit") ){
                    String[] action = str.split(" ");
                    if (action[0].equals("GET")) {
                        //System.out.println("Скачивание файла с сервера...");
                        getFiles(action, output);
                    } else if (action[0].equals("PUT")) {
                        //System.out.println("Загрузка файла на сервер...");
                        putFiles(action, output, input);

                    } else {
                        //System.out.println("Удаление файла с сервера...");
                        deleteFiles(action, output);
                }
            str = input.readUTF();
            }
            serverFiles.writeDirFiles();
            this.executorService.shutdown();
            this.serverSocket.close();

        }catch (IOException e){

            e.printStackTrace();
        }
    }


    private  void getFiles(String[] act, DataOutputStream out) throws IOException {
        String name;
        if (act[1].equals("имя")) {
            name = act[2];

        } else {
            String id = act[2];
            name = serverFiles.getFileIds().getOrDefault(id, "Файл не найден");
        }
        if (Files.exists(Path.of(serverFiles.getDIRECTORY() + name))) {
            out.writeUTF("200");
            System.out.println("GET 200");

            byte[] bytes = Files.readAllBytes(Path.of(serverFiles.getDIRECTORY() + name));
            out.writeUTF(String.valueOf(bytes.length));
            out.write(bytes);
            out.flush();
            //System.out.println("C сервера скачан файл " + name);
        } else {
            out.writeUTF("404");
            System.out.println("GET 404");
        }
    }

    private  void putFiles(String[] act, DataOutputStream out, DataInputStream input) throws IOException {
        String name;

        if (act.length == 2) {
            name = act[1];
        } else {
            name = randomName2();
        }

        if (Files.exists(Path.of(serverFiles.getDIRECTORY() + name))) {
            out.writeUTF("403");
            System.out.println("PUT 403");
            return;
        }

        int len = Integer.parseInt(input.readUTF());
        byte[] bytes = new byte[len];
        input.readFully(bytes, 0, len);
        Files.write(Path.of(serverFiles.getDIRECTORY() + name), bytes);
        String id = randomID();
        serverFiles.putMapFiles(id, name);
        out.writeUTF("200");
        out.writeUTF(id);
        out.flush();
        System.out.println("PUT 200");
        //System.out.println("На сервер сохранен новый файл: " + name + ", id = " + id);


    }

    private  void deleteFiles(String[] act, DataOutputStream out) throws IOException {
        String name;
        if (act[1].equals("имя")) {
            name = act[2];
        } else {
            String id = act[2];
            name = serverFiles.getFileIds().getOrDefault(id, "Файл не найден");
        }
        if (Files.exists(Path.of(serverFiles.getDIRECTORY() + name))) {
            out.writeUTF("200");
            System.out.println("DELETE 200");
            String iddel = serverFiles.searchIdDel(name);

            //System.out.println("С сервера удален файл id = " + iddel);
            Files.deleteIfExists(Path.of(serverFiles.getDIRECTORY() + name));
            serverFiles.removeMapFiles(iddel);

        } else {
            out.writeUTF("DELETE 404");
        }


    }

    private  String randomName() {

        return UUID.randomUUID().toString().replace("-", "") + ".txt";


    }
    private  String randomName2() {

        File folder = new File(this.serverFiles.getDIRECTORY());
        int i = 1;
        while (true) {
            String fileName = "file" + i;
            File file = new File(folder, fileName);
            if (!file.exists()) {
                return fileName;
            }
            i++;
        }
    }


    private  String randomID() {
        int range = 100;

        if (serverFiles.getFileIds().size() >= 100) {
            range = serverFiles.getFileIds().size() * 2;
        }
        int id = (int) (Math.random() * range);
        while (serverFiles.getFileIds().containsKey(Integer.toString(id))) {
            id = (int) (Math.random() * range);
        }

        return Integer.toString(id);

    }


}







