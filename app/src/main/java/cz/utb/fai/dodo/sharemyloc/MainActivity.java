package cz.utb.fai.dodo.sharemyloc;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
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
    private FirebaseAuth auth;
    private FirebaseAuth.AuthStateListener authListener;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        auth = FirebaseAuth.getInstance();

        friends = new ArrayList<Person>();
        recyclerView = (RecyclerView) findViewById(R.id.friends_view);

        sharedPref = getSharedPreferences(shared.SHARED_FILE, MODE_PRIVATE);
        shared = new Shared(sharedPref);

        startService(new Intent(MainActivity.this,PositionService.class));

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

        recyclerView.addOnItemTouchListener(new RecyclerItemClickListener(getApplicationContext(), recyclerView, new RecyclerItemClickListener.OnItemClickListener(){
            @Override
            public void onItemClick(View view, int position) {
                String uid = friends.get(position).getUid();
                String name = friends.get(position).getFullName();
                shared.saveStringToShared(Shared.SHARED_WATCHING,uid);
                shared.saveStringToShared(Shared.SHARED_NAME,name);
                startActivity(new Intent(MainActivity.this, MapsActivity.class));
            }
            @Override
            public void onLongItemClick(View view, int position) {
                //On Long press event here
            }
        }));

        authListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = firebaseAuth.getCurrentUser();
                if (user == null) {
                    // user auth state is changed - user is null
                    // launch login activity
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }
            }
        };
/*
        Button btn = (Button) findViewById(R.id.button2);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, NameSurnameActivity.class));
            }
        });*/
        requestPermission();
        refreshRecycleView();
    }

    private void requestPermission() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    101);
            return;
        }
    }

    private void refreshRecycleView() {

        List<String> people = Person.listOfPersonsToFullNames(friends);
        ArrayList<ListItem> listItems = new ArrayList<>();

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

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_account) {
            startActivity(new Intent(MainActivity.this, AccountActivity.class));
            return true;
        }else if(id == R.id.action_signout){
            //(new Intent(MainActivity.this, PositionService.class));
            auth.signOut();
            return true;
        }else if(id == R.id.action_quit){
            finish();
            //// TODO: 03.02.2018 pada to
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onStart() {
        super.onStart();
        auth.addAuthStateListener(authListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (authListener != null) {
            auth.removeAuthStateListener(authListener);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopService(new Intent(MainActivity.this, PositionService.class));
    }
}
