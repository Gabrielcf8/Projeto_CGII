/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

/**
 *
 * @author Gabriel
 */
public class Monster extends Node{

    private RigidBodyControl physicsMonster;
    private AnimControl animationControl;
    private AnimChannel animationChannel;
    private Vector3f walkDirection = new Vector3f(0, 0, 0);
    private float airTime;
    private Spatial monster;

    public void createMonster(String name, AssetManager assetManager, BulletAppState bulletAppState, Node rootNode, float x, float y, float z) {
        monster = assetManager.loadModel("Models/Oto/Oto.mesh.xml");
        monster.setName("Monster");
        monster.setLocalTranslation(x,y,z);
        rootNode.attachChild(monster);
        
        physicsMonster = new RigidBodyControl(0);
        monster.addControl(physicsMonster);
        bulletAppState.getPhysicsSpace().add(physicsMonster);
        
        animationControl = monster.getControl(AnimControl.class);
        animationChannel = animationControl.createChannel();
    }
    public static final Quaternion ROLL180 = new Quaternion().fromAngleAxis(FastMath.PI, new Vector3f(0, 1, 0));

    public Vector3f getWalkDirection() {
        return walkDirection;
    }

    public void setWalkDirection(Vector3f walkDirection) {
        this.walkDirection = walkDirection;
    }

    void upDateAnimationPlayer() {

        if (walkDirection.length() == 0) {
            if (!"Walk".equals(animationChannel.getAnimationName())) {
                animationChannel.setAnim("Walk", 1f);
            }
        } else {
            if (airTime > .3f) {
                if (!"Idle2".equals(animationChannel.getAnimationName())) {
                    animationChannel.setAnim("Walk");
                }
            } else if (!"Walk".equals(animationChannel.getAnimationName())) {
                animationChannel.setAnim("Walk", 2f);
            }
        }
    }

    void upDateKeys(PlayerCameraNode player, double tpf) {
        //walkDirection.set(0, 0, 0);
        //walkDirection.addLocal(player.getWalkDirection().mult(2));
        //monster.setLocalTranslation(player.getWalkDirection().x,player.getWalkDirection().y,player.getWalkDirection().z);
        //monster.move(player.getLocalTranslation().x,0,player.getLocalTranslation().z); 
        //physicsMonster.setWalkDirection(walkDirection);
        //upDateAnimationPlayer();
    }
}
