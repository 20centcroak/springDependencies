package fr.croak.spring.dependencies.template.elementarycomponents;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;

import fr.croak.spring.dependencies.template.toplevelcomponents.ModelDescriptorService;

/**
 * AsContext defines implementations of methods defined by the AsContextInterface 
 * to be reused when extending this abstract class. Context is built thanks to Settings. 
 * The settings type  should be defined by typing this class. 
 *
 * @param <S> type of settings used to build this object.
 */
public abstract class Context<S extends Settings> implements ContextInterface{

	@Autowired
	ModelDescriptorService modelDescriptorService;
	
	private S settings;
	private List<Reference> dependencies = new ArrayList<>();

	/**
	 * The default constructor set the settings fields. 
	 * 
	 * @param settings	settings used to build this object
	 */
	public Context(S settings) {
		this.settings = settings;		
	}

	@Override
	public S getSettings() {
		return settings;
	}

	@Override
	public Reference getDependency(Class<? extends Annotation> modelType) {
		for(Reference dependency: dependencies) {
			if(dependency.getModelType().equals(modelType)) {
				return dependency;
			}
		}
		return null;
	}
	
	@Override
	public List<Reference> getDependencies() {
		return dependencies;
	}

	@Override
	public void addDependency(Reference reference) {
		dependencies.add(reference);		
	}

}
