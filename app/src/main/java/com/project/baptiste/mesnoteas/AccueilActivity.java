package com.project.baptiste.mesnoteas;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.style.StyleSpan;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.project.baptiste.mesnoteas.bdd.interfacesBdd.IObjetBdd;
import com.project.baptiste.mesnoteas.bdd.RunBDD;
import com.project.baptiste.mesnoteas.general.interfaces.IAnnee;
import com.project.baptiste.mesnoteas.general.interfaces.IMatiere;
import com.project.baptiste.mesnoteas.general.interfaces.IMoyenne;
import com.project.baptiste.mesnoteas.general.interfaces.INote;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;
import com.project.baptiste.mesnoteas.listAdapter.InitSpinnerAndList;
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
    private InitSpinnerAndList initSpinnerAndList;
    private TextView moyennePeriodeField;
    private TextView moyennePeriodeLabel;
    private Utilitaire utilitaire;
    private TextView labelMoyenneAnnee;
    private TextView fieldMoyenneAnnee;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueil);
        runBDD = RunBDD.getInstance(this);
        initVariable();
        initTextView();
        initMoyennePeriodeTextView("-- Toutes --");
        beginSpinner();
    }

    /**
     * Mettre Text view en bold italic
     */
    private void initTextView() {
        TextView labelAnneeAccueil = (TextView) findViewById(R.id.labelAnneeAccueil);
        TextView labelMoyenneAccueil = (TextView) findViewById(R.id.labelMoyenneAccueil);
        TextView labelMatiereAccueil = (TextView) findViewById(R.id.labelMatiereAccueil);
        TextView[] tabTV = {labelAnneeAccueil,labelMoyenneAccueil,labelMatiereAccueil};
        SpannableString annee = new SpannableString(getText(R.string.labelAnneeAccueil));
        SpannableString moyenne = new SpannableString(getText(R.string.labelMoyenneAccueil));
        SpannableString matiere = new SpannableString(getText(R.string.labelMatiereAccueil));
        SpannableString[] tabSS = {annee,moyenne,matiere};
        for(int i =0;i<tabSS.length;i++){
            tabSS[i].setSpan(new StyleSpan(Typeface.BOLD_ITALIC), 0, tabSS[i].length(), 0);
            tabTV[i].setText(tabSS[i]);
        }
    }

    public void beginSpinner(){
        if(begin){
            spinner.setEnabled(false);
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
        fieldMoyenneAnnee = (TextView) findViewById(R.id.fieldMoyenneAnnee);
        labelMoyenneAnnee = (TextView) findViewById(R.id.labelMoyenneAnnee);
        moyennePeriodeField = (TextView) findViewById(R.id.fieldMoyennePeriode);
        moyennePeriodeLabel = (TextView) findViewById(R.id.labelMoyennePeriode);
        notes = new ArrayList<>();
        matieres = new ArrayList<>();
        moyennes = new ArrayList<>();
        noteBdd = runBDD.getNoteBdd();
        annees = new ArrayList<>();
        anneeSpinner = (Spinner) findViewById(R.id.accueilAnneeSpinner);
        spinner = (Spinner) findViewById(R.id.accueilSpinner);
        matiereSpinner = (Spinner) findViewById(R.id.accueilMatiereSpinner);
        initSpinnerAndList = new InitSpinnerAndList(runBDD,spinner,anneeSpinner,matiereSpinner);
        annees = initSpinnerAndList.getAnnees();
        anneeSpinner = initSpinnerAndList.getAnneeSpinner();
        initAnneeSpinner2();
    }


    public void initAnneeSpinner2(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, initSpinnerAndList.getAnneeString());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        anneeSpinner.setAdapter(adapter);
        if(initSpinnerAndList.getAnneeString().size() == 0){
            anneeSpinner.setSelection(0);
        }
        else {
            anneeSpinner.setSelection(1);
        }
        final String selectionner = "-- Selectionner --";
        anneeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item_selected = anneeSpinner.getSelectedItem().toString();
                if (!(item_selected.equals(selectionner))) {
                    spinner.setEnabled(true);
                    matiereSpinner.setEnabled(true);
                    moyennes = initSpinnerAndList.initMoyenne(item_selected);
                    spinner = initSpinnerAndList.getMoyenneSpinner();
                    matieres = initSpinnerAndList.initMatieresParAnnee(item_selected);
                    matiereSpinner = initSpinnerAndList.getMatiereSpinner();
                    initMoyenneSpinner2();
                    initMatiereSpinner2();
                } else {
                    initListView();
                    spinner.setEnabled(false);
                    matiereSpinner.setEnabled(false);
                }
                initMoyenneAnneeTextView(item_selected);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
    }

    public void initMoyenneSpinner2(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, initSpinnerAndList.getMoyenneString());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
        final String toutes = "-- Toutes --";
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item_selected = spinner.getSelectedItem().toString();
                if (item_selected.equals(toutes)) {
                    matieres = initSpinnerAndList.initMatieresParAnnee(anneeSpinner.getSelectedItem().toString());
                } else {
                    matieres = initSpinnerAndList.initMatieresParMoyenne(item_selected);
                }
                initMoyennePeriodeTextView(item_selected);
                matiereSpinner = initSpinnerAndList.getMatiereSpinner();
                initMatiereSpinner2();
                initListView();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
    }

    public void initMatiereSpinner2(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, initSpinnerAndList.getMatiereString());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        matiereSpinner.setAdapter(adapter);
        matiereSpinner.setSelection(0);
        final String selectionner = "-- Toutes --";
        matiereSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item_selected = matiereSpinner.getSelectedItem().toString();
                if (!(item_selected.equals(selectionner))) {
                    matieres = initSpinnerAndList.initMatiereParMatiere(item_selected);
                    initListView();
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
    }

    public void initMoyennePeriodeTextView(String item_selected){
        if(item_selected.equals("-- Toutes --")){
            moyennePeriodeField.setText("");
            moyennePeriodeLabel.setText("");
        }
        else{
            runBDD.open();
            IMoyenne m = (IMoyenne) runBDD.getMoyenneBdd().getWithName(item_selected);
            runBDD.close();
            moyennePeriodeField.setText(utilitaire.coupeMoyenne(m.getMoyenne()));
            moyennePeriodeLabel.setText("Moyenne période "+item_selected+ " :");
        }
    }

    public void initMoyenneAnneeTextView(String item_selected){
        if(item_selected.equals("-- Toutes --")){
            labelMoyenneAnnee.setText("");
            fieldMoyenneAnnee.setText("");
        }
        else{
            runBDD.open();
            IAnnee a = (IAnnee) runBDD.getAnneeBdd().getWithName(item_selected);
            runBDD.close();
            fieldMoyenneAnnee.setText(utilitaire.coupeMoyenne(a.getMoyenne()));
            labelMoyenneAnnee.setText("Moyenne année "+item_selected+ " :");
        }
    }

    public void initListView(){
        notes = initSpinnerAndList.getNotes();
        NoteListViewAdapter noteListViewAdapter = new NoteListViewAdapter(this,notes,matieres);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(noteListViewAdapter);
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
