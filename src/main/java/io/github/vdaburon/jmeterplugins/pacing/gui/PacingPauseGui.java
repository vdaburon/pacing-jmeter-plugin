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

package io.github.vdaburon.jmeterplugins.pacing.gui;

import io.github.vdaburon.jmeterplugins.pacing.PacingPause;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.layout.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class PacingPauseGui extends AbstractSamplerGui  {

	private static final long serialVersionUID = 2433007L;

    private static final Logger log = LoggerFactory.getLogger(PacingPauseGui.class);

    private static final String PACING_PAUSE_TITLE = "Pacing Pause"; // $NON-NLS-1$
    private static final String DEFAULT_PACING_PAUSE_VARIABLE_NAME = "";
    private static final String DEFAULT_PACING_DURATION_MS= "";

    private static final String PACING_PAUSE_VARIABLE_NAME = JMeterUtils.getPropDefault("pacing.default_variable_name", DEFAULT_PACING_PAUSE_VARIABLE_NAME); // $NON-NLS-1$
    private static final String PACING_PAUSE_DURATION_MS = JMeterUtils.getPropDefault("pacing.default_duration_ms", DEFAULT_PACING_DURATION_MS); // $NON-NLS-1$

    private JTextField variableNameTextField;
    private JTextField pacingDurationTextField;

    public PacingPauseGui() {
        super();
        init();
    }

    public String getStaticLabel() {
        return PACING_PAUSE_TITLE; // $NON-NLS-1$
    }

    public TestElement createTestElement() {
        log.debug("Begin createTestElement");
        PacingPause pacing = new PacingPause();
        variableNameTextField.setText(PACING_PAUSE_VARIABLE_NAME);
        pacingDurationTextField.setText(PACING_PAUSE_DURATION_MS);
        modifyTestElement(pacing);
        log.debug("End createTestElement");
        return pacing;
    }

    public void modifyTestElement(TestElement element) {
        super.configureTestElement(element);
        PacingPause pacing = (PacingPause) element;
        pacing.setVariableName(variableNameTextField.getText());
        pacing.setDuration(pacingDurationTextField.getText());
    }


    public String getLabelResource() {
        return PACING_PAUSE_TITLE;
    }

    private void init() { // WARNING: called from ctor so must not be overridden (i.e. must be private or final)
        setLayout(new VerticalLayout(5, VerticalLayout.BOTH, VerticalLayout.TOP));
        setBorder(makeBorder());
        add(makeTitlePanel());
        add(createPacingPausePanel());
    }

    public void clearUI() {
        variableNameTextField.setText("");
        pacingDurationTextField.setText("");
    }

    public void configure(TestElement element) {
        super.configure(element);
        PacingPause pacing = (PacingPause) element;
        variableNameTextField.setText(pacing.getVariableNameAsString());
        pacingDurationTextField.setText(pacing.getDurationAsString());
    }

    private JPanel createPacingPausePanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout. LEFT));

        JLabel variableNameLabel = new JLabel("Variable name contains start time ms:");
        variableNameTextField = new JTextField(PACING_PAUSE_VARIABLE_NAME, 50);

        JLabel pacingDurationLabel = new JLabel(" Pacing duration ms:");
        pacingDurationTextField = new JTextField(PACING_PAUSE_DURATION_MS, 50);

        JLabel computeLabel = new JLabel(" Pause = Pacing duration - (Current Time - Start Time)");
        panel.add(variableNameLabel);
        panel.add(variableNameTextField);
        panel.add(pacingDurationLabel);
        panel.add(pacingDurationTextField);
        panel.add(computeLabel);

        return panel;

    }

}
