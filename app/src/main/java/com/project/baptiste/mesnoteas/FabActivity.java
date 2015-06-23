package com.project.baptiste.mesnoteas;

import android.app.Activity;

import com.getbase.floatingactionbutton.FloatingActionButton;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;

/**
 * Created by Baptiste on 22/06/2015.
 */
public class FabActivity extends Activity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fab);


        final FloatingActionButton actionA = (FloatingActionButton) findViewById(R.id.action_annee);
        actionA.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                actionA.setTitle("Action A clicked");
            }
        });


    }
}
