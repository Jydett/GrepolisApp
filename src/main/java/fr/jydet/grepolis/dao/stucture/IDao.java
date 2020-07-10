package fr.jydet.grepolis.dao.stucture;

import fr.jydet.grepolis.model.Identifiable;

import java.io.Serializable;

public interface IDao<Id extends Serializable, T extends Identifiable<Id>> {
}
