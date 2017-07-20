package sk.dominika.dluhy.databases_objects;

import android.support.design.widget.TextInputEditText;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;

public class Debt {
    private String name;
    private String sum;
    private String note;
    private SimpleDateFormat dateOfCreation;
    private SimpleDateFormat timeOfCreation;
    private SimpleDateFormat dateOfAlert;
    private SimpleDateFormat timeOfAlert;

    public Debt(TextView edTxtName, TextInputEditText edTxtSum, TextInputEditText edTxtNote) {
        this.name = edTxtName.getText().toString();
        this.sum = edTxtSum.getText().toString();
        this.note = edTxtNote.getText().toString();
    }

    public Debt(String name, String sum, String note) {
        this.name = name;
        this.sum = sum;
        this.note = note;
    }

    public String getName() {
        return this.name;
    }

    public String getSum(){
        return this.sum;
    }

    public String getNote() {
        return this.note;
    }

    public SimpleDateFormat getDateOfCreation(){
        return this.dateOfCreation;
    }

    public SimpleDateFormat getTimeOfCreation(){
        return this.timeOfCreation;
    }

    public SimpleDateFormat getDateOfAlert(){
        return this.dateOfAlert;
    }

    public SimpleDateFormat getTimeOfAlert(){
        return this.timeOfAlert;
    }

    public static List<Debt> myDebts = new ArrayList<Debt>();
}
