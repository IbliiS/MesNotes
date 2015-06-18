package com.project.baptiste.mesnoteas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;

import com.project.baptiste.mesnoteas.bdd.IObjetAssoBdd;
import com.project.baptiste.mesnoteas.bdd.IObjetBdd;
import com.project.baptiste.mesnoteas.bdd.RunBDD;
import com.project.baptiste.mesnoteas.general.interfaces.IMatiere;
import com.project.baptiste.mesnoteas.general.interfaces.IMoyenne;
import com.project.baptiste.mesnoteas.general.interfaces.INote;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;
import com.project.baptiste.mesnoteas.listAdapter.NoteListViewAdapter;
import com.project.baptiste.mesnoteas.utilitaire.Utilitaire;

import java.util.ArrayList;
import java.util.List;


public class AccueilActivity extends Activity {
    private RunBDD runBDD;
    private List<IObjet> notes;
    private List<IObjet> matieres;
    private Spinner spinner;
    private List<IObjet> moyennes;
    private IObjetBdd noteBdd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueil);
        runBDD = RunBDD.getInstance(this);
        initVariable();
        initSpinner();
    }

    public void initVariable(){
        notes = new ArrayList<>();
        matieres = new ArrayList<>();
        moyennes = new ArrayList<>();
        noteBdd = runBDD.getNoteBdd();
        moyennes = runBDD.getMoyenneBdd().getAll();
    }



    public void initSpinner(){
        final String selectionner = "-- Selectionner une période --";
        final String ajouter = "+ Ajouter une période";
        spinner = (Spinner) findViewById(R.id.accueilSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item_selected = spinner.getSelectedItem().toString();
                if (!(item_selected.equals(selectionner))) {
                    if (item_selected.equals(ajouter)) {
                        startActivity(new Intent(getApplicationContext(), AjouterMoyenneActivity.class));
                        finish();
                    }
                    else{
                        initListView(item_selected);
                    }

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        List<String> listeMoyenne = new ArrayList<String>();
        listeMoyenne.add(selectionner);
        IMoyenne m;
        for(IObjet o : moyennes){
            m = (IMoyenne) o;
            listeMoyenne.add(m.getNomMoyenne());
        }
        listeMoyenne.add(ajouter);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listeMoyenne);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }

    public void initListView(String item_selected){
        initMatieres(item_selected);
        initNotes();
        NoteListViewAdapter noteListViewAdapter = new NoteListViewAdapter(this,notes,matieres);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(noteListViewAdapter);
    }

    public void initMatieres(String item_selected) {
        matieres.clear();
        Utilitaire utilitaire = new Utilitaire();
        /*
        runBDD.open();
        IMoyenne moyenne = (IMoyenne) runBDD.getMoyenneBdd().getWithName(item_selected);
        matieres = runBDD.getMoyenneMatiereBdd().getListObjetWithId(moyenne.getId());
        runBDD.close();
        */
        runBDD.open();
        IMoyenne moyenne = (IMoyenne) runBDD.getMoyenneBdd().getWithName(item_selected);
        matieres = utilitaire.copyList(runBDD.getMoyenneMatiereBdd().getListObjetWithId(moyenne.getId()));
        runBDD.close();

    }


    public void initNotes(){
        notes.clear();
        IMatiere m;
        for(IObjet o : matieres){
            m = (IMatiere) o;
            INote n;
            runBDD.open();
            for(IObjet on : runBDD.getMatiereNoteBdd().getListObjetWithId(m.getId())){
                n = (INote) on;
                notes.add(n);
            }
            runBDD.close();
        }
    }

    //lancer une autre vue
    public void addNote(View view){
        startActivity(new Intent(getApplicationContext(),AjouterNoteActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mes_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
