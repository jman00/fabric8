/**
 *  Copyright 2005-2014 Red Hat, Inc.
 *
 *  Red Hat licenses this file to you under the Apache License, version
 *  2.0 (the "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or
 *  implied.  See the License for the specific language governing
 *  permissions and limitations under the License.
 */
package io.fabric8.partition.internal.repositories;

import io.fabric8.api.FabricException;
import io.fabric8.api.FabricService;
import io.fabric8.api.Profile;
import io.fabric8.api.ProfileRegistry;
import io.fabric8.api.ProfileService;
import io.fabric8.api.DataStore;
import io.fabric8.partition.internal.BaseWorkItemRepository;

import java.net.URL;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;
import com.google.common.collect.Lists;
import com.google.common.io.Resources;

public class ProfileWorkItemRepository extends BaseWorkItemRepository implements Runnable {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZkWorkItemRepository.class);

    private final String name;
    private final FabricService fabricService;
    private final DataStore dataStore;
    private final String profileId;
    private final String folderPath;

    private volatile String lastModified = "";

    public ProfileWorkItemRepository(String name, DataStore dataStore, String partitionsPath, FabricService fabricService) {
        this.name = name;
        this.dataStore = dataStore;
        this.fabricService = fabricService;
        int index = partitionsPath.indexOf("/");
        this.profileId = partitionsPath.substring((ProfileWorkItemRepositoryFactory.SCHEME + ":").length(), index);
        this.folderPath = partitionsPath.substring(index + 1);
    }


    @Override
    public void start() {
       dataStore.trackConfiguration(this);
       run();
    }

    @Override
    public void stop() {
       dataStore.untrackConfiguration(this);
    }

    @Override
    public void close() {
       stop();
    }

    @Override
    public List<String> listWorkItemLocations() {
        List<String> items = Lists.newArrayList();
        try {
            ProfileService profileService = fabricService.adapt(ProfileService.class);
            String version = dataStore.getContainerVersion(name);
            Profile p = profileService.getRequiredProfile(version, profileId);
            for (String f : p.getFileConfigurations().keySet()) {
                if (f.startsWith(folderPath)) {
                    items.add(f);
                }
            }
        } catch (Exception e) {
            LOGGER.debug("Error getting work items from profile repository. Returning empty.", e);
        }
        return items;
    }


    @Override
    public String readContent(String location) {
        try {
            return Resources.toString(new URL(ProfileWorkItemRepositoryFactory.SCHEME + ":" + location), Charsets.UTF_8);
        } catch (Exception e) {
            throw FabricException.launderThrowable(e);
        }
    }

    @Override
    public void run() {
       ProfileRegistry profileRegistry = fabricService.adapt(ProfileRegistry.class);
       String versionId = dataStore.getContainerVersion(name);
       Profile profile = profileRegistry.getProfile(versionId, versionId);
       if (profile != null && !profile.getProfileHash().equals(lastModified)) {
           notifyListeners();
           lastModified = profile.getProfileHash();
       }
    }
}
