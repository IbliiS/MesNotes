package com.project.baptiste.mesnoteas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;

import com.andreabaccega.widget.FormEditText;
import com.project.baptiste.mesnoteas.bdd.IObjetAssoBdd;
import com.project.baptiste.mesnoteas.bdd.IObjetBdd;
import com.project.baptiste.mesnoteas.bdd.RunBDD;
import com.project.baptiste.mesnoteas.general.interfaces.IMatiere;
import com.project.baptiste.mesnoteas.general.interfaces.IMoyenne;
import com.project.baptiste.mesnoteas.general.interfaces.INote;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baptiste on 12/06/2015.
 */
public class AjouterNoteActivity extends Activity {
    private RunBDD runBDD;
    private Button buttonAjouter;
    private FormEditText noteField;
    private FormEditText coefField;
    private INote note;
    private IMatiere matiere;
    private Spinner spinner;
    private List<IObjet> matieres;
    private List<IObjet> moyennes;
    private IObjetBdd matiereBdd;
    private IObjetBdd noteBdd;
    private IObjetAssoBdd matiereNoteBdd;
    private Spinner ajouterNotespinnerMoyenne;
    private Boolean[] tousValides = {false, false};
    private boolean begin = true;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajouter_note);
        initVariable();
        initField();
        initSpinnerMoyenne();
    }



    public void initVariable(){
        runBDD = RunBDD.getInstance(this);
        matiereBdd =  runBDD.getMatiereBdd();
        matiereNoteBdd = runBDD.getMatiereNoteBdd();
        noteBdd = runBDD.getNoteBdd();
        matieres = new ArrayList<>();
        moyennes = new ArrayList<>();
        moyennes = runBDD.getMoyenneBdd().getAll();

    }

    public void initSpinnerMatiere(){
        final String selectionner = "-- Selectionner une matiere --";
        final String ajouter = "+ Ajouter une matière";
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item_selected = spinner.getSelectedItem().toString();
                if (!(item_selected.equals(selectionner))) {
                    if (item_selected.equals(ajouter)) {
                        startActivity(new Intent(getApplicationContext(), AjouterMatiereActivity.class));
                        finish();
                    } else {
                        tousValides[0] = true;
                        activerBoutonAjouter();
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
        List<String> exemple = new ArrayList<String>();
        exemple.add(selectionner);
        IMatiere m;
        for(IObjet o : matieres){
            m = (IMatiere) o;
            exemple.add(m.getNomMatiere());
        }
        exemple.add(ajouter);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, exemple);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }

    private void initSpinnerMoyenne() {
        final String selectionner = "-- Selectionner une période --";
        final String ajouter = "+ Ajouter une période";
        ajouterNotespinnerMoyenne.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item_selected = ajouterNotespinnerMoyenne.getSelectedItem().toString();
                if (!(item_selected.equals(selectionner))) {
                    if (item_selected.equals(ajouter)) {
                        startActivity(new Intent(getApplicationContext(), AjouterMoyenneActivity.class));
                        finish();
                    } else {
                        tousValides[1] = true;
                        spinner.setEnabled(true);
                        activerBoutonAjouter();
                        runBDD.open();
                        IMoyenne moyenne = (IMoyenne) runBDD.getMoyenneBdd().getWithName(item_selected);
                        matieres = runBDD.getMoyenneMatiereBdd().getListObjetWithId(moyenne.getId());
                        runBDD.getMoyenneBdd().close();
                        initSpinnerMatiere();
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
        List<String> exemple = new ArrayList<String>();
        exemple.add(selectionner);
        IMoyenne m;
        for(IObjet o : moyennes){
            m = (IMoyenne) o;
            exemple.add(m.getNomMoyenne());
        }
        exemple.add(ajouter);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, exemple);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ajouterNotespinnerMoyenne.setAdapter(adapter);
        ajouterNotespinnerMoyenne.setSelection(0);
        spinner.setEnabled(false);
    }

    public void beginSpinner(){
        if(begin) {
            spinner = (Spinner) findViewById(R.id.spinner);
            final String selectionner = "-- Selectionner une matiere --";
            List<String> exemple = new ArrayList<String>();
            exemple.add(selectionner);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, exemple);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinner.setAdapter(adapter);
            spinner.setSelection(0);
            begin = false;
        }
    }

    public void initField(){
        beginSpinner();
        ajouterNotespinnerMoyenne = (Spinner) findViewById(R.id.ajouterNotespinnerMoyenne);
        buttonAjouter = (Button) findViewById(R.id.ajouterBouton);
        buttonAjouter.setEnabled(false);
        noteField = (FormEditText) findViewById(R.id.noteField);
        coefField = (FormEditText) findViewById(R.id.coefField);
    }

    // BOUTON AJOUTER NOTE
    public void ajouterNote(View view){
        boolean allValid = true;
        FormEditText[] formEditTexts = {noteField, coefField};
        for(FormEditText f : formEditTexts){
            allValid = f.testValidity() && allValid; // IMPORTANT Vérifie les regexp
        }
        if (allValid) {
            matiereBdd.open();
            matiere = (IMatiere) matiereBdd.getWithName(spinner.getSelectedItem().toString());
            note = matiere.creerNote();
            note.setNote(Double.valueOf(noteField.getText().toString()));
            note.setCoef(Integer.valueOf(coefField.getText().toString()));
            note.setId((int) noteBdd.insert(note));
            runBDD.getMatiereNoteBdd().insert(note,matiere);
            matiereBdd.close();
            startActivity(new Intent(getApplicationContext(), AccueilActivity.class));
            finish();
        }
    }

    public void activerBoutonAjouter(){
        if(tousValides[1] && tousValides[0] ){
            buttonAjouter.setEnabled(true);
        }
    }

    public void retourNoteButton(View view){
        startActivity(new Intent(getApplicationContext(), AccueilActivity.class));
        finish();
    }

}
