package es.ucm.fdi.dalgs.classes;


import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.SortedMap;



public class CharsetString {
	
	
	
	public CharsetString() {
		super();
	}

	@SuppressWarnings("rawtypes")
	public List<String> ListCharsets() {
		List<String> listCharsets = new ArrayList<String>();
		SortedMap charsets = Charset.availableCharsets();
		Set names = charsets.keySet();
		for (Iterator e = names.iterator(); e.hasNext();) {
			String name = (String) e.next();
			Charset charset = (Charset) charsets.get(name);
			listCharsets.add(charset.displayName());
		}
		return listCharsets;
	}




	

}
