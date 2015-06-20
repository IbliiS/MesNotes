package com.project.baptiste.mesnoteas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.project.baptiste.mesnoteas.bdd.RunBDD;
import com.project.baptiste.mesnoteas.general.Annee;
import com.project.baptiste.mesnoteas.general.Moyenne;
import com.project.baptiste.mesnoteas.general.interfaces.IAnnee;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baptiste on 19/06/2015.
 */
public class AjouterAnneeActivity extends Activity{
    private FormEditText nomAnnee;
    private List<IObjet> annees;
    private List<String> anneeString;
    private RunBDD runBDD;
    private IAnnee annee;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajouter_annee);
        initField();
        initVar();
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

    public void ajouterAnnee(View view){
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
                startActivity(new Intent(getApplicationContext(), AccueilActivity.class));
                finish();
            }
        }
    }

    public void retourAnneeBouton(View view){
        startActivity(new Intent(getApplicationContext(), AccueilActivity.class));
        finish();
    }
}
