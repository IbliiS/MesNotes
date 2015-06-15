package com.project.baptiste.mesnoteas.general;


import com.project.baptiste.mesnoteas.general.interfaces.ICoefficient;
import com.project.baptiste.mesnoteas.general.interfaces.INote;

/**
 * Created by Baptiste on 01/06/2015.
 */
public class Note implements INote {
    private double note;
    private int coef;
    private int id;

    public Note() {
        note = -1;
    }

    @Override
    public double getNote() {
        return note;
    }

    @Override
    public void setNote(double note) {
        this.note = note;
    }

    @Override
    public int getCoef() {
        return coef;
    }

    @Override
    public void setCoef(int coef) {
        this.coef = coef;
    }

    @Override
    public String toString() {
        return ""+note;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof INote){
            INote note = (INote) o;
            return note.getId() == this.id;
        }
        return false;
    }
}

