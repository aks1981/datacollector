/**
 * Copyright 2015 StreamSets Inc.
 *
 * Licensed under the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.streamsets.pipeline.stage.destination.hdfs;

import com.streamsets.pipeline.api.Config;
import com.streamsets.pipeline.api.StageException;
import com.streamsets.pipeline.api.StageUpgrader;
import com.streamsets.pipeline.api.impl.Utils;

import java.util.ArrayList;
import java.util.List;

public class HdfsTargetUpgrader implements StageUpgrader {

  @Override
  public List<Config> upgrade (
    String library,
    String stageName,
    String stageInstance,
    int fromVersion,
    int toVersion,
    List<Config> configs
  ) throws StageException {
    switch(fromVersion) {
      case 1:
        upgradeV1ToV2(configs);
        break;
      default:
        throw new IllegalStateException(Utils.format("Unexpected fromVersion {}", fromVersion));
    }
    return configs;
  }

  private void upgradeV1ToV2(List<Config> configs) {

    List<Config> configsToRemove = new ArrayList<>();
    List<Config> configsToAdd = new ArrayList<>();

    for(Config config : configs) {
      switch (config.getName()) {
        case "dataFormat":
          configsToRemove.add(config);
          configsToAdd.add(new Config("hdfsTargetConfigBean." + config.getName(), config.getValue()));
          break;
        case "hdfsUri":
        case "hdfsUser":
        case "hdfsKerberos":
        case "hdfsConfDir":
        case "hdfsConfigs":
        case "uniquePrefix":
        case "dirPathTemplate":
        case "timeZoneID":
        case "timeDriver":
        case "maxRecordsPerFile":
        case "maxFileSize":
        case "compression":
        case "otherCompression":
        case "fileType":
        case "keyEl":
        case "seqFileCompressionType":
        case "lateRecordsLimit":
        case "lateRecordsAction":
        case "lateRecordsDirPathTemplate":
          configsToRemove.add(config);
          configsToAdd.add(new Config("hdfsTargetConfigBean." + config.getName(), config.getValue()));
          break;
        case "charset":
        case "csvFileFormat":
        case "csvHeader":
        case "csvReplaceNewLines":
        case "jsonMode":
        case "textFieldPath":
        case "textEmptyLineIfNull":
        case "avroSchema":
        case "binaryFieldPath":
          configsToRemove.add(config);
          configsToAdd.add(new Config("hdfsTargetConfigBean.dataGeneratorFormatConfig." + config.getName(), config.getValue()));
          break;
      }
    }

    configs.removeAll(configsToRemove);
    configs.addAll(configsToAdd);

    configs.add(new Config("hdfsTargetConfigBean.dataGeneratorFormatConfig.csvCustomDelimiter", '|'));
    configs.add(new Config("hdfsTargetConfigBean.dataGeneratorFormatConfig.csvCustomEscape", '\\'));
    configs.add(new Config("hdfsTargetConfigBean.dataGeneratorFormatConfig.csvCustomQuote", '\"'));
  }
}
