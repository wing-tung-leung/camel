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
package org.apache.camel.component.mina;

import org.apache.camel.ContextTestSupport;
import org.apache.camel.Exchange;
import org.apache.camel.Processor;
import org.apache.camel.builder.RouteBuilder;
import org.apache.camel.component.mock.MockEndpoint;

/**
 * @version $Revision$
 */
public class MinaToAsyncTest extends ContextTestSupport {

    public void testToAsync() throws Exception {
        MockEndpoint mock = getMockEndpoint("mock:result");
        mock.expectedBodiesReceivedInAnyOrder("Bye Camel", "Bye World", "Bye Donkey", "Bye Tiger", "Bye Elephant");

        template.sendBody("direct:start", "Camel");
        template.sendBody("direct:start", "World");
        template.sendBody("direct:start", "Donkey");
        template.sendBody("direct:start", "Tiger");
        template.sendBody("direct:start", "Elephant");

        assertMockEndpointsSatisfied();
    }

    @Override
    protected RouteBuilder createRouteBuilder() throws Exception {
        return new RouteBuilder() {
            @Override
            public void configure() throws Exception {
                from("mina:tcp://localhost:6202?textline=true&sync=true").process(new Processor() {
                    public void process(Exchange exchange) throws Exception {
                        String body = exchange.getIn().getBody(String.class);
                        Thread.sleep(2000);
                        exchange.getOut().setBody("Bye " + body);
                    }
                });

                from("direct:start")
                    .toAsync("mina:tcp://localhost:6202?sync=true&textline=true")
                    .to("log:reply")
                    .to("mock:result");
            }
        };
    }
}