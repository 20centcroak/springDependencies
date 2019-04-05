package fr.croak.spring.dependencies.template.toplevelcomponents;

import java.lang.annotation.Annotation;

/**
 *	ModelDescriptorBean retrieve information about a given implementation and concatenate the useful details.
 *
 */
public class ModelDescriptorBean {
	
	private String beanName;
	private String className;
	private String description;
	private int id;
	private Class<? extends Annotation> type;
	
	/**
	 * @return the type (based on the annotation used to define the implementation)
	 */
	public Class<? extends Annotation> getType() {
		return type;
	}
	/**
	 * @return the beanName (class name starting with a minuscule letter)
	 */
	public String getBeanName() {
		return beanName;
	}
	/**
	 * @param beanName the beanName to set
	 */
	public void setBeanName(String beanName) {
		this.beanName = beanName;
	}
	/**
	 * @return the className
	 */
	public String getClassName() {
		return className;
	}
	/**
	 * @param className the className (equivalent to model name) to set
	 */
	public void setClassName(String className) {
		this.className = className;
	}
	/**
	 * @return the description
	 */
	public String getDescription() {
		return description;
	}
	/**
	 * @param description the description to set
	 */
	public void setDescription(String description) {
		this.description = description;
	}
	/**
	 * @return the id
	 */
	public int getId() {
		return id;
	}
	/**
	 * @param id the id to set
	 */
	public void setId(int id) {
		this.id = id;
	}
	public void setType(Class<? extends Annotation> type) {
		this.type = type;
		
	}

	
	
}
