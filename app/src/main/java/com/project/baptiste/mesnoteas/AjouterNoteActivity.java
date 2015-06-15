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
import com.project.baptiste.mesnoteas.bdd.RunBDD;
import com.project.baptiste.mesnoteas.general.interfaces.IMatiere;
import com.project.baptiste.mesnoteas.general.interfaces.INote;

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
    private List<IMatiere> matieres;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajouter_note);
        runBDD = RunBDD.getInstance(this);
        initVariable();
        initField();
        initSpinner();
    }

    public void initVariable(){
        matieres = new ArrayList<>();
        matieres = runBDD.getMatiereBdd().getAllMatiere();
    }

    public void initSpinner(){
        final String selectionner = "-- Selectionner une matiere --";
        final String ajouter = "+ Ajouter une matière";
        spinner = (Spinner) findViewById(R.id.spinner);
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                                              @Override
                                              public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                                                  String item_selected = spinner.getSelectedItem().toString();
                                                  if( ! (item_selected.equals(selectionner)) ){
                                                      if(item_selected.equals(ajouter)){
                                                          startActivity(new Intent(getApplicationContext(), AjouterMatiereActivity.class));
                                                          finish();
                                                      }
                                                      else buttonAjouter.setEnabled(true);
                                                  }
                                              }

                                              @Override
                                              public void onNothingSelected(AdapterView<?> parent) {
                                                return;
                                              }
                                          });
        List<String> exemple = new ArrayList<String>();
        exemple.add(selectionner);
        for(IMatiere m : matieres){
            exemple.add(m.getNomMatiere());
        }
        exemple.add(ajouter);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, exemple);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(0);
    }

    public void initField(){
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
            runBDD.getNoteBdd().open();
            matiere = runBDD.getMatiereBdd().getMatiereWithName(spinner.getSelectedItem().toString());
            System.out.println(spinner.getSelectedItem().toString());
            note = matiere.creerNote();
            note.setNote(Double.valueOf(noteField.getText().toString()));
            note.setCoef(Integer.valueOf(coefField.getText().toString()));
            note.setId((int) runBDD.getNoteBdd().insertNote(note));
            runBDD.getMatiereNoteBdd().insertMatiereNote(note,matiere);
            runBDD.getNoteBdd().close();
            startActivity(new Intent(getApplicationContext(), AccueilActivity.class));
            finish();
        }
    }

    public void retourNoteButton(View view){
        startActivity(new Intent(getApplicationContext(), AccueilActivity.class));
        finish();
    }

}
