package fr.croak.spring.dependencies.master.slave2.firstimpl;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.croak.spring.dependencies.master.slave2.AsSlave2Annotation;
import fr.croak.spring.dependencies.master.slave2.Slave2Input;
import fr.croak.spring.dependencies.master.slave2.Slave2Result;
import fr.croak.spring.dependencies.template.elementarycomponents.Status;
import fr.croak.spring.dependencies.template.elementarycomponents.TemplateService;

@Service
@AsSlave2Annotation
public class Slave2Impl1 extends TemplateService<Slave2Input, Slave2Impl1Settings, Slave2Impl1Context, Slave2Result> {

	@Override
	public Slave2Impl1Context createMyContext(Slave2Impl1Settings settings) {
		return new Slave2Impl1Context(settings);
	}

	@Override
	public Slave2Impl1Settings createSettings() {
		return new Slave2Impl1Settings();
	}

	@Override
	public Status updateMyResult(Slave2Input input, Slave2Impl1Context context, Slave2Result result) {
		result.setResultString("slave2 service"+context.getSettings().getArg1());
		return null;
	}

	@Override
	public List<Class<? extends Annotation>> getExpectedDependencies() {
		return Collections.emptyList();
	}
}
