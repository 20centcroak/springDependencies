package fr.croak.spring.dependencies.template.elementarycomponents;

import java.lang.annotation.Annotation;
import java.util.List;

/**
 *	AsContextInterface defines an execution context for services.
 * AsContextInterface defines the methods to access the settings and dependencies managed by the context. 
 *
 */
public interface ContextInterface {


	/**
	 * getter for settings.
	 * 
	 * @return the settings used to build the context
	 */
	public Settings getSettings();
	
	public TemplateServiceInterface getService();
	
	/**
	 * return the dependency based on its type. (Only 1 dependency by type is allowed).
	 * 
	 * @param modelType	model type
	 * @return	Reference on the dependency, null if the dependency is not found.
	 */
	public Reference getDependency(Class<? extends Annotation> modelType);
	
	/**
	 * return all dependencies defined in this context. An empty list is returned when no dependency has been added.
	 * 
	 * @return references to dependencies.
	 */
	public List<Reference> getDependencies();
	
	/**
	 * add a new dependency. Only 1 dependency per expected type is allowed.
	 * 
	 * @param reference	reference to dependency.
	 */
	public void addDependency(Reference reference);
	
}
