package org.springframework.jdbc.support.incrementer;

import javax.sql.DataSource;

/**
 * Class to retrieve the next value of a given Oracle Sequence.
 * @author Dmitriy Kopylenko
 * @author Thomas Risberg
 * @author Juergen Hoeller
 * @version $Id$
 */
public class OracleSequenceMaxValueIncrementer extends AbstractSequenceMaxValueIncrementer {

	/**
	 * Default constructor.
	 **/
	public OracleSequenceMaxValueIncrementer() {
	}

	/**
	 * Convenience constructor.
	 * @param ds the DataSource to use
	 * @param incrementerName the name of the sequence/table to use
	 */
	public OracleSequenceMaxValueIncrementer(DataSource ds, String incrementerName) {
		setDataSource(ds);
		setIncrementerName(incrementerName);
		afterPropertiesSet();
	}

	protected String getSequenceQuery() {
		return "select " + getIncrementerName() + ".nextval from dual";
	}

}
