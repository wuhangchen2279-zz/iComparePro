/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import com.icompare.entity.Books;
import java.util.ArrayList;
import java.util.List;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;

/**
 *
 * @author hwu65
 */
@Stateless
@Path("com.icompare.entity.books")
public class BooksFacadeREST extends AbstractFacade<Books> {
    @PersistenceContext(unitName = "iCompareProPU")
    private EntityManager em;

    public BooksFacadeREST() {
        super(Books.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Books entity) {
        super.create(entity);
    }

    @PUT
    @Override
    @Consumes({"application/xml", "application/json"})
    public void edit(Books entity) {
        super.edit(entity);
    }

    @DELETE
    @Path("{id}")
    public void remove(@PathParam("id") Integer id) {
        super.remove(super.find(id));
    }

    @GET
    @Path("{id}")
    @Produces({"application/xml", "application/json"})
    public Books find(@PathParam("id") Integer id) {
        return super.find(id);
    }
    
    
    @GET
    @Path("{booktitle}/booktitle")
    @Produces({"application/xml", "application/json"})
    public List<Books> searchBookWithTitle(@PathParam("booktitle") String inTitle) {
        List<Books> books = new ArrayList<Books>();
        if(!"".equals(inTitle)) {
            inTitle = "%" + inTitle + "%";
            books = em.createQuery("SELECT b FROM Books b WHERE UPPER(b.booktitle) LIKE :inTitle").setParameter("inTitle", inTitle.toUpperCase()).getResultList();
        }
        return books;
        
    }
    
    @GET
    @Path("{author}/author")
    @Produces({"application/xml", "application/json"})
    public List<Books> searchBookWithAuthor(@PathParam("author") String inAuthor) {
        List<Books> books = new ArrayList<Books>();
        if (!"".equals(inAuthor)){
            inAuthor = "%" + inAuthor + "%";
            books = em.createQuery("SELECT b FROM Books b WHERE UPPER(b.author) LIKE :inAuthor").setParameter("inAuthor", inAuthor.toUpperCase()).getResultList();
        }
        return books;
    }
    
    @GET
    @Path("{isbn}/isbn")
    @Produces({"application/xml", "application/json"})
    public List<Books> searchBookWithIsbn(@PathParam("isbn") String inIsbn) {
        List<Books> books = new ArrayList<Books>();
        if(inIsbn != ""){ 
            inIsbn = "%" + inIsbn + "%";
            books = em.createQuery("SELECT b FROM Books b WHERE UPPER(b.isbn) LIKE :inIsbn").setParameter("inIsbn", inIsbn.toUpperCase()).getResultList();
        }
        return books;
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Books> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Books> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
        return super.findRange(new int[]{from, to});
    }

    @GET
    @Path("count")
    @Produces("text/plain")
    public String countREST() {
        return String.valueOf(super.count());
    }

    @Override
    protected EntityManager getEntityManager() {
        return em;
    }
    
}
