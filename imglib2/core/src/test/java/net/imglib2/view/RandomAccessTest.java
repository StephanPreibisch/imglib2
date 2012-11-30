/*
 * #%L
 * ImgLib2: a general-purpose, multidimensional image processing library.
 * %%
 * Copyright (C) 2009 - 2012 Stephan Preibisch, Stephan Saalfeld, Tobias
 * Pietzsch, Albert Cardona, Barry DeZonia, Curtis Rueden, Lee Kamentsky, Larry
 * Lindsey, Johannes Schindelin, Christian Dietz, Grant Harris, Jean-Yves
 * Tinevez, Steffen Jaensch, Mark Longair, Nick Perry, and Jan Funke.
 * %%
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDERS OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 * 
 * The views and conclusions contained in the software and documentation are
 * those of the authors and should not be interpreted as representing official
 * policies, either expressed or implied, of any organization.
 * #L%
 */

package net.imglib2.view;

import static org.junit.Assert.assertTrue;
import net.imglib2.RandomAccess;
import net.imglib2.img.Img;
import net.imglib2.img.array.ArrayImgFactory;
import net.imglib2.type.numeric.integer.UnsignedByteType;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

/**
 * TODO
 *
 */
public class RandomAccessTest
{
	Img< UnsignedByteType > img;

	@Before
	public void setUp()
	{
		long[] dimension = new long[] {100, 60, 10, 30, 50 };
		img = new ArrayImgFactory< UnsignedByteType >().create( dimension, new UnsignedByteType() );
	}

	@Test
	public void testRandomAccess()
	{
		RandomAccess< UnsignedByteType > a = img.randomAccess();

		long[] pos = new long[] { 28, 30, 5, 5, 12 };
		long[] dist = new long[] { 2, 3, 4, 2, 1 };
		
		testlocalize( a, pos );
		testfwd( a, pos );
		testbck( a, pos );
		testmove( a, pos, 3 );
		testmove( a, pos, dist );
	}

	@Test
	public void testFullSourceMapMixedAccess()
	{
		long[] offset = new long[] { 1, 10, 0, -5 };
		long[] dim = new long[] { 10, 10, 10, 10 };
		RandomAccess< UnsignedByteType > a = Views.offsetInterval( Views.flippedView( Views.hyperSlice( img, 2, 2 ), 3 ), offset, dim ).randomAccess();
		
		assertTrue( FullSourceMapMixedRandomAccess.class.isInstance( a ) );

		long[] pos = new long[] { 28, 30, 2, 15 };
		long[] dist = new long[] { 2, 3, 4, 1 };
		
		testlocalize( a, pos );
		testfwd( a, pos );
		testbck( a, pos );
		testmove( a, pos, 3 );
		testmove( a, pos, -2 );
		testmove( a, pos, dist );
	}

	public < T > void testlocalize( RandomAccess< T > a, final long[] pos )
	{
		long[] loc = new long[ pos.length ];
		long[] expected = pos.clone();

		a.setPosition( pos );
		a.localize( loc );			
		Assert.assertArrayEquals( expected, loc );
		
		for( int d = 0; d < a.numDimensions(); ++d )
		{
			Assert.assertTrue( expected[ d ] == a.getLongPosition( d ) );
			Assert.assertTrue( expected[ d ] == (long) a.getIntPosition( d ) );
			Assert.assertTrue( expected[ d ] == (long) a.getFloatPosition( d ) );
			Assert.assertTrue( expected[ d ] == (long) a.getDoublePosition( d ) );
		}
	}

	public < T > void testfwd( RandomAccess< T > a, final long[] pos )
	{
		long[] loc = new long[ pos.length ];
		long[] expected = new long[ pos.length ];

		for( int d = 0; d < a.numDimensions(); ++d )
		{
			a.setPosition( pos );
			a.fwd( d );
			a.localize( loc );
			
			for ( int i = 0; i < pos.length; ++i )
				expected[ i ] = pos[ i ];
			expected[ d ] += 1;

			Assert.assertArrayEquals( expected, loc );
		}
	}

	public < T > void testbck( RandomAccess< T > a, final long[] pos )
	{
		long[] loc = new long[ pos.length ];
		long[] expected = new long[ pos.length ];

		for( int d = 0; d < a.numDimensions(); ++d )
		{
			a.setPosition( pos );
			a.bck( d );
			a.localize( loc );
			
			for ( int i = 0; i < pos.length; ++i )
				expected[ i ] = pos[ i ];
			expected[ d ] -= 1;

			Assert.assertArrayEquals( expected, loc );
		}
	}

	public < T > void testmove( RandomAccess< T > a, final long[] pos, final long distance )
	{
		long[] loc = new long[ pos.length ];
		long[] expected = new long[ pos.length ];

		for( int d = 0; d < a.numDimensions(); ++d )
		{
			a.setPosition( pos );
			a.move( distance, d );
			a.localize( loc );
			
			for ( int i = 0; i < pos.length; ++i )
				expected[ i ] = pos[ i ];
			expected[ d ] += distance;

			Assert.assertArrayEquals( expected, loc );
		}
	}

	public < T > void testmove( RandomAccess< T > a, final long[] pos, final long[] distance )
	{
		long[] loc = new long[ pos.length ];
		long[] expected = new long[ pos.length ];

		for ( int d = 0; d < pos.length; ++d )
			expected[ d ] = pos[ d ] + distance[ d ];

		a.setPosition( pos );
		a.move( distance );
		a.localize( loc );
		
		Assert.assertArrayEquals( expected, loc );
	}
}
