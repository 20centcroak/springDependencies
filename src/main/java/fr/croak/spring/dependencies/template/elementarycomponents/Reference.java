package fr.croak.spring.dependencies.template.elementarycomponents;

import java.lang.annotation.Annotation;

/**
 * Reference a Service and its context. It also defines a key to be used  to access a initialized implementation. This key is returned
 * when opening the service.
 *
 */
public class Reference {
			
		private TemplateServiceInterface service;
		private ContextInterface context;
		private String modelName;
		private Class<? extends Annotation> modelType;
		
		/**
		 * the contructor sets the service, context, model name and model type.
		 * 
		 * @param service				service to be referenced
		 * @param context				context to be associated with this service. The context is created thanks the method createContext of the given service.
		 * @param modelName	model name (class name)
		 * @param modelType		model type (annotation name)
		 */
		public Reference(TemplateServiceInterface service, ContextInterface context, String modelName, Class<? extends Annotation> modelType) {
			this.service = service;
			this.context = context;
			this.modelName = modelName;
			this.modelType = modelType;
		}
		/**
		 * @return the service
		 */
		public TemplateServiceInterface getService() {
			return service;
		}
		
		/**
		 * @return the context
		 */
		public ContextInterface getContext() {
			return context;
		}
		/**
		 * @return the modelName
		 */
		public String getModelName() {
			return modelName;
		}
		/**
		 * @return the mode ltype
		 */
		public Class< ? extends Annotation> getModelType() {
			return modelType;
		}

	
}