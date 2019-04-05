package fr.croak.spring.dependencies.template.toplevelcomponents;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * ModelHierarchy defines links between parents and offsprings, allowing
 * dependency injection.
 * 
 * This object should be seen as a construction set of elementary services to
 * plus ones with others.
 *
 */
public class ModelHierarchy {

	private ModelDefinitionInterface parent;
	private List<ModelDefinitionInterface> latestOffsprings = new ArrayList<>();

	/**
	 * defines a new ModelHierarchy with its first level (a unique parent).
	 * 
	 * @param modelDefinition parent definition
	 */
	public ModelHierarchy(ModelDefinitionInterface modelDefinition) {
		parent = modelDefinition;
		latestOffsprings = Collections.singletonList(parent);
	}

	/**
	 * add offspring to the the latest level (latest offspring).
	 * 
	 * Then the previously latest offspring of this hierarchy is added as a parent
	 * of the modelDefinition of this new offspring and the modelDefinition of this
	 * new offpsring is added as an offspring of the modelDefinition of the
	 * previously latest offspring.
	 * 
	 * @param offspring offspring to add
	 */
	public void addOffspring(ModelDefinitionInterface offspring) {
		for (ModelDefinitionInterface newParent : latestOffsprings) {
			newParent.addOffspring(offspring);
			offspring.addParent(newParent);
		}
		latestOffsprings = Collections.singletonList(offspring);
	}

	/**
	 * add a list of offsprings sharing the same parent. They are at the same level.
	 * 
	 * @param offsprings offsprings to add
	 */
	public void addOffsprings(List<ModelDefinitionInterface> offsprings) {
		for (ModelDefinitionInterface offspring : offsprings) {
			addOffspring(offspring);
		}
		latestOffsprings = offsprings;
	}

	/**
	 * add offspring to a parent defined by its model name
	 * 
	 * @param modelName defines parent
	 * @param offspring offspring
	 * @return true if the offspring has been added
	 */
	public boolean addOffspring(String modelName, ModelDefinitionInterface offspring) {

		boolean success = false;
		for (ModelDefinitionInterface parent : latestOffsprings) {
			if (parent.getModelName().equals(modelName)) {
				parent.addOffspring(offspring);
				offspring.addParent(parent);
				success = true;
			}
		}

		return success;
	}

	/**
	*
	 * add offsprings to a parent defined by its  model name
	 * @param modelName	defines parent
	 * @param offsprings			offsprings
	 * @return							true if the offsprings has been added
	 */
	public boolean addOffsprings(String modelName, List<ModelDefinitionInterface> offsprings) {

		boolean success = false;
		for (ModelDefinitionInterface offspring : offsprings) {
			success = addOffspring(modelName, offspring);
		}
		return success;
	}

	/**
	 * @return the parentElement
	 */
	public ModelDefinitionInterface getParent() {
		return parent;
	}

	/**
	 * 
	 * @return the latest offsprings
	 */
	public List<ModelDefinitionInterface> getLatestOffsprings() {
		return latestOffsprings;
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("ModelHierarchy: parent=");
		buildString(builder, parent);
		return builder.toString();
	}

	private static void buildString(StringBuilder builder, ModelDefinitionInterface parent) {
		builder.append(parent.getModelName());
		while (parent.hasOffSpring()) {
			builder.append("->");
			List<ModelDefinitionInterface> offsprings = parent.getOffsprings();
			for (ModelDefinitionInterface offspring : offsprings) {
				parent = offspring;
				buildString(builder, parent);
				builder.append(", ");
			}
		}
	}

}
