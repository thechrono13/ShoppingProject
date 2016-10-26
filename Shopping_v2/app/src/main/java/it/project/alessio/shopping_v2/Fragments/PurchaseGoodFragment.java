package it.project.alessio.shopping_v2.Fragments;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;

import it.project.alessio.shopping_v2.DBAdapter.DBShoppingAdapter;
import it.project.alessio.shopping_v2.DBAdapter.Good;
import it.project.alessio.shopping_v2.DBAdapter.Shopping;
import it.project.alessio.shopping_v2.Dialogs.CreateNewGoodAlertDialog;
import it.project.alessio.shopping_v2.MyShoppingActivity;
import it.project.alessio.shopping_v2.PurchaseGoodActivity;
import it.project.alessio.shopping_v2.R;
import it.project.alessio.shopping_v2.Utils.Utils;


public class PurchaseGoodFragment extends Fragment
        /*implements CreateNewGoodAlertDialog.DialogListener */{

    public static final String TAG_PURCHASE_GOOD_FRAGMENT = "PurchaseGoodFragment";

    private OnFragmentInteractionListener mListener;
    // TODO: listener per far sparire l'interfaccia quando compare questo fragment e farla riapparire quando viene distrutto!

    private AutoCompleteTextView goodNameEdtTxt;
    private Button createNewGoodBtn;
    private Button addGoodToCurrentShoppingBtn;
    private Spinner unitOfMeasureSpn;
    private EditText pricePerUnitEdtTxt;
    private EditText quantityEdtTxt;
    private TextView currencyTxt;

    private HashMap<String, Good> mGoodsHashMap;
    private ArrayList<Good> mGoods;

    private Good selectedGood;



    public PurchaseGoodFragment() {
        // Required empty public constructor
    }


    public static PurchaseGoodFragment newInstance() {
        PurchaseGoodFragment fragment = new PurchaseGoodFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        goodNameEdtTxt = (AutoCompleteTextView) getActivity()
                .findViewById(R.id.activity_purchase_new_good_auto_edt_txt_name);
        createNewGoodBtn = (Button) getActivity()
                .findViewById(R.id.activity_purchase_new_good_btn_create_new_good);
        addGoodToCurrentShoppingBtn = (Button) getActivity()
                .findViewById(R.id.activity_purchase_new_good_btn_add_good_to_current_shopping);
        unitOfMeasureSpn = (Spinner) getActivity()
                .findViewById(R.id.activity_purchase_new_good_txt_unit_of_measure);
        pricePerUnitEdtTxt = (EditText) getActivity()
                .findViewById(R.id.activity_purchase_new_good_edt_txt_price_per_unit);
        quantityEdtTxt = (EditText) getActivity()
                .findViewById(R.id.activity_purchase_new_good_edt_txt_quantity);
        currencyTxt = (TextView) getActivity()
                .findViewById(R.id.activity_purchase_new_good_txt_currency);

        configureUIBehaviour();

        if (savedInstanceState != null) {
            restoreState(savedInstanceState);
        }
    }

    @Override
    public void onResume() {
        super.onResume();



        mGoods = ((MyShoppingActivity) getActivity()).getGoods();
        mGoodsHashMap = ((MyShoppingActivity) getActivity()).getGoodsHashMap();
        setupGoodsAdapter(null);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_purchase_good, container, false);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        saveState(outState);
    }

    private void saveState(Bundle outState) {
        outState.putBoolean("goodNameEdtTxtState", goodNameEdtTxt.isEnabled());
        outState.putBoolean("createNewGoodBtnState", createNewGoodBtn.isEnabled());
        outState.putBoolean("addGoodToCurrentShoppingBtnState", addGoodToCurrentShoppingBtn.isEnabled());
        outState.putBoolean("unitOfMeasureSpnState", unitOfMeasureSpn.isEnabled());
        outState.putInt("unitOfMeasureSpnValue", unitOfMeasureSpn.getSelectedItemPosition());
        outState.putBoolean("pricePerUnitEdtTxtState", pricePerUnitEdtTxt.isEnabled());
        outState.putBoolean("quantityEdtTxtState", quantityEdtTxt.isEnabled());

        if (isGoodSelected())
            outState.putParcelable("selectedGood", selectedGood);
    }

    private void restoreState(Bundle savedInstanceState) {
        if (savedInstanceState.containsKey("selectedGood"))
            setGoodAsSelected((Good) savedInstanceState.getParcelable("selectedGood"));

        goodNameEdtTxt.setEnabled(savedInstanceState.getBoolean("goodNameEdtTxtState"));
        createNewGoodBtn.setEnabled(savedInstanceState.getBoolean("createNewGoodBtnState"));
        addGoodToCurrentShoppingBtn
                .setEnabled(savedInstanceState.getBoolean("addGoodToCurrentShoppingBtnState"));
        unitOfMeasureSpn.setEnabled(savedInstanceState.getBoolean("unitOfMeasureSpnState"));
        unitOfMeasureSpn.setSelection(savedInstanceState.getInt("unitOfMeasureSpnValue"));
        pricePerUnitEdtTxt.setEnabled(savedInstanceState.getBoolean("pricePerUnitEdtTxtState"));
        quantityEdtTxt.setEnabled(savedInstanceState.getBoolean("quantityEdtTxtState"));
    }


    public interface OnFragmentInteractionListener {
        void onFragmentInteraction();
    }

    public ArrayAdapter<Good> getGoodNameEdtTxtAdapter(){ // TODO: 25/10/2016 da provare
        return (ArrayAdapter<Good>) goodNameEdtTxt.getAdapter();
    }



    public void setupGoodsAdapter(@Nullable Good aGood) {
        goodNameEdtTxt.setAdapter(new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_dropdown_item_1line,
                mGoods));
        if (aGood != null)
            goodNameEdtTxt.setText(aGood.getName());
    }


    // Sets up listeners and watchers for the UI
    private void configureUIBehaviour(){
        createNewGoodBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String goodName = goodNameEdtTxt.getText().toString().trim();
                if (!goodName.isEmpty())
                    showDialog(goodName);
            }
        });

        addGoodToCurrentShoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertSelectedGoodInShopping();
            }
        });


        currencyTxt.setText(String.format(Locale.getDefault(),
                getString(R.string.currency_euro_per_unit), "-"));


        goodNameEdtTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mGoods != null) {
                    setGoodAsSelected((Good)parent.getAdapter().getItem(position));
                }
            }
        });

        goodNameEdtTxt.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
            @Override
            public void onDismiss() {
                String s = goodNameEdtTxt.getText().toString();

                if (mGoods != null && !s.isEmpty()) {
                    Good mGood = mGoodsHashMap.get(s);
                    if (mGood != null)
                        setGoodAsSelected(mGood);
                    else
                        deselectGood();
                }
            }
        });

        goodNameEdtTxt.addTextChangedListener(new TextWatcher() {
            private int mPreviousLength;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                mPreviousLength = s.length();
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (mPreviousLength > s.length()) {
                    // sto cancellando
                    if (isGoodSelected())
                        deselectGood();
                }

                if (s.length() == 0) {
                    if (isGoodSelected())
                        deselectGood();
                    createNewGoodBtn.setEnabled(false);
                }

                if (mGoods != null && s.length() > 0) {
                    Good mGood = mGoodsHashMap.get(s.toString());
                    if (mGood != null) {
                        if (!goodNameEdtTxt.isPopupShowing())
                            setGoodAsSelected(mGood);

                    } else {
                        if (!goodNameEdtTxt.isPopupShowing())
                            deselectGood();
                    }
                }

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    private boolean isGoodSelected() {
        return selectedGood != null;
    }

    // Once found the good to purchase fills fields with good data
    // and also set the good ID as selected
    private void setGoodAsSelected(Good good){
        selectedGood = good;
        unitOfMeasureSpn.setAdapter((new ArrayAdapter<>(getActivity(),
                android.R.layout.simple_dropdown_item_1line,
                Good.UnitsOfMeasure.getSameCategoryUnits(good.getUnitOfMeasureIndex()))));
        unitOfMeasureSpn.setEnabled(true);
        currencyTxt.setText(String.format(Locale.getDefault(),
                getString(R.string.currency_euro_per_unit),
                good.getUnitOfMeasure()));
        quantityEdtTxt.setEnabled(true);
        pricePerUnitEdtTxt.setEnabled(true);
        createNewGoodBtn.setEnabled(false);
    }

    private void deselectGood() {
        // puisci tutti i campi a parte il nome
        // e li ridisabilita
        selectedGood = null;

        //goodNameEdtTxt;
        createNewGoodBtn.setEnabled(true);
        //addGoodToCurrentShoppingBtn;
        unitOfMeasureSpn.setAdapter(null);
        unitOfMeasureSpn.setEnabled(false);
        pricePerUnitEdtTxt.setText("");
        pricePerUnitEdtTxt.setEnabled(false);
        currencyTxt.setText(String.format(Locale.getDefault(),
                getString(R.string.currency_euro_per_unit),
                "-"));
        quantityEdtTxt.setText("");
        quantityEdtTxt.setEnabled(false);
    }

    private boolean getSelectedGoodData() {
        String sQuantity = quantityEdtTxt.getText().toString();
        String sPricePerUnit = pricePerUnitEdtTxt.getText().toString();
        //String sQuantityUnitOfMeasure = (String) unitOfMeasureSpn.getSelectedItem();

        if (isGoodSelected() &&
                !selectedGood.isTemporary() &&
                !sQuantity.isEmpty() &&
                !sPricePerUnit.isEmpty()) {

            int quantity = Integer.parseInt(sQuantity);
            double pricePerUnit = Double.parseDouble(sPricePerUnit);

            if (quantity == 0) {
                quantityEdtTxt.setError("0 non è valido!");
                return  false;
            }
            if (pricePerUnit == 0) {
                pricePerUnitEdtTxt.setError("0 non è valido!");
                return false;
            }
            int quantityUnitOfMeasure =
                    Good.UnitsOfMeasure.getFixedUnitIndex(unitOfMeasureSpn.getSelectedItemPosition(),
                            Good.UnitsOfMeasure.getUnitCategoryIndex(selectedGood.getUnitOfMeasureIndex()));

            Shopping mShopping = ((MyShoppingActivity) getActivity()).getShopping();

            selectedGood.setIdShopping(mShopping.getId());
            selectedGood.setQuantity(quantity);
            selectedGood.setQuantityUnitOfMeasure(quantityUnitOfMeasure);
            selectedGood.setPricePerUnit(pricePerUnit);
            return true;
        } else if (!isGoodSelected()){
            Snackbar.make(getActivity().findViewById(android.R.id.content), "Nessun bene selezionato!",
                    Snackbar.LENGTH_LONG)
                    .show();
            return false;
        } else if (selectedGood.isTemporary()) {
            Snackbar.make(getActivity().findViewById(android.R.id.content), "ID del bene non valido",
                    Snackbar.LENGTH_LONG)
                    .show();
            return false;
        } else if (sQuantity.isEmpty()) {
            quantityEdtTxt.setError(getString(
                    R.string.activity_purchase_new_good_edt_txt_quantity_error_message));
            return false;
        } else if (sPricePerUnit.isEmpty()){
            pricePerUnitEdtTxt.setError(getString(
                    R.string.activity_purchase_new_good_edt_txt_price_per_unit_error_message_no_value));
            return false;
        } else {
            Snackbar.make(getActivity().findViewById(android.R.id.content), "ID spesa non valido!",
                    Snackbar.LENGTH_LONG)
                    .show();
            return false;
        }

    }


    private void insertSelectedGoodInShopping(){
        if (getSelectedGoodData()) {
            try {
                ((MyShoppingActivity) getActivity()).getShopping().addGood(selectedGood);
            } catch (IllegalArgumentException e){
                Log.w("ADD GOOD", e.getMessage());
                Snackbar.make(getActivity()
                        .findViewById(android.R.id.content),
                        String.format(Locale.getDefault(), "Inserimento %s fallito",
                                selectedGood.getName()),
                        Snackbar.LENGTH_LONG)
                        .show();
            }
        }
    }


/************ Crea nuovo Good **********/
    private void showDialog(String goodName) {
        DialogFragment dialog = CreateNewGoodAlertDialog.newInstance(goodName);
        dialog.show(getFragmentManager(), CreateNewGoodAlertDialog.TAG);
    }
/*
    @Override
    public void onDialogPositiveClick(Good newGood) {
        ((MyShoppingActivity) getActivity()).writeNewGoodDataToDB(newGood);
    }

    @Override
    public void onDialogNegativeClick() {

    }*/

    // Calls a new Thread to write new Good data to DB
    private void writeNewGoodDataToDB(Good newGood){
        new AsyncTask<Good, Void, Long>() {
            ProgressDialog mProgressDialog;

            // Writes new data to DB, executed only in a separated Thread
            /*
            private long insertNewGoodIntoDB(Good newGood){
                if (mDB == null)
                    mDB = new DBShoppingAdapter(getBaseContext());
                mDB.openWriteable();
                long rowId = mDB.createNewGood(newGood.getName(), newGood.getUnitOfMeasureIndex(), newGood.getTags());
                mDB.close();
                return rowId;
            }*/

            @Override
            protected void onPreExecute() {
                // Lanciare un alert di caricamento
                mProgressDialog = Utils.buildProgressDialog(getContext());
                mProgressDialog.show();
            }

            @Override
            protected Long doInBackground(Good... params) {
                //return insertNewGoodIntoDB(params[0]);
                return null;
            }

            @Override
            protected void onPostExecute(Long result) {
                // Tolgo l'alert
                mProgressDialog.dismiss();

                // Mostrare messaggio in base al risultato
                String message = "Inserimento fallito";
                if (result > 0) {
                    message = "Inserito con successo";
                    goodNameEdtTxt.setText(""); // TODO: trovare un modo per ripescare l'ultimo good inserito
                }
                Snackbar.make(getActivity().findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                        .show();
            }


        }.execute(newGood);
    }
}
