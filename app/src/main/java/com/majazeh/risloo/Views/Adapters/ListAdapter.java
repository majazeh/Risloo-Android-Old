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
import com.majazeh.risloo.Views.Activities.EditAccountActivity;
import com.majazeh.risloo.Views.Activities.TAConditionActivity;

import org.json.JSONException;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListHolder> {

    // Adapters
    private SubListBigAdapter subListBigAdapter;
    private SubListSmallAdapter subListSmallAdapter;

    // Vars
    private String asset;
    private ArrayList<Model> list;

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
        Model model = list.get(i);

        try {
            if (!model.get("title").equals("none")) {
                holder.titleTextView.setText((String) model.get("title"));
                holder.titleTextView.setVisibility(View.VISIBLE);
            } else {
                holder.titleTextView.setVisibility(View.GONE);
            }

            if (!model.get("description").equals("none")) {
                holder.descriptionTextView.setText((String) model.get("description"));
                holder.descriptionTextView.setVisibility(View.VISIBLE);
            } else {
                holder.descriptionTextView.setVisibility(View.GONE);
            }

            if (model.get("subset").equals("big")) {
                switch (asset) {
                    case "AboutUs":
                        subListBigAdapter.setList(((AboutUsActivity) activity).aboutUsViewModel.getSubset(i));
                        break;
                    case "TACondition":
                        subListBigAdapter.setList(((TAConditionActivity) activity).taConditionViewModel.getSubset(i));
                        break;
                    case "EditCryptoPublic":
                        subListBigAdapter.setList(((EditAccountActivity) activity).authViewModel.getKeyAssetSubset("public", i));
                        break;
                    case "EditCryptoPrivate":
                        subListBigAdapter.setList(((EditAccountActivity) activity).authViewModel.getKeyAssetSubset("private", i));
                        break;
                }

                holder.listRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", 0, (int) activity.getResources().getDimension(R.dimen._8sdp), (int) activity.getResources().getDimension(R.dimen._12sdp)));
                holder.listRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
                holder.listRecyclerView.setHasFixedSize(false);
                holder.listRecyclerView.setAdapter(subListBigAdapter);

                holder.listRecyclerView.setVisibility(View.VISIBLE);

            } else if (list.get(i).get("subset").equals("small")) {
                switch (asset) {
                    case "AboutUs":
                        subListSmallAdapter.setList(((AboutUsActivity) activity).aboutUsViewModel.getSubset(i));
                        break;
                    case "TACondition":
                        subListSmallAdapter.setList(((TAConditionActivity) activity).taConditionViewModel.getSubset(i));
                        break;
                    case "EditCryptoPublic":
                        subListSmallAdapter.setList(((EditAccountActivity) activity).authViewModel.getKeyAssetSubset("public", i));
                        break;
                    case "EditCryptoPrivate":
                        subListSmallAdapter.setList(((EditAccountActivity) activity).authViewModel.getKeyAssetSubset("private", i));
                        break;
                }

                holder.listRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", 0, (int) activity.getResources().getDimension(R.dimen._6sdp), (int) activity.getResources().getDimension(R.dimen._8sdp)));
                holder.listRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
                holder.listRecyclerView.setHasFixedSize(false);
                holder.listRecyclerView.setAdapter(subListSmallAdapter);

                holder.listRecyclerView.setVisibility(View.VISIBLE);

            } else if (list.get(i).get("subset").equals("none")) {
                holder.listRecyclerView.setVisibility(View.GONE);
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private void initializer(View view) {
        subListBigAdapter = new SubListBigAdapter(activity);
        subListSmallAdapter = new SubListSmallAdapter(activity);
    }

    public void setList(ArrayList<Model> list, String asset) {
        this.list = list;
        this.asset = asset;
        notifyDataSetChanged();
    }

    public class ListHolder extends RecyclerView.ViewHolder {

        public TextView titleTextView, descriptionTextView;
        public RecyclerView listRecyclerView;

        public ListHolder(View view) {
            super(view);
            titleTextView = view.findViewById(R.id.item_list_title_textView);
            descriptionTextView = view.findViewById(R.id.item_list_description_textView);
            listRecyclerView = view.findViewById(R.id.item_list_recyclerView);
        }
    }

}