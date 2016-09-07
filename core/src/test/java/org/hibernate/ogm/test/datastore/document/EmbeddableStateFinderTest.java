/*
 * Hibernate OGM, Domain model persistence for NoSQL datastores
 *
 * License: GNU Lesser General Public License (LGPL), version 2.1 or later
 * See the lgpl.txt file in the root directory or <http://www.gnu.org/licenses/lgpl-2.1.html>.
 */

package org.hibernate.ogm.test.datastore.document;

import static org.fest.assertions.Assertions.assertThat;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.hibernate.ogm.datastore.document.impl.EmbeddableStateFinder;
import org.hibernate.ogm.datastore.map.impl.MapTupleSnapshot;
import org.hibernate.ogm.dialect.batch.spi.OperationsQueue;
import org.hibernate.ogm.dialect.spi.TransactionContext;
import org.hibernate.ogm.dialect.spi.TupleContext;
import org.hibernate.ogm.dialect.spi.TupleTypeContext;
import org.hibernate.ogm.model.spi.Tuple;
import org.hibernate.ogm.utils.GridDialectOperationContexts;
import org.junit.Test;

/**
 * @author Emmanuel Bernard &lt;emmanuel@hibernate.org&gt;
 */
public class EmbeddableStateFinderTest {
	@Test
	public void testEmbeddableData() throws Exception {
		final Map<String, Object> tupleData = new HashMap<String, Object>();
		tupleData.put( "foo", 1 );

		tupleData.put( "null1.b1", null );
		tupleData.put( "null1.b2", null );

		tupleData.put( "nonnull1.1", null );
		tupleData.put( "nonnull1.2", 1 );

		tupleData.put( "nested1.null1.b1", null );
		tupleData.put( "nested1.null1.b2", null );
		tupleData.put( "nested1.notnull2", 1 );

		tupleData.put( "nested2.nonnull1.1", null );
		tupleData.put( "nested2.nonnull1.2", 1 );

		tupleData.put( "nested3.null1.b1", null );
		tupleData.put( "nested3.null1.b2", null );
		tupleData.put( "nested3.null2", null );

		Tuple tuple = new Tuple( new MapTupleSnapshot( tupleData ) );

		TupleContext context = new TupleContext() {
			@Override
			public OperationsQueue getOperationsQueue() {
				return null;
			}

			@Override
			public TransactionContext getTransactionContext() {
				return null;
			}

			@Override
			public TupleTypeContext getTupleTypeContext() {
				List<String> results = new ArrayList<String>();
				results.addAll( tupleData.keySet() );
				return new GridDialectOperationContexts.TupleTypeContextBuilder().selectableColumns( results ).buildTupleTypeContext();
			}
		};

		EmbeddableStateFinder data = new EmbeddableStateFinder( tuple, context );
		assertThat( data.getOuterMostNullEmbeddableIfAny( "foo" ) ).isNull();
		assertThat( data.getOuterMostNullEmbeddableIfAny( "null1.b1" ) ).isEqualTo( "null1" );
		assertThat( data.getOuterMostNullEmbeddableIfAny( "null1.b2" ) ).isEqualTo( "null1" );
		assertThat( data.getOuterMostNullEmbeddableIfAny( "nonnull1.1" ) ).isEqualTo( null );
		assertThat( data.getOuterMostNullEmbeddableIfAny( "nonnull1.2" ) ).isEqualTo( null );
		assertThat( data.getOuterMostNullEmbeddableIfAny( "nested1.notnull2" ) ).isEqualTo( null );
		assertThat( data.getOuterMostNullEmbeddableIfAny( "nested1.null1.b1" ) ).isEqualTo( "nested1.null1" );
		assertThat( data.getOuterMostNullEmbeddableIfAny( "nested1.null1.b2" ) ).isEqualTo( "nested1.null1" );
		assertThat( data.getOuterMostNullEmbeddableIfAny( "nested2.nonnull1.1" ) ).isEqualTo( null );
		assertThat( data.getOuterMostNullEmbeddableIfAny( "nested2.nonnull1.2" ) ).isEqualTo( null );
		assertThat( data.getOuterMostNullEmbeddableIfAny( "nested3.null1.b2" ) ).isEqualTo( "nested3" );
		assertThat( data.getOuterMostNullEmbeddableIfAny( "nested3.null2" ) ).isEqualTo( "nested3" );
	}
}
