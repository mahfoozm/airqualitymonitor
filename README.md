# Air Quality Monitor

### YouTube Video Link : https://www.youtube.com/watch?v=Uq2lYNdrcdU

## Introduction
As we are currently still fighting the COVID-19 pandemic, I
thought it would be a no-brainer to build an air quality monitoring system, as
monitoring CO2 levels in a space is a great way to determine if the space is
adequately ventilated. Good ventilation is key in the fight against COVID, and
my project is instrumental in pushing the cause to improve ventilation in indoor
spaces.

## Context
Air quality monitors (CO2 Sensors) are commonly used to determine the
“freshness” of air. Now relevant more than ever, as COVID is airborne, they can
be used to determine if a space is being sufficiently ventilated. The consensus
is that if the CO2 levels in a space are higher than 800ppm, the space needs
better airflow. My project focuses on displaying real time CO2 levels in different
ways, and to allow the user to manipulate the airflow in a setting, so that
insufficiently ventilated spaces can have their airflow improved.

## Technical Requirements / Specifications
● Acquire real-time CO2 levels in ppm and TVOC (Total Volatile Organic
Compounds) levels in ppb from VOC and eCO2 Gas Sensor (SGP30)
● Acquire real-time temperature and relative humidity data from
Temp/Humidity sensor (DHT11)
● Print real-time CO2 level, TVOC level, temp and humidity to OLED
display (SSD1315)
● Send real-time CO2 levels over serial port interface, so Java can be
utilised
● Plot real-time CO2 levels using JavaFX library
● Allow user to turn on Fan (DC Motor)/future accompanying ventilation
system through JavaFX GUI, and turn on LED and sound buzzer while
fan is running

## Components List
● Seeedstudio Grove Arduino UNO compatible board (1x)
● Grove OLED Display 0.96 inch - SGP1315 (1x)
● Grove VOC and eCO2 Gas Sensor (Air Quality Sensor) - SGP30 (1x)
● Grove Temperature and Humidity Sensor - DHT11 (1x)
● LED (1x)
● Buzzer (1x)
● DC Motor (1x)
● DC Motor Driver Components (1x PN2222 Transistor, 1x 1N4001 diode, 1x
270 Ohm Resistor, 1x Breadboard)

![Diagram of the system and its associated components](https://i.imgur.com/sbXL8zU.png)

Diagram of the system and its associated components


## Procedure
Coming into this project, my idea was to display CO2 values on the Grove
OLED display, using the Firmata4j library. However, I quickly realised that due to
limitations with Firmata4j, this would not be very easy. Although the OLED,
which is an I2C device, is natively supported by this library, the SGP30 sensor is
not natively supported by the library. I took a few different approaches to trying
to solve this problem, which I’ll go through.

The first thing I tried was attempting to create an I2C driver for the SGP
sensor so that I could contain all of the processing in Java, over Firmata. I found
a library called Diozero that looked very promising. However, after some
digging and with confirmation from the developer of the library (thank you,
Matthew Lewis!) I found out that the SGP30 sensor relies on raw I2C read/write
instructions and that Firmata wouldn’t work to interface with this sensor (see:
https://github.com/mattjlewis/diozero/discussions/92 ). So, back to the drawing
board.

After this, I went back to brainstorming. My next idea was to use a second
arduino board (pictured below, along with the Grove Board) to act as a
dedicated device with the SGP30 sensor connected to it, with the sole purpose
of outputting real-time CO2 levels over a serial port, so that I could use
jSerialComm to obtain this value and still use Firmata on the grove board for
the display, and other components. However, as pictured below, I ran into some
unexpected errors with having multiple arduinos connected to the same
system (and polling both for serial data). Only one serial port would open at
once, which meant that I would only be able to obtain a CO2 value from the
second arduino, or I would be able to interface with the grove board. Doing
both at the same time wasn’t an option. Feeling hopeless, I decided to go back
to brainstorming.

![Surely this’ll work....](https://i.imgur.com/C2GkKPG.png)

Surely this’ll work....


![Nope. Nevermind. Not happening.](https://i.imgur.com/EQhbRum.png)

Nope. Nevermind. Not happening.


At this point, I already invested so much time into this idea, so I really wanted to
follow through with it. I decided to create a custom Arduino program, and use
jSerialComm (a serial library) on the Java end. This allowed me to use an
existing SGP30 library that I could load onto the grove board. This option was
far more successful, and it really made me wish that it was the first thing I tried
(but at least I learned a lot with my first two attempts!).

So, my final project basically operates like this: CO2 data is sent over the serial
bus, where jSerialComm reads the value and will graph it in real time and
display values on a table using the JavaFX library. The GUI window created by
JavaFX also has two buttons on it, which allows the user to turn a fan (motor)
on/off, by sending a value over the serial bus which the grove board is
constantly looking for. While the fan is running, a buzzer and LED will sound to
alert others. Simultaneously, the OLED is being constantly updated with CO
levels and temperature/humidity levels from the SGP30 sensor and DHT
sensor. The end result is a fully fledged air quality monitoring station, that gives
the user the ability to increase ventilation based on the real time CO2 levels.

I’m glad I didn’t give up and think of a new idea after my first attempt, as I
learned a lot reading through all the documentation for Diozero and Firmata4J
when attempting to create my own sensor driver. I hope that I can come back
to this original attempt later when I have more programming experience, and
attempt to create a driver for the SGP30 that works with Firmata4J, but I’m not
really on that level yet, unfortunately.

## Test
My testing process was quite simple. For the temperature/humidity sensor, I
opened the window and slowly saw the temperature and humidity dropping (it’s
getting cold!), and for the CO2/TVOC sensor, I blew into the sensor (as we
exhale CO2) and saw the values start rising rapidly. I also wanted to see how
well ventilated my room was with the HVAC system turned off, and, well,

![CO2 Levels.](https://i.imgur.com/dQqaBsa.png)


I hope that YorkU is doing a better job with ventilation than my current setup. :)
I also tested the JavaFX chart and table with the same testing methodology:

![CO2 Levels.](https://i.imgur.com/N8X67P7.png)


As you can see, this is working as desired. The turn fan on/off buttons also
work just fine, as I will demonstrate in the video.

## Contingency
When I found out that the SGP30 sensor wouldn’t play nicely with Firmata4j, I
was definitely disappointed. I was thinking of just making a simple weather
monitoring station with the DHT11 sensor, this wouldn’t be complex enough and
would not satisfy the requirements of the major project (and it wouldn’t be
cool!). I also wanted to plot real-time CO2 values in Toronto from a different
data source on the real time chart to compare the values, but could not find a
free API that provided this data. I was definitely ready to give up and switch my
idea when I found out that Firmata4J would not support the SGP30 sensor
natively, but (as cliche as it sounds) not giving up ended up paying off in the
end.

## Additional Material
I believe the best part of my project is how relevant it is today, and the
expandability it has. Obviously, COVID is airborne, and better ventilation to
ensure fresher air is key in preventing the spread of this virus. Implementing
CO2 sensors in closed spaces is useful enough on its own to help determine
which rooms require better ventilation, but the great thing here is that there is
so much room for expansion. For example, the board/code can be
reconfigured to enable an air purifier with the use of a MOSFET/relay when the
CO2 levels drop below the 800ppm threshold. Even in its current state, it is a
very useful tool as it can alert people in a closed space if the air is “dirty”
enough to allow the spread of COVID, so they can take steps to correct the
issue (e.g, by turning the fan on). It can also be used as a long-term monitoring
tool to detect trends in CO2 levels over a long period of time, to identify times
of the day where CO2 levels become troublesomely high. CO2 sensors like my
project should be in every classroom/lecture hall at York, even if ventilation
improvements were made in preparation for our return to campus back in
February. This is so that if any ventilation system fails, there is an obvious
indication available as soon as possible, and it could be remedied temporarily
with this backup system. I believe this project, even in its current state, is very
relevant in a number of social and environmental aspects, and there is a lot of
room to build upon it for those who desire more.

## Conclusion
Although I went through an emotional rollercoaster when working through this
project, I am proud of the end result I achieved and I believe that the journey I
went through strengthened my coding skills and solidified what I learned over
this semester. My project turned out better than I thought as it ended up being
a lot more versatile than I originally envisioned (the addition of the real-time
CO2 levels JavaFX chart was a later addition, and brought a whole new aspect
of long-term CO2 monitoring to the project). I’m glad I chose to undertake the
challenge of completing the major project, and I do not regret my decision at
all.

## References
Indoor CO2 sensors for COVID-19 risk mitigation. National Collaborating Centre
for Environmental Health. (2021, May 19). Retrieved December 3, 2021, from
https://ncceh.ca/sites/default/files/FINAL%20-%20Using%20Indoor%20CO2%
0Sensors%20for%20COVID%20MAY%2017%202021.pdf.

Smith, J. A. (2021, May 14). Fighting COVID with Ventilation. York University.
Retrieved December 3, 2021, from
https://www.yorku.ca/professor/drsmith/2021/05/14/fighting-covid-with-ventilati
on/.

Jimenez, J.-L. (2020, August 25). Covid-19 is transmitted through aerosols. we
need to adapt. Time. Retrieved December 3, 2021, from
https://time.com/5883081/covid-19-transmitted-aerosols/.

Marr, L., Miller, S., Prather, K., Haas, C., Bahnfleth, W., Corsi, R., Tang, J.,
Herrmann, H., Pollitt, K., Ballester, J., & Jiminez, J.-L. (n.d.). FAQs on
protecting yourself from aerosol transmission. Retrieved December 3, 2021,
from
https://docs.google.com/document/d/1fB5pysccOHvxphpTmCG_TGdytavMmc1c
Uumn8m0pwzo/edit.

Thank you to Matthew Lewis (maintainer of the Diozero project) for confirming
my suspicions, especially when I thought I was going crazy!
https://github.com/mattjlewis/diozero/discussions/

Thank you to Richard Robinson and James Andrew Smith of York University for
providing the DataController.java (which I edited quite substantially) and the
SerialPortService.java code, to help interface with jSerialComm!
