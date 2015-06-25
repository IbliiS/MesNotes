package com.project.baptiste.mesnoteas.general;

import com.project.baptiste.mesnoteas.fabrique.note.FabriqueNote;
import com.project.baptiste.mesnoteas.fabrique.note.IFabriqueNote;
import com.project.baptiste.mesnoteas.general.interfaces.IMatiere;
import com.project.baptiste.mesnoteas.general.interfaces.INote;
import com.project.baptiste.mesnoteas.general.interfaces.IObjet;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by Baptiste on 01/06/2015.
 */
public class Matiere implements IMatiere {

    private int id;
    private String nomMatiere;
    private int coef;
    private List<IObjet> notes;
    private IFabriqueNote fabriqueNote;
    private double moyenne;

    public Matiere() {
        moyenne = 0;
        coef=0;
        nomMatiere="";
        fabriqueNote = new FabriqueNote();
        notes = new ArrayList<>();
    }

    @Override
    public INote creerNote(){
        INote n = fabriqueNote.createNote();
        notes.add(n);
        return n;
    }

    @Override
    public IObjet supprimerNote(int i){
        return notes.remove(i);
    }

    @Override
    public String getNomMatiere() {
        return nomMatiere;
    }

    @Override
    public void setNomMatiere(String nomMatiere) {
        this.nomMatiere = nomMatiere;
    }

    @Override
    public int getCoef() {
        return coef;
    }

    @Override
    public void setCoef(int coef) {
        this.coef = coef;
    }

    @Override
    public List<IObjet> getNotes() {
        return notes;
    }

    @Override
    public void setNotes(List<IObjet> notes) {
        this.notes = notes;
    }

    @Override
    public IFabriqueNote getFabriqueNote() {
        return fabriqueNote;
    }

    @Override
    public void setFabriqueNote(IFabriqueNote fabriqueNote) {
        this.fabriqueNote = fabriqueNote;
    }

    @Override
    public double resultatMatiere(){
        INote n;
        double r = 0.0;
        int diviseur = 0;
        if(notes.isEmpty()){
            return r;
        }
        else {
            for (IObjet o : notes) {
                n = (INote) o;
                diviseur += n.getCoef();
                r += n.getNote() * n.getCoef();
            }
            if(diviseur == 0){
                moyenne = -1;
            }
            else {
                moyenne = r / diviseur;
            }
            return moyenne;
        }
    }

    @Override
    public String toString() {
        INote n;
        String s = "        -" + nomMatiere +" : " ;
        for (IObjet o : notes){
            n = (INote) o;
            s = s +n.toString()+" ";
        }
        return s+"\n        = "+resultatMatiere()+"\n";
    }

    @Override
    public int getId() {
        return id;
    }

    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public double getMoyenne() {
        return resultatMatiere();
    }

    @Override
    public void setMoyenne(double moyenne) {
        this.moyenne = moyenne;
    }

    @Override
    public boolean equals(Object o) {
        if(o instanceof IMatiere){
            IMatiere m = (IMatiere) o;
            return m.getId() == this.id;
        }
        return false;
    }

    @Override
    public void copyMatiere(IMatiere matiere){
        this.id = matiere.getId();
        //
        this.notes = matiere.getNotes();
        this.nomMatiere = matiere.getNomMatiere();
        this.moyenne = matiere.getMoyenne();
        this.coef = matiere.getCoef();
    }
}
