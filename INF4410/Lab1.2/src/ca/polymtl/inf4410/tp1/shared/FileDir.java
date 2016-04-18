package ca.polymtl.inf4410.tp1.shared;

import java.io.IOException;
import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import javax.xml.bind.DatatypeConverter;

/**
 * Class that represent a file. A file is constituted with a name, a content, a
 * locker and a checksum.
 */
public class FileDir implements Serializable {
	private static final long serialVersionUID = 4737411586687066890L;

	private String name;
	private byte[] content = new byte[0];
	private int locker = -1;
	private String checksum;
	private long lockTTL = -1;

	public FileDir(String name) {
		this.name = name;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public byte[] getContent() {
		return content;
	}

	public void setContent(byte[] content) throws NoSuchAlgorithmException,
			IOException {
		this.content = content;
		setChecksum(getFileChecksum());
	}

	public void setLocker(int locker) {
		this.locker = locker;
		lockTTL = new java.util.Date().getTime() + 1000 * 60 * 3; // Add 3
																	// minutes
																	// to the
																	// locker
	}

	public int getLocker() {
		return locker;
	}

	/**
	 * Return true if the file have a locker AND the locker is still valid
	 */
	public boolean isLocked() {
		return locker != -1
				&& (lockTTL == -1 || new java.util.Date().getTime() < lockTTL);
	}

	public void setChecksum(String checksum) {
		this.checksum = checksum;
	}

	public String getChecksum() {
		return checksum;
	}

	/**
	 * Generate the checksum based on the file content.
	 */
	private String getFileChecksum() throws NoSuchAlgorithmException,
			IOException {
		MessageDigest md = MessageDigest.getInstance("MD5");
		md.update(content);
		byte[] digest = md.digest();
		return DatatypeConverter.printHexBinary(digest).toUpperCase();
	}

	public String toString() {
		return " * "
				+ name
				+ ((isLocked()) ? " verouille par " + locker : " non verouille")
				+ "\n";
	}
}
