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

package org.apache.hop.pipeline.transforms.userdefinedjavaclass;

import static org.junit.Assert.assertArrayEquals;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.mock;

import java.net.InetAddress;
import java.sql.Timestamp;
import org.apache.hop.core.exception.HopValueException;
import org.apache.hop.core.logging.LogChannel;
import org.apache.hop.core.row.IRowMeta;
import org.apache.hop.core.row.IValueMeta;
import org.apache.hop.core.row.value.ValueMetaBigNumber;
import org.apache.hop.core.row.value.ValueMetaBinary;
import org.apache.hop.core.row.value.ValueMetaBoolean;
import org.apache.hop.core.row.value.ValueMetaDate;
import org.apache.hop.core.row.value.ValueMetaInteger;
import org.apache.hop.core.row.value.ValueMetaInternetAddress;
import org.apache.hop.core.row.value.ValueMetaNumber;
import org.apache.hop.core.row.value.ValueMetaSerializable;
import org.apache.hop.core.row.value.ValueMetaString;
import org.apache.hop.core.row.value.ValueMetaTimestamp;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class FieldHelperTest {

  @BeforeEach
  void setUpStaticMocks() {
    // Do nothing
  }

  @AfterEach
  void tearDownStaticMocks() {
    // Do nothing
  }

  @Test
  void getNativeDataTypeSimpleName_Unknown() throws Exception {
    HopValueException e = new HopValueException();

    IValueMeta v = mock(IValueMeta.class);
    doThrow(e).when(v).getNativeDataTypeClass();

    LogChannel log = mock(LogChannel.class);

    assertEquals("Object", FieldHelper.getNativeDataTypeSimpleName(v));
    //    verify(log, times(1)).logDebug("Unable to get name from data type");
  }

  @Test
  void getNativeDataTypeSimpleName_String() {
    ValueMetaString v = new ValueMetaString();
    assertEquals("String", FieldHelper.getNativeDataTypeSimpleName(v));
  }

  @Test
  void getNativeDataTypeSimpleName_InetAddress() {
    ValueMetaInternetAddress v = new ValueMetaInternetAddress();
    assertEquals("InetAddress", FieldHelper.getNativeDataTypeSimpleName(v));
  }

  @Test
  void getNativeDataTypeSimpleName_Timestamp() {
    ValueMetaTimestamp v = new ValueMetaTimestamp();
    assertEquals("Timestamp", FieldHelper.getNativeDataTypeSimpleName(v));
  }

  @Test
  void getNativeDataTypeSimpleName_Binary() {
    ValueMetaBinary v = new ValueMetaBinary();
    assertEquals("Binary", FieldHelper.getNativeDataTypeSimpleName(v));
  }

  @Test
  void getGetSignature_String() {
    ValueMetaString v = new ValueMetaString("Name");
    String accessor = FieldHelper.getAccessor(true, "Name");

    assertEquals(
        "String Name = get(Fields.In, \"Name\").getString(r);",
        FieldHelper.getGetSignature(accessor, v));
  }

  @Test
  void getGetSignature_InetAddress() {
    ValueMetaInternetAddress v = new ValueMetaInternetAddress("IP");
    String accessor = FieldHelper.getAccessor(true, "IP");

    assertEquals(
        "InetAddress IP = get(Fields.In, \"IP\").getInetAddress(r);",
        FieldHelper.getGetSignature(accessor, v));
  }

  @Test
  void getGetSignature_Timestamp() {
    ValueMetaTimestamp v = new ValueMetaTimestamp("TS");
    String accessor = FieldHelper.getAccessor(true, "TS");

    assertEquals(
        "Timestamp TS = get(Fields.In, \"TS\").getTimestamp(r);",
        FieldHelper.getGetSignature(accessor, v));
  }

  @Test
  void getGetSignature_Binary() {
    ValueMetaBinary v = new ValueMetaBinary("Data");
    String accessor = FieldHelper.getAccessor(true, "Data");

    assertEquals(
        "byte[] Data = get(Fields.In, \"Data\").getBinary(r);",
        FieldHelper.getGetSignature(accessor, v));
  }

  @Test
  void getGetSignature_BigNumber() {
    ValueMetaBigNumber v = new ValueMetaBigNumber("Number");
    String accessor = FieldHelper.getAccessor(true, "Number");

    assertEquals(
        "BigDecimal Number = get(Fields.In, \"Number\").getBigDecimal(r);",
        FieldHelper.getGetSignature(accessor, v));
  }

  @Test
  void getGetSignature_Boolean() {
    ValueMetaBoolean v = new ValueMetaBoolean("Value");
    String accessor = FieldHelper.getAccessor(true, "Value");

    assertEquals(
        "Boolean Value = get(Fields.In, \"Value\").getBoolean(r);",
        FieldHelper.getGetSignature(accessor, v));
  }

  @Test
  void getGetSignature_Date() {
    ValueMetaDate v = new ValueMetaDate("DT");
    String accessor = FieldHelper.getAccessor(true, "DT");

    assertEquals(
        "Date DT = get(Fields.In, \"DT\").getDate(r);", FieldHelper.getGetSignature(accessor, v));
  }

  @Test
  void getGetSignature_Integer() {
    ValueMetaInteger v = new ValueMetaInteger("Value");
    String accessor = FieldHelper.getAccessor(true, "Value");

    assertEquals(
        "Long Value = get(Fields.In, \"Value\").getLong(r);",
        FieldHelper.getGetSignature(accessor, v));
  }

  @Test
  void getGetSignature_Number() {
    ValueMetaNumber v = new ValueMetaNumber("Value");
    String accessor = FieldHelper.getAccessor(true, "Value");

    assertEquals(
        "Double Value = get(Fields.In, \"Value\").getDouble(r);",
        FieldHelper.getGetSignature(accessor, v));
  }

  @Test
  void getGetSignature_Serializable() throws Exception {
    LogChannel log = mock(LogChannel.class);

    ValueMetaSerializable v = new ValueMetaSerializable("Data");
    String accessor = FieldHelper.getAccessor(true, "Data");

    assertEquals(
        "Object Data = get(Fields.In, \"Data\").getObject(r);",
        FieldHelper.getGetSignature(accessor, v));
  }

  @Test
  void getInetAddress_Test() throws Exception {
    ValueMetaInternetAddress v = new ValueMetaInternetAddress("IP");

    IRowMeta row = mock(IRowMeta.class);
    doReturn(v).when(row).searchValueMeta(anyString());
    doReturn(0).when(row).indexOfValue(anyString());

    assertEquals(
        InetAddress.getLoopbackAddress(),
        new FieldHelper(row, "IP").getInetAddress(new Object[] {InetAddress.getLoopbackAddress()}));
  }

  @Test
  void getTimestamp_Test() throws Exception {
    ValueMetaTimestamp v = new ValueMetaTimestamp("TS");

    IRowMeta row = mock(IRowMeta.class);
    doReturn(v).when(row).searchValueMeta(anyString());
    doReturn(0).when(row).indexOfValue(anyString());

    assertEquals(
        Timestamp.valueOf("2018-07-23 12:40:55"),
        new FieldHelper(row, "TS")
            .getTimestamp(new Object[] {Timestamp.valueOf("2018-07-23 12:40:55")}));
  }

  @Test
  void getSerializable_Test() throws Exception {
    ValueMetaSerializable v = new ValueMetaSerializable("Data");

    IRowMeta row = mock(IRowMeta.class);
    doReturn(v).when(row).searchValueMeta(anyString());
    doReturn(0).when(row).indexOfValue(anyString());

    assertEquals("...", new FieldHelper(row, "Data").getSerializable(new Object[] {"..."}));
  }

  @Test
  void getBinary_Test() throws Exception {
    ValueMetaBinary v = new ValueMetaBinary("Data");

    IRowMeta row = mock(IRowMeta.class);
    doReturn(v).when(row).searchValueMeta(anyString());
    doReturn(0).when(row).indexOfValue(anyString());

    assertArrayEquals(
        new byte[] {0, 1, 2},
        new FieldHelper(row, "Data").getBinary(new Object[] {new byte[] {0, 1, 2}}));
  }

  @Test
  void setValue_String() {
    ValueMetaString v = new ValueMetaString("Name");

    IRowMeta row = mock(IRowMeta.class);
    doReturn(v).when(row).searchValueMeta(anyString());
    doReturn(0).when(row).indexOfValue(anyString());

    Object[] data = new Object[1];
    new FieldHelper(row, "Name").setValue(data, "Hop");

    assertEquals("Hop", data[0]);
  }

  @Test
  void setValue_InetAddress() throws Exception {
    ValueMetaInternetAddress v = new ValueMetaInternetAddress("IP");

    IRowMeta row = mock(IRowMeta.class);
    doReturn(v).when(row).searchValueMeta(anyString());
    doReturn(0).when(row).indexOfValue(anyString());

    Object[] data = new Object[1];
    new FieldHelper(row, "IP").setValue(data, InetAddress.getLoopbackAddress());

    assertEquals(InetAddress.getLoopbackAddress(), data[0]);
  }

  @Test
  void setValue_ValueMetaBinary() throws Exception {
    ValueMetaBinary v = new ValueMetaBinary("Data");

    IRowMeta row = mock(IRowMeta.class);
    doReturn(v).when(row).searchValueMeta(anyString());
    doReturn(0).when(row).indexOfValue(anyString());

    Object[] data = new Object[1];
    new FieldHelper(row, "Data").setValue(data, new byte[] {0, 1, 2});

    assertArrayEquals(new byte[] {0, 1, 2}, (byte[]) data[0]);
  }
}
