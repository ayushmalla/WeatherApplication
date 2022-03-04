package com.burwa.weatherapp.WeatherApp.view;


import com.burwa.weatherapp.WeatherApp.controller.WeatherService;
import com.vaadin.icons.VaadinIcons;
import com.vaadin.server.ClassResource;
import com.vaadin.server.ExternalResource;
import com.vaadin.server.VaadinRequest;
import com.vaadin.spring.annotation.SpringUI;
import com.vaadin.ui.Label;
import com.vaadin.ui.*;
import com.vaadin.ui.VerticalLayout;
import com.vaadin.ui.themes.ValoTheme;
import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;


@SpringUI(path = "")
public class MainView  extends UI{

    @Autowired
    private WeatherService service;
    private VerticalLayout mainLayout;
    private NativeSelect<String> unitSelect;
    private TextField cityTextField;
    private Button showWeatherButton;
    private Label currentLocationTitle;
    private Label currentTemp;
    private Label weatherDescription;
    private Label weatherMini;
    private Label weatherMax;
    private Label pressureLabel;
    private Label humidityLabel;
    private Label windSpeedLabel;
    private Label sunRiseLabel;
    private Label sunSetLabel;
    private Image iconImage;
    private HorizontalLayout formLayout;
    private HorizontalLayout dashLayout;
    private HorizontalLayout mainDescriptionLayout;
    private VerticalLayout descriptionLayout;
    private VerticalLayout pressureLayout;

    @Override
    protected void init(VaadinRequest request) {
        setUpLayout();
        setHeader();
        setLogo();
        setUpForm();
        dashBoardTitle();
        dashBoardDescription();

        showWeatherButton.addClickListener(event ->{
           if(!cityTextField.getValue().equals("")){
               try {
                   updateUI();
               } catch (IOException e) {
                   e.printStackTrace();
               } catch (JSONException e) {
                   e.printStackTrace();
               }
           }else{
               Notification.show("Please enter the city");
           }
        });
    }


    public void setUpLayout(){
        service = new WeatherService();
        iconImage = new Image();

        mainLayout = new VerticalLayout();
        mainLayout.setWidth("100%");
        mainLayout.setMargin(true);
        mainLayout.setSpacing(true);

        mainLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        setContent(mainLayout);
    }
    private void setHeader(){
        HorizontalLayout headerLayout = new HorizontalLayout();
        headerLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        Label title = new Label("Weather!");
        title.addStyleName(ValoTheme.LABEL_H1);
        title.addStyleName(ValoTheme.LABEL_BOLD);
        title.addStyleName(ValoTheme.LABEL_COLORED);

        headerLayout.addComponent(title);

        mainLayout.addComponents(headerLayout);
    }
    private void setLogo() {
        HorizontalLayout logoLayout = new HorizontalLayout();
        logoLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        Image logo = new Image(null, new ClassResource("/weather.png"));
        logo.setWidth("120px");
        logo.setHeight("120px");

        logoLayout.addComponent(logo);

        mainLayout.addComponent(logoLayout);
    }
    private  void setUpForm(){
        formLayout = new HorizontalLayout();
        formLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);
        formLayout.setSpacing(true);
        formLayout.setMargin(true);

        //Creating a selection component
        unitSelect  = new NativeSelect<>();
        unitSelect.setWidth("120px");
        ArrayList<String> items = new ArrayList<>();
        items.add("Celcius");
        items.add("Fahrenheit");

        unitSelect.setItems(items);
        unitSelect.setValue(items.get(1));
        formLayout.addComponent(unitSelect);

        //Add TextField
        cityTextField = new TextField();
        cityTextField.setStyleName("City Name");
        cityTextField.setWidth("200px");
        formLayout.addComponent(cityTextField);

        //Add button
        showWeatherButton = new Button();
        showWeatherButton.setIcon(VaadinIcons.SEARCH);
        formLayout.addComponent(showWeatherButton);

        mainLayout.addComponent(formLayout);
    }
    private void dashBoardTitle(){
        dashLayout = new HorizontalLayout();
        dashLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        currentLocationTitle = new Label("Currently in Spoken");
        currentLocationTitle.setStyleName(ValoTheme.LABEL_H1);
        currentLocationTitle.setStyleName(ValoTheme.LABEL_LIGHT);



        //Add current Temp;
        currentTemp = new Label("19F");
        currentTemp.setStyleName(ValoTheme.LABEL_H1);
        currentTemp.setStyleName(ValoTheme.LABEL_BOLD);
        currentTemp.setStyleName(ValoTheme.LABEL_LIGHT);


    }
    private void dashBoardDescription(){
        mainDescriptionLayout = new HorizontalLayout();
        mainDescriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        //Description layout
        descriptionLayout = new VerticalLayout();
        descriptionLayout.setStyleName(ValoTheme.FORMLAYOUT_LIGHT);
        descriptionLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        weatherDescription = new Label("Descrption : Clear Sky");
        descriptionLayout.addComponent(weatherDescription);

        weatherMini = new Label("Min: 56F");
        descriptionLayout.addComponent(weatherMini);

        weatherMax = new Label("Max : 89F");
        descriptionLayout.addComponent(weatherMax);

        //Pressure, humidity etc....
        pressureLayout = new VerticalLayout();
        pressureLayout.setDefaultComponentAlignment(Alignment.MIDDLE_CENTER);

        pressureLabel = new Label("Pressure : 123pa");
        pressureLayout.addComponent(pressureLabel);

        humidityLabel = new Label("Humidity: 34");
        pressureLayout.addComponent(humidityLabel);

        windSpeedLabel = new Label("WindSpeed: 123/hr");
        pressureLayout.addComponent(windSpeedLabel);

        sunRiseLabel = new Label("Sunrise");
        pressureLayout.addComponent(sunRiseLabel);

        sunSetLabel = new Label("Sunset");
        pressureLayout.addComponent(sunSetLabel);



    }
    private void updateUI() throws IOException, JSONException {
        String city = cityTextField.getValue();
        String defualtUnit;
        String unit;

        if(unitSelect.getValue().equals("Fahrenheit")){
            defualtUnit = "imperial";
            unitSelect.setValue("Fahrenheit");
            //signifies degree
            unit = "\u00b0"+"F";
        }else{
            defualtUnit = "metric";
            unitSelect.setValue("Celcius");
            unit = "\u00b0"+"C";
        }

        service.setCityName(city);
        service.setUnit(defualtUnit);

        currentLocationTitle.setValue("Currently in "+city);

        JSONObject myObject = service.returnMain();
        double temp = myObject.getDouble("temp");

        currentTemp.setValue(temp+unit);


        //pressure,humidity, temp min,max
        JSONObject mainObject = service.returnMain();
        double minTemp = mainObject.getDouble("temp_min");
        double maxTemp = mainObject.getDouble("temp_max");
        int pressure = mainObject.getInt("pressure");
        int humidity = mainObject.getInt("humidity");




        //get windSpeed
        JSONObject windSpeed = service.returnWindObject();
        double wind = windSpeed.getDouble("speed");

        //get sunRise, sunset
        JSONObject sun = service.returnSun();
        long sunRise = sun.getLong("sunrise")*1000;
        long sunSet = sun.getLong("sunset")*1000;


        //set up Icon
        String iconCode = "";
        String description = "";
        JSONArray jsonArray = service.returnWeatherArray();



        for(int i = 0; i < jsonArray.length();i++){
            JSONObject weatherObject = jsonArray.getJSONObject(i);
            description = weatherObject.getString("description");
            iconCode = weatherObject.getString("icon");
            System.out.println(iconCode);

        }
        iconImage.setSource(new ExternalResource("http://openweathermap.org/img/wn/"+iconCode+".png"));


        dashLayout.addComponents(currentLocationTitle, iconImage, currentTemp);
        mainLayout.addComponent(dashLayout);



        //Update Description UI
        weatherDescription.setValue("Cloudness: "+description);
        weatherMini.setValue("Temp_Min : "+String.valueOf(minTemp)+unit);
        weatherMax.setValue("Temp_Max : "+String.valueOf(maxTemp)+unit);
        pressureLabel.setValue("Pressure : "+String.valueOf(pressure)+"hpa");

        humidityLabel.setValue("Humidity : "+String.valueOf(humidity)+"%");
        windSpeedLabel.setValue("WindSpeed : "+String.valueOf(wind)+"m/s");

        sunRiseLabel.setValue("SunRise : "+convertTime(sunRise));
        sunSetLabel.setValue("SunSet : "+convertTime(sunSet));

        //Adding the Components in layouts
        mainDescriptionLayout.addComponents(descriptionLayout, pressureLayout);
        mainLayout.addComponent(mainDescriptionLayout);


    }

    @NotNull
    private String convertTime(long time){
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yy hh.mm aa");
        return dateFormat.format(new Date(time));
    }
    }



/*
try {
            //System.out.println("Data: "+service.getWeather("Dehli").getString("coord"));
    //for weather array
            JSONArray jsonArray = service.returnWeatherArray("mumbai");

            for(int i = 0; i < jsonArray.length();i++){
                JSONObject weatherObject = jsonArray.getJSONObject(i);

                System.out.println("id: "+weatherObject.getInt("id")
                +", "+"main"+weatherObject.getString("main")+
                        ", "+weatherObject.getString("description")+
                        ", "+weatherObject.getString("icon"));
            }
            //for main object
            JSONObject myObject = service.returnMain("mumbai");

            System.out.println("temp: "+myObject.getInt("temp")
                    +", "+"Feels_like: "+myObject.getString("feels_like")+
                    ", "+"temp_min: "+myObject.getDouble("temp_min")+
                    ", "+"temo_max: "+myObject.getDouble("temp_max")+
                    ", "+"pressure: "+myObject.getLong("pressure"));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
 */
