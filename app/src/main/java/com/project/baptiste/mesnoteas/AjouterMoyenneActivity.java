package com.project.baptiste.mesnoteas;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.Layout;
import android.view.View;
import android.widget.Toast;

import com.andreabaccega.widget.FormEditText;
import com.project.baptiste.mesnoteas.bdd.IObjetBdd;
import com.project.baptiste.mesnoteas.bdd.RunBDD;
import com.project.baptiste.mesnoteas.general.Matiere;
import com.project.baptiste.mesnoteas.general.Moyenne;
import com.project.baptiste.mesnoteas.general.interfaces.IMatiere;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ajouter_moyenne);
        initField();
        initVar();


    }

    private void initVar() {
        runBDD = RunBDD.getInstance(this);
        moyenneBdd = runBDD.getMoyenneBdd();
        moyennes= moyenneBdd.getAll();
        moyenneString = new ArrayList<>();
        IMoyenne m;
        for(IObjet o : moyennes){
            m = (IMoyenne) o;
            moyenneString.add(m.getNomMoyenne());
        }

    }

    private void initField() {
        nomMoyenne = (FormEditText) findViewById(R.id.nom_moyenneField);
    }

    public void ajouterMoyenne(View view){
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
                moyenne = new Moyenne();
                moyenne.setNomMoyenne(String.valueOf(nomMoyenne));
                moyenneBdd.open();
                moyenneBdd.insert(moyenne);
                moyenneBdd.close();
                startActivity(new Intent(getApplicationContext(), AccueilActivity.class));
                finish();
            }
        }
    }

    public void retourMoyenneButton(View view){
        startActivity(new Intent(getApplicationContext(), AccueilActivity.class));
    }
}
