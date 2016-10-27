package it.project.alessio.shopping_v2;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import it.project.alessio.shopping_v2.DBAdapter.DBShoppingAdapter;
import it.project.alessio.shopping_v2.DBAdapter.Good;
import it.project.alessio.shopping_v2.DBAdapter.Shopping;
import it.project.alessio.shopping_v2.Dialogs.CreateNewGoodAlertDialog;
import it.project.alessio.shopping_v2.Fragments.GoodsListFragment;
import it.project.alessio.shopping_v2.Fragments.DataFragment;
import it.project.alessio.shopping_v2.Fragments.PurchaseGoodFragment;
import it.project.alessio.shopping_v2.Fragments.ShoppingDataFragment;
import it.project.alessio.shopping_v2.Utils.Utils;


public class MyShoppingActivity extends AppCompatActivity
        implements ShoppingDataFragment.OnFragmentInteractionListener,
        PurchaseGoodFragment.OnFragmentInteractionListener,
        CreateNewGoodAlertDialog.DialogListener,
        GoodsListFragment.OnFragmentInteractionListener{

    private DBShoppingAdapter mDB;

    private Shopping mShopping;
    private HashMap<String, Good> mGoodsHashMap;
    // Used for AutoCompleteTextView
    private ArrayList<Good> mGoods;

    private Toolbar toolbar;
    private TabLayout tabLayout;
    private ViewPager viewPager;

    private FloatingActionButton fab;

    //private GoodsListFragment_OLD mGoodsListFragment;
    private ShoppingDataFragment mShoppingDataFragment;
    private GoodsListFragment mGoodsListFragment;

    private DataFragment dataFragment;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_shopping);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);// TODO: titolo activity in base e in base al fragment
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        viewPager = (ViewPager) findViewById(R.id.viewpager);
        setupViewPager(viewPager);

        tabLayout = (TabLayout) findViewById(R.id.tabs);
        tabLayout.setupWithViewPager(viewPager);
        fab = (FloatingActionButton) findViewById(R.id.fab);

        FragmentManager fm = getSupportFragmentManager();

        dataFragment = (DataFragment) fm.findFragmentByTag(DataFragment.DATA_FRAGMENT_TAG);
        if (dataFragment == null) {
            dataFragment = DataFragment.newInstance();
            fm.beginTransaction().add(dataFragment, DataFragment.DATA_FRAGMENT_TAG).commit();
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FragmentManager fm = getSupportFragmentManager();
                Fragment f = fm.findFragmentByTag(PurchaseGoodFragment.TAG_PURCHASE_GOOD_FRAGMENT);
                if (f == null) {
                    AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
                    params.setScrollFlags(0);

                    fm.beginTransaction()
                            .add(R.id.fragment_placeholder,
                                    PurchaseGoodFragment.newInstance(),
                                    PurchaseGoodFragment.TAG_PURCHASE_GOOD_FRAGMENT)
                            .commit();
                }
            }
        });

    }

    @Override
    protected void onResume() {
        super.onResume();
        loadShopping();
        loadGoods();
    }


    @Override
    public boolean onSupportNavigateUp() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentByTag(PurchaseGoodFragment.TAG_PURCHASE_GOOD_FRAGMENT);
        if (f != null) {
            fm.beginTransaction().remove(f).commit();
            return false;
        } else {

            return super.onSupportNavigateUp();
        }
    }

    @Override
    public void onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment f = fm.findFragmentByTag(PurchaseGoodFragment.TAG_PURCHASE_GOOD_FRAGMENT);
        if (f != null) {
            fm.beginTransaction().remove(f).commit();
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void getShoppingDataFragmentInstance(ShoppingDataFragment fragment) {
        mShoppingDataFragment = fragment;
    }

    @Override
    public void getGoodsListFragmentInstance(GoodsListFragment fragment) {
        mGoodsListFragment = fragment;
    }

    @Override
    public void onDialogPositiveClick(Good newGood) {
        writeNewGoodDataToDB(newGood);
    }

    @Override
    public void onDialogNegativeClick() {

    }

    @Override
    public void onAddPurchaseGoodFragment() {
        viewPager.setVisibility(View.GONE);
        tabLayout.setVisibility(View.GONE);
        fab.setVisibility(View.GONE);
    }

    @Override
    public void onRemovePurchaseGoodFragment() {
        viewPager.setVisibility(View.VISIBLE);
        tabLayout.setVisibility(View.VISIBLE);
        fab.setVisibility(View.VISIBLE);
        AppBarLayout.LayoutParams params = (AppBarLayout.LayoutParams) toolbar.getLayoutParams();
        params.setScrollFlags(AppBarLayout.LayoutParams.SCROLL_FLAG_SCROLL
                |AppBarLayout.LayoutParams.SCROLL_FLAG_ENTER_ALWAYS);
    }

    public Shopping getShopping() {
        return mShopping;
    }

    public ArrayList<Good> getGoods() {
        return mGoods;
    }

    public HashMap<String, Good> getGoodsHashMap() {
        return mGoodsHashMap;
    }

    private void loadGoods() {
        mGoods = dataFragment.getGoods();
        if (mGoods != null) {
            mGoodsHashMap = new HashMap<>();
            for (Good g : mGoods)
                mGoodsHashMap.put(g.getName(), g);

        } else {
            new AsyncTask<Void, Void, Boolean>(){
                private ProgressDialog mProgressDialog;

                @Override
                protected void onPreExecute() {
                    // Mostra un altert di caricamento
                    mProgressDialog = Utils.buildProgressDialog(MyShoppingActivity.this);
                    mProgressDialog.show();
                }

                @Override
                protected Boolean doInBackground(Void... params) {
                    if (mDB == null)
                        mDB = new DBShoppingAdapter(MyShoppingActivity.this);
                    mDB.openReadable();
                    mGoods = mDB.getAllGoods();
                    mDB.close();

                    if (mGoods == null)
                        mGoods = new ArrayList<>();

                    mGoodsHashMap = new HashMap<>();
                    for (Good g : mGoods)
                        mGoodsHashMap.put(g.getName(), g);

                    return !mGoods.isEmpty();
                }

                @Override
                protected void onPostExecute(Boolean aBoolean) {
                    mProgressDialog.dismiss();
                }

            }.execute();
        }
    }

    // Calls a new Thread to write new Good data to DB
    public void writeNewGoodDataToDB(Good newGood){
        new AsyncTask<Good, Void, Good>() {
            ProgressDialog mProgressDialog;


            @Override
            protected void onPreExecute() {
                // Lanciare un alert di caricamento
                mProgressDialog = Utils.buildProgressDialog(MyShoppingActivity.this);
                mProgressDialog.show();
            }

            @Override
            protected Good doInBackground(Good... params) {
                Good newGood = params[0];
                if (mDB == null)
                    mDB = new DBShoppingAdapter(getBaseContext());
                mDB.openWriteable();
                long rowId = mDB.createNewGood(newGood.getName(), newGood.getUnitOfMeasureIndex(), newGood.getTags());
                Good mGood = null;
                if (rowId > 0) {
                    mGood = mDB.getGoodById(rowId);
                }
                mDB.close();
                return mGood;
            }

            @Override
            protected void onPostExecute(Good good) {
                // Tolgo l'alert
                mProgressDialog.dismiss();

                // Mostrare messaggio in base al risultato
                String message = "Inserimento fallito";
                if (good != null) {
                    message = "Inserito con successo";
                    mGoods.add(good);
                    mGoodsHashMap.put(good.getName(), good);
                    PurchaseGoodFragment fragment =
                            (PurchaseGoodFragment) getSupportFragmentManager()
                                    .findFragmentByTag(PurchaseGoodFragment.TAG_PURCHASE_GOOD_FRAGMENT);
                    if (fragment != null) {
                        fragment.getGoodNameEdtTxtAdapter().add(good);
                    }
                }
                Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
                        .show();
            }


        }.execute(newGood);
    }

    private void loadShopping() {

        mShopping = dataFragment.getShopping();
        if (mShopping != null) {
            Log.d("LoadShopping", "Caricata dal fragment");
            setOnChangeShoppingBehaviour();
        } else {
            long idShopping = getIntent().getLongExtra(DBShoppingAdapter.ShoppingTable.KEY_ID_SHOPPING, 1);

            new AsyncTask<Long, Void, Boolean>() {
                private ProgressDialog mProgressDialog;

                @Override
                protected void onPreExecute() {
                    mProgressDialog = Utils.buildProgressDialog(MyShoppingActivity.this);
                    mProgressDialog.show();
                }

                @Override
                protected Boolean doInBackground(Long... params) {
                    if (mDB == null)
                        mDB = new DBShoppingAdapter(MyShoppingActivity.this);
                    mDB.openReadable();
                    mShopping = mDB.getShoppingById(params[0]);
                    mDB.close();
                    return mShopping != null;
                }

                @Override
                protected void onPostExecute(Boolean result) {
                    mProgressDialog.dismiss();

                    //Log.d("LoadShopping", "Caricata dal DB");
                    if (result) {
                        if (mGoodsListFragment != null)
                            mGoodsListFragment.setRecyclerViewAdapter();

                        if (mShoppingDataFragment != null)
                            mShoppingDataFragment.setFields();


                        setOnChangeShoppingBehaviour();
                        Snackbar.make(findViewById(android.R.id.content),
                                "Spesa caricata!", Snackbar.LENGTH_LONG)
                                .show();
                    }
                }
            }.execute(idShopping);
        }
    }


    private void setOnChangeShoppingBehaviour() {
        mShopping.setOnEditGoodListener(new Shopping.OnEditGoodListener() {
            @Override
            public void onEditGoodPricePerUnit(double oldValue, double newValue) {

            }

            @Override
            public void onEditGoodQuantity(int oldValue, int newValue) {

            }

            @Override
            public void afterEditGood(Good aGood) {
                mGoodsListFragment.setRecyclerViewAdapter();
                mShoppingDataFragment.updateFields();

                new AsyncTask<Good, Void, Boolean>() {
                    @Override
                    protected Boolean doInBackground(Good... params) {
                        Good mGood = params[0];
                        if (mDB == null)
                            mDB = new DBShoppingAdapter(MyShoppingActivity.this);
                        mDB.openWriteable();
                        boolean result = mDB.updatePurchasedGood(mGood, mGood.getQuantity(), mGood.getPricePerUnit());
                        mDB.close();
                        return result;
                    }
                }.execute(aGood);
            }
        });

        mShopping.setOnAddGoodListener(new Shopping.OnAddGoodListener() {
            @Override
            public void onAddGood(Good aGood) {
                //mGoodsListFragment.setRecyclerViewAdapter();
                mGoodsListFragment.getRecyclerViewAdapter().add(aGood);
                mShoppingDataFragment.updateFields();

                Fragment f = getSupportFragmentManager()
                        .findFragmentByTag(PurchaseGoodFragment.TAG_PURCHASE_GOOD_FRAGMENT);
                if (f != null) {
                    getSupportFragmentManager().beginTransaction().remove(f).commit();
                    viewPager.setVisibility(View.VISIBLE);
                    tabLayout.setVisibility(View.VISIBLE);
                    fab.setVisibility(View.VISIBLE);
                }

                new AsyncTask<Good, Void, Boolean>() {
                    @Override
                    protected Boolean doInBackground(Good... params) {
                        Good mGood = params[0];
                        if (mDB == null)
                            mDB = new DBShoppingAdapter(MyShoppingActivity.this);
                        mDB.openWriteable();
                        boolean result = mDB.addGoodToShopping(mGood);
                        mDB.close();
                        return result;
                    }
                }.execute(aGood);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dataFragment.setData(mShopping);
        dataFragment.setData(mGoods);
    }

    private void setupViewPager(ViewPager viewPager) {
        ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());
        adapter.addFragment(ShoppingDataFragment.newInstance(), "DATI");
        adapter.addFragment(GoodsListFragment.newInstance(), "ACQUISTI");
        viewPager.setAdapter(adapter);
    }

    class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> mFragmentList = new ArrayList<>();
        private final List<String> mFragmentTitleList = new ArrayList<>();

        public ViewPagerAdapter(FragmentManager manager) {
            super(manager);
        }

        @Override
        public Fragment getItem(int position) {
            return mFragmentList.get(position);
        }

        @Override
        public int getCount() {
            return mFragmentList.size();
        }

        public void addFragment(Fragment fragment, String title) {
            mFragmentList.add(fragment);
            mFragmentTitleList.add(title);
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return mFragmentTitleList.get(position);
        }
    }

}
