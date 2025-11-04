package org.me.gcu.bruce_jack_s2432194;

/*  Starter project for Mobile Platform Development - 1st diet 25/26
    You should use this project as the starting point for your assignment.
    This project simply reads the data from the required URL and displays the
    raw data in a TextField
*/

//
// Name                 Jack Bruce
// Student ID           S2432194
// Programme of Study   Software Development
//

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import androidx.appcompat.app.AppCompatActivity;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Date;

import org.me.gcu.bruce_jack_s2432194.Currency;

public class MainActivity extends AppCompatActivity implements OnClickListener, AdapterView.OnItemClickListener {
    private TextView rawDataDisplay;

    private String result;
    private String url1="";
    private String urlSource="https://www.fx-exchange.com/gbp/rss.xml";

    private ArrayList<Currency> currencies;

    private ListView myListView;

    private String[] names;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        rawDataDisplay = (TextView)findViewById(R.id.rawDataDisplay);

        currencies = new ArrayList<Currency>();

        myListView = (ListView) findViewById(R.id.countryListView);
        myListView.setOnItemClickListener(this);

        names = new String[0];
        CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), names);
        myListView.setAdapter(customAdapter);

        startProgress();



    }

    public void onClick(View aview)
    {

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object clickedObject = parent.getItemAtPosition(position);

        if (clickedObject != null) {
            // LINE 82: Now this line is safe
            String someValue = clickedObject.toString();

            rawDataDisplay.setText(currencies.get(position).toString());
        } else {
            // Handle the case where the object is null, e.g., show an error message
            Log.e("MainActivity", "Clicked object at position " + position + " is null.");
        }
    }

    public void startProgress()
    {
        // Run network access on a separate thread;
        new Thread(new Task(urlSource)).start();
    } //

    // Need separate thread to access the internet resource over network
    // Other neater solutions should be adopted in later iterations.
    private class Task implements Runnable
    {
        private String url;
        public Task(String aurl){
            url = aurl;
        }
        @Override
        public void run(){
            URL aurl;
            URLConnection yc;
            BufferedReader in = null;
            String inputLine = "";


            Log.d("MyTask","in run");

            try
            {
                Log.d("MyTask","in try");
                aurl = new URL(url);
                yc = aurl.openConnection();
                in = new BufferedReader(new InputStreamReader(yc.getInputStream()));
                while ((inputLine = in.readLine()) != null){
                    result = result + inputLine;
                }
                in.close();
            }
            catch (IOException ae) {
                Log.e("MyTask", "ioexception");
            }

            //Clean up any leading garbage characters
            int i = result.indexOf("<?"); //initial tag
            result = result.substring(i);

            //Clean up any trailing garbage at the end of the file
            i = result.indexOf("</rss>"); //final tag
            result = result.substring(0, i + 6);

            // Now that you have the xml data into result, you can parse it
            Currency thisCurrency = null;
            try {
                XmlPullParserFactory factory =
                        XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( new StringReader( result ) );

                //YOUR PARSING HERE!!!

                int eventType = xpp.getEventType();
                boolean insideAnItem = false; //flag indicating when the parser is traversing a new item


                while (eventType != XmlPullParser.END_DOCUMENT)
                {

                    if(eventType == XmlPullParser.START_TAG) // Found a start tag
                    {   // Check which start Tag we have as we'd do different things
                        if (xpp.getName().equalsIgnoreCase("item"))
                        {
                            insideAnItem = true;
                            //Start a new Thing object
                            thisCurrency = new Currency();
                            Log.d("MyTag","New Item found!");
                        }
                        else if (xpp.getName().equalsIgnoreCase("title"))
                        {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            //if  "description" tag is inside an item, or not
                            if(insideAnItem){ //the parser is currently inside an item block
                                temp = temp.substring(temp.indexOf("/")+1);
                                thisCurrency.setName(temp);
                                Log.d("MyTag","Item name : " + temp);
                            }

                        }
                        else if (xpp.getName().equalsIgnoreCase("description"))
                        {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            double thisRate = 0.0;
                            if(insideAnItem){ //the parser is currently inside a Thing block
                                thisCurrency.setDescription(temp);

                                int beginIndex = temp.indexOf("= ") + 2;
                                int endIndex = temp.indexOf(" ", beginIndex);

                                thisRate = Double.parseDouble(temp.substring(beginIndex, endIndex));
                                thisCurrency.setRate(thisRate);

                                Log.d("MyTag","Description is " + temp);
                            }
                        }
                        else if (xpp.getName().equalsIgnoreCase("pubDate"))
                        {
                            // Now just get the associated text
                            String temp = xpp.nextText();
                            if(insideAnItem){ //the parser is currently inside a Thing block
                                thisCurrency.setPubDate(temp);
                                Log.d("MyTag","Publish date is " + temp);
                            }
                        }

                    }
                    else if(eventType == XmlPullParser.END_TAG) // Found an end tag
                    {
                        if (xpp.getName().equalsIgnoreCase("item"))
                        {
                            currencies.add(thisCurrency); //add to collection
                            insideAnItem = false;
                            Log.d("MyTag","Item parsing completed!");
                        }
                    }




                    eventType = xpp.next(); // Get the next event  before looping again
                } // End of while



            } catch (XmlPullParserException e) {
                Log.e("Parsing","EXCEPTION" + e);
                //throw new RuntimeException(e);
            } catch (IOException e) {
                Log.e("Parsing","I/O EXCEPTION" + e);
                //throw new RuntimeException(e);
            }


            // Now update the TextView to display raw XML data
            // Probably not the best way to update TextView
            // but we are just getting started !

            MainActivity.this.runOnUiThread(new Runnable()
            {
                public void run() {
                    Log.d("UI thread", "I am the UI thread");

                    String newRes = "";

                    if (currencies.isEmpty()) {
                        rawDataDisplay.setText("Error: Failed to parse data.");
                        return;
                    }

                    names = new String[currencies.size()];
                    for (int i = 0; i < currencies.size(); i++) {
                        names[i] = currencies.get(i).getName();
                    }

                    CustomAdapter customAdapter = new CustomAdapter(getApplicationContext(), names);
                    Log.d("Log",names[1]);

                    myListView.setAdapter(customAdapter);

                    rawDataDisplay.setText("Select a currency");

                }
            });
        }

    }




}