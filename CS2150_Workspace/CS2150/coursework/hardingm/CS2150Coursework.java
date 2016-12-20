/* CS2150Coursework.java
 * TODO: Aston University Malvin Harding
 *
 * Scene Graph:
 *  Scene origin
 *  |
 *
 *  TODO: Provide a scene graph for your submission
 */
package coursework.hardingm;

import org.lwjgl.opengl.GL11;
import org.lwjgl.util.glu.GLU;
import org.lwjgl.input.Keyboard;
import org.newdawn.slick.opengl.Texture;

import GraphicsLab.*;

/**
 * TODO: Briefly describe your submission here
 *
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
	/** display list id for the unit AlleyWay */
	private final int AlleyList = 1;
	/** display list id for the unit plane */
    private final int planeList = 2;
	
    public static void main(String args[])
    {   
    	new CS2150Coursework().run(WINDOWED,"CS2150 Coursework Submission",0.01f);
    }

    protected void initScene() throws Exception
    {
    	//TODO: Initialise your resources here - might well call other methods you write.
    	
    	//groundTextures = loadTexture("Lab5/textures/grass.bmp");
        //skyTextures = loadTexture("Lab5/textures/nightSky.bmp");
    	
    	// global ambient light level
        float globalAmbient[] = {0.2f,  0.2f,  0.2f, 1f};
        // set the global ambient lighting
        GL11.glLightModel(GL11.GL_LIGHT_MODEL_AMBIENT, FloatBuffer.wrap(globalAmbient));

        
        // the first light for the scene is white...
        float diffuse0[]  = { 0.6f,  0.6f, 0.6f, 1.0f};
        // ...with a dim ambient contribution...
        float ambient0[]  = { 0.1f,  0.1f, 0.1f, 1.0f};
        // ...and is positioned above and behind the viewpoint
        float position0[] = { -10.0f, 10.0f, 5.0f, 1.0f}; 

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
        
        GL11.glNewList(AlleyList,GL11.GL_COMPILE);
        {
        	drawUnitAlleyWay();
        }
        GL11.glEndList();
        
        GL11.glNewList(AlleyList,GL11.GL_COMPILE);
        {
        	drawUnitPlane();
        }
        GL11.glEndList();
        
    }
    
    protected void checkSceneInput()
    {
    	//TODO: Check for keyboard and mouse input here
    }
    
    protected void updateScene()
    {
        //TODO: Update your scene variables here - remember to use the current animation scale value
        //        (obtained via a call to getAnimationScale()) in your modifications so that your animations
        //        can be made faster or slower depending on the machine you are working on
    }
    
    protected void renderScene()
    {
    	//TODO: Render your scene here - remember that a scene graph will help you write this method! 
    	//      It will probably call a number of other methods you will write.
    	
    	// draw the ground plane
        GL11.glPushMatrix();
        {
            // disable lighting calculations so that they don't affect
            // the appearance of the texture 
            GL11.glPushAttrib(GL11.GL_LIGHTING_BIT);
            GL11.glDisable(GL11.GL_LIGHTING);
            // change the geometry colour to white so that the texture
            // is bright and details can be seen clearly
            Colour.WHITE.submit();
            // enable texturing and bind an appropriate texture
            GL11.glEnable(GL11.GL_TEXTURE_2D);
            //GL11.glBindTexture(GL11.GL_TEXTURE_2D,groundTextures.getTextureID());
            
            // position, scale and draw the ground plane using its display list
            GL11.glTranslatef(0.0f,-1.0f,-10.0f);
            GL11.glScaled(25.0f, 1.0f, 20.0f);
            GL11.glCallList(planeList);

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

        //TODO: If it is appropriate for your scene, modify the camera's position and orientation here
        //        using a call to GL11.gluLookAt(...)
    }

    protected void cleanupScene()
    {
    	//TODO: Clean up your resources here
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

}
