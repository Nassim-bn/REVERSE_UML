package fr.univrouen.umlreverse.ui.component.packag.elements;

import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewEntity;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewPackage;
import fr.univrouen.umlreverse.model.diagram.util.IStyle;
import fr.univrouen.umlreverse.ui.component.common.elements.AEntityGraphicController;
import fr.univrouen.umlreverse.ui.component.common.elements.IEntityTextGraphicController;
import fr.univrouen.umlreverse.ui.component.packag.dialog.DialogOPackage;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import fr.univrouen.umlreverse.ui.view.packag.IPackageController;
import fr.univrouen.umlreverse.ui.view.packag.PackageController;
import fr.univrouen.umlreverse.util.Contract;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Point2D;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.ContextMenuEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;

public class PackageGraphicController extends AEntityGraphicController
implements IEntityTextGraphicController, IPackageGraphicController {

	private final PackageGraphic packageG;
	private final IViewPackage model;
	private final PackageController diagramController;

	public PackageGraphicController(PackageGraphic packageGraphic, PackageController diagram, IViewPackage packag) {
		 super();
        Contract.check(packageGraphic != null,
                 "L'argument noteG ne doit pas être nul.");
        Contract.check(diagram != null,
                 "L'arguments diagramController ne doit pas être nul.");
        Contract.check(packag != null,
                 "L'argument packag ne doit pas être nul.");

        this.diagramController = diagram;
        this.model = packag;
        this.packageG = packageGraphic;
        setText();
        IStyle style = model.getStyle();
        setStyle(style, true);
        createController();
	}

	// REQUEST
    @Override
    public Color getBackgroundColor() {
        IStyle style = model.getStyle();
        String styleBackgroundColor = style.getValue(IDiagramEditorController.BACKGROUND_COLOR_STYLE_ID);
        return Color.web(styleBackgroundColor);
    }

    @Override
    public IViewPackage getModel() {
        return model;
    }

    @Override
    public IViewEntity getViewEntity() {
        return (IViewEntity) model;
    }

    @Override
    public Color getTextColor() {
        IStyle style = model.getStyle();
        String styleColor = style.getValue(IDiagramEditorController.TEXT_COLOR_STYLE_ID);
        return Color.web(styleColor);
    }

    @Override
    public IPackageController getDiagramController() {
    	return diagramController;
    }

// COMMANDS

    @Override
    public void refresh() {
    	setText();
    }

    @Override
    public void loadRelations() {
    }

    @Override
    public final void setText(String s) {
    	/*inutile*/
    }

    @Override
    public void setBackgroundColor(Color c) {
        Contract.check(c != null, "L'argument c ne doit pas être nul.");
        model.addStyle(IDiagramEditorController.BACKGROUND_COLOR_STYLE_ID, c.toString());
    }

    @Override
    public void setTextColor(Color c) {
        Contract.check(c != null, "L'argument c ne doit pas être nul.");
        model.addStyle(IDiagramEditorController.TEXT_COLOR_STYLE_ID, c.toString());
    }
    // TODO setSize
    @Override
    public void setPosition(Point2D value) {
    	Contract.check(value != null, "L'argument value "
                + "ne doit pas être nul.");
        IStyle style = model.getStyle();
        String position = style.getValue(IDiagramEditorController.POSITION_STYLE_ID);
        String[] positionTab = position.split("\\|");

        double tX = Double.valueOf(positionTab[2]);
        double tY = Double.valueOf(positionTab[3]);

        model.addStyle(IDiagramEditorController.POSITION_STYLE_ID,
                value.getX() + "|" + value.getY() + "|"
                + tX + "|" + tY);
        positionProperty().set(value);
    }

    @Override
    public void setTranslatePosition(Point2D value) {
    	 Contract.check(value != null, "L'argument value "
                 + "ne doit pas être nul.");
         IStyle style = model.getStyle();
         String position = style.getValue(IDiagramEditorController.POSITION_STYLE_ID);
         double lX = 0;
         double lY = 0;
         if (position != null) {
             String[] positionTab = position.split("\\|");
             lX = Double.valueOf(positionTab[0]);
             lY = Double.valueOf(positionTab[1]);
         }

         model.addStyle(IDiagramEditorController.POSITION_STYLE_ID,
                 lX + "|" + lY + "|"
                 + value.getX() + "|" + value.getY());

         positionProperty().set(new Point2D(value.getX() + lX, value.getY() + lY));
    }

    // CONTROLLER
    private void createController() {
        final ContextMenu ctxMenu = getContextMenu();
        MenuItem editMI = getEditMI();
        MenuItem removeMI = getRemoveMI();

        // Show context menu.
        packageG.addEventHandler(ContextMenuEvent.CONTEXT_MENU_REQUESTED,
            new EventHandler<ContextMenuEvent>() {
                @Override
                public void handle(ContextMenuEvent event) {
                	packageG.setSelected(true);
                    ctxMenu.show(packageG, event.getScreenX(), event.getScreenY());
                }
            }
        );

        // Show DialogNoteEdit when a double click is detected.
        packageG.addEventFilter(MouseEvent.MOUSE_CLICKED,
                new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (event.getButton() == MouseButton.PRIMARY
                        && event.getClickCount() == 2) {
                    edit();
                }
            }
        });

        editMI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                edit();
            }
        });

        removeMI.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                diagramController.removePackage(model);
            }
        });

        getAddRelationMI().setOnAction(
        		event -> diagramController.createPackageRelation((IViewEntity) model));
        getViewEntity().addPropertyChangeListener(event -> packageG.refresh());

        model.addPropertyChangeListener(IViewPackage.NAME_CHANGED_PROPERTY_NAME, evt -> {
            setText();
        });

        model.addPropertyChangeListener(IViewPackage.PARAMETER_CHANGED_PROPERTY_NAME, evt -> {
            setText();
        });

        model.addPropertyChangeListener(IViewPackage.ENTITY_ADDED_TO_PACKAGE_PROPERTY_CHANGED, evt -> {
            setText();
        });

        //TODO pour le père


        model.addPropertyChangeListener(IViewPackage.STYLE_CHANGED_PROPERTY_NAME, evt -> {
            setStyle((IStyle) evt.getNewValue(), false);
        });
    }

// PRIVATE
    /**Relation
     * Show DialogEditNote to edit NoteGraphic.
     * @param
     */
    private void edit() {
        DialogOPackage dialog = new DialogOPackage(diagramController, model);
        dialog.showAndWait();
    }

    private void setText() {
    	packageG.getText().setText(model.getName());

    	packageG.getTextParam().setText(model.getNameParameter());

    	if (model.getNameParameter().equals("")) {
    		packageG.getRectangleParameter().setVisible(false);
    	} else {
    		packageG.getRectangleParameter().setVisible(true);
    	}

    	packageG.getText().setX(5);
    	packageG.getText().setY(15);
    	packageG.getRectangleTitle().setHeight(packageG.getText().getLayoutBounds().getHeight()
                + 5);
    	packageG.getRectangleTitle().setWidth(packageG.getText().getLayoutBounds().getWidth()
                + 10);

    	packageG.getRectangleParameter().setTranslateX(packageG.getRectangleTitle().getWidth() + 5);
    	packageG.getTextParam().setTranslateX(packageG.getRectangleTitle().getWidth() + 10);

    	packageG.getRectangleParameter().setHeight(packageG.getText().getLayoutBounds().getHeight()
                + 5);
    	packageG.getRectangleParameter().setWidth(packageG.getTextParam().getLayoutBounds().getWidth()
                + 10);

        double height = 200;
        double width = 350;

        if (model.getEntities().size() != 0) {
	        height = height + 20 * model.getEntities().size();
	        width = width + 20 * model.getEntities().size();
        }

        packageG.getRectangle().setHeight(height);
		packageG.getRectangle().setWidth(width /*model.getLevel()*/);
		packageG.autosize();
    }

    private void setStyle(IStyle style, boolean inConstructor) {
    	packageG.getRectangle().setFill(Color.TRANSPARENT);
    	packageG.getRectangleParameter().setFill(Color.WHITE);
    	packageG.getRectangleTitle().setFill(Color.web(IDiagramEditorController.BACKGROUND_COLOR_DEFAULT_ENTITY));
    	packageG.getText().setFill(Color.web(IDiagramEditorController.TEXT_COLOR_DEFAULT));

        String position = style.getValue(IDiagramEditorController.POSITION_STYLE_ID);
        String[] positionTab = position.split("\\|");
        double lX = Double.valueOf(positionTab[0]);
        double lY = Double.valueOf(positionTab[1]);
        double tX = Double.valueOf(positionTab[2]);
        double tY = Double.valueOf(positionTab[3]);

        String size = style.getValue(IDiagramEditorController.SIZE_STYLE_ID);
        if (size == null) {
        	size = packageG.getRectangle().getWidth() + "|" + packageG.getRectangle().getHeight();
        }
        String[] sizeTab = size.split("\\|");
        double w = Double.valueOf(sizeTab[0]);
        double h = Double.valueOf(sizeTab[1]);

        packageG.getRectangle().setWidth(w);
        packageG.getRectangle().setHeight(h);

        packageG.setLayoutX(lX);
        packageG.setLayoutY(lY);
        packageG.setTranslateX(tX);
        packageG.setTranslateY(tY);

        if (inConstructor) {
            positionProperty().setValue(new Point2D(tX + lX, tY + lY));
        }
    }

	@Override
	public void setTextParameter(String s) {
		// TODO Auto-generated method stub
		
	}


}
