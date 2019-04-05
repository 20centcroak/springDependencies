package fr.croak.spring.dependencies.master.slave1.firstimpl;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.croak.spring.dependencies.master.slave1.AsSlave1Annotation;
import fr.croak.spring.dependencies.master.slave1.Slave1Input;
import fr.croak.spring.dependencies.master.slave1.Slave1Result;
import fr.croak.spring.dependencies.master.slave2.AsSlave2Annotation;
import fr.croak.spring.dependencies.master.slave2.Slave2Input;
import fr.croak.spring.dependencies.master.slave2.Slave2Result;
import fr.croak.spring.dependencies.template.elementarycomponents.Reference;
import fr.croak.spring.dependencies.template.elementarycomponents.Status;
import fr.croak.spring.dependencies.template.elementarycomponents.TemplateService;
import fr.croak.spring.dependencies.template.elementarycomponents.TemplateServiceInterface;

@Service
@AsSlave1Annotation
public class Slave1Impl1 extends TemplateService<Slave1Input, Slave1Impl1Settings, Slave1Impl1Context, Slave1Result>{
	
	private static final Class<? extends Annotation> SLAVE2 = AsSlave2Annotation.class; 
	
	@Override
	protected Slave1Impl1Context createMyContext(Slave1Impl1Settings settings) {
		return new Slave1Impl1Context(settings);
	}
	
	@Override
	public Slave1Impl1Settings createSettings() {
		return new Slave1Impl1Settings();
	}
	
	@Override
	public List<Class<? extends Annotation>> getExpectedDependencies() {		
		List<Class<? extends Annotation>> dependencyTypes= new ArrayList<>();
		dependencyTypes.add(SLAVE2);
		return dependencyTypes;
	}

	@Override
	public Status updateMyResult(Slave1Input input, Slave1Impl1Context context, Slave1Result result) {
		
		Slave2Input input2 = new Slave2Input(); //some are auto-generated, others come from input
		Slave2Result result2 = new Slave2Result();
		Reference serviceReference2 = context.getDependency(AsSlave2Annotation.class);
		TemplateServiceInterface service = serviceReference2.getService();
		service.updateResult(input2, serviceReference2.getContext(), result2);
		
		StringBuilder stringResult = new StringBuilder();
		stringResult.append("***************************** RESULT **************************************\n");
		stringResult.append(input.getInput1());
		stringResult.append(", I'm the ");
		stringResult.append(context.getSettings().getArg1());
		stringResult.append(" and I call ");
		stringResult.append(result2.getResultString());
		stringResult.append("\n---------------------------------------------------------------------------------------------------------------");
		result.setResultString(stringResult.toString());
		
		return Status.OK;
	}

}
