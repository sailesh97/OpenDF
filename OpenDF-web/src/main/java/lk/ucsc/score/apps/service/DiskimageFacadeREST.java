/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package lk.ucsc.score.apps.service;

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
import lk.ucsc.score.apps.models.Diskimage;
import lk.ucsc.score.apps.models.Project;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import lk.ucsc.score.apps.models.Log;
/**
 *
 * @author Acer
 */
@Stateless
@Path("diskimage")
public class DiskimageFacadeREST extends AbstractFacade<Diskimage> {
    @PersistenceContext(unitName = "lk.ucsc.score.apps_OpenDF-web_war_1.0-SNAPSHOTPU")
    private EntityManager em;

    public DiskimageFacadeREST() {
        super(Diskimage.class);
    }

    @POST
    @Override
    @Consumes({"application/xml", "application/json"})
    public void create(Diskimage entity) {
        entity.setState(0);
        super.create(entity);
        Project project =  em.find(Project.class, entity.getProjectidProject().getIdProject());
        project.getDiskimageCollection().add(entity); // useful to maintain coherence, but ignored by JPA
        em.persist(project);
        getEntityManager().persist(new Log("A new report generated", project));
        
    }
    
    @PUT
    @Override
    @Consumes({"application/xml", "application/json"})
    public void edit(Diskimage entity) {
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
    public Diskimage find(@PathParam("id") Integer id) {
        return super.find(id);
    }

    @GET
    @Path("startAnalyzing/{id}")
    @Produces({"application/xml", "application/json"})
    public void startAnalyzing(@PathParam("id") Integer id) {
        Diskimage entity = super.find(id);
        entity.setState(2);
        super.edit(entity);
    }

    @GET
    @Override
    @Produces({"application/xml", "application/json"})
    public List<Diskimage> findAll() {
        return super.findAll();
    }

    @GET
    @Path("{from}/{to}")
    @Produces({"application/xml", "application/json"})
    public List<Diskimage> findRange(@PathParam("from") Integer from, @PathParam("to") Integer to) {
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
