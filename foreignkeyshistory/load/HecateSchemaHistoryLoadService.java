package foreignkeyshistory.load;

public class HecateSchemaHistoryLoadService extends SchemaHistoryLoadService{

	@Override
	protected SchemaHistoryLoader getSchemaHistoryLoader() {
		return new HecateSchemaHistoryLoader();
	}
}
