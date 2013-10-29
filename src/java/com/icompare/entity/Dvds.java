/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package com.icompare.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author hwu65
 */
@Entity
@Table(name = "DVDS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Dvds.findAll", query = "SELECT d FROM Dvds d"),
    @NamedQuery(name = "Dvds.findByDvdid", query = "SELECT d FROM Dvds d WHERE d.dvdid = :dvdid"),
    @NamedQuery(name = "Dvds.findByDvdtitle", query = "SELECT d FROM Dvds d WHERE d.dvdtitle = :dvdtitle"),
    @NamedQuery(name = "Dvds.findByDirector", query = "SELECT d FROM Dvds d WHERE d.director = :director"),
    @NamedQuery(name = "Dvds.findByPublicationtime", query = "SELECT d FROM Dvds d WHERE d.publicationtime = :publicationtime"),
    @NamedQuery(name = "Dvds.findByRetailprice", query = "SELECT d FROM Dvds d WHERE d.retailprice = :retailprice"),
    @NamedQuery(name = "Dvds.findByCurrencytype", query = "SELECT d FROM Dvds d WHERE d.currencytype = :currencytype"),
    @NamedQuery(name = "Dvds.findBySnippet", query = "SELECT d FROM Dvds d WHERE d.snippet = :snippet")})
public class Dvds implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "DVDID")
    private Integer dvdid;
    @Size(max = 50)
    @Column(name = "DVDTITLE")
    private String dvdtitle;
    @Size(max = 50)
    @Column(name = "DIRECTOR")
    private String director;
    @Size(max = 20)
    @Column(name = "PUBLICATIONTIME")
    private String publicationtime;
    // @Max(value=?)  @Min(value=?)//if you know range of your decimal fields consider using these annotations to enforce field validation
    @Column(name = "RETAILPRICE")
    private Double retailprice;
    @Size(max = 10)
    @Column(name = "CURRENCYTYPE")
    private String currencytype;
    @Size(max = 255)
    @Column(name = "SNIPPET")
    private String snippet;

    public Dvds() {
    }

    public Dvds(Integer dvdid) {
        this.dvdid = dvdid;
    }

    public Integer getDvdid() {
        return dvdid;
    }

    public void setDvdid(Integer dvdid) {
        this.dvdid = dvdid;
    }

    public String getDvdtitle() {
        return dvdtitle;
    }

    public void setDvdtitle(String dvdtitle) {
        this.dvdtitle = dvdtitle;
    }

    public String getDirector() {
        return director;
    }

    public void setDirector(String director) {
        this.director = director;
    }

    public String getPublicationtime() {
        return publicationtime;
    }

    public void setPublicationtime(String publicationtime) {
        this.publicationtime = publicationtime;
    }

    public Double getRetailprice() {
        return retailprice;
    }

    public void setRetailprice(Double retailprice) {
        this.retailprice = retailprice;
    }

    public String getCurrencytype() {
        return currencytype;
    }

    public void setCurrencytype(String currencytype) {
        this.currencytype = currencytype;
    }

    public String getSnippet() {
        return snippet;
    }

    public void setSnippet(String snippet) {
        this.snippet = snippet;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (dvdid != null ? dvdid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Dvds)) {
            return false;
        }
        Dvds other = (Dvds) object;
        if ((this.dvdid == null && other.dvdid != null) || (this.dvdid != null && !this.dvdid.equals(other.dvdid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.icompare.entity.Dvds[ dvdid=" + dvdid + " ]";
    }
    
}
