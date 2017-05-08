package foreignkeyshistory.visualization.load;

import java.io.IOException;
import java.nio.file.Path;

import foreignkeyshistory.visualization.TableLayout;

public abstract class TableLayoutLoadService {

	public TableLayout loadTableLayout(Path input) throws IOException
	{
		TableLayoutLoader loader = getTableLayoutLoader();
		
		return loader.loadTableLayout(input);
	}
	
	protected abstract TableLayoutLoader getTableLayoutLoader();
}
