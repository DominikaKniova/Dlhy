package sk.dominika.dluhy.databases_objects;

import android.support.design.widget.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class User {
    private String id;
    private String firstname;
    private String lastname;
    private String email;
    private String friends;

    public static List<User> listOfUsers = new ArrayList<User>();

    public User(String id, String firstname, String lastname, String email, String friends) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.friends = friends;
    }

    public String getFirstname() {
        return this.firstname;
    }

    public String getLastname() { return this.lastname; }

    public String getEmail() { return  this.email;}

    public void setId(String id) { this.id = id;}

    public String getId() {return this.id;}
}
