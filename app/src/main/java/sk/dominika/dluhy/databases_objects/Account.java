package sk.dominika.dluhy.databases_objects;

import android.support.design.widget.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class Account {
    private String name;
    private String email;
    private String password;

    public static List<Account> listOfAccounts = new ArrayList<Account>();

    public Account(TextInputEditText edTxtName, TextInputEditText edTxtEmail, TextInputEditText edTxtPassword) {
        this.name = edTxtName.getText().toString();
        this.email = edTxtEmail.getText().toString();
        this.password = edTxtPassword.getText().toString();
    }

    public String getName() {
        return this.name;
    }
}
