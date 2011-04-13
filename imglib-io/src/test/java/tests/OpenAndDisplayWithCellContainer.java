package tests;

import ij.ImageJ;
import ij.ImagePlus;
import ij.process.ColorProcessor;
import mpicbg.imglib.Cursor;
import mpicbg.imglib.RandomAccess;
import mpicbg.imglib.display.ARGBScreenImage;
import mpicbg.imglib.display.RealARGBConverter;
import mpicbg.imglib.display.XYProjector;
import mpicbg.imglib.img.Img;
import mpicbg.imglib.img.ImgFactory;
import mpicbg.imglib.img.cell.CellImgFactory;
import mpicbg.imglib.io.ImgOpener;
import mpicbg.imglib.type.Type;
import mpicbg.imglib.type.numeric.ARGBType;
import mpicbg.imglib.type.numeric.real.FloatType;

public class OpenAndDisplayWithCellContainer
{
	public static < T extends Type< T > > void copyLocalizing (Img< T > src, Img< T > dst)
	{
		final Cursor< T > srcCursor = src.localizingCursor();
		final RandomAccess< T > dstCursor = dst.randomAccess();
		
		int[] position = new int[ src.numDimensions() ];
		while ( srcCursor.hasNext() )
		{
			srcCursor.fwd();
			srcCursor.localize( position );
			dstCursor.setPosition( position );
			dstCursor.get().set( srcCursor.get() );
		}
		
	}
	
	public static < T extends Type< T > > void copyIterating (Img< T > src, Img< T > dst)
	{
		if ( ! src.equalIterationOrder( dst ) ) {
			System.err.println( "src and dst do not have compatible iteration order" );
			return;
		}

		final Cursor< T > srcCursor = src.cursor();
		final Cursor< T > dstCursor = dst.cursor();
		
		while ( srcCursor.hasNext() )
		{
			srcCursor.fwd();
			dstCursor.fwd();
			dstCursor.get().set( srcCursor.get() );
		}
		
	}
	
	final static public void main( final String[] args )
	{
		new ImageJ();
		
		Img< FloatType > img = null;
		try
		{
			ImgFactory< FloatType > imgFactory = new CellImgFactory<FloatType>( new int[] {64, 64} );
			final ImgOpener io = new ImgOpener();
			img = io.openImg( "/home/tobias/Desktop/73.tif", imgFactory, new FloatType() );
		}
		catch ( Exception e )
		{
			e.printStackTrace();
			return;
		}

//		final Img< FloatType > copy = img.factory().create( img, new FloatType() );
//		copyLocalizing (img, copy);

//		final Img< FloatType > copy2 = img.factory().create( img, new FloatType() );
//		copyIterating( img, copy2 );

		Img< FloatType > finalImg = img;

		final ARGBScreenImage screenImage = new ARGBScreenImage( ( int )finalImg.dimension( 0 ), ( int )finalImg.dimension( 1 ) );
		final XYProjector< FloatType, ARGBType > projector = new XYProjector< FloatType, ARGBType >( finalImg, screenImage, new RealARGBConverter< FloatType >( 0, 255 ) );
		
		projector.setPosition( 0, 2 );
		projector.setPosition( 20, 3 );
		projector.map();

		final ColorProcessor cp = new ColorProcessor( screenImage.image() );
		final ImagePlus imp = new ImagePlus( "argbScreenProjection", cp );
		imp.show();
		imp.updateAndDraw();
	}
}
