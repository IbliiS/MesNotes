package com.project.baptiste.mesnoteas.general;

import com.project.baptiste.mesnoteas.fabrique.annee.FabriqueAnnee;
import com.project.baptiste.mesnoteas.fabrique.annee.IFabriqueAnnee;
import com.project.baptiste.mesnoteas.general.interfaces.IAnnee;
import com.project.baptiste.mesnoteas.general.interfaces.IEtudiant;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;

/**
 * Created by Baptiste on 01/06/2015.
 */
public class Etudiant implements IEtudiant {
    private int id;
    private String nom;
    private String prenom;
    private Calendar dateNaissance;
    private String numEtudiant;
    private Collection<IAnnee> annees;
    private IFabriqueAnnee fabriqueAnnee;

    public Etudiant() {
        annees = new ArrayList<>();
        fabriqueAnnee = new FabriqueAnnee();
    }

    @Override
    public IAnnee creerAnnee(){
        IAnnee a = fabriqueAnnee.createAnnee();
        annees.add(a);
        return a;
    }

    @Override
    public boolean supprimerAnnee(int i){
        return annees.remove(i);
    }

    @Override
    public String getNom() {
        return nom;
    }

    @Override
    public void setNom(String nom) {
        this.nom = nom;
    }

    @Override
    public String getPrenom() {
        return prenom;
    }

    @Override
    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    @Override
    public Calendar getDateNaissance() {
        return dateNaissance;
    }

    @Override
    public void setDateNaissance(Calendar dateNaissance) {
        this.dateNaissance = dateNaissance;
    }

    @Override
    public String getNumEtudiant() {
        return numEtudiant;
    }

    @Override
    public void setNumEtudiant(String numEtudiant) {
        this.numEtudiant = numEtudiant;
    }

    @Override
    public Collection<IAnnee> getAnnees() {
        return annees;
    }

    @Override
    public void setAnnees(Collection<IAnnee> annees) {
        this.annees = annees;
    }

    @Override
    public IFabriqueAnnee getFabriqueAnnee() {
        return fabriqueAnnee;
    }

    @Override
    public void setFabriqueAnnee(IFabriqueAnnee fabriqueAnnee) {
        this.fabriqueAnnee = fabriqueAnnee;
    }

    @Override
    public String identiteEtudiant() {
        String s = nom + " " + prenom + ", N°Etu : " + numEtudiant + ". Née le : " + dateNaissance;
        return s;
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }
}
