/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.asset.plugins.ZipLocator;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.bullet.util.CollisionShapeFactory;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import java.util.Random;

/**
 *
 * @author Gabriel
 */
public class Scene {

    private final AssetManager assetManager;
    private final Node rootNode;
    private final BulletAppState bulletAppState;
        private Node monsters = new Node();

    public Scene(AssetManager assetManager, Node rootNode, BulletAppState bulletAppState) {
        this.assetManager = assetManager;
        this.rootNode = rootNode;
        this.bulletAppState = bulletAppState;
    }

    public void createFeno() {
        Random r = new Random();
        for (int i = 0; i < 20; i++) {
            Feno.createSphere(r.nextInt(80), 0.01f, r.nextInt(80), assetManager, rootNode, bulletAppState);
        }
    }

    public Node createMonsters() {
        Random r = new Random();
        for (int i = 0; i < 5; i++){
            Monster m = new Monster();
            m.createMonster("monster", assetManager, bulletAppState, rootNode, r.nextInt(70), 1f, r.nextInt(70));
            m.createMonster("monster", assetManager, bulletAppState, rootNode, r.nextInt(70), 1f, -r.nextInt(70));
            m.createMonster("monster", assetManager, bulletAppState, rootNode, -r.nextInt(70), 1f, r.nextInt(70));
            m.createMonster("monster", assetManager, bulletAppState, rootNode, -r.nextInt(70), 1f, -r.nextInt(70));
            monsters.attachChild(m);
        }
        return monsters;
    }

    public void createLigth() {

        DirectionalLight l1 = new DirectionalLight();
        l1.setDirection(new Vector3f(1, -0.7f, 0));
        rootNode.addLight(l1);

        DirectionalLight l2 = new DirectionalLight();
        l2.setDirection(new Vector3f(-1, 0, 0));
        rootNode.addLight(l2);

        DirectionalLight l3 = new DirectionalLight();
        l3.setDirection(new Vector3f(0, 0, -1.0f));
        rootNode.addLight(l3);

        DirectionalLight l4 = new DirectionalLight();
        l4.setDirection(new Vector3f(0, 0, 1.0f));
        rootNode.addLight(l4);

        AmbientLight ambient = new AmbientLight();
        ambient.setColor(ColorRGBA.White);
        rootNode.addLight(ambient);
    }

    public void createCity() {
        assetManager.registerLocator("town.zip", ZipLocator.class);
        Spatial scene = assetManager.loadModel("main.scene");
        scene.setLocalTranslation(0, -5.2f, 0);
        rootNode.attachChild(scene);

        RigidBodyControl cityPhysicsNode = new RigidBodyControl(CollisionShapeFactory.createMeshShape(scene), 0);
        scene.addControl(cityPhysicsNode);
        bulletAppState.getPhysicsSpace().add(cityPhysicsNode);
    }
}
