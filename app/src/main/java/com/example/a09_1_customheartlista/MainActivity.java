package com.example.a09_1_customheartlista;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import androidx.preference.PreferenceManager;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity - thomas";

    public static final String ANIMAL_KEY = "animal selected";
    public static final String SHORTLISTED_KEY = "the shortlist";

    private SharedPreferences myPreferences;
    private SharedPreferences.Editor myEditor;

    ArrayList<Integer> shortlistIDs = new ArrayList<>();

    ListView listview;
    ArrayList<Animal> animals;
    CustomListAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeViews();

        initializeData();

        initializePreferences();

        loadShortlisted();

        markShortlisted();

        initializeAdapter();

    }

    private void initializeAdapter() {
        // Κάνε initialize τον adapter
        adapter = new CustomListAdapter(this, R.layout.listview_row, animals);

        // Θέσε τον adapter στο listview
        listview.setAdapter(adapter);

        // απλό κλικ event
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                //showToast(position);
                openDetailActivity(position);
            }
        });

        // παρατεταμένο click event
        listview.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                removeAnimalDialog(position);

                // γιατί επιστρέφουμε true?
                // https://stackoverflow.com/questions/12230469/android-why-does-onitemlongclick-return-a-boolean
                // με λίγα λόγια - αν επιστρέψουμε false, το long click θα κάνει trigger και ένα "απλό" click event.
                return true;
            }
        });
    }

    private void openDetailActivity(int position) {
        Intent i = new Intent(this, DetailActivity.class);
        Gson gson = new Gson();
        String myJson = gson.toJson(animals.get(position));
        i.putExtra(ANIMAL_KEY, myJson);
        startActivity(i);
    }

    private void showToast(int position) {
        Toast.makeText(this, "Hello from "+animals.get(position).getTitle(), Toast.LENGTH_LONG).show();
    }

    // Φτιάξε dialog για να γίνεται remove ένα item. Παρατηρείστε τον κώδικα, τρέξτε το και δείτε τα logs.
    private void removeAnimalDialog(int position) {
        AlertDialog.Builder builder1 = new AlertDialog.Builder(this);
        builder1.setMessage("Delete "+animals.get(position).getTitle()+" ?");
        builder1.setCancelable(true);

        builder1.setPositiveButton(
                "Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        Toast.makeText(MainActivity.this, animals.get(position).getTitle()+" deleted !", Toast.LENGTH_LONG).show();

                        Log.i(TAG, "onClick: counting size of ArrayList BEFORE: "+animals.size());
                        Log.i(TAG, "onClick: deleting "+animals.get(position).getTitle()+" at position: "+position);

                        animals.remove(position);

                        Log.i(TAG, "onClick: counting size of ArrayList AFTER: "+animals.size());

                        adapter.notifyDataSetChanged();

                        dialog.cancel();
                    }
                });

        builder1.setNegativeButton(
                "No",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });

        AlertDialog alert11 = builder1.create();
        alert11.show();
    }

    private void initializeViews() {
        listview = findViewById(R.id.listview);
    }

    private void initializePreferences() {

        // getDefaultSharedPreferences and getSharedPreferences is basically the same,
        // see https://stackoverflow.com/questions/5946135/difference-between-getdefaultsharedpreferences-and-getsharedpreferences

        // also, see this: https://stackoverflow.com/questions/56833657/preferencemanager-getdefaultsharedpreferences-deprecated-in-android-q
        // getDefaultSharedPreferences deprecated after Android 10 - include library in build.gradle (Module) and include androix package.
        myPreferences = PreferenceManager.getDefaultSharedPreferences(this);

        myEditor = myPreferences.edit();

        //boolean nameExists = myPreferences.contains(NAME);
        //Log.i(TAG, "initializePreferences: nameExists is: "+nameExists);


    }


    private void loadShortlisted() {
        //String shortlisted = prefManager.fetchValueString(AnimaPreferencesManager.SHORTLISTED_LIST);

        String shortlistedIDs = myPreferences.getString(MainActivity.SHORTLISTED_KEY, "");

        // no shortlisted animals, but need to initialize ArrayList to avoid null pointer
        if (shortlistedIDs.length() == 0) {
            shortlistIDs = new ArrayList<>();
        }
        else {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Integer>>() {}.getType();
            shortlistIDs = gson.fromJson(shortlistedIDs, type);
        }

    }

    private void markShortlisted() {
        Log.i(TAG, "markShortlisted: shortlist size is: "+shortlistIDs.size());
        for (int i=0; i<shortlistIDs.size(); i++) {

            for (int j=0; j<animals.size(); j++) {
                if (shortlistIDs.get(i) == animals.get(j).getID()) {
                    animals.get(j).setShortlisted(true);
                }
            }
        }
    }



    /**
     * Δημιουργώ τα data μου. Επιλέγω σε αυτό το app να το κάνω χρησιμοποιώντας μια class-wide μεταβλητή
     * τύπου ArrayList<Animal>. Έχει ήδη δηλωθεί στην αρχή της κλάσης. Μετά την κλήση αυτής της μεθόδου, θα
     * έχει "γεμίσει" με κατάλληλα αντικείμενα.
     */
    private void initializeData() {

        animals = new ArrayList<>();

        animals.add(new Animal("Bee", "Sting like a bee!", R.drawable.bee_icon, false));

        animals.add(new Animal("Bird", "It's a bird!", R.drawable.bird_icon, false));
        animals.add(new Animal("Cat", "little cat",  R.drawable.cat_icon, false));
        animals.add(new Animal("Dinosaur", "are they extinct?",  R.drawable.dinosaur_icon, false));

        animals.add(new Animal("Dog", "Faithful !",  R.drawable.dog_icon, false));
        animals.add(new Animal("Fish", "Swims fast",  R.drawable.fish_icon, false));
        animals.add(new Animal("Flamingo", "Pink and beautiful",  R.drawable.flamingo_icon, false));
        animals.add(new Animal("Octopus", "Many legs!",  R.drawable.octopus_icon, false));

        animals.add(new Animal("Owl", "Wise !",  R.drawable.owl_icon, false));
        animals.add(new Animal("Parrot", "Speaks many languages",  R.drawable.parrot_icon, false));
        animals.add(new Animal("Stork", "Delivers babies",  R.drawable.stork_icon, false));
        animals.add(new Animal("Unicorn", "It's magical",  R.drawable.unicorn_icon, false));

        animals.add(new Animal("Bee2", "Sting like a bee",  R.drawable.bee_icon, false));
        animals.add(new Animal("Bird2", "it's a bird !!",  R.drawable.bird_icon, false));
        animals.add(new Animal("Cat2", "little cat",  R.drawable.cat_icon, false));
        animals.add(new Animal("Dinosaur2", "are they extinct?",  R.drawable.dinosaur_icon, false));

        animals.add(new Animal("Dog2", "Faithful !",  R.drawable.dog_icon, false));
        animals.add(new Animal("Fish2", "Swims fast",  R.drawable.fish_icon, false));
        animals.add(new Animal("Flamingo2", "Pink and beautiful",  R.drawable.flamingo_icon, false));
        animals.add(new Animal("Octopus2", "Many legs!",  R.drawable.octopus_icon, false));

        animals.add(new Animal("Owl2", "Wise !",  R.drawable.owl_icon, false));
        animals.add(new Animal("Parrot2", "Speaks many languages",  R.drawable.parrot_icon, false));
        animals.add(new Animal("Stork2", "Delivers babies",  R.drawable.stork_icon, false));
        animals.add(new Animal("Unicorn2", "It's magical",  R.drawable.unicorn_icon, false));

    }
}