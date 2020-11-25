package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.ViewModels.AboutUsViewModel;
import com.majazeh.risloo.ViewModels.TAConditionsViewModel;

import org.json.JSONException;

import java.util.ArrayList;

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ListHolder> {

    // ViewModel
    private AboutUsViewModel aboutUsViewModel;
    private TAConditionsViewModel TAConditionsViewModel;

    // Adapters
    private SubListBigAdapter subListBigAdapter;
    private SubListSmallAdapter subListSmallAdapter;

    // Vars
    private String asset;
    private ArrayList<Model> list;

    // Objects
    private Activity activity;

    public ListAdapter(Activity activity) {
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
            holder.titleTextView.setText(model.get("title").toString());
            holder.descriptionTextView.setText(model.get("description").toString());

            if (model.get("subset").equals("big")) {
                if (asset.equals("AboutUs")) {
                    subListBigAdapter.setSubListBig(aboutUsViewModel.getSubset(i));
                } else if (asset.equals("TermCondition")) {
                    subListBigAdapter.setSubListBig(TAConditionsViewModel.getSubset(i));
                }

                holder.listRecyclerView.setVisibility(View.VISIBLE);

                holder.listRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", 0, (int) activity.getResources().getDimension(R.dimen._8sdp), (int) activity.getResources().getDimension(R.dimen._12sdp)));
                holder.listRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
                holder.listRecyclerView.setHasFixedSize(false);
                holder.listRecyclerView.setAdapter(subListBigAdapter);

            } else if (list.get(i).get("subset").equals("small")) {
                if (asset.equals("AboutUs")) {
                    subListSmallAdapter.setSubListSmall(aboutUsViewModel.getSubset(i));
                } else if (asset.equals("TermCondition")) {
                    subListSmallAdapter.setSubListSmall(TAConditionsViewModel.getSubset(i));
                }

                holder.listRecyclerView.setVisibility(View.VISIBLE);

                holder.listRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", 0, (int) activity.getResources().getDimension(R.dimen._6sdp), (int) activity.getResources().getDimension(R.dimen._8sdp)));
                holder.listRecyclerView.setLayoutManager(new LinearLayoutManager(activity, LinearLayoutManager.VERTICAL, false));
                holder.listRecyclerView.setHasFixedSize(false);
                holder.listRecyclerView.setAdapter(subListSmallAdapter);

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
        aboutUsViewModel = new ViewModelProvider((FragmentActivity) activity).get(AboutUsViewModel.class);
        TAConditionsViewModel = new ViewModelProvider((FragmentActivity) activity).get(TAConditionsViewModel.class);

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
            titleTextView = view.findViewById(R.id.single_item_list_title_textView);
            descriptionTextView = view.findViewById(R.id.single_item_list_description_textView);
            listRecyclerView = view.findViewById(R.id.single_item_list_recyclerView);
        }
    }

}