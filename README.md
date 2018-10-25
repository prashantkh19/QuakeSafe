## Problem Statement

Effects on humans. Earthquakes are the most expensive natural disasters humans face. A strong earthquake in the wrong place at the wrong time can cause great financial damage and cost tens of thousands of human lives. People panic during such tragedies and randomly follow the mob which is sometimes deadly as they may not be heading in the right direction and are always in need of an alternative source to confirm the safest nearby location. Also, people try to know the well-being of their dear ones through whatever platform is available to them, but the use of such platforms by others about whom we want to know is not guaranteed during such a situation.

## Prototype
An android application that will help a user in identifying the nearest and the safest place where he should move in order to stay safe from any casualty during an earthquake and will help him to connect and know the current safety status of his friends and family members. 


## Working
A user just has to open the application and he will be able to find the nearest safe spot during an earthquake. 
Users can save the contact of their loved ones and in case of any mishappening, they will have the facility of marking themselves safe so that he will be able to know their safety status during an earthquake.
Also, the user will have the facility to give special status to some of his connections and he would be notified automatically as soon as those connections have marked themselves safe. (An SMS will be received in case of offline mode)
In the event of an earthquake or any other natural disaster, a great concern is network fluctuations and connectivity problems. Our application will consider such problems and will try to provide a best possible solution on the basis of available resources (such as a local connection of devices in proximity could contribute to the offline algorithm, eg - getting GPS data.) 
#### Offline capabilities
The app would dynamically update the nearest and the safest location in the background and in case of any network failure, the latest calculated result will still be available for the user.
The app will identify if the user has gone offline and in case of an exceptionally identified safe spot, the user will be notified through an SMS, the directions of that spot.

## Parameters for identifying the nearest and safest spot
1. Recording other users' dynamic response - Algorithm to identify the safest spot will consider recently recorded responses of other users who have already marked themselves safe.
2. Geometrical aspects of the area (such as detecting as open areas, height, age of nearby structures,etc.) 

## Dataset Used
1. Architectural specifications of the area around the user, which can be collected from users' responses or some relevant sources (eg. data from any government office)
2. Surface elevation data from any known satellite

## Technology Used
Google direction API, Google maps API, Android SDK, Machine Learning and Azure.
