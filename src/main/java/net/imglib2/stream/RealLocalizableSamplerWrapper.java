package net.imglib2.stream;

import java.util.Spliterator;
import java.util.function.Consumer;
import net.imglib2.RealLocalizableSampler;
import net.imglib2.RealPositionable;

class RealLocalizableSamplerWrapper< T > implements Spliterator< RealLocalizableSampler< T > >, RealLocalizableSampler< T >
{
	private final RealLocalizableSpliterator< T > delegate;

	RealLocalizableSamplerWrapper( final RealLocalizableSpliterator< T > delegate )
	{
		this.delegate = delegate;
	}

	@Override
	public T get()
	{
		return delegate.get();
	}

	@Override
	public RealLocalizableSamplerWrapper< T > copy()
	{
		return new RealLocalizableSamplerWrapper<>( delegate.copy() );
	}

	@Override
	public void forEachRemaining( final Consumer< ? super RealLocalizableSampler< T > > action )
	{
		delegate.forEachRemaining( () -> action.accept( this ) );
	}

	@Override
	public boolean tryAdvance( final Consumer< ? super RealLocalizableSampler< T > > action )
	{
		if ( delegate.tryAdvance() )
		{
			action.accept( this );
			return true;
		}
		else
			return false;
	}

	@Override
	public Spliterator< RealLocalizableSampler< T > > trySplit()
	{
		final RealLocalizableSpliterator< T > prefix = delegate.trySplit();
		return prefix == null ? null : new RealLocalizableSamplerWrapper<>( prefix );
	}

	@Override
	public long estimateSize()
	{
		return delegate.estimateSize();
	}

	@Override
	public int characteristics()
	{
		return delegate.characteristics();
	}


	// -----------------------------------------------------------
	//   RealLocalizable

	@Override
	public int numDimensions()
	{
		return delegate.numDimensions();
	}

	@Override
	public void localize( final float[] position )
	{
		delegate.localize( position );
	}

	@Override
	public void localize( final double[] position )
	{
		delegate.localize( position );
	}

	@Override
	public void localize( final RealPositionable position )
	{
		delegate.localize( position );
	}

	@Override
	public float getFloatPosition( final int d )
	{
		return delegate.getFloatPosition( d );
	}

	@Override
	public double getDoublePosition( final int d )
	{
		return delegate.getDoublePosition( d );
	}
}
