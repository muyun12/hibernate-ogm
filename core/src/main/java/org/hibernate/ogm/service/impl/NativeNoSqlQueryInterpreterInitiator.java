/*
 * Hibernate OGM, Domain model persistence for NoSQL datastores
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */
package org.hibernate.ogm.service.impl;

import org.hibernate.cfg.Configuration;
import org.hibernate.engine.query.spi.NativeQueryInterpreter;
import org.hibernate.engine.spi.SessionFactoryImplementor;
import org.hibernate.metamodel.source.MetadataImplementor;
import org.hibernate.ogm.dialect.GridDialect;
import org.hibernate.ogm.query.impl.NativeNoSqlQueryInterpreter;
import org.hibernate.ogm.query.spi.ParameterMetadataBuilder;
import org.hibernate.service.spi.ServiceRegistryImplementor;
import org.hibernate.service.spi.SessionFactoryServiceInitiator;

/**
 * Contributes the {@link NativeNoSqlQueryInterpreter}.
 *
 * @author Gunnar Morling
 */
public class NativeNoSqlQueryInterpreterInitiator implements SessionFactoryServiceInitiator<NativeQueryInterpreter> {

	public static NativeNoSqlQueryInterpreterInitiator INSTANCE = new NativeNoSqlQueryInterpreterInitiator();

	private NativeNoSqlQueryInterpreterInitiator() {
	}

	@Override
	public NativeQueryInterpreter initiateService(SessionFactoryImplementor sessionFactory, Configuration configuration,
			ServiceRegistryImplementor registry) {

		return getParameterMetadataRecognizer( registry );
	}

	@Override
	public NativeQueryInterpreter initiateService(SessionFactoryImplementor sessionFactory, MetadataImplementor metadata,
			ServiceRegistryImplementor registry) {

		return getParameterMetadataRecognizer( registry );
	}

	@Override
	public Class<NativeQueryInterpreter> getServiceInitiated() {
		return NativeQueryInterpreter.class;
	}

	private NativeQueryInterpreter getParameterMetadataRecognizer(ServiceRegistryImplementor registry) {
		ParameterMetadataBuilder parameterMetadataBuilder = registry.getService( GridDialect.class ).getParameterMetadataBuilder();
		return new NativeNoSqlQueryInterpreter( parameterMetadataBuilder );
	}
}
