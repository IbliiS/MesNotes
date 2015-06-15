package com.project.baptiste.mesnoteas.general;

import com.project.baptiste.mesnoteas.general.interfaces.ICoefficient;

/**
 * Created by Baptiste on 01/06/2015.
 */
public class Coefficient implements ICoefficient {

    private int coef;

    public Coefficient() {
    }

    public Coefficient(int coef) {
        this.coef = coef;
    }

    @Override
    public int getCoef() {
        return coef;
    }

    @Override
    public void setCoef(int coef) {
        this.coef = coef;
    }
}
