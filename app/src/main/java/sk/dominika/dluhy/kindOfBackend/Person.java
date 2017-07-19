package sk.dominika.dluhy.kindOfBackend;

import android.support.design.widget.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class Person {
    private String name;
    private String email;
    private String password;

    public static List<Person> listOfAccounts = new ArrayList<Person>();

    public Person(TextInputEditText edTxtName, TextInputEditText edTxtEmail, TextInputEditText edTxtPassword) {
        this.name = edTxtName.getText().toString();
        this.email = edTxtEmail.getText().toString();
        this.password = edTxtPassword.getText().toString();
    }

    public String getName() {
        return this.name;
    }
}
