package foreignkeyshistory.visualization.export;

public class ImageSchemaHistoryVisualizationExportService extends SchemaHistoryVisualizationExportService{

	@Override
	protected SchemaHistoryVisualizationExport getSchemaHistoryVisualizationExport() {
		return new ImageSchemaHistoryVisualizationExport();
	}

}
