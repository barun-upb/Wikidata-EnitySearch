package org.aksw.agdistis.webapp;

import org.aksw.agdistis.util.GetWikiData;
import org.aksw.agdistis.util.WikiData;
import org.apache.commons.io.IOUtils;

import org.restlet.data.Form;

import org.restlet.representation.Representation;
import org.restlet.resource.Get;
import org.restlet.resource.Post;
import org.restlet.resource.ServerResource;


import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;



//@Path("myresource")
public class MyResource extends ServerResource {
    //WikiServiceRepository repository= new WikiServiceRepository();

    @Post
    //@Path("/fetchDocx")
    //@Produces("application/json")

    public List<WikiData> getData(Representation entity) throws IOException {

        InputStream input = entity.getStream();
// here the inputStream is duplicated due to it can be read only once.
// Therefore, we do it for checking if the input is from gerbil or not.
        byte[] byteArray = IOUtils.toByteArray(input);
        InputStream input1 = new ByteArrayInputStream(byteArray);


        String string = IOUtils.toString(input1);
// Parse the given representation and retrieve data
        Form form = new Form(string);
        String text = form.getFirstValue("text");


        GetWikiData repo= new GetWikiData();
        System.out.println("Called from resource");
        System.out.println(text.split("<entity>")[1].split("</entity>")[0]);
        List<WikiData> wikiDataResponseList = new ArrayList<WikiData>();
        ArrayList<String> fileList = new ArrayList<String>();

        for(WikiData wiki: repo.sendData(text.split("<entity>")[1].split("</entity>")[0])) {
            System.out.println(wiki.getDescription() + wiki.getUrl());
        }
        return repo.sendData(text.split("<entity>")[1].split("</entity>")[0]);
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
