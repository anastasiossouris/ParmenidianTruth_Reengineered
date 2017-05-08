package foreignkeyshistory.visualization.export;

public class PowerPointSchemaHistoryVisualizationExportService extends SchemaHistoryVisualizationExportService{

	@Override
	protected SchemaHistoryVisualizationExport getSchemaHistoryVisualizationExport() {
		return new PowerPointSchemaHistoryVisualizationExport();
	}

}
