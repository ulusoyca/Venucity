<h1 align="center">Welcome to Venucity 👋</h1>
<p>
  <a href="http://www.apache.org/licenses/LICENSE-2.0" target="_blank">
    <img alt="License: Apache License, Version 2.0 (the &#34;License&#34;)" src="https://img.shields.io/badge/License-Apache License, Version 2.0 (the &#34;License&#34;)-yellow.svg" />
  </a>
  <a href="https://twitter.com/ulusoyapps" target="_blank">
    <img alt="Twitter: ulusoyapps" src="https://img.shields.io/twitter/follow/ulusoyapps.svg?style=social" />
  </a>
</p>

Venucity Android project lists the nearby venues with an option to save them as favorite. 

MAD Scorecard of the project can be accessed <a href= "https://madscorecard.withgoogle.com/scorecards/2542951502/">here</a>.

![screencapture_resized.gif](screenshots/screencapture_resized.gif)

The application data layer consists of 3 following components:
<li><b>remote</b> fetches JSON files from a web server</li>
<li><b>cache</b> responsible for database operations</li>
<li><b>locationprovider</b> provides mock location data with a given time interval</li>

Following libraries and concepts are used in this application:

<li><a href= "https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html">Clean architecture</a></li> 
<li><a href= "https://developer.android.com/topic/libraries/architecture">Android Architecture Components</a> and some of the Jetpack Libraries (ViewModel, LiveData, Lifecycle, Room, Navigation Component, and more)</li> 
<li>Unit testing with <a href= "https://site.mockito.org/">Mockito</a>, assertions in tests with <a href= "https://truth.dev/">Truth</a></li> 
<li><a href= "https://github.com/airbnb/epoxy">Epoxy</a> for building screens in a RecyclerView</li> 
<li><a href= "https://dagger.dev/dev-guide/android.html">Dagger Android</a> for dependency injection</li>
<li><a href= "https://developer.android.com/kotlin/flow">Kotlin flows</a> and <a href= "https://developer.android.com/kotlin/coroutines">Kotlin coroutines</a></li> 
<li><a href= "https://square.github.io/retrofit/">Retrofit</a> HTTP API with <a href= "https://square.github.io/retrofit/">Moshi</a> to parse JSON</li> 
<li><a href= "https://github.com/michaelbull/kotlin-result">kotlin-result</a> for monad for modelling success (Ok) or failure (Err) operations</li> 
<li><a href= "https://github.com/coil-kt/coil">coil</a> for image loading</li> 
<li><a href= "https://github.com/airbnb/lottie-android">lottie-android</a> for Lottie animations</li>

## Author

👤 **Cagatay Ulusoy**

* Twitter: [@ulusoyapps](https://twitter.com/ulusoyapps)
* Github: [@ulusoyca](https://github.com/ulusoyca)
* LinkedIn:
  [@https:\/\/www.linkedin.com\/in\/cagatayulusoy\/](https://linkedin.com/in/https:\/\/www.linkedin.com\/in\/cagatayulusoy\/)

## Show your support

Give a ⭐️ if this project helped you!

## 📝 License

Copyright © 2020 [Cagatay Ulusoy](https://github.com/ulusoyca).<br />
This project is
[Apache License, Version 2.0 (the &#34;License&#34;)](http://www.apache.org/licenses/LICENSE-2.0)
licensed.
