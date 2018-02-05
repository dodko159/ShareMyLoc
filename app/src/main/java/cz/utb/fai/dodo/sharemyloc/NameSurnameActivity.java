package cz.utb.fai.dodo.sharemyloc;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class NameSurnameActivity extends AppCompatActivity {

    private Button saveBtn;
    private EditText nameEditText, surnameEditText;
    private ProgressBar progressBar;
    private FirebaseDatabase database;
    private FirebaseAuth auth;
    private  DatabaseReference myRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_name_surname);

        nameEditText = (EditText) findViewById(R.id.textEditName);
        surnameEditText = (EditText) findViewById(R.id.textEditSurname);
        saveBtn = (Button) findViewById(R.id.btn_save);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        database = FirebaseDatabase.getInstance();
        myRef = database.getReference("names");

        auth = FirebaseAuth.getInstance();

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                progressBar.setVisibility(View.VISIBLE);

                String uid = auth.getCurrentUser().getUid();
                String name = nameEditText.getText().toString().trim();
                String surname = surnameEditText.getText().toString();

                if(name == null || name.trim().isEmpty()){
                    Toast.makeText(getApplicationContext(), "Name must not be empty!", Toast.LENGTH_SHORT).show();
                    return;
                }

                Person me = new Person(uid,name,surname);

                myRef.child(uid).setValue(me);

                progressBar.setVisibility(View.INVISIBLE);

                Toast.makeText(getApplicationContext(), "Name saved.", Toast.LENGTH_SHORT).show();

                startActivity(new Intent(NameSurnameActivity.this, MainActivity.class));
                finish();
            }
        });

    }
}
