package sk.dominika.dluhy.database_models;

import android.support.design.widget.TextInputEditText;

import java.util.ArrayList;
import java.util.List;

public class Debt {
    private String id_debt;
    private String id_who;
    private String id_toWhom;
    private String name_who;
    private String name_toWhom;
    private float sum;
    private String note;
    private String dateOfAlert;
    private String timeOfAlert;
    private String isPaid;
    //isPaid = true if debt is paid
    //isPaid = false if debt is not paid

    public Debt() {}

    public Debt(String id_debt, String id_who, String id_toWhom, String name_who,
                String name_toWhom, float sum, String note, String dateOfAlert, String timeOfAlert, String isPaid) {
        this.id_debt = id_debt;
        this.id_who = id_who;
        this.id_toWhom = id_toWhom;
        this.name_who = name_who;
        this.name_toWhom = name_toWhom;
        this.sum = sum;
        this.note = note;
        this.dateOfAlert = dateOfAlert;
        this.timeOfAlert = timeOfAlert;
        this.isPaid = isPaid;
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

    public String getIsPaid() {
        return isPaid;
    }

    public void setIsPaid(String isPaid) {
        this.isPaid = isPaid;
    }

    public static List<Debt> myDebts = new ArrayList<Debt>();
}
