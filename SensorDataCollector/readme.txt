03/13/2014
Done
(1) Done 2014-02-21-(6)
    Added a confirm and a cancel button on the Sensor Setup Fragment layout.
    When the confirm button is pressed, it uses values entered in the user
    interface and goes back to the sensor list fragment. When the cancel button is
    pressed, it ignores the values in the interface and goes back to the sensor
    list fragment. 

To do(2014-03-13)
(9) If on the sensor setup fragment, nothing can be changed, replace the two
    buttons ("Confirm" and "Cancel") by a "Back" button.
    

02/24/2014
Done
(1) Prohibited screen rotation to defer handling restoration of application
    state from savedInstanceState, i.e., to defer dealing with configuraiton
    change.

    Reference : http://stackoverflow.com/questions/8997822/android-efficient-screen-rotation-handling

(2) Done 2014-02-21-(1)
    Additional logic added to disable controls/buttons in SensorListFragment
    when the program is collection data. 



02/21/2014
To do(2014-02-21):
(1) change the Run button logic.
    when the button is clicked, if the action is successful, the button
    becomes a "stop" button. When the stop button is clicked, it calls
    DataSensorEventListener's finish() method to flush and close all
    output streams. 

(2) restructure the application so that the application functions when
the configuration (orientation) changes and when it is paused and resumed. 

(3) change the style to something better, such as Holo

(4) add "pseudo-sensors", i.e., audio and camera sensor functionality. 
    For this task, need to add a sensor type to the DataSensor class. The
    predefined type can be "Sensor (Integer 0)", "Audio (Integer 1)", and
    "Camera (Integer 2)". 

(5) revise and enhance experiment setup logic and interface.
    simple solution
    (a) create directory called DataSensorCollector
    (b) create directory for each experiment. The directory takes pattern of
        exp001, exp002, ... format. 
    (c) add a expnum.txt file under the directory DataSensorCollector, in which
        it stores next experiment number to use. 

(6) add a confirm and a cancel button on the Sensor Setup Fragment layout.
When the confirm button is pressed, it uses values entered in the user
interface and goes back to the sensor list fragment. When the cancel button is
pressed, it ignores the values in the interface and goes back to the sensor
list fragment. 

(7) add exit button

(8) Abstract sensors
    Android Sensors
    Camerea Sensors
    Audio Sensors
    Bluetooth Sensors
    WiFi Sensors
    Internal Health Sensors (battery voltage and temperature)

Reference: 
http://stackoverflow.com/questions/4878159/android-whats-the-best-way-to-share-data-between-activities
