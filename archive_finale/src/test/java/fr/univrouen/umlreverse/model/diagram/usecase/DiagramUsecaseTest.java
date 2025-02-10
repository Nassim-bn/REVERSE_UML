package fr.univrouen.umlreverse.model.diagram.usecase;

import fr.univrouen.umlreverse.model.project.IProject;
import fr.univrouen.umlreverse.model.project.Project;
import fr.univrouen.umlreverse.model.diagram.common.IRelation;
import fr.univrouen.umlreverse.model.diagram.common.Relation;
import org.junit.Before;
import org.junit.Test;
import static org.assertj.core.api.Assertions.assertThat;

import static org.junit.Assert.*;

public class DiagramUsecaseTest {

    private IProject prj;
    private IUsecaseDiagram diagram;
    //private System DefaultGrp, SousGrp;
    //private System UseGrp;
    private IRelation relation;
    private String nameRelation,comment;
    private Usecase source, destination;
    private Actor actor;
    private INoteUsecase note;

    @Before
    public void setUp() throws Exception {
        prj = new Project("a");
        diagram = new UsecaseDiagram(prj, "Diagramme de cas d'utilisation");
        //UseGrp = new System(UUID.randomUUID().toString(), "Group Use Case", diagram);
        //DefaultGrp = new System(UUID.randomUUID().toString(), "Default", diagram);
        nameRelation = "relation1";
        comment="simpleRelation";
        actor = new Actor("Actor1", diagram.getRootSystem());
        source = new Usecase("CaseSource", diagram.getRootSystem());
        destination = new Usecase("CaseDestination", diagram.getRootSystem());
        relation = new Relation(diagram, source, destination);
        note = new NoteUsecase("newNote", diagram);
        //SousGrp = new System(UUID.randomUUID().toString(), "Sous groupe", diagram);
    }
    
    public class BooleanSetter {
        boolean b = false;
        
        public boolean getBoolean() {
            return b;
        }
        public void setBoolean(boolean b) {
            this.b = b;
        }
    }

    @Test
    public void testaddRemoveEntity() throws Exception {
       assertTrue(diagram.getNotes().isEmpty());
        assertTrue(diagram.getRelations().isEmpty());
        assertTrue(diagram.getSystems().isEmpty());
        assertTrue(diagram.getUsecases().isEmpty());

        diagram.addNote(note);
        diagram.addActor(actor);
        diagram.addUsecase(source);
        diagram.addUsecase(destination);
        diagram.addRelation(relation);
        assertTrue(diagram.getNotes().contains(note));
        assertTrue(diagram.getActors().contains(actor));
        assertTrue(diagram.getUsecases().contains(source));
        assertTrue(diagram.getUsecases().contains(destination));
        assertTrue(diagram.getRelations().contains(relation));

        diagram.removeNote(note);
        diagram.removeActor(actor);
        diagram.removeUsecase(source);
        diagram.removeUsecase(destination);
        diagram.removeRelation(relation);
        assertFalse(diagram.getNotes().contains(note));
        assertFalse(diagram.getActors().contains(actor));
        assertFalse(diagram.getUsecases().contains(source));
        assertFalse(diagram.getUsecases().contains(destination));
        assertFalse(diagram.getRelations().contains(relation));
    }
    
    @Test
    public void testListenerNoteAdded() {
        final BooleanSetter noteB = new BooleanSetter(); 

        diagram.addPropertyChangeListener(IUsecaseDiagram.NOTE_ADDED_PROPERTY_NAME, evt -> {
            INoteUsecase noteAdded = (INoteUsecase) evt.getNewValue();
            noteB.setBoolean(note.equals(noteAdded));
        });
        
        diagram.addNote(note);
        
        assertTrue(noteB.getBoolean());
    }
    
    @Test
    public void testListenerActorAdded() {
        final BooleanSetter actorB = new BooleanSetter(); 

        diagram.addPropertyChangeListener(IUsecaseDiagram.ACTOR_ADDED_PROPERTY_NAME, evt -> {
            IActor actorAdded = (IActor) evt.getNewValue();
            actorB.setBoolean(actor.equals(actorAdded));
        });
        
        diagram.addActor(actor);
        
        assertTrue(actorB.getBoolean());
    }
    
    @Test
    public void testListenerUsecaseAdded() {
        final BooleanSetter usecaseB = new BooleanSetter(); 

        diagram.addPropertyChangeListener(IUsecaseDiagram.USECASE_ADDED_PROPERTY_NAME, evt -> {
            IUsecase usecaseAdded = (IUsecase) evt.getNewValue();
            usecaseB.setBoolean(source.equals(usecaseAdded));
        });
        
        diagram.addUsecase(source);
        
        assertTrue(usecaseB.getBoolean());
    }
    
    @Test
    public void testListenerRelationAdded() {
        final BooleanSetter relationB = new BooleanSetter(); 

        diagram.addPropertyChangeListener(IUsecaseDiagram.RELATION_ADDED_PROPERTY_NAME, evt -> {
            IRelation relationAdded = (IRelation) evt.getNewValue();
            relationB.setBoolean(relation.equals(relationAdded));
        });
        
        diagram.addRelation(relation);
        
        assertTrue(relationB.getBoolean());
    }
    
    @Test
    public void testListenerNoteRemoved() {
        final BooleanSetter noteB = new BooleanSetter(); 

        diagram.addPropertyChangeListener(IUsecaseDiagram.NOTE_REMOVED_PROPERTY_NAME, evt -> {
            INoteUsecase noteRemoved = (INoteUsecase) evt.getOldValue();
            noteB.setBoolean(note.equals(noteRemoved));
        });
        
        diagram.addNote(note);
        diagram.removeNote(note);
        
        assertTrue(noteB.getBoolean());
    }
    
    @Test
    public void testListenerActorRemoved() {
        final BooleanSetter actorB = new BooleanSetter(); 

        diagram.addPropertyChangeListener(IUsecaseDiagram.ACTOR_REMOVED_PROPERTY_NAME, evt -> {
            IActor actorRemoved = (IActor) evt.getOldValue();
            actorB.setBoolean(actor.equals(actorRemoved));
        });
        
        diagram.addActor(actor);
        diagram.removeActor(actor);
        
        assertTrue(actorB.getBoolean());
    }
    
    @Test
    public void testListenerUsecaseRemoved() {
        final BooleanSetter usecaseB = new BooleanSetter(); 

        diagram.addPropertyChangeListener(IUsecaseDiagram.USECASE_REMOVED_PROPERTY_NAME, evt -> {
            IUsecase usecaseRemoved = (IUsecase) evt.getOldValue();
            usecaseB.setBoolean(source.equals(usecaseRemoved));
        });
        
        diagram.addUsecase(source);
        diagram.removeUsecase(source);
        
        assertTrue(usecaseB.getBoolean());
    }
    
    @Test
    public void testListenerRelationRemoved() {
        final BooleanSetter relationB = new BooleanSetter(); 

        diagram.addPropertyChangeListener(IUsecaseDiagram.RELATION_REMOVED_PROPERTY_NAME, evt -> {
            IRelation relationRemoved = (IRelation) evt.getOldValue();
            relationB.setBoolean(relation.equals(relationRemoved));
        });
        
        diagram.addRelation(relation);
        diagram.removeRelation(relation);
        
        assertTrue(relationB.getBoolean());
    }
    
    @Test
    public void testRelation() {  
        diagram.addActor(actor);
        actor.addRelation(destination);
        
        assertThat(diagram.getRelations().size()).isEqualTo(1);
    }
    
    @Test
    public void testRemovableRelationWithActor() {  
        diagram.addActor(actor);
        actor.addRelation(destination);
        
        diagram.removeActor(actor);
        assertThat(diagram.getRelations().size()).isEqualTo(0);
    }
    
    @Test
    public void testRemovableRelationWithActorListener() {  
        diagram.addActor(actor);
        actor.addRelation(destination);
        
        BooleanSetter relationB = new BooleanSetter();
        diagram.addPropertyChangeListener(IUsecaseDiagram.RELATION_REMOVED_PROPERTY_NAME, evt -> {
            relationB.setBoolean(true);
        });
        
        diagram.removeActor(actor);
        assertThat(relationB.getBoolean()).isTrue();
    }

    @Test
    public void testGetAddRemoveListActors() throws Exception {
        assertTrue(diagram.getActors().isEmpty());

        diagram.addActor(actor);
        assertTrue(diagram.getActors().contains(actor));

        diagram.removeActor(actor);
        assertFalse(diagram.getActors().contains(actor));
    }

    @Test
    public void testGetAddRemoveListNotes() throws Exception {
        assertTrue(diagram.getNotes().isEmpty());

        diagram.addNote(note);
        assertTrue(diagram.getNotes().contains(note));

        diagram.removeNote(note);
        assertFalse(diagram.getNotes().contains(note));
    }

    @Test
    public void testGetAddRemoveListUseCases() throws Exception {
        assertTrue(diagram.getUsecases().isEmpty());

        diagram.addUsecase(source);
        assertTrue(diagram.getUsecases().contains(source));

        diagram.removeUsecase(source);
        assertFalse(diagram.getUsecases().contains(source));
    }

    @Test
    public void testGetAddRemoveListRelation() throws Exception {/*
        assertTrue(diagram.getListRelation().isEmpty());

        diagram.addRelation(relation);
        assertTrue(diagram.getListRelation().contains(relation));

        diagram.removeRelation(relation);
        assertFalse(diagram.getListRelation().contains(relation));*/
    }
}