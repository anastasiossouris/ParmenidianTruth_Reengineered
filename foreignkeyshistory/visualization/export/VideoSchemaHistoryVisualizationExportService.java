package foreignkeyshistory.visualization.export;

public class VideoSchemaHistoryVisualizationExportService extends SchemaHistoryVisualizationExportService{

	@Override
	protected SchemaHistoryVisualizationExport getSchemaHistoryVisualizationExport() {
		return new VideoSchemaHistoryVisualizationExport();
	}

}
