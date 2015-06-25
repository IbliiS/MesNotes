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
public class AjouterMoyenneActivity extends AppCompatActivity {
    private FormEditText nomMoyenne;
    private List<IObjet> moyennes;
    private List<String> moyenneString;
    private RunBDD runBDD;
    private IObjetBdd moyenneBdd;
    private IMoyenne moyenne;
    private List<IObjet> annees;
    private Spinner anneeSpinner;
    private boolean estSelect = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajouter_moyenne);
        initField();
        initToolbar();
        initVar();
        initAnneeSpinner();
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
            ajouterMoyenne();
        }
        if (item.getItemId() == android.R.id.home) {
            retourMoyenneButton();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAjouterMoyenne);
        toolbar.setTitle("Ajout Période");
        toolbar.setLogo(R.drawable.ic_periode);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

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
                    IMoyenne m;
                    for(IObjet o : moyennes){
                        m = (IMoyenne) o;
                        moyenneString.add(m.getNomMoyenne());
                    }
                    estSelect = true;
                }
                else estSelect = false;
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
        annees = runBDD.getAnneeBdd().getAll();
        moyenneBdd = runBDD.getMoyenneBdd();
        moyennes= moyenneBdd.getAll();
        moyenneString = new ArrayList<>();
        anneeSpinner = (Spinner) findViewById(R.id.ajouterMoyenneSpinnerAnnee);
    }

    private void initField() {
        nomMoyenne = (FormEditText) findViewById(R.id.nom_moyenneField);
    }

    public void ajouterMoyenne(){
        String nomAnnee = anneeSpinner.getSelectedItem().toString();
        String nomMoyenne = this.nomMoyenne.getText().toString();
        runBDD.open();
        IMoyenne m = (IMoyenne) runBDD.getMoyenneBdd().getWithName(nomMoyenne);
        runBDD.close();
        if(! (m.getNomMoyenne().equals(""))){
            Toast.makeText(getApplicationContext(),
                    "Ce nom existe déjà, changer de nom", Toast.LENGTH_LONG).show();
        }
        else{
            boolean allValid = true;
            FormEditText[] formEditTexts = {this.nomMoyenne};
            for(FormEditText f : formEditTexts){
                allValid = f.testValidity() && allValid;
            }
            if (allValid && estSelect) {
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

    public void retourMoyenneButton(){
        startActivity(new Intent(getApplicationContext(), AccueilActivity.class));
        finish();
    }
}
