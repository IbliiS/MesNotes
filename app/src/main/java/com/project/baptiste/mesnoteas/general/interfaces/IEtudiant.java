package com.project.baptiste.mesnoteas.general.interfaces;


import com.project.baptiste.mesnoteas.fabrique.annee.IFabriqueAnnee;

import java.util.Calendar;
import java.util.Collection;

/**
 * Created by Baptiste on 02/06/2015.
 */
public interface IEtudiant {
    IAnnee creerAnnee();

    boolean supprimerAnnee(int i);

    String getNom();

    void setNom(String nom);

    String getPrenom();

    void setPrenom(String prenom);

    Calendar getDateNaissance();

    void setDateNaissance(Calendar dateNaissance);

    String getNumEtudiant();

    void setNumEtudiant(String numEtudiant);

    Collection<IAnnee> getAnnees();

    void setAnnees(Collection<IAnnee> annees);

    IFabriqueAnnee getFabriqueAnnee();

    void setFabriqueAnnee(IFabriqueAnnee fabriqueAnnee);

    String identiteEtudiant();

    int getId();

    void setId(int id);
}
