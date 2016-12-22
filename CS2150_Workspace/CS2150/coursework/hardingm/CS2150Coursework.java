/* CS2150Coursework.java
 * TODO: Aston University Malvin Harding
 *
 * Scene Graph:
 *  Scene origin
 *  |
 *	+--[S(25,1,40) T(0,-1,-10)] Ground Plane
 *	|
 *	+--[S(25,1,10) Rx(90) T(0,4,-30)] Back Plane
 *	|
 *	+--[S(25,1,10) Rx(90) Ry(180) T(0,4,10)] Back AlleyWay Plane
 *	|
 *	+--[S(40,1,10) Rx(90) Ry(90) T(-12.5,4,-10)] LeftWall Plane
 *	|
 *	+--[S(40,1,10) Rx(270) Ry(90) T(12.5,4,-10)] RightWall Plane
 *	|
 *	+--[S(5,6,5) Ry(180) T(-12.5,4,-10)] Door
 *	|
 *	+--[Rx(10) Ry(180) T(0,6,10)] Camera 1
 *	|
 *	+--[Rx(10) Ry(270) T(12.5,6,-4)] Camera 2
 *	|
 *	+--[Rx(10) Ry(270) T(12.5,6,-22)] Camera 3
 */
package coursework.hardingm;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;

import GraphicsLab.*;

/**
 * TODO: Briefly describe your submission here
 *	Description:
 * <p>Controls:
 * <ul>
 * <li>Press the escape key to exit the application.
 * <li>Hold the x, y and z keys to view the scene along the x, y and z axis, respectively
 * <li>While viewing the scene along the x, y or z axis, use the up and down cursor keys
 *      to increase or decrease the viewpoint's distance from the scene origin
 * </ul>
 * TODO: Add any additional controls for your sample to the list above
 *
 */
public class CS2150Coursework extends GraphicsLab
{
	
    //TODO: Feel free to change the window title and default animation scale here
	/** 
	 * display list id for the unit AlleyWay 
	 */
	private final int alleyList = 1;
	/** 
	 * display list id for the unit plane 
	 */
    private final int planeList = 2;
    /**
     * display list id for the unit door
     */
    private final int doorList = 3;
    /**
     * display list id for the unit door
     */
    private final int cameraList = 4;
    
    float camera1X = 0.0f;
    float camera1Y = 6.0f;
    float camera1Z = 10.0f;
    float camera1YR = 180.f;
    float camera1XR = 10.0f;
    
    float camera1XView = 0.0f;
    float camera1YView = 0.0f;
    float camera1ZView = 0.0f;
    
    float cCamera1XView;
    float cCamera1YView;
    float cCamera1ZView;
    
    float camera2X = 12.5f;
    float camera2Y = 6.0f;
    float camera2Z = -4.0f;
    float camera2YR = 270.f;
    float camera2ZR = 0.0f;
    
    float camera2XView = 0.0f;
    float camera2YView = 0.0f;
    float camera2ZView = 0.0f;
    
    float cCamera2XView;
    float cCamera2YView;
    float cCamera2ZView;
    
    float camera3X = 12.5f;
    float camera3Y = 6.0f;
    float camera3Z = -22.0f;
    float camera3YRotation = 270.f;
    
    float camera3XView = 0.0f;
    float camera3YView = 0.0f;
    float camera3ZView = 0.0f;
    
    float cCamera3XView;
    float cCamera3YView;
    float cCamera3ZView;
    
    boolean camera1Active = false; 
    boolean camera2Active = false;
    boolean camera3Active = false;
    
    private Texture groundTextures;
    private Texture backdropTextures;
    private Texture wallTextures;
    private Texture alleyTextures;
    private Texture doorTextures;
	
    public static void main(String args[])
    {   
    	new CS2150Coursework().run(WINDOWED,"CS2150 Coursework Submission - The Alleyway",0.01f);
    }

    protected void initScene() throws Exception
    { 
    	//TODO: Initialise your resources here - might well call other methods you write.
    	
    	groundTextures = loadTexture("Lab5/textures/paving4.bmp");
        backdropTextures = loadTexture("Lab5/textures/cityBackdrop.bmp");
        wallTextures = loadTexture("Lab5/textures/BrickWall.bmp");
        alleyTextures = loadTexture("Lab5/textures/BackAlleyWall3.bmp");
        doorTextures = loadTexture("Lab5/textures/doorscratch.bmp");
    	
    	// global ambient light level
        float globalAmbient[] = {1.8f, 1.8f, 1.8f, 1.0f};
        // set the global ambient lighting
        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, FloatBuffer.wrap(globalAmbient));

        
        // the first light for the scene is white...
        float diffuse0[]  = { 0.6f,  0.6f, 0.4f, 1.0f};
        // ...with a dim ambient contribution...
        float ambient0[]  = { 0.5f,  0.5f, 0.5f, 1.0f};
        // ...and is positioned above and behind the viewpoint
        float position0[] = { -5.0f, 5.0f, 2.5f, 1.0f}; 

        //Lighting properties
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_AMBIENT, FloatBuffer.wrap(ambient0));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_DIFFUSE, FloatBuffer.wrap(diffuse0));
  		GL11.glLight(GL11.GL_LIGHT0, GL11.GL_SPECULAR, FloatBuffer.wrap(diffuse0));
        GL11.glLight(GL11.GL_LIGHT0, GL11.GL_POSITION, FloatBuffer.wrap(position0));
        GL11.glEnable(GL11.GL_LIGHT0);
        
        //Lighting calculations
        GL11.glEnable(GL11.GL_LIGHTING);
        //Ensure that all normals are re-normalised after transformations automatically
        GL11.glEnable(GL11.GL_NORMALIZE);
        
        GL11.glNewList(alleyList,GL11.GL_COMPILE);
        {
        	drawUnitAlleyWay();
        }
        GL11.glEndList();
        
        GL11.glNewList(planeList,GL11.GL_COMPILE);
        {
        	drawUnitPlane();
        }
        GL11.glEndList();
        
        GL11.glNewList(doorList,GL11.GL_COMPILE);
        {
        	drawUnitDoor();
        }
        GL11.glEndList(); 
        
        GL11.glNewList(cameraList,GL11.GL_COMPILE);
        {
        	drawUnitCamera();
        }
        GL11.glEndList(); 
        
    }
    
    protected void checkSceneInput()
    {
    	//TODO: Check for keyboard and mouse input here
    	if(Keyboard.isKeyDown(Keyboard.KEY_1))
        {   
    		camera2Active = false;
    		camera3Active = false;
    		camera1Active = true;
        }
    	else if (Keyboard.isKeyDown(Keyboard.KEY_2))
    	{
    		camera1Active = false;
    		camera3Active = false;
    		camera2Active = true;
    	}
    	else if (Keyboard.isKeyDown(Keyboard.KEY_3))
    	{
    		camera1Active = false;
    		camera2Active = false;
    		camera3Active = true;
    	}
        else if(Keyboard.isKeyDown(Keyboard.KEY_SPACE))
        {   
        	camera1Active = false;
        	camera2Active = false;
        	camera3Active = false;
        	resetAnimations();
        }
    	
    	//Camera 1 movement
    	if((camera1Active == true) && Keyboard.isKeyDown(Keyboard.KEY_UP))
        {   
    		cCamera1YView += 2.0f * getAnimationScale();
    		camera1YR += 2.0f * getAnimationScale();
        }
        else if((camera1Active == true) && Keyboard.isKeyDown(Keyboard.KEY_DOWN))
        {
        	cCamera1YView -= 2.0f * getAnimationScale();
        	camera1YR -= 2.0f * getAnimationScale();
        }
        else if((camera1Active == true) && Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
        {
        	cCamera1XView += 2.0f * getAnimationScale();
        	//camera1XRotation += 2.0f * getAnimationScale();
        }
        else if((camera1Active == true) && Keyboard.isKeyDown(Keyboard.KEY_LEFT))
        {
        	cCamera1XView -= 2.0f * getAnimationScale();
        	//camera1XRotation -= 2.0f * getAnimationScale();
        }
    	
    	//Camera 2 Movement
    	if((camera2Active == true) && Keyboard.isKeyDown(Keyboard.KEY_UP))
        {   
    		cCamera2YView += 2.0f * getAnimationScale();
        }
        else if((camera2Active == true) && Keyboard.isKeyDown(Keyboard.KEY_DOWN))
        {
        	cCamera2YView -= 2.0f * getAnimationScale();
        }
        else if((camera2Active == true) && Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
        {
        	cCamera2ZView -= 2.0f * getAnimationScale();
        }
        else if((camera2Active == true) && Keyboard.isKeyDown(Keyboard.KEY_LEFT))
        {
        	cCamera2ZView += 2.0f * getAnimationScale();
        }
    	
        //Camera 3 Movement
    	if((camera3Active == true) && Keyboard.isKeyDown(Keyboard.KEY_UP))
        {   
    		cCamera3YView += 2.0f * getAnimationScale();
        }
        else if((camera3Active == true) && Keyboard.isKeyDown(Keyboard.KEY_DOWN))
        {
        	cCamera3YView -= 2.0f * getAnimationScale();
        }
        else if((camera3Active == true) && Keyboard.isKeyDown(Keyboard.KEY_RIGHT))
        {
        	cCamera3ZView -= 2.0f * getAnimationScale();
        }
        else if((camera3Active == true) && Keyboard.isKeyDown(Keyboard.KEY_LEFT))
        {
        	cCamera3ZView += 2.0f * getAnimationScale();
        }
    }
    
    protected void updateScene()
    {
        //TODO: Update your scene variables here - remember to use the current animation scale value
        //        (obtained via a call to getAnimationScale()) in your modifications so that your animations
        //        can be made faster or slower depending on the machine you are working on
        
    	if(camera1Active == false)
    	{
    		
    	}
    	else if(camera2Active == false)
    	{

    	}
    	else if(camera3Active == false)
    	{

    	}
    } 
    
    protected void renderScene()
    {
    	//TODO: Render your scene here - remember that a scene graph will help you write this method! 
    	//      It will probably call a number of other methods you will write.
    	
    	// draw the ground plane
        GL11.glPushMatrix();
        {
        	// how shiny are the front faces of the house (specular exponent)
            float Shininess  = 2.0f;
            // specular reflection of the front faces of the house
            float Specular[] = {0.1f, 0.1f, 0.1f, 1.0f};
            // diffuse reflection of the front faces of the house
            float Diffuse[]  = {0.2f, 0.2f, 0.2f, 1.0f};
            
            // set the material properties for the house using OpenGL
            GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, Shininess);
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(Specular));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(Diffuse));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, FloatBuffer.wrap(Diffuse));
            
            // change the geometry colour to white so that the texture
            // is bright and details can be seen clearly
            Colour.WHITE.submit();
            // enable texturing and bind an appropriate texture
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D,groundTextures.getTextureID());
            
            // position, scale and draw the ground plane using its display list
            GL11.glTranslatef(0.0f,-1.0f,-10.0f);
            GL11.glScaled(25.0f, 1.0f, 40.0f);
            GL11.glCallList(planeList);

            // disable textures and reset any local lighting changes
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPopAttrib();
        }
        GL11.glPopMatrix();
        
        // draw the back plane
        GL11.glPushMatrix();
        {
        	// how shiny are the front faces of the house (specular exponent)
            float Shininess  = 2.0f;
            // specular reflection of the front faces of the house
            float Specular[] = {0.1f, 0.1f, 0.1f, 1.0f};
            // diffuse reflection of the front faces of the house
            float Diffuse[]  = {0.2f, 0.2f, 0.2f, 1.0f};
            
            // set the material properties for the house using OpenGL
            GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, Shininess);
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(Specular));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(Diffuse));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, FloatBuffer.wrap(Diffuse));
            
            // change the geometry colour to white so that the texture
            // is bright and details can be seen clearly
            Colour.WHITE.submit();
            
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, backdropTextures.getTextureID());
            
            // position, scale and draw the back plane using its display list
            GL11.glTranslatef( 0.0f, 4.0f, -30.0f);
            GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
            GL11.glScaled(25.0f, 1.0f, 10.0f);
            GL11.glCallList(planeList);
            
            // disable textures and reset any local lighting changes
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPopAttrib();
        }
        GL11.glPopMatrix();
        
     // draw the back AlleyWay plane
        GL11.glPushMatrix();
        {
        	// how shiny are the front faces of the house (specular exponent)
            float Shininess  = 2.0f;
            // specular reflection of the front faces of the house
            float Specular[] = {0.1f, 0.1f, 0.1f, 1.0f};
            // diffuse reflection of the front faces of the house
            float Diffuse[]  = {0.2f, 0.2f, 0.2f, 1.0f};
            
            // set the material properties for the house using OpenGL
            GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, Shininess);
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(Specular));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(Diffuse));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, FloatBuffer.wrap(Diffuse));
            
            // change the geometry colour to white so that the texture
            // is bright and details can be seen clearly
            Colour.WHITE.submit();
            
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, alleyTextures.getTextureID());
            
            // position, scale and draw the back plane using its display list
            GL11.glTranslatef(0.0f,4.0f, 10.0f);
            GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
            GL11.glScaled(25.0f, 1.0f, 10.0f);
            GL11.glCallList(planeList);
            
            // disable textures and reset any local lighting changes
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPopAttrib();
        }
        GL11.glPopMatrix();
        
        //draw the LeftWall plane
        GL11.glPushMatrix();
        {
        	// how shiny are the front faces of the house (specular exponent)
            float Shininess  = 2.0f;
            // specular reflection of the front faces of the house
            float Specular[] = {0.1f, 0.1f, 0.1f, 1.0f};
            // diffuse reflection of the front faces of the house
            float Diffuse[]  = {0.2f, 0.2f, 0.2f, 1.0f};
            
            // set the material properties for the house using OpenGL
            GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, Shininess);
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(Specular));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(Diffuse));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, FloatBuffer.wrap(Diffuse));
            
            // change the geometry colour to white so that the texture
            // is bright and details can be seen clearly
            Colour.WHITE.submit();
            
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, wallTextures.getTextureID());
            
            // position, scale and draw the back plane using its display list
            GL11.glTranslatef(-12.5f,4.0f, -10.0f);
            GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef(90.0f, 1.0f, 0.0f, 0.0f);
            GL11.glScaled(40.0f, 1.0f, 10.0f);
            GL11.glCallList(planeList);
            
            // disable textures and reset any local lighting changes
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPopAttrib();
        }
        GL11.glPopMatrix();
        
        //Draw the RightWall plane
        GL11.glPushMatrix();
        {
        	// how shiny are the front faces of the house (specular exponent)
            float Shininess  = 2.0f;
            // specular reflection of the front faces of the house
            float Specular[] = {0.1f, 0.1f, 0.1f, 1.0f};
            // diffuse reflection of the front faces of the house
            float Diffuse[]  = {0.2f, 0.2f, 0.2f, 1.0f};
            
            // set the material properties for the house using OpenGL
            GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, Shininess);
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(Specular));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(Diffuse));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, FloatBuffer.wrap(Diffuse));
        	
            // change the geometry colour to white so that the texture
            // is bright and details can be seen clearly
            Colour.WHITE.submit();
            
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, wallTextures.getTextureID());
            
            // position, scale and draw the back plane using its display list
            GL11.glTranslatef(12.5f, 4.0f, -10.0f);
            GL11.glRotatef(90.0f, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef(270.0f, 1.0f, 0.0f, 0.0f);
            GL11.glScaled(40.0f, 1.0f, 10.0f);
            GL11.glCallList(planeList);
            
            // disable textures and reset any local lighting changes
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPopAttrib();
        }
        GL11.glPopMatrix();
        
        //draw the door
        GL11.glPushMatrix();
        {
        	// how shiny are the front faces of the house (specular exponent)
            float Shininess  = 2.0f;
            // specular reflection of the front faces of the house
            float Specular[] = {0.1f, 0.1f, 0.1f, 1.0f};
            // diffuse reflection of the front faces of the house
            float Diffuse[]  = {0.2f, 0.2f, 0.2f, 1.0f};
            
            // set the material properties for the house using OpenGL
            GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, Shininess);
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(Specular));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(Diffuse));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, FloatBuffer.wrap(Diffuse));
            
            // change the geometry colour to white so that the texture
            // is bright and details can be seen clearly
            Colour.WHITE.submit();
            // enable texturing and bind an appropriate texture
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            GL11.glBindTexture(GL11.GL_TEXTURE_2D, doorTextures.getTextureID());
            
            // position, scale and draw the ground plane using its display list
            GL11.glTranslatef(0.0f, -1.0f, 10.0f);
            GL11.glRotatef(180.0f, 0.0f, 1.0f, 0.0f);
            GL11.glScaled( 5.0f, 6.0f, 5.0f);
            GL11.glCallList(doorList);

            // disable textures and reset any local lighting changes
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPopAttrib();
        }
        GL11.glPopMatrix();
        
        //Draw Camera 1
        GL11.glPushMatrix();
        {
        	// how shiny are the front faces of the house (specular exponent)
            float Shininess  = 2.0f;
            // specular reflection of the front faces of the house
            float Specular[] = {0.1f, 0.1f, 0.1f, 1.0f};
            // diffuse reflection of the front faces of the house
            float Diffuse[]  = {0.2f, 0.2f, 0.2f, 1.0f};
            
            // set the material properties for the house using OpenGL
            GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, Shininess);
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(Specular));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(Diffuse));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, FloatBuffer.wrap(Diffuse));
            
            // position, scale and draw the ground plane using its display list
            GL11.glTranslatef(camera1X, camera1Y, camera1Z);
            GL11.glRotatef(camera1YR, 0.0f, 1.f, 0.0f);
            GL11.glRotatef(camera1XR, 1.0f, 0.0f, 0.0f);
            GL11.glCallList(cameraList);

            // disable textures and reset any local lighting changes
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPopAttrib();
        }
        GL11.glPopMatrix();
        
        //Draw Camera 2
        GL11.glPushMatrix();
        {
        	// how shiny are the front faces of the house (specular exponent)
            float Shininess  = 2.0f;
            // specular reflection of the front faces of the house
            float Specular[] = {0.1f, 0.1f, 0.1f, 1.0f};
            // diffuse reflection of the front faces of the house
            float Diffuse[]  = {0.2f, 0.2f, 0.2f, 1.0f};
            
            // set the material properties for the house using OpenGL
            GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, Shininess);
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(Specular));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(Diffuse));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, FloatBuffer.wrap(Diffuse));
            
            // position, scale and draw the ground plane using its display list
            GL11.glTranslatef(camera2X, camera2Y, camera2Z);
            GL11.glRotatef(camera2YR, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef(10.0f, 1.0f, 0.0f, 0.0f);
            GL11.glCallList(cameraList);

            // disable textures and reset any local lighting changes
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPopAttrib();
        }
        GL11.glPopMatrix();
        
      //Draw Camera 3
        GL11.glPushMatrix();
        {
        	// how shiny are the front faces of the house (specular exponent)
            float Shininess  = 2.0f;
            // specular reflection of the front faces of the house
            float Specular[] = {0.1f, 0.1f, 0.1f, 1.0f};
            // diffuse reflection of the front faces of the house
            float Diffuse[]  = {0.2f, 0.2f, 0.2f, 1.0f};
            
            // set the material properties for the house using OpenGL
            GL11.glMaterialf(GL11.GL_FRONT, GL11.GL_SHININESS, Shininess);
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_SPECULAR, FloatBuffer.wrap(Specular));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_DIFFUSE, FloatBuffer.wrap(Diffuse));
            GL11.glMaterial(GL11.GL_FRONT, GL11.GL_AMBIENT, FloatBuffer.wrap(Diffuse));
            
            // position, scale and draw the ground plane using its display list
            GL11.glTranslatef(camera3X, camera3Y, camera3Z);
            GL11.glRotatef(camera3YRotation, 0.0f, 1.0f, 0.0f);
            GL11.glRotatef(10.0f, 1.0f, 0.0f, 0.0f);
            GL11.glCallList(cameraList);

            // disable textures and reset any local lighting changes
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glPopAttrib();
        }
        GL11.glPopMatrix();
    }
    
    protected void setSceneCamera()
    {
        // call the default behaviour defined in GraphicsLab. This will set a default perspective projection
        // and default camera settings ready for some custom camera positioning below...  
        super.setSceneCamera();
        
        if(camera1Active == true)
    	{
    		GLU.gluLookAt(camera1X, camera1Y, camera1Z - 1.2f,   // viewer location        
    	  		      cCamera1XView, cCamera1YView, cCamera1ZView,    // view point loc.
    	  		      0.0f, 1.0f, 0.0f);
    	}
    	else if(camera2Active == true)
    	{
    		GLU.gluLookAt(camera2X - 1.2f, camera2Y, camera2Z,   // viewer location        
    	  		      cCamera2XView, cCamera2YView, cCamera2ZView,    // view point loc.
    	  		      0.0f, 1.0f, 0.0f);
    	}
    	else if(camera3Active == true)
    	{
    		GLU.gluLookAt(camera3X - 1.2f, camera3Y, camera3Z,   // viewer location        
    	  		      cCamera3XView, cCamera3YView, cCamera3ZView,    // view point loc.
    	  		      0.0f, 1.0f, 0.0f);
    	}
    	else
    	{
        GLU.gluLookAt(0.0f, 20.0f, -60.0f,   // viewer location        
  		      0.0f, 0.0f, 0.0f,    // view point loc.
  		      0.0f, 1.0f, 0.0f);   // view-up vector
        }
        /*
        GLU.gluLookAt(camera1X,camera1Y,camera1Z,   // viewer location        
        		cCamera1XView, cCamera1YView, cCamera1ZView,    // view point loc.
    		      0.0f, 1.0f, 0.0f);   // view-up vector
    	*/
        //TODO: If it is appropriate for your scene, modify the camera's position and orientation here
        //        using a call to GL11.gluLookAt(...)
    }
    
    protected void cleanupScene()
    {
    	//TODO: Clean up your resources here
    }
    
    private void resetAnimations()
    {
        // reset all attributes that are modified by user controls or animations
        cCamera1XView = camera1XView;
        cCamera1YView = camera1YView;
        cCamera1ZView = camera1ZView;
        
        cCamera2XView = camera2XView;
        cCamera2YView = camera2YView;
        cCamera2ZView = camera2ZView;
        
        cCamera3XView = camera3XView;
        cCamera3YView = camera3YView;
        cCamera3ZView = camera3ZView;
        
    }
    
    private void drawUnitPlane()
    {
    	Vertex v1 = new Vertex(-0.5f, 0.0f,-0.5f); // left,  back
        Vertex v2 = new Vertex( 0.5f, 0.0f,-0.5f); // right, back
        Vertex v3 = new Vertex( 0.5f, 0.0f, 0.5f); // right, front
        Vertex v4 = new Vertex(-0.5f, 0.0f, 0.5f); // left,  front
        
        // draw the plane geometry. order the vertices so that the plane faces up
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v4.toVector(),v3.toVector(),v2.toVector(),v1.toVector()).submit();
            
            GL11.glTexCoord2f(0.0f,0.0f);
            v4.submit();
            
            GL11.glTexCoord2f(1.0f,0.0f);
            v3.submit();
            
            GL11.glTexCoord2f(1.0f,1.0f);
            v2.submit();
            
            GL11.glTexCoord2f(0.0f,1.0f);
            v1.submit();
        }
        GL11.glEnd();
        
        // if the user is viewing an axis, then also draw this plane
        // using lines so that axis aligned planes can still be seen
        if(isViewingAxis())
        {
            // also disable textures when drawing as lines
            // so that the lines can be seen more clearly
            GL11.glPushAttrib(GL11.GL_TEXTURE_2D);
            GL11.glDisable(GL11.GL_TEXTURE_2D);
            GL11.glBegin(GL11.GL_LINE_LOOP);
            {
                v4.submit();
                v3.submit();
                v2.submit();
                v1.submit();
            }
            GL11.glEnd();
            GL11.glPopAttrib();
        }
    }
    
    private void drawUnitDoor()
    {
    	Vertex v1 = new Vertex(-1.0f, 1.0f, 0.125f);
		Vertex v2 = new Vertex(1.0f, 1.0f, 0.125f);
		Vertex v3 = new Vertex(1.0f, 0.0f, 0.125f);
		Vertex v4 = new Vertex(-1.0f, 0.0f, 0.125f);
		
		Vertex v5 = new Vertex(-1.0f, 1.0f, 0.0f);
		Vertex v6 = new Vertex(1.0f, 1.0f, 0.0f);
		Vertex v7 = new Vertex(1.0f, 0.0f, 0.0f);
		Vertex v8 = new Vertex(-1.0f, 0.0f, 0.0f);	 
		
		//Near face of the door
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v1.toVector(), v4.toVector(), v3.toVector(), v2.toVector()).submit();
			
			
			GL11.glTexCoord2f(1.0f,1.0f); //(0.0f, 0.0f)
            v1.submit();
            
            GL11.glTexCoord2f(1.0f,0.0f); //(1.0f, 0.0f)
            v4.submit();
            
            GL11.glTexCoord2f(0.0f,0.0f); //(1.0f, 1.0f)
            v3.submit();
            
            GL11.glTexCoord2f(0.0f,1.0f); //(0.0f, 1.0f)
            v2.submit();
            
			GL11.glEnd();
		}
		
		//Far face of the door
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v5.toVector(), v6.toVector(), v7.toVector(), v8.toVector()).submit();
					
			v5.submit();
			v6.submit();
			v7.submit();
			v8.submit();
					
			GL11.glEnd();
		}
		
		//Right face of the door
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v2.toVector(), v3.toVector(), v7.toVector(), v6.toVector()).submit();
					
			v2.submit();
			v3.submit();
			v7.submit();
			v6.submit();
					
			GL11.glEnd();
		}
		
		//Left face of the door
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v1.toVector(), v5.toVector(), v8.toVector(), v4.toVector()).submit();
					
			v1.submit();
			v5.submit();
			v8.submit();
			v4.submit();
					
			GL11.glEnd();
		}
		
		//Top face of the door
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v1.toVector(), v2.toVector(), v6.toVector(), v5.toVector()).submit();
					
			v1.submit();
			v2.submit();
			v6.submit();
			v5.submit();
					
			GL11.glEnd();
		}
		
		//Bottom face of the door
		GL11.glBegin(GL11.GL_POLYGON);
		{
			new Normal(v8.toVector(), v7.toVector(), v3.toVector(), v4.toVector()).submit();
					
			v8.submit();
			v7.submit();
			v3.submit();
			v4.submit();
					
			GL11.glEnd();
		}
    }
    
    private void drawUnitAlleyWay()
    {
    	//Scene object Groups
    	Vertex v1 = new Vertex(-0.5f,0.0f,1.0f);
		Vertex v2 = new Vertex(-0.5f,0.5f,1.0f);
		Vertex v3 = new Vertex(0.5f,0.5f,1.0f);
		Vertex v4 = new Vertex(0.5f,0.0f,1.0f);
		Vertex v5 = new Vertex(-0.5f,0.0f,-1.0f);
		Vertex v6 = new Vertex(-0.5f,0.5f,-1.0f);
		Vertex v7 = new Vertex(0.5f,0.5f,-1.0f);
		Vertex v8 = new Vertex(0.5f,0.0f,-1.0f);
			
		GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v1.toVector(),v2.toVector(),v3.toVector(),v4.toVector()).submit();
        	
            v1.submit();
            v2.submit();
            v3.submit();
            v4.submit();
            
        }
        GL11.glEnd();
        
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v1.toVector(),v5.toVector(),v6.toVector(),v2.toVector()).submit();
        	
        	v1.submit();
            v5.submit();
            v6.submit();
            v2.submit();
            
        }
        GL11.glEnd();
        
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v4.toVector(),v3.toVector(),v7.toVector(),v8.toVector()).submit();

            v4.submit();
            v3.submit();
            v7.submit();
            v8.submit();
        }
        GL11.glEnd();
        
        GL11.glBegin(GL11.GL_POLYGON);
        {
            new Normal(v4.toVector(),v8.toVector(),v5.toVector(),v1.toVector()).submit();

            v4.submit();
            v8.submit();
            v5.submit();
            v1.submit();
        }
        GL11.glEnd();
        
    }

    private void drawUnitCamera()
        {
    		/*
    		 * Camera Base Coordinates
    		 */
        	Vertex v1 = new Vertex(-0.25f,0.0f,1.0f);
    		Vertex v2 = new Vertex(-0.25f,0.5f,1.0f);
    		Vertex v3 = new Vertex(0.25f,0.5f,1.0f);
    		Vertex v4 = new Vertex(0.25f,0.0f,1.0f);	
    		Vertex v5 = new Vertex(-0.25f,0.5f,0.0f);
    		Vertex v6 = new Vertex(0.25f,0.5f,0.0f);
    		Vertex v7 = new Vertex(0.25f,0.0f,0.0f);
    		Vertex v8 = new Vertex(-0.25f,0.0f,0.0f);
    	
    		/*
    		 * Larger Lens Piece Coordinates
    		 */
    		Vertex v9 = new Vertex(-0.15f, 0.1f, 1.2f);
    		Vertex v10 = new Vertex(-0.15f, 0.4f, 1.2f);
    		Vertex v11 = new Vertex(0.15f, 0.4f, 1.2f);
    		Vertex v12 = new Vertex(0.15f, 0.1f, 1.2f);
    		Vertex v13 = new Vertex(-0.15f, 0.4f, 1.0f);
    		Vertex v14 = new Vertex(0.15f, 0.4f, 1.0f);
    		Vertex v15 = new Vertex(0.15f, 0.1f, 1.0f);
    		Vertex v16 = new Vertex(-0.15f, 0.1f, 1.0f);
    		
    		/*
    		 * Smaller Lens Piece Coordinates
    		 */
    		Vertex v17 = new Vertex(-0.075f, 0.2f, 1.25f);
    		Vertex v18 = new Vertex(-0.075f, 0.3f, 1.25f);
    		Vertex v19 = new Vertex(0.075f, 0.3f, 1.25f);
    		Vertex v20 = new Vertex(0.075f, 0.2f, 1.25f);
    		Vertex v21 = new Vertex(-0.075f, 0.3f, 1.2f);
    		Vertex v22 = new Vertex(0.075f, 0.3f, 1.2f);
    		Vertex v23 = new Vertex(0.075f, 0.2f, 1.2f);
    		Vertex v24 = new Vertex(-0.075f, 0.2f, 1.2f);
    		
    		/*
    		 * LED Coordinates
    		 */
    		Vertex v25 = new Vertex(0.175f, 0.4f, 1.115f);
    		Vertex v26 = new Vertex(0.175f, 0.45f, 1.115f);
    		Vertex v27 = new Vertex(0.225f, 0.45f, 1.115f);
    		Vertex v28 = new Vertex(0.225f, 0.4f, 1.115f);
    		Vertex v29 = new Vertex(0.175f, 0.4f, 1.0f);
    		Vertex v30 = new Vertex(0.225f, 0.4f, 1.0f);
    		Vertex v31 = new Vertex(0.225f, 0.45f, 1.0f);
    		Vertex v32 = new Vertex(0.175f, 0.45f, 1.0f);
    		
    		
    		GL11.glBegin(GL11.GL_POINTS);
    		/*
    		 * Camera Base Points 
    		 */
    		v1.submit();
    		v2.submit();
    		v3.submit();
    		v4.submit();	
    		v5.submit();
    		v6.submit();
    		v7.submit();
    		v8.submit();

    		/*
    		 * Larger Lens Points
    		 */
    		v9.submit();
    		v10.submit();
    		v11.submit();
    		v12.submit();
    		v13.submit();
    		v14.submit();
    		v15.submit();
    		v16.submit();
    		
    		/*
    		 * Smaller Lens points
    		 */
    		v17.submit();
    		v18.submit();
    		v19.submit();
    		v20.submit();
    		v21.submit();
    		v22.submit();
    		v23.submit();
    		v24.submit();
    		
    		/*
    		 * LED Point
    		 */
    		v25.submit();
    		v26.submit();
    		v27.submit();
    		v28.submit();
    		v29.submit();
    		v30.submit();
    		v31.submit();
    		v32.submit();
    		
    		GL11.glEnd();
    		
    		/*
    		 * Camera Base Coordinates
    		 */
    		GL11.glBegin(GL11.GL_POLYGON);
            {
    			//Near face
    			
               	v1.submit();
    	    	v4.submit();
               	v3.submit();
               	v2.submit();
            }
           	GL11.glEnd();
            
            GL11.glBegin(GL11.GL_POLYGON);
            {
            	//Far face
            
            	v8.submit();
    	    	v5.submit();
    	    	v6.submit();
    	    	v7.submit();
            }
            GL11.glEnd();
            
    	    GL11.glBegin(GL11.GL_POLYGON);
            {
            	//Top face

    	        v3.submit();
            	v6.submit();
                v5.submit();
                v2.submit();
            }
            GL11.glEnd();
            
            GL11.glBegin(GL11.GL_POLYGON);
            {
                //Bottom face

                v1.submit();
                v8.submit();
                v7.submit();
                v4.submit();
            }
            GL11.glEnd();
            
            GL11.glBegin(GL11.GL_POLYGON);
            {
                //Left face

                v1.submit();
                v2.submit();
                v5.submit();
                v8.submit();
            }
            GL11.glEnd();
            
            GL11.glBegin(GL11.GL_POLYGON);
            {
                //Right face

                v3.submit();
                v4.submit();
                v7.submit();
                v6.submit();
            }
            GL11.glEnd();
            
            /*
             * 
             * LARGER LENS PIECE
             */
            GL11.glBegin(GL11.GL_POLYGON);
            {
    			//Lens Near face
    			
            	
                v9.submit();
                v12.submit();
                v11.submit();
                v10.submit();
            }
            GL11.glEnd();
            
            GL11.glBegin(GL11.GL_POLYGON);
            {
            	//Lens Far face
            	
            	v16.submit();
                v13.submit();
                v14.submit();
                v15.submit();
            }
            GL11.glEnd();
            
            GL11.glBegin(GL11.GL_POLYGON);
            {
                //Lens Top face

                v10.submit();
                v11.submit();
                v14.submit();
                v13.submit();
            }
            GL11.glEnd();
            
            GL11.glBegin(GL11.GL_POLYGON);
            {
                //Lens Bottom face

                v9.submit();
                v16.submit();
                v15.submit();
                v12.submit();
            }
            GL11.glEnd();
            
            GL11.glBegin(GL11.GL_POLYGON);
            {
                //Lens Left face

                v9.submit();
                v10.submit();
                v13.submit();
                v16.submit();
            }
            GL11.glEnd();
            
            GL11.glBegin(GL11.GL_POLYGON);
            {
                //Lens Right face

                v15.submit();
                v14.submit();
                v11.submit();
                v12.submit();
            }
            GL11.glEnd();
            
            /*
             * 
             * SMALLER LENS PIECE
             */
            GL11.glBegin(GL11.GL_POLYGON);
            {
    			//Lens Near face
    			
                v17.submit();
                v20.submit();
                v19.submit();
                v18.submit();
            }
            GL11.glEnd();
            
            GL11.glBegin(GL11.GL_POLYGON);
            {
            	//Lens Far face
            	
            	v24.submit();
                v21.submit();
                v22.submit();
                v23.submit();
            }
            GL11.glEnd();
            
            GL11.glBegin(GL11.GL_POLYGON);
            {
                //Lens Top face

                v18.submit();
                v19.submit();
                v22.submit();
                v21.submit();
            }
            GL11.glEnd();
            
            GL11.glBegin(GL11.GL_POLYGON);
            {
                //Lens Bottom face

                v17.submit();
                v24.submit();
                v23.submit();
                v20.submit();
            }
            GL11.glEnd();
            
            GL11.glBegin(GL11.GL_POLYGON);
            {
                //Lens Left face

                v17.submit();
                v18.submit();
                v21.submit();
                v24.submit();
            }
            GL11.glEnd();
            
            GL11.glBegin(GL11.GL_POLYGON);
            {
                //Lens Right face

                v20.submit();
                v23.submit();
                v22.submit();
                v19.submit();
            }
            GL11.glEnd();
            
            /*
             * 
             * Camera LED
             */
            /*
            GL11.glBegin(GL11.GL_POLYGON);
            {
    			//LED Near face
    			
                v25.submit();
                v28.submit();
                v27.submit();
                v26.submit();
            }
            GL11.glEnd();
            
            GL11.glBegin(GL11.GL_POLYGON);
            {
            	//LED Far face
            	
            	v32.submit();
                v29.submit();
                v30.submit();
                v31.submit();
            }
            GL11.glEnd();
            
            GL11.glBegin(GL11.GL_POLYGON);
            {
                //LED Top face

                v26.submit();
                v27.submit();
                v30.submit();
                v29.submit();
            }
            GL11.glEnd();
            
            GL11.glBegin(GL11.GL_POLYGON);
            {
                //LED Bottom face

                v25.submit();
                v32.submit();
                v31.submit();
                v28.submit();
            }
            GL11.glEnd();
            
            GL11.glBegin(GL11.GL_POLYGON);
            {
                //LED Left face

                v25.submit();
                v26.submit();
                v29.submit();
                v32.submit();
            }
            GL11.glEnd();
            
            GL11.glBegin(GL11.GL_POLYGON);
            {
                //Lens Right face

                v27.submit();
                v28.submit();
                v31.submit();
                v30.submit();
            }
            GL11.glEnd();
            */
        }

}
