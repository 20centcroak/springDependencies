package fr.croak.spring.dependencies.master.slave1.sndimpl;

import java.lang.annotation.Annotation;
import java.util.Collections;
import java.util.List;

import org.springframework.stereotype.Service;

import fr.croak.spring.dependencies.master.slave1.AsSlave1Annotation;
import fr.croak.spring.dependencies.master.slave1.Slave1Input;
import fr.croak.spring.dependencies.master.slave1.Slave1Result;
import fr.croak.spring.dependencies.template.elementarycomponents.Status;
import fr.croak.spring.dependencies.template.elementarycomponents.TemplateService;

@Service
@AsSlave1Annotation
public class Slave1Impl2 extends TemplateService<Slave1Input, Slave1Impl2Settings, Slave1impl2Context, Slave1Result> {

	@Override
	public Slave1impl2Context createMyContext(Slave1Impl2Settings settings) {
		return new Slave1impl2Context((Slave1Impl2Settings)settings);
	}

	@Override
	public Slave1Impl2Settings createSettings() {
		return new Slave1Impl2Settings();
	}

	@Override
	public Status updateMyResult(Slave1Input input, Slave1impl2Context context, Slave1Result result) {

		StringBuilder stringResult = new StringBuilder();
		stringResult.append(input.getInput1());
		stringResult.append(context.getSettings().getArg1());
		result.setResultString(stringResult.toString());

		return Status.OK;
	}

	@Override
	public List<Class<? extends Annotation>> getExpectedDependencies() {
		return Collections.emptyList();
	}
}
