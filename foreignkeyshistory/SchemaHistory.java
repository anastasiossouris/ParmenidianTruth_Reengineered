package foreignkeyshistory;

import java.util.Iterator;
import java.util.List;
import java.util.Set;

/**
 * This class represents a history of a relational schema as:
 * 		(1) A list of versions in chronological order (i.e from version 0 to the last version)
 * 		(2) A list of transitions between versions. If i have N versions, then there are N-1 transition objects.
 * 		from version 0 to version 1, from version 1 to version 2, ..., from version N-1 to version N.
 * 
 * Each Schema contains its own Table objects. That is, if a table appears in multiple versions, a different Table
 * object is constructed for each version. These Table objects have the same name attribute. 
 * Consequently, the name of a table is used to determine equality between tables in different versions.
 * 
 * About transitions:
 * 
 */
public class SchemaHistory {
	private List<Schema> schemaVersions = null;
	private List<Set<SchemaTransition>> schemaTransitions = null;
	
	public SchemaHistory()
	{}
	
	public void addSchemaVersions(List<Schema> schemaVersions)
	{
		assert schemaVersions != null;
		
		this.schemaVersions = schemaVersions;
	}
	
	public void addSchemaTransitions(List<Set<SchemaTransition>> schemaTransitions)
	{
		assert schemaTransitions != null;
		
		this.schemaTransitions = schemaTransitions;
	}
	
	
	public int getNumberOfSchemaVersions()
	{
		return schemaVersions.size();
	}
	
	/**
	 * The schema versions range from 0 up to N-1, where N is the total number of schemas. 
	 * This method returns the schema with index = version.
	 */
	public Schema getSchemaVersion(int version)
	{
		if (version < 0 || version >= schemaVersions.size())
		{
			throw new IndexOutOfBoundsException("schema version out of range");
		}
		
		return schemaVersions.get(version);
	}
	
	/**
	 * Returns an iterator over the schema versions of this history.
	 */
	public Iterator<Schema> getSchemaVersionsIterator()
	{
		return schemaVersions.iterator();
	}
	
	/**
	 * Returns an iterator over the transitions of this history.
	 */
	public Iterator<Set<SchemaTransition>> getSchemaTransitionsIterator()
	{
		return schemaTransitions.iterator();
	}
}
