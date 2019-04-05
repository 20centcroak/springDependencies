package fr.croak.spring.dependencies.template.toplevelcomponents;

import java.lang.annotation.Annotation;
import java.util.List;

import fr.croak.spring.dependencies.template.elementarycomponents.Settings;

/**
 * ModelDefinitionInterface defines methods to build and exploit a model definition.
 * Model definitions are used to create and initialized services.
 *
 */
public interface ModelDefinitionInterface {

	/**
	 * get model name (class name of a given implementation)
	 * @return model name
	 */
	public String getModelName();
	
	/**
	 * get model type (annotation used to define the implementation).
	 * 
	 * @return model type
	 */
	public Class<? extends Annotation> getAnnotation();
	
	/**
	 * get Settings used to initialize the service (create the context).
	 * 
	 * @return settings
	 */
	public Settings getSettings();
	
	/**
	 * indicates if the model is associated with at least one offspring.
	 * 
	 * @return
	 */
	public boolean hasOffSpring();
	
	/**
	 * indicates if the model is associated with at least one parent.
	 * 
	 * @return
	 */
	public boolean hasParent();
	
	/**
	 * add offspring to this model definition.
	 * 
	 * @param modelDefinition offspring definition
	 */
	public void addOffspring(ModelDefinitionInterface modelDefinition);
	
	/**
	 * add parent to this model definition/
	 * 
	 * @param parent parent definition
	 */
	void addParent(ModelDefinitionInterface parent);
	
	/**
	 * get offsprings associated with this model definition.
	 * 
	 * @return offspings
	 */
	List<ModelDefinitionInterface> getOffsprings();
	
	/**
	 * get offspring based on its type.
	 * @param type	type of offpsring to look for
	 * @return			offpsring
	 */
	ModelDefinitionInterface getOffspring(Class<? extends Annotation> type);
	
	/**
	 * get parent based on its type.
	 * @param type	type of parent to look for
	 * @return
	 */
	ModelDefinitionInterface getParent(Class<? extends Annotation> type);
	
	/**
	 * get parents associated with this model definition.
	 *  
	 * @return parents
	 */
	List<ModelDefinitionInterface> getParents();
	
}
