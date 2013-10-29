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
@Table(name = "BOOKS")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Books.findAll", query = "SELECT b FROM Books b"),
    @NamedQuery(name = "Books.findByBookid", query = "SELECT b FROM Books b WHERE b.bookid = :bookid"),
    @NamedQuery(name = "Books.findByIsbn", query = "SELECT b FROM Books b WHERE b.isbn = :isbn"),
    @NamedQuery(name = "Books.findByBooktitle", query = "SELECT b FROM Books b WHERE b.booktitle = :booktitle"),
    @NamedQuery(name = "Books.findByAuthor", query = "SELECT b FROM Books b WHERE b.author = :author"),
    @NamedQuery(name = "Books.findByPublicationtime", query = "SELECT b FROM Books b WHERE b.publicationtime = :publicationtime"),
    @NamedQuery(name = "Books.findByRetailprice", query = "SELECT b FROM Books b WHERE b.retailprice = :retailprice"),
    @NamedQuery(name = "Books.findByCurrencytype", query = "SELECT b FROM Books b WHERE b.currencytype = :currencytype"),
    @NamedQuery(name = "Books.findBySnippet", query = "SELECT b FROM Books b WHERE b.snippet = :snippet")})
public class Books implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "BOOKID")
    private Integer bookid;
    @Size(max = 50)
    @Column(name = "ISBN")
    private String isbn;
    @Size(max = 50)
    @Column(name = "BOOKTITLE")
    private String booktitle;
    @Size(max = 50)
    @Column(name = "AUTHOR")
    private String author;
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

    public Books() {
    }

    public Books(Integer bookid) {
        this.bookid = bookid;
    }

    public Integer getBookid() {
        return bookid;
    }

    public void setBookid(Integer bookid) {
        this.bookid = bookid;
    }

    public String getIsbn() {
        return isbn;
    }

    public void setIsbn(String isbn) {
        this.isbn = isbn;
    }

    public String getBooktitle() {
        return booktitle;
    }

    public void setBooktitle(String booktitle) {
        this.booktitle = booktitle;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
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
        hash += (bookid != null ? bookid.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Books)) {
            return false;
        }
        Books other = (Books) object;
        if ((this.bookid == null && other.bookid != null) || (this.bookid != null && !this.bookid.equals(other.bookid))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.icompare.entity.Books[ bookid=" + bookid + " ]";
    }
    
}
