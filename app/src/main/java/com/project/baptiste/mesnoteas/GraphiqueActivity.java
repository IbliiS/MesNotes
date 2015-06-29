package com.project.baptiste.mesnoteas;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.echo.holographlibrary.Bar;
import com.echo.holographlibrary.BarGraph;
import com.project.baptiste.mesnoteas.R.color;

import com.echo.holographlibrary.Line;
import com.echo.holographlibrary.LineGraph;
import com.echo.holographlibrary.LinePoint;
import com.project.baptiste.mesnoteas.bdd.RunBDD;
import com.project.baptiste.mesnoteas.fragment.DialogGraphiqueFragment;
import com.project.baptiste.mesnoteas.general.interfaces.IAnnee;
import com.project.baptiste.mesnoteas.general.interfaces.IMatiere;
import com.project.baptiste.mesnoteas.general.interfaces.IMoyenne;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;
import com.project.baptiste.mesnoteas.graphique.BarGraphique;
import com.project.baptiste.mesnoteas.graphique.LineGraphique;
import com.project.baptiste.mesnoteas.utilitaire.Utilitaire;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baptiste on 27/06/2015.
 */
public class GraphiqueActivity extends AppCompatActivity {
    private final String Key_Extrat = "annee";
    private String selected_annee;
    private List<IObjet> moyennes = new ArrayList<>();
    private List<IObjet> matieres = new ArrayList<>();
    private IAnnee annee;
    private RunBDD runBDD;
    private LineGraphique lg;
    private Utilitaire utilitaire = new Utilitaire();
    private Spinner periodeSpinner;
    private BarGraphique bg;
    private FragmentActivity myContext;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphique);
        runBDD = RunBDD.getInstance(this);
        myContext=(FragmentActivity) this;
        periodeSpinner = (Spinner) findViewById(R.id.graphiqueSpinner);
        Intent intent = getIntent();
        if (intent != null) {
            selected_annee = intent.getStringExtra(Key_Extrat);
        }
        runBDD.open();
        annee = (IAnnee) runBDD.getAnneeBdd().getWithName(selected_annee);
        runBDD.close();
        initMoyennes();
        showAlertDialog();
        initToolbar();
        initLineChart();
        initSpinnerPeriode();

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (item.getItemId() == android.R.id.home) {
            startActivity(new Intent(getApplicationContext(), AccueilActivity.class));
            finish();
        }

        return super.onOptionsItemSelected(item);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.my_menu_without_add, menu);
        return super.onCreateOptionsMenu(menu);
    }

    private void initToolbar() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbarGraphique);
        toolbar.setLogo(R.drawable.ic_graphique_3);
        toolbar.setTitle("Graphiques");
        setSupportActionBar(toolbar);
        getSupportActionBar().setHomeButtonEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

    }

    private void showAlertDialog(){
        if(moyennes.size()==0){
            //afficher dialog
            myContext.getSupportFragmentManager();
            DialogGraphiqueFragment dialogGraphiqueFragment = new DialogGraphiqueFragment();
            dialogGraphiqueFragment.show(myContext.getFragmentManager(),null);
        }
    }

    private void initMoyennes(){
        runBDD.open();
        List<IObjet> listMoy = utilitaire.copyList(runBDD.getAnneeMoyenneBdd().getListObjetWithId(annee.getId()));
        List<IObjet> listMat = new ArrayList<>();
        IMoyenne m;
        for(IObjet o : listMoy){
            m = (IMoyenne) o;
            IMatiere mat;
            for(IObjet ob : runBDD.getMoyenneMatiereBdd().getListObjetWithId(m.getId())){
                mat = (IMatiere) ob;
                mat.setNotes(runBDD.getMatiereNoteBdd().getListObjetWithId(mat.getId()));
                if(mat.getNotes().size()>0) {
                    listMat.add(mat);
                }
            }
            m.setMatieres(utilitaire.copyList(listMat));
            if(m.getMatieres().size()>0) {
                moyennes.add(m);
            }
            listMat.clear();
        }
        runBDD.close();
    }

    private void initLineChart(){
        lg = new LineGraphique(moyennes);
        if(lg.getNbMoyennes()>0) {
            Line l = lg.getLine();
            Line ll = lg.getLineOrd();
            LineGraph li = (LineGraph) findViewById(R.id.graphiquePeriode);
            li.addLine(l);
            li.addLine(ll);
            li.setRangeY(0, (float) lg.getNoteMax());
            li.setRangeX(0, (float) (lg.getOrdX()-1.95));
//            li.setLineToFill(0);
        }
    }

    public void initBarChart(){
        bg = new BarGraphique(matieres);
        BarGraph g = (BarGraph)findViewById(R.id.graphiqueMatiere);
        g.setBackgroundColor(Color.parseColor("#FFFFFF"));
        g.setBars(bg.getBars());


    }

    public void initSpinnerPeriode(){
        final String selectionner = "--Pas de période--";
        periodeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item_selected = periodeSpinner.getSelectedItem().toString();
                if (!(item_selected.equals(selectionner))) {
                    IMoyenne m;
                    for(IObjet o : moyennes){
                        m = (IMoyenne) o;
                        if(m.getNomMoyenne().equals(item_selected)){
                            matieres.clear();
                            matieres = utilitaire.copyList(m.getMatieres());
                        }
                        initBarChart();
                    }
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
        List<String> exemple = new ArrayList<String>();
        if(moyennes.size()==0) {
            exemple.add(selectionner);
        }
        IMoyenne moy;
        for(IObjet o : moyennes){
            moy = (IMoyenne) o;
            exemple.add(moy.getNomMoyenne());
        }
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, exemple);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        periodeSpinner.setAdapter(adapter);
        periodeSpinner.setSelection(0);
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), AccueilActivity.class));
        finish();
        super.onBackPressed();
    }
}
