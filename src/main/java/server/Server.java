package server;

import java.io.*;
import java.net.ServerSocket;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server {
    private static final int PORT = 34522;
    ServerFiles serverFiles;

    public Server(ServerFiles serverFiles) {
        this.serverFiles = serverFiles;
    }

    public void startSession()  {
        ExecutorService executorService = Executors.newFixedThreadPool(7);
        try (ServerSocket serverSocket = new ServerSocket(PORT)) {
            serverFiles.readDirFiles();
            ServerSession serverSession = new ServerSession(this.serverFiles, serverSocket,executorService);
            executorService.submit(serverSession);

        } catch (IOException e) {
            serverFiles.writeDirFiles();
            e.printStackTrace();
        }
    }

}
