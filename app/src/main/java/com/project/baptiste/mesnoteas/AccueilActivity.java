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

import com.project.baptiste.mesnoteas.bdd.interfacesBdd.IObjetBdd;
import com.project.baptiste.mesnoteas.bdd.RunBDD;
import com.project.baptiste.mesnoteas.general.interfaces.IAnnee;
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
    private Spinner spinner; //spinner période moyenne
    private Spinner anneeSpinner;
    private Spinner matiereSpinner;
    private List<IObjet> moyennes;
    private List<IObjet> annees;
    private IObjetBdd noteBdd;
    private boolean begin = true;
    private Utilitaire utilitaire;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueil);
        runBDD = RunBDD.getInstance(this);
        initVariable();
        beginSpinner();
        initAnneeSpinner();
    }

    public void beginSpinner(){
        if(begin){
            anneeSpinner = (Spinner) findViewById(R.id.accueilAnneeSpinner);
            spinner = (Spinner) findViewById(R.id.accueilSpinner);
            spinner.setEnabled(false);
            matiereSpinner = (Spinner) findViewById(R.id.accueilMatiereSpinner);
            matiereSpinner.setEnabled(false);
            final String tous = "-- Selectionner --";
            List<String> exemple = new ArrayList<String>();
            exemple.add(tous);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, exemple);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setSelection(0);
            matiereSpinner.setAdapter(adapter);
            matiereSpinner.setSelection(0);
            begin = false;
        }
    }

    public void initVariable(){
        utilitaire = new Utilitaire();
        notes = new ArrayList<>();
        matieres = new ArrayList<>();
        matieres = utilitaire.copyList(runBDD.getMatiereBdd().getAll());
        moyennes = new ArrayList<>();
        moyennes = utilitaire.copyList(runBDD.getMoyenneBdd().getAll());
        noteBdd = runBDD.getNoteBdd();
        annees = new ArrayList<>();
        annees = utilitaire.copyList(runBDD.getAnneeBdd().getAll());
    }


    public void initAnneeSpinner(){
        final String selectionner = "-- Selectionner --";
        anneeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item_selected = anneeSpinner.getSelectedItem().toString();
                if (!(item_selected.equals(selectionner))) {
                    spinner.setEnabled(true);
                    matiereSpinner.setEnabled(true);
                    runBDD.open();
                    IAnnee annee = (IAnnee) runBDD.getAnneeBdd().getWithName(item_selected);
                    moyennes.clear();
                    moyennes = utilitaire.copyList(runBDD.getAnneeMoyenneBdd().getListObjetWithId(annee.getId()));
                    runBDD.close();
                    initSpinner();
                    initMatiereSpinner();
                    initMatieresParAnnee();
                    initListView();
                }
                else{
                    spinner.setEnabled(false);
                    matiereSpinner.setEnabled(false);
                }

            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
        List<String> exemple = new ArrayList<String>();
        exemple.add(selectionner);
        IAnnee a;
        for(IObjet o : annees){
            a = (IAnnee) o;
            exemple.add(a.getNomAnnee());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, exemple);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        anneeSpinner.setAdapter(adapter);
        anneeSpinner.setSelection(0);
    }

    public void initMatiereSpinner(){
        final String selectionner = "-- Toutes --";
        matiereSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item_selected = matiereSpinner.getSelectedItem().toString();
                if (!(item_selected.equals(selectionner))) {
                    initMatiereParMatiere();
                    initListView();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

        List<String> listeMatiere = new ArrayList<String>();
        listeMatiere.add(selectionner);
        IMatiere m;
        for(IObjet o : matieres){
            m = (IMatiere) o;
            listeMatiere.add(m.getNomMatiere());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listeMatiere);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        matiereSpinner.setAdapter(adapter);
        matiereSpinner.setSelection(0);
    }



    // SPINNER MOYENNE
    public void initSpinner(){
        final String toutes = "-- Toutes --";
        spinner = (Spinner) findViewById(R.id.accueilSpinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item_selected = spinner.getSelectedItem().toString();
                if(item_selected.equals(toutes)){
                    initMatieresParAnnee();
                }
                else{
                    initMatieresParMoyenne(item_selected);
                }
                initMatiereSpinner();
                initListView();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
        List<String> listeMoyenne = new ArrayList<String>();
        listeMoyenne.add(toutes);
        IMoyenne m;
        for(IObjet o : moyennes){
            m = (IMoyenne) o;
            listeMoyenne.add(m.getNomMoyenne());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, listeMoyenne);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }


    public void initListView(){
        initNotes();
        NoteListViewAdapter noteListViewAdapter = new NoteListViewAdapter(this,notes,matieres);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(noteListViewAdapter);
    }

    /**
     * Recupere la matiere selectionnée dans le spinner
     */
    private void initMatiereParMatiere() {
        matieres.clear();
        String item_selected = matiereSpinner.getSelectedItem().toString();
        runBDD.open();
        IMatiere m = (IMatiere) runBDD.getMatiereBdd().getWithName(item_selected);
        m.setNotes(runBDD.getMatiereNoteBdd().getListObjetWithId(m.getId()));
        matieres.add(m);
        runBDD.close();

    }

    /**
     * Recupere toutes les matieres de l'année sélectionnée
     */
    private void initMatieresParAnnee() {
        matieres.clear();
        String nomAnnee = anneeSpinner.getSelectedItem().toString();
        runBDD.open();
        IAnnee annee = (IAnnee) runBDD.getAnneeBdd().getWithName(nomAnnee);
        List<IObjet> listMoyennes = utilitaire.copyList(runBDD.getAnneeMoyenneBdd().getListObjetWithId(annee.getId()));
        List<IObjet> listMatieres = new ArrayList<>();
        IMoyenne m;
        for(IObjet o : listMoyennes){
            m = (IMoyenne) o;
            listMatieres = utilitaire.copyList(runBDD.getMoyenneMatiereBdd().getListObjetWithId(m.getId()));
            IMatiere mat;
            for(IObjet ob : listMatieres){
                mat = (IMatiere) ob;
                matieres.add(mat);
            }
        }
        runBDD.close();
    }

    /**
     * Recupere matieres par moyenne
     * @param item_selected le nom de la moyenne
     */
    public void initMatieresParMoyenne(String item_selected) {
        matieres.clear();
        /*
        runBDD.open();
        IMoyenne moyenne = (IMoyenne) runBDD.getMoyenneBdd().getWithName(item_selected);
        matieres = runBDD.getMoyenneMatiereBdd().getListObjetWithId(moyenne.getId());
        runBDD.close();
        */
        runBDD.open();
        IMoyenne moyenne = (IMoyenne) runBDD.getMoyenneBdd().getWithName(item_selected);
        List<IObjet> l = new ArrayList<>();
        l = utilitaire.copyList(runBDD.getMoyenneMatiereBdd().getListObjetWithId(moyenne.getId()));
        IMatiere m;
        for(IObjet o : l){
            m = (IMatiere) o;
            m.setNotes(runBDD.getMatiereNoteBdd().getListObjetWithId(m.getId()));
            matieres.add(m);
        }
        runBDD.close();

    }

    /**
     * Recuperes toutes les notes de la variable matieres et les mets dans la variable notes
     */
    public void initNotes(){
        notes.clear();
        IMatiere m;
        for(IObjet o : matieres){
            m = (IMatiere) o;
            INote n;
            runBDD.open();
            List<IObjet> l = utilitaire.copyList(runBDD.getMatiereNoteBdd().getListObjetWithId(m.getId()));
            for(IObjet on : l){
                n = (INote) on;
                notes.add(n);
            }
            runBDD.close();
        }
    }

    //lancer une autre vue
    public void addNote(View view){
        startActivity(new Intent(getApplicationContext(), AjouterNoteActivity.class));
        finish();
    }

    public void addAnnee(View view){
        startActivity(new Intent(getApplicationContext(), AjouterAnneeActivity.class ));
        finish();
    }

    public void addMoyenne(View view){
        startActivity(new Intent(getApplicationContext(), AjouterMoyenneActivity.class));
        finish();
    }

    public void addMatiere(View view){
        startActivity(new Intent(getApplicationContext(), AjouterMatiereActivity.class));
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
