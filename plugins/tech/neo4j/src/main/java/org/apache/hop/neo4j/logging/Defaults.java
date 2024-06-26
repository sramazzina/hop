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
 *
 */

package org.apache.hop.neo4j.logging;

import org.apache.hop.core.variables.Variable;

public class Defaults {

  @Variable(
      description =
          "Set this variable to the name of an existing Neo4j connection to enable execution logging to a Neo4j database.")
  public static final String NEO4J_LOGGING_CONNECTION = "NEO4J_LOGGING_CONNECTION";

  public static String TRANS_NODE_UPDATES_GROUP = "NODE_UPDATES";

  public static final String VARIABLE_NEO4J_LOGGING_CONNECTION_DISABLED = "-";
}
