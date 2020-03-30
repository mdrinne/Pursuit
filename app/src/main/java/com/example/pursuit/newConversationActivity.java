package com.example.pursuit;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.pursuit.models.Company;
import com.example.pursuit.models.Student;

public class newConversationActivity extends AppCompatActivity {

    Student currentStudent = null;
    Company currentCompany = null;
    String  currentRole = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_conversation);

        findAndSetCurrentUser();
    }

    private void findAndSetCurrentUser() {
        if (((PursuitApplication) this.getApplication()).getCurrentStudent() != null) {
            currentStudent = ((PursuitApplication) this.getApplication()).getCurrentStudent();
        } else {
            currentCompany = ((PursuitApplication) this.getApplication()).getCurrentCompany();
        }
        currentRole = ((PursuitApplication) this.getApplication()).getRole();
    }
}
