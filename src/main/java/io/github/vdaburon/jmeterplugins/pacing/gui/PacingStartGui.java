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

import io.github.vdaburon.jmeterplugins.pacing.PacingStart;
import org.apache.jmeter.samplers.gui.AbstractSamplerGui;
import org.apache.jmeter.testelement.TestElement;
import org.apache.jmeter.util.JMeterUtils;
import org.apache.jorphan.gui.layout.VerticalLayout;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.swing.*;
import java.awt.*;

public class PacingStartGui extends AbstractSamplerGui  {

	private static final long serialVersionUID = 2433008L;

    private static final Logger log = LoggerFactory.getLogger(PacingStartGui.class);

    private static final String PACING_START_TITLE = "Pacing Start"; // $NON-NLS-1$
    private static final String DEFAULT_PACING_START_VARIABLE_NAME = "";

    private static final String PACING_START_VARIABLE_NAME = JMeterUtils.getPropDefault("pacing.default_variable_name", DEFAULT_PACING_START_VARIABLE_NAME); // $NON-NLS-1$

    private JTextField variableNameTextField;

    public PacingStartGui() {
        super();
        init();
    }

    public String getStaticLabel() {
        return PACING_START_TITLE; // $NON-NLS-1$
    }

    public TestElement createTestElement() {
        log.debug("Begin createTestElement");
        PacingStart pacing = new PacingStart();
        variableNameTextField.setText(PACING_START_VARIABLE_NAME);
        modifyTestElement(pacing);
        log.debug("End createTestElement");
        return pacing;
    }

    public void modifyTestElement(TestElement element) {
        super.configureTestElement(element);
        PacingStart pacing = (PacingStart) element;
        pacing.setVariableName(variableNameTextField.getText());
    }
 
    public String getLabelResource() {
        return PACING_START_TITLE;
    }

    private void init() { // WARNING: called from ctor so must not be overridden (i.e. must be private or final)
        setLayout(new VerticalLayout(5, VerticalLayout.BOTH, VerticalLayout.TOP));
        setBorder(makeBorder());
        add(makeTitlePanel());
        add(createPacingStartPanel());
    }

    public void clearUI() {
        variableNameTextField.setText("");
    }

    public void configure(TestElement element) {
        super.configure(element);
        PacingStart pacing = (PacingStart) element;
        variableNameTextField.setText(pacing.getVariableNameAsString());
    }

    private JPanel createPacingStartPanel() {
        JPanel panel = new JPanel(new FlowLayout(FlowLayout. LEFT));

        JLabel variableNameLabel = new JLabel("Variable name will contain value start time ms:");
        variableNameTextField = new JTextField(PACING_START_VARIABLE_NAME, 50);
        JLabel startLabel = new JLabel(" Start = System.currentTimeMillis()");
        panel.add(variableNameLabel);
        panel.add(variableNameTextField);
        panel.add(startLabel);

        return panel;

    }

}
