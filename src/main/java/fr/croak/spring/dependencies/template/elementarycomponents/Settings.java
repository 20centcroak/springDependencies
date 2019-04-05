package fr.croak.spring.dependencies.template.elementarycomponents;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * AsSettings is the interface to be used to create specific Setting classes. It contains default methods to retrieve the 
 * fields defined in a specific implementation of this interface. Then user can be aware of what is expected when initializing a relative service.
 *
 */
public interface Settings {
	
	/**
	 * get Fields composing the settings.
	 * 
	 * @return	map with field name as key and filed type as value
	 */
	default Map<String, Class<?>> getFields(){
		Field[] fields = this.getClass().getFields();
	    Map<String, Class<?>> map = new HashMap<>();
	    for(Field f : fields) {
	            map.put(f.getName(),f.getType());
	    }
	    
	    return map;
	}
	
	/**
	 * return fields with a given type.
	 * 
	 * @param type	type of the fields to return
	 * @return			list of fields of the given type composing the settings.
	 */
	default List<Field> getFieldsOfType(Class<?> type){
		Field[] fields = this.getClass().getFields();
	    List<Field> fieldList= new ArrayList<>();
	    for(Field f : fields) {
	    	if (f.getType().isAssignableFrom(type)) {
	    		fieldList.add(f);
	    	}
	    }	    
	    return fieldList;
	}
	
}
