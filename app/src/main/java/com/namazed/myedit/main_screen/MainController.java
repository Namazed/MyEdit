package com.namazed.myedit.main_screen;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.hannesdorfmann.mosby.mvp.MvpView;
import com.hannesdorfmann.mosby.mvp.conductor.MvpController;
import com.namazed.myedit.MyEditApplication;
import com.namazed.myedit.R;
import com.namazed.myedit.SettingsActivity;
import com.nbsp.materialfilepicker.MaterialFilePicker;
import com.nbsp.materialfilepicker.ui.FilePickerActivity;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;

import static android.app.Activity.RESULT_OK;

// 26.12.2016 Сделал нормальное открытие файла, и сохранение в директорию MyEdit.
// TODO: 26.12.2016  Добавить сохранение текста в базу данных, и вывода их в виде списка.
// TODO: 26.12.2016 сделать заготовку контроллера и презентера для переводчика
public class MainController extends MvpController<MainController, MainPresenter> implements MvpView {

    private static final int PICK_FILE_CODE = 1;
    @BindView(R.id.editText)
    EditText mainFieldEditText;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.controller_main, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        return view;
    }

    @Override
    protected void onAttach(@NonNull View view) {
        super.onAttach(view);
        registerForActivityResult(PICK_FILE_CODE);
    }

    @Override
    protected void onActivityResumed(Activity activity) {
        super.onActivityResumed(activity);
        //Setting size for EditText
        mainFieldEditText.setTextSize(getPresenter().changeSettingsText(getActivity()
                .getString(R.string.pref_size)));

        //Setting style for EditText
        mainFieldEditText.setTypeface(null, (int) getPresenter().changeSettingsText(getActivity()
                .getString(R.string.pref_style)));

        //Setting color for EditText
        mainFieldEditText.setTextColor((int) getPresenter().changeSettingsText(getActivity()
                .getString(R.string.pref_color)));
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        getActivity().getMenuInflater().inflate(R.menu.main_menu, menu);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != RESULT_OK) {
            return;
        }

        if (requestCode == PICK_FILE_CODE) {
            Uri uri = Uri.fromFile(
                    new File(data.getStringExtra(FilePickerActivity.RESULT_FILE_PATH))
            );
            try {
                InputStream inputStream = getMyEditContext().getContentResolver().openInputStream(uri);
                getPresenter().workingWithFile(IdMenu.OPEN, inputStream);
            } catch (FileNotFoundException e) {
                Log.d("debug", "FileNotFound", e);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(
            int requestCode,
            @NonNull String[] permissions,
            @NonNull int[] grantResults
    ) {
        switch (requestCode) {
            case MyEditApplication.PERMISSION_EXTERNAL: {
                if ((grantResults.length > 0)
                        && (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                        && (grantResults[1] == PackageManager.PERMISSION_GRANTED)) {
                    showDialog();
                }
            }
        }

        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                getPresenter().workingWithFile(IdMenu.CLEAR, null);
                return true;
            case R.id.action_open:
                getPresenter().tryOpeningFile();
                return true;
            case R.id.action_save:
                final EditText input = new EditText(getActivity());
                new AlertDialog.Builder(getActivity())
                        .setTitle(getActivity().getString(R.string.dialog_title))
                        .setMessage(getActivity().getString(R.string.dialog_message_save))
                        .setView(input)
                        .setPositiveButton(
                                getActivity().getString(R.string.dialog_name_positive_button_save),
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialogInterface, int i) {
                                        getPresenter().workingWithFile(
                                                IdMenu.SAVE,
                                                null,
                                                input.getText().toString(),
                                                mainFieldEditText.getText().toString()
                                        );
                                    }
                                }
                        )
                        .show();
                return true;
            case R.id.action_settings:
                Intent intentSettings =
                        new Intent(getActivity(), SettingsActivity.class);
                startActivity(intentSettings);
        }
        return super.onOptionsItemSelected(item);
    }

    @NonNull
    @Override
    public MainPresenter createPresenter() {
        return new MainPresenter(((MyEditApplication) getMyEditContext()).getPreferenceDataManager());
    }

    void showFileError(String textError) {
        Toast.makeText(getActivity(), textError, Toast.LENGTH_SHORT).show();
    }

    void actionOverFileSuccessful(String actionSuccessful) {
        Toast.makeText(getActivity(), actionSuccessful, Toast.LENGTH_SHORT).show();
    }

    void clearFieldWithText() {
        mainFieldEditText.setText("");
    }

    void setTextFromFile(String textFromFile) {
        mainFieldEditText.setText(textFromFile);
    }

    Context getMyEditContext() {
        return this.getApplicationContext();
    }

    void showDirectoriesWithFiles() {
        if (Build.VERSION.SDK_INT == Build.VERSION_CODES.M) {
            if (isPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                    && isPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showDialog();
            } else {
                requestPermissions(
                        new String[]{
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE
                        },
                        MyEditApplication.PERMISSION_EXTERNAL
                );
            }
        } else {
            if (isPermissionOldVersion(Manifest.permission.READ_EXTERNAL_STORAGE)
                    && isPermissionOldVersion(Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                showDialog();
            }
        }
    }

    private boolean isPermissionOldVersion(String permission) {
        return getMyEditContext().getPackageManager().checkPermission(
                permission,
                getMyEditContext().getPackageName()
        ) == PackageManager.PERMISSION_GRANTED;
    }

    private boolean isPermission(String permission) {
        return ContextCompat.checkSelfPermission(getMyEditContext(), permission)
                == PackageManager.PERMISSION_GRANTED;
    }

    private void showDialog() {
        new MaterialFilePicker()
                .withActivity(getActivity())
                .withRequestCode(PICK_FILE_CODE)
                .withFilter(Pattern.compile(".*\\.txt$"))
                .withFilterDirectories(false)
                .withHiddenFiles(false)
                .start();
    }
}
