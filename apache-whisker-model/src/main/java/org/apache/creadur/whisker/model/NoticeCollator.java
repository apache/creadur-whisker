/**
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 *  to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 *  with the License.  You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License. 
 */
package org.apache.creadur.whisker.model;

import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;

/**
 * 
 */
public class NoticeCollator extends Visitor {

    private final Map<String, Collection<Resource>> resourcesByNoticeId = new HashMap<String, Collection<Resource>>();

    /**
     * @return the noticeIds
     */
    public Set<String> getNoticeIds() {
        return this.resourcesByNoticeId.keySet();
    }

    /**
     * @see Visitor#visit(Resource)
     */
    @Override
    public void visit(final Resource resource) {
        final String noticeId = resource.getNoticeId();
        if (noticeId != null) {
            if (!this.resourcesByNoticeId.containsKey(noticeId)) {
                this.resourcesByNoticeId.put(noticeId, new TreeSet<Resource>());
            }
            this.resourcesByNoticeId.get(noticeId).add(resource);
        }
    }

    public Map<String, Collection<Resource>> resourceNotices(
            final Map<String, String> notices) {
        final Map<String, Collection<Resource>> results = new HashMap<String, Collection<Resource>>();
        for (final Map.Entry<String, Collection<Resource>> entry : this.resourcesByNoticeId
                .entrySet()) {
            if (notices.containsKey(entry.getKey())) {
                results.put(notices.get(entry.getKey()), new TreeSet<Resource>(
                        entry.getValue()));
            } else {
                throw new IllegalArgumentException("Notice missing for id "
                        + entry.getKey());
            }
        }
        return results;
    }

    /**
     * @param notices map of notice-id and notices.
     * @return list of notices.
     */
    public Set<String> notices(final Map<String, String> notices) {
        final Set<String> results = new HashSet<String>();
        for (final String id : getNoticeIds()) {
            if (notices.containsKey(id)) {
                results.add(notices.get(id));
            } else {
                throw new IllegalArgumentException("Notice missing for id "
                        + id);
            }
        }
        return results;
    }
}
