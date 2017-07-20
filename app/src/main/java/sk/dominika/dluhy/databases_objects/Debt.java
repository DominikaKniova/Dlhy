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
    private String dateOfCreation;
    private String timeOfCreation;
    private String dateOfAlert;
    private String timeOfAlert;
    //TODO: casy a datumy na SimpleDateFormat

    public Debt(TextView edTxtName, TextInputEditText edTxtSum, TextInputEditText edTxtNote,
                TextInputEditText edTxtDateA, TextInputEditText edTxtTimeA) {
        this.name = edTxtName.getText().toString();
        this.sum = edTxtSum.getText().toString();
        this.note = edTxtNote.getText().toString();
        this.dateOfAlert = edTxtDateA.getText().toString();
        this.timeOfAlert = edTxtTimeA.getText().toString();
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
