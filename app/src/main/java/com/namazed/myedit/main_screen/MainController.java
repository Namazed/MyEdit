package com.namazed.myedit.main_screen;

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

public class MainController extends MvpController<MainController, MainPresenter> implements MvpView {

    @BindView(R.id.editText) EditText mainFieldEditText;

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
                getPresenter().workingWithFile(IdMenu.CLEAR.getId());
                return true;
            case R.id.action_open:
                getPresenter().createDialog(
                        getActivity().getString(R.string.dialog_title),
                        getActivity().getString(R.string.dialog_message_open),
                        getActivity().getString(R.string.dialog_name_positive_button_open),
                        getActivity().getString(R.string.menu_action_open)
                );
                return true;
            case R.id.action_save:
                getPresenter().createDialog(
                        getActivity().getString(R.string.dialog_title),
                        getActivity().getString(R.string.dialog_message_save),
                        getActivity().getString(R.string.dialog_name_positive_button_save),
                        getActivity().getString(R.string.menu_action_save)
                );
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

    void showDialog(final String... dialog) {
        final EditText input = new EditText(getActivity());
        new AlertDialog.Builder(getActivity())
                .setTitle(dialog[0])
                .setMessage(dialog[1])
                .setView(input)
                .setPositiveButton(dialog[2], new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        getPresenter().chooseDialog(dialog[3], input.getText().toString(),
                                mainFieldEditText.getText().toString());
                    }
                })
                .show();
    }
}
