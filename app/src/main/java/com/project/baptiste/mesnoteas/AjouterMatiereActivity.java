package com.project.baptiste.mesnoteas;

import android.content.Intent;
import android.os.Bundle;
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
import com.project.baptiste.mesnoteas.general.interfaces.IMatiere;
import com.project.baptiste.mesnoteas.general.interfaces.IMoyenne;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;
import com.project.baptiste.mesnoteas.listAdapter.MatiereListViewAdapter;
import com.project.baptiste.mesnoteas.spinner.MySpinner;
import com.project.baptiste.mesnoteas.spinner.MySpinnerBlack;
import com.project.baptiste.mesnoteas.utilitaire.Utilitaire;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baptiste on 14/06/2015.
 */
public class AjouterMatiereActivity extends AppCompatActivity {
    private RunBDD runBDD;
    private FormEditText nomMatiereField;
    private FormEditText coefMatiereField;
    private IMatiere matiere;
    private Spinner ajouterMatiereSpinnerMoyenne;
    private List<IObjet> moyennes = new ArrayList<>();
    private boolean[] tousValides = {false,false};
    private int countSelectItem = 0;
    private List<IObjet> list_selected = new ArrayList<>();
    private List<IObjet> listMatieres;
    private Utilitaire utilitaire = new Utilitaire();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajouter_matiere);
        runBDD = RunBDD.getInstance(this);
        listMatieres = runBDD.getMatiereBdd().getAll();
        initToolbar();
        initListView();
        initField();
        initSpinnerMoyenne();

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
            ajouterMatiere();
        }
        if (item.getItemId() == android.R.id.home) {
            retourMatiereButton();
        }

        return super.onOptionsItemSelected(item);
    }

    private void initListView() {
        MatiereListViewAdapter matiereListViewAdapter = new MatiereListViewAdapter(listMatieres,this);
        ListView listView = (ListView) findViewById(R.id.listViewMatiere);
        listView.setAdapter(matiereListViewAdapter);
        listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
        listView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode mode, int position, long id, boolean checked) {
                if (list_selected.contains(listMatieres.get(position))) {
                    countSelectItem--;
                    list_selected.remove(listMatieres.get(position));
                } else {
                    countSelectItem++;
                    list_selected.add(listMatieres.get(position));
                }
                mode.setTitle(countSelectItem + " Matière(s) select.");
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
                        supprimerMatiere(list_selected);
                        Toast.makeText(getBaseContext(), countSelectItem + " matière(s) supprimée(s)", Toast.LENGTH_LONG).show();
                        mode.finish();
                        return true;
                    default:
                        return false;
                }
            }

            @Override
            public void onDestroyActionMode(ActionMode mode) {
                countSelectItem = 0;
                listMatieres = runBDD.getMatiereNoteBdd().getAll();
                list_selected.clear();
                initListView();
                initSpinnerMoyenne();
            }
        });
    }

    private void supprimerMatiere(List<IObjet> list_selected) {
        runBDD.open();
        IMatiere m;
        for(IObjet o : list_selected){
            m = (IMatiere) o;
            runBDD.getMatiereNoteBdd().removeWithID(m.getId());
        }
        runBDD.close();
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarAjouterMatiere);
        toolbar.setTitle("Ajout Matière");
        toolbar.setLogo(R.drawable.ic_matiere);
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    private void initSpinnerMoyenne() {
        moyennes = runBDD.getMoyenneBdd().getAll();
        final String selectionner = "-- Selectionner une période --";
        ajouterMatiereSpinnerMoyenne = (Spinner) findViewById(R.id.ajouterMatiereSpinnerMoyenne);
        MySpinner ms = new MySpinnerBlack();
        ajouterMatiereSpinnerMoyenne = ms.creerSpinner(ajouterMatiereSpinnerMoyenne,utilitaire.copyList(moyennes),selectionner,this,moyennes.size() == 0);
        ajouterMatiereSpinnerMoyenne.setSelection(0);

        ajouterMatiereSpinnerMoyenne.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item_selected = ajouterMatiereSpinnerMoyenne.getSelectedItem().toString();
                if (!(item_selected.equals(selectionner))) {
                    tousValides[1] = true;
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
    }

    public void initField(){
        nomMatiereField = (FormEditText) findViewById(R.id.nom_matiereField);
        coefMatiereField = (FormEditText) findViewById(R.id.coef_matiereField);

    }

    public void verifMatiere(){
        String nomMatiere = nomMatiereField.getText().toString();
        runBDD.open();
        IMatiere m = (IMatiere) runBDD.getMatiereBdd().getWithName(nomMatiere);
        runBDD.close();
        if( !(m.getNomMatiere().equals("")) ){
            Toast.makeText(getApplicationContext(), "Ce nom existe déjà, changer de nom", Toast.LENGTH_LONG).show();
        }
        else tousValides[0]=true;
    }


    public void ajouterMatiere(){
        verifMatiere();
        if( tousValides[0] && tousValides[1]){
            boolean allValid = true;
            if(coefMatiereField.getText().toString().equals("")){
                coefMatiereField.setText("1");
            }
            FormEditText[] formEditTexts = {nomMatiereField, coefMatiereField};
            for(FormEditText f : formEditTexts){
                allValid = f.testValidity() && allValid; // IMPORTANT Vérifie les regexp
            }
            if (allValid) {
                runBDD.open();
                String nomMoyenne = ajouterMatiereSpinnerMoyenne.getSelectedItem().toString();
                IMoyenne moyenne = (IMoyenne) runBDD.getMoyenneBdd().getWithName(nomMoyenne);
                matiere = moyenne.creerMatiere();
                matiere.setNomMatiere(String.valueOf(nomMatiereField.getText().toString()));
                matiere.setCoef(Integer.valueOf(coefMatiereField.getText().toString()));
                matiere.setId((int) runBDD.getMatiereBdd().insert(matiere));
                runBDD.getMoyenneMatiereBdd().insert(matiere, moyenne);
                runBDD.close();
                startActivity(new Intent(getApplicationContext(), AjouterMatiereActivity.class));
                finish();
                Toast.makeText(getApplicationContext(), "Matière ajoutée dans " +nomMoyenne, Toast.LENGTH_LONG).show();
            }
        }
        else{
            Toast.makeText(getApplicationContext(), "Selectionner une période", Toast.LENGTH_LONG).show();

        }

    }

    public void retourMatiereButton(){
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
