This folder contains an Eclipse project that calls the Sovren ParsingService
using JAX-WS, which is built into recent versions of Java.

Downloads used by this sample:
Eclipse - http://www.eclipse.org/downloads/download.php?file=/technology/epp/downloads/release/juno/SR2/eclipse-jee-juno-SR2-win32.zip

There is nothing in this project that depends on Eclipse. You could just as easily do the same
thing in NetBeans, IntelliJ IDEA, in a command prompt, or whatever your preference is for doing
Java development.

====================
To run this project:

1) Open the project in Eclipse.

2) Edit ParsingServiceClient.java to assign valid values to the AccountId and ServiceKey request parameters.

3) If your account was setup in the EU environment, change the API URL to "http://eu-services.resumeparsing.com/ParsingService.asmx".

4) Run the project.

====================
These are the exact steps used to produce this project:

1) Launch Eclipse

2) File > New > Other... > Java Project, enter name "ParsingService-jaxws", click Finish.

3) Generate client stubs for accessing the web service:

    a) Open a command prompt and change to the new project's folder.

	b) Run the following command to generate the Java proxy classes for the web service:
	   
	   "%JAVA_HOME%"\bin\wsimport -B-XautoNameResolution -s src http://services.resumeparsing.com/ParsingService.asmx?wsdl
	   
	   The "-s src" option generates java code in the "src" folder.
	   
	   The "-B-XautoNameResolution" option avoids a name collision from the WSDL. It causes
	   the return type from the web service to be named ParseResumeResponse2.
	   
	c) In Eclipse, refresh the project folder to see the new source files.
	
4) Manually create the ParsingServiceClient.java file in the src folder.

5) Put sample "resume.doc" file into the project folder.

6) Put sample "JobOrder.txt" file into the project folder.

7) Assign valid values to the AccountId and ServiceKey request parameters.

8) If your account was setup in the EU environment, change the API URL to "http://eu-services.resumeparsing.com/ParsingService.asmx".

9) Run the project to confirm that it works.
