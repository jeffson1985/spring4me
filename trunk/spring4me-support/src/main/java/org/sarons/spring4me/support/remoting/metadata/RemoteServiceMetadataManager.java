package org.sarons.spring4me.support.remoting.metadata;

import java.util.List;


/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午10:00:41
 */
public interface RemoteServiceMetadataManager {
	
	String loadLocalAsString();
	
	List<RemoteServiceMetadata> loadLocal();
	
	void storeLocal(List<RemoteServiceMetadata> metadataList);
	
	List<RemoteServiceMetadata> loadRemote();
	
}
