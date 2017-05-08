package foreignkeyshistory.visualization.export;

import java.io.IOException;
import java.nio.file.Path;

import edu.uci.ics.jung.visualization.VisualizationViewer;
import foreignkeyshistory.Schema;
import foreignkeyshistory.SchemaHistory;
import foreignkeyshistory.visualization.TableLayout;

public abstract class SchemaHistoryVisualizationExportService {
	
	public final void export(SchemaHistory schemaHistory, Schema diachronicSchema, TableLayout tableLayout, int edgeType, VisualizationViewer< String, String> vv,
			Path output) throws IOException
	{
		SchemaHistoryVisualizationExport exporter = getSchemaHistoryVisualizationExport();
		
		exporter.exportSchemaHistory(schemaHistory, diachronicSchema, tableLayout, edgeType, vv, output);
	}
	
	protected abstract SchemaHistoryVisualizationExport getSchemaHistoryVisualizationExport();
}
