package com.project.baptiste.mesnoteas.fabrique.moyenne;


import com.project.baptiste.mesnoteas.general.Moyenne;
import com.project.baptiste.mesnoteas.general.interfaces.IMoyenne;

/**
 * Created by Baptiste on 01/06/2015.
 */
public class FabriqueMoyenne implements IFabriqueMoyenne {
    private IMoyenne moyenne;

    public FabriqueMoyenne() {
    }

    @Override
    public IMoyenne createMoyenne(){
        moyenne = new Moyenne();
        return moyenne;
    }
}
