package it.project.alessio.shopping_v2.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import it.project.alessio.shopping_v2.DBAdapter.Good;
import it.project.alessio.shopping_v2.DBAdapter.Shopping;
import it.project.alessio.shopping_v2.Adapters.GoodAdapter;
import it.project.alessio.shopping_v2.MyShoppingActivity;
import it.project.alessio.shopping_v2.R;
import it.project.alessio.shopping_v2.RecyclerItemClickListener;


public class GoodsListFragment extends Fragment {

    private Shopping mShopping;

    private RecyclerView mRecyclerView;

    private OnFragmentInteractionListener mListener;

    public GoodsListFragment() {
        // Required empty public constructor
    }

    public static GoodsListFragment newInstance() {
        GoodsListFragment fragment = new GoodsListFragment();
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
        View viewRoot = inflater.inflate(R.layout.fragment_goods_list, container, false);
        mRecyclerView = (RecyclerView) viewRoot.findViewById(R.id.cardList);
        mRecyclerView.setHasFixedSize(true);
        LinearLayoutManager llm = new LinearLayoutManager(getActivity());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(llm);

        mShopping = ((MyShoppingActivity) getActivity()).getShopping();
        if (mShopping != null)
            setRecyclerViewAdapter();

        mRecyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getActivity(),
                mRecyclerView, new RecyclerItemClickListener.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Good mGood = ((GoodAdapter) mRecyclerView.getAdapter()).getItem(position);
                mShopping.setGoodQuantity(mGood.getId(), mGood.getQuantity() + 1);
            }

            @Override
            public void onItemLongClick(View view, int position) {
                Good mGood = ((GoodAdapter) mRecyclerView.getAdapter()).getItem(position);
            }
        }));
        return viewRoot;
    }

    public void setRecyclerViewAdapter(){
        if (mShopping == null)
            mShopping = ((MyShoppingActivity) getActivity()).getShopping();

        mRecyclerView.setAdapter(new GoodAdapter(mShopping.getPurchasedGoodsArray(), getContext()));
    }

    public GoodAdapter getRecyclerViewAdapter() {
        return (GoodAdapter) mRecyclerView.getAdapter();
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

        mListener.getGoodsListFragmentInstance(this);
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {

        void getGoodsListFragmentInstance(GoodsListFragment fragment);
    }
}
