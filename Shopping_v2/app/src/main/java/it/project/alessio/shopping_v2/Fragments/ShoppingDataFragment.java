package it.project.alessio.shopping_v2.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import it.project.alessio.shopping_v2.DBAdapter.Shopping;
import it.project.alessio.shopping_v2.MyShoppingActivity;
import it.project.alessio.shopping_v2.R;
import it.project.alessio.shopping_v2.Utils.Utils;


public class ShoppingDataFragment extends Fragment {

    private TextView shoppingNameTxt;
    private TextView shoppingDateTxt;
    private TextView shoppingPurchasedGoodsNoTxt;
    private TextView shoppingTotalExpenditureTxt;
    private TextView shoppingBudgetTxt;

    private Shopping mShopping;

    private OnFragmentInteractionListener mListener;

    public ShoppingDataFragment() {
        // Required empty public constructor
    }


    public static ShoppingDataFragment newInstance() {
        ShoppingDataFragment fragment = new ShoppingDataFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        // TODO: cercare su google regole di naming per i view ID
        View viewRoot = inflater.inflate(R.layout.fragment_shopping_data, container, false);
        shoppingNameTxt = (TextView) viewRoot.findViewById(R.id.fragment_shopping_data_txt_shopping_name);
        shoppingDateTxt = (TextView) viewRoot.findViewById(R.id.fragment_shopping_data_txt_shopping_date);
        shoppingPurchasedGoodsNoTxt = (TextView) viewRoot.findViewById(R.id.fragment_shopping_data_txt_shopping_purchased_goods_no);
        shoppingTotalExpenditureTxt = (TextView) viewRoot.findViewById(R.id.fragment_shopping_data_txt_shopping_total_expenditure);
        shoppingBudgetTxt = (TextView) viewRoot.findViewById(R.id.fragment_shopping_data_txt_shopping_budget);
        return viewRoot;
    }

    @Override
    public void onResume() {
        super.onResume();

        //mShopping = ((MyShoppingActivity) getActivity()).getShopping();
        //if (mShopping != null)
            setFields();
    }

    public void updateFields() {
        shoppingPurchasedGoodsNoTxt.setText(String.valueOf(mShopping.getPurchasedGoodsNo()));
        shoppingTotalExpenditureTxt.setText(String.format(Locale.getDefault(), "%.2f", mShopping.getTotalExpenditure()));
    }

    public void setFields() {
        if (mShopping == null)
            mShopping = ((MyShoppingActivity) getActivity()).getShopping();
        if (mShopping == null)
            return;

        shoppingNameTxt.setText(mShopping.getName());
        shoppingDateTxt.setText(mShopping.getDate().toString()); // TODO: da implementare
        shoppingBudgetTxt.setText(String.format(Locale.getDefault(), "%.2f", mShopping.getBudget()));
        updateFields();
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

        mListener.getShoppingDataFragmentInstance(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


   public interface OnFragmentInteractionListener {
        void getShoppingDataFragmentInstance(ShoppingDataFragment fragment);
    }
}
