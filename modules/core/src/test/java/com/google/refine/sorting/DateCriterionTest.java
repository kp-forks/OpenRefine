/*******************************************************************************
 * Copyright (C) 2018, OpenRefine contributors
 * All rights reserved.
 * 
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 * 
 * 1. Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 * 
 * 2. Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 * 
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE
 * IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR PURPOSE
 * ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT HOLDER OR CONTRIBUTORS BE
 * LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 * CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF
 * SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS
 * INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY, WHETHER IN
 * CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE OR OTHERWISE)
 * ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF ADVISED OF THE
 * POSSIBILITY OF SUCH DAMAGE.
 ******************************************************************************/

package com.google.refine.sorting;

import java.io.IOException;
import java.util.Map;

import org.testng.annotations.Test;

import com.google.refine.util.ParsingUtilities;
import com.google.refine.util.TestUtils;

public class DateCriterionTest {

    String json = "{\n" +
            "  \"errorPosition\": 2,\n" +
            "  \"valueType\": \"date\",\n" +
            "  \"column\": \"start_year\",\n" +
            "  \"blankPosition\": -1,\n" +
            "  \"reverse\": true\n" +
            "}\n";

    @Test
    public void serializeDateCriterion() throws IOException {
        TestUtils.isSerializedTo(ParsingUtilities.mapper.readValue(json, Criterion.class), json);
    }

    @Test
    public void testRenameColumn() throws Exception {
        String renamedJson = "{\n" +
                "  \"errorPosition\": 2,\n" +
                "  \"valueType\": \"date\",\n" +
                "  \"column\": \"start\",\n" +
                "  \"blankPosition\": -1,\n" +
                "  \"reverse\": true\n" +
                "}\n";
        Criterion renamed = ParsingUtilities.mapper.readValue(json, Criterion.class).renameColumns(Map.of("start_year", "start"));
        TestUtils.isSerializedTo(renamed, renamedJson);
    }
}
