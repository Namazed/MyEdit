package com.namazed.myedit.data;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.namazed.myedit.main_screen.MainPresenter;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;

// TODO: 11.11.2016 желательно добавить сохранение в DATABASE, хотя можно и без этого
public class OperationOnFile {
    private String pathExternal = Environment.
            getExternalStorageDirectory().getAbsolutePath() + MainPresenter.MY_DIR;

    public void saveFileSd(String fileName, String body) {
        try {
            File root = new File(pathExternal);
            if(!root.mkdir()) {
                Log.d("dir", "Failed create directory!");
            }

            File file = new File(root, fileName + MainPresenter.FORMAT_FILE);

            FileWriter writer = new FileWriter(file);
            writer.append(body);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String openFileSd(String fileName) {
        StringBuilder textOutput = new StringBuilder();
        File file = new File(pathExternal, fileName + MainPresenter.FORMAT_FILE);
        try {
            BufferedReader br = new BufferedReader(new FileReader(file));
            String line;
            while ((line = br.readLine()) != null) {
                textOutput.append(line)
                        .append("\n");
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textOutput.toString();
    }

    public void saveFile(String fileName, String body, Context context) {
        try {
            // TODO: 11.11.2016 проверить на работоспособность
            FileOutputStream fos = context
                    .openFileOutput(fileName + MainPresenter.FORMAT_FILE, Context.MODE_PRIVATE);
            OutputStreamWriter writer = new OutputStreamWriter(fos);
            writer.write(body);
            writer.flush();
            writer.close();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String openFile(String fileName) {
        StringBuilder textOutput = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(new FileInputStream(fileName + MainPresenter.FORMAT_FILE)));
            String line;
            while ((line = br.readLine()) != null) {
                textOutput.append(line)
                        .append("\n");
            }
            br.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return textOutput.toString();
    }
}
