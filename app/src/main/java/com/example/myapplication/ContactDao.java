package com.example.myapplication;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ContactDao { @Insert
    void insert(Contact contact);
    @Update
    void update(Contact contact);
    @Delete
    void delete(Contact contact);
    @Query("SELECT * from Contact ORDER BY name")
    LiveData<List<Contact>> getAllContacts();
}