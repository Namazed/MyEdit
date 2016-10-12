package com.namazed.myedit.input_text;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
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

import butterknife.BindView;
import butterknife.ButterKnife;
// TODO: 04.10.2016 не могу печатать в поле EditText

public class MainController extends MvpController<MainController, MainPresenter> implements MvpView {

    @BindView(R.id.editText) EditText mainFieldEditText;

    private static final int ID_MENU_CLEAR = 0;
    private static final int ID_MENU_OPEN = 1;
    private static final int ID_MENU_SAVE = 2;

    private String text;

    @NonNull
    @Override
    protected View onCreateView(@NonNull LayoutInflater inflater, @NonNull ViewGroup container) {
        View view = inflater.inflate(R.layout.controller_main, container, false);
        ButterKnife.bind(this, view);
        setHasOptionsMenu(true);

        return view;
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
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_clear:
                getPresenter().workingWithFile(ID_MENU_CLEAR);
                return true;
            case R.id.action_open:
                // TODO: 29.09.2016 разобраться с AlertDialog, может поместить в отдельный файл
//                getPresenter().createDialog(
//                        getActivity().getString(R.string.dialog_title),
//                        getActivity().getString(R.string.dialog_message_open),
//                        getActivity().getString(R.string.menu_action_open)
//                );

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Имя файла");
                builder.setMessage("Введите имя файла для открытия");
                final EditText INPUT = new EditText(getActivity());
                builder.setView(INPUT);
                builder.setPositiveButton("Открыть", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getPresenter().workingWithFile(ID_MENU_CLEAR);
                        getPresenter().workingWithFile(ID_MENU_OPEN, INPUT.getText().toString());
                    }
                });
                builder.show();
                return true;
            case R.id.action_save:
//                getPresenter().createDialog(
//                        getActivity().getString(R.string.dialog_title),
//                        getActivity().getString(R.string.dialog_message_save),
//                        getActivity().getString(R.string.menu_action_save)
//                );
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Имя файла");
                alert.setMessage("Введите имя файла для сохранения");
                final EditText INPUT_2 = new EditText(getActivity());
                alert.setView(INPUT_2);
                alert.setPositiveButton("Сохранить", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        getPresenter().workingWithFile(ID_MENU_SAVE, INPUT_2.getText().toString(),
                                mainFieldEditText.getText().toString());
                    }
                });
                alert.show();
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
        return new MainPresenter(((MyEditApplication)getMyEditContext()).getPreferenceDataManager());
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

    public Context getMyEditContext() {
        return this.getApplicationContext();
    }

//    void showDialog(final String... dialog/*String title, String message, final String whatDialog*/) {
//        final EditText input = new EditText(getActivity());
//        AlertDialog builder = new AlertDialog.Builder(getActivity())
//                .setTitle(dialog[0])
//                .setMessage(dialog[1])
//                .setView(input)
//                .setPositiveButton("Открыть", new DialogInterface.OnClickListener() {
//            @Override
//            public void onClick(DialogInterface dialogInterface, int i) {
//                switch (dialog[3]) {
//                    case "Open":
//                        getPresenter().workingWithFile(ID_MENU_CLEAR);
//                        getPresenter().workingWithFile(ID_MENU_OPEN, input.getText().toString());
//                        break;
//                    case "Save":
//                        getPresenter().workingWithFile(ID_MENU_SAVE, input.getText().toString(),
//                                mainFieldEditText.getText().toString());
//                        break;
//                }
//            }
//        })
//                .show();
//    }
}
