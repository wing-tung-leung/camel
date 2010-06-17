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
package org.apache.camel.core.osgi;

import java.util.Collections;
import java.util.Set;

import org.apache.camel.core.osgi.utils.BundleDelegatingClassLoader;
import org.apache.camel.impl.DefaultPackageScanClassResolver;
import org.osgi.framework.Bundle;
import org.osgi.framework.BundleContext;

public class OsgiPackageScanClassResolver extends DefaultPackageScanClassResolver {
    private Bundle bundle;
    
    public OsgiPackageScanClassResolver(Bundle bundle) {
        this.bundle = bundle;
    }

    public OsgiPackageScanClassResolver(BundleContext context) {
        bundle = context.getBundle();
    }

    public Set<ClassLoader> getClassLoaders() {
        return Collections.<ClassLoader>singleton(new BundleDelegatingClassLoader(bundle));
    }
    

}