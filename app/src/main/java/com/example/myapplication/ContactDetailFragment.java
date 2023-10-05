package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ContactDetailFragment extends Fragment {

    public static final String ARG_NAME = "name";
    public static final String ARG_EMAIL = "email";
    public static final String ARG_MOBILE = "mobile";

    private String name;
    private String email;
    private String mobile;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            name = getArguments().getString(ARG_NAME);
            email = getArguments().getString(ARG_EMAIL);
            mobile = getArguments().getString(ARG_MOBILE);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact_detail, container, false);

        TextView nameTextView = view.findViewById(R.id.contactName);
        TextView emailTextView = view.findViewById(R.id.contactEmail);
        TextView mobileTextView = view.findViewById(R.id.contactMobile);

        nameTextView.setText(name);
        emailTextView.setText(email);
        mobileTextView.setText(mobile);

        return view;
    }
}
