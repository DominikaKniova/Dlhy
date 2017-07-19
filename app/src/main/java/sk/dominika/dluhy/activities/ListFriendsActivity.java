package sk.dominika.dluhy.activities;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import sk.dominika.dluhy.R;

public class ListFriendsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_friends);
        overridePendingTransition(R.animator.left_in, R.animator.left_out);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.animator.left_in, R.animator.left_out);
    }
}
