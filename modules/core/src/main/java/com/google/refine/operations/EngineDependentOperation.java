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

package com.google.refine.operations;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import com.google.refine.browsing.Engine;
import com.google.refine.browsing.EngineConfig;
import com.google.refine.model.AbstractOperation;
import com.google.refine.model.Project;

abstract public class EngineDependentOperation extends AbstractOperation {

    transient protected EngineConfig _engineConfig;

    protected EngineDependentOperation(EngineConfig engineConfig) {
        _engineConfig = engineConfig;
    }

    @Override
    public void validate() {
        _engineConfig.validate();
    }

    protected Engine createEngine(Project project) throws Exception {
        Engine engine = new Engine(project);
        engine.initializeFromConfig(getEngineConfig());
        return engine;
    }

    @JsonProperty("engineConfig")
    protected EngineConfig getEngineConfig() {
        return _engineConfig;
    }

    /**
     * Method to be overridden by subclasses to expose their column dependencies. They are then merged with dependencies
     * from the engine in {@link #getColumnDependencies()}.
     */
    @JsonIgnore
    protected Optional<Set<String>> getColumnDependenciesWithoutEngine() {
        return Optional.empty();
    }

    @Override
    public final Optional<Set<String>> getColumnDependencies() {
        Optional<Set<String>> engineDependencies = _engineConfig.getColumnDependencies();
        if (engineDependencies.isEmpty()) {
            return Optional.empty();
        }
        Optional<Set<String>> ownDependencies = getColumnDependenciesWithoutEngine();
        if (ownDependencies.isEmpty()) {
            return Optional.empty();
        }
        Set<String> merged = new HashSet<>(ownDependencies.get());
        merged.addAll(engineDependencies.get());
        return Optional.of(merged);
    }
}
