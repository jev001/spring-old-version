/*
 * Copyright 2002-2006 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.springframework.jdbc.object;

import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.SqlNamedParameterHolder;
import org.springframework.jdbc.core.SqlNamedParameterTypes;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.NamedParameterUtils;
import org.springframework.jdbc.support.ParsedSql;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Implementation providing a basic set of JDBC operations expressed as a command object.
 * This class is loosely modelled after the .NET SqlCommand class.
 * All database access is done via the JdbcTemplate.
 *
 * @author Thomas Risberg
 * @see org.springframework.jdbc.object.SqlCommand
 * @see org.springframework.jdbc.core.JdbcTemplate
 */
public class SqlCommand implements SqlCommandOperations {
    private String sql;
    private ParsedSql parsedSql;
    private SqlNamedParameterTypes defaultSqlTypes = new SqlNamedParameterTypes(new HashMap());
    private NamedParameterJdbcTemplate namedParameteJdbcTemplate;

    //-------------------------------------------------------------------------
    // Constructor
    //-------------------------------------------------------------------------

    /**
     * Convenient constructor with DataSource and SQL string.
     * @param dataSource DataSource to use to get connections
     * @param sql to execute.
     */
    public SqlCommand(DataSource dataSource, String sql) {
        this.sql = sql;
        this.parsedSql = NamedParameterUtils.parseSqlStatement(sql);
        this.namedParameteJdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    //-------------------------------------------------------------------------
    // Bean properties
    //-------------------------------------------------------------------------

    /**
     * Set the default SQL types that should be used for setting parameter values.
     * Can be overridden at execution time.
     */
    public Map getDefaultSqlTypes() {
        return defaultSqlTypes.getTypes();
    }

    /**
     * Set the default SQL types that should be used for setting parameter values.
     * Can be overridden at execution time.
     */
    public void setDefaultSqlTypes(Map types) {
        this.defaultSqlTypes.getTypes().putAll(types);
    }

    /**
     * Get the default SQL types that should be used for setting parameter values.
     */
    public void setDefaultSqlTypes(SqlNamedParameterTypes namedTypes) {
        this.defaultSqlTypes.getTypes().putAll(namedTypes.getTypes());
    }


    //-------------------------------------------------------------------------
    // Execute methods
    //-------------------------------------------------------------------------

    public Object executeScalar() {
        return namedParameteJdbcTemplate.getJdbcOperations().queryForObject(sql, Object.class);
    }

    public Object executeScalar(Map parameters) {
        return namedParameteJdbcTemplate.queryForObject(sql, parameters, Object.class);
    }

    public Object executeScalar(SqlNamedParameterHolder parameterHolder) {
        return namedParameteJdbcTemplate.queryForObject(sql, parameterHolder, defaultSqlTypes, Object.class);
    }

    public Object executeObject(RowMapper rowMapper) {
        return namedParameteJdbcTemplate.getJdbcOperations().queryForObject(parsedSql.getNewSql(), rowMapper);
    }

    public Object executeObject(RowMapper rowMapper, Map parameters) {
        return namedParameteJdbcTemplate.queryForObject(sql, parameters, rowMapper);
    }

    public Object executeObject(RowMapper rowMapper, SqlNamedParameterHolder parameterHolder) {
        return namedParameteJdbcTemplate.queryForObject(sql, parameterHolder, defaultSqlTypes, rowMapper);
    }

    public List executeQuery() {
        return namedParameteJdbcTemplate.getJdbcOperations().queryForList(parsedSql.getNewSql());
    }

    public List executeQuery(Map parameters) {
        return namedParameteJdbcTemplate.queryForList(sql, parameters);
    }

    public List executeQuery(SqlNamedParameterHolder parameterHolder) {
        return namedParameteJdbcTemplate.queryForList(sql, parameterHolder, defaultSqlTypes);
    }

    public List executeQuery(RowMapper rowMapper) {
        return namedParameteJdbcTemplate.getJdbcOperations().query(parsedSql.getNewSql(), rowMapper);
    }

    public List executeQuery(RowMapper rowMapper, Map parameters) {
        return namedParameteJdbcTemplate.query(sql, parameters, rowMapper);
    }

    public List executeQuery(RowMapper rowMapper, SqlNamedParameterHolder parameterHolder) {
        return namedParameteJdbcTemplate.query(sql, parameterHolder, defaultSqlTypes, rowMapper);
    }

    public SqlRowSet executeRowSet() {
        return namedParameteJdbcTemplate.getJdbcOperations().queryForRowSet(parsedSql.getNewSql());
    }

    public SqlRowSet executeRowSet(Map parameters) {
        return namedParameteJdbcTemplate.queryForRowSet(sql, parameters);
    }

    public SqlRowSet executeRowSet(SqlNamedParameterHolder parameterHolder) {
        return namedParameteJdbcTemplate.queryForRowSet(sql, parameterHolder, defaultSqlTypes);
    }

    public int executeUpdate() {
        return namedParameteJdbcTemplate.getJdbcOperations().update(parsedSql.getNewSql());
    }

    public int executeUpdate(Map parameters) {
        return namedParameteJdbcTemplate.update(sql, parameters);
    }

    public int executeUpdate(SqlNamedParameterHolder parameterHolder) {
        Map typesToUse = new HashMap();
        System.out.println("..... " + typesToUse);
        typesToUse.putAll(defaultSqlTypes.getTypes());
        System.out.println(">>>>> " + typesToUse);
        if (parameterHolder.getTypes().size() > 0) {
            typesToUse.putAll(parameterHolder.getTypes());
            System.out.println("+++++ " + parameterHolder.getTypes());
        }
        System.out.println("===== " + typesToUse);
        parameterHolder.setTypes(typesToUse);
        return namedParameteJdbcTemplate.update(sql, parameterHolder, defaultSqlTypes);
    }

    public int executeUpdate(SqlNamedParameterHolder parameterHolder, KeyHolder keyHolder) {
        return executeUpdate(parameterHolder, keyHolder, null);
    }

    public int executeUpdate(SqlNamedParameterHolder parameterHolder, KeyHolder keyHolder, String[] keyColumnNames) {
        return namedParameteJdbcTemplate.update(sql, parameterHolder, defaultSqlTypes, keyHolder, keyColumnNames);
    }


    public int executeInsert(SqlInsertBuilder insertBuilder) {
        if (insertBuilder.getKeyHolder() == null) {
            return namedParameteJdbcTemplate.update(insertBuilder.buildSqlToUse(sql), insertBuilder.getNamedParameterHolder(), insertBuilder.getNamedParameterTypes());
        }
        else {
            return namedParameteJdbcTemplate.update(insertBuilder.buildSqlToUse(sql), insertBuilder.getNamedParameterHolder(), insertBuilder.getNamedParameterTypes(), insertBuilder.getKeyHolder(), insertBuilder.getKeyColumnNames());
        }
    }

    public Map executeCall(SqlCallBuilder callBuilder) {
        StoredProcedure proc = new StoredProcedure() {
            public Map execute(Map map) throws DataAccessException {
                return super.execute(map);
            }
        };
        return proc.execute((Map)null);
    }

}
