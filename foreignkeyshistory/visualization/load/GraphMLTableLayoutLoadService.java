package foreignkeyshistory.visualization.load;

public class GraphMLTableLayoutLoadService extends TableLayoutLoadService{

	@Override
	protected TableLayoutLoader getTableLayoutLoader() {
		return new GraphMLTableLayoutLoader();
	}

}
