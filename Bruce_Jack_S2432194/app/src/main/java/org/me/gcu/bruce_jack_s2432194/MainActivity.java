package org.me.gcu.bruce_jack_s2432194;

//
// Name                 Jack Bruce
// Student ID           S2432194
// Programme of Study   Software Development
//



import android.annotation.SuppressLint;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.view.View.OnClickListener;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProvider;
import android.os.Handler;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import android.widget.SearchView;
import android.widget.ViewSwitcher;

public class MainActivity extends AppCompatActivity implements OnClickListener, AdapterView.OnItemClickListener, SearchView.OnQueryTextListener, TextWatcher {
    private TextView rawDataDisplay;

    //conversion page widgets
    private TextView convLeftTv;
    private TextView convRightTv;
    private TextView convResultTv;
    private EditText convEditText;
    private Button btnGoBack;
    private Button btnSwap;
    private String result;

    //components for the maps display
    private MapsFragment mapsFragment;
    private FrameLayout mapFrame;

    private ViewSwitcher switcher;

    //rss feed Strings
    private String urlSource="https://www.fx-exchange.com/gbp/rss.xml";

    //main page variables and widgets
    private CurrencyViewModel currencyViewModel;

    private ListView myListView;
    private SearchView searchView;
    private ImageView flagImageView;

    private CustomAdapter customAdapter;

    private Button switchButton;

    private Currency currentCurrency;

    private DecimalFormat df;
    private Handler mHandler;
    private ArrayList<Currency> mainList;
    private FrameLayout mainsContainer;
    private MainsFragment mainsFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //initialise the ViewModel
        currencyViewModel = new ViewModelProvider(this).get(CurrencyViewModel.class);

        rawDataDisplay = (TextView)findViewById(R.id.rawDataDisplay);

        df = new DecimalFormat("#.##");

        //Conversion page widgets
        convLeftTv = (TextView)findViewById(R.id.convLeftTextView);
        convRightTv = (TextView)findViewById(R.id.convRightTextView);
        convResultTv = (TextView)findViewById(R.id.convResultTextView);
        convEditText = (EditText)findViewById(R.id.convEditText);
        btnGoBack = (Button)findViewById(R.id.btnGoBack);
        btnGoBack.setOnClickListener(this);
        btnSwap = (Button)findViewById(R.id.btnSwap);
        btnSwap.setOnClickListener(this);
        convEditText.addTextChangedListener(this);

        mapsFragment = new MapsFragment();
        FragmentManager manager1 = getSupportFragmentManager();
        FragmentTransaction transaction1 = manager1.beginTransaction();
        transaction1.replace(R.id.mapFragment, mapsFragment);
        transaction1.commit();

        mainsFragment = new MainsFragment();
        FragmentManager manager2 = getSupportFragmentManager();
        FragmentTransaction transaction2 = manager2.beginTransaction();
        transaction2.replace(R.id.mainsContainer, mainsFragment);
        transaction2.commit();

        //list page widgets and variables
        myListView = (ListView) findViewById(R.id.countryListView);
        myListView.setOnItemClickListener(this);

        searchView = (SearchView) findViewById(R.id.searchView);
        searchView.setOnQueryTextListener(this);

        flagImageView = (ImageView) findViewById(R.id.flagImageView);

        switcher = (ViewSwitcher) findViewById(R.id.vwSwitch);

        switchButton = (Button) findViewById(R.id.btnSwitch);
        switchButton.setOnClickListener(this);

        customAdapter = new CustomAdapter(getApplicationContext(), currencyViewModel.getCurrencyList());
        myListView.setAdapter(customAdapter);

        mainList = new ArrayList<Currency>();

        //observe for changes in the list of currencies
        currencyViewModel.getCurrencies().observe(this, currencies -> {
            //use lambda expression to pass newly fetched data to the adapter
            customAdapter.updateData(currencies);
        });

        //only fetch data if ViewModel is empty (e.g screen has not been rotated)
        if (currencyViewModel.getCurrencyList().isEmpty()) {
            startProgress();
        }
    }

    public void onClick(View aview)
    {
        //if the button pressed is either button meant for switching ViewSwitcher
        if (aview.getId() == R.id.btnSwitch || aview.getId() == R.id.btnGoBack){
            //only two views so we can just make use of the circular nature of the ViewSwitcher
            switcher.showNext();
        }
        //if user presses button to swap currencies on conversion page
        else if (aview.getId() == R.id.btnSwap){

            //maybe look at storing these in viewModel

            //swap the widget text contents around
            String temp1 = convLeftTv.getText().toString();
            String temp2 = convRightTv.getText().toString();
            convLeftTv.setText(temp2);
            convRightTv.setText(temp1);

            //preserve the current conversion values
            temp1 = convEditText.getText().toString();
            temp2 = convResultTv.getText().toString();
            convEditText.setText(temp2);
            convResultTv.setText(temp1);
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Object clickedObject = parent.getItemAtPosition(position);
        currentCurrency = (Currency) clickedObject;

        //set the clicked currency as the current currency in the ViewModel
        currencyViewModel.selectCurrency(currentCurrency);

        //round to two dp
        df.format(currentCurrency.getRate());


        if (currentCurrency != null) {

            //use toString() to display basic currency information
            rawDataDisplay.setText(currentCurrency.toString());
            double rate = currentCurrency.getRate();

            //get the int identifier for color resources here
            //data annotation is because Android Studio does NOT like this logic
            @SuppressLint("DiscouragedApi") int colourId = getResources().getIdentifier(currentCurrency.getColour(), "color", getPackageName());
            rawDataDisplay.setTextColor(ContextCompat.getColor(this, colourId));

            //Once again get int identifier for images
            @SuppressLint("DiscouragedApi") int flagId = getResources().getIdentifier(currentCurrency.getCode().toLowerCase(), "drawable", getPackageName());
            if (flagId != 0) {
                flagImageView.setImageResource(flagId);
            } else {
                Log.w("Error", "Resource Not Found ");
                flagImageView.setImageResource(0);
            }


            convLeftTv.setText("GBP");
            convRightTv.setText(currentCurrency.getCode());
            convResultTv.setText(String.valueOf(currentCurrency.getRate()));
            convEditText.setText("1");
        } else {
            //null handler
            Log.e("MainActivity", "Clicked object at position " + position + " is null.");
        }
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        //Don't need to use this but it is required to implement the next method
        return false;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        // This is called every time the user types a character
        if (customAdapter != null) {
            customAdapter.getFilter().filter(newText);
        }
        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence s, int start, int count, int after) {
        //Don't need to use this but it is required to implement the interface
    }

    @Override
    public void onTextChanged(CharSequence s, int start, int before, int count) {
        //Don't need to use this but it is required to implement the interface
    }

    @Override
    public void afterTextChanged(Editable s) {
        if (currentCurrency != null && s.length() > 0) {
            try {
                double input = Double.parseDouble(s.toString());
                double result;

                //if we are converting INTO GBP
                if(convRightTv.getText().toString().equals("GBP")){
                    result = input / currentCurrency.getRate();
                }
                else{
                    result = input * currentCurrency.getRate();
                }

                //round to two dp
                convResultTv.setText(df.format(result));
            } catch (NumberFormatException e) {
                convResultTv.setText("Invalid Input");
            }
        } else if (s.length() == 0) {
            convResultTv.setText("");
        }
    }

    public void startProgress()
    {
        mHandler = new Handler();
        // Run network access on a separate thread;
        new Thread(new Task(urlSource)).start();
    }

    //method to fetch the coordinates from the strings file and set the currency lat and long to these values
    //Currency class did not like this method being in it so it is now just a helper method
    //called in the parsing thread
    public void setCoordinates(Currency thisCurrency){
        //make code lower because resource names must be lowercase
        String currencyCode = thisCurrency.getCode().toLowerCase();

        //get int identifier for coordinate item
        @SuppressLint("DiscouragedApi")
        int coordStringId = getResources().getIdentifier(currencyCode, "string", getPackageName());

        if (coordStringId != 0) {
            String coordinates = getString(coordStringId);

            //split into latitude and longitude
            String[] parts = coordinates.split(",");

            //if parsed correctly it will be two parts
            if (parts.length == 2) {
                try {
                    //parse into doubles and set values
                    thisCurrency.setLatitude(Double.parseDouble(parts[0].trim()));
                    thisCurrency.setLongitude(Double.parseDouble(parts[1].trim()));

                } catch (NumberFormatException e) {
                    Log.e("MainActivity", "Failed to parse coordinates for: " + currencyCode, e);
                }
            }
        } else {
            Log.w("MainActivity", "Coordinate string not found for currency: " + currencyCode);
        }
    }



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
            ArrayList<Currency> currencies = new ArrayList<>();

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

            //Now that you have the xml data into result, you can parse it
            Currency thisCurrency = null;
            try {
                XmlPullParserFactory factory =
                        XmlPullParserFactory.newInstance();
                factory.setNamespaceAware(true);
                XmlPullParser xpp = factory.newPullParser();
                xpp.setInput( new StringReader( result ) );

                int eventType = xpp.getEventType();
                boolean insideAnItem = false; //flag indicating when the parser is traversing a new item

                String[] mainCurrencyCodes = getResources().getStringArray(R.array.main_currencies);
                List<String> mainCurrenciesList = Arrays.asList(mainCurrencyCodes);

                while (eventType != XmlPullParser.END_DOCUMENT)
                {
                    if(eventType == XmlPullParser.START_TAG) // Found a start tag
                    {   //Check which start Tag we have as we'd do different things
                        if (xpp.getName().equalsIgnoreCase("item"))
                        {
                            insideAnItem = true;
                            //Start a new Thing object
                            thisCurrency = new Currency();
                            Log.d("MyTag","New Item found!");
                        }
                        else if (xpp.getName().equalsIgnoreCase("title"))
                        {
                            //Now just get the associated text
                            String temp = xpp.nextText();
                            //if  "description" tag is inside an item, or not
                            if(insideAnItem){ //the parser is currently inside an item block
                                temp = temp.substring(temp.indexOf("/")+1);
                                thisCurrency.setName(temp);
                                thisCurrency.setCode(temp.substring(temp.indexOf("(")+1,temp.indexOf(")")));

                            }
                        }
                        else if (xpp.getName().equalsIgnoreCase("description"))
                        {
                            //Now just get the associated text
                            String temp = xpp.nextText();
                            double thisRate = 0.0;
                            if(insideAnItem){ //the parser is currently inside an item block
                                thisCurrency.setDescription(temp);

                                int beginIndex = temp.indexOf("= ") + 2;
                                int endIndex = temp.indexOf(" ", beginIndex);

                                thisRate = Double.parseDouble(temp.substring(beginIndex, endIndex));
                                thisCurrency.setRate(thisRate);
                                thisCurrency.setColour(thisRate);


                                Log.d("MyTag","Description is " + temp);
                            }
                        }
                        else if (xpp.getName().equalsIgnoreCase("pubDate"))
                        {
                            //Now just get the associated text
                            String temp = xpp.nextText();
                            if(insideAnItem){ //the parser is currently inside an item block
                                thisCurrency.setPubDate(temp);
                                Log.d("MyTag","Publish date is " + temp);
                            }
                        }
                    }
                    else if(eventType == XmlPullParser.END_TAG) //Found an end tag
                    {
                        if (xpp.getName().equalsIgnoreCase("item"))
                        {
                            //ensure object has been read correctly
                            if (thisCurrency != null) {
                                //if this currency's code is in the list of main currencies
                                if (mainCurrenciesList.contains(thisCurrency.getCode().toLowerCase())) {
                                    thisCurrency.setIsMain(true); //set main to true
                                    mainList.add(thisCurrency); //add to main list
                                }

                                //and then finally set the coordinates
                                setCoordinates(thisCurrency);
                            }
                            currencies.add(thisCurrency); //add to collection
                            insideAnItem = false;
                            Log.d("MyTag","Item parsing completed!");
                        }
                    }


                    eventType = xpp.next(); //Get the next event  before looping again
                } //End of while
            } catch (XmlPullParserException e) {
                Log.e("Parsing","EXCEPTION" + e);
            } catch (IOException e) {
                Log.e("Parsing","I/O EXCEPTION");
            }


            mHandler.post(new Runnable(){
                @Override
                public void run(){
                    Log.d("UI thread", "I am the UI thread");

                    if (currencies.isEmpty()) {
                        rawDataDisplay.setText("Error: Failed to parse data.");
                        return;
                    }
                    //update the LiveData in the ViewModel therefore triggering the observers and updating the adapter
                    currencyViewModel.setCurrencies(currencies);
                    currencyViewModel.setMainList(mainList);
                }
            });
        }
    }
}
