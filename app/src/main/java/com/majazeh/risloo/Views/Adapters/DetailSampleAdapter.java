package com.majazeh.risloo.Views.Adapters;

import android.app.Activity;
import android.os.Handler;
import android.text.InputType;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.Entities.Model;
import com.majazeh.risloo.Models.Managers.ExceptionManager;
import com.majazeh.risloo.Models.Repositories.SampleRepository;
import com.majazeh.risloo.R;
import com.majazeh.risloo.ViewModels.SampleViewModel;

import org.json.JSONException;

import java.util.ArrayList;

public class DetailSampleAdapter extends RecyclerView.Adapter<DetailSampleAdapter.DetailSampleHolder> {

    // ViewModels
    private SampleViewModel viewModel;

    // Vars
    private String result = "";
    private ArrayList<Model> values;
    private boolean editable = false;

    // Objects
    private Activity activity;
    private Handler handler;

    public DetailSampleAdapter(Activity activity) {
        this.activity = activity;
    }

    @NonNull
    @Override
    public DetailSampleHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.single_item_detail_sample, viewGroup, false);

        initializer(view);

        return new DetailSampleHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull DetailSampleHolder holder, int i) {
//        Model model = values.get(i);
//
//        if (editable) {
//            holder.itemView.setBackgroundResource(R.drawable.draw_16sdp_white_solid_border_quartz);
//            holder.typeEditText.setFocusableInTouchMode(true);
//            holder.optionSpinner.setEnabled(true);
//        } else {
//            holder.itemView.setBackgroundResource(R.drawable.draw_16sdp_border_quartz);
//            holder.typeEditText.setFocusableInTouchMode(false);
//            holder.optionSpinner.setEnabled(false);
//        }
//
//        try {
//            if () {
//                holder.typeEditText.setVisibility(View.VISIBLE);
//                holder.optionSpinner.setVisibility(View.GONE);
//                holder.arrowImageView.setVisibility(View.GONE);
//
//                holder.typeEditText.setText();
//                holder.typeEditText.setHint();
//                holder.typeEditText.setInputType(InputType.TYPE_CLASS_NUMBER);
//
//                holder.typeEditText.setOnKeyListener((v, keyCode, event) -> {
//                    result = holder.typeEditText.getText().toString().trim();
//                    doWork(holder, result, "editText");
//                    return false;
//                });
//            } else if () {
//                holder.typeEditText.setVisibility(View.VISIBLE);
//                holder.optionSpinner.setVisibility(View.GONE);
//                holder.arrowImageView.setVisibility(View.GONE);
//
//                holder.typeEditText.setText();
//                holder.typeEditText.setHint();
//                holder.typeEditText.setInputType(InputType.TYPE_CLASS_TEXT);
//
//                holder.typeEditText.setOnKeyListener((v, keyCode, event) -> {
//                    result = holder.typeEditText.getText().toString().trim();
//                    doWork(holder, result, "editText");
//                    return false;
//                });
//            } else if () {
//                holder.typeEditText.setVisibility(View.GONE);
//                holder.optionSpinner.setVisibility(View.VISIBLE);
//                holder.arrowImageView.setVisibility(View.VISIBLE);
//
//                ArrayAdapter<String> adapter = new ArrayAdapter<String>(activity, R.layout.spinner_background) {
//
//                    @NonNull
//                    @Override
//                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
//                        View view = super.getView(position, convertView, parent);
//
//                        if (position == getCount()) {
//                            ((TextView) view.findViewById(R.id.spinner_background_textView)).setText("");
//                            ((TextView) view.findViewById(R.id.spinner_background_textView)).setHint(getItem(getCount()));
//                        } else {
//                            ((TextView) view.findViewById(R.id.spinner_background_textView)).setText(getItem(position));
//                            ((TextView) view.findViewById(R.id.spinner_background_textView)).setHint("");
//                        }
//
//                        return view;
//                    }
//
//                    @Override
//                    public int getCount() {
//                        return super.getCount() - 1;
//                    }
//
//                };
//
//                for (int j = 0; j < value.length(); j++) {
//                    adapter.add("value");
//                }
//                adapter.add("");
//
//                adapter.setDropDownViewResource(R.layout.spinner_dropdown);
//                holder.optionSpinner.setAdapter(adapter);
//                holder.optionSpinner.setSelection(adapter.getCount());
//                holder.optionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                    @Override
//                    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                        if (adapter.getCount() != position) {
//                            int pos = position + 1;
//                            result = String.valueOf(pos);
//                            doWork(holder, result, "spinner");
//                        }
//                    }
//
//                    @Override
//                    public void onNothingSelected(AdapterView<?> parent) {
//
//                    }
//                });
//            }
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }

    }

    @Override
    public int getItemCount() {
        return 3;
    }

    private void initializer(View view) {
        viewModel = new ViewModelProvider((FragmentActivity) activity).get(SampleViewModel.class);

        handler = new Handler();
    }

    public void setValue(ArrayList<Model> value) {
        this.values = value;
        notifyDataSetChanged();
    }

    public void setEditable(boolean editable) {
        this.editable = editable;
        notifyDataSetChanged();
    }

    private void doWork(@NonNull DetailSampleHolder holder, String result, String type) {
//        if (type.equals("editText")) {
//            holder.updateProgressBar.setVisibility(View.VISIBLE);
//            holder.typeEditText.setClickable(false);
//        } else {
//            holder.updateProgressBar.setVisibility(View.VISIBLE);
//            holder.arrowImageView.setVisibility(View.GONE);
//            holder.optionSpinner.setClickable(false);
//        }
//
//        try {
//            viewModel.update();
//            observeWork(holder, type);
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
    }

    private void observeWork(@NonNull DetailSampleHolder holder, String type) {
//        SampleRepository.workStateDetail.observe((LifecycleOwner) this, integer -> {
//            if (SampleRepository.work.equals("update")) {
//                if (integer == 1) {
//                    if (type.equals("editText")) {
//                        holder.updateProgressBar.setVisibility(View.GONE);
//                        holder.typeEditText.setClickable(true);
//                    } else {
//                        holder.updateProgressBar.setVisibility(View.GONE);
//                        holder.arrowImageView.setVisibility(View.VISIBLE);
//                        holder.optionSpinner.setClickable(true);
//                    }
//
//                    SampleRepository.workStateDetail.removeObservers((LifecycleOwner) this);
//                } else if (integer == 0) {
//                    if (type.equals("editText")) {
//                        holder.updateProgressBar.setVisibility(View.GONE);
//                        holder.typeEditText.setClickable(true);
//                    } else {
//                        holder.updateProgressBar.setVisibility(View.GONE);
//                        holder.arrowImageView.setVisibility(View.VISIBLE);
//                        holder.optionSpinner.setClickable(true);
//                    }
//
//                    Toast.makeText(activity, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
//                    SampleRepository.workStateDetail.removeObservers((LifecycleOwner) this);
//                } else if (integer == -2) {
//                    if (type.equals("editText")) {
//                        holder.updateProgressBar.setVisibility(View.GONE);
//                        holder.typeEditText.setClickable(true);
//                    } else {
//                        holder.updateProgressBar.setVisibility(View.GONE);
//                        holder.arrowImageView.setVisibility(View.VISIBLE);
//                        holder.optionSpinner.setClickable(true);
//                    }
//
//                    Toast.makeText(activity, "" + ExceptionManager.farsi_message, Toast.LENGTH_SHORT).show();
//                    SampleRepository.workStateDetail.removeObservers((LifecycleOwner) this);
//                }
//            }
//        });
    }

    public class DetailSampleHolder extends RecyclerView.ViewHolder {

        public EditText typeEditText;
        public Spinner optionSpinner;
        public ImageView arrowImageView;
        public ProgressBar updateProgressBar;

        public DetailSampleHolder(View view) {
            super(view);
            typeEditText = view.findViewById(R.id.single_item_detail_sample_type_editText);
            optionSpinner = view.findViewById(R.id.single_item_detail_sample_option_spinner);
            arrowImageView = view.findViewById(R.id.single_item_detail_sample_arrow_imageView);
            updateProgressBar = view.findViewById(R.id.single_item_detail_sample_update_progressBar);
        }
    }

}