package sk.dominika.dluhy.databases_objects;

import android.support.design.widget.TextInputEditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Debt {
    private String id_friend;
    private String friend_name;
    private String sum;
    private String note;
    private String dateOfCreation;
    private String timeOfCreation;
    private String dateOfAlert;
    private String timeOfAlert;
    //TODO: casy a datumy na SimpleDateFormat

    public Debt(TextView edTxtName, TextInputEditText edTxtSum, TextInputEditText edTxtNote,
                TextInputEditText edTxtDateA, TextInputEditText edTxtTimeA) {
        this.friend_name = edTxtName.getText().toString();
        this.sum = edTxtSum.getText().toString();
        this.note = edTxtNote.getText().toString();
        this.dateOfAlert = edTxtDateA.getText().toString();
        this.timeOfAlert = edTxtTimeA.getText().toString();
    }

    public Debt(String name, String sum, String note) {
        this.friend_name = name;
        this.sum = sum;
        this.note = note;
    }

    public String getFriend_name() {
        return this.friend_name;
    }

    public String getSum(){
        return this.sum;
    }

    public String getNote() {
        return this.note;
    }

    public String getId_friend() { return this.id_friend; }

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
