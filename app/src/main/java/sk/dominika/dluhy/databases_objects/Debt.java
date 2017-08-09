package sk.dominika.dluhy.databases_objects;

import android.support.design.widget.TextInputEditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Debt {
    private String id_debt;
//    private String id_me;
//    private String id_friend;
    private String id_who;
    private String id_toWhom;
    private String name_who;
    private String name_toWhom;
    private float sum;
    private String note;
    private String dateOfAlert;
    private String timeOfAlert;
    //TODO: casy a datumy na SimpleDateFormat

    public Debt() {}

    public Debt(String id_friend, TextInputEditText edTxtSum, TextInputEditText edTxtNote,
                TextInputEditText edTxtDateA, TextInputEditText edTxtTimeA, boolean heOwesMe) {
        this.id_who = id_friend;
        this.sum = Float.parseFloat(edTxtSum.getText().toString());
        this.note = edTxtNote.getText().toString();
        this.dateOfAlert = edTxtDateA.getText().toString();
        this.timeOfAlert = edTxtTimeA.getText().toString();
    }

    public Debt(String id_friend, String name, float sum, String note, boolean heOwesMe) {
        this.id_who = id_friend;
        this.sum = sum;
        this.note = note;
    }

    public Debt(String id_debt, String id_who, String id_toWhom, String name_who,
                String name_toWhom, float sum, String note, String dateOfAlert, String timeOfAlert) {
        this.id_debt = id_debt;
//        this.id_me = id_me;
//        this.id_friend = id_friend;
        this.id_who = id_who;
        this.id_toWhom = id_toWhom;
        this.name_who = name_who;
        this.name_toWhom = name_toWhom;
        this.sum = sum;
        this.note = note;
        this.dateOfAlert = dateOfAlert;
        this.timeOfAlert = timeOfAlert;
    }

    public String getId_debt() { return this.id_debt; }

//    public String getId_me() {
//        return id_me;
//    }

//    public String getId_friend() {return id_friend;}

    public float getSum(){
        return this.sum;
    }

    public String getNote() {
        return this.note;
    }

    public String getId_who() { return this.id_who; }

    public String getId_toWhom() { return this.id_toWhom; }

    public String getName_who() {return name_who;}

    public String getName_toWhom() {return name_toWhom;}

    public String getDateOfAlert(){
        return this.dateOfAlert;
    }

    public String getTimeOfAlert(){
        return this.timeOfAlert;
    }

    public static List<Debt> myDebts = new ArrayList<Debt>();
}
