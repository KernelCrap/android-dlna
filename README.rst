==========================
SlickDLNA for Android 4.0+
==========================
SlickDLNA is a free, simple and easy to use client for browsing UPnP and DLNA media servers and it will enable you to stream media to your device. It focuses on delivering a simple and lightweight experience.

Download
========
Available at the
`Google Play Store <https://play.google.com/store/apps/details?id=com.cajor.dk.dlna>`__

Requirements
============
* `Android 4.0+ <http://android.com/>`__
* `Cling 2.0-alpha3 <http://4thline.org/projects/cling/>`__ (Java/Android UPnP library and tools)
* `Jetty 8.1.12 <http://eclipse.org/jetty/>`__ (Servlet Engine and Http Server)
* `UrlImageViewHelper <https://github.com/koush/UrlImageViewHelper>`__ (Android library that sets an ImageView's contents from a url)

I have included a *build.gradle* file for easy dependency management in Android Studio. You will receive an error though when building, since most of the libraries for Jetty includes an *about.html* file that needs to be deleted from the .jar files.

The .jar files will be in sub folders in the *.gradle* folder (on Windows: C:\\Users\\<Username>\\.gradle).

Icons
=====
* `Elementary icons <http://danrabbit.deviantart.com/art/elementary-Icons-65437279>`__
* `Oxygen icons <http://oxygen-icons.org/>`__

License
=======
* `Apache Software License 2.0 <http://www.apache.org/licenses/LICENSE-2.0.html>`__
* Each of the components have their own license
