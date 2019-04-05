package fr.croak.spring.dependencies.template.elementarycomponents;

import java.lang.annotation.Annotation;
import java.util.List;

public interface TemplateServiceInterface {

	/**
	 * createContext creates the context to be used by this implementation, based on settings.
	 *
	 * @param settings	 initialization settings
	 * @return				generated context
	 */
	public ContextInterface createContext(Settings settings);
	
	/**
	 * create a new settings object that could be used to retrieve the expected information. Use {@link Settings .getFields} to
	 * retrieve these fields.
	 * 
	 * @return	Settings
	 */
	public abstract Settings createSettings();
	
	/**
	 * compute and update the result thanks to the input parameters and the context representing the initialization of the service.
	 * 
	 * @param input				input parameters for the calculation
	 * @param asContext	execution context (specific behaviour due to the initialization)
	 * @param result				result to be updated
	 * @return						Status (ok if sucessfull)
	 */
	public Status updateResult(Object input, ContextInterface asContext, Object result);
	
	/**
	 * return the expected dependency types. These dependency types should be associated with AsReferences in the context via the method 
	 * addDependency.
	 * 
	 * @return	expected dependency types
	 */
	public List<Class<? extends Annotation>> getExpectedDependencies();
	
}
