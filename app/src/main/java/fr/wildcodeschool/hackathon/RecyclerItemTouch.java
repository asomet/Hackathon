package fr.wildcodeschool.hackathon;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

public class RecyclerItemTouch extends ItemTouchHelper.SimpleCallback {

    private RecyclerItemTouchHelperListener listener;


    public RecyclerItemTouch(int dragDirs, int swipeDirs, RecyclerItemTouchHelperListener listener) {
        super(dragDirs, swipeDirs);
        this.listener = listener;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
        if(listener != null)
        listener.onSwipe(viewHolder,direction,viewHolder.getAdapterPosition());

        //21:30

    }
}
