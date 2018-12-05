/*
 * Copyright 2018 OpenAPI-Generator Contributors (https://openapi-generator.tech)
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.openapitools.codegen.mysql;

import org.openapitools.codegen.AbstractOptionsTest;
import org.openapitools.codegen.CodegenConfig;
import org.openapitools.codegen.languages.PostgresqlSchemaCodegen;
import org.openapitools.codegen.options.PostgresqlSchemaOptionsProvider;

import mockit.Expectations;
import mockit.Tested;

public class PostgresqlSchemaOptionsTest extends AbstractOptionsTest {

    @Tested
    private PostgresqlSchemaCodegen clientCodegen;

    public PostgresqlSchemaOptionsTest() {
        super(new PostgresqlSchemaOptionsProvider());
    }

    @Override
    protected CodegenConfig getCodegenConfig() {
        return clientCodegen;
    }

    @SuppressWarnings("unused")
    @Override
    protected void setExpectations() {
        new Expectations(clientCodegen) {{
            clientCodegen.setDefaultDatabaseName(PostgresqlSchemaOptionsProvider.DEFAULT_DATABASE_NAME_VALUE);
            times = 1;
            clientCodegen.setJsonDataTypeEnabled(Boolean.valueOf(PostgresqlSchemaOptionsProvider.JSON_DATA_TYPE_ENABLED_VALUE));
            times = 1;
        }};
    }
}
