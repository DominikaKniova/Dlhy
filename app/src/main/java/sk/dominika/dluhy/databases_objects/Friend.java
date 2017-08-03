package sk.dominika.dluhy.databases_objects;

import android.support.design.widget.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class Friend {
    //private long id;
    private String firstName;
    private String lastName;
    private String email;
    private String id;
    private float overallDebt;

    public  Friend() {}

    public Friend(TextInputEditText firstname, TextInputEditText lastname, TextInputEditText email) {
        this.firstName = firstname.getText().toString();
        this.lastName = lastname.getText().toString();
        this.email = email.getText().toString();
        this.overallDebt = 0;
    }

    public Friend(String firstname, String lastname, String email) {
        this.firstName = firstname;
        this.lastName = lastname;
        this.email = email;
        this.overallDebt = 0;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public String getLastName() { return this.lastName; }

    public String getEmail() {
        return this.email;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getId() { return this.id; }

//    public void setKey(String key) {
//        this.key = key;
//    }

    //public String getKey() { return this.key; }

    public static List<Friend> myFriends = new ArrayList<Friend>();
}
