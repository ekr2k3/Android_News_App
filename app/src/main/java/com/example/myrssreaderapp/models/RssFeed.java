package com.example.myrssreaderapp.models;

import org.simpleframework.xml.Element;
import org.simpleframework.xml.Root;

@Root(name="rss", strict=false)
public class RssFeed {

    @Element(name="channel", required=false)
    private Channel channel;

    public RssFeed(){}
    public Channel getChannel(){ return channel; }
}