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
package org.apache.hop.pipeline.transforms.userdefinedjavaclass;

import java.util.List;
import java.util.Map;
import org.apache.hop.core.BlockingRowSet;
import org.apache.hop.core.IRowSet;
import org.apache.hop.core.ResultFile;
import org.apache.hop.core.exception.HopException;
import org.apache.hop.core.exception.HopRowException;
import org.apache.hop.core.exception.HopTransformException;
import org.apache.hop.core.row.IRowMeta;
import org.apache.hop.i18n.BaseMessages;
import org.apache.hop.pipeline.Pipeline;
import org.apache.hop.pipeline.PipelineMeta;
import org.apache.hop.pipeline.engine.EngineComponent.ComponentExecutionStatus;
import org.apache.hop.pipeline.engine.IPipelineEngine;
import org.apache.hop.pipeline.transform.BaseTransform;
import org.apache.hop.pipeline.transform.IRowListener;
import org.apache.hop.pipeline.transform.ITransform;
import org.apache.hop.pipeline.transform.ITransformData;
import org.apache.hop.pipeline.transform.TransformMeta;

public class UserDefinedJavaClass
    extends BaseTransform<UserDefinedJavaClassMeta, UserDefinedJavaClassData> {
  private static final Class<?> PKG = UserDefinedJavaClassMeta.class;
  private TransformClassBase child;
  public static final String HOP_DEFAULT_CLASS_CACHE_SIZE = "HOP_DEFAULT_CLASS_CACHE_SIZE";

  public UserDefinedJavaClass(
      TransformMeta transformMeta,
      UserDefinedJavaClassMeta meta,
      UserDefinedJavaClassData data,
      int copyNr,
      PipelineMeta pipelineMeta,
      Pipeline pipeline) {
    super(transformMeta, meta, data, copyNr, pipelineMeta, pipeline);

    if (copyNr == 0) {
      try {
        meta.cookClasses();
      } catch (HopException e) {
        throw new RuntimeException(e);
      }
    }

    child = meta.newChildInstance(this, meta, data);

    if (!meta.cookErrors.isEmpty()) {
      for (Exception e : meta.cookErrors) {
        logErrorImpl("Error initializing UserDefinedJavaClass:", e);
      }
      setErrorsImpl(meta.cookErrors.size());
      stopAllImpl();
    }
  }

  @Override
  public void addResultFile(ResultFile resultFile) {
    if (child == null) {
      addResultFileImpl(resultFile);
    } else {
      child.addResultFile(resultFile);
    }
  }

  public void addResultFileImpl(ResultFile resultFile) {
    super.addResultFile(resultFile);
  }

  @Override
  public void addRowListener(IRowListener rowListener) {
    if (child == null) {
      addRowListenerImpl(rowListener);
    } else {
      child.addRowListener(rowListener);
    }
  }

  public void addRowListenerImpl(IRowListener rowListener) {
    super.addRowListener(rowListener);
  }

  @Override
  public boolean checkFeedback(long lines) {
    if (child == null) {
      return checkFeedbackImpl(lines);
    } else {
      return child.checkFeedback(lines);
    }
  }

  public boolean checkFeedbackImpl(long lines) {
    return super.checkFeedback(lines);
  }

  @Override
  public void cleanup() {
    if (child == null) {
      cleanupImpl();
    } else {
      child.cleanup();
    }
  }

  public void cleanupImpl() {
    super.cleanup();
  }

  @Override
  public long decrementLinesRead() {
    if (child == null) {
      return decrementLinesReadImpl();
    } else {
      return child.decrementLinesRead();
    }
  }

  public long decrementLinesReadImpl() {
    return super.decrementLinesRead();
  }

  @Override
  public long decrementLinesWritten() {
    if (child == null) {
      return decrementLinesWrittenImpl();
    } else {
      return child.decrementLinesWritten();
    }
  }

  public long decrementLinesWrittenImpl() {
    return super.decrementLinesWritten();
  }

  public void disposeImpl() {
    super.dispose();
  }

  @Override
  public IRowSet findInputRowSet(String sourceTransform) throws HopTransformException {
    if (child == null) {
      return findInputRowSetImpl(sourceTransform);
    } else {
      return child.findInputRowSet(sourceTransform);
    }
  }

  @Override
  public IRowSet findInputRowSet(String from, int fromcopy, String to, int tocopy) {
    if (child == null) {
      return findInputRowSetImpl(from, fromcopy, to, tocopy);
    } else {
      return child.findInputRowSet(from, fromcopy, to, tocopy);
    }
  }

  public IRowSet findInputRowSetImpl(String sourceTransform) throws HopTransformException {
    return super.findInputRowSet(sourceTransform);
  }

  public IRowSet findInputRowSetImpl(String from, int fromcopy, String to, int tocopy) {
    return super.findInputRowSet(from, fromcopy, to, tocopy);
  }

  @Override
  public IRowSet findOutputRowSet(String targetTransform) throws HopTransformException {
    if (child == null) {
      return findOutputRowSetImpl(targetTransform);
    } else {
      return child.findOutputRowSet(targetTransform);
    }
  }

  @Override
  public IRowSet findOutputRowSet(String from, int fromcopy, String to, int tocopy) {
    if (child == null) {
      return findOutputRowSetImpl(from, fromcopy, to, tocopy);
    } else {
      return child.findOutputRowSet(from, fromcopy, to, tocopy);
    }
  }

  public IRowSet findOutputRowSetImpl(String targetTransform) throws HopTransformException {
    return super.findOutputRowSet(targetTransform);
  }

  public IRowSet findOutputRowSetImpl(String from, int fromcopy, String to, int tocopy) {
    return super.findOutputRowSet(from, fromcopy, to, tocopy);
  }

  public int getCopyImpl() {
    return super.getCopy();
  }

  @Override
  public IRowMeta getErrorRowMeta() {
    if (child == null) {
      return getErrorRowMetaImpl();
    } else {
      return child.getErrorRowMeta();
    }
  }

  public IRowMeta getErrorRowMetaImpl() {
    return super.getErrorRowMeta();
  }

  @Override
  public long getErrors() {
    if (child == null) {
      return getErrorsImpl();
    } else {
      return child.getErrors();
    }
  }

  public long getErrorsImpl() {
    return super.getErrors();
  }

  @Override
  public IRowMeta getInputRowMeta() {
    if (child == null) {
      return getInputRowMetaImpl();
    } else {
      return child.getInputRowMeta();
    }
  }

  public IRowMeta getInputRowMetaImpl() {
    return super.getInputRowMeta();
  }

  @Override
  public List<IRowSet> getInputRowSets() {
    return child == null ? getInputRowSetsImpl() : child.getInputRowSets();
  }

  public List<IRowSet> getInputRowSetsImpl() {
    return super.getInputRowSets();
  }

  @Override
  public long getLinesInput() {
    if (child == null) {
      return getLinesInputImpl();
    } else {
      return child.getLinesInput();
    }
  }

  public long getLinesInputImpl() {
    return super.getLinesInput();
  }

  @Override
  public long getLinesOutput() {
    if (child == null) {
      return getLinesOutputImpl();
    } else {
      return child.getLinesOutput();
    }
  }

  public long getLinesOutputImpl() {
    return super.getLinesOutput();
  }

  @Override
  public long getLinesRead() {
    if (child == null) {
      return getLinesReadImpl();
    } else {
      return child.getLinesRead();
    }
  }

  public long getLinesReadImpl() {
    return super.getLinesRead();
  }

  @Override
  public long getLinesRejected() {
    if (child == null) {
      return getLinesRejectedImpl();
    } else {
      return child.getLinesRejected();
    }
  }

  public long getLinesRejectedImpl() {
    return super.getLinesRejected();
  }

  @Override
  public long getLinesSkipped() {
    if (child == null) {
      return getLinesSkippedImpl();
    } else {
      return child.getLinesSkipped();
    }
  }

  public long getLinesSkippedImpl() {
    return super.getLinesSkipped();
  }

  @Override
  public long getLinesUpdated() {
    if (child == null) {
      return getLinesUpdatedImpl();
    } else {
      return child.getLinesUpdated();
    }
  }

  public long getLinesUpdatedImpl() {
    return super.getLinesUpdated();
  }

  @Override
  public long getLinesWritten() {
    if (child == null) {
      return getLinesWrittenImpl();
    } else {
      return child.getLinesWritten();
    }
  }

  public long getLinesWrittenImpl() {
    return super.getLinesWritten();
  }

  @Override
  public List<IRowSet> getOutputRowSets() {
    if (child == null) {
      return getOutputRowSetsImpl();
    } else {
      return child.getOutputRowSets();
    }
  }

  public List<IRowSet> getOutputRowSetsImpl() {
    return super.getOutputRowSets();
  }

  @Override
  public String getPartitionId() {
    if (child == null) {
      return getPartitionIdImpl();
    } else {
      return child.getPartitionId();
    }
  }

  public String getPartitionIdImpl() {
    return super.getPartitionId();
  }

  @Override
  public Map<String, BlockingRowSet> getPartitionTargets() {
    if (child == null) {
      return getPartitionTargetsImpl();
    } else {
      return child.getPartitionTargets();
    }
  }

  public Map<String, BlockingRowSet> getPartitionTargetsImpl() {
    return super.getPartitionTargets();
  }

  @Override
  public long getProcessed() {
    if (child == null) {
      return getProcessedImpl();
    } else {
      return child.getProcessed();
    }
  }

  public long getProcessedImpl() {
    return super.getProcessed();
  }

  @Override
  public int getRepartitioning() {
    if (child == null) {
      return getRepartitioningImpl();
    } else {
      return child.getRepartitioning();
    }
  }

  public int getRepartitioningImpl() {
    return super.getRepartitioning();
  }

  @Override
  public Map<String, ResultFile> getResultFiles() {
    if (child == null) {
      return getResultFilesImpl();
    } else {
      return child.getResultFiles();
    }
  }

  public Map<String, ResultFile> getResultFilesImpl() {
    return super.getResultFiles();
  }

  @Override
  public Object[] getRow() throws HopException {
    if (child == null) {
      return getRowImpl();
    } else {
      return child.getRow();
    }
  }

  @Override
  public Object[] getRowFrom(IRowSet rowSet) throws HopTransformException {
    if (child == null) {
      return getRowFromImpl(rowSet);
    } else {
      return child.getRowFrom(rowSet);
    }
  }

  public Object[] getRowFromImpl(IRowSet rowSet) throws HopTransformException {
    return super.getRowFrom(rowSet);
  }

  public Object[] getRowImpl() throws HopException {
    return super.getRow();
  }

  @Override
  public List<IRowListener> getRowListeners() {
    if (child == null) {
      return getRowListenersImpl();
    } else {
      return child.getRowListeners();
    }
  }

  public List<IRowListener> getRowListenersImpl() {
    return super.getRowListeners();
  }

  @Override
  public ComponentExecutionStatus getStatus() {
    if (child == null) {
      return getStatusImpl();
    } else {
      return child.getStatus();
    }
  }

  @Override
  public String getStatusDescription() {
    if (child == null) {
      return getStatusDescriptionImpl();
    } else {
      return child.getStatusDescription();
    }
  }

  public String getStatusDescriptionImpl() {
    return super.getStatusDescription();
  }

  public ComponentExecutionStatus getStatusImpl() {
    return super.getStatus();
  }

  @Override
  public String getTransformPluginId() {
    if (child == null) {
      return getTransformPluginIdImpl();
    } else {
      return child.getTransformPluginId();
    }
  }

  public String getTransformPluginIdImpl() {
    return super.getTransformPluginId();
  }

  @Override
  public TransformMeta getTransformMeta() {
    if (child == null) {
      return getTransformMetaImpl();
    } else {
      return child.getTransformMeta();
    }
  }

  public TransformMeta getTransformMetaImpl() {
    return super.getTransformMeta();
  }

  @Override
  public String getTransformName() {
    if (child == null) {
      return getTransformNameImpl();
    } else {
      return child.getTransformName();
    }
  }

  public String getTransformNameImpl() {
    return super.getTransformName();
  }

  public IPipelineEngine getPipelineImpl() {
    return super.getPipeline();
  }

  @Override
  public PipelineMeta getPipelineMeta() {
    if (child == null) {
      return getPipelineMetaImpl();
    } else {
      return child.getPipelineMeta();
    }
  }

  public PipelineMeta getPipelineMetaImpl() {
    return super.getPipelineMeta();
  }

  @Override
  public String getVariable(String variableName) {
    if (child == null) {
      return getVariableImpl(variableName);
    } else {
      return child.getVariable(variableName);
    }
  }

  @Override
  public String getVariable(String variableName, String defaultValue) {
    if (child == null) {
      return getVariableImpl(variableName, defaultValue);
    } else {
      return child.getVariable(variableName, defaultValue);
    }
  }

  public String getVariableImpl(String variableName) {
    return super.getVariable(variableName);
  }

  public String getVariableImpl(String variableName, String defaultValue) {
    return super.getVariable(variableName, defaultValue);
  }

  @Override
  public long incrementLinesInput() {
    if (child == null) {
      return incrementLinesInputImpl();
    } else {
      return child.incrementLinesInput();
    }
  }

  public long incrementLinesInputImpl() {
    return super.incrementLinesInput();
  }

  @Override
  public long incrementLinesOutput() {
    if (child == null) {
      return incrementLinesOutputImpl();
    } else {
      return child.incrementLinesOutput();
    }
  }

  public long incrementLinesOutputImpl() {
    return super.incrementLinesOutput();
  }

  @Override
  public long incrementLinesRead() {
    if (child == null) {
      return incrementLinesReadImpl();
    } else {
      return child.incrementLinesRead();
    }
  }

  public long incrementLinesReadImpl() {
    return super.incrementLinesRead();
  }

  @Override
  public long incrementLinesRejected() {
    if (child == null) {
      return incrementLinesRejectedImpl();
    } else {
      return child.incrementLinesRejected();
    }
  }

  public long incrementLinesRejectedImpl() {
    return super.incrementLinesRejected();
  }

  @Override
  public long incrementLinesSkipped() {
    if (child == null) {
      return incrementLinesSkippedImpl();
    } else {
      return child.incrementLinesSkipped();
    }
  }

  public long incrementLinesSkippedImpl() {
    return super.incrementLinesSkipped();
  }

  @Override
  public long incrementLinesUpdated() {
    if (child == null) {
      return incrementLinesUpdatedImpl();
    } else {
      return child.incrementLinesUpdated();
    }
  }

  public long incrementLinesUpdatedImpl() {
    return super.incrementLinesUpdated();
  }

  @Override
  public long incrementLinesWritten() {
    if (child == null) {
      return incrementLinesWrittenImpl();
    } else {
      return child.incrementLinesWritten();
    }
  }

  public long incrementLinesWrittenImpl() {
    return super.incrementLinesWritten();
  }

  @Override
  public boolean init() {
    if (!meta.cookErrors.isEmpty()) {
      return false;
    }

    if (meta.cookedTransformClass == null) {
      logError("No UDFC marked as Pipeline class");
      return false;
    }

    if (child == null) {
      return initImpl();
    } else {
      return child.init();
    }
  }

  @Override
  public void initBeforeStart() throws HopTransformException {
    if (child == null) {
      initBeforeStartImpl();
    } else {
      child.initBeforeStart();
    }
  }

  public void initBeforeStartImpl() throws HopTransformException {
    super.initBeforeStart();
  }

  public boolean initImpl() {
    return super.init();
  }

  @Override
  public boolean isDistributed() {
    if (child == null) {
      return isDistributedImpl();
    } else {
      return child.isDistributed();
    }
  }

  public boolean isDistributedImpl() {
    return super.isDistributed();
  }

  @Override
  public boolean isInitialising() {
    if (child == null) {
      return isInitialisingImpl();
    } else {
      return child.isInitialising();
    }
  }

  public boolean isInitialisingImpl() {
    return super.isInitialising();
  }

  @Override
  public boolean isPartitioned() {
    if (child == null) {
      return isPartitionedImpl();
    } else {
      return child.isPartitioned();
    }
  }

  public boolean isPartitionedImpl() {
    return super.isPartitioned();
  }

  public boolean isSafeModeEnabled() {
    if (child == null) {
      return isSafeModeEnabledImpl();
    } else {
      return child.isSafeModeEnabled();
    }
  }

  public boolean isSafeModeEnabledImpl() {
    return getPipeline().isSafeModeEnabled();
  }

  @Override
  public boolean isStopped() {
    if (child == null) {
      return isStoppedImpl();
    } else {
      return child.isStopped();
    }
  }

  public boolean isStoppedImpl() {
    return super.isStopped();
  }

  @Override
  public void logBasic(String s) {
    if (child == null) {
      logBasicImpl(s);
    } else {
      child.logBasic(s);
    }
  }

  public void logBasicImpl(String s) {
    super.logBasic(s);
  }

  @Override
  public void logDebug(String s) {
    if (child == null) {
      logDebugImpl(s);
    } else {
      child.logDebug(s);
    }
  }

  public void logDebugImpl(String s) {
    super.logDebug(s);
  }

  @Override
  public void logDetailed(String s) {
    if (child == null) {
      logDetailedImpl(s);
    } else {
      child.logDetailed(s);
    }
  }

  public void logDetailedImpl(String s) {
    super.logDetailed(s);
  }

  @Override
  public void logError(String s) {
    if (child == null) {
      logErrorImpl(s);
    } else {
      child.logError(s);
    }
  }

  @Override
  public void logError(String s, Throwable e) {
    if (child == null) {
      logErrorImpl(s, e);
    } else {
      child.logError(s, e);
    }
  }

  public void logErrorImpl(String s) {
    super.logError(s);
  }

  public void logErrorImpl(String s, Throwable e) {
    super.logError(s, e);
  }

  @Override
  public void logMinimal(String s) {
    if (child == null) {
      logMinimalImpl(s);
    } else {
      child.logMinimal(s);
    }
  }

  public void logMinimalImpl(String s) {
    super.logMinimal(s);
  }

  @Override
  public void logRowlevel(String s) {
    if (child == null) {
      logRowlevelImpl(s);
    } else {
      child.logRowlevel(s);
    }
  }

  public void logRowlevelImpl(String s) {
    super.logRowlevel(s);
  }

  @Override
  public void logSummary() {
    if (child == null) {
      logSummaryImpl();
    } else {
      child.logSummary();
    }
  }

  public void logSummaryImpl() {
    super.logSummary();
  }

  @Override
  public void markStart() {
    if (child == null) {
      markStartImpl();
    } else {
      child.markStart();
    }
  }

  public void markStartImpl() {
    super.markStart();
  }

  @Override
  public void markStop() {
    if (child == null) {
      markStopImpl();
    } else {
      child.markStop();
    }
  }

  public void markStopImpl() {
    super.markStop();
  }

  @Override
  public boolean outputIsDone() {
    if (child == null) {
      return outputIsDoneImpl();
    } else {
      return child.outputIsDone();
    }
  }

  public boolean outputIsDoneImpl() {
    return super.outputIsDone();
  }

  @Override
  public boolean processRow() throws HopException {
    if (child == null) {
      return false;
    } else {
      try {
        return child.processRow();
      } catch (Exception e) {
        logError(BaseMessages.getString(PKG, "UserDefinedJavaClass.ErrorInTransformRunning"), e);
        setErrors(1);
        setOutputDone();
        stopAll();
        return false;
      }
    }
  }

  @Override
  public void putError(
      IRowMeta rowMeta,
      Object[] row,
      long nrErrors,
      String errorDescriptions,
      String fieldNames,
      String errorCodes)
      throws HopTransformException {
    if (child == null) {
      putErrorImpl(rowMeta, row, nrErrors, errorDescriptions, fieldNames, errorCodes);
    } else {
      child.putError(rowMeta, row, nrErrors, errorDescriptions, fieldNames, errorCodes);
    }
  }

  public void putErrorImpl(
      IRowMeta rowMeta,
      Object[] row,
      long nrErrors,
      String errorDescriptions,
      String fieldNames,
      String errorCodes)
      throws HopTransformException {
    super.putError(rowMeta, row, nrErrors, errorDescriptions, fieldNames, errorCodes);
  }

  @Override
  public void putRow(IRowMeta row, Object[] data) throws HopTransformException {
    if (child == null) {
      putRowImpl(row, data);
    } else {
      child.putRow(row, data);
    }
  }

  public void putRowImpl(IRowMeta row, Object[] data) throws HopTransformException {
    super.putRow(row, data);
  }

  @Override
  public void putRowTo(IRowMeta rowMeta, Object[] row, IRowSet rowSet)
      throws HopTransformException {
    if (child == null) {
      putRowToImpl(rowMeta, row, rowSet);
    } else {
      child.putRowTo(rowMeta, row, rowSet);
    }
  }

  public void putRowToImpl(IRowMeta rowMeta, Object[] row, IRowSet rowSet)
      throws HopTransformException {
    super.putRowTo(rowMeta, row, rowSet);
  }

  @Override
  public void removeRowListener(IRowListener rowListener) {
    if (child == null) {
      removeRowListenerImpl(rowListener);
    } else {
      child.removeRowListener(rowListener);
    }
  }

  public void removeRowListenerImpl(IRowListener rowListener) {
    super.removeRowListener(rowListener);
  }

  @Override
  public int rowsetInputSize() {
    if (child == null) {
      return rowsetInputSizeImpl();
    } else {
      return child.rowsetInputSize();
    }
  }

  public int rowsetInputSizeImpl() {
    return super.rowsetInputSize();
  }

  @Override
  public int rowsetOutputSize() {
    if (child == null) {
      return rowsetOutputSizeImpl();
    } else {
      return child.rowsetOutputSize();
    }
  }

  public int rowsetOutputSizeImpl() {
    return super.rowsetOutputSize();
  }

  @Override
  public void safeModeChecking(IRowMeta row) throws HopRowException {
    if (child == null) {
      safeModeCheckingImpl(row);
    } else {
      child.safeModeChecking(row);
    }
  }

  public void safeModeCheckingImpl(IRowMeta row) throws HopRowException {
    super.safeModeChecking(row);
  }

  @Override
  public void setErrors(long errors) {
    if (child == null) {
      setErrorsImpl(errors);
    } else {
      child.setErrors(errors);
    }
  }

  public void setErrorsImpl(long errors) {
    super.setErrors(errors);
  }

  @Override
  public void setInputRowMeta(IRowMeta rowMeta) {
    if (child == null) {
      setInputRowMetaImpl(rowMeta);
    } else {
      child.setInputRowMeta(rowMeta);
    }
  }

  public void setInputRowMetaImpl(IRowMeta rowMeta) {
    super.setInputRowMeta(rowMeta);
  }

  @Override
  public void setInputRowSets(List<IRowSet> inputRowSets) {
    if (child == null) {
      setInputRowSetsImpl(inputRowSets);
    } else {
      child.setInputRowSets(inputRowSets);
    }
  }

  public void setInputRowSetsImpl(List<IRowSet> inputRowSets) {
    super.setInputRowSets(inputRowSets);
  }

  @Override
  public void setLinesInput(long newLinesInputValue) {
    if (child == null) {
      setLinesInputImpl(newLinesInputValue);
    } else {
      child.setLinesInput(newLinesInputValue);
    }
  }

  public void setLinesInputImpl(long newLinesInputValue) {
    super.setLinesInput(newLinesInputValue);
  }

  @Override
  public void setLinesOutput(long newLinesOutputValue) {
    if (child == null) {
      setLinesOutputImpl(newLinesOutputValue);
    } else {
      child.setLinesOutput(newLinesOutputValue);
    }
  }

  public void setLinesOutputImpl(long newLinesOutputValue) {
    super.setLinesOutput(newLinesOutputValue);
  }

  @Override
  public void setLinesRead(long newLinesReadValue) {
    if (child == null) {
      setLinesReadImpl(newLinesReadValue);
    } else {
      child.setLinesRead(newLinesReadValue);
    }
  }

  public void setLinesReadImpl(long newLinesReadValue) {
    super.setLinesRead(newLinesReadValue);
  }

  @Override
  public void setLinesRejected(long linesRejected) {
    if (child == null) {
      setLinesRejectedImpl(linesRejected);
    } else {
      child.setLinesRejected(linesRejected);
    }
  }

  public void setLinesRejectedImpl(long linesRejected) {
    super.setLinesRejected(linesRejected);
  }

  @Override
  public void setLinesSkipped(long newLinesSkippedValue) {
    if (child == null) {
      setLinesSkippedImpl(newLinesSkippedValue);
    } else {
      child.setLinesSkipped(newLinesSkippedValue);
    }
  }

  public void setLinesSkippedImpl(long newLinesSkippedValue) {
    super.setLinesSkipped(newLinesSkippedValue);
  }

  @Override
  public void setLinesUpdated(long newLinesUpdatedValue) {
    if (child == null) {
      setLinesUpdatedImpl(newLinesUpdatedValue);
    } else {
      child.setLinesUpdated(newLinesUpdatedValue);
    }
  }

  public void setLinesUpdatedImpl(long newLinesUpdatedValue) {
    super.setLinesUpdated(newLinesUpdatedValue);
  }

  @Override
  public void setLinesWritten(long newLinesWrittenValue) {
    if (child == null) {
      setLinesWrittenImpl(newLinesWrittenValue);
    } else {
      child.setLinesWritten(newLinesWrittenValue);
    }
  }

  public void setLinesWrittenImpl(long newLinesWrittenValue) {
    super.setLinesWritten(newLinesWrittenValue);
  }

  @Override
  public void setOutputDone() {
    if (child == null) {
      setOutputDoneImpl();
    } else {
      child.setOutputDone();
    }
  }

  public void setOutputDoneImpl() {
    super.setOutputDone();
  }

  @Override
  public void setOutputRowSets(List<IRowSet> outputRowSets) {
    if (child == null) {
      setOutputRowSetsImpl(outputRowSets);
    } else {
      child.setOutputRowSets(outputRowSets);
    }
  }

  public void setOutputRowSetsImpl(List<IRowSet> outputRowSets) {
    super.setOutputRowSets(outputRowSets);
  }

  @Override
  public void setVariable(String variableName, String variableValue) {
    if (child == null) {
      setVariableImpl(variableName, variableValue);
    } else {
      child.setVariable(variableName, variableValue);
    }
  }

  public void setVariableImpl(String variableName, String variableValue) {
    super.setVariable(variableName, variableValue);
  }

  @Override
  public void stopAll() {
    if (child == null) {
      stopAllImpl();
    } else {
      child.stopAll();
    }
  }

  public void stopAllImpl() {
    super.stopAll();
  }

  public void stopRunning(ITransform transformMetaInterface, ITransformData iTransformData)
      throws HopException {
    if (child == null) {
      stopRunningImpl(transformMetaInterface, data);
    } else {
      child.stopRunning(transformMetaInterface, data);
    }
  }

  public void stopRunningImpl(ITransform transformMetaInterface, ITransformData iTransformData)
      throws HopException {
    super.stopRunning();
  }

  @Override
  public String toString() {
    if (child == null) {
      return toStringImpl();
    } else {
      return child.toString();
    }
  }

  public String toStringImpl() {
    return super.toString();
  }
}
