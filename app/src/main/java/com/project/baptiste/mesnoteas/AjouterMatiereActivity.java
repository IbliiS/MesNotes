package com.project.baptiste.mesnoteas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.project.baptiste.mesnoteas.bdd.RunBDD;
import com.project.baptiste.mesnoteas.general.Matiere;
import com.project.baptiste.mesnoteas.general.Note;
import com.project.baptiste.mesnoteas.general.interfaces.IMatiere;

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


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajouter_matiere);
        runBDD = RunBDD.getInstance(this);
        initMatieres();
        initField();

    }

    public void initField(){
        ajouterMatiereButton = (Button) findViewById(R.id.ajouterMatiereBouton);
        nomMatiereField = (FormEditText) findViewById(R.id.nom_matiereField);
        coefMatiereField = (FormEditText) findViewById(R.id.coef_matiereField);

    }

    public void initMatieres(){
        matieres = new ArrayList<>();
        List<IMatiere> matiereList = runBDD.getMatiereBdd().getAllMatiere();
        for(IMatiere m : matiereList){
            matieres.add(m.getNomMatiere());
        }
    }

    public void ajouterMatiere(View view){
        String nomMatiere = nomMatiereField.getText().toString();
        // LA MATIERE EXISTE DEJA
        if(matieres.contains(nomMatiere)){
            Toast.makeText(getApplicationContext(),
                    "Ce nom existe déjà, changer de nom", Toast.LENGTH_LONG).show();
        }
        //ON AJOUTE LA MATIERE
        else{
            boolean allValid = true;
            FormEditText[] formEditTexts = {nomMatiereField, coefMatiereField};
            for(FormEditText f : formEditTexts){
                allValid = f.testValidity() && allValid; // IMPORTANT Vérifie les regexp
            }
            if (allValid) {
                matiere = new Matiere();
                matiere.setNomMatiere(String.valueOf(nomMatiereField.getText().toString()));
                matiere.setCoef(Integer.valueOf(coefMatiereField.getText().toString()));
                runBDD.getMatiereBdd().open();
                runBDD.getMatiereBdd().insertMatiere(matiere);
                runBDD.getMatiereBdd().close();
                startActivity(new Intent(getApplicationContext(), AjouterNoteActivity.class));
                finish();
            }
        }
    }

    public void retourMatiereButton(View view){
        startActivity(new Intent(getApplicationContext(), AjouterNoteActivity.class));
    }
}
