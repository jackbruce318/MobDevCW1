package org.me.gcu.bruce_jack_s2432194;

import java.util.Date;

//based on rss feed it must have: title, publishing date, description (shows us the rate)
public class Currency {

    private String name;
    private String description;
    private String pubDate;

    public Currency(){
    }

    public String getName(){
        return name;
    }

    public String getDescription(){
        return description;
    }

    public String getPubDate(){
        return pubDate;
    }

    public void setName(String name){
        this.name = name;
    }

    public void setDescription(String description){
        this.description = description;
    }

    public void setPubDate(String pubDate){
        this.pubDate = pubDate;
    }

    public String toString() {
        return name + " " + description + " " + pubDate;
    }

}
