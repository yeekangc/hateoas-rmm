package gcc.demos.rest.model;

import java.util.ArrayList;
import java.util.List;

import javax.json.bind.annotation.JsonbProperty;
import org.eclipse.microprofile.openapi.annotations.media.Schema;


public class AddressWithLinks extends Address {

    private List<Link> links =  new ArrayList<Link>();

    public void addLink(Link link) {
        links.add(link);
    }

    public AddressWithLinks(Address address) {
        super(address);
    }

    @JsonbProperty("_links")
    @Schema(name = "_links", description = "HATEOAS Links")
    public List<Link> getLinks() {return links;}

}