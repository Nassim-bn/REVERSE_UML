/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package fr.univrouen.umlreverse.model.diagram.common;

import java.io.Serializable;

/**
 * Interface for all entities.
 */
public interface IEntity extends IObservable, Serializable, IStylizable {

    /**
     * getDiagram Getter that permits to give a diagram
     * @return IDiagram
     */
    IDiagram getDiagram();

    /**
     * getId Getter that permits to give the id of entities and relations
     * @return String
     */
    String getId();

    /**
     * getStyleId Getter that permits to give the id of the style
     * @return String
     */
    String getStyleId();
}
