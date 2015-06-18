package com.project.baptiste.mesnoteas.general.interfaces;


import com.project.baptiste.mesnoteas.fabrique.moyenne.IFabriqueMoyenne;

import java.util.Collection;

/**
 * Created by Baptiste on 02/06/2015.
 */
public interface IAnnee extends IObjet {
    double moyenneAnnee();

    boolean supprimerMoyenne(int i);

    IMoyenne creerMoyenne();

    String getNomAnnee();

    void setNomAnnee(String nomAnnee);

    Collection<IMoyenne> getMoyennes();

    void setMoyennes(Collection<IMoyenne> moyennes);

    IFabriqueMoyenne getFabriqueMoyenne();

    void setFabriqueMoyenne(IFabriqueMoyenne fabriqueMoyenne);

    int getId();

    void setId(int id);
}
