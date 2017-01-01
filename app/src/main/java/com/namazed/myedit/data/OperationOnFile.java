package com.namazed.myedit.data;

import android.os.Environment;
import android.util.Log;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

public class OperationOnFile {
    private static final String MY_DIR = "/MyEdit/";
    private static final String FORMAT_FILE = ".txt";
    private static final String PATH_EXTERNAL = Environment.
            getExternalStorageDirectory().getAbsolutePath() + MY_DIR;

    public void saveFileSd(String fileName, String body) {
        try {
            File root = new File(PATH_EXTERNAL);
            if (!root.mkdir()) {
                Log.d("dir", "Failed create directory!");
            }

            File file = new File(root, fileName + FORMAT_FILE);

            FileWriter writer = new FileWriter(file);
            writer.append(body);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public String openFileSd(InputStream inputStream) {
        StringBuilder textOutput = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            inputStream,
                            Charset.forName("UTF-8")
                    )
            );
            String line;
            while ((line = br.readLine()) != null) {
                textOutput.append(line)
                        .append("\n");
            }
            br.close();
        } catch (IOException e) {
            Log.e("file_exception", "IOException", e);
        }
        return textOutput.toString();
    }
}
