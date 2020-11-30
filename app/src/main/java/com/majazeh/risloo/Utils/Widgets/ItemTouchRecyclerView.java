package com.majazeh.risloo.Utils.Widgets;

import android.graphics.Canvas;
import android.view.View;

import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Views.Adapters.ArchivesAdapter;

import org.jetbrains.annotations.NotNull;

public class ItemTouchRecyclerView extends ItemTouchHelper.SimpleCallback {

    // Objects
    private final OnItemTouchListener onItemTouchListener;

    public ItemTouchRecyclerView(int dragDirs, int swipeDirs, OnItemTouchListener onItemTouchListener) {
        super(dragDirs, swipeDirs);
        this.onItemTouchListener = onItemTouchListener;
    }

    @Override
    public void onSwiped(@NotNull RecyclerView.ViewHolder viewHolder, int direction) {
        onItemTouchListener.onSwiped(viewHolder, direction, viewHolder.getAdapterPosition());
    }

    @Override
    public boolean onMove(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder, @NotNull RecyclerView.ViewHolder target) {
        return true;
    }

    @Override
    public void onSelectedChanged(RecyclerView.ViewHolder viewHolder, int actionState) {
        if (viewHolder != null) {
            View foregroundView = ((ArchivesAdapter.ArchivesHolder) viewHolder).foreGroundView;
            getDefaultUIUtil().onSelected(foregroundView);
        }
    }

    @Override
    public void clearView(@NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder) {
        View foregroundView = ((ArchivesAdapter.ArchivesHolder) viewHolder).foreGroundView;
        getDefaultUIUtil().clearView(foregroundView);
    }

    @Override
    public void onChildDraw(@NotNull Canvas canvas, @NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foregroundView = ((ArchivesAdapter.ArchivesHolder) viewHolder).foreGroundView;
        getDefaultUIUtil().onDraw(canvas, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onChildDrawOver(@NotNull Canvas canvas, @NotNull RecyclerView recyclerView, @NotNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        View foregroundView = ((ArchivesAdapter.ArchivesHolder) viewHolder).foreGroundView;
        getDefaultUIUtil().onDrawOver(canvas, recyclerView, foregroundView, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public int convertToAbsoluteDirection(int flags, int layoutDirection) {
        return super.convertToAbsoluteDirection(flags, layoutDirection);
    }

    public interface OnItemTouchListener {
        void onSwiped(RecyclerView.ViewHolder viewHolder, int direction, int position);
    }

}