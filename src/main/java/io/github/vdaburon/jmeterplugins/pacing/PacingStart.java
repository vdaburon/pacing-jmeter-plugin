/*
 * Copyright 2024 Vincent DABURON
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package io.github.vdaburon.jmeterplugins.pacing;

import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class PacingStart extends AbstractSampler  {

    private static final Logger log = LoggerFactory.getLogger(PacingStart.class);
     private static final long serialVersionUID = 242008L;

    private static final String VARIABLE_NAME = "PacingStart.variableName";

    public SampleResult sample(Entry e) {
        JMeterContext context = JMeterContextService.getContext();

        String variableName = getVariableNameAsString();
        log.debug("variableName="+ variableName);
        String startTime = "" + System.currentTimeMillis();
        // Add the variable to the thread context
        context.getVariables().put(variableName,startTime);
        return null; // This means no sample is saved
    }

    public void setVariableName(String variableName) {
        setProperty(new StringProperty(VARIABLE_NAME, variableName));
    }

    public String getVariableNameAsString() {
        return getPropertyAsString(VARIABLE_NAME);
    }
}