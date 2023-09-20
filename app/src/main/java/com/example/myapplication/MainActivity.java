package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.nio.channels.AlreadyBoundException;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ArrayList<Contact> contacts = new ArrayList<Contact>();
    private ArrayAdapter<Contact> adapter;
    private ListView contactListView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Setup Adapter
        contactListView = (ListView) findViewById(R.id.contactsListView);

        if (contactListView != null) {
            adapter = new ArrayAdapter<Contact>(this, android.R.layout.simple_list_item_1, contacts);
            contactListView.setAdapter(adapter);
            contactListView.setOnItemClickListener(this);

            // Add some Contacts
            contacts.add(new Contact("Duke Zhu", "zlnirvana4@gmail.com", "0224543833"));
            contacts.add(new Contact("Jerry Liang", "574918962@qq.com", "0211234567"));
            contacts.add(new Contact("Alex An", "AnQian$888@yeah.net", "0279031784" ));
        }

    }

    public void saveContact(View view) {
        EditText nameField = (EditText) findViewById(R.id.name);
        EditText emailField = (EditText) findViewById(R.id.email);
        EditText mobileField = (EditText) findViewById(R.id.mobile);

        String name = nameField.getText().toString();
        String email = emailField.getText().toString();
        String mobile = mobileField.getText().toString();

        // Create a new Contact object
        Contact newContact = new Contact(name, email, mobile);

        // Check if the contact already exists in the contacts ArrayList
        int existingIndex = contacts.indexOf(newContact);
        if (existingIndex >= 0) {
            // Update the existing contact
            contacts.set(existingIndex, newContact);
            String message = "Updated contact for " + name + "\nEmail: " + email + "\nMobile: " + mobile;
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else {
            // Add the new contact
            contacts.add(newContact);
            String message = "Saved contact for " + name + "\nEmail: " + email + "\nMobile: " + mobile;
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }

        // Notify the adapter that the data set has changed
        if (adapter != null) {
            adapter.notifyDataSetChanged();
        }
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Contact contact = (Contact) parent.getAdapter().getItem(position);

        // Fill the edit contact form with the contact details
        ((EditText) findViewById(R.id.name)).setText(contact.name);
        ((EditText) findViewById(R.id.email)).setText(contact.email);
        ((EditText) findViewById(R.id.mobile)).setText(contact.mobile);

        Toast.makeText(parent.getContext(), "Clicked " + contact, Toast.LENGTH_SHORT).show();
    }


}

