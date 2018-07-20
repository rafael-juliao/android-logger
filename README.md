# Android Logger

* Simple way to log
  * Info, Debug and Error
  * Excecptions classes
* Auto-tag the object with LoggerInterface
* Exception never thrown

## Gradle Dependency

https://jitpack.io/#rafael-juliao/android-logger/1.2

Step 1. Add the JitPack repository to your build file
Add it in your root build.gradle at the end of repositories:
```
allprojects {
  repositories {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
Step 2. Add the dependency

```
dependencies {
  implementation 'com.github.rafael-juliao:android-logger:1.2'
}
```

## How to

You have to call setup from Logger, and then you can
* Call the methods directly
* Extend the class and create your own Logger


### SETUP

The setup receives two parameters

* App name string used to log.(you can filter the all the app log by #app_name in LogCat)
* Logger Interface used to auto-tag the caller given his class

```
  Logger.setup("MyApp", new LoggerInterface() {
      @Override
      public String tag(Object object) 
      {
          if( object instanceof MyAwesomeClassA || object instanceof MyAwesomeClassB){
              return "AWESOME";
          }
          return "APPLICATION";
      }
  });
```

###### Configuration

```
public static int MAX_SIZE_OBJECT_NAME = 25;
public static int MAX_TAG_SIZE = 15;
public static boolean LOG_TIME = true; 
```

#### Basic Logging

###### API
```
//INFO
Logger.I(this, "The battery is low");

//DEBUG
Logger.D(this, "Location Received");

//ERROR
Logger.E(this, "No connection with Internet when trying to retrieve something");
```

###### RESULT
```
I:  #MyApp | 13:01:26:612 | #ACTIVITY        | #MainActivity -> The battery is low
D:  #MyApp | 13:01:26:613 | #ACTIVITY        | #MainActivity -> Location Received
E:  #MyApp | 13:01:26:614 | #ACTIVITY        | #MainActivity -> No connection with Internet when trying to retrieve something
```

#### Basic Logging with Parameters

###### API
```
Logger.I(this, "The battery is low(%d)", batteryPercentageInt );

Logger.E(this, "Error : %d | Message %s", errorNumberInt, errorMessageString);
```



#### Exception Logging

###### API
```
catch(Exception e){
  Logger.EXP(this, e );
}

```

###### RESULT
```
E:  #MyApp | 13:01:26:618 | #ACTIVITY        | #MainActivity -> ArrayIndexOutOfBoundsException --> com.lfyt.mobile.android.logsample.MainActivity.onStart:81
```


## LoggerInterface

When calling the logger methods, will have to pass the caller instance(this).
This is used to print the name of the caller,
and used to *group different classes under the same tag*

Then you can normal call the methods of the Logger class (I,D,E)

###### Sample
```
  Logger.setup("MyApp", new LoggerInterface() {
      @Override
      public String tag(Object object) 
      {
          if( object instanceof MyAwesomeClassA || object instanceof MyAwesomeClassB){
              return "AWESOME";
          }
          
          
          if( object instanceof Model ){
              return "MODEL";
          }
          
          if( object instanceof GenericWebService){
              return "WEB";
          }
          return "APPLICATION";
      }
  });
```

Normal call the D, I, E, methods
but now the logger will recognize the TAG by the instance

###### RESULT

```
D:  #MyApp | 13:01:26:428 | #APP             | #LogSampleApp -> The Application Has Started
I:  #MyApp | 13:01:26:616 | #ACTIVITY        | #MainActivity -> Activity Started
D:  #MyApp | 13:01:26:617 | #AWESOME         | #MyAwesomeClass -> Awesome Class Handling onActivityStarted
D:  #MyApp | 13:01:26:617 | #MODEL           | #MyModel -> Call Web something
D:  #MyApp | 13:01:26:617 | #WEB             | #WebSomethingService -> HTTP POST --> { ABCD } ...
D:  #MyApp | 13:01:26:617 | #WEB             | #WebSomethingService -> HTTP RESPONSE <-- { XYZ } ...

```

Then you can filter by the #Tag in LogCat to only see the logs related to that tag...
#APP, #ACTIVITY, #WEB


## Create your Logger

###### Class
```
import android.app.Activity;

import com.lfyt.mobile.android.log.Logger;

public class L extends Logger {

  static void start(){
      LOG_TIME = true;
      Logger.setup("MyApp", new LoggerInterface() {
          @Override
          public String tag(Object object) {
              return TAG(object);
          }
      });
  }

  private static String TAG(Object object) {

      if( object instanceof MyAwesomeClass ){
          return "AWESOME";
      }

      if( object instanceof Activity ){
          return "ACTIVITY";
      }

      if( object instanceof Model ){
          return "MODEL";
      }

      if( object instanceof GenericWebService){
          return "WEB";
      }

      return "APP";
  }

  public static void W(Object caller, String message, Object... params){
      debug(caller, tagify("WAE"), message, params);
  }

}
```


###### Your API

```
L.W(this, "Hey, this is my own custon log, how awesome");

```

###### RESULT
```
D:  #MyApp | 13:01:26:427 | #WAE             | #WaeObject -> Hey, this is my own custon log, how awesome
D:  #MyApp | 13:01:26:428 | #APP             | #LogSampleApp -> The Application Has Started
I:  #MyApp | 13:01:26:616 | #ACTIVITY        | #MainActivity -> Activity Started
D:  #MyApp | 13:01:26:617 | #AWESOME         | #MyAwesomeClass -> Awesome Class Handling onActivityStarted
D:  #MyApp | 13:01:26:617 | #MODEL           | #MyModel -> Call Web something
D:  #MyApp | 13:01:26:617 | #WEB             | #WebSomethingService -> HTTP POST --> { ABCD } ...
D:  #MyApp | 13:01:26:617 | #WEB             | #WebSomethingService -> HTTP RESPONSE <-- { XYZ } ...

```

