/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;

/**
 *
 * @author Gabriel
 */
public class Feno {
    public static void createSphere(float x, float y, float z, AssetManager assetManager,Node rootNode, BulletAppState bulletAppState) {
        /* A colored lit cube. Needs light source! */

        Sphere boxMesh = new Sphere(30, 30, 0.8f);
        Geometry boxGeo = new Geometry("Feno", boxMesh);
        Material boxMat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture boxText = assetManager.loadTexture("Textures/feno.jpg");
        //boxMat.setColor("Ambient", ColorRGBA.Green);
        boxMat.setTexture("ColorMap", boxText);
        boxGeo.setMaterial(boxMat);
        boxGeo.setLocalTranslation(x, y, z);
        rootNode.attachChild(boxGeo);

        RigidBodyControl boxPhysicsNode = new RigidBodyControl(1);
        boxGeo.addControl(boxPhysicsNode);
        bulletAppState.getPhysicsSpace().add(boxPhysicsNode);
    }
}
