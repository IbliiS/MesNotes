package com.project.baptiste.mesnoteas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

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
public class AjouterNoteActivity extends AppCompatActivity {
    private final String Key_Modifier_Note = "note";
    private RunBDD runBDD;
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
    private boolean estSelect = false;
    private INote noteADelete;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajouter_note);
        initVariable();
        initToolbar();
        initField();
        initIntent();
    }

    private void initIntent() {
        Intent intent = getIntent();
        if(intent != null && (intent.getStringExtra(Key_Modifier_Note)!=null) ){
            runBDD.open();
            int id = Integer.parseInt(intent.getStringExtra(Key_Modifier_Note));
            noteADelete = (INote) runBDD.getNoteBdd().getWithId(id);
            runBDD.close();
            noteField.setText(Double.toString(noteADelete.getNote()));
            coefField.setText(Integer.toString(noteADelete.getCoef()));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        MenuInflater myMenu = getMenuInflater();
        myMenu.inflate(R.menu.my_menu, menu);
        return true;

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if(id == R.id.addOne){
            ajouterNote();
        }
        if (item.getItemId() == android.R.id.home) {
            retourNoteButton();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAjouterNote);
        toolbar.setTitle("Ajout Note");
        toolbar.setLogo(R.drawable.ic_note);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
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
                    estSelect = true;
                }
                else{
                    estSelect = false;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
    }


    public void ajouterNote(){
        boolean allValid = true;
        if(coefField.getText().toString().equals("")){
            coefField.setText("1");
        }
        FormEditText[] formEditTexts = {noteField, coefField};
        for(FormEditText f : formEditTexts){
            allValid = f.testValidity() && allValid; // IMPORTANT Vérifie les regexp
        }
        if (allValid && estSelect) {
            runBDD.open();
            matiere = (IMatiere) matiereBdd.getWithName(spinnerMatiere.getSelectedItem().toString());
            note = matiere.creerNote();
            note.setNote(Double.valueOf(noteField.getText().toString()));
            note.setCoef(Integer.valueOf(coefField.getText().toString()));
            /** Si c'est une modification **/
            if(noteADelete != null){
                //runBDD.getMatiereNoteBdd().removeOtherObjectWithID(noteADelete.getId());
                runBDD.getMatiereNoteBdd().updateOtherObject(noteADelete.getId(),note, matiere);
            }
            /** Si c'est un insert **/
            else {
                note.setId((int) noteBdd.insert(note));
                runBDD.getMatiereNoteBdd().insert(note, matiere);
            }
            runBDD.close();
//            startActivity(new Intent(getApplicationContext(), AjouterNoteActivity.class));
//            finish();
            noteField.setText("");
            coefField.setText("");
            Toast.makeText(getApplicationContext(), "Note ajoutée dans " +matiere.getNomMatiere() , Toast.LENGTH_LONG).show();
        }
        else{
            Toast.makeText(getApplicationContext(), "Selectionner une matière", Toast.LENGTH_LONG).show();
        }
    }


    public void retourNoteButton(){
        startActivity(new Intent(getApplicationContext(), AccueilActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), AccueilActivity.class));
        finish();
        super.onBackPressed();
    }

}
