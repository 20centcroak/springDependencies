package fr.croak.spring.dependencies.master.slave1.sndimpl;

import org.springframework.beans.factory.annotation.Autowired;

import fr.croak.spring.dependencies.template.elementarycomponents.Context;
import fr.croak.spring.dependencies.template.elementarycomponents.TemplateServiceInterface;

public final class Slave1impl2Context extends Context<Slave1Impl2Settings> {
	
	@Autowired
	private Slave1Impl2 slave1Impl2;
	
	public Slave1impl2Context(Slave1Impl2Settings settings) {
		super(settings);
	}

	@Override
	public TemplateServiceInterface getService() {
		return slave1Impl2;
	}
	
	
}
