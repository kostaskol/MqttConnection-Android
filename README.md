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
		
			|Class Name		 			|Package	|Class Type		|Description|
			|---------------------------|-----------|---------------|-----------|
			|OfflineMode	 			|Activities	|Activity		|Provides the full application usage when the device **_is not_** connected to the MQTT broker
			|OnlineMode		 			|Activities |Activity		|Provides the full application usage when the device **_is_** connected to the MQTT broker
			|SettingsActivity			|Activities |Activity		|Allows the user to change the application's settings
			|SplashScreen				|Activities |Activity		|Makes the necessary checks about the availability of an internet connection or a GPS signal and starts an activity accordingly
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

		+ <a name="methods">A brief description of every class's methods:</a>
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
		
	 		