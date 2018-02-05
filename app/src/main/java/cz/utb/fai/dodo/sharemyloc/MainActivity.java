package cz.utb.fai.dodo.sharemyloc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    ArrayList<Person> friends;
    private Shared shared;
    private SharedPreferences sharedPref;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        friends = new ArrayList<Person>();

        sharedPref = getSharedPreferences(shared.SHARED_FILE, MODE_PRIVATE);
        shared = new Shared(sharedPref);

        //Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        //setSupportActionBar(toolbar);

        DatabaseReference rootRef = FirebaseDatabase.getInstance().getReference();
        DatabaseReference usersdRef = rootRef.child("names");
        ValueEventListener eventListener = new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                for(DataSnapshot ds : dataSnapshot.getChildren()) {
                    Person person = ds.getValue(Person.class);
                    friends.add(person);
                }
                shared.addListToShared(friends);
                refreshRecycleView();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {}
        };

        usersdRef.addListenerForSingleValueEvent(eventListener);
/*
        Button btn = (Button) findViewById(R.id.button2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NameSurnameActivity.class));
            }
        });*/
        refreshRecycleView();
    }

    private void refreshRecycleView() {

        List<String> people = Person.listOfPersonsToFullNames(friends);
        ArrayList<ListItem> listItems = new ArrayList<>();

        recyclerView = (RecyclerView) findViewById(R.id.friends_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, LinearLayoutManager.VERTICAL));

        //// TODO: 29.01.2018 pridat boolean do Person kvoli chackboxu
        for (String fullName : people) {
            ListItem listItem = new ListItem(fullName,false);
            listItems.add(listItem);
        }

        adapter = new MyAdapter(listItems);
        recyclerView.setAdapter(adapter);
    }
}
