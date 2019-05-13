package gcc.demos.rest.model;

import java.net.URI;

public class Link {

    private String rel;
    private URI href;

    public Link(String rel, URI href) {
        this.href = href;
        this.rel = rel;
    }

    public void setHref(URI href) {
        this.href = href;
    }

    public URI getHref() {
        return href;
    }

    public void setRel(String rel) {
        this.rel = rel;
    }

    public String getRel() {
        return rel;
    }

}