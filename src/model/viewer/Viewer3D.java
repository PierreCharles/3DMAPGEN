package model.viewer;

import java.io.File;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import javafx.event.EventHandler;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.MeshView;

/**
 * Builder of viewer 3D. This object instantiate interaction and configuration
 * of the subscene 3D use Intercation3D class
 * 
 * @author picharles
 *
 */
public class Viewer3D {

	final Group root = new Group();
	final Group axisGroup = new Group();

	final PerspectiveCamera camera = new PerspectiveCamera(true);

	final double cameraDistance = 500;

	boolean timelinePlaying = false;

	double ONE_FRAME = 1.0 / 24.0;
	double DELTA_MULTIPLIER = 200.0;
	double CONTROL_MULTIPLIER = 0.1;
	double SHIFT_MULTIPLIER = 0.1;
	double ALT_MULTIPLIER = 0.5;
	double mousePosX, mousePosY, mouseOldX, mouseOldY, mouseDeltaX, mouseDeltaY;

	final Interactor3D object3DGroup = new Interactor3D();
	final Interactor3D world = new Interactor3D();
	final Interactor3D cameraXform = new Interactor3D();
	final Interactor3D cameraXform2 = new Interactor3D();
	final Interactor3D cameraXform3 = new Interactor3D();

	final static float minX = -10;
	final static float minY = -10;
	final static float maxX = 10;
	final static float maxY = 10;

	private MeshView currentMeshView;
 
	/**
	 * Initialize method for add a new world, build a camera and build the axes
	 * 
	 * @param paneViewer3D
	 *            the paneViewer3D JavaFX of the main window
	 * @return
	 */
	public SubScene initializeViewer3D(Pane paneViewer3D) {
		root.getChildren().add(world);
		buildCamera();
		buildAxes();
		return new SubScene(root, paneViewer3D.widthProperty().get(), paneViewer3D.heightProperty().get(), true,
				SceneAntialiasing.BALANCED);
	}

	
	/**
	 * Method to display a .obj file to an 3d object into viewer
	 * 
	 * @param file
	 */
	public void build3DObjectViewer(File file) {
		ObjModelImporter objImporter = new ObjModelImporter();
		try {
			objImporter.read(file);
		} catch (ImportException e) {}
		
		world.getChildren().remove(currentMeshView);
		currentMeshView = objImporter.getImport()[0];
		world.getChildren().addAll(currentMeshView);
	}
	
	/**
	 * Method to reset and clear viewer
	 */
	public void resetAndClearViewer() {
		if(currentMeshView!=null){
			world.getChildren().remove(currentMeshView);
		}
	}

	/**
	 * Method for configure the 3d viewer and define view controller
	 * 
	 * @param subSceneViewer3D
	 *            : the subScene to configure
	 */
	public void configure(SubScene subSceneViewer3D) {
		subSceneViewer3D.setFill(Color.LIGHTSLATEGREY);
		handleMouse(subSceneViewer3D, world);
		subSceneViewer3D.setCamera(camera);
		subSceneViewer3D.setCache(true);
		subSceneViewer3D.setCacheHint(CacheHint.SCALE_AND_ROTATE);
	}

	/**
	 * Method to build a camera
	 */
	private void buildCamera() {
		root.getChildren().add(cameraXform);
		cameraXform.getChildren().add(cameraXform2);
		cameraXform2.getChildren().add(cameraXform3);
		cameraXform3.getChildren().add(camera);
		cameraXform3.setRotateZ(180.0);

		camera.setNearClip(1);
		camera.setFarClip(1000.0);
		camera.setTranslateZ(-cameraDistance);
		cameraXform.ry.setAngle(180.0);
		cameraXform.rx.setAngle(40);
	}

	/**
	 * Method to create axes
	 */
	private void buildAxes() {
		final PhongMaterial redMaterial = new PhongMaterial();
		redMaterial.setDiffuseColor(Color.RED);

		final PhongMaterial greenMaterial = new PhongMaterial();
		greenMaterial.setDiffuseColor(Color.GREEN);

		final PhongMaterial blueMaterial = new PhongMaterial();
		blueMaterial.setDiffuseColor(Color.BLUE);

		final Box xAxis = new Box(250.0, 0.5, 0.5);
		final Box yAxis = new Box(0.5, 250.0, 0.5);
		final Box zAxis = new Box(0.5, 0.5, 250.0);

		xAxis.setMaterial(redMaterial);
		yAxis.setMaterial(greenMaterial);
		zAxis.setMaterial(blueMaterial);

		axisGroup.getChildren().addAll(xAxis, yAxis, zAxis);
		world.getChildren().addAll(axisGroup);
	}

	/**
	 * Mouse controller
	 * 
	 * @param subSceneViewer3D2
	 * @param root
	 */
	private void handleMouse(SubScene subSceneViewer3D2, final Node root) {
		subSceneViewer3D2.setOnMousePressed(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				mousePosX = me.getSceneX();
				mousePosY = me.getSceneY();
				mouseOldX = me.getSceneX();
				mouseOldY = me.getSceneY();
			}
		});
		subSceneViewer3D2.setOnMouseDragged(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent me) {
				mouseOldX = mousePosX;
				mouseOldY = mousePosY;
				mousePosX = me.getSceneX();
				mousePosY = me.getSceneY();
				mouseDeltaX = (mousePosX - mouseOldX);
				mouseDeltaY = (mousePosY - mouseOldY);

				double modifier = 1.0;
				double modifierFactor = 0.1;

				if (me.isControlDown()) {
					modifier = 0.1;
				}
				if (me.isShiftDown()) {
					modifier = 10.0;
				}
				if (me.isPrimaryButtonDown()) {
					cameraXform.ry.setAngle(cameraXform.ry.getAngle() - mouseDeltaX * modifierFactor * modifier * 2.0); // +
					cameraXform.rx.setAngle(cameraXform.rx.getAngle() + mouseDeltaY * modifierFactor * modifier * 2.0); // -
				} else if (me.isSecondaryButtonDown()) {
					double z = camera.getTranslateZ();
					double newZ = z + mouseDeltaX * modifierFactor * modifier * 10;
					camera.setTranslateZ(newZ);
				} else if (me.isMiddleButtonDown()) {
					cameraXform2.t.setX(cameraXform2.t.getX() + mouseDeltaX * modifierFactor * modifier * 0.3); // -
					cameraXform2.t.setY(cameraXform2.t.getY() + mouseDeltaY * modifierFactor * modifier * 0.3); // -
				}
			}
		});
	}

}