package com.evola.edt.jcr;

import java.net.URL;
import java.net.URLDecoder;

import javax.annotation.PreDestroy;
import javax.jcr.Repository;

import org.apache.jackrabbit.core.RepositoryImpl;
import org.apache.jackrabbit.core.config.RepositoryConfig;
import org.springframework.beans.factory.FactoryBean;
import org.springframework.beans.factory.InitializingBean;

/**
 * @author Nikola
 * 
 */
public class RepositoryFactoryBean implements FactoryBean<Repository>,
		InitializingBean {
	private String configuration;
	private String homeDir;

	private Repository repository;

	/**
	 * @author Nikola
	 */
	public Repository getObject() throws Exception {
		return repository;
	}

	/**
	 * @author Nikola
	 */
	public Class<?> getObjectType() {
		return (this.repository != null ? this.repository.getClass()
				: Repository.class);
	}

	/**
	 * @author Nikola
	 */
	public boolean isSingleton() {
		return true;
	}

	/**
	 * @author Nikola
	 */
	public String getConfiguration() {
		return configuration;
	}

	/**
	 * @author Nikola
	 */
	public void setConfiguration(String configuration) {
		this.configuration = configuration;
	}

	/**
	 * @author Nikola
	 */
	public String getHomeDir() {
		return homeDir;
	}

	/**
	 * @author Nikola
	 */
	public void setHomeDir(String homeDir) {
		this.homeDir = homeDir;
	}

	/**
	 * @author Nikola
	 */
	public void afterPropertiesSet() throws Exception {
		String config = "";
		RepositoryConfig repoConfig = null;
		String startsWith = "classpath:";
		if (configuration.startsWith(startsWith)) {
			URL resource = this
					.getClass()
					.getClassLoader()
					.getResource(
							configuration.substring(startsWith.length(),
									configuration.length()));
			config = URLDecoder.decode(resource.getPath(), "UTF-8");

			repoConfig = RepositoryConfig.create(config, homeDir);

			repository = RepositoryImpl.create(repoConfig);
		}

		// repository = new TransientRepository(config, homeDir);
		// repository = RepositoryConfig.create(config);
	}

	@PreDestroy
	public void close() {
		if (repository != null && repository instanceof RepositoryImpl) {
			RepositoryImpl repo = (RepositoryImpl) repository;
			repo.shutdown();
		}
	}

}
