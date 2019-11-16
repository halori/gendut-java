package org.gendut.func;

// !Chunk for Lazy-Evalution
/* <literate> */
/**
 * Lazy-value.
 */
@SuppressWarnings({ "unchecked", "rawtypes" })
public class LazyValue<T> {

	private volatile Function fun; // r/w barrier
	private Object value;

	public <A> LazyValue(Function<? super A, ? extends T> f, A arg) {
		this.value = arg;
		this.fun = f; // write barrier!!!
	}

	private final static class ExceptionWrapper {
		public Throwable exception;
	}

	/**
	 * Computes the value.
	 */
	final public T value() {
		if (fun != null) // read barrier!!!
		{
			synchronized (this) {
				if (fun != null) {
					Object v = value;
					value = null;
					try {
						value = fun.get(v);
					} catch (Throwable e) {
						value = new ExceptionWrapper();
						((ExceptionWrapper) value).exception = e;
					}
					fun = null; // write barrier!!!
				} // `if`
			} // `synchronized
		} // `if`
		if (value != null && value.getClass() == ExceptionWrapper.class) {
			doThrow(((ExceptionWrapper) value).exception);
		}
		return (T) value;
	}

	private static void doThrow(Throwable e) {
		if (e instanceof Error)
			throw (Error) e;
		else if (e instanceof RuntimeException)
			throw (RuntimeException) e;
		else
			throw new IllegalStateException("Unknown exception type", e);

	}
}// `class`
