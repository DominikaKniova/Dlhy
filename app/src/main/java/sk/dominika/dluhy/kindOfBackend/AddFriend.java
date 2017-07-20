package sk.dominika.dluhy.kindOfBackend;

import android.support.design.widget.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class AddFriend {
    private String name;
    private String email;

    public AddFriend(TextInputEditText edTxtName, TextInputEditText edTxtEmail) {
        this.name = edTxtName.getText().toString();
        this.email = edTxtEmail.getText().toString();
    }

    public AddFriend(String name, String email) {
        this.name = name;
        this.email = email;
    }

    public String getName() {
        return this.name;
    }

    public String getEmail() {
        return this.email;
    }

    public static List<AddFriend> myFriends = new ArrayList<AddFriend>();
}
