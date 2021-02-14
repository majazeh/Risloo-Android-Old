package com.majazeh.risloo.Views.Fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.majazeh.risloo.R;
import com.majazeh.risloo.Utils.Widgets.ItemDecorateRecyclerView;
import com.majazeh.risloo.Views.Activities.EditAccountActivity;
import com.majazeh.risloo.Views.Adapters.ListAdapter;

import org.json.JSONException;

public class EditCryptoFragment extends Fragment {

    // Adapters
    private ListAdapter publicListAdapter, privateListAdapter;

    // Vars
    private String publicKey = "", privateKey = "";

    // Objects
    private Activity activity;

    // Widgets
    private RecyclerView publicKeyRecyclerView, privateKeyRecyclerView;
    private EditText publicKeyEditText, privateKeyEditText;
    private Button editButton;

    public EditCryptoFragment(@NonNull Activity activity) {
        this.activity = activity;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup viewGroup, @Nullable Bundle savedInstanceState) {
        View view = LayoutInflater.from(activity).inflate(R.layout.fragment_edit_cryto, viewGroup, false);

        initializer(view);

        detector();

        listener();

        setData();

        return view;
    }

    private void initializer(View view) {
        publicListAdapter = new ListAdapter(getActivity());
        privateListAdapter = new ListAdapter(getActivity());

        publicKeyRecyclerView = view.findViewById(R.id.edit_crypto_publicKey_recyclerView);
        publicKeyRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._24sdp), (int) getResources().getDimension(R.dimen._16sdp), (int) getResources().getDimension(R.dimen._24sdp)));
        publicKeyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        publicKeyRecyclerView.setHasFixedSize(true);

        privateKeyRecyclerView = view.findViewById(R.id.edit_crypto_privateKey_recyclerView);
        privateKeyRecyclerView.addItemDecoration(new ItemDecorateRecyclerView("verticalLayout", (int) getResources().getDimension(R.dimen._24sdp), (int) getResources().getDimension(R.dimen._16sdp), (int) getResources().getDimension(R.dimen._24sdp)));
        privateKeyRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false));
        privateKeyRecyclerView.setHasFixedSize(true);

        publicKeyEditText = view.findViewById(R.id.edit_crypto_publicKey_editText);
        privateKeyEditText = view.findViewById(R.id.edit_crypto_privateKey_editText);

        editButton = view.findViewById(R.id.edit_crypto_button);
    }

    private void detector() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.LOLLIPOP) {
            editButton.setBackgroundResource(R.drawable.draw_16sdp_solid_primary_ripple_primarydark);
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private void listener() {
        publicKeyEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!publicKeyEditText.hasFocus()) {
                    if (((EditAccountActivity) getActivity()).controlEditText.input() != null && ((EditAccountActivity) getActivity()).controlEditText.input().hasFocus()) {
                        ((EditAccountActivity) getActivity()).controlEditText.clear(getActivity(), ((EditAccountActivity) getActivity()).controlEditText.input());
                    }

                    ((EditAccountActivity) getActivity()).controlEditText.focus(publicKeyEditText);
                    ((EditAccountActivity) getActivity()).controlEditText.select(publicKeyEditText);
                }
            }
            return false;
        });

        privateKeyEditText.setOnTouchListener((v, event) -> {
            if (MotionEvent.ACTION_UP == event.getAction()) {
                if (!privateKeyEditText.hasFocus()) {
                    if (((EditAccountActivity) getActivity()).controlEditText.input() != null && ((EditAccountActivity) getActivity()).controlEditText.input().hasFocus()) {
                        ((EditAccountActivity) getActivity()).controlEditText.clear(getActivity(), ((EditAccountActivity) getActivity()).controlEditText.input());
                    }

                    ((EditAccountActivity) getActivity()).controlEditText.focus(privateKeyEditText);
                    ((EditAccountActivity) getActivity()).controlEditText.select(privateKeyEditText);
                }
            }
            return false;
        });

        editButton.setOnClickListener(v -> {
            if (((EditAccountActivity) getActivity()).controlEditText.input() != null && ((EditAccountActivity) getActivity()).controlEditText.input().hasFocus()) {
                ((EditAccountActivity) getActivity()).controlEditText.clear(getActivity(), ((EditAccountActivity) getActivity()).controlEditText.input());
            }

            if (publicKeyEditText.length() == 0) {
                ((EditAccountActivity) getActivity()).controlEditText.error(getActivity(), publicKeyEditText);
            }

            if (publicKeyEditText.length() != 0) {
                ((EditAccountActivity) getActivity()).controlEditText.clear(getActivity(), publicKeyEditText);

                doWork();
            }
        });
    }

    private void setData() {
        if (((EditAccountActivity) getActivity()).authViewModel.getPublicKey().equals("")) {
            publicKeyEditText.setHint(getResources().getString(R.string.EditCryptoPublicKeyHint));
        } else {
            publicKey = ((EditAccountActivity) getActivity()).authViewModel.getPublicKey();
            publicKeyEditText.setText(publicKey);
        }

        if (((EditAccountActivity) getActivity()).authViewModel.getPrivateKey().equals("")) {
            privateKeyEditText.setHint(getResources().getString(R.string.EditCryptoPrivateKeyHint));
        } else {
            privateKey = ((EditAccountActivity) getActivity()).authViewModel.getPrivateKey();
            privateKeyEditText.setText(privateKey);
        }

        try {
            publicListAdapter.setList(((EditAccountActivity) getActivity()).authViewModel.getKeyAsset("public"), "EditCryptoPublic");
            privateListAdapter.setList(((EditAccountActivity) getActivity()).authViewModel.getKeyAsset("private"), "EditCryptoPrivate");
            publicKeyRecyclerView.setAdapter(publicListAdapter);
            privateKeyRecyclerView.setAdapter(privateListAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void doWork() {
        publicKey = publicKeyEditText.getText().toString().trim();
        privateKey = privateKeyEditText.getText().toString().trim();

        ((EditAccountActivity) getActivity()).authViewModel.setPublicKey(publicKey);
        ((EditAccountActivity) getActivity()).authViewModel.setPrivateKey(privateKey);
    }

}