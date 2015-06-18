package com.project.baptiste.mesnoteas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.project.baptiste.mesnoteas.bdd.IObjetBdd;
import com.project.baptiste.mesnoteas.bdd.RunBDD;
import com.project.baptiste.mesnoteas.general.Matiere;
import com.project.baptiste.mesnoteas.general.Moyenne;
import com.project.baptiste.mesnoteas.general.Note;
import com.project.baptiste.mesnoteas.general.interfaces.IMatiere;
import com.project.baptiste.mesnoteas.general.interfaces.IMoyenne;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baptiste on 14/06/2015.
 */
public class AjouterMatiereActivity extends Activity {
    private RunBDD runBDD;
    private Button ajouterMatiereButton;
    private FormEditText nomMatiereField;
    private FormEditText coefMatiereField;
    private List<String> matieres;
    private IMatiere matiere;
    private IObjetBdd matiereBdd;
    private Spinner ajouterMatiereSpinnerMoyenne;
    private List<IObjet> moyennes;
    private List<String> moyennesString;
    private boolean[] tousValides = {false,false};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajouter_matiere);
        runBDD = RunBDD.getInstance(this);
        matiereBdd = runBDD.getMatiereBdd();
        initMatieres();
        initMoyennes();
        initField();
        initSpinnerMoyenne();

    }

    private void initSpinnerMoyenne() {
        moyennes = new ArrayList<>();
        moyennes = runBDD.getMoyenneBdd().getAll();
        final String selectionner = "-- Selectionner une période --";
        ajouterMatiereSpinnerMoyenne = (Spinner) findViewById(R.id.ajouterMatiereSpinnerMoyenne);
        ajouterMatiereSpinnerMoyenne.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item_selected = ajouterMatiereSpinnerMoyenne.getSelectedItem().toString();
                if (!(item_selected.equals(selectionner))) {
                    tousValides[1]=true;
                   ajouterMatiereButton.setEnabled(true);
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
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, exemple);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ajouterMatiereSpinnerMoyenne.setAdapter(adapter);
        ajouterMatiereSpinnerMoyenne.setSelection(0);
    }

    public void initField(){
        ajouterMatiereButton = (Button) findViewById(R.id.ajouterMatiereBouton);
        nomMatiereField = (FormEditText) findViewById(R.id.nom_matiereField);
        coefMatiereField = (FormEditText) findViewById(R.id.coef_matiereField);
        ajouterMatiereButton.setEnabled(false);


    }

    public void initMatieres(){
        matieres = new ArrayList<>();
        List<IObjet> matiereList = matiereBdd.getAll();
        IMatiere m;
        for(IObjet o : matiereList){
            m = (IMatiere) o;
            matieres.add(m.getNomMatiere());
        }
    }

    public void initMoyennes(){
        moyennesString = new ArrayList<>();
        List<IObjet> moyenneList = runBDD.getMoyenneBdd().getAll();
        IMoyenne m;
        for(IObjet o : moyenneList){
            m = (IMoyenne) o;
            moyennesString.add(m.getNomMoyenne());
        }
    }

    public void ajouterMatiere(View view){
        verifMatiere();
        if( tousValides[0] && tousValides[1]){
            boolean allValid = true;
            FormEditText[] formEditTexts = {nomMatiereField, coefMatiereField};
            for(FormEditText f : formEditTexts){
                allValid = f.testValidity() && allValid; // IMPORTANT Vérifie les regexp
            }
            if (allValid) {
                runBDD.open();
                IMoyenne moyenne = (IMoyenne) runBDD.getMoyenneBdd().getWithName(ajouterMatiereSpinnerMoyenne.getSelectedItem().toString());
                matiere = moyenne.creerMatiere();
                matiere.setNomMatiere(String.valueOf(nomMatiereField.getText().toString()));
                matiere.setCoef(Integer.valueOf(coefMatiereField.getText().toString()));
                matiere.setId((int) matiereBdd.insert(matiere));
                int i = (int) runBDD.getMoyenneMatiereBdd().insert(matiere, moyenne);
                System.out.println(i);
                runBDD.close();
                startActivity(new Intent(getApplicationContext(), AjouterNoteActivity.class));
                finish();
            }
        }

    }

    public void verifMatiere(){
        String nomMatiere = nomMatiereField.getText().toString();
        // LA MATIERE EXISTE DEJA
        if(matieres.contains(nomMatiere)){
            Toast.makeText(getApplicationContext(),
                    "Ce nom existe déjà, changer de nom", Toast.LENGTH_LONG).show();
        }
        else tousValides[0]=true;
    }


    public void retourMatiereButton(View view){
        startActivity(new Intent(getApplicationContext(), AjouterNoteActivity.class));
        finish();
    }
}
