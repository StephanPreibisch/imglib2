package mpicbg.imglib.scripting.math2.fn;

import java.util.Collection;
import mpicbg.imglib.cursor.Cursor;

public interface IFunction {
	public double eval();
	public void findCursors(Collection<Cursor<?>> cursors);
}