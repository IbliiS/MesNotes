package com.project.baptiste.mesnoteas.general.interfaces;


import com.project.baptiste.mesnoteas.fabrique.moyenne.IFabriqueMoyenne;

import java.util.Collection;
import java.util.List;

/**
 * Created by Baptiste on 02/06/2015.
 */
public interface IAnnee extends IObjet {
    double moyenneAnnee();

    IObjet supprimerMoyenne(int i);

    IMoyenne creerMoyenne();

    String getNomAnnee();

    void setNomAnnee(String nomAnnee);

    List<IObjet> getMoyennes();

    void setMoyennes(List<IObjet> moyennes);

    IFabriqueMoyenne getFabriqueMoyenne();

    void setFabriqueMoyenne(IFabriqueMoyenne fabriqueMoyenne);

    int getId();

    void setId(int id);

    double getMoyenne();

    void setMoyenne(double moyenne);
}
