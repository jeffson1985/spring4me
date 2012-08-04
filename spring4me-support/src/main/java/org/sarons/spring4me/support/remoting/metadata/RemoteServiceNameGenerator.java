package org.sarons.spring4me.support.remoting.metadata;

/**
 * 
 * @author Gavin
 * @since 0.5.0
 * @create 2012-7-30 - 下午10:00:50
 */
public interface RemoteServiceNameGenerator {

	/**
	 * 根据具体的服务实现类产生服务名
	 * @param serviceClass 服务实现类
	 * @return
	 */
	String generate(Class<?> serviceClass);
	
}
