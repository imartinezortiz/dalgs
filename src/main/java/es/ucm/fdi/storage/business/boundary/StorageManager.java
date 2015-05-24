package es.ucm.fdi.storage.business.boundary;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

import javax.annotation.PostConstruct;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import es.ucm.fdi.domain.events.Action1;
import es.ucm.fdi.storage.business.control.StorageObjectRepository;
import es.ucm.fdi.storage.business.entity.DomainEvents;
import es.ucm.fdi.storage.business.entity.StorageObject;
import es.ucm.fdi.storage.business.entity.StorageObjectId;

@Service
@Transactional
public class StorageManager {	

	private static final Logger logger = LoggerFactory
			.getLogger("es.ucm.fdi.storage");
	
	@Autowired
	private ResourceLoader loader;
	
	@Value("#{storagePrefs[rootPath]}")
	private String rootPath;
	
	private Path root;
	
	@Value("#{storagePrefs[serviceURL]}")
	private URL serviceURL;
	
	@Autowired
	private StorageObjectRepository repository;
	
	public StorageManager() {
		DomainEvents.OBJECT_DELETED.register(new RemoveFromDBOnDeletion(this));
	}
	
	@PostConstruct
	private void init() {
		try {
			this.root = loader.getResource(rootPath).getFile().toPath();
		} catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	public StorageObject putObject(String bucket, String key, InputStream is) throws IOException {
		return putObject(bucket, key, null, is);
	}

	public StorageObject putObject(String bucket, String key, String mimeType, InputStream is) throws IOException {
		StorageObjectId id = new StorageObjectId(bucket, key);
		StorageObject object = new StorageObject(id, mimeType, root);
		object.save(is);
		repository.save(object);
		return object;
	}

	public URL getUrl(String bucket, String key) {
		URL url = null;
		try {
			url = serviceURL.toURI().resolve(bucket+"/"+key).toURL();
		} catch (MalformedURLException e) {
			logger.warn("Error generando URL", e);
			throw new RuntimeException(e);
		} catch (URISyntaxException e) {
			logger.warn("Error generando URL", e);
		}		
		return url;
	}

	public StorageObject getObject(String bucket, String key) {
		return getObject(new StorageObjectId(bucket, key));
	}
	
	public StorageObject getObject(StorageObjectId objectId) {
		StorageObject object = repository.findOne(objectId);
		object.setRootPath(root);
		return object;
	}

	public StorageObjectId getObjectId(String location) {
		StorageObjectId id = null;
		String serviceLocation = serviceURL.toString();
		if (location.startsWith(serviceLocation)) {
			int serviceLocationLength = serviceLocation.length();
			if (serviceLocationLength <= location.length()) {
				String objectId = location.substring(serviceLocationLength);
				int pos = objectId.indexOf('/');
				if (pos >= 0 && pos+1 <= objectId.length()) {
					id = new StorageObjectId(objectId.substring(0, pos), objectId.substring(pos+1));	
				}
			}
		}
		return id;
	}

	public void removeObject(StorageObjectId id) throws IOException {
		StorageObject object = getObject(id);
		object.delete();
		repository.delete(object);
	}
	
	private static class RemoveFromDBOnDeletion implements Action1<StorageObject> {
		
		private StorageManager storage;

		public RemoveFromDBOnDeletion(StorageManager storage) {
			this.storage = storage;
		}

		@Override
		public void execute(StorageObject obj) {
			storage.repository.delete(obj);
		}		
	}
}
