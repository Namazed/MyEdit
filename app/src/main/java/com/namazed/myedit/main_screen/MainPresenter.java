package com.namazed.myedit.main_screen;

import android.graphics.Typeface;
import android.support.annotation.Nullable;
import android.support.v4.content.res.ResourcesCompat;

import com.hannesdorfmann.mosby.mvp.MvpBasePresenter;
import com.namazed.myedit.R;
import com.namazed.myedit.data.OperationOnFile;
import com.namazed.myedit.data.PreferenceDataManager;

import java.io.InputStream;

public class MainPresenter extends MvpBasePresenter<MainController> {
    private OperationOnFile operation = new OperationOnFile();
    private final PreferenceDataManager preferenceDataManager;

    MainPresenter(PreferenceDataManager preferenceDataManager) {
        this.preferenceDataManager = preferenceDataManager;
    }

    void workingWithFile(IdMenu idMenu, @Nullable InputStream inputStream, String... file) {
        if (!isViewAttached() || getView() == null) {
            return;
        }

        switch (idMenu) {
            case CLEAR:
                getView().clearFieldWithText();
                getView().actionOverFileSuccessful(getView().getMyEditContext()
                        .getString(R.string.toast_clear));
                break;
            case OPEN:
                String textOut = operation.openFileSd(inputStream);
                if (textOut == null) {
                    getView().showFileError(getView().getMyEditContext()
                            .getString(R.string.toast_error_find_file));
                } else {
                    getView().setTextFromFile(textOut);
                }
                break;
            case SAVE:
                operation.saveFileSd(file[0], file[1]);
                getView().actionOverFileSuccessful(getView().getMyEditContext()
                        .getString(R.string.toast_save));
                break;
            default:
                break;
        }
    }

    float changeSettingsText(String setting) {
        // TODO: 01.01.2017 Изменить данный метод, т.к. presenter не должен возвращать, что либо кроме void.
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

    void tryOpeningFile() {
        if (!isViewAttached() || getView() == null) {
            return;
        }

        getView().showDirectoriesWithFiles();
    }
}
