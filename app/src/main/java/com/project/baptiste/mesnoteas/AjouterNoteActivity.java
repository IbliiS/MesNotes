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
import com.project.baptiste.mesnoteas.bdd.interfacesBdd.IObjetAssoBdd;
import com.project.baptiste.mesnoteas.bdd.interfacesBdd.IObjetBdd;
import com.project.baptiste.mesnoteas.bdd.RunBDD;
import com.project.baptiste.mesnoteas.general.interfaces.IAnnee;
import com.project.baptiste.mesnoteas.general.interfaces.IMatiere;
import com.project.baptiste.mesnoteas.general.interfaces.IMoyenne;
import com.project.baptiste.mesnoteas.general.interfaces.INote;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;
import com.project.baptiste.mesnoteas.listAdapter.InitSpinnerAndList;

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
    private Spinner spinnerMatiere;
    private List<IObjet> matieres;
    private List<IObjet> moyennes;
    private List<IObjet> annees;
    private IObjetBdd matiereBdd;
    private IObjetBdd noteBdd;
    private Spinner ajouterNotespinnerMoyenne;
    private boolean begin = true;
    private Spinner anneeSpinner;
    private InitSpinnerAndList initSpinnerAndList;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajouter_note);
        initVariable();
        initField();
        //initAnneeSpinner();
    }



    public void initVariable(){
        runBDD = RunBDD.getInstance(this);
        ajouterNotespinnerMoyenne = (Spinner) findViewById(R.id.ajouterNotespinnerMoyenne);
        spinnerMatiere = (Spinner) findViewById(R.id.spinner);
        anneeSpinner = (Spinner) findViewById(R.id.ajouterNotespinnerAnnee);
        initSpinnerAndList = new InitSpinnerAndList(runBDD,ajouterNotespinnerMoyenne,anneeSpinner,spinnerMatiere);
        matiereBdd =  runBDD.getMatiereBdd();
        noteBdd = runBDD.getNoteBdd();
        matieres = new ArrayList<>();
        moyennes = new ArrayList<>();
        annees = new ArrayList<>();
        moyennes = runBDD.getMoyenneBdd().getAll();
        //
        annees = initSpinnerAndList.getAnnees();
        anneeSpinner = initSpinnerAndList.getAnneeSpinner();
        initAnneeSpinner2();


    }

    public void initField(){
        beginSpinner();
        buttonAjouter = (Button) findViewById(R.id.ajouterBouton);
        buttonAjouter.setEnabled(false);
        noteField = (FormEditText) findViewById(R.id.noteField);
        coefField = (FormEditText) findViewById(R.id.coefField);
    }

    public void beginSpinner(){
        if(begin) {
            final String selectionner = "-- Selectionner --";
            List<String> exemple = new ArrayList<String>();
            exemple.add(selectionner);
            ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, exemple);
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            spinnerMatiere.setAdapter(adapter);
            spinnerMatiere.setSelection(0);
            spinnerMatiere.setEnabled(false);
            ajouterNotespinnerMoyenne.setAdapter(adapter);
            ajouterNotespinnerMoyenne.setSelection(0);
            ajouterNotespinnerMoyenne.setEnabled(false);
            begin = false;
        }
    }

    private void initAnneeSpinner2() {
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, initSpinnerAndList.getAnneeString());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        anneeSpinner.setAdapter(adapter);
        if(initSpinnerAndList.getAnneeString().size() == 1){
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
                    ajouterNotespinnerMoyenne.setEnabled(true);
                    spinnerMatiere.setEnabled(true);
                    moyennes = initSpinnerAndList.initMoyenne(item_selected);
                    ajouterNotespinnerMoyenne = initSpinnerAndList.getMoyenneSpinner();
                    matieres = initSpinnerAndList.initMatieresParAnnee(item_selected);
                    spinnerMatiere = initSpinnerAndList.getMatiereSpinner();
                    initMoyenneSpinner2();
                    initMatiereSpinner2();
                } else {
                    //moyennes <- toutes les moyennes
                    //matieres <- toutes les matieres
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
    }

    private void initMoyenneSpinner2(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, initSpinnerAndList.getMoyenneString());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ajouterNotespinnerMoyenne.setAdapter(adapter);
        ajouterNotespinnerMoyenne.setSelection(0);
        final String toutes = "-- Toutes --";
        ajouterNotespinnerMoyenne.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item_selected = ajouterNotespinnerMoyenne.getSelectedItem().toString();
                if (item_selected.equals(toutes)) {
                    matieres = initSpinnerAndList.initMatieresParAnnee(anneeSpinner.getSelectedItem().toString());
                } else {
                    matieres = initSpinnerAndList.initMatieresParMoyenne(item_selected);
                }
                spinnerMatiere = initSpinnerAndList.getMatiereSpinner();
                initMatiereSpinner2();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });

    }

    private void initMatiereSpinner2(){
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, initSpinnerAndList.getMatiereString());
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMatiere.setAdapter(adapter);
        spinnerMatiere.setSelection(0);
        final String selectionner = "-- Toutes --";
        spinnerMatiere.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item_selected = spinnerMatiere.getSelectedItem().toString();
                int id_selected = spinnerMatiere.getSelectedItemPosition();
                if (!(item_selected.equals(selectionner))) {
                    matieres = initSpinnerAndList.initMatiereParMatiere(item_selected);
                    buttonAjouter.setEnabled(true);
                } else {
                    buttonAjouter.setEnabled(false);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
    }


    public void initAnneeSpinner(){
        final String selectionner = "-- Selectionner une année --";
        anneeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item_selected = anneeSpinner.getSelectedItem().toString();
                if (!(item_selected.equals(selectionner))) {
                    ajouterNotespinnerMoyenne.setEnabled(true);
                    runBDD.open();
                    IAnnee annee = (IAnnee) runBDD.getAnneeBdd().getWithName(item_selected);
                    moyennes.clear();
                    moyennes = runBDD.getAnneeMoyenneBdd().getListObjetWithId(annee.getId());
                    runBDD.close();
                    initSpinnerMoyenne();
                    initSpinnerMatiere();
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
                        spinnerMatiere.setEnabled(true);
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
    }


    public void initSpinnerMatiere(){
        final String selectionner = "-- Selectionner une matiere --";
        final String ajouter = "+ Ajouter une matière";
        spinnerMatiere.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item_selected = spinnerMatiere.getSelectedItem().toString();
                if (!(item_selected.equals(selectionner))) {
                    if (item_selected.equals(ajouter)) {
                        startActivity(new Intent(getApplicationContext(), AjouterMatiereActivity.class));
                        finish();
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
        spinnerMatiere.setAdapter(adapter);
        spinnerMatiere.setSelection(0);
    }






    // BOUTON AJOUTER NOTE
    public void ajouterNote(View view){
        boolean allValid = true;
        FormEditText[] formEditTexts = {noteField, coefField};
        for(FormEditText f : formEditTexts){
            allValid = f.testValidity() && allValid; // IMPORTANT Vérifie les regexp
        }
        if (allValid) {
            runBDD.open();
            matiere = (IMatiere) matiereBdd.getWithName(spinnerMatiere.getSelectedItem().toString());
            note = matiere.creerNote();
            note.setNote(Double.valueOf(noteField.getText().toString()));
            note.setCoef(Integer.valueOf(coefField.getText().toString()));
            note.setId((int) noteBdd.insert(note));
            runBDD.getMatiereNoteBdd().insert(note,matiere);
            //IMoyenne moyenne = (IMoyenne) runBDD.getMoyenneMatiereBdd().getOtherObjetWithId(matiere.getId());
            runBDD.close();
            startActivity(new Intent(getApplicationContext(), AccueilActivity.class));
            finish();
        }
    }


    public void retourNoteButton(View view){
        startActivity(new Intent(getApplicationContext(), AccueilActivity.class));
        finish();
    }

}
