package com.project.baptiste.mesnoteas.spinner;

import android.content.Context;
import android.widget.ArrayAdapter;

/**
 * Created by Baptiste on 18/07/2015.
 */
public class MySpinnerBlack extends MySpinner {
    @Override
    public ArrayAdapter<String> initAdapter(Context context) {
        return new ArrayAdapter<String>(context,android.R.layout.simple_spinner_item, this.list);

    }
}
