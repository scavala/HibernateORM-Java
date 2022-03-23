/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.viewmodel;

import hr.algebra.model.Klub;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 *
 * @author daniel.bele
 */
public class KlubViewModel {

    private final Klub klub;

    public KlubViewModel(Klub klub) {
        if (klub == null) {
            klub = new Klub(0, "");
        }
        this.klub = klub;
    }

    public Klub getKlub() {
        return klub;
    }

    public StringProperty getNameProperty() {
        return new SimpleStringProperty(klub.getName());
    }

    @Override
    public String toString() {
        return klub.toString();
    }
    

}
