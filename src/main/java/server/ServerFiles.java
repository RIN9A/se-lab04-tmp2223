package server;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

public class ServerFiles {
    private String DIRECTORY ;
    private String file_Name_id;
    private HashMap<String, String> fileIds;

    public ServerFiles(String DIRECTORY, String file_Name_id) {
        this.DIRECTORY = DIRECTORY;
        this.file_Name_id = file_Name_id;
        this.fileIds = new HashMap<>();
    }

    public String getDIRECTORY() {
        return DIRECTORY;
    }

    public void setDIRECTORY(String DIRECTORY) {
        this.DIRECTORY = DIRECTORY;
    }

    public String getFile_Name_id() {
        return file_Name_id;
    }

    public void setFile_Name_id(String file_Name_id) {
        this.file_Name_id = file_Name_id;
    }

    public HashMap<String, String> getFileIds() {
        return fileIds;
    }

    public void setFileIds(HashMap<String, String> fileIds) {
        this.fileIds = fileIds;
    }

    public void putMapFiles(String id, String name){
        this.fileIds.put(id, name);
    }
    public void removeMapFiles(String id){
        this.fileIds.remove(id);
    }

    public   void readDirFiles() {
        if (Files.exists(Path.of(file_Name_id))) {
            try (BufferedReader bufferedReader = new BufferedReader(new FileReader(file_Name_id))) {
                String str = bufferedReader.readLine();
                while (str != null && !str.isEmpty()) {
                    String[] name_id_file = str.split(" : ");
                    fileIds.put(name_id_file[0], name_id_file[1]);
                    str = bufferedReader.readLine();
                }

            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public   void writeDirFiles() {
        if (!fileIds.isEmpty()) {
            try (PrintWriter printWriter = new PrintWriter(file_Name_id)) {
                for (Map.Entry<String, String> mp : fileIds.entrySet()
                ) {
                    printWriter.println(mp.getKey() + " : " + mp.getValue());

                }
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }

    public String searchIdDel(String name) {
        for (Map.Entry<String, String> mp : this.fileIds.entrySet()
        ) {
            if (mp.getValue().equals(name)) {
                return mp.getKey();
            }

        }
        return null;
    }




}
