package fr.univrouen.umlreverse.model.diagram.clazz.importXMI;

import static fr.univrouen.umlreverse.util.Util.toRGBCode;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.Attributes;
import org.xml.sax.ContentHandler;
import org.xml.sax.ErrorHandler;
import org.xml.sax.InputSource;
import org.xml.sax.SAXException;
import org.xml.sax.XMLReader;
import org.xml.sax.helpers.DefaultHandler;

import fr.univrouen.umlreverse.UmlReverseBeans;
import fr.univrouen.umlreverse.model.diagram.clazz.view.ClassDiagram;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IClassDiagram;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewEntity;
import fr.univrouen.umlreverse.model.diagram.clazz.view.IViewPackage;
import fr.univrouen.umlreverse.model.diagram.clazz.view.ViewEntity;
import fr.univrouen.umlreverse.model.diagram.clazz.view.ViewPackage;
import fr.univrouen.umlreverse.model.diagram.clazz.view.ViewRelation;
import fr.univrouen.umlreverse.model.diagram.common.Direction;
import fr.univrouen.umlreverse.model.diagram.common.IDiagram;
import fr.univrouen.umlreverse.model.diagram.common.IRelation;
import fr.univrouen.umlreverse.model.diagram.common.TypeHeadArrow;
import fr.univrouen.umlreverse.model.diagram.common.TypeLineArrow;
import fr.univrouen.umlreverse.model.diagram.packag.PackageDiagram;
import fr.univrouen.umlreverse.model.diagram.sequence.IObject;
import fr.univrouen.umlreverse.model.diagram.sequence.ISegment;
import fr.univrouen.umlreverse.model.diagram.sequence.ISegmentCommon;
import fr.univrouen.umlreverse.model.diagram.sequence.ISegmentRef;
import fr.univrouen.umlreverse.model.diagram.sequence.ISequenceDiagram;
import fr.univrouen.umlreverse.model.diagram.sequence.SequenceDiagram;
import fr.univrouen.umlreverse.model.diagram.sequence.Object;
import fr.univrouen.umlreverse.model.diagram.sequence.RelationToObject;
import fr.univrouen.umlreverse.model.diagram.sequence.Segment;
import fr.univrouen.umlreverse.model.diagram.sequence.Segment.SegmentType;
import fr.univrouen.umlreverse.model.diagram.sequence.SegmentCommon;
import fr.univrouen.umlreverse.model.diagram.sequence.SegmentRef;
import fr.univrouen.umlreverse.model.diagram.sequence.SegmentWithElse;
import fr.univrouen.umlreverse.model.diagram.usecase.Actor;
import fr.univrouen.umlreverse.model.diagram.usecase.IEntityUsecase;
import fr.univrouen.umlreverse.model.diagram.usecase.Usecase;
import fr.univrouen.umlreverse.model.diagram.usecase.UsecaseDiagram;
import fr.univrouen.umlreverse.model.io.parser.util.log.Logger;
import fr.univrouen.umlreverse.model.project.Argument;
import fr.univrouen.umlreverse.model.project.Attribute;
import fr.univrouen.umlreverse.model.project.IAttribute;
import fr.univrouen.umlreverse.model.project.IMethod;
import fr.univrouen.umlreverse.model.project.IObjectEntity;
import fr.univrouen.umlreverse.model.project.IType;
import fr.univrouen.umlreverse.model.project.Method;
import fr.univrouen.umlreverse.model.project.Modifier;
import fr.univrouen.umlreverse.model.project.ObjectEntity;
import fr.univrouen.umlreverse.model.project.Relation;
import fr.univrouen.umlreverse.model.project.Type;
import fr.univrouen.umlreverse.model.project.TypeEntity;
import fr.univrouen.umlreverse.model.project.Variable;
import fr.univrouen.umlreverse.model.project.Visibility;
import fr.univrouen.umlreverse.model.util.RefusedAction;
import fr.univrouen.umlreverse.ui.component.common.relation.type.RelationTypeEnum;
import fr.univrouen.umlreverse.ui.view.common.IDiagramEditorController;
import javafx.geometry.Point2D;

public class ImportXMI {
	private InputSource is;
	private IDiagram diag;
	private String name;
	
	public ImportXMI(InputSource is, String name) {
		this.is = is;
		this.name = name;
	}


	public IDiagram build() throws Exception {
		SAXParserFactory spf = SAXParserFactory.newInstance();
		SAXParser sp = spf.newSAXParser();
		XMLReader xr = sp.getXMLReader();
		HandlerImpl handler = new HandlerImpl();
		xr.setContentHandler(handler);
		xr.setErrorHandler(handler);
		try {
			xr.parse(is);
		} catch (Exception e) {
			throw e;
		}
		return diag;
	}
	
	private class HandlerImpl extends DefaultHandler 
					implements ContentHandler, ErrorHandler {
		
		private IViewEntity entity;
		
		private IObjectEntity entityObject;
		
		private IObjectEntity source;
		
		private IObjectEntity destination;
		
		private IEntityUsecase sourceUC;
		
		private IEntityUsecase destinationUC;
		
		private RelationTypeEnum relationType;
		
		private Attribute attribute;
		
		private Method method;
		
		private String direction;
		
		private String idAttr;
		
		private String nameObject;
		
		private List<Argument> arguments = new ArrayList<>();
		
		private Map<String, IObjectEntity> relations = new HashMap<>();
		
		private Map<String, IObjectEntity> objects = new HashMap<>();

		private Map<String, IEntityUsecase> usecases = new HashMap<>();
		
		private Map<String, String> linkType = new HashMap<>();
		private Map<String, String> generalizationType = new HashMap<>();
		
		private Map<String, String> lowerValueSource = new HashMap<>();
		private Map<String, String> upperValueSource = new HashMap<>();
		private Map<String, String> lowerValueDest = new HashMap<>();
		private Map<String, String> upperValueDest = new HashMap<>();
		
		private int i;
		private int j;
		
		private boolean isDone;
		private String idAssoc;

		private String nameAssoc;

		private IViewPackage packag;

		private IEntityUsecase usecaseEntity;
		private Map<String, IEntityUsecase> relationsUC = new HashMap<>();

		private Map<String,String> lifelineName = new HashMap<>();

		private Map<String, String> activities  = new HashMap<>();

		private Map<String, String[]> messages = new HashMap<>();

		private Map<String, String> activitiesOrder = new HashMap<>();

		private Map<String, String> messagesActivities = new HashMap<>();

		private Map<String, String> messageName = new HashMap<>();

		private Map<String, String> messageType = new HashMap<>();

		private SegmentCommon segment;

		private Map<SegmentCommon, String> segments = new LinkedHashMap<>();
		
		private List<IObject> objectsSeq = new ArrayList<>();

		private Map<String, SegmentCommon> segmentRelations = new HashMap<>();
		
		private Map<SegmentCommon, SegmentCommon> fathers = new HashMap<>();
		
		private boolean segmentsDone = false;
		
		public void characters(char[] ch, int start, int length) 
				throws SAXException {
			String buf = new String(ch, start, length);
		
			
		}
		public void startDocument() throws SAXException {
		}
		
		public void endElement(String uri, String LocalName, String qName) throws SAXException {
			if (qName.equals("packagedElement")) {
				if (packag != null && entity == null && source == null) {
					packag = packag.getPackage();
				}
				entity = null;
				entityObject = null;
				usecaseEntity = null;
				handleRelation();
				source = null;
				destination = null;
				sourceUC = null;
				destinationUC = null;
				idAssoc = null;
			} else if (qName.equals("ownedAttribute")) {
				attribute = null;
				idAttr = null;
				idAssoc = null;
			} else if (qName.equals("ownedOperation") && diag instanceof ClassDiagram) {
					try {
						method.setArguments(arguments);
					} catch (RefusedAction e) {
						throw new SAXException(e);
					}
				method = null;
			} else if (qName.equals("fragment")) {
				if (segment != null) {
					segment = (SegmentCommon) segment.getParent();
				}
			}
			
		}
		
		public void endDocument() throws SAXException {

		}
		
		public void startElement(String uri, String LocalName, 
				String qName, Attributes attrs) throws SAXException {
			
			if (qName.equals("packagedElement")) {
				entity = handleEntity(attrs);
			} else if (qName.equals("ownedAttribute")) {
				handleAttribute(attrs);
			} else if (qName.equals("ownedOperation")) {
				handleOperation(attrs);
			} else if (qName.equals("memberEnd")) {
				handleRelationEntity(attrs);
			} else if (qName.equals("type")) {
				handleType(attrs);
			} else if (qName.equals("ownedParameter")) {
				handleParameter(attrs);
			} else if (qName.equals("ownedEnd")) {
				handleRelationType(attrs);
			} else if (qName.equals("generalization")) {
				handleGeneratization(attrs);
			} else if (qName.equals("lowerValue")) {
				handleLowerValue(attrs);
			} else if (qName.equals("upperValue")) {
				handleUpperValue(attrs);
			} else if (qName.equals("classifier")) {
				handleObject(attrs);
			} else if (qName.equals("lifeline")) {
				handleLifeline(attrs);
			} else if (qName.equals("fragment")) {
				handleFragment(attrs);
			} else if (qName.equals("message")) {
				handleMessage(attrs);
			}
		}
		
		private void handleMessage(Attributes attrs) {
			String[] strings =  new String[2];
			strings[0] = attrs.getValue("sendEvent");
			strings[1] = attrs.getValue("receiveEvent");
			messages.put(attrs.getValue("xmi:id"), strings);
			messageName.put(attrs.getValue("xmi:id"), attrs.getValue("name"));
			messageType.put(attrs.getValue("xmi:id"), attrs.getValue("messageSort"));
		}
		
		private void handleFragment(Attributes attrs) {
			if (attrs.getValue("xmi:type").equals("uml:MessageOccurrenceSpecification")) {
				activities.put(attrs.getValue("xmi:id"), attrs.getValue("covered"));
				if (!isDone) {
					activitiesOrder.put(attrs.getValue("event"), attrs.getValue("xmi:id"));
					segmentRelations.put(attrs.getValue("event"), segment);
					isDone = true;
				} else {
					isDone = false;
				}
				messagesActivities.put(attrs.getValue("xmi:id"), attrs.getValue("message"));
			} else if (attrs.getValue("xmi:type").equals("uml:CombinedFragment")) {
				//TODO les autres types et bien placé le segment
				SegmentType segmentType = SegmentType.valueOf(attrs.getValue("interactionOperator").toUpperCase());
				SegmentCommon segmentTemp = null;
				if (segmentType.equals(SegmentType.REF)) {
					/*segmentTemp = new SegmentRef(segmentType, (ISequenceDiagram) diag);
					((ISegmentRef) segment).setDiagramContained(diagramOfFullname.get(sequence.getValue()));*/
				} else if (segmentType.equals(SegmentType.ALT)) {
					segmentTemp = new SegmentWithElse(segmentType, (ISequenceDiagram) diag);
				} else {
					segmentTemp = new Segment(segmentType, (ISequenceDiagram) diag);
				}
				
				fathers.put(segmentTemp, segment);
				//	segmentTemp.setParent(segment);
				segment = segmentTemp;
				segments.put(segment, attrs.getValue("covered"));
			}
			
		}
		private void handleLifeline(Attributes attrs) {
			lifelineName.put(attrs.getValue("xmi:id"), attrs.getValue("name"));
			
		}
		
		private void handleObject(Attributes attrs) throws SAXException {
			IObject object = new Object(objects.get(attrs.getValue("xmi:idref")), nameObject, (ISequenceDiagram) diag);
			object.addStyle(IDiagramEditorController.POSITION_STYLE_ID, "0|0|0|50");
			//entityObject = object.getEntity();
			try {
				((SequenceDiagram) diag).addObject(object);
			} catch (RefusedAction e) {
				throw new SAXException(e);
			}
		}
		
		private void handleUpperValue(Attributes attrs) {
			if (upperValueSource.get(idAssoc) != null) {
				upperValueDest.put(idAssoc, attrs.getValue("value"));
			} else {
				upperValueSource.put(idAssoc, attrs.getValue("value"));
			}
			
		}
		
		private void handleLowerValue(Attributes attrs) {
			if (lowerValueSource.get(idAssoc) != null) {
				lowerValueDest.put(idAssoc, attrs.getValue("value"));
			} else {
				lowerValueSource.put(idAssoc, attrs.getValue("value"));
			}
			
		}
		
		private void handleGeneratization(Attributes attrs) {
			idAttr = attrs.getValue("xmi:id");
			generalizationType.put(attrs.getValue("general"), idAttr);
			if (diag instanceof ClassDiagram) {
				relations.put(idAttr, entityObject);
			} else {
				relationsUC.put(idAttr, usecaseEntity);
			}
		}
		
		private void handleRelationType(Attributes attrs) {
			if (diag instanceof ClassDiagram) {
				switch (attrs.getValue("aggregation")) {
					case "shared" : relationType = RelationTypeEnum.AGGREGATION;
					break;
					case "none" : relationType = RelationTypeEnum.ASSOCIATION;
					break;
					case "composite" : relationType = RelationTypeEnum.COMPOSITION;
					break;
				}
			} else {
				if (sourceUC == null) {
					sourceUC = usecases.get(attrs.getValue("type"));
				} else {
					destinationUC = usecases.get(attrs.getValue("type"));
				}
			}
				
			}
		
		private void handleParameter(Attributes attrs) {
			direction = attrs.getValue("direction");
			String name = attrs.getValue("name");
			IType type = Type.getTypefromString("void");
			if (! direction.equals("return")) {
				String dir;
				switch (direction) {
					case "in" :  dir = "In";
					case "out" : dir = "Out";
					case "inout" : dir = "InOut";
					default : dir = "";
				}
				arguments.add(new Argument(Direction.getDirection(dir), type, name, ""));
			}
		}
		
		private void handleType(Attributes attrs) throws SAXException {
			IType type = null;
			if (attrs.getValue("xmi:type").equals("uml:PrimitiveType")) {
				String[] tokens = attrs.getValue("href").split("#");
				type = new Type(tokens[1]);
			} else {
				linkType.put(idAttr, attrs.getValue("xmi:idref"));
			}
			if (attribute != null) {
				try {
					attribute.getVariable().setType(type);
				} catch (RefusedAction e) {
					throw new SAXException(e);
				}
			} else if (method != null) {
				if (direction.equals("return")) {
					method.setReturn(type);
				} else {
					try {
						arguments.get(arguments.size() - 1).setType(type);
					} catch (RefusedAction e) {
						throw new SAXException(e);
					}
				}
			}
			
				
		}
		private void handleRelationEntity(Attributes attrs) {
			if (source == null) {
				source = relations.get(attrs.getValue("xmi:idref"));
				destination = objects.get(linkType.get(attrs.getValue("xmi:idref")));
			}
			
		}
		private void handleOperation(Attributes attrs) throws SAXException {
			if (diag instanceof ClassDiagram) {
				String name = attrs.getValue("name");
				String visibility = attrs.getValue("visibility");
				IType type = Type.getTypefromString("int");
				method = new Method(Visibility.getVisibility(visibility), type, name);
				if (attrs.getValue("isAbstract").equals("true")) {
					method.addModifier(Modifier.Abstract);
				}
				if (attrs.getValue("isStatic") != null && attrs.getValue("isStatic").equals("true")) {
					method.addModifier(Modifier.Static);
				}
				try {
					entity.addMethod(method);
				} catch (RefusedAction e) {
					throw new SAXException(e);
				}
			}
		}
		private void handleAttribute(Attributes attrs) throws SAXException {
			if (!(diag instanceof SequenceDiagram)) {
				if (attrs.getIndex("association") == -1) {
					String name = attrs.getValue("name");
					String visibility = attrs.getValue("visibility");
					IType type = Type.getTypefromString("int");
					attribute = new Attribute(Visibility.getVisibility(visibility), 
							"", new Variable(type, name, ""));
					try {
						entity.addAttribute(attribute);
					} catch (RefusedAction e) {
						throw new SAXException(e);
					}
				} else {
					idAssoc = attrs.getValue("association");
					idAttr = attrs.getValue("xmi:id");
					relations.put(idAttr, entityObject);
				}
			}
		}
		
		private void addEntityToDiagram(IObjectEntity objectEntity, Attributes attrs) throws SAXException {
			try {
				if (diag == null) {
					diag = new ClassDiagram(UmlReverseBeans.getInstance().getProject(), name);
					UmlReverseBeans.getInstance().setDiagram(diag);
					UmlReverseBeans.getInstance().setSaved(false);
				}
				diag.getProject().addEntity(objectEntity);
				entityObject = objectEntity;

				objects.put(attrs.getValue("xmi:id"), entityObject);
				entity = new ViewEntity(objectEntity, (IClassDiagram) diag);
				entity.addStyle(IDiagramEditorController.POSITION_STYLE_ID, (i*150 + 10) + "|" + (j*150 + 10) + "|0|0");
				i++;
				if (i%3 == 0) {
					i = 0;
					j++;
				}
				
				if (packag != null) {
					packag.addEntity(entity);
					entity.setPackage(packag);
				}
			} catch (RefusedAction e) {
				throw new SAXException(e);
			}
		}
		
		private IViewEntity handleEntity(Attributes attrs) throws SAXException {
			
			String name = attrs.getValue("name");
			String visibility = "";
			if (attrs.getValue("visibility") != null) {
				visibility = attrs.getValue("visibility");
				if (visibility.equals("package")) {
					visibility = "default";
				}
			}
			try {
				IObjectEntity objectEntity = null;
				if (attrs.getValue("xmi:type").equals("uml:Class")) {
					if (diag instanceof ClassDiagram) {
						if (attrs.getValue("isAbstract") != null && attrs.getValue("isAbstract").equals("true")) {
							 objectEntity = new ObjectEntity(name, TypeEntity.Abstract, Visibility.getVisibility(visibility));
						} else {
							objectEntity = new ObjectEntity(name, TypeEntity.Clazz, Visibility.getVisibility(visibility));
						}
						addEntityToDiagram(objectEntity, attrs);
					} else {
						entityObject = new ObjectEntity(name, TypeEntity.Clazz, Visibility.getVisibility(visibility));
						objects.put(attrs.getValue("xmi:id"), entityObject);
					}
				} else if (attrs.getValue("xmi:type").equals("uml:Interface")) {
					
					objectEntity = new ObjectEntity(name, TypeEntity.Interface, Visibility.getVisibility(visibility));
					addEntityToDiagram(objectEntity, attrs);
				} else if (attrs.getValue("xmi:type").equals("uml:Enumeration")) {
					 objectEntity = new ObjectEntity(name, TypeEntity.Enumeration, Visibility.getVisibility(visibility));
					 addEntityToDiagram(objectEntity, attrs);
				} else if (attrs.getValue("xmi:type").equals("uml:Association")) {
					relationType = RelationTypeEnum.NORMAL;
					idAssoc = attrs.getValue("xmi:id");
					nameAssoc = attrs.getValue("name");
				} else if (attrs.getValue("xmi:type").equals("uml:Realization")) {
					relationType = RelationTypeEnum.IMPLEMENTATION;
					Relation rel = new Relation(Math.random() + "", objects.get(attrs.getValue("client")), 
							objects.get(attrs.getValue("supplier")), 
							relationType.getHead(),
							relationType.getTail(),
							relationType.getLine());
					diag.getProject().addRelation(rel);
					ViewRelation viewRelation = new ViewRelation(rel, (IClassDiagram) diag);
				}  else if (attrs.getValue("xmi:type").equals("uml:Dependency")) {
					relationType = RelationTypeEnum.DEPENDENCY;
					if (diag instanceof ClassDiagram) {
						Relation rel = new Relation(Math.random() + "", objects.get(attrs.getValue("client")), 
								objects.get(attrs.getValue("supplier")), 
								relationType.getHead(),
								relationType.getTail(),
								relationType.getLine());
						diag.getProject().addRelation(rel);
						ViewRelation viewRelation = new ViewRelation(rel, (IClassDiagram) diag);
					} else {
						Usecase usecase1 = (Usecase) usecases.get(attrs.getValue("client"));
						Usecase usecase2 = (Usecase) usecases.get(attrs.getValue("supplier"));
						usecase1.addRelation(usecase2); 
						usecase1.getRelation(usecase2).setArrowHead(relationType.getHead());
						usecase1.getRelation(usecase2).setArrowTail(relationType.getTail());
						usecase1.getRelation(usecase2).setLineArrow(relationType.getLine());
						usecase1.getRelation(usecase2).setType(relationType);
					}
				} else if (attrs.getValue("xmi:type").equals("uml:Package")) {
					if (diag == null) {
						diag = new PackageDiagram(UmlReverseBeans.getInstance().getProject(), name);
						UmlReverseBeans.getInstance().setDiagram(diag);
						UmlReverseBeans.getInstance().setSaved(false);
					}
					objectEntity = new ObjectEntity(attrs.getValue("name"), TypeEntity.Packag, Visibility.Packaged);
					IViewPackage packag = new ViewPackage(objectEntity, visibility, (IClassDiagram) diag);
					packag.setName(attrs.getValue("name"));
					packag.addStyle(IDiagramEditorController.POSITION_STYLE_ID, (i*150) + "|" + (j*150) + "|0|0");
			        ((PackageDiagram) diag).addPackage(packag);
			        this.packag = packag;
				} else if (attrs.getValue("xmi:type").equals("uml:UseCase")) {
					if (diag == null) {
						diag = new UsecaseDiagram(UmlReverseBeans.getInstance().getProject(), name);
						UmlReverseBeans.getInstance().setDiagram(diag);
						UmlReverseBeans.getInstance().setSaved(false);
					}
					Usecase usecase = new Usecase(attrs.getValue("name"), ((UsecaseDiagram) diag).getRootSystem());
					usecase.addStyle(IDiagramEditorController.POSITION_STYLE_ID, (i*150 + 10) + "|" + (j*150 + 10) + "|0|0");
					usecaseEntity = usecase;
					usecases.put(attrs.getValue("xmi:id"), usecase);
					((UsecaseDiagram) diag).addUsecase(usecase);
					
					i++;
					if (i%3 == 0) {
						i = 0;
						j++;
					}
					
				} else if (attrs.getValue("xmi:type").equals("uml:Actor")) {
					if (diag == null) {
						diag = new UsecaseDiagram(UmlReverseBeans.getInstance().getProject(), name);
						UmlReverseBeans.getInstance().setDiagram(diag);
						UmlReverseBeans.getInstance().setSaved(false);
					}
					Actor actor = new Actor(attrs.getValue("name"), ((UsecaseDiagram) diag).getRootSystem());
					actor.addStyle(IDiagramEditorController.POSITION_STYLE_ID, (i*150 + 10) + "|" + (j*150 + 10) + "|0|0");
					usecaseEntity = actor;
					usecases.put(attrs.getValue("xmi:id"), actor);
					((UsecaseDiagram) diag).addActor(actor);
					i++;
					if (i%3 == 0) {
						i = 0;
						j++;
					}
				} else if (attrs.getValue("xmi:type").equals("uml:Collaboration")) {
					diag = new SequenceDiagram(UmlReverseBeans.getInstance().getProject(), name);
					UmlReverseBeans.getInstance().setDiagram(diag);
					UmlReverseBeans.getInstance().setSaved(false);
				} else if (attrs.getValue("xmi:type").equals("uml:InstanceSpecification")) {
					nameObject = attrs.getValue("name");
				} else if (attrs.getValue("xmi:type").equals("uml:ExecutionEvent") || 
						attrs.getValue("xmi:type").equals("uml:SendOperationEvent")) {
					if (!segmentsDone) {
						for (Entry<SegmentCommon, String> entry : segments.entrySet())  {
							String[] lifeLines = entry.getValue().split(" ");
							IObject src = ((SequenceDiagram) diag).getObject(lifelineName.get(lifeLines[0]));
							IObject end = ((SequenceDiagram) diag).getObject(lifelineName.get(lifeLines[lifeLines.length - 1]));
							entry.getKey().setStart(src);
							entry.getKey().setEnd(end);
							entry.getKey().setCondition("");
							((ISequenceDiagram) diag).addSegment(entry.getKey());
							ISegmentCommon father = fathers.get(entry.getKey());
							if (father != null) {
								entry.getKey().setParent((ISegment) father);
							}
						}
						segmentsDone = true;
					}
					if (activitiesOrder.containsKey(attrs.getValue("xmi:id"))) {
						String name1 = lifelineName.get(activities.get(activitiesOrder.get(attrs.getValue("xmi:id"))));
						String idMess = messagesActivities.get(activitiesOrder.get(attrs.getValue("xmi:id")));
						String[] tabCible = messages.get(idMess);
						String nameSrc;
						String nameDest;
						String nameCible = lifelineName.get(activities.get(tabCible[0]));
						if (nameCible.equals(name1)) {
							nameSrc = name1;
							nameDest =  lifelineName.get(activities.get(tabCible[1]));
						} else {
							nameDest = name1;
							nameSrc =  lifelineName.get(activities.get(tabCible[0]));
						}
						IObject src = ((SequenceDiagram) diag).getObject(nameSrc);
						IObject dest = ((SequenceDiagram) diag).getObject(nameDest);
						IRelation rel = src.addRelation(dest);
						ISegment seg = (ISegment) segmentRelations.get(attrs.getValue("xmi:id"));
						if (seg != null) {
							seg.addRelation(rel);
							((RelationToObject) rel).setSegmentContainer(seg);
						}
						String nameRelation = messageName.get(idMess);
						IMethod newMethod = new Method(new Type("void"), nameRelation);
						((RelationToObject) rel).setMethod(newMethod);
						switch (messageType.get(idMess)) {
						case "asynchCall" :
							((RelationToObject) rel).setType(RelationTypeEnum.ASYNCHRONE);
							break;
						case "synchCall" :
							((RelationToObject) rel).setType(RelationTypeEnum.SYNCHRONE);
							break;
						case "reply" :
							((RelationToObject) rel).setType(RelationTypeEnum.RETURN);
							break;
						}
					}
					//TODO les activités et les segments
				}
					
			} catch (RefusedAction e) {
				throw new SAXException(e);
			}
			if (generalizationType.get(attrs.getValue("xmi:id")) != null) {
				if (diag instanceof ClassDiagram) {
					Relation rel = new Relation(Math.random() + "", relations.get(generalizationType.get(attrs.getValue("xmi:id"))), 
							entityObject, 
							relationType.getHead(),
							relationType.getTail(),
							relationType.getLine());
					diag.getProject().addRelation(rel);
					ViewRelation viewRelation = new ViewRelation(rel, (IClassDiagram) diag);
				} else {
					Usecase usecase1 = (Usecase) relationsUC.get(generalizationType.get(attrs.getValue("xmi:id")));
					Usecase usecase2 = (Usecase) usecaseEntity;
					usecase1.addRelation(usecase2); 
					usecase1.getRelation(usecase2).setArrowHead(relationType.getHead());
					usecase1.getRelation(usecase2).setArrowTail(relationType.getTail());
					usecase1.getRelation(usecase2).setLineArrow(relationType.getLine());
					usecase1.getRelation(usecase2).setType(relationType);
				}
			}
			return entity;
		}
		
		private void handleRelation() {
			 if (source != null) {
				Relation rel = new Relation(Math.random() + "", source, destination, 
						relationType.getHead(),
						relationType.getTail(),
						relationType.getLine());
				if (nameAssoc != null) {
					rel.setNameRelation(nameAssoc);
				}
				if (lowerValueSource.get(idAssoc) != null) {
					rel.setHeadCardinality(lowerValueSource.get(idAssoc) + ".." + upperValueSource.get(idAssoc));
				}
				if (lowerValueDest.get(idAssoc) != null) {
					rel.setTailCardinality(lowerValueDest.get(idAssoc) + ".." + upperValueDest.get(idAssoc));
				}
				diag.getProject().addRelation(rel);
				ViewRelation viewRelation = new ViewRelation(rel, (IClassDiagram) diag);
			}
			if (sourceUC != null) {
				sourceUC.addRelation(destinationUC);
				if (nameAssoc != null) {
					sourceUC.getRelation(destinationUC).setNameRelation(nameAssoc);
				}
			}
		}
	}

	public Logger getLog() {
		return new Logger();
	}
}
