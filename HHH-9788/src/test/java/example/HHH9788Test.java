package example;

import org.hibernate.boot.MetadataSources;
import org.hibernate.boot.registry.StandardServiceRegistryBuilder;
import org.hibernate.boot.spi.MetadataImplementor;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.tool.hbm2ddl.SchemaExport;
import org.hibernate.tool.hbm2ddl.SchemaUpdate;
import org.junit.Before;
import org.junit.Test;

/*
 * Copyright 2015 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * @author Oliver Gierke
 */
public class HHH9788Test {

	ServiceRegistry serviceRegistry;
	MetadataImplementor metadata;

	@Before
	public void setUp() {

		StandardServiceRegistryBuilder registryBuilder = new StandardServiceRegistryBuilder();

		registryBuilder.applySetting("hibernate.dialect", "org.hibernate.dialect.HSQLDialect");
		registryBuilder.applySetting("hibernate.connection.driver_class", "org.hsqldb.jdbcDriver");
		registryBuilder.applySetting("hibernate.connection.username", "sa");
		registryBuilder.applySetting("hibernate.connection.password", "");
		registryBuilder.applySetting("hibernate.connection.url", "jdbc:hsqldb:mem:test");

		this.serviceRegistry = registryBuilder.build();

		MetadataSources sources = new MetadataSources(serviceRegistry);
		sources.addAnnotatedClass(Role.class);
		sources.addAnnotatedClass(User.class);

		this.metadata = (MetadataImplementor) sources.buildMetadata();
	}

	/**
	 * @see HHH-9788
	 */
	@Test
	public void updatesSchemaCorrectly() {

		SchemaExport schemaExport = new SchemaExport(serviceRegistry, metadata);
		schemaExport.create(true, true);

		SchemaUpdate schemaUpdate = new SchemaUpdate(serviceRegistry, metadata);
		schemaUpdate.execute(true, true);
	}
}
