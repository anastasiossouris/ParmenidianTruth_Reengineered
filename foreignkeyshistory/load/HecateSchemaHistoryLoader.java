package foreignkeyshistory.load;

import java.io.File;
import java.io.FileFilter;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.Unmarshaller;

import externalTools.Attribute;
import externalTools.Deletion;
import externalTools.HecateParser;
import externalTools.Insersion;
import externalTools.Table;
import externalTools.Transition;
import externalTools.TransitionList;
import externalTools.Transitions;
import externalTools.Update;
import foreignkeyshistory.Schema;
import foreignkeyshistory.SchemaBuilder;
import foreignkeyshistory.SchemaHistory;
import foreignkeyshistory.SchemaHistoryBuilder;
import foreignkeyshistory.SchemaTransitionChange;

/**
 * An implementation of the SchemaHistoryLoader interface using the Hecate functionality.
 */
public class HecateSchemaHistoryLoader implements SchemaHistoryLoader{
	
	/**
	 * The first Path object specifies the directory that contains the sql files.
	 * The chronological order of the versions coincides with their alphabetical order.
	 * The second Path object specifies the XML file that contains the transitions.
	 */
	@Override
	public SchemaHistory loadSchemaHistory(Path [] input)
			throws IOException, UnsupportedSchemaHistoryInputFormat, InvalidSchemaHistoryException 
	{
		assert input.length == 2;
		
		SchemaHistoryBuilder schemaHistoryBuilder = new SchemaHistoryBuilder();
		
		loadSchemaVersions(input[0], schemaHistoryBuilder);
		loadSchemaTransitions(input[1], schemaHistoryBuilder);
		
		return schemaHistoryBuilder.constructSchemaHistory();
	}

	
		
	/**
	 * The path is the XML transitions file which is loaded and added to the schema history.
	 */
	private void loadSchemaTransitions(Path path, SchemaHistoryBuilder schemaHistoryBuilder) 
	{
		externalTools.Transitions t = loadTransitionsFromXML(path.toFile());
		int oldVersion = 0;
		int newVersion = 1;
		
		for (externalTools.TransitionList tl : t.getList())
		{
			for (externalTools.Transition transition : tl.getTransitionList())
			{
				String affectedTable = transition.getAffTable().getName();
				SchemaTransitionChange transitionType = getSchemaTransitionChange(transition);
				
				if (transitionType == null){ continue; }
				
				schemaHistoryBuilder.addSchemaTransition(oldVersion, newVersion, affectedTable, transitionType);
			}
		
			oldVersion = oldVersion + 1;
			newVersion = newVersion + 1;
		}
	}

	
	
	private SchemaTransitionChange getSchemaTransitionChange(Transition transition) {
		SchemaTransitionChange change = null;
		
		switch (transition.getType())
		{
		case "NewTable":
			change = SchemaTransitionChange.TABLE_CREATED;
			break;
		case "DeleteTable":
			change = SchemaTransitionChange.TABLE_DELETED;
			break;
		case "UpdateTable":
			change = SchemaTransitionChange.TABLE_UPDATED;
			break;
		default:
			// i don't care for other change types.
			break;
		}
		
		return change;
	}



	private externalTools.Transitions loadTransitionsFromXML(File transition)
	{
		InputStream inputStream;
		Transitions t = null;
		try {
			inputStream = new FileInputStream(transition.getAbsolutePath());
			JAXBContext jaxbContext = JAXBContext.newInstance(Update.class, Deletion.class, Insersion.class, TransitionList.class, Transitions.class);
			Unmarshaller u = jaxbContext.createUnmarshaller();
			t = (Transitions)u.unmarshal( inputStream );
		} catch (Exception e) {
			e.printStackTrace();
		}

		return t;
	}

	
		
	/**
	 * The path input is a directory containing all the sql versions. This method loads the Schema versions using
	 * the passed SchemaHistoryBuilder object. 
	 */
	private void loadSchemaVersions(Path input, SchemaHistoryBuilder schemaHistoryBuilder)
	{
		File [] versions = input.toFile().listFiles(new SQLFileFilter());
		
		// The assumption is that the chronological order of the versions coincides with their
		// alphabetical order.
		Arrays.sort(versions);
		
		for (File version : versions)
		{
			schemaHistoryBuilder.appendSchemaVersion(loadSchema(version.toPath()));
		}
	}
	
	
	
	/**
	 * 
	 * @param input			The Path object representing the sql DDL file
	 * @return				A constructed Schema object from the sql file.
	 */
	private Schema loadSchema(Path input)
	{
		File file = input.toFile();
		SchemaBuilder schemaBuilder = new SchemaBuilder();
		
		HecateParser parser = new HecateParser();
		externalTools.Schema parsedSchema = parser.parse(file.getAbsolutePath());

		addTablesToSchema(parsedSchema, schemaBuilder);
		addForeignKeysToSchema(parsedSchema, schemaBuilder);
		setVersionNameToSchema(input, schemaBuilder);
		
		return schemaBuilder.buildSchema(); 
	}


	
	private void setVersionNameToSchema(Path input, SchemaBuilder schemaBuilder)
	{
		String sqlFile = input.toFile().getName();
		int index = sqlFile.indexOf(".");
		String name = sqlFile.substring(0, index);
		schemaBuilder.setVersionName(name);
	}
	
	
	
	/**
	 * Adds the foreign keys in the parsed schema as ForeignKey objects using the passed schema builder.
	 */
	private void addForeignKeysToSchema(externalTools.Schema parsedSchema, SchemaBuilder schemaBuilder) 
	{
		TreeMap<String, externalTools.Table> versionTables = parsedSchema.getTables();
		
		for(Map.Entry<String, Table> iterator : versionTables.entrySet())
		{
			Set<Map.Entry<Attribute, Attribute>> fkList = iterator.getValue().getfKey().getForeignKeys();
			
			for (Map.Entry<Attribute, Attribute> entry : fkList) {
				Attribute or = entry.getKey();
				Attribute re = entry.getValue();
			
				schemaBuilder.addForeignKey(or.getTable().getName(), re.getTable().getName());
			}
		}
	}

	

	/**
	 * Adds the tables as Table objects in the constructed Schema using the passed SchemaBuilder object.
	 */
	private void addTablesToSchema(externalTools.Schema parsedSchema, SchemaBuilder schemaBuilder) 
	{
		schemaBuilder.addTables(parsedSchema.getAllTables());
	}
	
	
	
	private class SQLFileFilter implements FileFilter {
		
		public boolean accept(File pathname) {
			if(pathname.getName().endsWith(".sql"))
				return true;
			return false;
			
		}
		
	}
}
