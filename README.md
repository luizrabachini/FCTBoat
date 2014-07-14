FCTBoat
=======

Simple Android application developed to remote control a boat prototype using Raspberry Pi. Basically, the navigation system consist in define directions and intensity of movement in 2D plan, convert position into a command and send this to remote service running on Raspberry Pi. The command is interpreted by server and motors are activated using GPIO ports.

More informations available in this [article](http://luizrabachini.com/media/projects/FCTBoat/relatory.pdf) (in portuguese).


Installation
------------

1. Install [Eclipse IDE and ADT](http://developer.android.com/sdk/installing/installing-adt.html);

2. To run server (in Raspberry Pi): ```sudo python server.py```

A Nexus 4 device has been utilized in the tests to verify system functionalities. This [article](http://developer.android.com/tools/device.html) describes how to configure hardware devices to run Android applications.

The current minimum SDK required is 14 (Android 4.0 Ice Cream Sandwich).