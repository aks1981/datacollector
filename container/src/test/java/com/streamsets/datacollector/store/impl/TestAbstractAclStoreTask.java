/**
 * Copyright 2017 StreamSets Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.streamsets.datacollector.store.impl;

import com.google.common.collect.ImmutableList;
import com.streamsets.datacollector.main.RuntimeInfo;
import com.streamsets.datacollector.main.UserGroupManager;
import com.streamsets.datacollector.restapi.bean.UserJson;
import com.streamsets.datacollector.store.PipelineStoreTask;
import com.streamsets.datacollector.util.AuthzRole;
import com.streamsets.datacollector.util.LockCache;
import com.streamsets.lib.security.acl.dto.Action;
import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

import java.util.Collections;

public class TestAbstractAclStoreTask {

  @Test
  public void testAdminBypassAcls() throws Exception {
    FileAclStoreTask fileAclStoreTask = new FileAclStoreTask(
        Mockito.mock(RuntimeInfo.class),
        Mockito.mock(PipelineStoreTask.class),
        new LockCache(),
        Mockito.mock(UserGroupManager.class)
    );
    UserJson userJson = new UserJson();
    userJson.setName("admin");
    userJson.setRoles(ImmutableList.of(AuthzRole.ADMIN));
    Assert.assertTrue(fileAclStoreTask.isPermissionGranted("foo", Collections.<Action>emptySet(), userJson));
  }
}
