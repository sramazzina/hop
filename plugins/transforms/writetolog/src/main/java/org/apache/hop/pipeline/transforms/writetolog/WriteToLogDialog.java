/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.apache.hop.pipeline.transforms.writetolog;

import java.util.ArrayList;
import java.util.List;
import org.apache.hop.core.Const;
import org.apache.hop.core.Props;
import org.apache.hop.core.exception.HopException;
import org.apache.hop.core.logging.LogLevel;
import org.apache.hop.core.row.IRowMeta;
import org.apache.hop.core.util.Utils;
import org.apache.hop.core.variables.IVariables;
import org.apache.hop.i18n.BaseMessages;
import org.apache.hop.pipeline.PipelineMeta;
import org.apache.hop.pipeline.transform.TransformMeta;
import org.apache.hop.ui.core.ConstUi;
import org.apache.hop.ui.core.PropsUi;
import org.apache.hop.ui.core.dialog.BaseDialog;
import org.apache.hop.ui.core.dialog.ErrorDialog;
import org.apache.hop.ui.core.widget.ColumnInfo;
import org.apache.hop.ui.core.widget.StyledTextComp;
import org.apache.hop.ui.core.widget.TableView;
import org.apache.hop.ui.pipeline.transform.BaseTransformDialog;
import org.apache.hop.ui.pipeline.transform.ITableItemInsertListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.CCombo;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class WriteToLogDialog extends BaseTransformDialog {
  private static final Class<?> PKG = WriteToLogDialog.class;

  private final WriteToLogMeta input;

  private CCombo wLoglevel;
  private Button wPrintHeader;
  private StyledTextComp wLogMessage;
  private TableView wFields;
  private Button wLimitRows;
  private Label wlLimitRowsNumber;
  private Text wLimitRowsNumber;

  private final List<String> inputFields = new ArrayList<>();

  private ColumnInfo[] colinf;

  public WriteToLogDialog(
      Shell parent, IVariables variables, WriteToLogMeta transformMeta, PipelineMeta pipelineMeta) {
    super(parent, variables, transformMeta, pipelineMeta);
    input = transformMeta;
  }

  @Override
  public String open() {
    Shell parent = getParent();

    shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);
    PropsUi.setLook(shell);
    setShellImage(shell, input);

    Listener lsModify = event -> input.setChanged();

    changed = input.hasChanged();

    FormLayout formLayout = new FormLayout();
    formLayout.marginWidth = PropsUi.getFormMargin();
    formLayout.marginHeight = PropsUi.getFormMargin();

    shell.setLayout(formLayout);
    shell.setText(BaseMessages.getString(PKG, "WriteToLogDialog.Shell.Title"));

    int middle = props.getMiddlePct();
    int margin = PropsUi.getMargin();

    // TransformName line
    wlTransformName = new Label(shell, SWT.RIGHT);
    wlTransformName.setText(BaseMessages.getString(PKG, "WriteToLogDialog.TransformName.Label"));
    PropsUi.setLook(wlTransformName);
    fdlTransformName = new FormData();
    fdlTransformName.left = new FormAttachment(0, 0);
    fdlTransformName.right = new FormAttachment(middle, -margin);
    fdlTransformName.top = new FormAttachment(0, margin);
    wlTransformName.setLayoutData(fdlTransformName);
    wTransformName = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
    wTransformName.setText(transformName);
    PropsUi.setLook(wTransformName);
    wTransformName.addListener(SWT.Modify, lsModify);
    fdTransformName = new FormData();
    fdTransformName.left = new FormAttachment(middle, 0);
    fdTransformName.top = new FormAttachment(0, margin);
    fdTransformName.right = new FormAttachment(100, 0);
    wTransformName.setLayoutData(fdTransformName);

    // Log Level
    Label wlLoglevel = new Label(shell, SWT.RIGHT);
    wlLoglevel.setText(BaseMessages.getString(PKG, "WriteToLogDialog.Loglevel.Label"));
    PropsUi.setLook(wlLoglevel);
    FormData fdlLoglevel = new FormData();
    fdlLoglevel.left = new FormAttachment(0, 0);
    fdlLoglevel.right = new FormAttachment(middle, -margin);
    fdlLoglevel.top = new FormAttachment(wTransformName, margin);
    wlLoglevel.setLayoutData(fdlLoglevel);
    wLoglevel = new CCombo(shell, SWT.SINGLE | SWT.READ_ONLY | SWT.BORDER);
    wLoglevel.setItems(LogLevel.getLogLevelDescriptions());
    PropsUi.setLook(wLoglevel);
    FormData fdLoglevel = new FormData();
    fdLoglevel.left = new FormAttachment(middle, 0);
    fdLoglevel.top = new FormAttachment(wTransformName, margin);
    fdLoglevel.right = new FormAttachment(100, 0);
    wLoglevel.setLayoutData(fdLoglevel);
    wLoglevel.addListener(SWT.Selection, lsModify);

    // print header?
    Label wlPrintHeader = new Label(shell, SWT.RIGHT);
    wlPrintHeader.setText(BaseMessages.getString(PKG, "WriteToLogDialog.PrintHeader.Label"));
    PropsUi.setLook(wlPrintHeader);
    FormData fdlPrintHeader = new FormData();
    fdlPrintHeader.left = new FormAttachment(0, 0);
    fdlPrintHeader.top = new FormAttachment(wLoglevel, margin);
    fdlPrintHeader.right = new FormAttachment(middle, -margin);
    wlPrintHeader.setLayoutData(fdlPrintHeader);
    wPrintHeader = new Button(shell, SWT.CHECK);
    wPrintHeader.setToolTipText(
        BaseMessages.getString(PKG, "WriteToLogDialog.PrintHeader.Tooltip"));
    PropsUi.setLook(wPrintHeader);
    FormData fdPrintHeader = new FormData();
    fdPrintHeader.left = new FormAttachment(middle, 0);
    fdPrintHeader.top = new FormAttachment(wlPrintHeader, 0, SWT.CENTER);
    fdPrintHeader.right = new FormAttachment(100, 0);
    wPrintHeader.setLayoutData(fdPrintHeader);
    wPrintHeader.addListener(SWT.Selection, lsModify);

    // Limit output?
    // ICache?
    Label wlLimitRows = new Label(shell, SWT.RIGHT);
    wlLimitRows.setText(BaseMessages.getString(PKG, "WriteToLogDialog.LimitRows.Label"));
    PropsUi.setLook(wlLimitRows);
    FormData fdlLimitRows = new FormData();
    fdlLimitRows.left = new FormAttachment(0, 0);
    fdlLimitRows.right = new FormAttachment(middle, -margin);
    fdlLimitRows.top = new FormAttachment(wPrintHeader, margin);
    wlLimitRows.setLayoutData(fdlLimitRows);
    wLimitRows = new Button(shell, SWT.CHECK);
    PropsUi.setLook(wLimitRows);
    FormData fdLimitRows = new FormData();
    fdLimitRows.left = new FormAttachment(middle, 0);
    fdLimitRows.top = new FormAttachment(wlLimitRows, 0, SWT.CENTER);
    wLimitRows.setLayoutData(fdLimitRows);
    wLimitRows.addListener(SWT.Selection, lsModify);
    wLimitRows.addListener(SWT.Selection, e -> enableFields());

    // LimitRows size line
    wlLimitRowsNumber = new Label(shell, SWT.RIGHT);
    wlLimitRowsNumber.setText(
        BaseMessages.getString(PKG, "WriteToLogDialog.LimitRowsNumber.Label"));
    PropsUi.setLook(wlLimitRowsNumber);
    wlLimitRowsNumber.setEnabled(input.isLimitRows());
    FormData fdlLimitRowsNumber = new FormData();
    fdlLimitRowsNumber.left = new FormAttachment(0, 0);
    fdlLimitRowsNumber.right = new FormAttachment(middle, -margin);
    fdlLimitRowsNumber.top = new FormAttachment(wLimitRows, margin);
    wlLimitRowsNumber.setLayoutData(fdlLimitRowsNumber);
    wLimitRowsNumber = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
    PropsUi.setLook(wLimitRowsNumber);
    wLimitRowsNumber.setEnabled(input.isLimitRows());
    wLimitRowsNumber.addListener(SWT.Modify, lsModify);
    FormData fdLimitRowsNumber = new FormData();
    fdLimitRowsNumber.left = new FormAttachment(middle, 0);
    fdLimitRowsNumber.right = new FormAttachment(100, 0);
    fdLimitRowsNumber.top = new FormAttachment(wLimitRows, margin);
    wLimitRowsNumber.setLayoutData(fdLimitRowsNumber);

    // Log message to display
    Label wlLogMessage = new Label(shell, SWT.RIGHT);
    wlLogMessage.setText(BaseMessages.getString(PKG, "WriteToLogDialog.Shell.Title"));
    PropsUi.setLook(wlLogMessage);
    FormData fdlLogMessage = new FormData();
    fdlLogMessage.left = new FormAttachment(0, 0);
    fdlLogMessage.top = new FormAttachment(wLimitRowsNumber, margin);
    fdlLogMessage.right = new FormAttachment(middle, -margin);
    wlLogMessage.setLayoutData(fdlLogMessage);

    wLogMessage =
        new StyledTextComp(
            variables, shell, SWT.MULTI | SWT.LEFT | SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
    PropsUi.setLook(wLogMessage, Props.WIDGET_STYLE_FIXED);
    FormData fdLogMessage = new FormData();
    fdLogMessage.left = new FormAttachment(middle, 0);
    fdLogMessage.top = new FormAttachment(wLimitRowsNumber, margin);
    fdLogMessage.right = new FormAttachment(100, -margin);
    fdLogMessage.height = (int) (125 * props.getZoomFactor());
    wLogMessage.setLayoutData(fdLogMessage);
    wLogMessage.addListener(SWT.Modify, lsModify);

    wOk = new Button(shell, SWT.PUSH);
    wOk.setText(BaseMessages.getString(PKG, "System.Button.OK"));
    wGet = new Button(shell, SWT.PUSH);
    wGet.setText(BaseMessages.getString(PKG, "System.Button.GetFields"));
    wCancel = new Button(shell, SWT.PUSH);
    wCancel.setText(BaseMessages.getString(PKG, "System.Button.Cancel"));

    setButtonPositions(new Button[] {wOk, wGet, wCancel}, margin, null);

    // Table with fields
    Label wlFields = new Label(shell, SWT.NONE);
    wlFields.setText(BaseMessages.getString(PKG, "WriteToLogDialog.Fields.Label"));
    PropsUi.setLook(wlFields);
    FormData fdlFields = new FormData();
    fdlFields.left = new FormAttachment(0, 0);
    fdlFields.top = new FormAttachment(wLogMessage, margin);
    wlFields.setLayoutData(fdlFields);

    final int fieldsCols = 1;
    final int fieldsRows = input.getLogFields().size();

    colinf = new ColumnInfo[fieldsCols];
    colinf[0] =
        new ColumnInfo(
            BaseMessages.getString(PKG, "WriteToLogDialog.Fieldname.Column"),
            ColumnInfo.COLUMN_TYPE_CCOMBO,
            new String[] {""},
            false);
    wFields =
        new TableView(
            variables,
            shell,
            SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI,
            colinf,
            fieldsRows,
            event -> input.setChanged(),
            props);

    FormData fdFields = new FormData();
    fdFields.left = new FormAttachment(0, 0);
    fdFields.top = new FormAttachment(wlFields, margin);
    fdFields.right = new FormAttachment(100, 0);
    fdFields.bottom = new FormAttachment(wOk, -2 * margin);
    wFields.setLayoutData(fdFields);

    // Search the fields in the background
    //
    final Runnable runnable =
        () -> {
          TransformMeta transformMeta = pipelineMeta.findTransform(transformName);
          if (transformMeta != null) {
            try {
              IRowMeta row = pipelineMeta.getPrevTransformFields(variables, transformMeta);

              // Remember these fields...
              for (int i = 0; i < row.size(); i++) {
                inputFields.add(row.getValueMeta(i).getName());
              }
              setComboBoxes();
            } catch (HopException e) {
              logError(BaseMessages.getString(PKG, "System.Dialog.GetFieldsFailed.Message"));
            }
          }
        };
    new Thread(runnable).start();

    // Add listeners
    wCancel.addListener(SWT.Selection, e -> cancel());
    wOk.addListener(SWT.Selection, e -> ok());
    wGet.addListener(SWT.Selection, event -> get());

    getData();
    input.setChanged(changed);

    BaseDialog.defaultShellHandling(shell, c -> ok(), c -> cancel());

    return transformName;
  }

  protected void setComboBoxes() {
    // Something was changed in the row.
    //
    String[] fieldNames = ConstUi.sortFieldNames(inputFields);
    colinf[0].setComboValues(fieldNames);
  }

  private void get() {
    try {
      IRowMeta r = pipelineMeta.getPrevTransformFields(variables, transformName);
      if (r != null) {

        ITableItemInsertListener insertListener = (tableItem, v) -> true;

        BaseTransformDialog.getFieldsFromPrevious(
            r, wFields, 1, new int[] {1}, new int[] {}, -1, -1, insertListener);
      }
    } catch (HopException ke) {
      new ErrorDialog(
          shell,
          BaseMessages.getString(PKG, "System.Dialog.GetFieldsFailed.Title"),
          BaseMessages.getString(PKG, "System.Dialog.GetFieldsFailed.Message"),
          ke);
    }
  }

  /** Copy information from the meta-data input to the dialog fields. */
  public void getData() {

    wPrintHeader.setSelection(input.isDisplayHeader());
    wLimitRows.setSelection(input.isLimitRows());
    wLimitRowsNumber.setText("" + input.getLimitRowsNumber());

    LogLevel logLevel = input.getLogLevel();
    if (logLevel == null) {
      logLevel = LogLevel.BASIC;
    }
    wLoglevel.select(logLevel.getLevel());

    if (input.getLogMessage() != null) {
      wLogMessage.setText(input.getLogMessage());
    }

    for (int i = 0; i < input.getLogFields().size(); i++) {
      LogField field = input.getLogFields().get(i);
      TableItem ti = wFields.table.getItem(i);
      ti.setText(0, "" + (i + 1));
      ti.setText(1, field.getName());
    }

    wFields.setRowNums();
    wFields.optWidth(true);

    wTransformName.selectAll();
    wTransformName.setFocus();
  }

  private void cancel() {
    transformName = null;
    input.setChanged(changed);
    dispose();
  }

  private void ok() {
    if (Utils.isEmpty(wTransformName.getText())) {
      return;
    }
    transformName = wTransformName.getText(); // return value

    input.setDisplayHeader(wPrintHeader.getSelection());
    input.setLimitRows(wLimitRows.getSelection());
    input.setLimitRowsNumber(Const.toInt(wLimitRowsNumber.getText(), 0));

    if (wLoglevel.getSelectionIndex() < 0) {
      input.setLogLevel(LogLevel.BASIC);
    } else {
      input.setLogLevel(LogLevel.lookupCode(wLoglevel.getText()));
    }

    if (wLogMessage.getText() != null && !wLogMessage.getText().isEmpty()) {
      input.setLogMessage(wLogMessage.getText());
    } else {
      input.setLogMessage("");
    }

    int nrFields = wFields.nrNonEmpty();
    List<LogField> fields = new ArrayList<>(nrFields);
    for (int i = 0; i < nrFields; i++) {
      TableItem item = wFields.getNonEmpty(i);
      LogField field = new LogField();
      field.setName(item.getText(1));
      fields.add(field);
    }
    input.setLogFields(fields);

    dispose();
  }

  private void enableFields() {

    wLimitRowsNumber.setEnabled(wLimitRows.getSelection());
    wlLimitRowsNumber.setEnabled(wLimitRows.getSelection());
  }
}
