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
package org.apache.hop.databases.monetdb;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.apache.hop.core.database.DatabaseMeta;
import org.apache.hop.core.row.value.ValueMetaBigNumber;
import org.apache.hop.core.row.value.ValueMetaBoolean;
import org.apache.hop.core.row.value.ValueMetaDate;
import org.apache.hop.core.row.value.ValueMetaInteger;
import org.apache.hop.core.row.value.ValueMetaInternetAddress;
import org.apache.hop.core.row.value.ValueMetaNumber;
import org.apache.hop.core.row.value.ValueMetaString;
import org.apache.hop.core.row.value.ValueMetaTimestamp;
import org.junit.Before;
import org.junit.Test;

public class MonetDBDatabaseMetaTest {

  private MonetDBDatabaseMeta nativeMeta;

  @Before
  public void setupBefore() {
    nativeMeta = new MonetDBDatabaseMeta();
    nativeMeta.setAccessType(DatabaseMeta.TYPE_ACCESS_NATIVE);
  }

  @Test
  public void testSettings() {
    assertArrayEquals(new int[] {DatabaseMeta.TYPE_ACCESS_NATIVE}, nativeMeta.getAccessTypeList());
    assertEquals(50000, nativeMeta.getDefaultDatabasePort());
    assertTrue(nativeMeta.isSupportsAutoInc());
    assertEquals(1, nativeMeta.getNotFoundTK(true));
    assertEquals(0, nativeMeta.getNotFoundTK(false));
    assertEquals("org.monetdb.jdbc.MonetDriver", nativeMeta.getDriverClass());
    assertEquals("jdbc:monetdb://FOO:BAR/WIBBLE", nativeMeta.getURL("FOO", "BAR", "WIBBLE"));
    assertEquals("jdbc:monetdb://FOO/WIBBLE", nativeMeta.getURL("FOO", "", "WIBBLE"));
    assertFalse(nativeMeta.isFetchSizeSupported());
    assertTrue(nativeMeta.isSupportsBitmapIndex());
    assertFalse(nativeMeta.isSupportsSynonyms());
    assertTrue(nativeMeta.isSupportsBatchUpdates());
    assertTrue(nativeMeta.isSupportsSetMaxRows());
    assertArrayEquals(
        new String[] {
          "IS",
          "ISNULL",
          "NOTNULL",
          "IN",
          "BETWEEN",
          "OVERLAPS",
          "LIKE",
          "ILIKE",
          "NOT",
          "AND",
          "OR",
          "CHAR",
          "VARCHAR",
          "CLOB",
          "BLOB",
          "DECIMAL",
          "DEC",
          "NUMERIC",
          "TINYINT",
          "SMALLINT",
          "INT",
          "BIGINT",
          "REAL",
          "DOUBLE",
          "BOOLEAN",
          "DATE",
          "TIME",
          "TIMESTAMP",
          "INTERVAL",
          "YEAR",
          "MONTH",
          "DAY",
          "HOUR",
          "MINUTE",
          "SECOND",
          "TIMEZONE",
          "EXTRACT",
          "CURRENT_DATE",
          "CURRENT_TIME",
          "CURRENT_TIMESTAMP",
          "LOCALTIME",
          "LOCALTIMESTAMP",
          "CURRENT_TIME",
          "SERIAL",
          "START",
          "WITH",
          "INCREMENT",
          "CACHE",
          "CYCLE",
          "SEQUENCE",
          "GETANCHOR",
          "GETBASENAME",
          "GETCONTENT",
          "GETCONTEXT",
          "GETDOMAIN",
          "GETEXTENSION",
          "GETFILE",
          "GETHOST",
          "GETPORT",
          "GETPROTOCOL",
          "GETQUERY",
          "GETUSER",
          "GETROBOTURL",
          "ISURL",
          "NEWURL",
          "BROADCAST",
          "MASKLEN",
          "SETMASKLEN",
          "NETMASK",
          "HOSTMASK",
          "NETWORK",
          "TEXT",
          "ABBREV",
          "CREATE",
          "TYPE",
          "NAME",
          "DROP",
          "USER"
        },
        nativeMeta.getReservedWords());
    assertTrue(nativeMeta.isSupportsResultSetMetadataRetrievalOnly());
    assertEquals(Integer.MAX_VALUE, nativeMeta.getMaxVARCHARLength());
    assertTrue(nativeMeta.isSupportsSequences());
  }

  @Test
  public void testSqlStatements() {
    assertEquals(
        "ALTER TABLE FOO ADD BAR VARCHAR(15)",
        nativeMeta.getAddColumnStatement(
            "FOO", new ValueMetaString("BAR", 15, 0), "", false, "", false));

    assertEquals(
        "ALTER TABLE FOO MODIFY BAR VARCHAR(15)",
        nativeMeta.getModifyColumnStatement(
            "FOO", new ValueMetaString("BAR", 15, 0), "", false, "", false));

    assertEquals(
        "insert into FOO(FOOKEY, FOOVERSION) values (0, 1)",
        nativeMeta.getSqlInsertAutoIncUnknownDimensionRow("FOO", "FOOKEY", "FOOVERSION"));
    assertEquals("DELETE FROM FOO", nativeMeta.getTruncateTableStatement("FOO"));
    assertEquals(
        "SELECT * FROM FOO;",
        nativeMeta.getSqlQueryFields(
            "FOO")); // Pretty sure the semicolon shouldn't be there - likely bug.
    assertEquals("SELECT name FROM sys.sequences", nativeMeta.getSqlListOfSequences());
    assertEquals(
        "SELECT * FROM sys.sequences WHERE name = 'FOO'", nativeMeta.getSqlSequenceExists("FOO"));
    assertEquals(
        "SELECT get_value_for( 'sys', 'FOO' )", nativeMeta.getSqlCurrentSequenceValue("FOO"));
    assertEquals(
        "SELECT next_value_for( 'sys', 'FOO' )", nativeMeta.getSqlNextSequenceValue("FOO"));
  }

  @Test
  public void testGetFieldDefinition() {
    assertEquals(
        "FOO TIMESTAMP",
        nativeMeta.getFieldDefinition(new ValueMetaDate("FOO"), "", "", false, true, false));
    assertEquals(
        "TIMESTAMP",
        nativeMeta.getFieldDefinition(new ValueMetaTimestamp("FOO"), "", "", false, false, false));

    // Simple hack to prevent duplication of code. Checking the case of supported boolean type
    // both supported and unsupported. Should return BOOLEAN if supported, or CHAR(1) if not.
    String[] typeCk = new String[] {"CHAR(1)", "BOOLEAN", "CHAR(1)"};
    int i = (nativeMeta.isSupportsBooleanDataType() ? 1 : 0);
    assertEquals(
        typeCk[i],
        nativeMeta.getFieldDefinition(new ValueMetaBoolean("FOO"), "", "", false, false, false));

    assertEquals(
        "SERIAL",
        nativeMeta.getFieldDefinition(
            new ValueMetaBigNumber("FOO", 8, 0), "", "FOO", true, false, false));
    assertEquals(
        "BIGINT",
        nativeMeta.getFieldDefinition(
            new ValueMetaNumber("FOO", 10, 0), "FOO", "", false, false, false));
    assertEquals(
        "BIGINT",
        nativeMeta.getFieldDefinition(
            new ValueMetaInteger("FOO", 8, 0), "", "FOO", false, false, false));

    // integer types ( precision == 0 )
    assertEquals(
        "BIGINT",
        nativeMeta.getFieldDefinition(
            new ValueMetaInteger("FOO", 8, 0), "", "", false, false, false));
    assertEquals(
        "BIGINT",
        nativeMeta.getFieldDefinition(
            new ValueMetaNumber("FOO", 10, 0), "", "", false, false, false));
    assertEquals(
        "DECIMAL(19)",
        nativeMeta.getFieldDefinition(
            new ValueMetaBigNumber("FOO", 19, 0), "", "", false, false, false));
    assertEquals(
        "DOUBLE",
        nativeMeta.getFieldDefinition(
            new ValueMetaNumber("FOO", 8, 0), "", "", false, false, false));

    // Numerics with precisions
    assertEquals(
        "DECIMAL(19, 5)",
        nativeMeta.getFieldDefinition(
            new ValueMetaBigNumber("FOO", 19, 5), "", "", false, false, false));

    assertEquals(
        "DECIMAL(19)",
        nativeMeta.getFieldDefinition(
            new ValueMetaBigNumber("FOO", 19, -5), "", "", false, false, false));

    assertEquals(
        "DOUBLE",
        nativeMeta.getFieldDefinition(
            new ValueMetaBigNumber("FOO", 11, 5), "", "", false, false, false));

    // String Types
    assertEquals(
        "VARCHAR(10)",
        nativeMeta.getFieldDefinition(
            new ValueMetaString("FOO", 10, 0), "", "", false, false, false));

    // This next one is a bug:
    //   getMaxVARCHARLength = Integer.MAX_VALUE,
    //   if statement for CLOB trips if length > getMaxVARCHARLength()
    //   length is of type int - so this could never happen
    assertEquals(
        "VARCHAR()",
        nativeMeta.getFieldDefinition(
            new ValueMetaString("FOO", nativeMeta.getMaxVARCHARLength() + 1, 0),
            "",
            "",
            false,
            false,
            false));

    assertEquals(
        "VARCHAR()",
        nativeMeta.getFieldDefinition(
            new ValueMetaString("FOO", -2, 0),
            "",
            "",
            false,
            false,
            false)); // should end up with (100) if "safeMode = true"

    MonetDBDatabaseMeta.safeModeLocal.set(Boolean.valueOf(true));
    assertEquals(
        "VARCHAR(100)",
        nativeMeta.getFieldDefinition(
            new ValueMetaString("FOO", -2, 0),
            "",
            "",
            false,
            false,
            false)); // should end up with (100) if "safeMode = true"

    assertEquals(
        " UNKNOWN",
        nativeMeta.getFieldDefinition(
            new ValueMetaInternetAddress("FOO"), "", "", false, false, false));
    assertEquals(
        " UNKNOWN" + System.getProperty("line.separator"),
        nativeMeta.getFieldDefinition(
            new ValueMetaInternetAddress("FOO"), "", "", false, false, true));
  }
}
