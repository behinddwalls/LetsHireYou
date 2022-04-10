package com.portal.job.context;

import java.util.concurrent.Callable;

/**
 * ThreadLocal container for the {@link RequestContext} object. When spinning a
 * new thread make sure to get the {@link RequestContext} object from the parent
 * thread and populate it in the new thread. This would probably look something
 * like this:
 * 
 * <code>
 * public class RandomCallable implements {@link Callable}<Void> {
 *     private RequestContext currentRequestContext;
 *     public RandomCallable() {
 *         currentRequestContext = RequestContextContainer.getRequestContext();
 *     }
 * 
 * 
 * @author preetam
 * 
 */
public class RequestContextContainer {
	
	private static final ThreadLocal<RequestContext> contexts = new ThreadLocal<RequestContext>();

	/**
	 * returns the current request context associated with the current thread.
	 * 
	 * @return
	 */
	public static RequestContext getRequestContext() {
		return contexts.get();
	}

	/**
	 * Sets the {@link RequestContext} for the current thread.
	 * 
	 * @param context
	 */
	public static void setRequestContext(RequestContext context) {
		contexts.set(context);
	}

	/**
	 * Clear the current {@link RequestContext}.
	 * 
	 */
	public static void clearRequestContext() {
		contexts.remove();
	}
}
