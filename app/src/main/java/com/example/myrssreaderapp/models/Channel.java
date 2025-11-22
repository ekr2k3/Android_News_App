package com.example.myrssreaderapp.models;

import org.simpleframework.xml.ElementList;
import org.simpleframework.xml.Root;
import java.util.List;

@Root(name="channel", strict=false)
public class Channel {

    @ElementList(name="item", inline=true, required=false)
    private List<Item> items;

    public Channel(){}
    public List<Item> getItems(){ return items; }
}