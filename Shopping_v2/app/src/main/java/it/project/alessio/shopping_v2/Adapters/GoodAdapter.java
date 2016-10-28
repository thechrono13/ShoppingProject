package it.project.alessio.shopping_v2.Adapters;


import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.MotionEventCompat;
import android.support.v7.widget.RecyclerView;
import android.view.GestureDetector;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

import it.project.alessio.shopping_v2.DBAdapter.Good;
import it.project.alessio.shopping_v2.DBAdapter.Shopping;
import it.project.alessio.shopping_v2.Utils.Utils;
import it.project.alessio.shopping_v2.R;

public class GoodAdapter extends RecyclerView.Adapter<GoodAdapter.GoodViewHolder> {

    private ArrayList<Good> mGoodsList;
    private Context context;
    private OnRemoveItemListener mListener;

    public GoodAdapter(ArrayList<Good> goodsList, Context context, OnRemoveItemListener listener) {
        this.context = context;
        this.mGoodsList = goodsList;
        this.mListener = listener;
    }
    public interface OnRemoveItemListener {
        public void onRemoveItem(Good good);
    }

    @Override
    public int getItemCount() {
        return mGoodsList.size();
    }

    public Good getItem(int position){
        return mGoodsList.get(position);
    }

    public void addItem(Good good) {
        mGoodsList.add(good);
        notifyItemInserted(mGoodsList.size() -1);
        //notifyDataSetChanged();
    }

    public void removeItem(int position) {
        Good mGood = getItem(position);
        mGoodsList.remove(position);
        notifyItemRemoved(position);

        mListener.onRemoveItem(mGood);
    }

    public void updateItem(Good good, int position) { // TODO: 25/10/2016 da provare se funziona
        // i good non sono ancora comparabili
        //int i = mGoodsList.indexOf();
        mGoodsList.add(position, good);
        notifyItemChanged(position);
    }


    @Override
    public void onBindViewHolder(final GoodViewHolder goodViewHolder, int position) {
        Good mGood = getItem(position);
        goodViewHolder.vName.setText(Utils.firstLetterToUpperCase(mGood.getName()));
        goodViewHolder.vQuantity.setText(String.format(Locale.getDefault(), "%d", mGood.getQuantity()));
        goodViewHolder.vPricePerUnit.setText(String.format(Locale.getDefault(), "%.2f", mGood.getPricePerUnit()));
        goodViewHolder.vPrice.setText(String.format(Locale.getDefault(), "%.2f", mGood.computePrice()));
        goodViewHolder.vQuantityUnitOfMeasure.setText(mGood.getQuantityUnitOfMeasure());
        goodViewHolder.vPricePerUnitCurrency.setText(String.format(Locale.getDefault(),
                context.getString(R.string.currency_euro_per_unit),
                mGood.getUnitOfMeasure()));

    }

    @Override
    public GoodViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View itemView = LayoutInflater.
                from(viewGroup.getContext()).
                inflate(R.layout.goods_list_item, viewGroup, false);

        return new GoodViewHolder(itemView);
    }


    protected static class GoodViewHolder extends RecyclerView.ViewHolder {
        private TextView vName;
        private TextView vQuantity;
        private TextView vPricePerUnit;
        private TextView vPrice;
        private TextView vQuantityUnitOfMeasure;
        private TextView vPricePerUnitCurrency;

        public GoodViewHolder(View view) {
            super(view);
            vName = ((TextView) view.findViewById(R.id.goods_list_item_name_txt_view));
            vQuantity = ((TextView) view.findViewById(R.id.goods_list_item_quantity_value_txt_view));
            vPricePerUnit = ((TextView) view.findViewById(R.id.goods_list_item_price_per_unit_value_txt_view));
            vPrice = ((TextView) view.findViewById(R.id.goods_list_item_price_value_txt_view));
            vQuantityUnitOfMeasure = ((TextView) view.findViewById(R.id.goods_list_item_unit_of_measure_txt_view));
            vPricePerUnitCurrency = ((TextView) view.findViewById(R.id.goods_list_item_price_per_unit_currency_txt_view));
        }

    }

}
