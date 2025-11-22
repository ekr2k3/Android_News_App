package com.example.myrssreaderapp.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="item", strict=false)
public class Item {

    @Element(name="title", required=false)
    private String title;

    @Element(name="link", required=false)
    private String link;

    @Element(name="description", required=false)
    private String description;

    public Item(){}

    public String getTitle(){return title;}
    public String getLink(){return link;}
    public String getDescription(){return description;}
}