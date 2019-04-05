package com.airbus.as.test.unit.tries;

import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.test.context.junit4.SpringRunner;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import fr.croak.spring.dependencies.CoreApplication;
import fr.croak.spring.dependencies.master.MasterService;
import fr.croak.spring.dependencies.master.slave1.AsSlave1Annotation;
import fr.croak.spring.dependencies.master.slave1.Slave1Input;
import fr.croak.spring.dependencies.master.slave1.Slave1Result;
import fr.croak.spring.dependencies.master.slave1.firstimpl.Slave1Impl1Settings;
import fr.croak.spring.dependencies.master.slave2.AsSlave2Annotation;
import fr.croak.spring.dependencies.master.slave2.firstimpl.Slave2Impl1Settings;
import fr.croak.spring.dependencies.template.elementarycomponents.ContextInterface;
import fr.croak.spring.dependencies.template.elementarycomponents.Settings;
import fr.croak.spring.dependencies.template.elementarycomponents.Status;
import fr.croak.spring.dependencies.template.elementarycomponents.TemplateServiceInterface;
import fr.croak.spring.dependencies.template.toplevelcomponents.ModelDefinition;
import fr.croak.spring.dependencies.template.toplevelcomponents.ModelDefinitionInterface;
import fr.croak.spring.dependencies.template.toplevelcomponents.ModelDescriptorBean;
import fr.croak.spring.dependencies.template.toplevelcomponents.ModelDescriptorService;
import fr.croak.spring.dependencies.template.toplevelcomponents.ModelHierarchy;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = CoreApplication.class)
public class MasterServiceTest {

	@Autowired
	private MasterService masterService;
	
	@Autowired
	private ModelDescriptorService modelDescriptionService;

	@Autowired
	private ResourceLoader resourceLoader;

	private Logger logger = LoggerFactory.getLogger(MasterServiceTest.class);
	
	@Test
	public void callSingleService() {
		
		//defines implementation name and type to be used
		ModelDefinition singleDefinition = new ModelDefinition();
		singleDefinition.setModelName("Slave1Impl1");
		singleDefinition.setModelType(AsSlave1Annotation.class);
		
		//defines settings to initialize service
		Slave1Impl1Settings settings1 = new Slave1Impl1Settings(); 
		settings1.setArg1("Hello"); 
		singleDefinition.setSettings(settings1);
				
		ContextInterface context = masterService.createContext(singleDefinition);
			
		//defines input and result for calculation
		Slave1Input input = new Slave1Input();
		input.setInput1("Hello");
		Slave1Result result = new Slave1Result();
		
		//computation
		Status status = masterService.updateResult(input, context, result);	
		assertTrue(status.equals(Status.OK));
	}

	@Test
	public void callMasterService() throws JsonParseException, JsonMappingException, IOException {

		ModelDefinitionInterface parentDefinition = initializeParentDefinition();
		ModelDefinitionInterface firstDependency = initializeDependencyDefinition();
		ModelHierarchy hierarchy = createParentChildHierarchy(parentDefinition, firstDependency);

		Slave1Input input = new Slave1Input();
		input.setInput1("Hello");
		Slave1Result result = new Slave1Result();

		// create service Contexts
		ContextInterface context  = masterService.createContext(hierarchy);

		masterService.updateResult(input, context, result);
		logger.info(result.getResultString());
		
	}

	@Test
	public void CallMasterServiceWithConfigurationFiles() throws JsonParseException, JsonMappingException, IOException {

		List<ModelDescriptorBean> implementedModels = listParentImplementations();		
		ModelDefinitionInterface parentDefinition = chooseParentImplementation(implementedModels);
		
		List<List<ModelDescriptorBean>> dependencies = listDependencies(modelDescriptionService.getService(implementedModels.get(0).getBeanName()));
		List<ModelDefinitionInterface> dependencyDefinitions = chooseDependencyImplementations(dependencies);
		
		ModelHierarchy hierarchy = createParentChildHierarchy(parentDefinition, dependencyDefinitions.get(0));
		
		Slave1Input input = new Slave1Input();
		input.setInput1("Hello");
		Slave1Result result = new Slave1Result();

		// create service Contexts
		ContextInterface context  = masterService.createContext(hierarchy);

			masterService.updateResult(input, context, result);
			logger.info(result.getResultString());
	}

	private List<ModelDefinitionInterface> chooseDependencyImplementations(List<List<ModelDescriptorBean>> dependencies) throws JsonParseException, JsonMappingException, IOException {
		TemplateServiceInterface dependencyService = modelDescriptionService.getService(dependencies.get(0).get(0).getBeanName());
		ModelDefinition modelDefinition = new ModelDefinition();
		modelDefinition.setModelName(dependencies.get(0).get(0).getClassName());
		modelDefinition.setModelType(dependencies.get(0).get(0).getType());
		Resource resource = resourceLoader.getResource("classpath:masterServiceTest/settings2.json");
		ObjectMapper mapper = new ObjectMapper();
		Settings dependencySettings = mapper.readValue(resource.getInputStream(), dependencyService.createSettings().getClass());
		modelDefinition.setSettings(dependencySettings);
		return Collections.singletonList(modelDefinition);
	}

	private ModelDefinitionInterface chooseParentImplementation(List<ModelDescriptorBean> implementedModels) throws JsonParseException, JsonMappingException, IOException {
		TemplateServiceInterface parentService = modelDescriptionService.getService(implementedModels.get(0).getBeanName());
		ModelDefinition modelDefinition = masterService.getModelDefinition(implementedModels.get(0));
		Resource resource = resourceLoader.getResource("classpath:masterServiceTest/parentSettings.json");
		ObjectMapper mapper = new ObjectMapper();
		Settings parentSettings = mapper.readValue(resource.getInputStream(), parentService.createSettings().getClass());
		modelDefinition.setSettings(parentSettings);
		return modelDefinition;
	}

	private List<List<ModelDescriptorBean>> listDependencies(TemplateServiceInterface parentService) {
		logger.info("list of depency types expected");
		List<Class<? extends Annotation>> dependencyTypes = parentService.getExpectedDependencies();
		List<List<ModelDescriptorBean>> dependencies = new ArrayList<>();
		for(Class<? extends Annotation> dependencyType: dependencyTypes) {
			List<ModelDescriptorBean> implementedDependencies = modelDescriptionService.getImplementedModels(dependencyType);
			implementedDependencies.forEach(service->logger.info(service.getClassName()));
			dependencies.add(implementedDependencies);
		}
		return dependencies;		
	}

	private List<ModelDescriptorBean> listParentImplementations() {		
		logger.info("list of available parent service");
		List<ModelDescriptorBean> implementedModels = masterService.getImplementedModels();		
		implementedModels.forEach(service->logger.info(service.getClassName()));
		return implementedModels;
	}

	private ModelHierarchy createParentChildHierarchy(ModelDefinitionInterface... definitions) {
		// link services
		ModelHierarchy hierarchy = new ModelHierarchy(definitions[0]);
		for (int i = 1; i < definitions.length; i++) {
			hierarchy.addOffspring(definitions[i]);
		}
		return hierarchy;
	}

	private ModelDefinitionInterface initializeDependencyDefinition() {
		// create child service to be injected in context of 1st service
		Slave2Impl1Settings settings2 = new Slave2Impl1Settings();
		settings2.setArg1(" initialized with settings2 ");
		ModelDefinition modelDefinition = new ModelDefinition();
		modelDefinition.setModelName("Slave2Impl1");
		modelDefinition.setModelType(AsSlave2Annotation.class);
		modelDefinition.setSettings(settings2);
		return modelDefinition;
	}

	private ModelDefinitionInterface initializeParentDefinition() {
		// create 1st service
		Slave1Impl1Settings settings1 = new Slave1Impl1Settings(); // or it could be a key/value pair
		settings1.setArg1(" Slave1 service "); // in a configuration file
		ModelDefinition modelDefinition = new ModelDefinition();
		modelDefinition.setModelName("Slave1Impl1");
		modelDefinition.setModelType(AsSlave1Annotation.class);
		modelDefinition.setSettings(settings1);
		return modelDefinition;
	}

}
