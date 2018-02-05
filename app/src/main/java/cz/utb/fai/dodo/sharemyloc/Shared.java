package cz.utb.fai.dodo.sharemyloc;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;

import com.google.gson.Gson;
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

    public ArrayList<Person> loadFromShared(){
        Gson gson = new Gson();

        String json = sharedPref.getString(SHARED_FRIENDS,"");
        if(json == "") return new ArrayList<Person>();

        Type listType = new TypeToken<ArrayList<Person>>() {
        }.getType();

        ArrayList<Person> persons = gson.fromJson(json, listType);

        // return jsonListToPersonList(persons);
        return persons;
    }

    public <Person> void saveListToShared(String key, ArrayList<Person> list, SharedPreferences.Editor editor){
        Gson gson = new Gson();
        String json = gson.toJson(list);

        editor.putString(key, json);
        editor.commit();
    }
}
