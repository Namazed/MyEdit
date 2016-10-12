package com.namazed.myedit.input_text;

import android.graphics.Typeface;
import android.os.Environment;
import android.support.v4.content.res.ResourcesCompat;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.namazed.myedit.R;
import com.namazed.myedit.data.OperationOnFile;
import com.namazed.myedit.data.PreferenceDataManager;

import java.io.File;

public class MainPresenter extends MvpBasePresenter<MainController> {

    private String path = Environment.
            getExternalStorageDirectory().toString() + "/files/";
    private OperationOnFile operation = new OperationOnFile();
    private final PreferenceDataManager preferenceDataManager;

    public MainPresenter(PreferenceDataManager preferenceDataManager) {
        this.preferenceDataManager = preferenceDataManager;
    }


    void workingWithFile(int idAction, String... file) {
        if (!isViewAttached() || getView() == null) {
            return;
        }
        switch (idAction) {
            case 0:
                getView().clearFieldWithText();
                getView().actionOverFileSuccessful("Cleared!");
                break;
            case 1:
                File myFile = new File(path + file[0]);
                if (myFile.exists() && myFile.isFile()) {
                    String textOut = operation.openFile(file[0]);
                    getView().setTextFromFile(textOut);
                } else {
                    getView().showFileError("File is not found!");
                }
                break;
            case 2:
                operation.saveFile(file[0], file[1]);
                getView().actionOverFileSuccessful("Saved!");
                break;
            default:
                break;
        }
    }

     float changeSettingsText(String setting) {
         if (!isViewAttached() || getView() == null) {
             return 0;
         }
        switch (setting) {
            case PreferenceDataManager.PREF_TEXT_SIZE:
                return preferenceDataManager.getTextSize();
            case PreferenceDataManager.PREF_TEXT_STYLE:
                String editTextStyle = preferenceDataManager.getTextStyle();
                int typeface = Typeface.NORMAL;
                if (editTextStyle.contains("Bold")) typeface += Typeface.BOLD;
                if (editTextStyle.contains("Italic")) typeface += Typeface.ITALIC;
                return typeface;
            case PreferenceDataManager.PREF_TEXT_COLOR:
                int colorEditText = ResourcesCompat.getColor(getView().getResources(),
                        R.color.black, null);
                if (preferenceDataManager.whatColor(getView().getMyEditContext()
                        .getString(R.string.pref_color_red))) {
                    colorEditText += ResourcesCompat.getColor(getView().getResources(),
                            R.color.red, null);
                }
                if (preferenceDataManager.whatColor(getView().getMyEditContext()
                        .getString(R.string.pref_color_green))) {
                    colorEditText += ResourcesCompat.getColor(getView().getResources(),
                            R.color.green, null);
                }
                if (preferenceDataManager.whatColor(getView().getMyEditContext()
                        .getString(R.string.pref_color_blue))) {
                    colorEditText += ResourcesCompat.getColor(getView().getResources(),
                            R.color.blue, null);
                }
                return colorEditText;
                default:
                    return 0;
        }
    }

//    void createDialog(String... dialog/*String title, String message, String whatDialog*/) {
//        if (isViewAttached() || getView() == null){
//            return;
//        }
//        getView().showDialog(dialog[0], dialog[1], dialog[2]);
//    }
}
