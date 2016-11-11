package com.namazed.myedit.main_screen;

import android.graphics.Typeface;
import android.os.Environment;
import android.support.v4.content.res.ResourcesCompat;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.namazed.myedit.R;
import com.namazed.myedit.data.OperationOnFile;
import com.namazed.myedit.data.PreferenceDataManager;

import java.io.File;

public class MainPresenter extends MvpBasePresenter<MainController> {


    private static final String OPEN_MODE_DIALOG = "Open";
    private static final String SAVE_MODE_DIALOG = "Save";
    public static final String MY_DIR = "/MyEdit/";
    private static final String PATH_EXTERNAL = Environment.
            getExternalStorageDirectory().getAbsolutePath() + MY_DIR;
    public static final String FORMAT_FILE = ".txt";


    private OperationOnFile operation = new OperationOnFile();
    private final PreferenceDataManager preferenceDataManager;

    MainPresenter(PreferenceDataManager preferenceDataManager) {
        this.preferenceDataManager = preferenceDataManager;
    }

    void workingWithFile(int idAction, String... file) {
        if (!isViewAttached() || getView() == null) {
            return;
        }

        String pathInternal = getView().getMyEditContext().getFilesDir().toString() + MY_DIR;

        switch (idAction) {
            case 0:
                getView().clearFieldWithText();
                getView().actionOverFileSuccessful(getView().getMyEditContext()
                        .getString(R.string.toast_clear));
                break;
            case 1:
                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    File myFile = new File(pathInternal + file[0] + FORMAT_FILE);
                    if (myFile.exists() && !myFile.isDirectory()) {
                        String textOut = operation.openFile(file[0]);
                        getView().setTextFromFile(textOut);
                    } else {
                        getView().showFileError(getView().getMyEditContext()
                                .getString(R.string.toast_error_find_file));
                    }
                } else {
                    File myFile = new File(PATH_EXTERNAL + file[0] + FORMAT_FILE);
                    if (myFile.exists() && !myFile.isDirectory()) {
                        String textOut = operation.openFileSd(file[0]);
                        getView().setTextFromFile(textOut);
                    } else {
                        getView().showFileError(getView().getMyEditContext()
                                .getString(R.string.toast_error_find_file));
                    }
                }
                break;
            case 2:
                if (!Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                    operation.saveFile(file[0], file[1], getView().getMyEditContext());
                    getView().actionOverFileSuccessful(getView().getMyEditContext()
                            .getString(R.string.toast_save));
                } else {
                    operation.saveFileSd(file[0], file[1]);
                    getView().actionOverFileSuccessful(getView().getMyEditContext()
                            .getString(R.string.toast_save));
                }
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
                if (editTextStyle.contains(getView().getMyEditContext()
                        .getString(R.string.pref_style_bold))) {
                    typeface += Typeface.BOLD;
                }
                if (editTextStyle.contains(getView().getMyEditContext()
                        .getString(R.string.pref_style_italic))) {
                    typeface += Typeface.ITALIC;
                }
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

    void createDialog(String... dialog) {
        if (!isViewAttached() || getView() == null) {
            return;
        }
        getView().showDialog(dialog[0], dialog[1], dialog[2], dialog[3]);
    }

    void chooseDialog(String modeDialog, String textDialog, String body) {
        switch (modeDialog) {
            case OPEN_MODE_DIALOG:
                workingWithFile(IdMenu.CLEAR.getId());
                workingWithFile(IdMenu.OPEN.getId(), textDialog);
                break;
            case SAVE_MODE_DIALOG:
                workingWithFile(IdMenu.SAVE.getId(), textDialog, body);
                break;
        }
    }
}
