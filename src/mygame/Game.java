package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.PhysicsCollisionEvent;
import com.jme3.bullet.collision.PhysicsCollisionListener;
import com.jme3.input.KeyInput;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;

//To use the example assets in a new jMonkeyPlatform project, right-click your project, select "Properties", go to "Libraries", press "Add Library" and add the "jme3-test-data" library.
/*
 *
 * @author Gabriel
 */
public class Game
        extends SimpleApplication
        implements ActionListener, PhysicsCollisionListener {

    public static void main(String[] args) {
        Game app = new Game();
        app.showSettings = false;
        app.start();
    }
    private BulletAppState bulletAppState;
    private PlayerCameraNode player;
    private boolean up = false, down = false, left = false, right = false, space = false, rightclick = false;
    private Scene scene;
    private Node monsters = new Node();

    @Override
    public void simpleInitApp() {
        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);
        rootNode.detachAllChildren();
        scene = new Scene(assetManager, rootNode, bulletAppState);
        scene.createLigth();
        scene.createCity();

        scene.createFeno();
        monsters = scene.createMonsters();
        totalMonster = 20;
        rootNode.attachChild(monsters);
        createPlayer();
        initKeys();

        bulletAppState.setDebugEnabled(false);
        bulletAppState.getPhysicsSpace().addCollisionListener(this);
    }

    @Override
    public void simpleUpdate(float tpf) {
        player.upDateKeys(tpf, up, down, left, right, space, rightclick);
        
        for(int i = 0; i < monsters.getQuantity(); i++){
            Monster m = (Monster)monsters.getChild(i);
            m.upDateKeys(player, tpf);
        }
        
        if (vidaplayer == 0) {
            bulletAppState.cleanup();
            rootNode.detachAllChildren();
            System.out.println("YOU LOSE!!!");
        }
        else if (totalMonster <= 0){
            bulletAppState.cleanup();
            rootNode.detachAllChildren();
            System.out.println("YOU WIN!!!");
        } 
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    private void createPlayer() {
        player = new PlayerCameraNode("player", assetManager, bulletAppState, cam);
        rootNode.attachChild(player);
        flyCam.setEnabled(false);
    }

    @Override
    public void onAction(String binding, boolean value, float tpf) {
        switch (binding) {
            case "CharLeft":
                if (value) {
                    left = true;
                } else {
                    left = false;
                }
                break;
            case "CharRight":
                if (value) {
                    right = true;
                } else {
                    right = false;
                }
                break;
        }
        switch (binding) {
            case "CharForward":
                if (value) {
                    up = true;
                } else {
                    up = false;
                }
                break;
            case "CharBackward":
                if (value) {
                    down = true;
                } else {
                    down = false;
                }
                break;
        }
        switch (binding) {
            case "CharSpace":
                if (value) {
                    space = true;
                } else {
                    space = false;
                }
                break;
        }
        switch (binding) {
            case "CharAttack":
                if (value) {
                    rightclick = true;
                } else {
                    rightclick = false;
                }
                break;
        }
    }

    private void initKeys() {
        inputManager.addMapping("CharLeft", new KeyTrigger(KeyInput.KEY_A));
        inputManager.addMapping("CharRight", new KeyTrigger(KeyInput.KEY_D));
        inputManager.addMapping("CharForward", new KeyTrigger(KeyInput.KEY_W));
        inputManager.addMapping("CharBackward", new KeyTrigger(KeyInput.KEY_S));
        inputManager.addMapping("CharSpace", new KeyTrigger(KeyInput.KEY_SPACE));
        inputManager.addMapping("CharAttack", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));

        inputManager.addListener(this, "CharLeft", "CharRight");
        inputManager.addListener(this, "CharForward", "CharBackward");
        inputManager.addListener(this, "CharSpace");
        inputManager.addListener(this, "CharAttack");
    }

    private int totalMonster = 0;
    static int cont = 100;
    int vidaplayer = 10;

    /* A colored lit cube. Needs light source! */
    @Override
    public void collision(PhysicsCollisionEvent event) {
        Spatial nodeA = event.getNodeA();
        Spatial nodeB = event.getNodeB();
        if (vidaplayer > 0) {
            if (nodeA.getName().equals("Monster")) {
                if (player.getAnim().equals("Attack3")) {
                    bulletAppState.getPhysicsSpace().remove(nodeA);
                    monsters.detachChild(nodeA);
                    rootNode.detachChild(nodeA);
                    totalMonster--;
                    System.out.println("GANHOU 10 PONTOS");
                } else if (cont == 0) {
                    System.out.println("PERDEU VIDA");
                    cont = 100;
                    vidaplayer--;
                } else {
                    cont--;
                }
            } else {
                if (nodeB.getName().equals("Monster")) {
                    if (player.getAnim().equals("Attack3")) {
                        bulletAppState.getPhysicsSpace().remove(nodeB);
                        monsters.detachChild(nodeB);
                        rootNode.detachChild(nodeB);
                        totalMonster--;
                        System.out.println("GANHOU 10 PONTOS");
                    } else if (cont == 0) {
                        System.out.println("PERDEU VIDA");
                        cont = 100;
                        vidaplayer--;
                    } else {
                        cont--;
                    }
                }
            }
        }
    }
}
