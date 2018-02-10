package cz.utb.fai.dodo.sharemyloc;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.Gson;
import com.google.gson.internal.Streams;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import cz.utb.fai.dodo.sharemyloc.Person;

/**
 * Created by Dodo on 29.01.2018.
 */

public class Shared extends AppCompatActivity {
    public static final String SHARED_FRIENDS = "friends";
    public static final String SHARED_FILE = "shared_variables";
    public static final String FROM_ACCOUNT_ACTIVITY = "from_accont_activity";
    public static final String SHARED_POSITIONS = "positions";
    public static final String SHARED_POSITION = "position";
    public static final String SHARED_NAME = "name";

    private SharedPreferences sharedPref;
    private SharedPreferences.Editor editor;

    public Shared(SharedPreferences sharedPref) {
        this.sharedPref = sharedPref;
        editor = sharedPref.edit();
    }

    public void addToShared(Person person, ArrayList<Person> friends){
        //ArrayList<Person> persons = loadFromShared();
        //if(persons == null) persons = new ArrayList<>();
        //persons.add(person);
        //friends = persons;
        friends.add(person);
        saveListToShared(SHARED_FRIENDS, friends, editor);
    }

    public void addListToShared(ArrayList<Person> friends){
        saveListToShared(SHARED_FRIENDS, friends, editor);
    }

    public ArrayList<Person> loadPersonsFromShared(){
        Gson gson = new Gson();

        String json = sharedPref.getString(SHARED_FRIENDS,"");
        if(json == "") return new ArrayList<Person>();

        Type listType = new TypeToken<ArrayList<Person>>() {
        }.getType();

        ArrayList<Person> persons = gson.fromJson(json, listType);

        // return jsonListToPersonList(persons);
        return persons;
    }

    public ArrayList<LatLng> loadPositionsFromShared(){
        Gson gson = new Gson();

        String json = sharedPref.getString(SHARED_POSITIONS,"");
        if(json == "") return new ArrayList<LatLng>();

        Type listType = new TypeToken<ArrayList<Person>>() {
        }.getType();

        ArrayList<LatLng> positions = gson.fromJson(json, listType);

        // return jsonListToPersonList(persons);
        return positions;
    }

    public LatLng loadPosition(){

        String json = sharedPref.getString(SHARED_POSITION,"");

        Type type = new TypeToken<LatLng>() {
        }.getType();

        LatLng position = new Gson().fromJson(json,type);

        return position;
    }

    public void savePosition(LatLng latLng){
        Gson gson = new Gson();
        String json = gson.toJson(latLng);

        editor.putString(SHARED_POSITION, json);
        editor.commit();
    }

    public <Person> void saveListToShared(String key, ArrayList<Person> list, SharedPreferences.Editor editor){
        Gson gson = new Gson();
        String json = gson.toJson(list);

        editor.putString(key, json);
        editor.commit();
    }

    public void saveStringToShared(String key, String value){
        editor.putString(key, value);
        editor.commit();
    }

    public void saveBooleanToShared(String key, Boolean value){
        editor.putBoolean(key, value);
        editor.commit();
    }
}
