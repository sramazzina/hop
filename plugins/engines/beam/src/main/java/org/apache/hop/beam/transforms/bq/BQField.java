/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hop.beam.transforms.bq;

import org.apache.commons.lang.StringUtils;
import org.apache.hop.metadata.api.HopMetadataProperty;

public class BQField {

  @HopMetadataProperty(key = "name")
  private String name;

  @HopMetadataProperty(key = "new_name")
  private String newName;

  @HopMetadataProperty(key = "type")
  private String hopType;

  public BQField() {}

  public BQField(String name, String newName, String hopType) {
    this.name = name;
    this.newName = newName;
    this.hopType = hopType;
  }

  @Override
  public String toString() {
    return "BQField{" + "name='" + name + '\'' + ", hopType='" + hopType + '\'' + '}';
  }

  public String getNewNameOrName() {
    if (StringUtils.isNotEmpty(newName)) {
      return newName;
    } else {
      return name;
    }
  }

  /**
   * Gets name
   *
   * @return value of name
   */
  public String getName() {
    return name;
  }

  /**
   * @param name The name to set
   */
  public void setName(String name) {
    this.name = name;
  }

  /**
   * Gets newName
   *
   * @return value of newName
   */
  public String getNewName() {
    return newName;
  }

  /**
   * @param newName The newName to set
   */
  public void setNewName(String newName) {
    this.newName = newName;
  }

  /**
   * Gets hopType
   *
   * @return value of hopType
   */
  public String getHopType() {
    return hopType;
  }

  /**
   * @param hopType The hopType to set
   */
  public void setHopType(String hopType) {
    this.hopType = hopType;
  }
}
