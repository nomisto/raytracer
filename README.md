# Raytracer

A simple java raytracer.

<p align="center">
  <img src="https://raw.githubusercontent.com/nomisto/raytracer/master/examples/example5.png">
</p>

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

Please make sure the scene.dtd is in the same folder as the scene xml. Scene specifications are from Lukas Herzberger.

### Scene Description

The input file begins with the header and a scene node, that specifies the output file, followed by the camera specifications. This is followed by the lights and surfaces. Each surface consists of a material, and zero or more transformations. Note that the transformations should be composed in the order they appear in the file. 

### Basic

```xml
<?xml version="1.0" standalone="no" ?>
<!DOCTYPE scene SYSTEM "scene.dtd">
```

Every file has to start with this header that specifies the xml version and the schema that should be used:

```xml
<scene output_file="myImage.png">
   <background_color r="1.0" g="0.0" b="0.0"/>
   <!-- add scene here -->
</scene>
```

The scene node is the main node in the scene file. Every other node has to be a child node of it. The output is sends to the file specified by output_file, in this example to "myImage.png". `<background_color>` sets the color of the background for when rays do not intersect any geometry. 

```xml
<!-- XML-Style Comment -->
```

Everything in the file between <!-- and --> should be treated as a comment (as always in XML).

### Camera

```xml
<camera>
   <position x="1.0" y="-2.0E-10" z="-3"/>
   <lookat x="1" y="2" z="3"/>
   <up x="1" y="2" z="3"/>
   <horizontal_fov angle="45"/>
   <resolution horizontal="1920" vertical="1080"/>
   <max_bounces n="100"/>
</camera> 
```

The camera node has to appear once in every file in the scene node. It has to include the following information: 

<**position** *x*="1.0" *y*="-2.0E-10" *z*="-3"/>

Location of the camera in world coordinates.  

<**lookat** *x*="1" *y*="2" *z*="3"/> 

The camera is looking at this point in space.  

<**up** *x*="1" *y*="2" *z*="3"/> 

The vector defines which direction is up.  

<**horizontal_fov** *angle*="45"/> 

The angle, *in degrees*, specifies **half** of the angle from the left side of the screen to the right side of the  screen. When you're rendering your image, you'll use this number to  space out the rays you fire. You should scale this angle appropriately  in the Y direction based on the resolution of the output image. If the  image is wider than tall, the angle from top to bottom will be  proportionately smaller.  

<**resolution** *horizontal*="1920" *vertical*="1080"/> 

The pixel resolution of the final output image.  

<**max_bounces** *n*="100"/> 

n is the maximum number of bounces for reflection and refraction.  

### Light

```xml
<lights>
   <!-- add lights here -->
</lights> 
```

All the lights in the scene should be grouped in the lights node. There are four different light types that can be used in the scene:

```xml
<ambient_light>
   <color r="1.0" g="0.2" b="0.3"/>
</ambient_light> 
```

Defines an ambient light with color (r,g,b) - all objects are illuminated by this light. *Note: the world can have precisely one ambient light.* 

```xml
<parallel_light>
   <color r="0.1" g="0.2" b="0.3"/>
   <direction x="1" y="2" z="3"/>
</parallel_light> 
```

Defines a parallel light with color (r,g,b) - much like the sun, these lights are infinitely far away, and only have a direction vector direction (x,y,z).  

```xml
<point_light>
   <color r="0.1" g="0.2" b="0.3"/>
   <position x="1" y="2" z="3"/>
</point_light> 
```

Defines a point light with color (r,g,b) and position (x,y,z).  

```xml
<spot_light>
   <color r="0.1" g="0.2" b="0.3"/>
   <position x="1" y="2" z="3"/>
   <direction x="1" y="2" z="3"/>
   <falloff alpha1="1" alpha2="3"/>
</spot_light> 
```

Defines a spot light - this is optional for your assignment. Spot lights exist at a point in space (position) and point in a given direction. falloff with angle1 and angle2, both in degrees, specify how the light falls off. For any angle between zero and angle1, the light should be just like a point light. Between angle1 and angle2, the light falls off. For angles greater than angle2, the light isn't there. If you do this, start with linear falloff, then you may want to play with various gamma functions. 

### Surface/Geometry

```xml
<surfaces>
   <!-- Add geometric primitives (spheres/meshes) -->
</surface> 
```

All surfaces of these scene have to be grouped in the surface node. There are two different types of geometric primitives: 

```xml
<sphere radius="123">
   <position x="1" y="2" z="3"/>
   <!-- Material -->
   <!-- Transform -->
</sphere> 
```

This adds a sphere to the world centered at the given point (position) with the given radius. 

```xml
<mesh name="duck.dae">
   <!-- Material -->
   <!-- Transform -->
</mesh> 
```

Adds a triangle mesh specified using the [OBJ file format](http://en.wikipedia.org/wiki/Wavefront_.obj_file).

### Material

```xml
<material_solid>
   <color r="0.1" g="0.2" b="0.3"/>
   <phong ka="1" kd="1" ks="1" exponent="1"/>
   <reflectance r="1.0"/>
   <transmittance t="1.0"/>
   <refraction iof="1.0"/>
</material_solid> 
```

This defines a non-textured material

<**color** *r*="0.1" *g*="0.2" *b*="0.3"/> 

Specifies the materials color. 

<**phong** *ka*="1" *kd*="1" *ks*="1" *exponent*="1"/> 

Specifies the coefficients for the phong illumination model. *ka* is the ambient component,  *kd* is the diffuse component 	*ks* is the specular component and *exponent* 	is the Phong cosine power for highlights. 

<**reflectance** *r*="1.0"/>
<**transmittance** *t*="1.0"/> 

Specifies the reflactance *r* (fraction of the contribution of the reflected ray) and the transmittance  *t* (fraction of contribution of the transmitting ray).

<**refraction** *iof*="1"/> 

Specifies the index of refraction *iof* 

Usually, 0 <= Ka <= 1, 0 <= Kd <= 1, and 0 <= Ks <=  1, though it is not required that Ka + Kd + Ks == 1.

```xml
<material_textured>
   <texture name=""/>
   <phong ka="1" kd="1" ks="1" exponent="1"/>
   <reflectance r="1.0"/>
   <transmittance t="1.0"/>
   <refraction iof="1.0"/>
</material_textured> 
```

This defines a textured material

<**texture** *name*=""/> 

Specifies the texture file that should be used for this material. 

All other inputs are the same as the non-textured material.    	

### Transformation

```xml
<transforms>
   <translate x="1" y="1" z="1"/>
   <scale x="1" y="1" z="1"/>
   <rotateX theta="1"/>
   <rotateY theta="1"/>
   <rotateZ theta="1"/>
</transforms> 
```

Transformations can be specified in every geometric primitive  (sphere/mesh). There can be zero or more transformations that should be  applied in the order of their appearence in the block.  

<**translate** *x*="1" *y*="1" *z*="1"/> 

Moves an object by the vector [*x*,*y*,*z*].  

<**scale** *x*="1" *y*="1" *z*="1"/> 

Scales an object by *x* on the x-axis, *y* on the y-axis and  	*z* on the z-axis.  

<**rotateX** *theta*="1"/>
<**rotateY** *theta*="1"/>
<**rotateZ** *theta*="1"/> 

Rotates an object by *theta* degrees around the specific axis.  

### Example

```xml
<?xml version="1.0" encoding="UTF-8"?>
<!DOCTYPE scene SYSTEM "scene.dtd">
<scene output_file="myImage.png">
   <background_color r="1.0" g="0.0" b="0.0" />
   <camera>
      <position x="1.0" y="-2.0E-10" z="-3" />
      <lookat x="1" y="2" z="3" />
      <up x="1" y="2" z="3" />
      <horizontal_fov angle="90" />
      <resolution horizontal="1920" vertical="1080" />
      <max_bounces n="100" />
   </camera>
   <lights>
      <ambient_light>
         <color r="0.1" g="0.2" b="0.3" />
      </ambient_light>
      <point_light>
         <color r="0.1" g="0.2" b="0.3" />
         <position x="1" y="2" z="3" />
      </point_light>
      <parallel_light>
         <color r="0.1" g="0.2" b="0.3" />
         <direction x="1" y="2" z="3" />
      </parallel_light>
      <spot_light>
         <color r="0.1" g="0.2" b="0.3" />
         <position x="1" y="2" z="3" />
         <direction x="1" y="2" z="3" />
         <falloff alpha1="1" alpha2="3" />
      </spot_light>
   </lights>
   <surfaces>
      <sphere radius="123">
         <position x="1" y="2" z="3" />
         <material_solid>
            <color r="0.1" g="0.2" b="0.3" />
            <phong ka="1.0" kd="1.0" ks="1.0" exponent="1" />
            <reflectance r="1.0" />
            <transmittance t="1.0" />
            <refraction iof="1.0" />
         </material_solid>
         <transform>
            <translate x="1" y="1" z="1" />
            <scale x="1" y="1" z="1" />
            <rotateX theta="1" />
            <rotateY theta="1" />
            <rotateZ theta="1" />
         </transform>
      </sphere>
      <mesh name="duck.dae">
         <material_textured>
            <texture name="" />
            <phong ka="1.0" kd="1.0" ks="1.0" exponent="1" />
            <reflectance r="1.0" />
            <transmittance t="1.0" />
            <refraction iof="1.0" />
         </material_textured>
         <transform>
            <translate x="1" y="1" z="1" />
            <scale x="1" y="1" z="1" />
            <rotateX theta="1" />
            <rotateY theta="1" />
            <rotateZ theta="1" />
            <translate x="1" y="1" z="1" />
            <scale x="1" y="1" z="1" />
         </transform>
      </mesh>
   </surfaces>
</scene>
```



## Examples

Examples of scenes and generated images can be found in the `examples` folder. All example images were generated with supersampling (ss 1 1 0.25).

<p align="center">
  <img src="https://raw.githubusercontent.com/nomisto/raytracer/master/examples/example3.png">
</p>

<p align="center">
  <img src="https://raw.githubusercontent.com/nomisto/raytracer/master/examples/example4.png">
</p>

<p align="center">
  <img src="https://raw.githubusercontent.com/nomisto/raytracer/master/examples/example5.png">
</p>

<p align="center">
  <img src="https://raw.githubusercontent.com/nomisto/raytracer/master/examples/example6.png">
</p>

## Runtimes

| Scene                                   | Time       |
| --------------------------------------- | ---------- |
| example4.xml + supersampling (1/1/0.25) | ~ 15.5 sec |
| example5.xml + supersampling(1/1/0.25)  | ~ 14.2 sec |
| example6.xml + supersampling(1/1/0.25)  | ~ 67.7 min |

Note that renderings without supersampling are much faster. Experiments run on an AMD Ryzen 7 PRO 4750U with Radeon Graphics, 1700 MHz, 8 Cores (16 logical Cores).
