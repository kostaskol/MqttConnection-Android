<h1> Software development for network systems - Android client </h1>
Index:
[Topics](#topics)
[Message Format](#message_format)
[Classes](#classes)
[Class Methods](#methods)
[Installation](#install)
[Algorithm](#algorithm)

+ <h2> <a name="topics">Topics</a> </h2>
<table>
<tr>
	<td>MQTT Topics</td>
	<td>Subtopics</td>
	<td>Usage</td>
</tr>
<tr>
	<td>log</td>
	<td>-None-</td>
	<td>Operation messages from the desktop client are sent here for better readability</td>
</tr>
<tr>
	<td>Connections</td>
	<td>-None-</td>
	<td>Everything that has to do with a connection.</td>
</tr>
<tr>
	<td>connections/newConnections</td>
	<td>-None-</td>
	<td>When a new android client connects, they notify the desktop client by publishing a message containing their UUID to this topic</td>
</tr>
<tr>
	<td>connections/connected</td>
	<td>-None</td>
	<td>Already connected android clients public to this + their UUID topic</td>
</tr>
<tr>
	<td>connections/connected/<Client's UUID\><td>
	<td>-None-</td>
	<td>Already connected android clients public to this topic</td>
</tr>
<tr>
	<td></td>
	<td>warning</td>
	<td>The desktop client published warning messages to this topics</td>
</tr>
<tr>
	<td></td>
	<td>danger</td>
	<td>The desktop client published the danger messages to this topic</td>
</tr>
<tr>
	<td></td>
	<td>stopSounds</td>
	<td>The desktop client published the stop all sounds messages to this topic</td>
</tr>
<tr>
	<td></td>
	<td>acknowledged</td>
	<td>The client published to this subtopic of a specified UUID to inform the android client that the desktop client is online</td>
</tr>
<tr>
	<td>connections/requestAck</td>
	<td>-None</td>
	<td>Before an android client goes into Online Mode, they request an acknowledgement message from the desktop client (to ensure that it is running)</td>
</tr>
<tr>
	<td>mainClient/disconnected</td>
	<td>-None-</td>
	<td>If the desktop application disconnects ungracefully (it always does), all of the connected clients are notified and go into Offline Mode</td>
</tr>
</table>

+ <h2><a name="message_format">__Message format__</a> </h2>

<table>
<tr>
<td>Topic</td>
<td>Subtopics</td>
<td>Message format</td>
</tr><tr>
<td>log</td>
<td>-None-</td>
<td>\<"Log message type" - "message"\></td>
</tr><tr>
<td>connections/newConnections</td>
<td>-None-</td>
<td>\<"Client's UUID"\></td>
</tr><tr>
<td>connections/connected/\<Client's UUID></td>
<td>-None-</td>
<td>\<"Client UUID"/"latitude"/"longitude"/"light sensor value"/"proximity sensor value"></td>
</tr><tr>
<td></td>
<td>warning</td>
<td>"warning"</td>
</tr><tr>
<td></td>
<td>danger</td>
<td>"danger"</td>
</tr><tr>
<td></td>
<td>stopSounds</td>
<td>"stop warning"</td>
</tr><tr>
<td></td>
<td>acknowledged</td>
<td>\<The frequency at which the android client should contact the desktop client\></td>
</tr><tr>
<td>connections/requestAck</td>
<td>-None-</td>
<td>\<"Client's UUID"\></td>
</tr><tr>
<td>mainClient/disconnected</td>
<td>-None-</td>
<td>"disconnecting"</td>
</tr></table>

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

This is the end of the android application's README file. For information regarding the desktop client, please see the relative repository [here](https://github.com/kostaskol/MSDN-Desktop)
