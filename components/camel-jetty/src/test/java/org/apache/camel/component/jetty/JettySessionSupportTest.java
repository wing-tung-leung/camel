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
package org.apache.camel.component.jetty;

import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.test.junit4.CamelTestSupport;
import org.junit.Test;

import static org.apache.camel.language.simple.SimpleLanguage.simple;

/**
 * @version $Revision$
 */
public class JettySessionSupportTest extends CamelTestSupport {

    @Override
    public boolean isUseRouteBuilder() {
        return false;
    }

    @Test
    public void testJettySessionSupportInvalid() throws Exception {
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("jetty:http://localhost:9282/hello").to("mock:foo");

                from("jetty:http://localhost:9282/bye?sessionSupport=true").to("mock:bar");
            }
        });
        try {
            context.start();
            fail("Should have thrown an exception");
        } catch (IllegalStateException e) {
            assertEquals("Server has already been started. Cannot enabled sessionSupport on http:localhost:9282", e.getMessage());
        }
    }

    @Test
    public void testJettySessionSupportOk() throws Exception {
        context.addRoutes(new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("jetty:http://localhost:9283/hello?sessionSupport=true").transform(simple("Bye ${body}"));
            }
        });
        context.start();

        try {
            String reply = template.requestBody("http://localhost:9283/hello", "World", String.class);
            assertEquals("Bye World", reply);

            reply = template.requestBody("http://localhost:9283/hello", "Moon", String.class);
            assertEquals("Bye Moon", reply);
        } finally {
            context.stop();
        }
    }
}