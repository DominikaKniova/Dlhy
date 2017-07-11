package sk.dominika.dluhy;

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

    public String getName() {
        return this.name;
    }

    public static List<AddFriend> myFriends = new ArrayList<AddFriend>();
}
