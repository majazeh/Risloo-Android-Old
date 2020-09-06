package com.majazeh.risloo.Utils;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ItemDecorator extends RecyclerView.ItemDecoration {

    // Vars
    private String layoutManager;
    private int margin;

    public ItemDecorator(String layoutManager, int margin) {
        this.layoutManager = layoutManager;
        this.margin = margin;
    }

    @Override
    public void getItemOffsets(@NonNull Rect rect, @NonNull View view, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.State state) {
        super.getItemOffsets(rect, view, recyclerView, state);

        int position = recyclerView.getChildAdapterPosition(view);
        int count = state.getItemCount();

        switch (layoutManager) {
            case "verticalLinearLayout":

                // Top Margin
                if (position == 0) {
                    rect.top = margin;
                } else {
                    rect.top = margin / 4;
                }

                // Bottom Margin
                if (count > 0 && position == count - 1) {
                    rect.bottom = margin;
                } else {
                    rect.bottom = margin / 4;
                }

                // Right And Left Margins
                rect.right = margin;
                rect.left = margin;

                break;
            case "verticalLinearLayout2":

                // Top Margin
                if (position == 0) {
                    rect.top = margin;
                } else {
                    rect.top = margin / 2;
                }

                // Bottom Margin
                if (count > 0 && position == count - 1) {
                    rect.bottom = margin;
                } else {
                    rect.bottom = margin / 2;
                }

                // Right And Left Margins
                rect.right = margin / 2;
                rect.left = margin / 2;

                break;
            case "verticalLinearLayout3":

                // Top Margin
                if (position == 0) {

                } else {
                    rect.top = margin / 3;
                }

                // Bottom Margin
                if (count > 0 && position == count - 1) {

                } else {
                    rect.bottom = margin / 3;
                }

                break;
            case "horizontalLinearLayout":

                // Top And Bottom Margins
                rect.top = margin;
                rect.bottom = margin;

                // Right Margin
                if (position == 0) {
                    rect.right = margin;
                } else {
                    rect.right = margin / 4;
                }

                // Left Margin
                if (count > 0 && position == count - 1) {
                    rect.left = margin;
                } else {
                    rect.left = margin / 4;
                }

                break;
            case "horizontalLinearLayout2":

                // Top And Bottom Margins
                rect.top = margin;
                rect.bottom = margin;

                // Right Margin
                if (position == 0) {
                    rect.right = margin;
                } else {
                    rect.right = margin / 2;
                }

                // Left Margin
                if (count > 0 && position == count - 1) {
                    rect.left = margin;
                } else {
                    rect.left = margin / 2;
                }

                break;
            case "horizontalLinearLayout3":

                // Right Margin
                if (position == 0) {
                    rect.right = margin;
                } else {
                    rect.right = margin / 4;
                }

                // Left Margin
                if (count > 0 && position == count - 1) {
                    rect.left = margin;
                } else {
                    rect.left = margin / 4;
                }

                break;
            case "gridLayout":

                // Top Margin
                if (position == 0 || position == 1) {
                    rect.top = margin / 2;
                } else {
                    rect.top = margin / 6;
                }

                // Bottom Margin
                if (count > 0 && position == count - 1) {
                    rect.bottom = margin / 2;
                } else if (count > 0 && position == count - 2){
                    if (position % 2 == 0){
                        rect.bottom = margin / 2;
                    } else {
                        rect.bottom = margin / 6;
                    }
                } else {
                    rect.bottom = margin / 6;
                }

                // Right And Left Margins
                if (position % 2 == 0) {
                    rect.right = margin;
                    rect.left = margin / 6;
                } else {
                    rect.right = margin / 6;
                    rect.left = margin;
                }

                break;
            case "listLayout":

                // Top Margin
                if (position == 0) {
                    rect.top = margin;
                } else {
                    rect.top = (int) (margin / 1.5);
                }

                // Bottom Margin
                if (count > 0 && position == count - 1) {
                    rect.bottom = margin;
                } else {
                    rect.bottom = (int) (margin / 1.5);
                }

                // Right And Left Margins
                rect.right = margin;
                rect.left = margin;

                break;
            case "subListLayout":

                // Top Margin
                if (position == 0) {

                } else {
                    rect.top = (int) (margin / 1.5);
                }

                // Bottom Margin
                if (count > 0 && position == count - 1) {

                } else {
                    rect.bottom = (int) (margin / 1.5);
                }

                // Right And Left Margins
                rect.right = margin;
                rect.left = margin;

                break;
            case "normalLayout":

                // Top And Bottom Margin
                rect.top = margin;
                rect.bottom = margin;

                // Right And Left Margins
                rect.right = margin;
                rect.left = margin;

                break;
        }

    }

}