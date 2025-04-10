/*

Copyright 2010, Google Inc.
All rights reserved.

Redistribution and use in source and binary forms, with or without
modification, are permitted provided that the following conditions are
met:

    * Redistributions of source code must retain the above copyright
notice, this list of conditions and the following disclaimer.
    * Redistributions in binary form must reproduce the above
copyright notice, this list of conditions and the following disclaimer
in the documentation and/or other materials provided with the
distribution.
    * Neither the name of Google Inc. nor the names of its
contributors may be used to endorse or promote products derived from
this software without specific prior written permission.

THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS
"AS IS" AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT
LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR
A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT
OWNER OR CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL,
SPECIAL, EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT
LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE,           
DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY           
THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT
(INCLUDING NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE
OF THIS SOFTWARE, EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.

*/

package com.google.refine.operations.column;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import org.apache.commons.lang.Validate;

import com.google.refine.history.HistoryEntry;
import com.google.refine.model.AbstractOperation;
import com.google.refine.model.ColumnsDiff;
import com.google.refine.model.Project;
import com.google.refine.model.changes.ColumnReorderChange;
import com.google.refine.operations.OperationDescription;

public class ColumnReorderOperation extends AbstractOperation {

    final protected List<String> _columnNames;

    @JsonCreator
    public ColumnReorderOperation(
            @JsonProperty("columnNames") List<String> columnNames) {
        _columnNames = columnNames;
    }

    @Override
    public void validate() {
        Validate.notNull(_columnNames, "Missing column names");
    }

    @JsonProperty("columnNames")
    public List<String> getColumnNames() {
        return _columnNames;
    }

    @Override
    protected String getBriefDescription(Project project) {
        return OperationDescription.column_reorder_brief();
    }

    @Override
    public Optional<Set<String>> getColumnDependencies() {
        return Optional.of(_columnNames.stream().collect(Collectors.toSet()));
    }

    @Override
    public Optional<ColumnsDiff> getColumnsDiff() {
        return Optional.empty(); // we don't know what columns there were before, so we can't diff them
    }

    @Override
    public ColumnReorderOperation renameColumns(Map<String, String> newColumnNames) {
        return new ColumnReorderOperation(
                _columnNames.stream().map(name -> newColumnNames.getOrDefault(name, name)).collect(Collectors.toList()));
    }

    @Override
    protected HistoryEntry createHistoryEntry(Project project, long historyEntryID) throws Exception {
        return new HistoryEntry(
                historyEntryID,
                project,
                getBriefDescription(null),
                this,
                new ColumnReorderChange(_columnNames));
    }
}
