package com.app.bookstore.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * @author Ananth Shanmugam
 * Primary class to convert dto to entity and entity to dto based on getters and setters
 */
public class TransformerUtil {
	   private static final String PREF_GET = "get";
	    private static final String PREF_IS = "is";
	    private static final String PREF_SET = "set";

		public static Object copyAllUtility(Object source, Object target) {
			Method[] sourcePublicMethods = source.getClass().getMethods();
			Method[] targetPublicMethods = target.getClass().getMethods();
			// exclude lazy workflow table to be initialized
			List<String> excludeMethods = Arrays.asList("getWorkflowInfo", "isEditable");

			try {
				for (int i = 0; i < sourcePublicMethods.length; i++) {
					if (excludeMethods.contains(sourcePublicMethods[i].getName())) {
						continue;
					}
					if (sourcePublicMethods[i].getName().startsWith("get")) {
						if (sourcePublicMethods[i].getParameterTypes().length > 0)
							continue;

						String setterName = "set"
								+ sourcePublicMethods[i].getName().substring(3);
						for (Method pubMethod : targetPublicMethods) {
							if (pubMethod.getName().equals(setterName)) {
								if (pubMethod.getParameterTypes().length != 1)
									continue;
								if (pubMethod.getParameterTypes()[0]
										.equals(sourcePublicMethods[i]
												.getReturnType())) {
									pubMethod.invoke(target, sourcePublicMethods[i]
											.invoke(source, new Object[] {}));
								}
							}
						}
					} else if (sourcePublicMethods[i].getName().startsWith("is")) {
						if (sourcePublicMethods[i].getParameterTypes().length > 0)
							continue;
						String setterName = "set"
								+ sourcePublicMethods[i].getName().substring(2);
						for (Method pubMethod : targetPublicMethods) {
							if (pubMethod.getName().equals(setterName)) {
								if (pubMethod.getParameterTypes().length != 1)
									continue;
								if (pubMethod.getParameterTypes()[0]
										.equals(sourcePublicMethods[i]
												.getReturnType())) {
									pubMethod.invoke(target, sourcePublicMethods[i]
											.invoke(source, new Object[] {}));
								}
							}
						}
					}
				}
			} catch (Exception e) {
				throw new RuntimeException(e);
			}
			return target;
		}
		
		/**
		 * Copy All utility with option to exclude define method(s).
		 * @param source
		 * @param target
		 * @param exclMethods (empty/null for nothing to exclude)
		 */
		public static void copyAllUtility(Object source, Object target, String... exclMethods) {
	        Method[] sourcePublicMethods = source.getClass().getMethods();
	        Method[] targetPublicMethods = target.getClass().getMethods();

	        List<String> excludeMethods = exclMethods != null
	                ? Arrays.asList(exclMethods) : new ArrayList<String>();

	        try {
	            for (int i = 0; i < sourcePublicMethods.length; i++) {
	                String methodName = sourcePublicMethods[i].getName();
	                /*
	                 * bypass any meant to exclude method and method with param +
	                 * non get/is method
	                 */
	                if ( excludeMethods.contains(methodName)
	                        || sourcePublicMethods[i].getParameterTypes().length > 0
	                        || !( methodName.startsWith(PREF_GET)
	                                || methodName.startsWith(PREF_IS) ) ) {
	                    continue;
	                }

	                String setterName = PREF_SET + ( methodName.startsWith(PREF_GET)
	                        ? methodName.substring(3) : methodName.substring(2) );
	                for (Method pubMethod : targetPublicMethods) {
	                    if ( pubMethod.getName().equals(setterName)
	                            && ( pubMethod.getParameterTypes().length == 1 )
	                            && pubMethod.getParameterTypes()[0].equals(
	                                    sourcePublicMethods[i].getReturnType()) ) {
	                        pubMethod.invoke(target, sourcePublicMethods[i]
	                                .invoke(source, new Object[] {}));
	                    }
	                }
	            }
	        } catch (IllegalAccessException | IllegalArgumentException
	                | InvocationTargetException ex) {
	            throw new RuntimeException(ex);
	        }

	    }
		
}
