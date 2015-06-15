package com.project.baptiste.mesnoteas.fabrique.note;


import com.project.baptiste.mesnoteas.general.Coefficient;
import com.project.baptiste.mesnoteas.general.Note;
import com.project.baptiste.mesnoteas.general.interfaces.ICoefficient;
import com.project.baptiste.mesnoteas.general.interfaces.INote;

/**
 * Created by Baptiste on 01/06/2015.
 */
public class FabriqueNote implements IFabriqueNote{

    private ICoefficient coef;
    private INote note;

    public FabriqueNote() {
    }

    @Override
    public INote createNote(){
        this.coef = new Coefficient();
        this.note = new Note();
        return this.note;
    }
}
