package cz.utb.fai.dodo.sharemyloc;

import java.util.ArrayList;

/**
 * Created by Dodo on 05.02.2018.
 */

public class Person {

    String uid, name, surname;

    public Person(String uid, String name, String surname) {
        this.uid = uid;
        this.name = name;
        this.surname = surname;
    }

    public Person() {
    }

    public String getUid() {

        return uid;
    }

    public String getName() {
        return name;
    }

    public String getSurname() {
        return surname;
    }

    public String getFullName(){
        return name + " " + surname;
    }

    public static ArrayList<String> listOfPersonsToFullNames(ArrayList<Person> persons){
        ArrayList<String> people = new ArrayList<String>();

        for (Person person:persons) {
            people.add(person.getFullName());
        }

        return people;
    }
}
