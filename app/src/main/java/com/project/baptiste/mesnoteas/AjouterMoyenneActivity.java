package com.project.baptiste.mesnoteas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.project.baptiste.mesnoteas.bdd.interfacesBdd.IObjetBdd;
import com.project.baptiste.mesnoteas.bdd.RunBDD;
import com.project.baptiste.mesnoteas.general.Moyenne;
import com.project.baptiste.mesnoteas.general.interfaces.IAnnee;
import com.project.baptiste.mesnoteas.general.interfaces.IMoyenne;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baptiste on 15/06/2015.
 */
public class AjouterMoyenneActivity extends Activity {
    private FormEditText nomMoyenne;
    private List<IObjet> moyennes;
    private List<String> moyenneString;
    private RunBDD runBDD;
    private IObjetBdd moyenneBdd;
    private IMoyenne moyenne;
    private List<IObjet> annees;
    private Spinner anneeSpinner;
    private Button ajouterButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajouter_moyenne);
        initField();
        initVar();
        initAnneeSpinner();


    }

    public void initAnneeSpinner(){
        final String selectionner = "-- Selectionner une année --";
        anneeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item_selected = anneeSpinner.getSelectedItem().toString();
                if (!(item_selected.equals(selectionner))) {
                    runBDD.open();
                    IAnnee annee = (IAnnee) runBDD.getAnneeBdd().getWithName(item_selected);
                    moyennes.clear();
                    moyennes = runBDD.getAnneeMoyenneBdd().getListObjetWithId(annee.getId());
                    runBDD.close();
                    ajouterButton.setEnabled(true);
                    IMoyenne m;
                    for(IObjet o : moyennes){
                        m = (IMoyenne) o;
                        moyenneString.add(m.getNomMoyenne());
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

    private void initVar() {
        runBDD = RunBDD.getInstance(this);
        annees = new ArrayList<>();
        annees = runBDD.getAnneeMoyenneBdd().getAll();
        moyenneBdd = runBDD.getMoyenneBdd();
        moyennes= moyenneBdd.getAll();
        moyenneString = new ArrayList<>();
        anneeSpinner = (Spinner) findViewById(R.id.ajouterMoyenneSpinnerAnnee);
    }

    private void initField() {
        ajouterButton = (Button) findViewById(R.id.ajouterMoyenneBouton);
        ajouterButton.setEnabled(false);
        nomMoyenne = (FormEditText) findViewById(R.id.nom_moyenneField);
    }

    public void ajouterMoyenne(View view){
        String nomAnnee = anneeSpinner.getSelectedItem().toString();
        String nomMoyenne = this.nomMoyenne.getText().toString();
        if(moyenneString.contains(nomMoyenne)){
            Toast.makeText(getApplicationContext(),
                    "Ce nom existe déjà, changer de nom", Toast.LENGTH_LONG).show();
        }
        else{
            boolean allValid = true;
            FormEditText[] formEditTexts = {this.nomMoyenne};
            for(FormEditText f : formEditTexts){
                allValid = f.testValidity() && allValid;
            }
            if (allValid) {
                runBDD.open();
                IAnnee annee = (IAnnee) runBDD.getAnneeBdd().getWithName(nomAnnee);
                moyenne = annee.creerMoyenne();
                moyenne.setNomMoyenne(String.valueOf(nomMoyenne));
                moyenne.setId((int) runBDD.getMoyenneBdd().insert(moyenne));
                runBDD.getAnneeMoyenneBdd().insert(moyenne,annee);
                moyenneBdd.close();
                startActivity(new Intent(getApplicationContext(), AccueilActivity.class));
                finish();
            }
        }
    }

    public void retourMoyenneButton(View view){
        startActivity(new Intent(getApplicationContext(), AccueilActivity.class));
        finish();
    }
}
