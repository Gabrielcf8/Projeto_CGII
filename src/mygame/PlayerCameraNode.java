/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.animation.AnimChannel;
import com.jme3.animation.AnimControl;
import com.jme3.asset.AssetManager;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.BetterCharacterControl;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.Camera;
import com.jme3.scene.CameraNode;
import com.jme3.scene.Node;
import com.jme3.scene.control.CameraControl;

/**
 *
 * @author Gabriel
 *
 */
public class PlayerCameraNode extends Node {

    private final BetterCharacterControl physicsCharacter;
    private final AnimControl animationControl;
    private final AnimChannel animationChannel;
    private Vector3f walkDirection = new Vector3f(0, 0, 0);
    private Vector3f viewDirection = new Vector3f(0, 0, 0);
    private float airTime;

    public String getAnim() {
        return anim;
    }

    public void setAnim(String anim) {
        this.anim = anim;
    }
    private String anim;

    public PlayerCameraNode(String name, AssetManager assetManager, BulletAppState bulletAppState, Camera cam) {
        super(name);
        
        Node ninja = (Node) assetManager.loadModel("Models/Ninja/Ninja.mesh.xml");
        setLocalTranslation(0, 0, 0);
        ninja.setLocalTranslation(0, 5, 0);
        ninja.setLocalRotation(ROLL180);
        ninja.scale(0.03f);
        ninja.setLocalTranslation(0, 0, 0);
        attachChild(ninja);

        physicsCharacter = new BetterCharacterControl(1, 5.5f, 16f);
        addControl(physicsCharacter);
        bulletAppState.getPhysicsSpace().add(physicsCharacter);

        animationControl = ninja.getControl(AnimControl.class);
        animationChannel = animationControl.createChannel();

        CameraNode camNode = new CameraNode("CamNode", cam);
        camNode.setControlDir(CameraControl.ControlDirection.SpatialToCamera);
        camNode.setLocalTranslation(new Vector3f(0, 20, -30));
        camNode.lookAt(this.getLocalTranslation(), Vector3f.UNIT_Y);

        this.attachChild(camNode);
    }
    public static final Quaternion ROLL180 = new Quaternion().fromAngleAxis(FastMath.PI, new Vector3f(0, 1, 0));

    public Vector3f getWalkDirection() {
        return walkDirection;
    }

    public void setWalkDirection(Vector3f walkDirection) {
        this.walkDirection = walkDirection;
    }

    public Vector3f getViewDirection() {
        return viewDirection;
    }

    public void setViewDirection(Vector3f viewDirection) {
        this.viewDirection = viewDirection;
    }

    void upDateAnimationPlayer() {

        if (walkDirection.length() == 0) {
            if (!"Idle2".equals(animationChannel.getAnimationName())) {
                animationChannel.setAnim("Idle2", 1f);
                anim = "Idle2";
            }
        } else {
            if (airTime > .3f) {
                if (!"Idle2".equals(animationChannel.getAnimationName())) {
                    animationChannel.setAnim("Idle2");
                    anim = "Idle2";
                }
            } else if (!"Walk".equals(animationChannel.getAnimationName())) {
                animationChannel.setAnim("Walk", 2f);
                anim = "Walk";
            }
        }
    }

    void upDateKeys(float tpf, boolean up, boolean down, boolean left, boolean right, boolean space, boolean rightclick) {

        Vector3f camDir = getWorldRotation().mult(Vector3f.UNIT_Z);

        viewDirection.set(camDir);
        walkDirection.set(0, 0, 0);

        walkDirection.addLocal(camDir.mult(3));

        if (up) {
            walkDirection.addLocal(camDir.mult(3));
        } else if (down) {
            walkDirection.addLocal(camDir.mult(3).negate());
        }

        if (left) {
            Quaternion rotateL = new Quaternion().fromAngleAxis(FastMath.PI * tpf, Vector3f.UNIT_Y);
            rotateL.multLocal(viewDirection);
        } else if (right) {
            Quaternion rotateR = new Quaternion().fromAngleAxis(-FastMath.PI * tpf, Vector3f.UNIT_Y);
            rotateR.multLocal(viewDirection);
        }

        physicsCharacter.setWalkDirection(walkDirection);
        physicsCharacter.setViewDirection(viewDirection);

        if (space) {
            animationChannel.setAnim("HighJump", 2f);
            anim = "HighJump";
        } else if (rightclick) {
            animationChannel.setAnim("Attack3", 1f);
            anim = "Attack3";
        } else {
            upDateAnimationPlayer();
        }
    }
}
