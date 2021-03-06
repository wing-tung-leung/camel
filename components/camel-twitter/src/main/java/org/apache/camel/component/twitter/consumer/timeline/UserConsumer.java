/**
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

package org.apache.camel.component.twitter.consumer.timeline;

import java.util.Iterator;
import java.util.List;

import org.apache.camel.component.twitter.TwitterEndpoint;
import org.apache.camel.component.twitter.consumer.Twitter4JConsumer;
import org.apache.camel.component.twitter.data.Status;
import org.apache.camel.component.twitter.util.TwitterConverter;

import twitter4j.Paging;
import twitter4j.TwitterException;

/**
 * Consumes the timeline of a given user.
 */
public class UserConsumer implements Twitter4JConsumer {

    TwitterEndpoint te;

    public UserConsumer(TwitterEndpoint te) {
        this.te = te;
    }

    public Iterator<Status> requestPollingStatus(long lastStatusUpdateId) throws TwitterException {
        List<twitter4j.Status> statusList = te.getTwitter().getUserTimeline(te.getProperties().getUser(),
                                                                            new Paging(lastStatusUpdateId));
        return TwitterConverter.convertStatuses(statusList).iterator();
    }

    public Iterator<Status> requestDirectStatus() throws TwitterException {
        List<twitter4j.Status> statusList = te.getTwitter().getUserTimeline(te.getProperties().getUser());
        return TwitterConverter.convertStatuses(statusList).iterator();
    }
}
