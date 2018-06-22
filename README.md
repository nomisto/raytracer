# java_raytracer
Simon Ott, 1463744


Build command, when in the java_raytracer folder:

(Windows) javac -cp "./src;./lib/*" -d ./bin src/Main.java
(Linux) javac -cp "./src:./lib/*" -d ./bin src/Main.java


Run command, when in the java_raytracer folder:

(Windows) java -cp "./bin;./lib/*" Main "locationOfXML"
(Linux) java -cp "./bin:./lib/*" Main "locationOfXML"

f.e. java -cp "./bin;./lib/*" Main "C:/Users/Simon/Desktop/Scenes/example3.xml"



Please note the direction of the slashes and the quotation marks of the location of the xml.
Please make sure the scene.dtd is near the xml.
Output location is the same as the path of the XML file.
For the output of a black image, use the "black.xml" provided.