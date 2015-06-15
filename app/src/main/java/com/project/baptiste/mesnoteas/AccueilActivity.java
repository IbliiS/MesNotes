package com.project.baptiste.mesnoteas;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.project.baptiste.mesnoteas.bdd.NoteBdd;
import com.project.baptiste.mesnoteas.bdd.RunBDD;
import com.project.baptiste.mesnoteas.general.interfaces.IMatiere;
import com.project.baptiste.mesnoteas.general.interfaces.INote;
import com.project.baptiste.mesnoteas.listAdapter.NoteListViewAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class AccueilActivity extends Activity {
    private RunBDD runBDD;
    private List<INote> notes;
    private List<IMatiere> matieres;
    private List<IMatiere> matiereList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.accueil);
        runBDD = RunBDD.getInstance(this);
        initVariable();
        initListView();
    }

    public void initVariable(){
        notes = new ArrayList<>();
        matiereList = new ArrayList<>();
        matieres = new ArrayList<>();
        notes = runBDD.getNoteBdd().getAllNote();
        matieres = runBDD.getMatiereNoteBdd().getAllMatiereNote();
        matiereList = runBDD.getMatiereBdd().getAllMatiere();
    }

    public void initListView(){
        NoteListViewAdapter noteListViewAdapter = new NoteListViewAdapter(this,notes,matieres);
        ListView listView = (ListView) findViewById(R.id.listView);
        listView.setAdapter(noteListViewAdapter);
    }

    //lancer une autre vue
    public void addNote(View view){
        startActivity(new Intent(getApplicationContext(),AjouterNoteActivity.class));
        finish();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_mes_notes, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
