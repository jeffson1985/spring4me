package org.sarons.spring4me.support.remoting.metadata;

import java.io.File;
import java.io.FileReader;
import java.util.Collections;
import java.util.List;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.type.TypeFactory;
import org.codehaus.jackson.type.JavaType;
import org.springframework.core.io.DefaultResourceLoader;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.util.FileCopyUtils;


/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午10:00:31
 */
public class DefaultRemoteServiceMetadataManager implements RemoteServiceMetadataManager {
	
	private static final Log log = LogFactory.getLog(DefaultRemoteServiceMetadataManager.class);

	private String metadataLocationLocal;
	
	private String metadataLocationRemote;
	
	private ResourceLoader resourceLoader = new DefaultResourceLoader();
	
	private ObjectMapper objectMapper = new ObjectMapper().enableDefaultTyping();
	
	private JavaType javaType = TypeFactory.defaultInstance().constructCollectionType(List.class, RemoteServiceMetadata.class);
	
	public void setMetadataLocationLocal(String metadataLocationLocal) {
		this.metadataLocationLocal = metadataLocationLocal;
	}
	
	public void setMetadataLocationRemote(String metadataLocationRemote) {
		this.metadataLocationRemote = metadataLocationRemote;
	}
	
	public List<RemoteServiceMetadata> loadLocal() {
		try {
			File dest = resourceLoader.getResource(metadataLocationLocal).getFile();
			return objectMapper.readValue(dest, javaType);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			//
			return Collections.emptyList();
		}
	}
	
	public String loadLocalAsString() {
		try {
			File dest = resourceLoader.getResource(metadataLocationLocal).getFile();
			return FileCopyUtils.copyToString(new FileReader(dest));
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			//
			return "";
		}
	}
	
	public List<RemoteServiceMetadata> loadRemote() {
		try {
			Resource metadataResource = resourceLoader.getResource(metadataLocationRemote);
			return objectMapper.readValue(metadataResource.getInputStream(), javaType);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
			//
			return Collections.emptyList();
		}
	}

	public void storeLocal(List<RemoteServiceMetadata> metadataList) {
		try {
			Resource metadataResource = resourceLoader.getResource(metadataLocationLocal);
			File dest = metadataResource.getFile();
			objectMapper.writerWithType(javaType).writeValue(dest, metadataList);
		} catch (Exception e) {
			log.error(e.getMessage(), e);
		}
	}

}
