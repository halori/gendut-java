package org.gendut.arithmetic;

class Messages {

	public static String getString(String key, Object ... objects) {
		String objectsAsString = "";
		for (int i = 0; i < objects.length; i++) {
			if (i > 0)
				objectsAsString += ',';
			objectsAsString += objects[i].toString();
		}
		return key+": "+objectsAsString;
	}

}
