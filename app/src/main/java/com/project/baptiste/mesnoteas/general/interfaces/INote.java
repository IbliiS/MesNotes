package com.project.baptiste.mesnoteas.general.interfaces;

/**
 * Une note et son coefficient.
 */
public interface INote extends IObjet{
    double getNote();

    void setNote(double note);

    int getCoef();

    void setCoef(int coef);

    int getId();

    void setId(int id);
}
