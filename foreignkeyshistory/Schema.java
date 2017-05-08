package foreignkeyshistory;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

/**
 * This class represents a specific relational schema. That is, its set of tables represents as Table objects and the foreign keys
 * between these objects represented as ForeignKey objects.
 * 
 * To construct a Schema object use a SchemaBuilder object. For that reason, Schema provides only an empty constructor.
 * 
 * If multiple foreign keys exist from a source to a target, then only one ForeignKey object exists.
 */
public class Schema {
	private String versionName = null;
	private Set<ForeignKey> foreignKeys = new HashSet<>();
	
	private Map<String, Table> nameToTable = new HashMap<>();
	
	public Schema()
	{}
	
	public ForeignKey getForeignKey(Table source, Table target)
	{
		for (ForeignKey foreignKey : foreignKeys)
		{
			if (foreignKey.getSourceTable() == source && foreignKey.getTargetTable() == target){ return foreignKey; }
		}
		return null;
	}
	
	public void setVersionName(String versionName)
	{
		assert versionName != null;
		
		this.versionName = versionName;
	}

	public int getNumberOfTables()
	{
		return nameToTable.size();
	}
	
	public int getNumberOfForeignKeys()
	{
		return foreignKeys.size();
	}
	
	public String getVersionName()
	{
		return versionName;
	}
	
	public void addTable(Table table)
	{
		assert table != null;
		
		if (!nameToTable.containsKey(table.getName()))
		{
			nameToTable.put(table.getName(), table);
		}
	}
	
	public void addForeignKey(ForeignKey foreignKey)
	{
		foreignKeys.add(foreignKey);
	}
	

	
	/**
	 * Returns the table object for the table with the given name. Returns null if no such table exists.
	 * @param name
	 * @return
	 */
	public Table getTableWithName(String name)
	{
		return nameToTable.get(name);
	}
	
	
	
	/**
	 * Returns an iterator over the tables of this schema.
	 */
	public Iterator<Table> getTableIterator()
	{
		return nameToTable.values().iterator();
	}
	
	/**
	 * Returns an iterator over the foreign keys of this schema.
	 */
	public Iterator<ForeignKey> getForeignKeyIterator()
	{
		
		return foreignKeys.iterator();
	}
}
