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

import org.apache.commons.lang3.StringUtils;
import org.apache.jmeter.samplers.AbstractSampler;
import org.apache.jmeter.samplers.Entry;
import org.apache.jmeter.samplers.Interruptible;
import org.apache.jmeter.samplers.SampleResult;
import org.apache.jmeter.testelement.property.StringProperty;
import org.apache.jmeter.threads.JMeterContext;
import org.apache.jmeter.threads.JMeterContextService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;


public class PacingPause extends AbstractSampler implements Interruptible {

    private static final Logger log = LoggerFactory.getLogger(PacingPause.class);
    private static final long serialVersionUID = 242007L;

    private static final String VARIABLE_NAME = "PacingPause.variableName";
    private static final String DURATION = "PacingPause.duration";

    private transient volatile Thread pauseThread;

    public SampleResult sample(Entry e) {

        JMeterContext context = JMeterContextService.getContext();

        String variableName = getVariableNameAsString();
        String duration = getDurationAsString();
        log.debug("variableName="+ variableName);
        log.debug("duration="+ duration);
        String variableValue = context.getVariables().get(variableName);
        if (variableValue == null) {
            log.warn("Variable value is null for variable: '{}' can't compute pacing", variableName);
            return null;
        }

        long lStartIterMillis = System.currentTimeMillis();
        try {
            if (!StringUtils.isEmpty(variableValue)) {
                lStartIterMillis = Long.parseLong(variableValue);
            } else {
                log.warn("Variable value start time is empty, defaulting to current time for variable '{}'", variableName);
            }
        } catch (NumberFormatException ex){
            log.warn("Variable value start time could not parse number: '{}' for variable '{}', defaulting to current time", variableValue, variableName);
        }

        String sPacingMillis = getDurationAsString();
        long pacingMillis;
        try {
            if (!StringUtils.isEmpty(sPacingMillis)) {
                pacingMillis = Long.parseLong(sPacingMillis);
            } else {
                log.warn("Pacing duration value is empty, defaulting to 0");
                pacingMillis = 0L;
            }
        } catch (NumberFormatException ex){
            log.warn("Pacing duration could not parse number: '{}'", sPacingMillis);
            pacingMillis=0L;
        }

        // Compute the dynamic waiting time to obtains the pacing time
        long lCurrentTime = System.currentTimeMillis();
        long lDurationIter = lCurrentTime - lStartIterMillis;

        long lWaitPacing = pacingMillis - lDurationIter;

        if (lWaitPacing <= 0) {
            lWaitPacing = 0;
        }

        if (lWaitPacing > 0) {
            pause(lWaitPacing);
        }
        return null; // This means no sample is saved
    }

    private void pause(long millis) {
        log.debug("pause, duration ask = " + millis);
         try {
            pauseThread = Thread.currentThread();
            if (millis > 0) {
                TimeUnit.MILLISECONDS.sleep(millis);
            }
         } catch (InterruptedException e) {
            log.debug("Pacing Pause got interrupted");
            Thread.currentThread().interrupt();
        } finally {
            pauseThread = null;
        }
    }

    public void setDuration(String duration) {
        setProperty(new StringProperty(DURATION, duration));
    }

    public String getDurationAsString() {
        return getPropertyAsString(DURATION);
    }

    public void setVariableName(String variableName) {
        setProperty(new StringProperty(VARIABLE_NAME, variableName));
    }

    public String getVariableNameAsString() {
        return getPropertyAsString(VARIABLE_NAME);
    }

    @Override
    public boolean interrupt() {
        Thread thrd = pauseThread; // take copy so cannot get NPE
        if (thrd!= null) {
            thrd.interrupt();
            return true;
        }
        return false;
    }
}