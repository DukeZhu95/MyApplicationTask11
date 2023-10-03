package com.example.myapplication;

import android.os.Parcel;
import android.os.Parcelable;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Objects;

@Entity
public class Contact implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    public long id;

    public String name;
    public String email;
    public String mobile;

    public Contact(String name, String email, String mobile) {
        this.name = name;
        this.email = email;
        this.mobile = mobile;
    }

    protected Contact(Parcel in) {
        id = in.readLong();
        name = in.readString();
        email = in.readString();
        mobile = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(id);
        dest.writeString(name);
        dest.writeString(email);
        dest.writeString(mobile);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Contact> CREATOR = new Creator<Contact>() {
        @Override
        public Contact createFromParcel(Parcel in) {
            return new Contact(in);
        }

        @Override
        public Contact[] newArray(int size) {
            return new Contact[size];
        }
    };

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        Contact contact = (Contact) obj;
        return Objects.equals(name, contact.name) &&
                Objects.equals(email, contact.email) &&
                Objects.equals(mobile, contact.mobile);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, email, mobile);
    }
}
