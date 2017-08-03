package sk.dominika.dluhy.databases_objects;

import android.support.design.widget.TextInputEditText;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class Debt {
    private String id_debt;
    private String id_who;
    private String id_toWhom;
    //private String friend_name;
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


    public float getSum(){
        return this.sum;
    }

    public String getNote() {
        return this.note;
    }

    public String getId_who() { return this.id_who; }

    public void setId_debt(String id) {
        this.id_debt = id;
    }

    public String getDateOfAlert(){
        return this.dateOfAlert;
    }

    public String getTimeOfAlert(){
        return this.timeOfAlert;
    }

    public static List<Debt> myDebts = new ArrayList<Debt>();
}
