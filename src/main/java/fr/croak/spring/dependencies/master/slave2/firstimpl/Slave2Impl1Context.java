package fr.croak.spring.dependencies.master.slave2.firstimpl;

import org.springframework.beans.factory.annotation.Autowired;

import fr.croak.spring.dependencies.template.elementarycomponents.Context;
import fr.croak.spring.dependencies.template.elementarycomponents.TemplateServiceInterface;


public class Slave2Impl1Context extends Context<Slave2Impl1Settings>{

	@Autowired
	private Slave2Impl1 slave2Impl1;
	
	public Slave2Impl1Context(Slave2Impl1Settings settings) {
		super(settings);
	}

	@Override
	public TemplateServiceInterface getService() {
		return slave2Impl1;
	}

}
