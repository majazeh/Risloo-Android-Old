package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.Views.Activities.AboutUsActivity;
import com.majazeh.risloo.Views.Activities.CryptoActivity;
import com.majazeh.risloo.Views.Activities.TAConditionActivity;

import org.json.JSONException;

import java.util.ArrayList;
import java.util.Objects;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListHolder> {

    // Adapters
    private SubListBigAdapter subListBigAdapter;
    private SubListSmallAdapter subListSmallAdapter;

    // Vars
    private String asset;
    private ArrayList<Model> lists;

    // Objects
    private Activity activity;

    public ListAdapter(@NonNull Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public ListHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_list, viewGroup, false);

        initializer(view);

        return new ListHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ListHolder holder, int i) {
        Model model = lists.get(i);

        try {
            if (!model.get("title").equals("none")) {
                holder.titleTextView.setVisibility(View.VISIBLE);

                holder.titleTextView.setText(model.get("title").toString());
            } else {
                holder.titleTextView.setVisibility(View.GONE);
            }

            if (!model.get("description").equals("none")) {
                holder.descriptionTextView.setVisibility(View.VISIBLE);

                holder.descriptionTextView.setText(model.get("description").toString());
            } else {
                holder.descriptionTextView.setVisibility(View.GONE);
            }

            if (model.get("subset").equals("big")) {
                switch (asset) {
                    case "AboutUs":
                        subListBigAdapter.setList(((AboutUsActivity) Objects.requireNonNull(activity)).aboutUsViewModel.getSubset(i));
                        break;
                    case "TACondition":
                        subListBigAdapter.setList(((TAConditionActivity) Objects.requireNonNull(activity)).taConditionViewModel.getSubset(i));
                        break;
                    case "CryptoPublic":
                        subListBigAdapter.setList(((CryptoActivity) Objects.requireNonNull(activity)).cryptoViewModel.getSubset("public", i));
                        break;
                    case "CryptoPrivate":
                        subListBigAdapter.setList(((CryptoActivity) Objects.requireNonNull(activity)).cryptoViewModel.getSubset("private", i));
                        break;
                }

                holder.listRecyclerView.setVisibility(View.VISIBLE);

                holder.listRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", 0, (int) activity.getResources().getDimension(R.dimen._8sdp), (int) activity.getResources().getDimension(R.dimen._12sdp)));
                holder.listRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
                holder.listRecyclerView.setHasFixedSize(false);
                holder.listRecyclerView.setAdapter(subListBigAdapter);

            } else if (lists.get(i).get("subset").equals("small")) {
                switch (asset) {
                    case "AboutUs":
                        subListSmallAdapter.setList(((AboutUsActivity) Objects.requireNonNull(activity)).aboutUsViewModel.getSubset(i));
                        break;
                    case "TACondition":
                        subListSmallAdapter.setList(((TAConditionActivity) Objects.requireNonNull(activity)).taConditionViewModel.getSubset(i));
                        break;
                    case "CryptoPublic":
                        subListSmallAdapter.setList(((CryptoActivity) Objects.requireNonNull(activity)).cryptoViewModel.getSubset("public", i));
                        break;
                    case "CryptoPrivate":
                        subListSmallAdapter.setList(((CryptoActivity) Objects.requireNonNull(activity)).cryptoViewModel.getSubset("private", i));
                        break;
                }

                holder.listRecyclerView.setVisibility(View.VISIBLE);

                holder.listRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", 0, (int) activity.getResources().getDimension(R.dimen._6sdp), (int) activity.getResources().getDimension(R.dimen._8sdp)));
                holder.listRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
                holder.listRecyclerView.setHasFixedSize(false);
                holder.listRecyclerView.setAdapter(subListSmallAdapter);

            } else if (lists.get(i).get("subset").equals("none")) {
                holder.listRecyclerView.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return lists.size();
    }

    private void initializer(View view) {
        subListBigAdapter = new SubListBigAdapter(activity);
        subListSmallAdapter = new SubListSmallAdapter(activity);
    }

    public void setList(ArrayList<Model> lists, String asset) {
        this.lists = lists;
        this.asset = asset;
        notifyDataSetChanged();
    }

    public class ListHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView, descriptionTextView;
        public RecyclerView listRecyclerView;

        public ListHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.single_item_list_title_textView);
            descriptionTextView = view.findViewById(R.id.single_item_list_description_textView);
            listRecyclerView = view.findViewById(R.id.single_item_list_recyclerView);
        }
    }

}