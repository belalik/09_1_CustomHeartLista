package com.example.a09_1_customheartlista;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.preference.PreferenceManager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

public class CustomListAdapter extends ArrayAdapter {

    private static final String TAG = "CustomListAdapter - thomas";

    private Context context;

    private ArrayList<Animal> animals;

    ArrayList<Integer> shortlistIDs;

    private SharedPreferences myPreferences;
    private SharedPreferences.Editor myEditor;

    public CustomListAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Animal> animals) {
        super(context, resource, animals);

        this.context = context;
        this.animals = animals;
        initializePreferences();
        loadShortlisted();
    }

    private void initializePreferences() {
        myPreferences = PreferenceManager.getDefaultSharedPreferences(context);
        myEditor = myPreferences.edit();
    }

    private void loadShortlisted() {
        String shortlistedIDs = myPreferences.getString(MainActivity.SHORTLISTED_KEY, "");
        if (shortlistedIDs.length() == 0) {
            shortlistIDs = new ArrayList<>();
        }
        else {
            Gson gson = new Gson();
            Type type = new TypeToken<ArrayList<Integer>>() {}.getType();
            shortlistIDs = gson.fromJson(shortlistedIDs, type);
        }
    }


    /**
     * Κάνουμε Override την GetView μέθοδο του Adapter. Αυτή η μέθοδος καλείται κάθε φορά που ένα list item
     * χρειάζεται να εμφανισθεί. Μας δίνεται το position του item, ένα recycled convertView (αν υπάρχει,
     * μπορεί να είναι και null) και το 'parent' ViewGroup (το οποίο είναι το ίδιο το ListView - που περιέχει
     * τα items).
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return
     */
    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //return super.getView(position, convertView, parent);

        Log.i(TAG, "getView: called, at position: "+position);

        // Δηλώνω ένα νέο View (rowView) για ευκολία (θα το συζητήσουμε στο εργαστήριο).
        // Αυτό το View θα είναι η κάθε "γραμμή" στη λίστα μου.

        // το αντιστοιχώ στο convertView που έρχεται ως παράμετρος στη μέθοδο.
        View rowView = convertView;

        // Αν είναι null, σημαίνει πως δεν έχω διαθέσιμο recycled View και πρέπει να
        // κάνω inflate - να δημιουργήσω δηλαδή το νέο View αντικείμενο για την γραμμή.

        if (convertView == null) {

            // O LayoutInflater χρησιμοποιείται για να κάνει inflate ένα XML layout resource
            // (εδώ το R.layout.listview_row) σε View αντικείμενο στην Java. Το false δηλώνει
            // πως δεν θέλω να κάνω αμέσως attach το inflated View στο parent ViewGroup.
            LayoutInflater inflater = LayoutInflater.from(context);
            rowView = inflater.inflate(R.layout.listview_row, parent, false);
        }
        else {
            rowView = convertView;
        }

        // Κάνω initialize τα Views
        TextView txtTitle = rowView.findViewById(R.id.txt_title);
        TextView txtSubtitle = rowView.findViewById(R.id.txt_subtitle);
        ImageView imgView = rowView.findViewById(R.id.imageView_animal);
        ImageView imgHeart = rowView.findViewById(R.id.img_heart);

        // Παίρνω το Animal αντικείμενο από την λίστα, για να έχω πρόσβαση στα δεδομένα του.
        // αν ήθελα να ήμουν 100% προσεκτικός, θα έβαζα έλεγχο πως το Animal δεν είναι NULL.
        // αυτό δεν χρειάζεται εδώ, αλλά θα χρειαζόταν σε περιπτώσεις που θα δημιουργούσα τα data μου
        // από ένα δημόσιο API, ή από μια βάση κλπ - θα το συζητήσουμε στο εργαστήριο ...
        Animal selectedAnimal = (Animal) getItem(position);


        txtTitle.setText(selectedAnimal.getTitle());
        txtSubtitle.setText(animals.get(position).getSubtitle());
        imgView.setImageResource(animals.get(position).getImage());

        if (selectedAnimal.isShortlisted()) {
            imgHeart.setImageResource(R.drawable.ic_red_heart);
        }
        else {
            imgHeart.setImageResource(R.drawable.ic_blank_heart);
        }

        imgHeart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (animals.get(position).isShortlisted()) {
                    imgHeart.setImageResource(R.drawable.ic_blank_heart);
                    selectedAnimal.setShortlisted(false);
                    shortlistIDs.remove((Integer) animals.get(position).getID());
                }
                else {
                    imgHeart.setImageResource(R.drawable.ic_red_heart);
                    selectedAnimal.setShortlisted(true);
                    shortlistIDs.add(animals.get(position).getID());
                }

                Gson gson = new Gson();
                String json2 = gson.toJson(shortlistIDs);
                Log.i(TAG, "onClick: saving shortlisted for "+selectedAnimal.getTitle());

                myEditor.putString(MainActivity.SHORTLISTED_KEY, json2);
                myEditor.apply();

                Log.i(TAG, "onClick: json saved is: "+json2);
            }
        });
        return rowView;
    }

}
