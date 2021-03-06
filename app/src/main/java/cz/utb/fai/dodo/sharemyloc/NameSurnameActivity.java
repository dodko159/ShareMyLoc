package cz.utb.fai.dodo.sharemyloc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.gson.internal.Primitives;

public class NameSurnameActivity extends AppCompatActivity {

    private Button saveBtn;
    private EditText nameEditText, surnameEditText;
    private ProgressBar progressBar;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private DatabaseReference myRef;
    private Shared shared;
    private SharedPreferences sharedPref;
    private String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_surname);

        nameEditText = (EditText) findViewById(R.id.textEditName);
        surnameEditText = (EditText) findViewById(R.id.textEditSurname);
        saveBtn = (Button) findViewById(R.id.btn_save);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);


        auth = FirebaseAuth.getInstance();

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("positions");

        uid = auth.getCurrentUser().getUid();

        sharedPref = getSharedPreferences(shared.SHARED_FILE, MODE_PRIVATE);
        shared = new Shared(sharedPref);

        if(sharedPref.getBoolean(Shared.FROM_ACCOUNT_ACTIVITY, false)){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }else {
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            myRef.child(uid).setValue(new LatLng(0,0));
        }

        myRef = database.getReference("names");

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                String name = nameEditText.getText().toString().trim();
                String surname = surnameEditText.getText().toString();

                if(name == null || name.trim().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Name must not be empty!", Toast.LENGTH_SHORT).show();
                    progressBar.setVisibility(View.INVISIBLE);
                    return;
                }

                Person me = new Person(uid,name,surname);

                myRef.child(uid).setValue(me);

                progressBar.setVisibility(View.INVISIBLE);

                Toast.makeText(getApplicationContext(), "Name saved.", Toast.LENGTH_SHORT).show();

                shared.saveBooleanToShared(Shared.FROM_ACCOUNT_ACTIVITY,false);

                startActivity(new Intent(NameSurnameActivity.this, MainActivity.class));
                finish();
            }
        });

    }


    @Override
    public boolean onSupportNavigateUp() {
        shared.saveBooleanToShared(Shared.FROM_ACCOUNT_ACTIVITY,false);
        return super.onSupportNavigateUp();
    }

}

//// TODO: 10.02.2018 add on back button  aby sa nastavil false  from_acc_act