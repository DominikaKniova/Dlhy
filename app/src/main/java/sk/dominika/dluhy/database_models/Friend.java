package sk.dominika.dluhy.database_models;

import java.util.ArrayList;
import java.util.List;

/**
 * Model for a friend.
 */
public class Friend {
    private String firstName;
    private String lastName;
    private String email;
    private String id;

    public  Friend() {}

    public Friend(String firstname, String lastname, String email) {
        this.firstName = firstname;
        this.lastName = lastname;
        this.email = email;
    }

    public Friend(String firstname,String id){
        this.firstName = firstname;
        this.id = id;
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

    public static List<Friend> myFriends = new ArrayList<Friend>();
}
