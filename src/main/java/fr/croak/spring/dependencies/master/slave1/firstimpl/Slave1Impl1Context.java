package fr.croak.spring.dependencies.master.slave1.firstimpl;

import org.springframework.beans.factory.annotation.Autowired;

import fr.croak.spring.dependencies.template.elementarycomponents.Context;
import fr.croak.spring.dependencies.template.elementarycomponents.TemplateServiceInterface;

public class Slave1Impl1Context extends Context<Slave1Impl1Settings>{

	@Autowired
	private Slave1Impl1 slave1Impl1;
		
	public Slave1Impl1Context(Slave1Impl1Settings settings) {
		super(settings);
	}

	@Override
	public TemplateServiceInterface getService() {
		return slave1Impl1;
	}
	
}
