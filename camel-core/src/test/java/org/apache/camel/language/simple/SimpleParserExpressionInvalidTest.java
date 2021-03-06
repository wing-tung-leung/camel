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
package org.apache.camel.language.simple;

import org.apache.camel.ExchangeTestSupport;
import org.apache.camel.language.simple.types.SimpleIllegalSyntaxException;

/**
 *
 */
public class SimpleParserExpressionInvalidTest extends ExchangeTestSupport {

    public void testSimpleUnbalanceFunction() throws Exception {
        SimpleExpressionParser parser = new SimpleExpressionParser("${body is a nice day");
        try {
            parser.parseExpression();
            fail("Should thrown exception");
        } catch (SimpleIllegalSyntaxException e) {
            assertEquals(19, e.getIndex());
        }
    }

    public void testSimpleNestedUnbalanceFunction() throws Exception {
        SimpleExpressionParser parser = new SimpleExpressionParser("${body${foo}");
        try {
            parser.parseExpression();
            fail("Should thrown exception");
        } catch (SimpleIllegalSyntaxException e) {
            assertEquals(11, e.getIndex());
        }
    }

    public void testSimpleUnknownFunction() throws Exception {
        SimpleExpressionParser parser = new SimpleExpressionParser("Hello ${foo} how are you?");
        try {
            parser.parseExpression();
            fail("Should thrown exception");
        } catch (SimpleIllegalSyntaxException e) {
            assertEquals(6, e.getIndex());
        }
    }

    public void testSimpleNestedUnknownFunction() throws Exception {
        SimpleExpressionParser parser = new SimpleExpressionParser("Hello ${bodyAs(${foo})} how are you?");
        try {
            // nested functions can only be syntax evaluated when evaluating an exchange at runtime
            parser.parseExpression().evaluate(exchange, String.class);
            fail("Should thrown exception");
        } catch (SimpleIllegalSyntaxException e) {
            assertEquals(15, e.getIndex());
        }
    }
    
    public void testNoEndFunction() throws Exception {
        SimpleExpressionParser parser = new SimpleExpressionParser("Hello ${body");
        try {
            parser.parseExpression();
            fail("Should thrown exception");
        } catch (SimpleIllegalSyntaxException e) {
            assertEquals(11, e.getIndex());
        }
    }

    public void testNoStartFunction() throws Exception {
        SimpleExpressionParser parser = new SimpleExpressionParser("Hello {body}");
        try {
            parser.parseExpression();
            fail("Should thrown exception");
        } catch (SimpleIllegalSyntaxException e) {
            assertEquals(11, e.getIndex());
        }
    }

    public void testNoStartFunction2() throws Exception {
        SimpleExpressionParser parser = new SimpleExpressionParser("Hello body}");
        try {
            parser.parseExpression();
            fail("Should thrown exception");
        } catch (SimpleIllegalSyntaxException e) {
            assertEquals(10, e.getIndex());
        }
    }

    public void testPropertyPlaceholderSyntax() throws Exception {
        SimpleExpressionParser parser = new SimpleExpressionParser("{{myserver}}/${header.myapp}");
        try {
            parser.parseExpression();
            fail("Should thrown exception");
        } catch (SimpleIllegalSyntaxException e) {
            assertEquals(10, e.getIndex());
        }
    }

}
