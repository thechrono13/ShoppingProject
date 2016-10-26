package it.project.alessio.shopping_v2;


import android.support.v4.app.DialogFragment;
//import android.app.DialogFragment;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
//import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Locale;

import it.project.alessio.shopping_v2.AsyncTasks.MyAsyncTask;
import it.project.alessio.shopping_v2.DBAdapter.Good;
import it.project.alessio.shopping_v2.DBAdapter.Good.UnitsOfMeasure;
import it.project.alessio.shopping_v2.DBAdapter.DBShoppingAdapter;
import it.project.alessio.shopping_v2.Dialogs.CreateNewGoodAlertDialog;
import it.project.alessio.shopping_v2.Utils.Utils;

public class PurchaseGoodActivity extends AppCompatActivity
            implements CreateNewGoodAlertDialog.DialogListener {

    private Toolbar toolbar;

    private AutoCompleteTextView goodNameEdtTxt;
    private Button createNewGoodBtn;
    private Button addGoodToCurrentShoppingBtn;
    private Spinner unitOfMeasureSpn;
    private EditText pricePerUnitEdtTxt;
    private EditText quantityEdtTxt;
    private TextView currencyTxt;

    private DBShoppingAdapter mDB;
    private HashMap<String, Good> mGoodsHashMap;// = new HashMap<>();
    private ArrayList<Good> mGoods;

    private Good selectedGood;
    private long shoppingId = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_purchase_new_good);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        // Ottengo l'id della spesa chiamante
        shoppingId = getIntent().getLongExtra(DBShoppingAdapter.ShoppingTable.KEY_ID_SHOPPING, -1);

        goodNameEdtTxt = (AutoCompleteTextView)
                findViewById(R.id.activity_purchase_new_good_auto_edt_txt_name);
        createNewGoodBtn = (Button) findViewById(R.id.activity_purchase_new_good_btn_create_new_good);
        addGoodToCurrentShoppingBtn = (Button)
                findViewById(R.id.activity_purchase_new_good_btn_add_good_to_current_shopping);
        unitOfMeasureSpn = (Spinner)
                findViewById(R.id.activity_purchase_new_good_txt_unit_of_measure);
        pricePerUnitEdtTxt = (EditText)
                findViewById(R.id.activity_purchase_new_good_edt_txt_price_per_unit);
        quantityEdtTxt = (EditText) findViewById(R.id.activity_purchase_new_good_edt_txt_quantity);
        currencyTxt = (TextView) findViewById(R.id.activity_purchase_new_good_txt_currency);

        configureUIBehaviour();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (mDB == null)
            mDB = new DBShoppingAdapter(getBaseContext());
        //setupUI();

        loadGoods();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
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
            outState.putParcelable("selectedGood", selectedGood); // TODO: da considerare

        //if (isGoodSelected())
          //  outState.putBundle("selectedGood", selectedGood.toBundle());
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        restoreState(savedInstanceState);
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
/*
        if (savedInstanceState.containsKey("selectedGood"))
            setGoodAsSelected(new Good(savedInstanceState.getBundle("selectedGood")));
        //else
          //  deselectGood();*/


    }

    /*
    // Sets up the UI
    private void setupUI() {
        configureUIBehaviour();
        loadGoods();
    }*/

    // Load data from DB and create the Adapter
    private void loadGoods(){
        new AsyncTask<Void, Void, Boolean>(){
            private ProgressDialog mProgressDialog;

            // Get data of the goods from DB
            private void loadGoodsDataFromDB() {
                // chiamata anche quando creo un nuovo Good per caricarlo
                mDB.openReadable();
                //mGoods = mDB.getAllGoodsIndexedByName();
                mGoods = mDB.getAllGoods();
                mDB.close();

                mGoodsHashMap = new HashMap<>();
                for (Good g : mGoods)
                    mGoodsHashMap.put(g.getName(), g);
            }

            @Override
            protected void onPreExecute() {
                // Mostra un altert di caricamento
                mProgressDialog = Utils.buildProgressDialog(PurchaseGoodActivity.this);
                mProgressDialog.show();
            }

            @Override
            protected Boolean doInBackground(Void... params) {
                loadGoodsDataFromDB();
                return null;
            }

            @Override
            protected void onPostExecute(Boolean aBoolean) {
                if (mGoods != null)
                    setupGoodsAdapter();
                else
                    deselectGood();
                    //createNewGoodBtn.setEnabled(true);
                // Tolgo alert di caricamento
                mProgressDialog.dismiss();
            }

        }.execute();
    }

    private void setupGoodsAdapter() {
        //if (mGoods != null)
        /*
            goodNameEdtTxt.setAdapter(new ArrayAdapter<>(this,
                    android.R.layout.simple_dropdown_item_1line,
                    mGoods.values().toArray(new Good[mGoods.size()])));*/
        goodNameEdtTxt.setAdapter(new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line,
                mGoods));
        //else
            //createNewGoodBtn.setEnabled(true);
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
        //createNewGoodBtn.setEnabled(false);

        addGoodToCurrentShoppingBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertSelectedGoodInShopping();
            }
        });
/*
        unitOfMeasureSpn.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String s = parent.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        currencyTxt.setText(String.format(Locale.getDefault(),
                getString(R.string.currency_euro_per_unit), "-"));


        goodNameEdtTxt.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (mGoods != null) {

                    //Good mGood = mGoods.get(parent.getAdapter().getItem(position).toString());
                    setGoodAsSelected((Good)parent.getAdapter().getItem(position));
                }
            }
        });

        goodNameEdtTxt.setOnDismissListener(new AutoCompleteTextView.OnDismissListener() {
            @Override
            public void onDismiss() {
                String s = goodNameEdtTxt.getText().toString();
                if (mGoods != null && !s.isEmpty()) {
                    int index = Collections.binarySearch(mGoods,
                            new Good(Good.TEMPORARY_ID, s, 0), Good.GoodNameComparator);
                    if (index >= 0) { // Ho un match
                        if (!goodNameEdtTxt.isPopupShowing())
                            setGoodAsSelected(mGoods.get(index));
                    } else {
                        if (!goodNameEdtTxt.isPopupShowing())
                            deselectGood();
                    }
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
                    /*
                    int index = Collections.binarySearch(mGoods, new Good(Good.TEMPORARY_ID, s.toString(), 0), Good.GoodNameComparator);
                    if (index >= 0) { // Ho un match
                        if (!goodNameEdtTxt.isPopupShowing())
                            setGoodAsSelected(mGoods.get(index));
                        //else
                            //deselectGood(); // da controllare questo else
                    } else {
                        if (!goodNameEdtTxt.isPopupShowing())
                            deselectGood();
                    }*/
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
        //goodNameEdtTxt.setText(good.getName());
        unitOfMeasureSpn.setAdapter((new ArrayAdapter<>(this,
                android.R.layout.simple_dropdown_item_1line,
                UnitsOfMeasure.getSameCategoryUnits(good.getUnitOfMeasureIndex()))));
        unitOfMeasureSpn.setEnabled(true);
        currencyTxt.setText(String.format(Locale.getDefault(),
                getString(R.string.currency_euro_per_unit),
                good.getUnitOfMeasure()));
        //unitOfMeasureSpn.setClickable(true);
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

    // Sets Good data to add to current shopping
    private boolean getSelectedGoodData() {
        String sQuantity = quantityEdtTxt.getText().toString();
        String sPricePerUnit = pricePerUnitEdtTxt.getText().toString();
        //String sQuantityUnitOfMeasure = (String) unitOfMeasureSpn.getSelectedItem();

        if (isGoodSelected() &&
                !selectedGood.isTemporary() &&
                shoppingId != -1 &&
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
                    UnitsOfMeasure.getFixedUnitIndex(unitOfMeasureSpn.getSelectedItemPosition(),
                            UnitsOfMeasure.getUnitCategoryIndex(selectedGood.getUnitOfMeasureIndex()));

            selectedGood.setIdShopping(shoppingId);
            selectedGood.setQuantity(quantity);
            selectedGood.setQuantityUnitOfMeasure(quantityUnitOfMeasure);
            selectedGood.setPricePerUnit(pricePerUnit);
            return true;
        } else if (!isGoodSelected()){
            Snackbar.make(findViewById(android.R.id.content), "Nessun bene selezionato!",
                    Snackbar.LENGTH_LONG)
                    .show();
            return false;
        } else if (selectedGood.isTemporary()) {
            Snackbar.make(findViewById(android.R.id.content), "ID del bene non valido",
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
            Snackbar.make(findViewById(android.R.id.content), "ID spesa non valido!",
                    Snackbar.LENGTH_LONG)
                    .show();
            return false;
        }

    }

    private void insertSelectedGoodInShopping(){
        if (getSelectedGoodData()) {
            new AsyncTask<Good, Void, Boolean>() {
                ProgressDialog mProgressDialog;

                // Function called in a separated Thread to insert the selected Good in current Shopping
                private boolean writeSelectedGoodInShopping(Good selectedGood) {
                    mDB.openWriteable();
                    boolean result = mDB.addGoodToShopping(selectedGood);
                    mDB.close();
                    return result;
                }

                @Override
                protected void onPreExecute() {
                    // Lanciare un alert di caricamento
                    mProgressDialog = Utils.buildProgressDialog(PurchaseGoodActivity.this);
                    mProgressDialog.show();
                }

                @Override
                protected Boolean doInBackground(Good... params) {
                    return writeSelectedGoodInShopping(params[0]);
                }

                @Override
                protected void onPostExecute(Boolean result) {
                    // Tolgo l'alert di caricamento
                    mProgressDialog.dismiss();
                    if (result)
                        finish();
                    else
                        Snackbar.make(findViewById(android.R.id.content),
                                "Inserimento di "
                                        + Utils.firstLetterToUpperCase(selectedGood.getName()) + " fallito!", Snackbar.LENGTH_LONG)
                                .show();
                }
            }.execute(selectedGood);
        }
    }


    //******** Creazione nuovo Good ********//

    private void showDialog(String goodName) {
        DialogFragment dialog = CreateNewGoodAlertDialog.newInstance(goodName);
        dialog.show(getSupportFragmentManager(), CreateNewGoodAlertDialog.TAG);
    }

    @Override
    public void onDialogPositiveClick(Good newGood) { // TODO: 12/10/2016 Verificare che l'input utente non contenga caratteri non validi
        // Eseguo la scrittura in un Thread separato
        //new WriteToDBTask().execute(newGood);
        writeNewGoodDataToDB(newGood);
    }

    @Override
    public void onDialogNegativeClick() {

    }

    // Calls a new Thread to write new Good data to DB
    private void writeNewGoodDataToDB(Good newGood){
        new AsyncTask<Good, Void, Long>() {
            ProgressDialog mProgressDialog;

            // Writes new data to DB, executed only in a separated Thread
            private long insertNewGoodIntoDB(Good newGood){
                if (mDB == null)
                    mDB = new DBShoppingAdapter(getBaseContext());
                mDB.openWriteable();
                long rowId = mDB.createNewGood(newGood.getName(), newGood.getUnitOfMeasureIndex(), newGood.getTags());
                mDB.close();
                return rowId;
            }

            @Override
            protected void onPreExecute() {
                // Lanciare un alert di caricamento
                mProgressDialog = Utils.buildProgressDialog(PurchaseGoodActivity.this);
                mProgressDialog.show();
            }

            @Override
            protected Long doInBackground(Good... params) {
                return insertNewGoodIntoDB(params[0]);
            }

            @Override
            protected void onPostExecute(Long result) {
                // Ricarico i dati
                loadGoods();

                // Tolgo l'alert
                mProgressDialog.dismiss();

                // Mostrare messaggio in base al risultato
                String message = "Inserimento fallito";
                if (result > 0) {
                    message = "Inserito con successo";
                    goodNameEdtTxt.setText(""); // TODO: trovare un modo per ripescare l'ultimo good inserito
                }
                Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                        .show();
            }


        }.execute(newGood);
    }

    //****************************************//

}
