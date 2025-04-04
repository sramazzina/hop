/*
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

package org.apache.hop.ui.core.widget;

import org.apache.hop.ui.core.PropsUi;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

/** Adds a line of text with a label and a variable to a composite (like a dialog shell) */
public class LabelText extends Composite {
  private static final PropsUi props = PropsUi.getInstance();

  private Label wLabel;
  private Text wText;

  public LabelText(Composite composite, String labelText, String toolTipText) {
    this(
        composite,
        SWT.SINGLE | SWT.LEFT | SWT.BORDER,
        labelText,
        toolTipText,
        props.getMiddlePct(),
        PropsUi.getMargin());
  }

  public LabelText(
      Composite composite, String labelText, String toolTipText, int middle, int margin) {
    this(composite, SWT.SINGLE | SWT.LEFT | SWT.BORDER, labelText, toolTipText, middle, margin);
  }

  public LabelText(
      Composite composite,
      int textStyle,
      String labelText,
      String toolTipText,
      int middle,
      int margin) {
    super(composite, SWT.NONE);

    FormLayout formLayout = new FormLayout();
    formLayout.marginWidth = 0;
    formLayout.marginHeight = 0;
    this.setLayout(formLayout);

    wText = new Text(this, textStyle);
    PropsUi.setLook(wText);
    FormData fdText = new FormData();
    fdText.left = new FormAttachment(middle, 0);
    fdText.right = new FormAttachment(100, 0);
    wText.setLayoutData(fdText);
    wText.setToolTipText(toolTipText);

    wLabel = new Label(this, SWT.RIGHT);
    PropsUi.setLook(wLabel);
    wLabel.setText(labelText);
    FormData fdLabel = new FormData();
    fdLabel.left = new FormAttachment(0, 0);
    fdLabel.right = new FormAttachment(middle, -margin);
    fdLabel.top = new FormAttachment(wText, 0, SWT.CENTER);
    wLabel.setLayoutData(fdLabel);
    wLabel.setToolTipText(toolTipText);
  }

  public String getText() {
    return wText.getText();
  }

  public void setText(String string) {
    wText.setText(string);
  }

  public Text getTextWidget() {
    return wText;
  }

  public void addModifyListener(ModifyListener lsMod) {
    wText.addModifyListener(lsMod);
  }

  public void addSelectionListener(SelectionListener lsDef) {
    wText.addSelectionListener(lsDef);
  }

  @Override
  public void setEnabled(boolean flag) {
    wText.setEnabled(flag);
    wLabel.setEnabled(flag);
  }

  public void selectAll() {
    wText.selectAll();
  }

  @Override
  public boolean setFocus() {
    return wText.setFocus();
  }
}
