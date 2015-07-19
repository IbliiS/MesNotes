package com.project.baptiste.mesnoteas.spinner;

import android.content.Context;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.project.baptiste.mesnoteas.R;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Baptiste on 02/07/2015.
 */
public abstract class MySpinner {
    protected List<String> list = new ArrayList<>();
    private Maillon monMaillon;

    public MySpinner() {
        monMaillon = new MaillonAnnee(new MaillonPeriode(new MaillonMatiere(null)));
    }

    public Spinner creerSpinner(Spinner spinner, List<IObjet> objets, String message, Context context, boolean ajouterPremierElement){
        list = monMaillon.remplirListe(objets);
        if(ajouterPremierElement) {
            list.add(0, message);
        }
//        ArrayAdapter<String> adapter = new ArrayAdapter<String>(context, R.layout.spinner_item_white, list);
        ArrayAdapter<String> adapter = initAdapter(context);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return false;
            }
        });

        return spinner;
    }

    /**
     * Retourne un adapter noir ou blanc
     * @param context
     * @return adapter
     */
    public abstract ArrayAdapter<String> initAdapter(Context context);

    public int getIndexOfItem(String s){
        if(list.contains(s)) {
            System.out.println(list.indexOf(s));
            return list.indexOf(s);
        }
        return -1;
    }

}
