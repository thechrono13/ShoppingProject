package it.project.alessio.shopping_v2.Dialogs;

import android.app.Activity;
import android.support.v7.app.AlertDialog;
import android.support.v4.app.DialogFragment;
import android.app.Dialog;
//import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import it.project.alessio.shopping_v2.DBAdapter.Good;
import it.project.alessio.shopping_v2.DBAdapter.Good.UnitsOfMeasure;
import it.project.alessio.shopping_v2.R;
import it.project.alessio.shopping_v2.Utils.Utils;

public class CreateNewGoodAlertDialog extends DialogFragment {

    private static final String ARG_TITLE = "title";
    /* The activity that creates an instance of this dialog fragment must
         * implement this interface in order to receive event callbacks.
         * Each method passes the DialogFragment in case the host needs to query it. */
    public interface DialogListener {
        void onDialogPositiveClick(Good newGood);
        void onDialogNegativeClick();
    }

    // Use this instance of the interface to deliver action events
    DialogListener mListener;
    public static final String TAG = "CreateNewGoodAlertDialog";

    public CreateNewGoodAlertDialog(){
        // Empty constructor
    }

    public static CreateNewGoodAlertDialog newInstance(String title) { // TODO: 09/10/2016 da sistemare il passaggio degli argomenti
        Bundle b = new Bundle();
        b.putString(ARG_TITLE, title);
        CreateNewGoodAlertDialog newDialog = new CreateNewGoodAlertDialog();
        newDialog.setArguments(b);
        return newDialog;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {

        final View layout = getActivity().getLayoutInflater()
                .inflate(R.layout.dialog_create_new_good, null);
        final String title = getArguments().getString(ARG_TITLE);

        ((Spinner) layout.findViewById(R.id.dialog_create_new_good_spn_unit_of_measure))
                .setAdapter(new ArrayAdapter<>(getActivity(),
                        android.R.layout.simple_dropdown_item_1line,
                        UnitsOfMeasure.getUnitsOfMeasure()));
        ((EditText) layout.findViewById(R.id.dialog_create_new_good_edt_txt_tags))
                .addTextChangedListener(new TextWatcher() {
                    @Override
                    public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                    }

                    @Override
                    public void onTextChanged(CharSequence s, int start, int before, int count) {

                    }

                    @Override
                    public void afterTextChanged(Editable s) {
                        // TODO: 12/10/2016 Valida il testo inserito
                    }
                });

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AppCompatAlertDialogStyle);
        builder.setTitle(Utils.firstLetterToUpperCase(title))
                .setPositiveButton(R.string.create_new_good_alert_dialog_positive_action, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        String rawTags = ((EditText) layout // TODO: 12/10/2016 Pulisci da eventuali caratteri non validi
                                .findViewById(R.id.dialog_create_new_good_edt_txt_tags))
                                .getText().toString().trim();

                        String[] tags = null;
                        if (!rawTags.isEmpty())
                            tags = TextUtils.split(rawTags, "\\s+");

                        int unitOfMeasureIndex = ((Spinner) layout
                                .findViewById(R.id.dialog_create_new_good_spn_unit_of_measure))
                                .getSelectedItemPosition();

                        mListener.onDialogPositiveClick(
                                new Good(Good.TEMPORARY_ID, title, unitOfMeasureIndex, tags));
                    }
                })
                .setNegativeButton(R.string.dialog_default_negative_action, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        mListener.onDialogNegativeClick();

                    }
                })
                .setView(layout);


        //final AlertDialog dialog = builder.create(); // TODO: da gestire bottoni OK e Cancel se i campi non sono validi
        //dialog.setCanceledOnTouchOutside(false);

        return builder.create();
    }

    // Override the Fragment.onAttach() method to instantiate the NoticeDialogListener
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);

        try
        {
            mListener = (DialogListener) activity;
        }
        catch (ClassCastException e)
        {
            throw new ClassCastException(activity.toString()
                    + " must implement AlertDialogListener");
        }
    }

/*
    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof DialogListener) {
            mListener = (DialogListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnListFragmentInteractionListener");
        }
    }*/

}
