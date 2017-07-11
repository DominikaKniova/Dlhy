package sk.dominika.dluhy;

import android.support.design.widget.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

class Accounts {
    public static List<Person> listOfAccounts = new ArrayList<Person>();

}

public class Person {
    private String name;
    private String email;
    private String password;

    public Person(TextInputEditText edTxtName, TextInputEditText edTxtEmail, TextInputEditText edTxtPassword) {
        this.name = edTxtName.getText().toString();
        this.email = edTxtEmail.getText().toString();
        this.password = edTxtPassword.getText().toString();
    }

    public String getName() {
        return this.name;
    }
}
