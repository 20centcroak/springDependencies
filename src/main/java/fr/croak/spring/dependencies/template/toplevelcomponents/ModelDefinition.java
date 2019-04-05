package fr.croak.spring.dependencies.template.toplevelcomponents;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import fr.croak.spring.dependencies.template.elementarycomponents.Settings;

/**
 * ModelDefinition concatenates information to be able to build a Service and auto-generate 
 * its context based on the settings.
 * 
 * ModelDefinition allows also to define parents and offsprings associated with this definition.
 *
 */
public class ModelDefinition implements ModelDefinitionInterface{

	private Class<? extends Annotation> annotation;
	private String modelName;
	private Settings settings;
	private List<ModelDefinitionInterface> offsprings = new ArrayList<>();
	private List<ModelDefinitionInterface> parents  = new ArrayList<>();

	/**
	 * @return the modelName
	 */
	@Override
	public String getModelName() {
		return modelName;
	}

	/**
	 * @param modelName the modelName to set
	 */
	public void setModelName(String modelName) {
		this.modelName = modelName;
	}

	/**
	 * @return the settings
	 */
	@Override
	public Settings getSettings() {
		return settings;
	}

	/**
	 * @param settings the settings to set
	 */
	public void setSettings(Settings settings) {
		this.settings = settings;
	}

	/**
	 * @return the annotation
	 */
	@Override
	public Class<? extends Annotation> getAnnotation() {
		return annotation;
	}

	/**
	 * @param annotation the annotation to set
	 */
	public void setModelType(Class<? extends Annotation> annotation) {
		this.annotation = annotation;
	}

	/**
	 * @return the offspring
	 */
	@Override
	public ModelDefinitionInterface getOffspring(Class<? extends Annotation> type) {
		return getModelDefinition(offsprings, type);		
	}

	/**
	 * @param offspring the offspring to set
	 */
	@Override
	public void addOffspring(ModelDefinitionInterface offspring) {
		this.offsprings.add(offspring);
	}

	@Override
	public boolean hasOffSpring() {
		return !offsprings.isEmpty();
	}

	/**
	 * @return the parent
	 */
	@Override
	public ModelDefinitionInterface getParent(Class<? extends Annotation> type) {
		return getModelDefinition(parents, type);		
	}

	/**
	 * @param parent the parent to set
	 */
	@Override
	public void addParent(ModelDefinitionInterface parent) {
		this.parents.add(parent);
	}
	
	@Override
	public boolean hasParent() {
		return !parents.isEmpty();
	}
	
	private static ModelDefinitionInterface getModelDefinition(List<ModelDefinitionInterface>modelDefinitions, Class<? extends Annotation> type) {
		for(ModelDefinitionInterface def: modelDefinitions) {
			if (def.getAnnotation().getClass().getTypeName().equals(type.getTypeName())) {
				return def;
			}
		}
		return null;
	}

	@Override
	public List<ModelDefinitionInterface> getParents() {
		return parents;
	}

	@Override
	public List<ModelDefinitionInterface> getOffsprings() {
		return offsprings;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return "ModelDefinition [modelName=" + modelName + " of type " + annotation + "]";
	}

}
