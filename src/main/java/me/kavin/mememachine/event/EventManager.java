package me.kavin.mememachine.event;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodHandles.Lookup;
import java.lang.reflect.Method;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * An Event Management System
 */
public class EventManager {

	// No need to change these
	private ConcurrentHashMap<Class<? extends AbstractEvent>, List<Handler>> registry;
	private final Comparator<Handler> comparator = (h, h1) -> Byte.compare(h.priority, h1.priority);
	private final Lookup lookup = MethodHandles.lookup();
	private static final EventManager defaultInstance = new EventManager();

	/**
	 * Default Event Manager
	 * 
	 * @return
	 */
	public static EventManager getDefault() {
		return defaultInstance;
	}

	/**
	 * Used when making new Event Managers
	 */
	public EventManager() {
		this.registry = new ConcurrentHashMap<>();
	}

	/**
	 * Register handlers from the specified Object(s)
	 * 
	 * @param o
	 */
	public void register(Object... objs) {
		for (Object obj : objs) {
			for (Method m : obj.getClass().getDeclaredMethods()) {
				if (m.getParameterCount() != 1 || !m.isAnnotationPresent(EventHandler.class)) {
					continue;
				}
				Class<? extends AbstractEvent> eventClass = (Class<? extends AbstractEvent>) m.getParameterTypes()[0];
				if (!this.registry.containsKey(eventClass)) {
					this.registry.put(eventClass, new CopyOnWriteArrayList<>());
				}
				this.registry.get(eventClass)
						.add(new Handler(m, obj, m.getDeclaredAnnotation(EventHandler.class).priority()));
				this.registry.get(eventClass).sort(this.comparator);
			}
		}
	}

	/**
	 * Unregister handlers from the specified Object(s)
	 * 
	 * @param objs
	 */
	public void unregister(Object... objs) {
		for (Object obj : objs) {
			for (List<Handler> list : this.registry.values()) {
				for (Handler data : list) {
					if (data.parent != obj) {
						continue;
					}
					list.remove(data);
				}
			}
		}
	}

	/**
	 * Call the Event specified by {@code event} & return it after all handlers have
	 * been called
	 * 
	 * @param event
	 * @return
	 */
	public <E extends AbstractEvent> E call(E event) {
		List<Handler> list = this.registry.get(event.getClass());
		if (!(list == null || list.isEmpty())) {
			for (Handler data : list) {
				if (event.skippingFutureCalls()) {
					break;
				}
				try {
					data.handler.invokeExact(data.parent, event);
				} catch (Throwable e1) {
					e1.printStackTrace();
				}
			}
		}
		return event;
	}

	/**
	 * Not much, just holds data for each Handler
	 *
	 */
	private class Handler {
		private MethodHandle handler;
		private Object parent;
		private byte priority;

		/**
		 * Used to make a new Handler
		 * 
		 * @param method
		 * @param parent
		 * @param priority
		 */
		public Handler(Method method, Object parent, byte priority) {
			if (!method.isAccessible()) {
				method.setAccessible(true);
			}
			MethodHandle m = null;
			try {
				m = lookup.unreflect(method);
			} catch (IllegalAccessException e) {
				e.printStackTrace();
			}
			if (m != null) {
				this.handler = m.asType(
						m.type().changeParameterType(0, Object.class).changeParameterType(1, AbstractEvent.class));
			}
			this.parent = parent;
			this.priority = priority;
		}
	}
}