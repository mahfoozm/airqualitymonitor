// CHANGE FILE EXTENSION TO .INO!!!
// EECS 1021 Major Project W22
// Mohammad Mahfooz
// 218621045

//libraries
#include "SparkFun_SGP30_Arduino_Library.h"
#include <Adafruit_GFX.h>
#include <Adafruit_SSD1306.h>
#include <dht.h>
#include <Wire.h>

//initialize display and temp/co2 sensors
Adafruit_SSD1306 display(128, 64, &Wire, -1);
dht DHT;
SGP30 co2Sensor;

void setup(void) 
{
  //start serial output to send data to Java
  Serial.begin(9600);

  pinMode(4, OUTPUT);
  pinMode(5, OUTPUT);
  pinMode(7, OUTPUT);

  //i2c components
  Wire.begin();
    if (co2Sensor.begin() == false) 
    {
      while (1);
    }
    
    if (!display.begin(SSD1306_SWITCHCAPVCC, 0x3C)) 
    {
      for(;;);
    }
    
  delay(2000);
  co2Sensor.initAirQuality();
  
  //clear display
  display.clearDisplay();
  display.setTextColor(WHITE);
  display.setRotation(2);

  //add humidity compensation
  co2Sensor.setHumidity(DHT.humidity);
  
}

void loop() 
{
 
  //refresh display to avoid text overlaying each other
  display.clearDisplay();

  //display temp
  display.setTextSize(1);
  display.setCursor(0,5);
  display.print("Temp: ");
  display.setCursor(0,15);
  display.print(DHT.temperature);
  display.print(" C");

  //display humidity
  display.setCursor(0,40);
  display.print("Humidity: ");
  display.setCursor(0,50);
  display.print(DHT.humidity);
  display.print(" %");

  //display co2
  display.setCursor(72,5);
  display.print("CO2: ");
  display.setCursor(72,15);
  display.print(co2Sensor.CO2);
  display.print(" ppm");
  display.display(); 

  //display tvoc (total volatile organic compounds)
  display.setCursor(72,40);
  display.print("TVOC: ");
  display.setCursor(72,50);
  display.print(co2Sensor.TVOC);
  display.print(" ppb");
  display.display(); 
 
}
