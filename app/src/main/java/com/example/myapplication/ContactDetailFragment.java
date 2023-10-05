package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

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
    private Contact editedContact;
    private EditText editName;
    private EditText editEmail;
    private EditText editMobile;
    private TextView nameTextView;
    private TextView emailTextView;
    private TextView mobileTextView;
    private Button editButton;
    private Button saveButton;
    private Button cancelButton;

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

        nameTextView = view.findViewById(R.id.contactName);
        emailTextView = view.findViewById(R.id.contactEmail);
        mobileTextView = view.findViewById(R.id.contactMobile);

        nameTextView.setText(name);
        emailTextView.setText(email);
        mobileTextView.setText(mobile);

        editButton = view.findViewById(R.id.editButton);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showEditFields();
            }
        });

        Button deleteButton = view.findViewById(R.id.deleteButton);
        deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteContact();
            }
        });

        editName = view.findViewById(R.id.editName);
        editEmail = view.findViewById(R.id.editEmail);
        editMobile = view.findViewById(R.id.editMobile);

        saveButton = view.findViewById(R.id.saveButton);
        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveContact();
            }
        });

        cancelButton = view.findViewById(R.id.cancelButton);
        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelEditing();
            }
        });

        return view;
    }

    private void showEditFields() {
        editName.setVisibility(View.VISIBLE);
        editEmail.setVisibility(View.VISIBLE);
        editMobile.setVisibility(View.VISIBLE);
        saveButton.setVisibility(View.VISIBLE);
        cancelButton.setVisibility(View.VISIBLE);

        nameTextView.setVisibility(View.GONE);
        emailTextView.setVisibility(View.GONE);
        mobileTextView.setVisibility(View.GONE);
        editButton.setVisibility(View.GONE);

        editName.setText(name);
        editEmail.setText(email);
        editMobile.setText(mobile);
    }

    private void saveContact() {
        String editedName = editName.getText().toString();
        String editedEmail = editEmail.getText().toString();
        String editedMobile = editMobile.getText().toString();

        editedContact = new Contact(editedName, editedEmail, editedMobile);

        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.updateContactDetail(editedContact);
            Toast.makeText(mainActivity, "Contact Updated", Toast.LENGTH_SHORT).show();
        }

        nameTextView.setText(editedName);
        emailTextView.setText(editedEmail);
        mobileTextView.setText(editedMobile);

        cancelEditing();  // Reuse the cancelEditing method to hide the edit fields and buttons
    }

    private void deleteContact() {
        MainActivity mainActivity = (MainActivity) getActivity();
        if (mainActivity != null) {
            mainActivity.updateContactDetail(null);
        }
    }

    private void cancelEditing() {
        editName.setVisibility(View.GONE);
        editEmail.setVisibility(View.GONE);
        editMobile.setVisibility(View.GONE);
        saveButton.setVisibility(View.GONE);
        cancelButton.setVisibility(View.GONE);

        nameTextView.setVisibility(View.VISIBLE);
        emailTextView.setVisibility(View.VISIBLE);
        mobileTextView.setVisibility(View.VISIBLE);
        editButton.setVisibility(View.VISIBLE);
    }
}
