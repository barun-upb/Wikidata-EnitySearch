package org.aksw.agdistis.util;

import org.restlet.data.MediaType;

import javax.ws.rs.*;
import java.io.IOException;
import java.util.List;



@Path("myresource")
public class MyResource {
    //WikiServiceRepository repository= new WikiServiceRepository();

    @GET
    @Path("/fetchDocx")
    @Produces("application/json")
    public List<WikiData> getData(@QueryParam("q") String queryTerms) throws IOException {
        GetWikiData repo= new GetWikiData();
        System.out.println("Called from resource");
        return repo.sendData(queryTerms);
        //return repository.getAllWikiData();
    }
/*
    @GET
    @Path("wiki/{label}")
    @Produces(MediaType.APPLICATION_JSON)
    public WikiData getData(@PathParam("label") String label)
    {
        return repository.getData(label);
    }



    @POST
    @Path("add")
    @Consumes(MediaType.APPLICATION_JSON)
    public WikiData createWikiData(WikiData wikidata)
    {
        repository.createData(wikidata);
        return wikidata;

    }

 */

}
