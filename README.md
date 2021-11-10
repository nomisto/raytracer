# Raytracer

A simple java raytracer.

## Build

```bash
(Windows) javac -cp "./src;./lib/*" -d ./bin src/Main.java
(Linux) javac -cp "./src:./lib/*" -d ./bin src/Main.java
```

## Run

```bash
(Windows) java -cp "./bin;./lib/*" Main {path-to-scene-XML} [-ss {width} {height} {minimumdistance}]
(Linux) java -cp "./bin:./lib/*" Main {path-to-scene-XML} [-ss {width} {height} {minimumdistance}]
```

Output location of the generated image is the same as the path of the scene XML file (Name of generated image has to be specified in the scene).

**-ss, --supersampling** {width} {height} {minimumdistance} (optional)

This option specifies [supersampling with a Poisson disc](https://en.wikipedia.org/wiki/Supersampling#Poisson_disk), where {width} and {height} specify the shape of the sample window and {minimumdistance} the minimum distance of the different rays.

If width and height are 1px, the different rays only shoot through the one pixel.
If width and height are 1.5px, the different rays shoot through the one pixel and through 0.25 of the
upper, left, right and bottom pixel.

F.e.: -ss 1 1 0.25 would result in a *maximum* of 25 rays cast (* is a cast):

```bash
     ​​​​​​​​​​​​​​​​​​​​​​​​ 1px ​​​​​​​​​​​​​​​​​​​​​​​​
 
​    * -- 0.25 -- * -- 0.25 -- * -- 0.25 -- * -- 0.25 -- *
​    |
​    0.25
​    |
​    * -- ... 
​    |
​    0.25
​    |
1px  * -- ...
​    |
​    0.25
​    |
​    * -- ...
​    |
​    0.25
​    |
​    * -- ...

```

## Scenes

Please make sure the scene.dtd is in the same folder as the scene xml. Scene specifications can be found [here]()

## Examples

Examples of scenes and generated images can be found in the `examples` folder. All example images were generated with supersampling (ss 1 1 0.25).

```
![alt text](http://url/to/img.png)
```

## Runtimes

| Scene                                   | Time       |
| --------------------------------------- | ---------- |
| example4.xml + supersampling (1/1/0.25) | ~ 15.5 sec |
| example5.xml + supersampling(1/1/0.25)  | ~ 14.2 sec |
| example6.xml + supersampling(1/1/0.25)  | ~ 67.7 min |

Note that renderings without supersampling are much faster. Experiments run on an AMD Ryzen 7 PRO 4750U with Radeon Graphics, 1700 MHz, 8 Cores (16 logical Cores).
