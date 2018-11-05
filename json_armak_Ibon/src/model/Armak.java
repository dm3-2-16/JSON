/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package model;

import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;

/**
 *
 * @author aitor
 */

public class Armak {
        
    private final SimpleStringProperty jatorria;
    private final SimpleStringProperty desk;
    private final SimpleStringProperty name;
    private final SimpleIntegerProperty urtea;
    
    
    public Armak(String fName, String lName, String deskri, int erabil) { //derrigortuta nago, ezta? public jartzera beste pakete batetik sortuko dudalako?
        this.name = new SimpleStringProperty(fName);
        this.jatorria = new SimpleStringProperty(lName);
        this.desk = new SimpleStringProperty(deskri);
        this.urtea = new SimpleIntegerProperty (erabil);
    }

    /*public Armak(String textContent, String textContent0, String textContent1, String textContent2, boolean parseBoolean, String textContent3) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }*/

    
    public String getName() {
        return name.get();
    }
    public void setName(String fName) {
        name.set(fName);
    }
    public String getJatorria() {
        return jatorria.get();
    }
    public void setJatorria(String lName) {
        jatorria.set(lName);
    }
    public String getDesk() {
        return desk.get();
    }
    public void setDesk(String deskri) {
        desk.set(deskri);
    } 
    public int getUrtea() {
        return urtea.get();
    }
    public void setUrtea(Integer erabil) {
        urtea.set(erabil);
    }
}