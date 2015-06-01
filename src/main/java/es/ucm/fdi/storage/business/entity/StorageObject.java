package es.ucm.fdi.storage.business.entity;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.UUID;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Transient;

import org.apache.commons.io.IOUtils;

@Entity
public class StorageObject {

	@EmbeddedId
	private StorageObjectId id;

	private String mimeType;

	private Long length;

	private String internalName;

	@Transient
	private Path root;

	StorageObject() {
		
	}
	
	public StorageObject(StorageObjectId id, Path root) {
		this(id, null, root);
	}

	public StorageObject(StorageObjectId id, String mimeType, Path root) {
		this.id = id;
		this.mimeType = mimeType;
		this.root = root;
	}

	public StorageObjectId getId() {
		return id;
	}

	void setId(StorageObjectId id) {
		this.id = id;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public Long getContentLength() {
		return length;
	}

	public void setContentLenght(Long length) {
		this.length = length;
	}

	public Path getRootPath() {
		return root;
	}

	public void setRootPath(Path root) {
		this.root = root;
	}

	public void transferTo(OutputStream output) throws IOException {
		try (FileInputStream input = getFile()) {
			IOUtils.copy(input, output);
		}
	}

	private FileInputStream getFile() throws FileNotFoundException {
		String folder = internalName.substring(0, 2);
		return new FileInputStream(root.resolve(folder).resolve(internalName).toFile());
	}

	public void save(InputStream input) throws IOException {
		Path file = root.resolve(UUID.randomUUID().toString());
		try (FileOutputStream out = new FileOutputStream(file.toFile())) {
			MessageDigest sha2sum = MessageDigest.getInstance("SHA-256");

			byte[] dataBytes = new byte[4*1024];

			int nread = 0;
			long length = 0;
			while ((nread = input.read(dataBytes)) != -1) {
				sha2sum.update(dataBytes, 0, nread);
				out.write(dataBytes, 0, nread);
				length+= nread;
			}

			this.internalName = toHexString(sha2sum.digest());
			this.length = length;
			
			out.close();
			String folder = internalName.substring(0, 2);
			Path parent = Files.createDirectories(root.resolve(folder));
			Files.move(file, parent.resolve(internalName), StandardCopyOption.REPLACE_EXISTING);
		} catch (NoSuchAlgorithmException nsae) {
			throw new IOException("Cant save file", nsae);
		}

	}

	private String toHexString(byte[] bytes) {
		StringBuilder hexString = new StringBuilder();
		for (int i = 0; i < bytes.length; i++) {
			hexString.append(Integer.toHexString(0xFF & bytes[i]));
		}
		return hexString.toString();
	}

	public void delete() throws IOException {
		String folder = internalName.substring(0, 2);
		Files.delete(root.resolve(folder).resolve(internalName));
	}
}
