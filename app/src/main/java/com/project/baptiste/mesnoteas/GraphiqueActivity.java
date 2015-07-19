package com.project.baptiste.mesnoteas;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;
import android.widget.Spinner;


import com.project.baptiste.mesnoteas.bdd.RunBDD;
import com.project.baptiste.mesnoteas.fragment.DialogGraphiqueFragment;
import com.project.baptiste.mesnoteas.general.interfaces.IAnnee;
import com.project.baptiste.mesnoteas.general.interfaces.IMatiere;
import com.project.baptiste.mesnoteas.general.interfaces.IMoyenne;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;
import com.project.baptiste.mesnoteas.graphique.fabrique.FabriqueAChart;
import com.project.baptiste.mesnoteas.graphique.fabrique.FabriqueGraphAbs;
import com.project.baptiste.mesnoteas.graphique.fabrique.FabriqueHolo;
import com.project.baptiste.mesnoteas.spinner.MySpinner;
import com.project.baptiste.mesnoteas.spinner.MySpinnerBlack;
import com.project.baptiste.mesnoteas.utilitaire.Utilitaire;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Baptiste on 27/06/2015.
 */
public class GraphiqueActivity extends AppCompatActivity {
    private final String Key_Extrat = "annee";
    private final String Key_Extrat_TypeGraph = "typeGraph";
    private final String HOLOGRAPH = "HoloGraph";
    private final String MPCHART = "MpChart";
    private String selected_annee;
    private List<IObjet> annees = new ArrayList<>();
    private List<IObjet> moyennes = new ArrayList<>();
    private List<IObjet> matieres = new ArrayList<>();
    private IAnnee annee;
    private RunBDD runBDD;
    private Utilitaire utilitaire = new Utilitaire();
    private Spinner periodeSpinner;
    private Spinner graphSpinnerAnnee;
    private FragmentActivity myContext;
    private String selected_chart;
    private FabriqueGraphAbs fabriqueGraphAbs;
    private RelativeLayout barLayout;
    private RelativeLayout lineLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graphique);
        runBDD = RunBDD.getInstance(this);
        myContext =  this;
        periodeSpinner = (Spinner) findViewById(R.id.graphiqueSpinner);
        graphSpinnerAnnee = (Spinner) findViewById(R.id.graphiqueSpinnerMoyenne);
        barLayout = (RelativeLayout) findViewById(R.id.barLayout);
        lineLayout = (RelativeLayout) findViewById(R.id.lineLayout);
        initFabrique();
        runBDD.open();
        annee = (IAnnee) runBDD.getAnneeBdd().getWithName(selected_annee);
        runBDD.close();
        initAnnee();
        initSpinnerAnnee();
        initToolbar();
    }

    private void initFabrique() {
        Intent intent = getIntent();
        if (intent != null) {
            selected_annee = intent.getStringExtra(Key_Extrat);
            selected_chart = intent.getStringExtra(Key_Extrat_TypeGraph);
            switch (selected_chart){
                case MPCHART :
                    fabriqueGraphAbs = new FabriqueAChart();
                    makeHoloGraphInvisible();
                    break;
                case HOLOGRAPH :
                    fabriqueGraphAbs = new FabriqueHolo();
                    break;
            }
        }
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
        if(moyennes.size()==0 || annees.size() == 0){
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
        moyennes.clear();
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
        initLine();
    }

    public void initAnnee(){
        annees = runBDD.getAnneeBdd().getAll();
    }

    public void initSpinnerAnnee(){
        final String selectionner = "--Pas de période--";
        MySpinner ms = new MySpinnerBlack();
        graphSpinnerAnnee = ms.creerSpinner(graphSpinnerAnnee,annees,selectionner,this,annees.size() == 0);
        graphSpinnerAnnee.setSelection(ms.getIndexOfItem(annee.getNomAnnee()));
        graphSpinnerAnnee.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item_selected = graphSpinnerAnnee.getSelectedItem().toString();
                if (!(item_selected.equals(selectionner))) {
                    selected_annee = item_selected;
                    runBDD.open();
                    annee = (IAnnee) runBDD.getAnneeBdd().getWithName(selected_annee);
                    runBDD.close();
                    initMoyennes();
                    initSpinnerPeriode();
                }
                showAlertDialog();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
    }

    public void initSpinnerPeriode(){
        final String selectionner = "--Pas de période--";
        MySpinner ms = new MySpinnerBlack();
        periodeSpinner = ms.creerSpinner(periodeSpinner, utilitaire.copyList(moyennes),selectionner,this,moyennes.size() == 0);
        periodeSpinner.setSelection(0);
        periodeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item_selected = periodeSpinner.getSelectedItem().toString();
                matieres.clear();
                if (!(item_selected.equals(selectionner))) {
                    IMoyenne m;
                    for (IObjet o : moyennes) {
                        m = (IMoyenne) o;
                        if (m.getNomMoyenne().equals(item_selected)) {
                            matieres = utilitaire.copyList(m.getMatieres());
                        }
                    }
                }
                initBar();
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                return;
            }
        });
    }

    @Override
    public void onBackPressed() {
        startActivity(new Intent(getApplicationContext(), AccueilActivity.class));
        finish();
        super.onBackPressed();
    }

    /** GRAPHIQUE HOLO GRAPH **/

    public void initBar(){
        barLayout.removeAllViews();
        barLayout.addView(fabriqueGraphAbs.creerBar(utilitaire.copyList(matieres), this).getGraph());
    }

    public void initLine(){
        lineLayout.removeAllViews();
        lineLayout.addView(fabriqueGraphAbs.creerLine(moyennes, this).getGraph());
    }


    public void makeHoloGraphInvisible(){
        FrameLayout f = (FrameLayout) findViewById(R.id.graphiqueFrame1);
        f.setVisibility(View.INVISIBLE);
        f = (FrameLayout) findViewById(R.id.graphiqueFrame2);
        f.setVisibility(View.INVISIBLE);
        f = (FrameLayout) findViewById(R.id.graphiqueFrame3);
        f.setVisibility(View.INVISIBLE);
        f = (FrameLayout) findViewById(R.id.graphiqueFrame4);
        f.setVisibility(View.INVISIBLE);
    }


}
