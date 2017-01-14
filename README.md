+ Project 2016 - 2017 - Phase 2 - Android client
	+  Index:
		+  [Topics](#topics)
		+  [Classes](#classes)
		+  [Class Methods](#methods)  
	

___________________________________________________________________

+ <a name="topics">Topics</a>
	+ **log:** Not being used by the android client
	+ **connections:** Everything that has to do with a connection.
	+ **connections/newConnections:** When a new android client connects, they notify the desktop client by publishing to this topic
	+ **connections/connected:** Already connected android clients pulish to this + their UUID topic.
	+ **connections/connected/\<Client UUID\>:** Already connected android client.
	+ **connections/connected/\<Client UUID\>/**
		+ **warning:** The desktop client publishes the warning messages to this topic.
		+ **danger:** The desktop client publishes the danger messages to this topic.
		+ **stopSounds:** The desktop client publishes the stop all sounds messages to this topic.
		+ **acknowledged:** The desktop client publishes to this topic of a specific UUID to inform the android client that the desktop client is online.
	+ **connections/requestAck:** Before an android client goes into Online Mode, they request an acknowledgement.
	+ **mainClient/disconnected:**  If the desktop application disconnects ungracefully (it always does), all of the connected clients are notified and go into Offline Mode.
	<p>
		<img src="https://lh3.googleusercontent.com/EMTDe2A2bCl9MuYJKZhCLX_tX7UdKtdf8fYdxdcuCFidDoLNWDPSGSRl4oVtERlpl-79DWBNPMcfacocgttfsDVU6azZSdBP_WkuWFD2jJ2F8gW6waPMZqLLN0g3wm8OvVqph2EdKa19BJYjsqYHRyAvq9kLpfUcwLtEaH2fTq80pnvMS5q1wzfTGcRKl0gtI-GgZoyINp9rIRpnKFovbLAk6h2PP1O4wwLJT99YfX0txHF1OFchctcVTubwk4zjAbNMjcq3cL0Ad6cVlIBt_71riQmHjeu2gCpdY2uXs68ZlwlUXNOzh9X7C2BwyIrDisLDZmPhtLoNUSl5eCoe_j5sL4CZAVWomGBtwjofZsbiA3fe1D4qyizQ1eRNseiFzk5f9Rcaoa3z4FiseBP9KKZbIyibwM7xOWOv-6B3U-l-9cg8B4FxBR1hLd7QfB9xZuQ80dU0NywqltCgg1tsRDz2-pbNJ1aL095OfSZeZwjX290c1as0ZkAkJZhaa1_eKt1ZajAqfI2CZdJIfhzdQIbiKmLn-NgJ6fQ4Mh6Ve1OXOlOXtiuRcmVbKGWFeoQXeBHu_o1KupHzSX5Xff5GLd_zgDs7cP_fIN0VhO6HFdMsm4GmYExz=w1063-h501-no" />
	 </p>
	+ <a name="message_format">Message format by topic:</a>
		+ **log** -> No standard format
		+ **connections/newConnections** -> \<"Client's UUID"\>
		+ **connections/connected/\<Client's UUID\>** -> \<"Client UUID"/"latitude"/"longitude"/"light sensor value"/"proximity sensor value">
		+ **connections/connected/\<Client's UUID\>/:**
			+   warning -> "warning"
			+   danger -> "danger"
			+   stopSounds -> "stop warning"
			+   acknowledged -> <The frequency at which the android client should send data to *the* desktop client
		+  **connections/requestAck** -> \<"Client UUID"\>
		+  **mainClient/disconnected** -> "disconnecting"
	+ **_Classes_**:
		<p align="center">
			<img src="https://lh3.googleusercontent.com/b-KJz-5ENFFol01knQUWUfueHWCdnVFrG1L2Lf04NZ_LDoNiPornBzh_a67oTlsxvOZzuJqT8FNfzA6QdKGz7KhDvUOW2nGfvYk_MK_LSRj8fJ_fn5pBcJ1b08_rBEI_eFcmgLNsNN7gPaw7Ekak_uate2O83RJ8Cltq-cZHU8JpYL8_06ettTzBiflPgywgPBKB9hVIe3rSCBNk_zfz18FIhIH9kcO6pVjp0JdbrP2dDePX_jGaivEisTGKPtF6NqKYXRO4eneHBcx3t1N5x57fFiXGJw8VyMRcSnHGoNekPWIBUEnNYNP-UlwdrleGvd9sQnfVWkejpKlF1P-oFGH5CY4H91lL9j3ZdUIbNIz8cw4R0gUex2M0YBnQvcX33SSumL6VX1fcSSb9ZKuXxqTLwD47FJnQu4FvNaQ45kM9z6rmrItfliPAqF2IMluVrmb_8dvrBjtdwOqPAFoI17p3JUL0nGWhrcHW8QgLngD8NQfBqLsj-NFm5p0-8tJsXqyFnYb-2TFmVKZ2ypYlHizvDoMk42qy2N4KFnuNKBuN5YCiNmc3NSU7udYQHDBM8F4ShX2Hl0gGNqqcvBpR97PZ7SE9yI6cEhYY-fkMCF-Xt1gOjjLD=w971-h546-no"
		</p>
		+ A brief description of all the classes:
			+ **Managers:** They simplify or automate an operation
			+ **Utilities:** They provide some useful methods
			+ **Activities:** The provide the UI of the application
		+ A description of all the classes can also be found at the beginning of the corresponding file
			|Class			 			|Package	|Class Type		|Description|
			|---------------------------|-----------|---------------|-----------|
			|OfflineMode	 			|Activities	|Activity		|Provides the full application usage when the device **_is not_** connected to the MQTT broker
			|OnlineMode		 			|Activities |Activity		|Provides the full application usage when the device **_is_** connected to the MQTT broker
			|SettingsActivity			|Activities |Activity		|Allows the user to change the application's settings
			|SplashScreen				|Activities |Activity		|Makes the necessary checks about the availability of an internet connection or a GPS signal and starts an
			activity accordingly
			|GpsManager					|GPS		|Manager		|Utilises Google Play Services to automate the acquisition of the user's current location.
			|OnlineAvailabilityChecker	|GPS		|Utility		|Provides functions that check the availability of internet/GPS and the user permissions
			|AlertBuilder				|HelpClasses|Utility		|Easy construction of alert dialogs
			|Constants					|HelpClasses|Utility		|Stores all of the application's constants
			|MyMediaPlayer				|HelpClasses|Utility		|Allows us to easily play sound files (offline_sound_warning.wav - online_sound_danger.wav - online_sound_warning.wav)
			|MqttManager				|MQTT		|Manager		|Handles any and all of the MQTT communications
			|MySensorManager			|sensors	|Manager		|Handles all of the sensor value acquisition
 		+ A brief description of the interfaces:
 	
 			|Interface				|Package		|Description|
			|----------------------	|---------------|-----------|
			|MqttConnectionCallback	|MQTT			|Provides a way to notify an activity about any messages the desktop application publishes
			|SensorCallback			|sensors		|Provides a way for a MySensorManager object to periodically send sensor value data to the caller activity

		+ A brief description of every class's methods:
			|Class Name				|Method							|Description|
			|-----------------------|-------------------------------|-----------|
			|OfflineMode			|@Over onCreate					|Initialises the activity and checks (and notifies the user) whether the application was forced into offline mode (i.e. There is no internet connection)
			|						|initialise						|Called by onCreate. Makes all of the necessary View initialisations
			|						|@Over onSensorValuesChanged 	|Overrides SensorCallback's onSensorValuesChanged. Is called by MySensorManager and sounds a warning according to calculations. (see [Value checking](#value_check))
			|						|@Over onBackPressed			|Asks the user and exits the application 
			|						|@Over onRequestPermissionResult|Checks whether the user granted the ACCESS_FINE_LOCATION permission
			|						|@Over notifyCaller				|Overrides MqttConnectionCallback's notifyCaller. Used to check whether the application can go into OnlineMode
			|						|stop							|Stops all the threads and warnings
			|						|exit							|Calls OfflineMode.stop() and exits the application
			|						|goOnline						|Calls OfflineMode.stop(), finishes the activity and starts the OnlineMode activity
			|OnlineMode				|@Over onCreate					|Calls OnlineMode.initialise()
			|						|@Over onStart					|Gets the saved UUID and tries to connect to the MQTT broker
			|						|initialise						|Called by onCreate(). Initialises all View objects
			|						|@Over onSensorValuesChanged	|Overrides SensorCallback's onSensorValuesChanged. Is called by MySensorManager, acquires the user's current location and publishes a message to the MQTT Client ([See message formats](#message_format)) 
			|						|clientFormat					|Formats the given parameters into a desktop client appropriate message
			|						|notifyCaller					|Overrides MqttConnectionCallback's notifyCaller. If the desktop client did not acknowledge us, we go into OfflineMode
			|						|\*sound* methods				|Name is self-explanatory
			|						|@Over onBackPressed			|Prompts the user for confirmation and closes the application upon acceptance
			|						|stop							|Disconnects from the MQTT broker and shuts down all of the created threads
			|						|exit							|Calls OnlineMode.stop() and closes the applications
			|						|goOffline						|Calls OnlineMode.stop() and takes the application into OfflineMode
			|SettingsActivity		|@Over onCreate					|Calls initialise and createListeners
			|						|initialise						|Initialises all of the View objects
			|						|createListeners				|Attaches listeners to some View objects
			|SplashScreen			|@Over onCreate					|Checks whether the user can go into OnlineMode **and** has chosen to do so. Otherwise, takes the user into OfflineMode
			|GpsManager				|start							|Makes the necessary initialisations to connect to the Google Location Services
			|						|@Over onLocationChanged		|Updates the latitude/longitude member variables with the current location
			|						|getLatitude					|Returns the user's current latitude or the last known latitude if the current one is unavailable
			|						|getLongitude					|Returns the user's current longitude or the last known longitude if the current one is unavailable
			|OnlineAvailabilityChecker|All Methods					|All of the methods' names are self-explanatory
			|AlertBuilder			|**Note 1**						|The class offers multiple constructors. We use them according to the buttons we want the Alert Dialog to have
			|MyMediaPlayer			|**Note 1**						|All methods basically automate the MediaPlayer's functionality. Used for easy sound file looping
			|MqttManager			|connect						|Returns true if the connection to the MQTT was successful or false if it was not. It also creates a thread that notifies the caller activity with the desktop client's response (if any)
			|						|finaliseConnection				|Subscribes to all the topics to which the client must be subscibed
			|						|@Over messageArrived			|Handles all of the incoming messages from the desktop client and notifies the caller activity (OnlineMode) of it
			|						|disconnect 					|Disconnects from the MQTT broker
			|MySensorManager		|@Over run						|Starts the MySensorManager thread with the given interval (provided by the acknowledgment message from the desktop client)
			|						|runAtInterval					|Notifies the caller activity every \<interval\> milliseconds with the acquired sensor values
			|						|unregisterListeners			|Self-explanatory
			|						|@Over onSensorChanged			|Updates the lightVal and proxVal member variables with the new values
	
    + **Screenshots**  
        + The application starts in the Main screen which contains 2 tabs:
            + The Search Tab:
                <p align="center">
                    <img src="https://lh3.googleusercontent.com/gpCrFCW23B5RzEcTW1zD45VoHURVFCwqtrsNbA7_wW5VsdDS6sPlrS_XMAHlBJNY5s6XTqJ0dEUEtPV0cATqYdO7NP_GKhLT1JP1iCvEKSRc0X8uDsZC0Kermb99TaXfkfn8-nN6EAubCAeV61h6jQoUrvl-WfMVUWgWkujqDezkx4-KTd5F10_tSvdCKZVH0OmFNaKc_bW0QlbG1lBTmsK1dgxUV-JfIHfa0U5_y3S8W7S0DmM6TSHoZCaDL96p33-bFEjfC9vcM_6fUjwt2rl_zLVg9PyzVMOkDR378UxMxjQ_zf-LkydLIZDTnb5Rqabkff1i5OAnJUzlT7oJeWFOAmrW_5KfXp7kb4Sy-fB9tbhuyYxVlANhiF4O86mtkZ2grBhUtRYzMI9Nyl4W9-YhXmdmEJ0ylaDAX6vxAv0_TtDHkKugKFWMUwEBnU_oNNNb2RVLrwXg1Gpowx4QVSVmCx2ZxEE4a-DDK1SwwZxrjWKJJ8DNWtz1RtnEm-qE7AqYKEZe_l8Ube1gNXPHljSmH4H0TZcxqc8Wgnut5_i3Bgj5bEQ5cFc1Zi1oKoeKzgTDfdxlCx_KaBrcK3iZwcy4Ki4rGAP69X_V2ICSQBDrLcJZfHH4=w597-h489-no" />
                </p>
                Here, the user can query the Data base by
                filling in the given filters. They can search 
                by any and all fields, except for Date and Time together.
                
                + An example:  
                    + We search for incidents that happened 
                        + To users with a UUID that contains the characters:
                        "f28"
                        + On the 7th of January, 2017
                        
                        <p align="center">
                        	<img src="https://lh3.googleusercontent.com/A-053ZZvedUWmwCMGW8_qv2VBEmcsC7JoFW0K871pbOPQGsMQSbPYS3AdX7VL_fros75pT0kmz7TuSecgFLcZ3K6xF_x1sTf7B-wp36f4y3GLj0YklWtFZU7hfYgBDIZX1xzwfC6_wL8GPbgj1TUOfdFKw_EenqWt5y5q1MTNVkP_Tb2yJQjGBL2rKtdBGKE-r0o4Tb30Zdhi27PH_Gi0mfY6BYK280p-5dwwY0bFR-uX2F1OMQIQ3fjliI4DxStGesLz-8p6pk-avtqMzUFxHZmesVDVygWbONzQCNChqlUU77Ufl6rqyNdgCT5LNouZPLZ4BhyVTddAEAnvFGlnByTXXCIzxRvxOMCz523CVS8vAExY2Vg7EfNg604XD0yu_7WzoBYxhe-s8ke5M625AvOLPUINmSED6obsOz8rFfM2DEuflYuPcXbbR8kCW-PfqDLXnP0fQMc6I1yCtLc5r_7u_4YNe9a19rPBew34a9FMh1gZTR8AEloRFzlrtjxMbh9wEWDQ2WUlaLOnalXa3vzZx8LSul3izqzkLFfShMlNgXZfm2Z07ni19-IqRDWQJbR7x3Z5kauYeynrMcCiVlVQn-5_iGnBM-w3mDBWn3xFKPrzrKc=w597-h487-no" />
                        </p>
                        + The results are:
                        
                        <p align="center">
                        	<img src="https://lh3.googleusercontent.com/ydnmG4LYeLqGYQ4GAfeEQ7J3-GBq6uel51rC7Jm6yzzl2RyaDDHBrkUI4PZAPMpyBNpo8kSBLvqtPoUT6yhnr-ebOFWdZbvY54yUMm7UVHDsp-aqfyJpOe6vRtIVBe8DqVnbZHu2eRDsRWLSPG_jl91yKH-VHJ0RdlfZeD66Zr5RuN32AsLY0yKrxvi7GBL23pB5cv1wBFwPF1qd_NZgMODDhbUnO4hWLm_o2eSYNe7S7-gSyEvQqchPzw3bVpyxbb-ESjGQ5G0hXdVTETiAsGlmbaSz8Bah0VULXl6_O1SJwdrlNOn7bm6PWvpAiLdWJR7MCspWaJPHO-SlCDFiyogJthujoaL0HqbVAup7ZgQKuwScrdg_Dvl5G5xOaBoDPaH_L8j6919UzwyPsbqijM71IjI_lyFlbcyy7i0jqu-OYL_iKRZwbyypARpcqNgf6FoxSvLBj3O9tnfq3po1VUcmMxUiw6FU64CTcfCTANPjY0KXH__oVf6OaGhJ6XW0FVs-a5n8Mz-F1QJC1Ve2983IuWGOhNqRygfPKrb5RMrpbwylTLLd4sYciR2qVLmOTSTIHeR398YSZp3AmWRrpSmW_rTHBggyOlV__hNQYlPEseTevdhv=w737-h574-no" />
                        </p>
                        
                        <p align="center">
                        	<img src="" />
                        </p>
                        
            + The Settings Tab:
                <p align="center">
                	<img src="https://lh3.googleusercontent.com/OdcrOkLP-f5WpzVh9oPdLBcPJ5zVg6BQQMiggBYQoYKgAXgbaJV3lpeh8ZLhrMRm9HHkvSZFM2B_Isz4k3IPOEaiwLDzuc2ZKXtEZK2RqI3B1r1NEnHqbCEyQ3p3JEOr2pFxbV9NVgilELEGLVzQ0BG-eue7vQVj_ZAOkH5l5F6kqBQPy6tLicoKG7X7JvHdCSq7jJjngAZcV79bjV0yizdKUVP2cuY8d4seQtz3wMlAp4q6M0U--WCRQeGjIXBrKPdnlEU4csu8ZtK4pmsedimcqQ9UljhUM_Dp3y8dWXLsZ0TDzAhZzB6Ot2NgUkd0JFuvaPa1KzlwXpaKK6tMgbEexniiL3tLxV61utJYAxF5_cwScK4o6vSmNPrTEqdyMyjmTDgO7J6_SfKOZgN9l6VwoIrIPorBQaUy6vZyLJVVzr0-uBQCPGoPUTvarsZ2T21nci4c09lfFLiIRLAav0bKSlXGDNgqp0FoYr4qJc2LjUNy8tL-UIYKivX8I12aYtLE6GzwQ-ufds9O5bcyo7ONm8xaSS3ALXN2dhK0sBWgCf2hyU74cW7QvS08IFDLEII90nhoMhZ9O5z69yW2Z-iz2R2JTt7tB-nUCbxLQG2_b2nwZEzK=w598-h486-no" />
                </p>
                
                + On the upper right corner, we can see the
                currently chosen profile (Default) and
                on the rest of the window, the user's current settings
               
               + We can also create a new profile:
                <p align="center">
                	<img src="https://lh3.googleusercontent.com/Ew-WyQe2zYDScc2vShZW7B1XLAhcrZY3byV5NGosIaLWvVtjlbfKJO184s5TYhcFbbIwgGW57R0LMYX_5J4iPAL2Si16YU6d6V88hzyHFPBDxQ_vkz-eIHNdOqdLWn_d4TWMA8YO82hMNoolWZuKH77j2Ol5YQE9LcrvQ321LLBWldCVGofdvSowUeKQfnUD3f0efjAGMYTP6mF_Au1SPjd_A9kMG3FrgGKqd_p0C7Ss5YM_IDJ0k1ovyenchLfSd1Dz29ZeKaFGHHuGcyklCpZSnsVxMzY_6UpNMgZpQ-83uodLokTc5eyB-cwO16AAHaySc84qApPGByHrJMYbomhugwe0n3G2q-R66GLgR4e658PIhh-gWqHN3ylaLnnhpoAxXxEbq7KwOvr9N4h66I0PhxmfsliKQmj_Dqqpr2dNtClIB0GKtmimRjzyNt-_EYSzCDrU_LsPx5MEBSrvAb8XTsOjDDQils2qFpIavJdJ5aFGAB8a9BzFX3M3c4e3rnVh6KK72cryoakaBuv5OO7Q4FSSy0LeQDlvugJzp5tZJ3i1TA8qH0c85iugfwWLr09kik2BwRdtBS2esekvriig58UEnNBfzzTTjo5EWxmKQt_WFI_q=w600-h486-no" />
                </p>
                
+ This is the end of the desktop application's README file.
For information regarding the android client, please 
see the relative repository [here](https://anapgit.scanlab.gr/1200058-1200066/phase2-Android)