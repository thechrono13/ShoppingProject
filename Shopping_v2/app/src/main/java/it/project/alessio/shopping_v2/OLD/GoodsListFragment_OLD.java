package it.project.alessio.shopping_v2.Fragments;


import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import it.project.alessio.shopping_v2.DBAdapter.Good;
import it.project.alessio.shopping_v2.DBAdapter.Shopping;
import it.project.alessio.shopping_v2.MyShoppingActivity;
import it.project.alessio.shopping_v2.R;
import it.project.alessio.shopping_v2.Utils.Utils;


public class GoodsListFragment_OLD extends ListFragment {

    private OnFragmentInteractionListener mListener;
    private Shopping mShopping;
    //private ArrayList<Good> mGoods;

    public GoodsArrayAdapter mAdapter;

    public GoodsListFragment_OLD() {
        // Required empty public constructor
    }


    public static GoodsListFragment_OLD newInstance() {
        GoodsListFragment_OLD fragment = new GoodsListFragment_OLD();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        mShopping = ((MyShoppingActivity) getActivity()).getShopping();

        if (mShopping != null)
            setAdapter();
    }

    public void setAdapter() {
        if (mShopping == null && getActivity() != null)
            mShopping = ((MyShoppingActivity) getActivity()).getShopping();

        if (mShopping == null)
            return;

        if (mAdapter != null)
            mAdapter.clear();

        ArrayList<Good> mGoods = mShopping.getPurchasedGoodsArray();
        mAdapter = new GoodsArrayAdapter(getContext(), 0, mGoods);

        setListAdapter(mAdapter);
    }


    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Good mGood = (Good) l.getItemAtPosition(position);
        mShopping.setGoodQuantity(mGood.getId(), mGood.getQuantity() + 1);
    }
/*
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(.fragment_goods_list_old, container, false);
    }*/

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }

        Log.d("Attach", "attach");

        mListener.getGoodsListFragment_OLDInstance(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragment to allow an interaction in this fragment to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    public interface OnFragmentInteractionListener {
        void getGoodsListFragment_OLDInstance(GoodsListFragment_OLD fragment);

    }


    public class GoodsArrayAdapter extends ArrayAdapter<Good>{

        private Context context;
        private List<Good> mGoods;

        //constructor, call on creation
        public GoodsArrayAdapter(Context context, int resource, ArrayList<Good> objects) {
            super(context, resource, objects);

            this.context = context;
            this.mGoods = objects;
        }

        @Nullable
        @Override
        public Good getItem(int position) {
            return mGoods.get(position);
        }

        @Override
        public int getCount() {
            return mGoods.size();
        }


        //called when rendering the list
        public View getView(int position, View convertView, ViewGroup parent) {

            //get the property we are displaying
            Good mGood = getItem(position);

            //get the inflater and inflate the XML layout for each item
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = inflater.inflate(R.layout.goods_list_item, null);

            ((TextView) view.findViewById(R.id.goods_list_item_name_txt_view))
                    .setText(Utils.firstLetterToUpperCase(mGood.getName()));
            ((TextView) view.findViewById(R.id.goods_list_item_quantity_value_txt_view))
                    .setText(String.format(Locale.getDefault(), "%d", mGood.getQuantity()));
            ((TextView) view.findViewById(R.id.goods_list_item_price_per_unit_value_txt_view))
                    .setText(String.format(Locale.getDefault(), "%.2f", mGood.getPricePerUnit()));
            ((TextView) view.findViewById(R.id.goods_list_item_price_value_txt_view))
                    .setText(String.format(Locale.getDefault(), "%.2f", mGood.computePrice()));
            ((TextView) view.findViewById(R.id.goods_list_item_unit_of_measure_txt_view))
                    .setText(mGood.getQuantityUnitOfMeasure());
            ((TextView) view.findViewById(R.id.goods_list_item_price_per_unit_currency_txt_view))
                    .setText(String.format(Locale.getDefault(),
                            getString(R.string.currency_euro_per_unit),
                            mGood.getUnitOfMeasure()));

            return view;
        }
    }
}