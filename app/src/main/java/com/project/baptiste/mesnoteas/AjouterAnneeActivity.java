package com.project.baptiste.mesnoteas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.ActionMode;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.Toast;
import android.support.v7.widget.Toolbar;
import android.support.v7.app.AppCompatActivity.*;
import com.andreabaccega.widget.FormEditText;
import com.project.baptiste.mesnoteas.bdd.RunBDD;
import com.project.baptiste.mesnoteas.general.Annee;
import com.project.baptiste.mesnoteas.general.interfaces.IAnnee;
import com.project.baptiste.mesnoteas.general.interfaces.IMatiere;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;
import com.project.baptiste.mesnoteas.listAdapter.AnneeListViewAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baptiste on 19/06/2015.
 */
public class AjouterAnneeActivity extends AppCompatActivity {
    private FormEditText nomAnnee;
    private List<IObjet> annees;
    private List<String> anneeString;
    private RunBDD runBDD;
    private IAnnee annee;
    private int countSelectItem = 0;
    private List<IObjet> list_selected = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajouter_annee);
        initField();
        initVar();
        initToolbar();
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
            ajouterAnnee();
        }
        if (item.getItemId() == android.R.id.home) {
            retourAnneeBouton();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAjouterAnnee);
        toolbar.setTitle("Ajout Année");
        toolbar.setLogo(R.drawable.ic_annee);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


    }

    private void initListView() {
        AnneeListViewAdapter anneeListViewAdapter = new AnneeListViewAdapter(annees,this);
        ListView listView = (ListView) findViewById(R.id.listViewAnnee);
        listView.setAdapter(anneeListViewAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (list_selected.contains(annees.get(position))) {
                    countSelectItem--;
                    list_selected.remove(annees.get(position));
                } else {
                    countSelectItem++;
                    list_selected.add(annees.get(position));
                }
                mode.setTitle(countSelectItem + " Année(s) select.");
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
                        supprimerAnnee(list_selected);
                        Toast.makeText(getBaseContext(), countSelectItem + " Année(s) supprimée(s)", Toast.LENGTH_LONG).show();
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                countSelectItem = 0;
                annees = runBDD.getAnneeBdd().getAll();
                list_selected.clear();
                initListView();
            }

        });
    }

    private void supprimerAnnee(List<IObjet> list_selected) {
        runBDD.open();
        IAnnee a;
        for(IObjet o : list_selected){
            a = (IAnnee) o;
            runBDD.getAnneeMoyenneBdd().removeWithID(a.getId());
        }
        runBDD.close();
    }

    private void initVar() {
        runBDD = RunBDD.getInstance(this);
        annees = runBDD.getAnneeBdd().getAll();
        anneeString = new ArrayList<>();
        IAnnee a;
        for(IObjet o : annees){
            a = (IAnnee) o;
            anneeString.add(a.getNomAnnee());
        }
    }

    private void initField() {
        nomAnnee = (FormEditText) findViewById(R.id.nom_anneeField);
    }

    public void ajouterAnnee(){
        String nomAnnee = this.nomAnnee.getText().toString();
        if(anneeString.contains(nomAnnee)){
            Toast.makeText(getApplicationContext(),
                    "Ce nom existe déjà, changer de nom", Toast.LENGTH_LONG).show();
        }
        else {
            boolean allValid = true;
            FormEditText[] formEditTexts = {this.nomAnnee};
            for(FormEditText f : formEditTexts){
                allValid = f.testValidity() && allValid;
            }
            if (allValid) {
                annee = new Annee();
                annee.setNomAnnee(String.valueOf(nomAnnee));
                runBDD.getAnneeBdd().open();
                runBDD.getAnneeBdd().insert(annee);
                runBDD.getAnneeBdd().close();
                startActivity(new Intent(getApplicationContext(), AjouterAnneeActivity.class));
                finish();
                Toast.makeText(getApplicationContext(), "Année ajoutée", Toast.LENGTH_LONG).show();
            }
        }
    }

    public void retourAnneeBouton(){
        startActivity(new Intent(getApplicationContext(), AccueilActivity.class));
        finish();
    }
}
