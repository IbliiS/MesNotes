package com.project.baptiste.mesnoteas;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.project.baptiste.mesnoteas.bdd.interfacesBdd.IObjetBdd;
import com.project.baptiste.mesnoteas.bdd.RunBDD;
import com.project.baptiste.mesnoteas.fragment.DialogModification;
import com.project.baptiste.mesnoteas.fragment.DialogModificationMoyenne;
import com.project.baptiste.mesnoteas.general.interfaces.IAnnee;
import com.project.baptiste.mesnoteas.general.interfaces.IMoyenne;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;
import com.project.baptiste.mesnoteas.listAdapter.MoyenneListViewAdapter;
import com.project.baptiste.mesnoteas.utilitaire.Utilitaire;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baptiste on 15/06/2015.
 */
public class AjouterMoyenneActivity extends AppCompatActivity {
    private FormEditText nomMoyenne;
    private List<IObjet> moyennes;
    private RunBDD runBDD;
    private IObjetBdd moyenneBdd;
    private IMoyenne moyenne;
    private List<IObjet> annees;
    private Spinner anneeSpinner;
    private boolean estSelect = false;
    private int countSelectItem = 0;
    private List<IObjet> list_selected = new ArrayList<>();
    private IMoyenne moyenneAModifier;
    private FragmentActivity myContext;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajouter_moyenne);
        myContext = this;
        initField();
        initToolbar();
        initVar();
        initAnneeSpinner();
        initListView();
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

    private void supprimerMoyenne(List<IObjet> list_selected) {
        runBDD.open();
        IMoyenne m;
        for(IObjet o : list_selected){
            m = (IMoyenne) o;
            runBDD.getMoyenneMatiereBdd().removeWithID(m.getId());
        }
        runBDD.close();
    }

    private void initListView() {
        Utilitaire u = new Utilitaire();
        MoyenneListViewAdapter moyenneListViewAdapter = new MoyenneListViewAdapter(u.copyList(moyennes),this);
        ListView listView = (ListView) findViewById(R.id.listViewMoyenne);
        listView.setAdapter(moyenneListViewAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (list_selected.contains(moyennes.get(position))) {
                    countSelectItem--;
                    list_selected.remove(moyennes.get(position));
                } else {
                    countSelectItem++;
                    list_selected.add(moyennes.get(position));
                }
                mode.setTitle(countSelectItem + " Moyenne(s) select.");
            }

            @Override
            public boolean onCreateActionMode(ActionMode mode, Menu menu) {
                MenuInflater inflater = mode.getMenuInflater();
                inflater.inflate(R.menu.menu_suppress, menu);
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.suppressItem:
                        supprimerMoyenne(list_selected);
                        Toast.makeText(getBaseContext(), countSelectItem + " moyenne(s) supprimée(s)", Toast.LENGTH_LONG).show();
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                countSelectItem = 0;
                moyennes = runBDD.getMoyenneBdd().getAll();
                list_selected.clear();
                initListView();
                initAnneeSpinner();
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                myContext.getSupportFragmentManager();
                DialogModification d = new DialogModificationMoyenne();
                d.setRefresh(new Refresh());
                d.setData(moyennes.get(position));
                d.setRunBDD(runBDD);
                d.show(myContext.getFragmentManager(), null);
                moyenneAModifier = (IMoyenne) moyennes.get(position);
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
                    runBDD.open();
                    IAnnee annee = (IAnnee) runBDD.getAnneeBdd().getWithName(item_selected);
                    runBDD.close();
                    IMoyenne m;
                    estSelect = true;
                }
                else {
                    estSelect = false;
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
        annees = runBDD.getAnneeBdd().getAll();
        moyenneBdd = runBDD.getMoyenneBdd();
        moyennes = runBDD.getMoyenneBdd().getAll();
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
                if(moyenneAModifier != null){
                    runBDD.getAnneeMoyenneBdd().updateOtherObject(moyenneAModifier.getId(),moyenne,annee);
                }
                else {
                    moyenne.setId((int) runBDD.getMoyenneBdd().insert(moyenne));
                    runBDD.getAnneeMoyenneBdd().insert(moyenne, annee);
                }
                moyenneBdd.close();
                startActivity(new Intent(getApplicationContext(), AjouterMoyenneActivity.class));
                finish();
                Toast.makeText(getApplicationContext(), "Période ajoutée dans "+nomAnnee, Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(getApplicationContext(), "Selectionner une année", Toast.LENGTH_LONG).show();

            }
        }
    }

    public void retourMoyenneButton(){
        startActivity(new Intent(getApplicationContext(), AccueilActivity.class));
        finish();
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), AccueilActivity.class));
        finish();
        super.onBackPressed();
    }

    public class Refresh{
        public void refresh(){
            moyennes = runBDD.getMoyenneBdd().getAll();
            initListView();
            initAnneeSpinner();
        }
        public void modifier(){
            nomMoyenne.setText(moyenneAModifier.getNomMoyenne());
        }
    }
}
