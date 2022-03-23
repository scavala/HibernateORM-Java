/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package hr.algebra.model;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author zakesekresa
 */
@Entity
@Table(name = "Klub")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Klub.findAll", query = "SELECT k FROM Klub k")
    , @NamedQuery(name = "Klub.findByIDKlub", query = "SELECT k FROM Klub k WHERE k.iDKlub = :iDKlub")
    , @NamedQuery(name = "Klub.findByName", query = "SELECT k FROM Klub k WHERE k.name = :name")})
public class Klub implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "IDKlub")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Integer iDKlub;
    @Basic(optional = false)
    @Column(name = "Name")
    private String name;
    @OneToMany(mappedBy = "omiljenKlub")
    private Collection<Person> personCollection;

    public Klub() {
    }

    public Klub(Klub data) {
        updateDeatils(data);
    }

    public Klub(Integer iDKlub) {
        this.iDKlub = iDKlub;
    }

    public Klub(Integer iDKlub, String name) {
        this.iDKlub = iDKlub;
        this.name = name;
    }

    public Integer getIDKlub() {
        return iDKlub;
    }

    public void setIDKlub(Integer iDKlub) {
        this.iDKlub = iDKlub;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public Collection<Person> getPersonCollection() {
        return personCollection;
    }

    public void setPersonCollection(Collection<Person> personCollection) {
        this.personCollection = personCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (iDKlub != null ? iDKlub.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Klub)) {
            return false;
        }
        Klub other = (Klub) object;
        if ((this.iDKlub == null && other.iDKlub != null) || (this.iDKlub != null && !this.iDKlub.equals(other.iDKlub))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return name;
    }

    public void updateDeatils(Klub data) {
        this.name = data.getName();

    }

}
