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

package org.apache.hop.vfs.azure;

import com.dalet.vfs2.provider.azure.AzFileSystemConfigBuilder;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.commons.vfs2.FileSystemException;
import org.apache.commons.vfs2.UserAuthenticationData;
import org.apache.commons.vfs2.UserAuthenticator;
import org.apache.commons.vfs2.auth.StaticUserAuthenticator;
import org.apache.commons.vfs2.provider.FileProvider;
import org.apache.commons.vfs2.util.UserAuthenticatorUtils;
import org.apache.hop.core.variables.IVariables;
import org.apache.hop.core.variables.Variables;
import org.apache.hop.core.vfs.plugin.IVfs;
import org.apache.hop.core.vfs.plugin.VfsPlugin;
import com.dalet.vfs2.provider.azure.AzFileProvider;
import org.apache.hop.vfs.azure.config.AzureConfig;
import org.apache.hop.vfs.azure.config.AzureConfigSingleton;

@VfsPlugin(type = "azure", typeDescription = "Azure VFS plugin")
public class AzureVfsPlugin implements IVfs {
  private Log logger = LogFactory.getLog(AzureVfsPlugin.class);
  @Override
  public String[] getUrlSchemes() {
    return new String[] {"azbs"};
  }

  @Override
  public FileProvider getProvider() {

    logger.info("Initialize Azure client");

    IVariables variables = Variables.getADefaultVariableSpace();
    AzureConfig config = AzureConfigSingleton.getConfig();
    String account = variables.resolve(config.getAccount());
    String key = variables.resolve(config.getKey());

    AzFileProvider azfp = new AzFileProvider();

    StaticUserAuthenticator auth = new StaticUserAuthenticator("", account, key);
      try {
          AzFileSystemConfigBuilder.getInstance().setUserAuthenticator(azfp.getDefaultFileSystemOptions(), auth);
      } catch (FileSystemException e) {
          throw new RuntimeException(e);
      }

      return azfp;
  }
}
