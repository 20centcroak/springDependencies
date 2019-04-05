package fr.croak.spring.dependencies.master;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import fr.croak.spring.dependencies.master.slave1.AsSlave1Annotation;
import fr.croak.spring.dependencies.master.slave1.Slave1Input;
import fr.croak.spring.dependencies.master.slave1.Slave1Result;
import fr.croak.spring.dependencies.template.elementarycomponents.ContextInterface;
import fr.croak.spring.dependencies.template.elementarycomponents.Status;
import fr.croak.spring.dependencies.template.toplevelcomponents.MasterServiceInterface;
import fr.croak.spring.dependencies.template.toplevelcomponents.ModelDescriptorBean;
import fr.croak.spring.dependencies.template.toplevelcomponents.ModelDescriptorService;

@Service
public class MasterService extends MasterServiceInterface<Slave1Input, Slave1Result>{

	@Autowired
	private ModelDescriptorService modelDescriptorService;

	@Override
	public List<ModelDescriptorBean> getImplementedModels() {
		return modelDescriptorService.getImplementedModels(AsSlave1Annotation.class);
	}

	public Status getMaster(Slave1Input input, ContextInterface context, Slave1Result result) {
		return updateResult(input, context, result);		
	}
}
