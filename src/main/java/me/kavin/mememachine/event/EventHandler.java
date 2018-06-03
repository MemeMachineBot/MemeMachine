package me.kavin.mememachine.event;

import static java.lang.annotation.ElementType.METHOD;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

/**
 * Put this over voids to be added to the handler registry
 */
@Retention(RUNTIME)
@Target(METHOD)
public @interface EventHandler {
	/**
	 * The priority of this Event Handler
	 * 
	 * @return
	 */
	byte priority() default EventPriority.NORMAL;
}
