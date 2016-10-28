package it.project.alessio.shopping_v2.Adapters;

import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

/**
 * Created by Alessio on 28/10/2016.
 */

public class GoodTouchHelper extends ItemTouchHelper.SimpleCallback {
    private GoodAdapter mGoodAdapter;

    public GoodTouchHelper(GoodAdapter goodAdapter){
        super(ItemTouchHelper.UP | ItemTouchHelper.DOWN, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.mGoodAdapter = goodAdapter;
    }

    @Override
    public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
        //TODO: Not implemented here
        return false;
    }

    @Override
    public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
        //Remove item
        mGoodAdapter.removeItem(viewHolder.getAdapterPosition());
    }
}
