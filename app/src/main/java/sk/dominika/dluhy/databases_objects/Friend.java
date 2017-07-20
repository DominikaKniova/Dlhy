package sk.dominika.dluhy.databases_objects;

import android.support.design.widget.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class Friend {
    private String name;
    private String email;

    public Friend(TextInputEditText edTxtName, TextInputEditText edTxtEmail) {
        this.name = edTxtName.getText().toString();
        this.email = edTxtEmail.getText().toString();
    }

    public Friend(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public static List<Friend> myFriends = new ArrayList<Friend>();
}
