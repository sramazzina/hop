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

package org.apache.hop.pipeline.transforms.stringcut;

import java.util.ArrayList;
import java.util.List;
import org.apache.hop.core.exception.HopException;
import org.apache.hop.core.row.IRowMeta;
import org.apache.hop.core.row.IValueMeta;
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
import org.apache.hop.ui.core.widget.TableView;
import org.apache.hop.ui.pipeline.transform.BaseTransformDialog;
import org.apache.hop.ui.pipeline.transform.ITableItemInsertListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class StringCutDialog extends BaseTransformDialog {

  private static final Class<?> PKG = StringCutMeta.class;

  private TableView wFields;

  private final StringCutMeta input;

  private final List<String> inputFields = new ArrayList<>();

  private ColumnInfo[] ciKey;

  public StringCutDialog(
      Shell parent, IVariables variables, StringCutMeta transformMeta, PipelineMeta pipelineMeta) {
    super(parent, variables, transformMeta, pipelineMeta);
    input = transformMeta;
  }

  @Override
  public String open() {
    Shell parent = getParent();

    shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MAX | SWT.MIN);
    PropsUi.setLook(shell);
    setShellImage(shell, input);

    ModifyListener lsMod = e -> input.setChanged();
    changed = input.hasChanged();

    FormLayout formLayout = new FormLayout();
    formLayout.marginWidth = PropsUi.getFormMargin();
    formLayout.marginHeight = PropsUi.getFormMargin();

    shell.setLayout(formLayout);
    shell.setText(BaseMessages.getString(PKG, "StringCutDialog.Shell.Title"));

    int middle = props.getMiddlePct();
    int margin = PropsUi.getMargin();

    // THE BUTTONS
    wOk = new Button(shell, SWT.PUSH);
    wOk.setText(BaseMessages.getString(PKG, "System.Button.OK"));
    wOk.addListener(SWT.Selection, e -> ok());
    wCancel = new Button(shell, SWT.PUSH);
    wCancel.setText(BaseMessages.getString(PKG, "System.Button.Cancel"));
    wCancel.addListener(SWT.Selection, e -> cancel());
    wGet = new Button(shell, SWT.PUSH);
    wGet.setText(BaseMessages.getString(PKG, "StringCutDialog.GetFields.Button"));
    wGet.addListener(SWT.Selection, e -> get());
    setButtonPositions(new Button[] {wOk, wGet, wCancel}, margin, null);

    // TransformName line
    wlTransformName = new Label(shell, SWT.RIGHT);
    wlTransformName.setText(BaseMessages.getString(PKG, "StringCutDialog.TransformName.Label"));
    PropsUi.setLook(wlTransformName);
    fdlTransformName = new FormData();
    fdlTransformName.left = new FormAttachment(0, 0);
    fdlTransformName.right = new FormAttachment(middle, -margin);
    fdlTransformName.top = new FormAttachment(0, margin);
    wlTransformName.setLayoutData(fdlTransformName);
    wTransformName = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
    wTransformName.setText(transformName);
    PropsUi.setLook(wTransformName);
    wTransformName.addModifyListener(lsMod);
    fdTransformName = new FormData();
    fdTransformName.left = new FormAttachment(middle, 0);
    fdTransformName.top = new FormAttachment(0, margin);
    fdTransformName.right = new FormAttachment(100, 0);
    wTransformName.setLayoutData(fdTransformName);

    Label wlKey = new Label(shell, SWT.NONE);
    wlKey.setText(BaseMessages.getString(PKG, "StringCutDialog.Fields.Label"));
    PropsUi.setLook(wlKey);
    FormData fdlKey = new FormData();
    fdlKey.left = new FormAttachment(0, 0);
    fdlKey.top = new FormAttachment(wTransformName, 2 * margin);
    wlKey.setLayoutData(fdlKey);

    int nrFieldCols = 4;
    int nrFieldRows =
        (input.getFields() != null && !input.getFields().isEmpty() ? input.getFields().size() : 1);

    ciKey = new ColumnInfo[nrFieldCols];
    ciKey[0] =
        new ColumnInfo(
            BaseMessages.getString(PKG, "StringCutDialog.ColumnInfo.InStreamField"),
            ColumnInfo.COLUMN_TYPE_CCOMBO,
            new String[] {""},
            false);
    ciKey[1] =
        new ColumnInfo(
            BaseMessages.getString(PKG, "StringCutDialog.ColumnInfo.OutStreamField"),
            ColumnInfo.COLUMN_TYPE_TEXT,
            false);
    ciKey[2] =
        new ColumnInfo(
            BaseMessages.getString(PKG, "StringCutDialog.ColumnInfo.CutFrom"),
            ColumnInfo.COLUMN_TYPE_TEXT,
            false);
    ciKey[3] =
        new ColumnInfo(
            BaseMessages.getString(PKG, "StringCutDialog.ColumnInfo.CutTo"),
            ColumnInfo.COLUMN_TYPE_TEXT,
            false);

    ciKey[2].setUsingVariables(true);
    ciKey[1].setToolTip(
        BaseMessages.getString(PKG, "StringCutDialog.ColumnInfo.OutStreamField.Tooltip"));
    ciKey[3].setUsingVariables(true);

    wFields =
        new TableView(
            variables,
            shell,
            SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI | SWT.V_SCROLL | SWT.H_SCROLL,
            ciKey,
            nrFieldRows,
            lsMod,
            props);

    FormData fdKey = new FormData();
    fdKey.left = new FormAttachment(0, 0);
    fdKey.top = new FormAttachment(wlKey, margin);
    fdKey.right = new FormAttachment(100, -margin);
    fdKey.bottom = new FormAttachment(wOk, -2 * margin);
    wFields.setLayoutData(fdKey);

    //
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
              logError("It was not possible to get the fields from the previous transform(s).");
            }
          }
        };
    new Thread(runnable).start();

    getData();
    input.setChanged(changed);

    BaseDialog.defaultShellHandling(shell, c -> ok(), c -> cancel());

    return transformName;
  }

  protected void setComboBoxes() {
    // Something was changed in the row.
    //
    String[] fieldNames = ConstUi.sortFieldNames(inputFields);
    ciKey[0].setComboValues(fieldNames);
  }

  /** Copy information from the meta-data input to the dialog fields. */
  public void getData() {
    if (input.getFields() != null && !input.getFields().isEmpty()) {
      int i = 0;
      for (StringCutField scf : input.getFields()) {
        TableItem item = wFields.table.getItem(i);
        if (scf.getFieldInStream() != null) {
          item.setText(1, scf.getFieldInStream());
        }
        if (scf.getFieldOutStream() != null) {
          item.setText(2, scf.getFieldOutStream());
        }
        if (scf.getCutFrom() != null) {
          item.setText(3, scf.getCutFrom());
        }
        if (scf.getCutTo() != null) {
          item.setText(4, scf.getCutTo());
        }
        i++;
      }
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

  private void getInfo(StringCutMeta inf) {

    int nrkeys = wFields.nrNonEmpty();
    inf.getFields().clear();

    if (log.isDebug()) {
      logDebug(
          BaseMessages.getString(PKG, "StringCutDialog.Log.FoundFields", String.valueOf(nrkeys)));
    }

    for (int i = 0; i < nrkeys; i++) {
      TableItem item = wFields.getNonEmpty(i);
      StringCutField scf =
          new StringCutField(item.getText(1), item.getText(2), item.getText(3), item.getText(4));
      inf.getFields().add(scf);
    }

    transformName = wTransformName.getText(); // return value
  }

  private void ok() {
    if (Utils.isEmpty(wTransformName.getText())) {
      return;
    }

    // Get the information for the dialog into the input structure.
    getInfo(input);

    dispose();
  }

  private void get() {
    try {
      IRowMeta r = pipelineMeta.getPrevTransformFields(variables, transformName);
      if (r != null) {
        ITableItemInsertListener listener =
            (tableItem, v) -> {
              if (v.getType() == IValueMeta.TYPE_STRING) {
                // Only process strings
                return true;
              } else {
                return false;
              }
            };

        BaseTransformDialog.getFieldsFromPrevious(
            r, wFields, 1, new int[] {1}, new int[] {}, -1, -1, listener);
      }
    } catch (HopException ke) {
      new ErrorDialog(
          shell,
          BaseMessages.getString(PKG, "StringCutDialog.FailedToGetFields.DialogTitle"),
          BaseMessages.getString(PKG, "StringCutDialog.FailedToGetFields.DialogMessage"),
          ke);
    }
  }
}
