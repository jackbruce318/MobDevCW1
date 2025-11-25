package org.me.gcu.bruce_jack_s2432194;



//based on rss feed it must have: title, publishing date, description (shows us the rate)
public class Currency {

    private String name;
    private String description;
    private String pubDate;
    private String code;
    private double rate;
    private String colour;
    private double latitude;
    private double longitude;
    private int flagId;


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

    public String getColour(){
        return colour;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public int getFlagId(){return flagId;}

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

    public void setColour(double rate){
        if(rate < 0.5){
            this.colour = "red";
        }
        else if(rate >= 0.5 && rate < 1){
            this.colour = "orange";
        }
        else if(rate >= 1 && rate < 1.5){
            this.colour = "yellow";
        }
        else{
            this.colour = "green";
        }
    }

    public void setLatitude(double latitude){
        this.latitude = latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public void setFlagId(int flagId){
        this.flagId = flagId;
    }


    @Override

    public String toString() {
        return "1 GBP is equal to " + rate + " " + code + " on " + pubDate;
    }

}
