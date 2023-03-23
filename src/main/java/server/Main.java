package server;

public class Main {

    public static void main(String[] args) {
        String drct ="src/main/java/server/data/";
        String file_Name_id = "fileIds.txt";
        ServerFiles serverFiles = new ServerFiles(drct, file_Name_id);

        Server server = new Server(serverFiles);
        server.startSession();



    }


}