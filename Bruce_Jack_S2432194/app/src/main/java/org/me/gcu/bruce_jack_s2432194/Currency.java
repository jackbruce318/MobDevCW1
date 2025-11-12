package org.me.gcu.bruce_jack_s2432194;

import java.util.Date;

//based on rss feed it must have: title, publishing date, description (shows us the rate)
public class Currency {

    private String name;
    private String description;
    private String pubDate;
    private String code;
    private double rate;
    private int strengthColour;


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

    public String getCode(){
        return code;
    }


    public double getRate(){
        return rate;
    }

    public int getStrengthColour(){
        return strengthColour;
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

    public void setRate(double rate){
        this.rate = rate;
    }

    public void setCode(String code){
        this.code = code;
    }

    public void setStrengthColour(double rate){
        rate = this.rate;

        if(rate < 0.5){
            strengthColour = 4276544;
        }
        else if(rate >= 0.5 && rate < 1){
            //strengthColour = "orange";
        }
        else if(rate >= 1 && rate < 1.5){
            //strengthColour = "yellow";
        }
        else{
            //strengthColour = "green";
        }
    }


    @Override

    public String toString() {
        return "1 GBP is equal to " + rate + " " + code + " on " + pubDate;
    }

}
