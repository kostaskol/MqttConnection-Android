+ <h1> Project 2016 - 2017 - Phase 2 - Android client </h1>  
__________________________________________________________________________________
	+  <h2> Index: </h2>
		+  <h3> [Topics](#topics) </h3>
		    +  <h3> [Message Format](#message_format) </h3>
		+  <h3> [Classes](#classes) </h3>
		    +  <h3> [Class Methods](#methods) </h3>
		+  <h3> [Usage Screenshots](#ss) </h3>
		+  <h3> [Installation](#install) </h3>
		+  <h3> [Algorithm](#algorithm) </h3>
	

__________________________________________________________________________________

+ <h2> <a name="topics">Topics</a> </h2>

	|Topic                                          |Subtopics                          |Usage                              |
    |:----------------------------------------------|:---------------------------------:|:----------------------------------|
    |log                                            |-None-                             |Operation messages from the desktop client are sent here for better readability
    |connections                                    |-None-                             |Everything that has to do with a connection. 
    |connections/newConnections                     |-None-                             |When a new android client connects, they notify the desktop client by publishing a message containing their UUID to this topic                |
    |connections/connected/                         |-None-                             |Already connected android clients publish to this + their UUID topic|
    |connections/connected/\<Client's UUID          |-None-                             |Already connected android clients publish to this topic.
    |                                               |warning                            |The desktop client publishes the warning messages to this topic                          |
    |                                               |danger                             |The desktop client publishes the danger messages to this topic                           |
    |                                               |stopSounds                         |The desktop client publishes the stop all sounds messages to this topic                       |
    |                                               |acknowledged                       |The client publishes to this topic of a specific UUID to inform the android client that the desktop client is online|
    |connections/requestAck                         |-None-                             |Before an android client goes into Online Mode, they request and acknowledgement message from the desktop client (ensuring that it is running)                |
    |mainClient/disconnected                        |-None-                             |If the desktop application disconnects ungracefully (it always does), all of the connected clients are notified and go into Offline Mode                      |
    <p align="center">
        <img src="https://lh3.googleusercontent.com/EMTDe2A2bCl9MuYJKZhCLX_tX7UdKtdf8fYdxdcuCFidDoLNWDPSGSRl4oVtERlpl-79DWBNPMcfacocgttfsDVU6azZSdBP_WkuWFD2jJ2F8gW6waPMZqLLN0g3wm8OvVqph2EdKa19BJYjsqYHRyAvq9kLpfUcwLtEaH2fTq80pnvMS5q1wzfTGcRKl0gtI-GgZoyINp9rIRpnKFovbLAk6h2PP1O4wwLJT99YfX0txHF1OFchctcVTubwk4zjAbNMjcq3cL0Ad6cVlIBt_71riQmHjeu2gCpdY2uXs68ZlwlUXNOzh9X7C2BwyIrDisLDZmPhtLoNUSl5eCoe_j5sL4CZAVWomGBtwjofZsbiA3fe1D4qyizQ1eRNseiFzk5f9Rcaoa3z4FiseBP9KKZbIyibwM7xOWOv-6B3U-l-9cg8B4FxBR1hLd7QfB9xZuQ80dU0NywqltCgg1tsRDz2-pbNJ1aL095OfSZeZwjX290c1as0ZkAkJZhaa1_eKt1ZajAqfI2CZdJIfhzdQIbiKmLn-NgJ6fQ4Mh6Ve1OXOlOXtiuRcmVbKGWFeoQXeBHu_o1KupHzSX5Xff5GLd_zgDs7cP_fIN0VhO6HFdMsm4GmYExz=w1063-h501-no"/>
    </P>
    + <h2><a name="message_format">**Message format:**</a> </h2>
    
        |Topic                                          |Subtopics                          |Message format                     |
        |:----------------------------------------------|:---------------------------------:|:----------------------------------|
        |log                                            |-None-                             |\<"Log message type" - "message"\> |
        |connections/newConnections                     |-None-                             |\<"Client's UUID"\>                |
        |connections/connected/\<Client's UUID>         |-None-                             |\<"Client UUID"/"latitude"/"longitude"/"light sensor value"/"proximity sensor value">|
        |                                               |warning                            |"warning"                          |
        |                                               |danger                             |"danger"                           |
        |                                               |stopSounds                         |"stop warning"                     |
        |                                               |acknowledged                       |\<The frequency at which the android client should contact the desktop client\>|
        |connections/requestAck                         |-None-                             |\<"Client's UUID"\>                |
        |mainClient/disconnected                        |-None-                             |"disconnecting"                    |

	+ <h2> <a name="classes"> __Classes__ :</a> </h2>
		<p align="center">
			<img src="https://lh3.googleusercontent.com/b-KJz-5ENFFol01knQUWUfueHWCdnVFrG1L2Lf04NZ_LDoNiPornBzh_a67oTlsxvOZzuJqT8FNfzA6QdKGz7KhDvUOW2nGfvYk_MK_LSRj8fJ_fn5pBcJ1b08_rBEI_eFcmgLNsNN7gPaw7Ekak_uate2O83RJ8Cltq-cZHU8JpYL8_06ettTzBiflPgywgPBKB9hVIe3rSCBNk_zfz18FIhIH9kcO6pVjp0JdbrP2dDePX_jGaivEisTGKPtF6NqKYXRO4eneHBcx3t1N5x57fFiXGJw8VyMRcSnHGoNekPWIBUEnNYNP-UlwdrleGvd9sQnfVWkejpKlF1P-oFGH5CY4H91lL9j3ZdUIbNIz8cw4R0gUex2M0YBnQvcX33SSumL6VX1fcSSb9ZKuXxqTLwD47FJnQu4FvNaQ45kM9z6rmrItfliPAqF2IMluVrmb_8dvrBjtdwOqPAFoI17p3JUL0nGWhrcHW8QgLngD8NQfBqLsj-NFm5p0-8tJsXqyFnYb-2TFmVKZ2ypYlHizvDoMk42qy2N4KFnuNKBuN5YCiNmc3NSU7udYQHDBM8F4ShX2Hl0gGNqqcvBpR97PZ7SE9yI6cEhYY-fkMCF-Xt1gOjjLD=w971-h546-no"
		</p>
		+ A brief description of all the classes:
			+ **Managers:** They simplify or automate an operation
			+ **Utilities:** They provide some useful methods
			+ **Activities:** The provide the UI of the application
		+ A description of all the classes can also be found at the beginning of the corresponding file:  
		
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

		+ <h2> <a name="methods">A brief description of every class's methods:</a> <h2>
		
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
	
        + <h2> <a name="ss"> **Screenshots** </a> </h2>
            + The first time we open the application 
            we are greeted by the splash screen an access location permission
            request.
            <p align="center">
            	<img src="https://lh3.googleusercontent.com/oaKOb3AR4Ut4ka754UNG8XijK63vF-lRNFwB-6sJ2CLu_JNJLqvTGx5eyrINLufe0R1NBNmODW2uQOQ8i7fP0bfio_OB8KJ6eREPQJLPXgiXfcTlWUvzI2zm5bL26hQ5D627Agcazegdc9kRNVSX8A7676Wfj0M1O630xYpA3Df8dGApMqtQdijvESRfhvBQx3cypR3FlZlj3Zu-cvlSvYAhxtjphYlL5dvAt3gsplg5kebCpPqry0k3aYuhqFnYgbzJ7FUX1KiazbW4yt3C77Mo5P6xCk0seDpiILwtEBuiXosFkPQdTj4dBbSDrOEXO0I2Hz_wJNtlUaQPiOVaBiPVNxRx8KlcbAD9eJ7tREl4En803eYi5OQn6tkjEag9Y2N-eUd-Hw8xffn56aUFycc5plIsmREXdtiyOgxbl-wn4Rlma5G2wW6inpjDEH5x8VCy1pUS3d1Qht77NbiMEjcPEd1ARvc1_vu4DDJxoVM25hcaks8fzP1736Ak925cTbDgCvDgrYu_rX8HIBWwH8bjbkL4imIe3AOsPM2J3K5uYlUKHKFTGCsDvoL8dGov4zrvSgSHhKWOQocgfvLoanyr2gHVwSdRoJPld3YiwxYQry8KrLhd=w161-h286-no" />
            </p>
            
            + After granting (or denying) the request, we are asked
             about the mode into which we want the application to go (this mode
             is henceforth set as the default (until the user changes it from the settings))
             
             <p align="center">
             	<img src="https://lh3.googleusercontent.com/JdpRQY9SAcBluRdvsLBoBTdZwiDRAxqOX2D-yrlnKwIF6eSAt7EclSlvb5AJS-fEeWdGa_3gLRHXA2YTULUbRX0GyYfkaL28QE52u2aq3VZUd_LS38e729MAhA3M0VqyLR156zSajvHJUpSWtbz9J1EItOt628ruzhB9lAylzDYtuXWaCBD0deiwqv0S5K5scV9YMSTUp6vm0aIXcpTtjhP-SfSLbT2umCong8JbTa8Zg_o_ZDfGskRFtPfwr17caKDPIQXaq2YSj13hNhcjBKHm46mpmmlvjrllkeLIEP8N8OhCiSnXTRon0TY4M3oO2cbVpPSiEMfiN6oZ5-7TWDao4WHWtc6y6IPM2G_TUSDHVOJokc6RUrr7ybRZMEuYjC5veg2n7UHEZ_6DFXWt3gJIjqQHxixvFFmQ4fYIXh88DHNE7DBxo3Efc_vzcFOBoorBi__9wp7pMc7q0VwJ3J-yRFfmMacBbsq_An92YYHz7DPnvomafMEXdBYK3VjNSjoZfzmb_oSkBA4pgjstbT5tLseWMqWve2fTAe3ryjTGoA0V3TYs9cea8tWehYss0nAtOmtRZHqEzmpGGYxMpkj1QR-Kl9kodYvcc87UXWDyS8_KJ-7L=w371-h659-no" />
             </p>
            + **Online Mode:** 
                + Upon entering Online Mode, the user will see
                the following screen.
                <p align="center">
                	<img src="https://lh3.googleusercontent.com/-CbiQ4CXkC_mlFB_8ClJVpT1EITv9dG1N6wh9idA_-GHEmZlu-7DBve_qfv-qP0QVKAVo9X9_caHTmDzfSiTTbJhvfelhSY1ldLLW8-Fq7AMvJSxtq0PIHA_B2VAKwnPuZz_lY_eVtOl8BguJVDaSFr_pY_3BfmuhPtw_kfKhdhsKGFcgiezwDTxHp2cqmxZRavDI6-SfHdEGvS8n5clR1LDQkGDkdPT_rZ1YgLpipQ7MO9Vex_ukn0d2IU9HxrezdLaVbp-1PTI1f8Aj-Mcj-aaUTH2hvUDhZ8Tz_jWsJPC-rxX2FmXNmXjBbmVayvr799jvcSWO7iB3FEw0XAXj9ESH8zPddZtxCbBRWXjOjje4DC3ahkoERv21xpAnea3P5qcS9k262PwhGoX8tH4mdOe_ZoqDzaIvvH5Q31BduA7gQmxhtDcN3h5M4fMGwnSne1m62wVPupbTo6NArjV4jI17dty8SUiO_2-ArxSgUQUylkrDyzQuapTKPrYyEiKtXOdmzuAy0Q7Xjdn7UA2y6K6zifRaMTI3dPP1KYCaYYfFH23Gybe_q96JUJoMy50hTmVvAqXzjx2tucX2SVi5hw01ol7QMsckqxOyo4IfzcHEAAAVFxh=w371-h659-no" />
                </p>
                + If a connection to the MQTT broker can be made
                and the desktop client acknowledges us,
                we remain in Online Mode. Otherwise, we go into
                Offline Mode: 
                <p align="center">
                    <img src="https://lh3.googleusercontent.com/9qwihb9FcHzoCw5ObE7-xb4tXFMMx7A0kExAQFqr4XX3vlQNfOY3SRqhYbgOn6mMjB_0y9PrzoEz_KmY64uh26e8Ea340BlfYgtBLvPnc8nFsFycIkXRE03xm7xEcZlFFJXLlxD4AzatjasWYNOrVVjzzJvxgd_REmW_Uhj0f3Dd923i9iBrZJWaJ8fNHqesfAUgf-giavxhmUk50_BvlUcuzQzq_j9BOS_Y11CezZZCsboBiMCGgt9dVP7lGd10YWY2eoTcvHYCZZfIerLgdjEt3TSQ50V0HosT8XSuUuHRMQU0r6NJRevmYjPJTXg6S-PPJjKXwGZ6KsGtW1N93QNlNk-nn4rN4ZUqMPv-7oIvHU7UXCNrL5fvlcrDHRUuKk4wUK0a13O3K5jd8bUwsQnRKFqc3g-E98vyLIlg8ROQzhw7yb3-tYezlFHrG4-bQmnxbzTPM1SF8d8zB9J0C1OsEA5g9imC3L4bOP3uWL0Su7kL7yDBkMyWoJznv9-Rdt3TaQO6XXfZgAwXZzTCxa5aGpdXbZzcJD0IfDEQZPJfpTMcwMiG4cEdn2JdVLkYIx10FFdNwVVUNIHJscpLGJy0vX4XyqU2eUbVCtZVM5Q4ynocffPO=w371-h659-no" />
                </p>
            + **Offline Mode:**
                +Upon entering Offline Mode, the user will see
                the following screen
                <p align="center">
                	<img src="https://lh3.googleusercontent.com/dKqoQU0TSgu8SWuCEG3kvoSPE8P8lAHk9rSvRssrVdvfH2Czp_IsoNLkyXszH6W1e8dMcbPnrfW3DQJc6CBBPb-_iW9hsl-ssr9utAAvtcJHQiAT3T6MxI8bE1QRgwvHkiZBYa2YMxxweNe-6deqqKQAlMk4RP6Aa-EY4acrnc2RlXuYhyFf8QXQoFM49BNHnr1lXRejL1RfWhGOUf9yIAhsis4kPCa-YWRjpA_iauTKCtgSH5KOZ_n4u4mJncdD_KV0Uags6SGPVqkEqOYJAPpUuh4o3HfqF6l5M_AEtanyYPobHfgeefCR3kpperPx_WkTrAGY2OMXwI-xMgphdvdOaVvePrSLSt9g-9YbhV77Dh6JYIA2Zv4dn0MBw8rMv8VW5pa_uKCLYzy-L6ewfNZp6Y5O7fQO8K4HbCH04OI_x4ajlyUth50loZQsIDgK55lJrFqcTGGOn1xzTwNwf1ZBfVIvKAYaUHNk5xAitZMdjFYn06KVTc4Htaqr34z4ikWO2KIdsj3Tan6FBmh42TPXWbpK1GqQvIhvHy6DyfMtxf1kScA9EOb22lKyhlkOco374gkdvh3syNoBlN83q7fnDRV_dhZFwQSZeTyZz6-XZTXwZaMM=w371-h659-no" />
                </p>
                + If, at any point, the user chooses to change the settings or
                close the application, 
                they can do so by using the navigation bar
                <p align="center">
                	<img src="https://lh3.googleusercontent.com/jR6uWHz85nPXznv17JTBFoCrYEyHMBusR3Vfz7XaYwDIeA2HKKdkbTv0fNHaIo_7RlxB3YtBo16jGBBZEjMK4s1t8NLzEWATVqLwyfCbSFdzUAVM_SQdbtvVQbRfFe3Ri3mpArOILuHZh7vpqRSEJSvbrLCtRNuxP3yXibQXNcZwxBx2AYeVKh04v0DS5F_wFWtgWR20KYuFgoEGZx5MQB6k848QvrF5ZZYcU3BClzX2vDpRthgo8ZW5yoOfuPzlHfD0C_W7sFuQ3OQ8gCrx8TDPYrxGFctemcpZXYS-nbp93Rb1zUozTHNPi-PYaKIXKrfUYmexpRG0WiJhP7Ry1NMFWoF4YglfC5sGiBjHEJBOtRxtaAMFNzE7fXKcLFpbhC9rY__q_Zatd0p2LbPHzO3dRGQYEGkAZRMm-EfZxZiJSlTVruVs4-JmFWUadOYxTfVeP82y8xz0S5fPDjFkm7Pp851PRYZSsTjw7qDb9YJqDLeXVrEaclnnnmaLF-ti1OAg-TjAXWt1NFgBNuOFaEDHfquRUTZQbgkNQAqF7gdH3XqiH67_6Zk6-Ui4PkuGlKyGCWmJopOuRwG68Znl6DYhh_kJ43-v1poEP-guVhYhUsMSfcX1=w371-h659-no" />
                </p>
                + When entering the settings activity, the user will
                see the following screen
                <p align="center">
                	<img src="https://lh3.googleusercontent.com/PXoBAUha4cQKVHJ-mHCzr_MZbQMCYvGkFyS59WtD0myDaS_uHTEG5WxOg95KCiUTjVnnDBBpAkChRlZ3k2AO49Z9Q-lfwsLHULTlAWbFYHR4XLbVi_QQYTXWJgg-u0LNdmUqYsGycjBedjnPmwNVuVejR5_JtUxiHey_nu1Cs_n0WMuu-NH54xz-eFIdlGx-YiYqPOefet1M5Das-359zx_FJuADh3Gy0HtF428Q-KLmM4C8tyh0pfhXSX_LPZpOM-Sj7-nLqC2jMkmwj5ffzCD4PRk3dvGZYiI6o7krQyYK4mGylFqGl25o8bSUzcT4kZw0JzgqIr7doYb5k0jKzeoDwbnA24L2AaCq8CJlAOeQJNpO6TGyGIGW5JBVoe3z6AgKa5ZtxOExJrIs-m2ocXGfPjKZ4VFDJx6Q1LPrYFD5leeQYZShBq-B7D0_gBnID37ita4xIP4OOD45Fm-4zJWC9UOaihrsNYK_D-OG0NphDDCx5qFJTXpUQJALcVMhpZK_XOa6Rt8oD0AHyg17aSSlTF0bAFPSx1eLUxYjTv4vMydwVtC6YhiVvquWc97AmqLbtTleQEmN9MCGDr9DdoLzpLVm4GSb-5T0B47gpS_kMvj0H9mU=w371-h659-no" />
                </p>
                + Note: Offline mode's settings have no effect when the application 
                is in Online Mode
        
    + <h2> <a name="install">Installation</a> </h2>
        1. Copy the following file:  
        "build/outputs/apk/app-debug.apk" to your android device.
        2. In your android device:  
        Go to Settings -> Security -> Allow installation of apps from unknown sources
        3. Install the application using the app-debug.apk file
    
    + <h2><a name="algorithm">**Algorithm**</a></h2>
        1. A threshold and a current lighting value are supplied to the algorithm.
        2. For the first set amount of times we receive a lighting value, we add it to a sum and do nothing else.
        3. When we have enough values, we calculate the user's current environment average
        4. We calculate the actual threshold ((100 - threshold) / 100)
        5. If the lighting value is greater than the user's current average + the actual threshold, we recalculate the average
        6. If the lighting value is less than the user's current average - the actual threshold, we warn them about a possibility of danger
        7. If the lighting value is within these two limits, the user is persumed in a safe state

This is the end of the android application's README file. For information regarding the desktop client, please see the relative repository [here](https://anapgit.scanlab.gr/1200058-1200066/phase2-Desktop)