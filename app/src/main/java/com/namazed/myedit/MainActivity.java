package com.namazed.myedit;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Environment;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    final EditText INPUT = new EditText(this);
    public EditText editText;
    public String fileName = null;
    private String path = Environment.
            getExternalStorageDirectory().toString() + "/files/";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        editText = (EditText) findViewById(R.id.editText);
    }

    @Override
    protected void onResume() {
        super.onResume();
        SharedPreferences sharedPreferences = PreferenceManager.
                getDefaultSharedPreferences(this);

        //Setting size for EditText
        float textSize = Float.
                parseFloat(sharedPreferences.
                        getString(getString(R.string.pref_size), "20"));
        editText.setTextSize(textSize);

        //Setting style for EditText
        String editTextStyle = sharedPreferences.
                getString(getString(R.string.pref_style), "");
        int typeface = Typeface.NORMAL;
        //if (editTextStyle.contains("Bold + Italic")) typeface += Typeface.BOLD_ITALIC;
        if (editTextStyle.contains("Bold")) typeface += Typeface.BOLD;
        if (editTextStyle.contains("Italic")) typeface += Typeface.ITALIC;
        editText.setTypeface(null, typeface);

        //Setting color for EditText
        int colorEditText = Color.BLACK;
        if (sharedPreferences.
                getBoolean(getString(R.string.pref_color_red),false)) {
            colorEditText += Color.RED;
        }
        if (sharedPreferences.
                getBoolean(getString(R.string.pref_color_green), false)) {
            colorEditText += Color.GREEN;
        }
        if (sharedPreferences.
                getBoolean(getString(R.string.pref_color_blue),false)) {
            colorEditText += Color.BLUE;
        }
        editText.setTextColor(colorEditText);


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                editText.setText("");
                Toast.makeText(getApplicationContext(),
                        "Cleared!", Toast.LENGTH_SHORT).show();
                return true;
            case R.id.action_open:
                //TODO Перенести AlertDialog в отдельный метод!
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("Имя файла");
                builder.setMessage("Введите имя файла для открытия");
                builder.setView(INPUT);
                builder.setPositiveButton("Открыть", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        editText.setText("");
                        //String value = INPUT.getText().toString();
                        //fileName = value;
                        fileName = INPUT.getText().toString();
                        File file = new File(path + fileName);
                        if (file.exists() && file.isFile()) {
                            editText.setText(openFile(fileName));
                        } else {
                            Toast.makeText(MainActivity.this, "File is not found!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                builder.show();
                return true;
            case R.id.action_save:
                //TODO Здесь тоже AlertDialog
            case R.id.action_settings:
                Intent intentSettings =
                        new Intent(MainActivity.this, SettingsActivity.class);
                startActivity(intentSettings);
                //break;
        }
        return super.onOptionsItemSelected(item);
    }

    private void saveFile(String fileName, String body) {
        try {
            File root = new File(this.path);
            if (!root.exists()) {
                root.mkdirs();
            }
            File file = new File(root, fileName);
            FileWriter writer = new FileWriter(file);
            writer.append(body);
            writer.flush();
            writer.close();
            Toast.makeText(MainActivity.this, "Saved!", Toast.LENGTH_SHORT).show();
        }catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String openFile(String fileName) {
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
