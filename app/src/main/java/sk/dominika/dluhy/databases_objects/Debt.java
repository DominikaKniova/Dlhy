package sk.dominika.dluhy.databases_objects;

import android.support.design.widget.TextInputEditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Debt {
    private String id_debt;
    private String id_friend;
    private String friend_name;
    private float sum;
    private String note;
    private String dateOfCreation;
    private String timeOfCreation;
    private String dateOfAlert;
    private String timeOfAlert;
    //TODO: casy a datumy na SimpleDateFormat

    public Debt() {}

    public Debt(String id_friend, TextView edTxtName, TextInputEditText edTxtSum, TextInputEditText edTxtNote,
                TextInputEditText edTxtDateA, TextInputEditText edTxtTimeA) {
        this.id_friend = id_friend;
        this.friend_name = edTxtName.getText().toString();
        this.sum = Float.parseFloat(edTxtSum.getText().toString());
        this.note = edTxtNote.getText().toString();
        this.dateOfAlert = edTxtDateA.getText().toString();
        this.timeOfAlert = edTxtTimeA.getText().toString();
    }

    public Debt(String id_friend, String name, float sum, String note) {
        this.id_friend = id_friend;
        this.friend_name = name;
        this.sum = sum;
        this.note = note;
    }

    public String getFriend_name() {
        return this.friend_name;
    }

    public float getSum(){
        return this.sum;
    }

    public String getNote() {
        return this.note;
    }

    public String getId_friend() { return this.id_friend; }

    public void setId_debt(String id) {
        this.id_debt = id;
    }

    public String getDateOfCreation(){
        return this.dateOfCreation;
    }

    public String getTimeOfCreation(){
        return this.timeOfCreation;
    }

    public String getDateOfAlert(){
        return this.dateOfAlert;
    }

    public String getTimeOfAlert(){
        return this.timeOfAlert;
    }

    public static List<Debt> myDebts = new ArrayList<Debt>();
}
