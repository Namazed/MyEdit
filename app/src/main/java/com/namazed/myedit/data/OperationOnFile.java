package com.namazed.myedit.data;

import android.os.Environment;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class OperationOnFile {
    public String fileName = null;
    private String path = Environment.
            getExternalStorageDirectory().toString() + "/files/";


    public void saveFile(String fileName, String body) {
        try {
            File root = new File(path);
            if (!root.exists()) {
                root.mkdirs();
            }
            File file = new File(root, fileName);
            FileWriter writer = new FileWriter(file);
            writer.append(body);
            writer.flush();
            writer.close();
//            Toast.makeText(MainActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String openFile(String fileName) {
        StringBuilder textOutput = new StringBuilder();
        File file = new File(this.path, fileName);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                textOutput.append(line + "\n");
            }
            br.close();
        }catch (IOException e) {
            e.printStackTrace();
        }
        return textOutput.toString();
    }
}
