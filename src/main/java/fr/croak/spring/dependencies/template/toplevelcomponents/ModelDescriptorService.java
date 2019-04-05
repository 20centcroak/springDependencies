package fr.croak.spring.dependencies.template.toplevelcomponents;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.ClassPathScanningCandidateComponentProvider;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.core.type.filter.AnnotationTypeFilter;
import org.springframework.stereotype.Service;

import fr.croak.spring.dependencies.template.elementarycomponents.ContextInterface;
import fr.croak.spring.dependencies.template.elementarycomponents.Reference;
import fr.croak.spring.dependencies.template.elementarycomponents.TemplateServiceInterface;

/**
 * ModelDescriptorService offers convenient methods to retrieve information about model implementations and allow service creation and initialization.
 * 
 * It also manages hierarchy to inject dependencies in parent context. 
 *
 */
@Service
public class ModelDescriptorService {

	@Autowired
	private ApplicationContext applicationContext;

	/**
	 * retrieve implemented model corresponding to a given annotation.
	 * 
	 * @param searchedClass			model type to return
	 * @return									implementation corresponding to the given model type
	 */
	public List<ModelDescriptorBean> getImplementedModels(Class<? extends Annotation> searchedClass) {
		ClassPathScanningCandidateComponentProvider scanner = new ClassPathScanningCandidateComponentProvider(false);
		scanner.addIncludeFilter(new AnnotationTypeFilter(searchedClass));

		List<ModelDescriptorBean> modelList = new ArrayList<>();

		int id = 0;
		for (BeanDefinition bd : scanner.findCandidateComponents("com.airbus.as")) {
			ModelDescriptorBean description = getDescription(searchedClass, bd.getBeanClassName(), id++);
			modelList.add(description);
		}

		return modelList;
	}

	/**
	 * create contexts based on a modelHierarchy. Then it creates parent service and all cascading dependency services, initialize them, build reference
	 * and inject these references in parent contexts.. Finally it returns the main parent reference.
	 * 
	 * @param hierarchy	definition of relationships between parents and dependencies.
	 * @return	reference to main parent
	 */
	public ContextInterface createContexts(ModelHierarchy hierarchy) {

		List<ModelDefinitionInterface> offsprings = hierarchy.getLatestOffsprings();
		if (offsprings.isEmpty()) {
			// no offspring in hierarchy
			return getService(hierarchy.getParent()).getContext();
		}

		for (ModelDefinitionInterface offspring : offsprings) {
			List<Reference> parents = createContexts(offspring);
			for (ModelDefinitionInterface parent : offspring.getParents()) {
				if (!parent.hasParent()) {
					return parents.get(0).getContext();
				}
			}
		}
		return null;
	}
	
	/**
	 * retrieve service based on its name.
	 * 
	 * @param beanName name of the service (class name starting with a minuscule letter)
	 * @return	Service interface
	 */
	public TemplateServiceInterface getService(String beanName) {
		ConfigurableListableBeanFactory clbf = ((AbstractApplicationContext) applicationContext)// KESACO ???
				.getBeanFactory();
		return (TemplateServiceInterface) clbf.getSingleton(beanName);
	}
	
	/**
	 * biuld ModelDescriptorBean thanks to the model name, type and id.
	 * 
	 * @param searchedClass		model type based on its annotation
	 * @param modelName		model name (class name)
	 * @param id							id
	 * @return								model descriptor bean
	 */
	public ModelDescriptorBean getDescription(Class<? extends Annotation> searchedClass, String modelName, int id) {
		int firstIndex = modelName.lastIndexOf('.') + 1;
		String firstLetter = modelName.substring(firstIndex, firstIndex + 1);
		firstLetter = firstLetter.toLowerCase();
		String rest = modelName.substring(firstIndex + 1);

		ModelDescriptorBean description = new ModelDescriptorBean();
		description.setType(searchedClass);
		description.setBeanName(firstLetter + rest);
		description.setClassName(modelName.substring(firstIndex));
		description.setDescription(""); // TODO get description from annotation

		return description;
	}

	private List<Reference> createContexts(ModelDefinitionInterface offspring) {
		Reference offspringReference = getService(offspring);

		List<ModelDefinitionInterface> parents = offspring.getParents();

		if (parents.isEmpty()) {
			return Collections.<Reference>emptyList();
		}

		List<Reference> references = getServices(parents);
		for (Reference reference : references) {
			reference.getContext().addDependency(offspringReference);
		}

		return references;
	}

	private List<Reference> getServices(List<ModelDefinitionInterface> modelDefinitions) {

		List<Reference> references = new ArrayList<>();
		for (ModelDefinitionInterface modelDefinition : modelDefinitions) {
			Reference reference = getService(modelDefinition);
			if (reference != null) {
				references.add(reference);
			}
		}
		return references;
	}

	private Reference getService(ModelDefinitionInterface modelDefinition) {

		List<ModelDescriptorBean> models = getImplementedModels(modelDefinition.getAnnotation()); // if costy, then
																									// supply the models
																									// and scan just
																									// once

		for (ModelDescriptorBean model : models) {
			if (model.getClassName().equalsIgnoreCase(modelDefinition.getModelName())) {
				TemplateServiceInterface service = getService(model.getBeanName());
				ContextInterface context = service.createContext(modelDefinition.getSettings());
				Reference reference = new Reference(service, context, modelDefinition.getModelName(),
						modelDefinition.getAnnotation());
				return reference;
			}
		}

		return null;
	}

}
