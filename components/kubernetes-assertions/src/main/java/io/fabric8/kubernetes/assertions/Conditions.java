/**
 *
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package io.fabric8.kubernetes.assertions;

import io.fabric8.kubernetes.api.KubernetesHelper;
import io.fabric8.kubernetes.api.PodStatus;
import io.fabric8.kubernetes.api.model.Pod;
import io.fabric8.kubernetes.api.model.ReplicationController;
import io.fabric8.kubernetes.api.model.Service;
import org.assertj.core.api.Condition;

import java.util.Map;
import java.util.Objects;

/**
 */
public class Conditions {

    public static Condition<Pod> status(final PodStatus status) {
        return new Condition<Pod>() {
            @Override
            public String toString() {
                return "podStatus(" + status + ")";
            }

            @Override
            public boolean matches(Pod pod) {
                return Objects.equals(status, KubernetesHelper.getPodStatus(pod));
            }
        };
    }

    public static Condition<Pod> runningStatus() {
        return status(PodStatus.OK);
    }

    public static Condition<Pod> waitingStatus() {
        return status(PodStatus.WAIT);
    }

    public static Condition<Pod> errorStatus() {
        return status(PodStatus.ERROR);
    }


    public static Condition<Pod> podLabel(final String key, final String value) {
        return new Condition<Pod>() {
            @Override
            public String toString() {
                return "podLabel(" + key + " = " + value + ")";
            }

            @Override
            public boolean matches(Pod pod) {
                return matchesLabel(pod.getLabels(), key, value);
            }
        };
    }

    public static Condition<Pod> podNamespace(final String namespace) {
        return new Condition<Pod>() {
            @Override
            public String toString() {
                return "podNamespace(" + namespace + ")";
            }

            @Override
            public boolean matches(Pod pod) {
                return Objects.equals(namespace, pod.getNamespace());
            }
        };
    }


    public static Condition<ReplicationController> replicationControllerLabel(final String key, final String value) {
        return new Condition<ReplicationController>() {
            @Override
            public String toString() {
                return "replicationControllerLabel(" + key + " = " + value + ")";
            }

            @Override
            public boolean matches(ReplicationController replicationControllerSchema) {
                return matchesLabel(replicationControllerSchema.getLabels(), key, value);
            }
        };
    }


    public static Condition<Service> serviceLabel(final String key, final String value) {
        return new Condition<Service>() {
            @Override
            public String toString() {
                return "serviceLabel(" + key + " = " + value + ")";
            }

            @Override
            public boolean matches(Service service) {
                return matchesLabel(service.getLabels(), key, value);
            }
        };
    }

    public static boolean matchesLabel(Map<String, String> labels, String key, String value) {
        if (labels != null) {
            String actual = labels.get(key);
            return Objects.equals(value, actual);
        } else {
            return false;
        }
    }

}
