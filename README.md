# Read theses very carefully before you start

### How it works:

* Your must have internet connection.
* Your must have a github account.
* First of all fork this repo.
* When you are done, push the changes to your forked repo and send us your repo url.
* We will automatically clone your repo, compile it and run against our own input set.
* This template is managed using gradle build system.
* To use gradle you do not need to install it by yourself.
* Gradle will be downloaded automatically when you run build command for first time.
* There are two different files inside project root directory (`gradlew.bat and gradlew`).
* Use `gradlew.bat` if you are in Windows system.
* Use `gradlew` if you are in Unix system.
* Compile command for Unix is `./gradlew -q compileJava` and for windows is `gradlew.bat -q compileJava`.
* Run the application and test using `./gradlew -q run` in unix system or `gradlew.bat -q run` in windows system.
* All of you application logic should be placed inside `Solution` class.
* `Solution` class is located inside `./src/main/java/com/tigerit/exam/` directory.
* There is a IO helper class named `./src/main/java/com/tigerit/exam/IO.java` for your convenience.
* If you have any questions, feel free feel free to contact us (zobayer@tigeritbd.com, ridowan@tigeritbd.com).

### System Requirements:

* Internet Connection
* Github Account
* JDK7+

### Caution:

* Do not change anything inside `build.gradle` file. That may cause failure building your application.
* Do not use any external `jar` file as your solution dependencies (you may add source to your project). 
* Use standard input/output for your application testing (we will redirect that to file input/output when we test).
