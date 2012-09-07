/*
 * Copyright (C) FuseSource, Inc.
 * http://fusesource.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.fusesource.bai.config;

import org.apache.camel.model.language.ExpressionDefinition;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 * Represents a filter of exchanges using a Camel expression language
 */
@XmlRootElement(name = "filter")
@XmlAccessorType(XmlAccessType.FIELD)
public class ExchangeFilter extends HasFilter {
    public ExchangeFilter() {
    }

    public ExchangeFilter(ExpressionDefinition filter) {
        super(filter);
    }
}
