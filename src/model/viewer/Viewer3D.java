package model.viewer;

import java.net.URL;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.sql.rowset.spi.TransactionalWriter;

import com.interactivemesh.jfx.importer.ImportException;
import com.interactivemesh.jfx.importer.obj.ObjModelImporter;

import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.scene.AmbientLight;
import javafx.scene.CacheHint;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.PerspectiveCamera;
import javafx.scene.PointLight;
import javafx.scene.SceneAntialiasing;
import javafx.scene.SubScene;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.paint.PhongMaterial;
import javafx.scene.shape.Box;
import javafx.scene.shape.CullFace;
import javafx.scene.shape.DrawMode;
import javafx.scene.shape.MeshView;
import javafx.scene.shape.TriangleMesh;
import javafx.util.Duration;
import model.mesh.Face;
import model.mesh.Mesh;
import model.mesh.Vertices;

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

	private Timeline timeline;
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

	/**
	 * Initialize methof for add a new world, build a camera and build the axes
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

	public void build3DObjectViewer() {

		ObjModelImporter objImporter = new ObjModelImporter();
		try {
			URL modelUrl = this.getClass().getResource("/other/MeshPart1.obj");
			objImporter.read(modelUrl);
		} catch (ImportException e) {
		}

		MeshView[] meshView = objImporter.getImport();

		Interactor3D meshForm3dObject = new Interactor3D();

		System.out.println(meshView[0]);
		
		meshForm3dObject.getChildren().addAll(meshView);
		meshForm3dObject.setTranslateX(-50);
		meshForm3dObject.setTranslateZ(-50);
		meshForm3dObject.setRotateX(180);

		world.getChildren().addAll(meshForm3dObject);
	}

	public void setNewMesh(List<Mesh> parcelsList) {

		for (Mesh mesh : parcelsList) {
			
			MeshView meshView = mesh.generate3DObject();
			
			meshView.setDrawMode(DrawMode.FILL);
			
			//meshView.setMaterial(new PhongMaterial(Color.RED));
			
			world.getChildren().addAll(meshView);
			
			/*
			
			TriangleMesh pyramidMesh = new TriangleMesh();
			
			pyramidMesh.getTexCoords().addAll(0,0);
			
			float h = 150;                    // Height
			float s = 300;                    // Side
			pyramidMesh.getPoints().addAll(
			        0,    0,    0,            // Point 0 - Top
			        0,    h,    -s/2,         // Point 1 - Front
			        -s/2, h,    0,            // Point 2 - Left
			        s/2,  h,    0,            // Point 3 - Back
			        0,    h,    s/2           // Point 4 - Right
			    );
			
			pyramidMesh.getFaces().addAll(
			        0,0,  2,0,  1,0,          // Front left face
			        0,0,  1,0,  3,0,          // Front right face
			        0,0,  3,0,  4,0,          // Back right face
			        0,0,  4,0,  2,0,          // Back left face
			        4,0,  1,0,  2,0,          // Bottom rear face
			        4,0,  3,0,  1,0           // Bottom front face
			    ); 
			
			System.out.println("1. : "+pyramidMesh.getPointElementSize());
			System.out.println("2. : "+pyramidMesh.getFaceElementSize());
			System.out.println("3. : "+pyramidMesh.getTexCoordElementSize());
			System.out.println("4. : "+pyramidMesh.getPoints().size());
			System.out.println("5. : "+pyramidMesh.getFaces().size());
			System.out.println("6. : "+pyramidMesh.getTexCoords().size());
			
			MeshView pyramid = new MeshView(pyramidMesh);
			pyramid.setDrawMode(DrawMode.FILL);
			pyramid.setTranslateX(200);
			pyramid.setTranslateY(100);
			pyramid.setTranslateZ(200);
			root.getChildren().add(pyramid);
			*/
		}

	}


	/**
	 * Method for configure the 3d viewer and define view controller
	 * 
	 * @param subSceneViewer3D
	 *            : the subscene to configure
	 */
	public void configure(SubScene subSceneViewer3D) {
		subSceneViewer3D.setFill(Color.LIGHTSLATEGREY);
		handleKeyboard(subSceneViewer3D, world);
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

	/**
	 * Keyboard controller
	 * 
	 * @param subSceneViewer3D2
	 * @param root
	 */
	private void handleKeyboard(SubScene subSceneViewer3D2, final Node root) {
		final boolean moveCamera = true;
		subSceneViewer3D2.setOnKeyPressed(new EventHandler<KeyEvent>() {
			@Override
			public void handle(KeyEvent event) {
				Duration currentTime;
				switch (event.getCode()) {
				case Z:
					if (event.isShiftDown()) {
						cameraXform.ry.setAngle(0.0);
						cameraXform.rx.setAngle(0.0);
						camera.setTranslateZ(-300.0);
					}
					cameraXform2.t.setX(0.0);
					cameraXform2.t.setY(0.0);
					break;
				case X:
					if (event.isControlDown()) {
						if (axisGroup.isVisible()) {
							axisGroup.setVisible(false);
						} else {
							axisGroup.setVisible(true);
						}
					}
					break;
				case S:
					if (event.isControlDown()) {
						if (object3DGroup.isVisible()) {
							object3DGroup.setVisible(false);
						} else {
							object3DGroup.setVisible(true);
						}
					}
					break;
				case SPACE:
					if (timelinePlaying) {
						timeline.pause();
						timelinePlaying = false;
					} else {
						timeline.play();
						timelinePlaying = true;
					}
					break;
				case UP:
					if (event.isControlDown() && event.isShiftDown()) {
						cameraXform2.t.setY(cameraXform2.t.getY() - 10.0 * CONTROL_MULTIPLIER);
					} else if (event.isAltDown() && event.isShiftDown()) {
						cameraXform.rx.setAngle(cameraXform.rx.getAngle() - 10.0 * ALT_MULTIPLIER);
					} else if (event.isControlDown()) {
						cameraXform2.t.setY(cameraXform2.t.getY() - 1.0 * CONTROL_MULTIPLIER);
					} else if (event.isAltDown()) {
						cameraXform.rx.setAngle(cameraXform.rx.getAngle() - 2.0 * ALT_MULTIPLIER);
					} else if (event.isShiftDown()) {
						double z = camera.getTranslateZ();
						double newZ = z + 5.0 * SHIFT_MULTIPLIER;
						camera.setTranslateZ(newZ);
					}
					break;
				case DOWN:
					if (event.isControlDown() && event.isShiftDown()) {
						cameraXform2.t.setY(cameraXform2.t.getY() + 10.0 * CONTROL_MULTIPLIER);
					} else if (event.isAltDown() && event.isShiftDown()) {
						cameraXform.rx.setAngle(cameraXform.rx.getAngle() + 10.0 * ALT_MULTIPLIER);
					} else if (event.isControlDown()) {
						cameraXform2.t.setY(cameraXform2.t.getY() + 1.0 * CONTROL_MULTIPLIER);
					} else if (event.isAltDown()) {
						cameraXform.rx.setAngle(cameraXform.rx.getAngle() + 2.0 * ALT_MULTIPLIER);
					} else if (event.isShiftDown()) {
						double z = camera.getTranslateZ();
						double newZ = z - 5.0 * SHIFT_MULTIPLIER;
						camera.setTranslateZ(newZ);
					}
					break;
				case RIGHT:
					if (event.isControlDown() && event.isShiftDown()) {
						cameraXform2.t.setX(cameraXform2.t.getX() + 10.0 * CONTROL_MULTIPLIER);
					} else if (event.isAltDown() && event.isShiftDown()) {
						cameraXform.ry.setAngle(cameraXform.ry.getAngle() - 10.0 * ALT_MULTIPLIER);
					} else if (event.isControlDown()) {
						cameraXform2.t.setX(cameraXform2.t.getX() + 1.0 * CONTROL_MULTIPLIER);
					} else if (event.isAltDown()) {
						cameraXform.ry.setAngle(cameraXform.ry.getAngle() - 2.0 * ALT_MULTIPLIER);
					}
					break;
				case LEFT:
					if (event.isControlDown() && event.isShiftDown()) {
						cameraXform2.t.setX(cameraXform2.t.getX() - 10.0 * CONTROL_MULTIPLIER);
					} else if (event.isAltDown() && event.isShiftDown()) {
						cameraXform.ry.setAngle(cameraXform.ry.getAngle() + 10.0 * ALT_MULTIPLIER); // -
					} else if (event.isControlDown()) {
						cameraXform2.t.setX(cameraXform2.t.getX() - 1.0 * CONTROL_MULTIPLIER);
					} else if (event.isAltDown()) {
						cameraXform.ry.setAngle(cameraXform.ry.getAngle() + 2.0 * ALT_MULTIPLIER); // -
					}
					break;
				}
			}
		});
	}

}