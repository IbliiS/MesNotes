package com.project.baptiste.mesnoteas.general.interfaces;

import com.project.baptiste.mesnoteas.fabrique.matiere.IFabriqueMatiere;

import java.util.Collection;
import java.util.List;

/**
 * Correspond a la moyenne globale d'un semestre
 */
public interface IMoyenne extends IObjet{
    IMatiere creerMatiere();

    IObjet supprimerMatiere(int i);

    double resultatMoyenne();

    String getNomMoyenne();

    void setNomMoyenne(String nomMoyenne);

    IFabriqueMatiere getFabriqueMatiere();

    void setFabriqueMatiere(IFabriqueMatiere fabriqueMatiere);

    List<IObjet> getMatieres();

    void setMatieres(List<IObjet> matieres);

    @Override
    String toString();

    int getId();

    void setId(int id);

    double getMoyenne();

    void setMoyenne(double moyenne);
}
