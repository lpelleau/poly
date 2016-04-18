package ca.polymtl.inf4410.tp1.server;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import ca.polymtl.inf4410.tp1.shared.FileDir;

/**
 * Represent a directory and contain files.
 */
public class Directory implements Serializable {
	private static final long serialVersionUID = -1695391816525400821L;

	private Map<String, FileDir> files = new HashMap<>(); // Dictionnary of the
															// files

	/**
	 * Add a file in the directory.
	 */
	public boolean createFile(String name) {
		if (!files.containsKey(name)) {
			files.put(name, new FileDir(name));
			return true;
		}
		return false;
	}

	/**
	 * Return a string with all toString of the files and the number of files.
	 */
	public String listDir() {
		String res = "";
		int count = 0;
		Iterator<Entry<String, FileDir>> it = files.entrySet().iterator();
		while (it.hasNext()) {
			res += ((Map.Entry<String, FileDir>) it.next()).getValue()
					.toString();
			count++;
		}
		res += count + " fichier(s)";
		return res;
	}

	/**
	 * Retrieve a file from the directory.
	 */
	public FileDir getFile(String name) {
		FileDir res = null;
		for (Entry<String, FileDir> f : files.entrySet()) {
			res = (f.getValue().getName().equals(name)) ? f.getValue() : null;
		}
		return res;
	}

	/**
	 * retrieve all files from the directory.
	 */
	public List<FileDir> getFiles() {
		List<FileDir> res = new ArrayList<FileDir>();
		for (Entry<String, FileDir> f : files.entrySet()) {
			res.add(f.getValue());
		}
		return res;
	}
}
