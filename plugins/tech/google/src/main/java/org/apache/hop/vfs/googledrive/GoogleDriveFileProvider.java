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

package org.apache.hop.vfs.googledrive;

import java.util.Collection;
import java.util.Set;
import org.apache.commons.vfs2.Capability;
import org.apache.commons.vfs2.FileName;
import org.apache.commons.vfs2.FileSystem;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.FileSystemOptions;
import org.apache.commons.vfs2.provider.AbstractOriginatingFileProvider;

public class GoogleDriveFileProvider extends AbstractOriginatingFileProvider {

  public static final String SCHEME = "googledrive";
  public static final String DISPLAY_NAME = "Google Drive";

  protected static final Collection<Capability> capabilities =
      Set.of(
          Capability.CREATE,
          Capability.DELETE,
          Capability.RENAME,
          Capability.GET_TYPE,
          Capability.LIST_CHILDREN,
          Capability.READ_CONTENT,
          Capability.URI,
          Capability.WRITE_CONTENT,
          Capability.GET_LAST_MODIFIED,
          Capability.RANDOM_ACCESS_READ);

  @Override
  public FileSystem doCreateFileSystem(FileName fileName, FileSystemOptions fileSystemOptions)
      throws FileSystemException {
    return new GoogleDriveFileSystem(fileName, fileSystemOptions);
  }

  @Override
  public Collection<Capability> getCapabilities() {
    return capabilities;
  }
}
