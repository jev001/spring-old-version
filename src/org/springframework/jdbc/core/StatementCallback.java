package org.springframework.jdbc.core;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * Generic callback interface for code that operates on a JDBC Statement.
 * Allows to execute any number of operations on a single Statement,
 * for example a single executeUpdate call or repeated executeUpdate
 * calls with varying parameters.
 *
 * <p>Used internally by JdbcTemplate, but also useful for application code.
 *
 * @author Juergen Hoeller
 * @since 16.03.2004
 * @see JdbcTemplate#execute(StatementCallback)
 */
public interface StatementCallback {

	/**
	 * Gets called by JdbcTemplate.execute with an active JDBC Statement.
	 * Does not need to care about activating or closing the Connection,
	 * or handling transactions.
	 *
	 * <p>If called without a thread-bound JDBC transaction (initiated by
	 * DataSourceTransactionManager), the code will simply get executed on the
	 * JDBC connection with its transactional semantics. If JdbcTemplate is
	 * configured to use a JTA-aware DataSource, the JDBC connection and thus
	 * the callback code will be transactional if a JTA transaction is active.
	 *
	 * <p>Allows for returning a result object created within the callback, i.e.
	 * a domain object or a collection of domain objects. Note that there's
	 * special support for single step actions: see JdbcTemplate.queryForObject etc.
	 * A thrown RuntimeException is treated as application exception, it gets
	 * propagated to the caller of the template.
	 *
	 * @param stmt active JDBC Statement
	 * @return a result object, or null if none
	 * @throws SQLException in case of errors
	 * @see JdbcTemplate#queryForObject(String, Class)
	 * @see JdbcTemplate#queryForList(String)
	 */
	Object doInStatement(Statement stmt) throws SQLException;

}
