package com.example.myapplication;

import androidx.lifecycle.Observer;

import android.content.res.Configuration;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity implements AdapterView.OnItemClickListener {
    private ArrayList<Contact> contacts = new ArrayList<Contact>();
    private ArrayList<Contact> filteredContacts = new ArrayList<Contact>();
    private ArrayAdapter<Contact> adapter;
    private ListView contactListView;
    private ContactRepository contactRepository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ContactRoomDatabase.getDatabase(this);
        setContentView(R.layout.activity_main);

        // Setup Adapter
        contactListView = (ListView) findViewById(R.id.contactsListView);
        EditText searchBar = findViewById(R.id.searchBar);  // Assuming you have added the EditText in your XML

        if (contactListView != null) {
            adapter = new ArrayAdapter<Contact>(this, android.R.layout.simple_list_item_1, filteredContacts);
            contactListView.setAdapter(adapter);
            contactListView.setOnItemClickListener(this);

            // Add some Contacts
            contacts.add(new Contact("Duke Zhu", "zlnirvana4@gmail.com", "0224543833"));
            contacts.add(new Contact("Jerry Ling", "574918962@qq.com", "0211234567"));
            contacts.add(new Contact("Alex An", "AnQian$888@yeah.net", "0279031784" ));
            filteredContacts.addAll(contacts);  // Initially, show all contacts

            // Add TextWatcher for the search bar
            searchBar.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                    // Do nothing
                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {
                    Log.d("Search", "Text changed: " + s.toString());
                    filterContacts(s.toString());
                }

                @Override
                public void afterTextChanged(Editable s) {
                    // Do nothing
                }
            });

        }

        // Adding Fragment
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.fragment_container, new ContactListFragment())
                    .commit();
        }

        // Create a ContactRepository and register an observer
        contactRepository = new ContactRepository(this);
        contactRepository.getAllContacts().observe(this, new Observer<List<Contact>>() {
            @Override
            public void onChanged(List<Contact> updatedContacts) {
                Log.d("MainActivity", "Contacts updated: " + updatedContacts.toString());
                // update the contacts list when the database changes
                contacts.clear();
                contacts.addAll(updatedContacts);
                filterContacts(searchBar.getText().toString());  // Update the filtered list based on the current search query
            }
        });
    }



    private void filterContacts(String query) {
        Toast.makeText(this, "Filtering with query: " + query, Toast.LENGTH_SHORT).show();
        filteredContacts.clear();
        for (Contact contact : contacts) {
            if (contact.name.toLowerCase().contains(query.toLowerCase())) {
                filteredContacts.add(contact);
            }
        }
        adapter.notifyDataSetChanged();
        Toast.makeText(this, "Filtered contacts: " + filteredContacts.toString(), Toast.LENGTH_SHORT).show();
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
            // Update the existing contact in the database
            Contact existingContact = contacts.get(existingIndex);
            existingContact.email = email;
            existingContact.mobile = mobile;
            contactRepository.update(existingContact);
            String message = "Updated contact for " + name + "\nEmail: " + email + "\nMobile: " + mobile;
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else {
            // Insert the new contact into the database
            contactRepository.insert(newContact);
            String message = "Saved contact for " + name + "\nEmail: " + email + "\nMobile: " + mobile;
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        }
    }

    public void deleteContact(View view) {
        EditText nameField = (EditText) findViewById(R.id.name);
        String name = nameField.getText().toString();

        // Find the contact in the contacts ArrayList
        Contact contactToDelete = null;
        for (Contact contact : contacts) {
            if (contact.name.equals(name)) {
                contactToDelete = contact;
                break;
            }
        }

        if (contactToDelete != null) {
            // Delete the contact from the database
            contactRepository.delete(contactToDelete);
            String message = "Deleted contact for " + name;
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show();
        } else {
//            Toast.makeText(this, "Contact not found!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Contact contact = (Contact) parent.getAdapter().getItem(position);
        int orientation = getResources().getConfiguration().orientation;

        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            // In landscape mode, update the Fragment content
            ContactDetailFragment contactDetailFragment = new ContactDetailFragment();

            // Pass the selected contact to the Fragment
            Bundle args = new Bundle();
            args.putString(ContactDetailFragment.ARG_NAME, contact.name);
            args.putString(ContactDetailFragment.ARG_EMAIL, contact.email);
            args.putString(ContactDetailFragment.ARG_MOBILE, contact.mobile);
            contactDetailFragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, contactDetailFragment)
                    .commit();
        } else {
            // In portrait mode, fill the edit contact form with the contact details
            ((EditText) findViewById(R.id.name)).setText(contact.name);
            ((EditText) findViewById(R.id.email)).setText(contact.email);
            ((EditText) findViewById(R.id.mobile)).setText(contact.mobile);
//            Toast.makeText(parent.getContext(), "Clicked " + contact, Toast.LENGTH_SHORT).show();
        }
    }

    public void updateContactDetail(Contact editedContact) {
        if (editedContact != null) {
            // 更新联系人列表
            int index = -1;
            for (int i = 0; i < contacts.size(); i++) {
                if (contacts.get(i).name.equals(editedContact.name)) {
                    index = i;
                    break;
                }
            }

            if (index != -1) {
                // 更新联系人信息
                contacts.set(index, editedContact);
                filterContacts(((EditText) findViewById(R.id.searchBar)).getText().toString());
            } else {
                // 添加新联系人
                contacts.add(editedContact);
                filterContacts(((EditText) findViewById(R.id.searchBar)).getText().toString());
            }

            // 更新编辑联系人表单的内容
            ((EditText) findViewById(R.id.name)).setText(editedContact.name);
            ((EditText) findViewById(R.id.email)).setText(editedContact.email);
            ((EditText) findViewById(R.id.mobile)).setText(editedContact.mobile);
        } else {
            // 清空编辑联系人表单的内容
            ((EditText) findViewById(R.id.name)).setText("");
            ((EditText) findViewById(R.id.email)).setText("");
            ((EditText) findViewById(R.id.mobile)).setText("");
        }
    }
}
