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

package org.apache.hop.pipeline.transforms.fieldschangesequence;

import java.util.ArrayList;
import java.util.List;
import org.apache.hop.core.Const;
import org.apache.hop.core.exception.HopException;
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
import org.apache.hop.ui.core.dialog.MessageDialogWithToggle;
import org.apache.hop.ui.core.gui.GuiResource;
import org.apache.hop.ui.core.widget.ColumnInfo;
import org.apache.hop.ui.core.widget.TableView;
import org.apache.hop.ui.core.widget.TextVar;
import org.apache.hop.ui.pipeline.transform.BaseTransformDialog;
import org.apache.hop.ui.pipeline.transform.ITableItemInsertListener;
import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.layout.FormAttachment;
import org.eclipse.swt.layout.FormData;
import org.eclipse.swt.layout.FormLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.TableItem;
import org.eclipse.swt.widgets.Text;

public class FieldsChangeSequenceDialog extends BaseTransformDialog {
  private static final Class<?> PKG = FieldsChangeSequenceMeta.class;

  private final FieldsChangeSequenceMeta input;

  private TextVar wStart;

  private TextVar wIncrement;

  private TableView wFields;

  private Text wResult;

  private final List<String> inputFields = new ArrayList<>();

  private ColumnInfo[] colinf;

  public static final String STRING_CHANGE_SEQUENCE_WARNING_PARAMETER = "ChangeSequenceSortWarning";

  public FieldsChangeSequenceDialog(
      Shell parent,
      IVariables variables,
      FieldsChangeSequenceMeta transformMeta,
      PipelineMeta pipelineMeta) {
    super(parent, variables, transformMeta, pipelineMeta);
    input = transformMeta;
  }

  @Override
  public String open() {
    Shell parent = getParent();
    Display display = parent.getDisplay();

    shell = new Shell(parent, SWT.DIALOG_TRIM | SWT.RESIZE | SWT.MIN | SWT.MAX);
    PropsUi.setLook(shell);
    setShellImage(shell, input);

    ModifyListener lsMod = e -> input.setChanged();
    changed = input.hasChanged();

    FormLayout formLayout = new FormLayout();
    formLayout.marginWidth = PropsUi.getFormMargin();
    formLayout.marginHeight = PropsUi.getFormMargin();

    shell.setLayout(formLayout);
    shell.setText(BaseMessages.getString(PKG, "FieldsChangeSequenceDialog.Shell.Title"));

    int middle = props.getMiddlePct();
    int margin = PropsUi.getMargin();

    // TransformName line
    wlTransformName = new Label(shell, SWT.RIGHT);
    wlTransformName.setText(
        BaseMessages.getString(PKG, "FieldsChangeSequenceDialog.TransformName.Label"));
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

    // Result line...
    Label wlResult = new Label(shell, SWT.RIGHT);
    wlResult.setText(BaseMessages.getString(PKG, "FieldsChangeSequenceDialog.Result.Label"));
    PropsUi.setLook(wlResult);
    FormData fdlResult = new FormData();
    fdlResult.left = new FormAttachment(0, 0);
    fdlResult.right = new FormAttachment(middle, -margin);
    fdlResult.top = new FormAttachment(wTransformName, 2 * margin);
    wlResult.setLayoutData(fdlResult);
    wResult = new Text(shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
    PropsUi.setLook(wResult);
    wResult.addModifyListener(lsMod);
    FormData fdResult = new FormData();
    fdResult.left = new FormAttachment(middle, 0);
    fdResult.top = new FormAttachment(wTransformName, 2 * margin);
    fdResult.right = new FormAttachment(100, 0);
    wResult.setLayoutData(fdResult);

    // Start
    Label wlStart = new Label(shell, SWT.RIGHT);
    wlStart.setText(BaseMessages.getString(PKG, "FieldsChangeSequenceDialog.Start.Label"));
    PropsUi.setLook(wlStart);
    FormData fdlStart = new FormData();
    fdlStart.left = new FormAttachment(0, 0);
    fdlStart.right = new FormAttachment(middle, -margin);
    fdlStart.top = new FormAttachment(wResult, margin);
    wlStart.setLayoutData(fdlStart);
    wStart = new TextVar(variables, shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
    PropsUi.setLook(wStart);
    FormData fdStart = new FormData();
    fdStart.left = new FormAttachment(middle, 0);
    fdStart.top = new FormAttachment(wResult, margin);
    fdStart.right = new FormAttachment(100, 0);
    wStart.setLayoutData(fdStart);

    // Increment
    Label wlIncrement = new Label(shell, SWT.RIGHT);
    wlIncrement.setText(BaseMessages.getString(PKG, "FieldsChangeSequenceDialog.Increment.Label"));
    PropsUi.setLook(wlIncrement);
    FormData fdlIncrement = new FormData();
    fdlIncrement.left = new FormAttachment(0, 0);
    fdlIncrement.right = new FormAttachment(middle, -margin);
    fdlIncrement.top = new FormAttachment(wStart, margin);
    wlIncrement.setLayoutData(fdlIncrement);
    wIncrement = new TextVar(variables, shell, SWT.SINGLE | SWT.LEFT | SWT.BORDER);
    PropsUi.setLook(wIncrement);
    FormData fdIncrement = new FormData();
    fdIncrement.left = new FormAttachment(middle, 0);
    fdIncrement.top = new FormAttachment(wStart, margin);
    fdIncrement.right = new FormAttachment(100, 0);
    wIncrement.setLayoutData(fdIncrement);

    wOk = new Button(shell, SWT.PUSH);
    wOk.setText(BaseMessages.getString(PKG, "System.Button.OK"));
    wGet = new Button(shell, SWT.PUSH);
    wGet.setText(BaseMessages.getString(PKG, "System.Button.GetFields"));
    wCancel = new Button(shell, SWT.PUSH);
    wCancel.setText(BaseMessages.getString(PKG, "System.Button.Cancel"));

    setButtonPositions(new Button[] {wOk, wGet, wCancel}, margin, null);

    // Table with fields
    Label wlFields = new Label(shell, SWT.NONE);
    wlFields.setText(BaseMessages.getString(PKG, "FieldsChangeSequenceDialog.Fields.Label"));
    PropsUi.setLook(wlFields);
    FormData fdlFields = new FormData();
    fdlFields.left = new FormAttachment(0, 0);
    fdlFields.top = new FormAttachment(wIncrement, margin);
    wlFields.setLayoutData(fdlFields);

    final int FieldsCols = 1;
    final int FieldsRows = input.getFields().size();

    colinf = new ColumnInfo[FieldsCols];
    colinf[0] =
        new ColumnInfo(
            BaseMessages.getString(PKG, "FieldsChangeSequenceDialog.Fieldname.Column"),
            ColumnInfo.COLUMN_TYPE_CCOMBO,
            new String[] {""},
            false);
    wFields =
        new TableView(
            variables,
            shell,
            SWT.BORDER | SWT.FULL_SELECTION | SWT.MULTI,
            colinf,
            FieldsRows,
            lsMod,
            props);

    FormData fdFields = new FormData();
    fdFields.left = new FormAttachment(0, 0);
    fdFields.top = new FormAttachment(wlFields, margin);
    fdFields.right = new FormAttachment(100, 0);
    fdFields.bottom = new FormAttachment(wOk, -2 * margin);
    wFields.setLayoutData(fdFields);

    // Add listeners
    wCancel.addListener(SWT.Selection, e -> cancel());
    wOk.addListener(SWT.Selection, e -> ok());
    wGet.addListener(SWT.Selection, e -> get());

    getData();

    //
    // Search the fields in the background
    //

    final Runnable runnable =
        () -> {
          TransformMeta transformMeta = pipelineMeta.findTransform(transformName);
          if (transformMeta != null) {
            try {
              IRowMeta row = pipelineMeta.getPrevTransformFields(variables, transformMeta);
              if (row != null) {
                // Remember these fields...
                for (int i = 0; i < row.size(); i++) {
                  inputFields.add(row.getValueMeta(i).getName());
                }
                setComboBoxes();
              }

              // Dislay in red missing field names
              display.asyncExec(
                  () -> {
                    if (!wFields.isDisposed()) {
                      for (int i = 0; i < wFields.table.getItemCount(); i++) {
                        TableItem it = wFields.table.getItem(i);
                        if (!Utils.isEmpty(it.getText(1))
                            && (!inputFields.contains(it.getText(1)))) {
                          it.setBackground(GuiResource.getInstance().getColorRed());
                        }
                      }
                    }
                  });

            } catch (HopException e) {
              logError(
                  BaseMessages.getString(
                      PKG,
                      "FieldsChangeSequenceDialog.ErrorGettingPreviousFields",
                      e.getMessage()));
            }
          }
        };
    new Thread(runnable).start();

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
        ITableItemInsertListener insertListener =
            (tableItem, v) -> {
              tableItem.setText(2, BaseMessages.getString(PKG, "System.Combo.Yes"));
              return true;
            };
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
    wStart.setText(Const.NVL(input.getStart(), "1"));
    wIncrement.setText(Const.NVL(input.getIncrement(), "1"));
    wResult.setText(Const.NVL(input.getResultFieldName(), "result"));

    Table table = wFields.table;
    if (!input.getFields().isEmpty()) {
      table.removeAll();
    }
    List<FieldsChangeSequenceField> fields = input.getFields();
    for (int i = 0; i < fields.size(); i++) {
      TableItem ti = new TableItem(table, SWT.NONE);
      ti.setText(0, "" + (i + 1));
      ti.setText(1, fields.get(i).getName());
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

    input.setStart(wStart.getText());
    input.setIncrement(wIncrement.getText());
    input.setResultFieldName(wResult.getText());

    int nrFields = wFields.nrNonEmpty();
    List<FieldsChangeSequenceField> fieldName = new ArrayList<>();
    for (int i = 0; i < nrFields; i++) {
      TableItem ti = wFields.getNonEmpty(i);
      fieldName.add(new FieldsChangeSequenceField(ti.getText(1)));
    }
    input.setFields(fieldName);

    if ("Y"
        .equalsIgnoreCase(
            props.getCustomParameter(STRING_CHANGE_SEQUENCE_WARNING_PARAMETER, "Y"))) {
      MessageDialogWithToggle md =
          new MessageDialogWithToggle(
              shell,
              BaseMessages.getString(PKG, "FieldsChangeSequenceDialog.InputNeedSort.DialogTitle"),
              BaseMessages.getString(
                      PKG, "FieldsChangeSequenceDialog.InputNeedSort.DialogMessage", Const.CR)
                  + Const.CR,
              SWT.ICON_WARNING,
              new String[] {
                BaseMessages.getString(PKG, "FieldsChangeSequenceDialog.InputNeedSort.Option1")
              },
              BaseMessages.getString(PKG, "FieldsChangeSequenceDialog.InputNeedSort.Option2"),
              "N"
                  .equalsIgnoreCase(
                      props.getCustomParameter(STRING_CHANGE_SEQUENCE_WARNING_PARAMETER, "Y")));
      md.open();
      props.setCustomParameter(
          STRING_CHANGE_SEQUENCE_WARNING_PARAMETER, md.getToggleState() ? "N" : "Y");
    }

    dispose();
  }
}
