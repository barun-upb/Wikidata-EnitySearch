package org.aksw.agdistis.webapp;

import org.aksw.agdistis.util.GetWikiData;
import org.aksw.agdistis.util.WikiData;
import org.apache.commons.io.IOUtils;

import org.restlet.Request;
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
import java.util.Map;


public class WikiDataService extends ServerResource {

    @Get
    public String getData() throws IOException {
        Request request = getRequest();
        String entity = getQueryValue("entity");// getRequest().getAttributes().get("entity");
        GetWikiData repo = new GetWikiData();
        System.out.println("Called wikidata index for resource " + entity);
        StringBuffer response = new StringBuffer();
        for (Map.Entry<String, WikiData> data : repo.sendData(entity).entrySet()) {
            WikiData wiki = data.getValue();
            response.append(wiki.getDescription() + "\t" + wiki.getUrl() + "\t" + wiki.getLabel() + "\t"
                    + wiki.getUnique_identifier() + "\n");
        }
        return response.toString();
    }

    @Post
    public String getData(Representation entity) throws IOException {

        InputStream input = entity.getStream();

        byte[] byteArray = IOUtils.toByteArray(input);
        InputStream input1 = new ByteArrayInputStream(byteArray);

        String string = IOUtils.toString(input1);
        Form form = new Form(string);
        String text = form.getFirstValue("text");

        GetWikiData repo = new GetWikiData();
        System.out.println("Called from resource");
        System.out.println(text.split("<entity>")[1].split("</entity>")[0]);
        List<WikiData> wikiDataResponseList = new ArrayList<WikiData>();
        ArrayList<String> fileList = new ArrayList<String>();
        //Map.Entry<String, WikiData> data : repo.sendData(text).entrySet()
        for (Map.Entry<String, WikiData> data : repo.sendData(text).entrySet()) {
            WikiData wiki = data.getValue();
            response.append(wiki.getDescription() + "\t" + wiki.getUrl() + "\t" + wiki.getLabel() + "\t"
                    + wiki.getUnique_identifier() + "\n");
        }
        return response.toString();
    }
}