package it.project.alessio.shopping_v2.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;

import java.util.ArrayList;

import it.project.alessio.shopping_v2.DBAdapter.Good;
import it.project.alessio.shopping_v2.DBAdapter.Shopping;

public class DataFragment extends Fragment {
    public static final String DATA_FRAGMENT_TAG = "data_fragment";


    private Good mGood;
    private Shopping mShopping;
    private ArrayList<Good> mGoods;

    public DataFragment() {
        // Required empty public constructor
    }

    public static DataFragment newInstance() {
        DataFragment fragment = new DataFragment();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // retain this fragment
        setRetainInstance(true);
    }

    public void setData(Good aGood) {
        mGood = aGood;
    }

    public void setData(Shopping aShopping) {
        mShopping = aShopping;
    }
    public void setData(ArrayList<Good> goods) {
        mGoods = goods;
    }

    public Good getGood() {
        return mGood;
    }

    public Shopping getShopping() {
        return mShopping;
    }

    public ArrayList<Good> getGoods() {
        return mGoods;
    }
}
