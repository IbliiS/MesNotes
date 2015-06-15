package com.project.baptiste.mesnoteas.general.interfaces;

import com.project.baptiste.mesnoteas.fabrique.matiere.IFabriqueMatiere;

import java.util.Collection;

/**
 * Correspond a la moyenne globale d'un semestre
 */
public interface IMoyenne {
    IMatiere creerMatiere();

    boolean supprimerMatiere(int i);

    double resultatMoyenne();

    String getNomMoyenne();

    void setNomMoyenne(String nomMoyenne);

    IFabriqueMatiere getFabriqueMatiere();

    void setFabriqueMatiere(IFabriqueMatiere fabriqueMatiere);

    Collection<IMatiere> getMatieres();

    void setMatieres(Collection<IMatiere> matieres);

    @Override
    String toString();

    int getId();

    void setId(int id);
}
