package com.example.compose_download;

public class Model  {

    String description;
    String url;


    public Model() {
    }

    public Model( String url, String description) {
        this.description = description;
        this.url = url;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
