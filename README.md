# java_raytracer
Simon Ott, 1463744


Build command, when in the java_raytracer folder:

(Windows) javac -cp "./src;./lib/*" -d ./bin src/Main.java
(Linux) javac -cp "./src:./lib/*" -d ./bin src/Main.java


Run command, when in the java_raytracer folder:

(Windows) java -cp "./bin;./lib/*" Main "locationOfXML"
(Linux) java -cp "./bin:./lib/*" Main "locationOfXML"

f.e. java -cp "./bin;./lib/*" Main "C:/Users/Simon/Desktop/Scenes/example3.xml"



If you want to run supersampling add the following parameters:
ss width height minimumdistance
f.e. a poissondisc with width and height 1 px and a minimumdistance of 0.25 would be:
java -cp "./bin;./lib/*" Main "C:/Users/Simon/Desktop/Scenes/example4.xml" ss 1 1 0.25




Please note the direction of the slashes and the quotation marks of the location of the xml.
Please make sure the scene.dtd is near the xml.
Output location is the same as the path of the XML file.
For the output of a black image, use the "black.xml" provided.
The width and height of the supersampling are the size of the window of the supersampling.
If width and height are 1px, the different rays only shoot through the one pixel.
If width and height are 1.5px, the different rays shoout through the one pixel and through 0.25 of the
upper, left, right and bottom pixel.

Runtimes:
example4 <1mins
example4+ss(1/1/0.25) <2mins
example5 ~6mins
example5+ss(1/1/0.25) ~90mins
example6 <1mins
example6+ss(1/1/0.25) <2mins
