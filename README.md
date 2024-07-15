CardBinder - a Magic: The Gathering collector's app.

The app is built using Jetpack Compose alongside Kotlin in order to give the best experience to it's users. The data is taken from the Scryfall API using Retrofit, serialized using KotlinX Serialization and cached using a Room local database in order to preserve networking resources and be available even if the device's connection fails, representing a single source of truth. When reading data from Scryfall, Paging 3 is used for paginating query responses. Text processing from card scans is managed using Google's Machine Learning Kit's Text Recognition library. MVVM is the architecture that was chosen for development, going hand in hand with Jetpack Compose's reactive nature. The app also uses Dagger Hilt for dependency injection and Coin for loading async images.
  
A user can log in with either email/password or Google:

![](https://github.com/dutaci28/CardBinder/blob/master/gifs/login%20with%20google.gif)

Users are greeted with a random card, retrieved from the Scryfall API.

![](https://github.com/dutaci28/CardBinder/blob/master/gifs/search%20page%20random%20card.gif)

They can search for individual cards using text;

![](https://github.com/dutaci28/CardBinder/blob/master/gifs/search%20individual%20card.gif)

Or by taking a picture of a card;

![](https://github.com/dutaci28/CardBinder/blob/master/gifs/card%20scan.gif)

And they can add those cards to their collections.

![](https://github.com/dutaci28/CardBinder/blob/master/gifs/collection%20pager.gif)

CardBinder - a Magic: The Gathering collector's app.

Copyright (C) 2024  Catalin Duta
