/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hop.databases.ingres;

import org.apache.hop.core.Const;
import org.apache.hop.core.database.BaseDatabaseMeta;
import org.apache.hop.core.database.DatabaseMeta;
import org.apache.hop.core.database.DatabaseMetaPlugin;
import org.apache.hop.core.database.IDatabase;
import org.apache.hop.core.gui.plugin.GuiPlugin;
import org.apache.hop.core.row.IValueMeta;
import org.apache.hop.core.util.Utils;

/** Contains Computer Associates Ingres specific information through static final members */
@DatabaseMetaPlugin(
    type = "INGRES",
    typeDescription = "Ingres",
    documentationUrl = "/database/databases/ingres.html")
@GuiPlugin(id = "GUI-IngresDatabaseMeta")
public class IngresDatabaseMeta extends BaseDatabaseMeta implements IDatabase {

  public static final String CONST_ALTER_TABLE = "ALTER TABLE ";

  @Override
  public int[] getAccessTypeList() {
    return new int[] {DatabaseMeta.TYPE_ACCESS_NATIVE};
  }

  @Override
  public int getDefaultDatabasePort() {
    if (getAccessType() == DatabaseMeta.TYPE_ACCESS_NATIVE) {
      return -1;
    }
    return -1;
  }

  @Override
  public String getDriverClass() {
    return "com.ingres.jdbc.IngresDriver";
  }

  @Override
  public String getURL(String hostname, String port, String databaseName) {
    if (Utils.isEmpty(port)) {
      return "jdbc:ingres://" + hostname + ":II7/" + databaseName;
    } else {
      return "jdbc:ingres://" + hostname + ":" + port + "/" + databaseName;
    }
  }

  /**
   * @return true if the database supports bitmap indexes
   */
  @Override
  public boolean isSupportsBitmapIndex() {
    return false;
  }

  /**
   * @return true if the database supports synonyms
   */
  @Override
  public boolean isSupportsSynonyms() {
    return false;
  }

  /**
   * Generates the SQL statement to add a column to the specified table
   *
   * @param tableName The table to add
   * @param v The column defined as a value
   * @param tk the name of the technical key field
   * @param useAutoinc whether or not this field uses auto increment
   * @param pk the name of the primary key field
   * @param semicolon whether or not to add a semi-colon behind the statement.
   * @return the SQL statement to add a column to the specified table
   */
  @Override
  public String getAddColumnStatement(
      String tableName, IValueMeta v, String tk, boolean useAutoinc, String pk, boolean semicolon) {
    return CONST_ALTER_TABLE
        + tableName
        + " ADD COLUMN "
        + getFieldDefinition(v, tk, pk, useAutoinc, true, false);
  }

  /**
   * Generates the SQL statement to modify a column in the specified table
   *
   * @param tableName The table to add
   * @param v The column defined as a value
   * @param tk the name of the technical key field
   * @param useAutoinc whether or not this field uses auto increment
   * @param pk the name of the primary key field
   * @param semicolon whether or not to add a semi-colon behind the statement.
   * @return the SQL statement to modify a column in the specified table
   */
  @Override
  public String getModifyColumnStatement(
      String tableName, IValueMeta v, String tk, boolean useAutoinc, String pk, boolean semicolon) {
    return CONST_ALTER_TABLE
        + tableName
        + " ALTER COLUMN "
        + getFieldDefinition(v, tk, pk, useAutoinc, true, false);
  }

  /**
   * Generates the SQL statement to drop a column from the specified table
   *
   * @param tableName The table to add
   * @param v The column defined as a value
   * @param tk the name of the technical key field
   * @param useAutoinc whether or not this field uses auto increment
   * @param pk the name of the primary key field
   * @param semicolon whether or not to add a semi-colon behind the statement.
   * @return the SQL statement to drop a column from the specified table
   */
  @Override
  public String getDropColumnStatement(
      String tableName, IValueMeta v, String tk, boolean useAutoinc, String pk, boolean semicolon) {
    return CONST_ALTER_TABLE + tableName + " DROP COLUMN " + v.getName() + Const.CR;
  }

  @Override
  public String getFieldDefinition(
      IValueMeta v, String tk, String pk, boolean useAutoinc, boolean addFieldName, boolean addCr) {
    String retval = "";

    String fieldname = v.getName();
    int length = v.getLength();
    int precision = v.getPrecision();

    if (addFieldName) {
      retval += fieldname + " ";
    }

    int type = v.getType();
    switch (type) {
      case IValueMeta.TYPE_TIMESTAMP:
        retval += "TIMESTAMP";
        break;
      case IValueMeta.TYPE_DATE:
        retval += "DATE";
        break;
      case IValueMeta.TYPE_BOOLEAN:
        retval += "CHAR(1)";
        break;
      case IValueMeta.TYPE_NUMBER, IValueMeta.TYPE_INTEGER, IValueMeta.TYPE_BIGNUMBER:
        if (fieldname.equalsIgnoreCase(tk)
            || // Technical key
            fieldname.equalsIgnoreCase(pk) // Primary key
        ) {
          if (useAutoinc) {
            retval += "BIGINT PRIMARY KEY IDENTITY(0,1)";
          } else {
            retval += "BIGINT PRIMARY KEY NOT NULL";
          }
        } else {
          if (type == IValueMeta.TYPE_INTEGER) {
            // Integer values...
            if (length < 3) {
              retval += "TINYINT";
            } else if (length < 5) {
              retval += "SMALLINT";
            } else if (length < 10) {
              retval += "INT";
            } else if (length < 20) {
              retval += "BIGINT";
            } else {
              retval += "DECIMAL(" + length + ")";
            }
          } else if (type == IValueMeta.TYPE_BIGNUMBER) {
            // Fixed point value...
            if (length
                < 1) { // user configured no value for length. Use 16 digits, which is comparable to
              // mantissa 2^53 of IEEE 754 binary64 "double".
              length = 16;
            }
            if (precision
                < 1) { // user configured no value for precision. Use 16 digits, which is comparable
              // to IEEE 754 binary64 "double".
              precision = 16;
            }
            retval += "DECIMAL(" + length + "," + precision + ")";
          } else {
            // Floating point value with double precision...
            retval += "FLOAT8";
          }
        }
        break;
      case IValueMeta.TYPE_STRING:
        // Maybe use some default DB String length in case length<=0
        if (length > 0) {
          retval += "VARCHAR(" + length + ")";
        } else {
          retval += "VARCHAR(2000)";
        }
        break;
      default:
        retval += " UNKNOWN";
        break;
    }

    if (addCr) {
      retval += Const.CR;
    }

    return retval;
  }

  /**
   * @param tableName The table to be truncated.
   * @return The SQL statement to truncate a table: remove all rows from it without a transaction
   */
  @Override
  public String getTruncateTableStatement(String tableName) {
    return "DELETE FROM " + tableName;
  }

  @Override
  public boolean isSupportsGetBlob() {
    return false;
  }
}
