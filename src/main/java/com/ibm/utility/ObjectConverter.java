package com.ibm.utility;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

public class ObjectConverter {

	
	public static byte[] serialize(Object obj) {
	    ByteArrayOutputStream out = new ByteArrayOutputStream();
	    ObjectOutputStream os;
		try {
			os = new ObjectOutputStream(out);
			  os.writeObject(obj);
		} catch (IOException e) {
			e.printStackTrace();
		}
	  
	    return out.toByteArray();
	}
	public static Object deserialize(byte[] data)  {
	    ByteArrayInputStream in = new ByteArrayInputStream(data);
	    ObjectInputStream is;
	    Object obj =null;
		try {
			is = new ObjectInputStream(in);
			obj= is.readObject();
		} catch (IOException e) {
			e.printStackTrace();
		}
	    catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
		return obj;
	}
}
