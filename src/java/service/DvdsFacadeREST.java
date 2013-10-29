/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package service;

import com.icompare.entity.Dvds;
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
@Path("com.icompare.entity.dvds")
public class DvdsFacadeREST extends AbstractFacade<Dvds> {
    @PersistenceContext(unitName = "iCompareProPU")
    private EntityManager em;

    public DvdsFacadeREST() {
        super(Dvds.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Dvds entity) {
        super.create(entity);
    }

    @PUT
    @Override
    @Consumes({"application/xml", "application/json"})
    public void edit(Dvds entity) {
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
    public Dvds find(@PathParam("id") Integer id) {
        return super.find(id);
    }
    
    @GET
    @Path("{director}/director")
    @Produces({"application/xml", "application/json"})
    public List<Dvds> searchDvdWithCreator(@PathParam("director") String inDirector) {
        List<Dvds> dvds = new ArrayList<Dvds>();
        if(!"".equals(inDirector)) {
            inDirector = "%" + inDirector + "%";
            dvds = em.createQuery("SELECT d FROM Dvds d WHERE UPPER(d.director) LIKE :inDirector").setParameter("inDirector", inDirector.toUpperCase()).getResultList();
        }       
        return dvds;
    }
    
    @GET
    @Path("{dvdtitle}/dvdtitle")
    @Produces({"application/xml", "application/json"})
    public List<Dvds> searchDvdWithTitle(@PathParam("dvdtitle") String inTitle) {
        List<Dvds> dvds = new ArrayList<Dvds>();
        if(!"".equals(inTitle)) {
            inTitle = "%" + inTitle + "%";
            dvds = em.createQuery("SELECT d FROM Dvds d WHERE UPPER(d.dvdtitle) LIKE :inTitle").setParameter("inTitle", inTitle.toUpperCase()).getResultList();
        }
        return dvds;
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Dvds> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Dvds> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
