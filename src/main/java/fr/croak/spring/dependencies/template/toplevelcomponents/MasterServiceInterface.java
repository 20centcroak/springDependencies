package fr.croak.spring.dependencies.template.toplevelcomponents;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import fr.croak.spring.dependencies.template.elementarycomponents.ContextInterface;
import fr.croak.spring.dependencies.template.elementarycomponents.Status;

/**
 * MasterServiceInterface defines methods and sert default implementation for top level components.
 * 
 * Top level components manages acces to a specific implementation of a service and its dependencies.
 * It also defines Input(I) and Result(R) of this top level service. The input and result type  should be defined by typing this class. 
 *
 * @param <I>			input type
 * @param <R>		result type
 */
public abstract class MasterServiceInterface<I, R> {

	@Autowired
	private ModelDescriptorService modelDescriptorService;

	/**
	 * retrieve the implementations relative to this top level component.
	 * 
	 * @return description of implementations that could be used with this service.
	 */
	public abstract List<ModelDescriptorBean> getImplementedModels();
	
	/**
	 * get a model definition (set type and name) based on a description of this model.
	 * 
	 * @param description	description (generally retrieve from the getImplementedModels() method.
	 * @return					model definition partially populated with class name and class type.
	 */
	public ModelDefinition getModelDefinition(ModelDescriptorBean description) {
		ModelDefinition definition = new ModelDefinition();
		definition.setModelName(description.getClassName());
		definition.setModelType(description.getType());
		return definition;
	}
	
	/**
	 * create execution context  based on a single model definition
	 * 
	 * a modelHierarchy is built with this unique model definition as a parent. 
	 * @param modelDefinition			model definition to create and initialize the service
	 * @return										the generated key
	 * @throws ServciceException		if the service can't be instanciated or the key can't be generated
	 */
	public ContextInterface createContext(ModelDefinitionInterface modelDefinition) {
		return createContext(new ModelHierarchy(modelDefinition));
	}

	/**
	 * create execution context based on modelHierarchy.
	 * 
	 * It means that the parent service and all dependencies are created and initialized (context are created for each service). 
	 * Context and Service are set in an AsReference object. Dependencies are added to the parent contexts.
	 * A context is generated to call the parent reference when needed. The key/reference pair is set in a map, then when calling
	 * executeService with this key, the associated reference is used to make the computation.
	 * 
	 * @param hierarchy						definition of parent/children links
	 * @return										the associated context
	 * @throws ServciceException		if the service can't be instanciated or the key can't be generated
	 */
	public ContextInterface createContext(ModelHierarchy hierarchy)  {
		return modelDescriptorService.createContexts(hierarchy);
	}
	
	/**
	 * use service. 
	 * 
	 * Retrieve the reference associated with the given key and then call the updateResult method of the elementary service with the given input 
	 * the retrieve context and the given result
	 * 
	 * @param context		execution context
	 * @param input			input for this service
	 * @param result			result to update	
	 * @return					status of the calculation (ok if successfull)
	 */
	public Status updateResult(I input, ContextInterface context, R result) {
		return context.getService().updateResult(input, context, result);
	}

}
