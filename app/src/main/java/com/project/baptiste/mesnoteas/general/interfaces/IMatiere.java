package com.project.baptiste.mesnoteas.general.interfaces;

import com.project.baptiste.mesnoteas.fabrique.note.IFabriqueNote;

import java.util.Collection;
import java.util.List;

/**
 * Un module, comprend une ou plusieurs notes, une matiere a également
 * un coeffcient. Elle possède aussi un nom
 */
public interface IMatiere extends IObjet{
    INote creerNote();

    IObjet supprimerNote(int i);

    String getNomMatiere();

    void setNomMatiere(String nomMatiere);

    int getCoef();

    void setCoef(int coef);

    List<IObjet> getNotes();

    void setNotes(List<IObjet> notes);

    IFabriqueNote getFabriqueNote();

    void setFabriqueNote(IFabriqueNote fabriqueNote);

    double resultatMatiere();

    int getId();

    void setId(int id);

    double getMoyenne();

    void setMoyenne(double moyenne);

    void copyMatiere(IMatiere matiere);
}
