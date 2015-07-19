package com.project.baptiste.mesnoteas.spinner;

import android.content.Context;
import android.widget.ArrayAdapter;

import com.project.baptiste.mesnoteas.R;

/**
 * Created by Baptiste on 18/07/2015.
 */
public class MySpinnerWhite extends MySpinner {
    @Override
    public ArrayAdapter<String> initAdapter(Context context) {
        return new ArrayAdapter<String>(context, R.layout.spinner_item_white, this.list);
    }
}
