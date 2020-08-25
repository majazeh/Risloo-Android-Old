//package com.majazeh.risloo.Views.Adapters;
//
//import android.app.Activity;
//import android.os.Build;
//import android.os.Handler;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.CheckBox;
//
//import androidx.annotation.NonNull;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.majazeh.risloo.R;
//
//import java.util.ArrayList;
//
//public class ReferenceCheckBoxAdapter extends RecyclerView.Adapter<ReferenceCheckBoxAdapter.ReferenceCheckBoxHolder> {
//
//    // Vars
//    private ArrayList<String> references;
//
//    // Objects
//    private Activity activity;
//    private Handler handler;
//
//    public ReferenceCheckBoxAdapter(Activity activity) {
//        this.activity = activity;
//    }
//
//    @NonNull
//    @Override
//    public ReferenceCheckBoxHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
//        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_reference_checkbox, viewGroup, false);
//
//        initializer(view);
//
//        return new ReferenceCheckBoxHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ReferenceCheckBoxHolder holder, int i) {
//
//        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
//            holder.itemCheckBox.setBackgroundResource(R.drawable.draw_4sdp_white_ripple);
//        }
//
//        holder.itemCheckBox.setText(references.get(i));
//
//        holder.itemView.setOnClickListener(v -> {
//            holder.itemView.setClickable(false);
//            handler.postDelayed(() -> holder.itemView.setClickable(true), 500);
//
//
//        });
//    }
//
//    @Override
//    public int getItemCount() {
//        return references.size();
//    }
//
//    private void initializer(View view) {
//        handler = new Handler();
//    }
//
//    public void setReferences(ArrayList<String> references) {
//        this.references = references;
//        notifyDataSetChanged();
//    }
//
//    public class ReferenceCheckBoxHolder extends RecyclerView.ViewHolder {
//
//        public CheckBox itemCheckBox;
//
//        public ReferenceCheckBoxHolder(View view) {
//            super(view);
//            itemCheckBox = view.findViewById(R.id.single_item_reference_checkbox);
//        }
//    }
//
//}