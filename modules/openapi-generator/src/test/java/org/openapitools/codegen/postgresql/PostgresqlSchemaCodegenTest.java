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

import org.testng.Assert;
import org.testng.annotations.Test;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.ArrayList;
import java.util.Arrays;

import org.openapitools.codegen.languages.PostgresqlSchemaCodegen;

public class PostgresqlSchemaCodegenTest {

    @Test
    public void testGetPostgresqlMatchedIntegerDataType() {
        final PostgresqlSchemaCodegen codegen = new PostgresqlSchemaCodegen();
        Assert.assertSame(codegen.getPostgresqlMatchedIntegerDataType(null, null, null), "INT");

        Assert.assertSame(codegen.getPostgresqlMatchedIntegerDataType(-128L, 127L, false), "SMALLINT");
        Assert.assertSame(codegen.getPostgresqlMatchedIntegerDataType(0L, 255L, true), "SMALLINT");

        Assert.assertSame(codegen.getPostgresqlMatchedIntegerDataType(500L, 100L, null), "SMALLINT");
        Assert.assertSame(codegen.getPostgresqlMatchedIntegerDataType(500L, 100L, true), "SMALLINT");
        Assert.assertSame(codegen.getPostgresqlMatchedIntegerDataType(500L, 100L, false), "SMALLINT");
        Assert.assertSame(codegen.getPostgresqlMatchedIntegerDataType(-32768L, 32767L, false), "SMALLINT");
        Assert.assertSame(codegen.getPostgresqlMatchedIntegerDataType(0L, 65535L, true), "INT");

        Assert.assertSame(codegen.getPostgresqlMatchedIntegerDataType(-8388608L, 8388607L, false), "INT");
        Assert.assertSame(codegen.getPostgresqlMatchedIntegerDataType(0L, 16777215L, true), "INT");

        Assert.assertSame(codegen.getPostgresqlMatchedIntegerDataType(-2147483648L, 2147483647L, false), "INT");
        Assert.assertSame(codegen.getPostgresqlMatchedIntegerDataType(Long.parseLong(String.valueOf(Integer.MIN_VALUE)), Long.parseLong(String.valueOf(Integer.MAX_VALUE)), false), "INT");

        Assert.assertSame(codegen.getPostgresqlMatchedIntegerDataType(0L, 4294967295L, true), "BIGINT");
        Assert.assertSame(codegen.getPostgresqlMatchedIntegerDataType(-2147483649L, 2147483648L, false), "BIGINT");
        Assert.assertSame(codegen.getPostgresqlMatchedIntegerDataType(0L, 4294967296L, true), "BIGINT");
    }

    @Test
    public void testGetPostgresqlMatchedStringDataType() {
        final PostgresqlSchemaCodegen codegen = new PostgresqlSchemaCodegen();
        Assert.assertSame(codegen.getPostgresqlMatchedStringDataType(6, 6), "CHAR");
        Assert.assertSame(codegen.getPostgresqlMatchedStringDataType(0, 0), "CHAR");
        Assert.assertSame(codegen.getPostgresqlMatchedStringDataType(255, 255), "CHAR");

        Assert.assertSame(codegen.getPostgresqlMatchedStringDataType(null, 100), "VARCHAR");
        Assert.assertSame(codegen.getPostgresqlMatchedStringDataType(null, 255), "VARCHAR");
        Assert.assertSame(codegen.getPostgresqlMatchedStringDataType(50, 255), "VARCHAR");
        Assert.assertSame(codegen.getPostgresqlMatchedStringDataType(100, 20), "VARCHAR");

        Assert.assertSame(codegen.getPostgresqlMatchedStringDataType(null, null), "TEXT");
        Assert.assertSame(codegen.getPostgresqlMatchedStringDataType(100, null), "TEXT");
        Assert.assertSame(codegen.getPostgresqlMatchedStringDataType(255, null), "TEXT");
        Assert.assertSame(codegen.getPostgresqlMatchedStringDataType(null, 256), "TEXT");

        Assert.assertSame(codegen.getPostgresqlMatchedStringDataType(16777215, null), "TEXT");
        Assert.assertSame(codegen.getPostgresqlMatchedStringDataType(16777215, 100), "TEXT");
        Assert.assertSame(codegen.getPostgresqlMatchedStringDataType(null, 16777215), "TEXT");
        Assert.assertSame(codegen.getPostgresqlMatchedStringDataType(100, 16777215), "TEXT");

        Assert.assertSame(codegen.getPostgresqlMatchedStringDataType(16777216, null), "TEXT");
        Assert.assertSame(codegen.getPostgresqlMatchedStringDataType(null, 16777216), "TEXT");
        Assert.assertSame(codegen.getPostgresqlMatchedStringDataType(16777216, 16777216), "TEXT");
        Assert.assertSame(codegen.getPostgresqlMatchedStringDataType(100, 16777216), "TEXT");
        Assert.assertSame(codegen.getPostgresqlMatchedStringDataType(100, Integer.MAX_VALUE), "TEXT");
    }

    @Test
    public void testToCodegenPostgresqlDataTypeArgument() {
        final PostgresqlSchemaCodegen codegen = new PostgresqlSchemaCodegen();
        String strArgument = "HelloWorld";
        HashMap<String, Object> strProp = codegen.toCodegenPostgresqlDataTypeArgument(strArgument, true);
        Assert.assertTrue((Boolean) strProp.get("isString"));
        Assert.assertTrue((Boolean) strProp.get("hasMore"));
        Assert.assertFalse((Boolean) strProp.get("isFloat"));
        Assert.assertFalse((Boolean) strProp.get("isInteger"));
        Assert.assertFalse((Boolean) strProp.get("isNumeric"));
        Assert.assertSame((String) strProp.get("argumentValue"), strArgument);

        Integer intArgument = 10;
        HashMap<String, Object> intProp = codegen.toCodegenPostgresqlDataTypeArgument(intArgument, true);
        Assert.assertFalse((Boolean) intProp.get("isString"));
        Assert.assertTrue((Boolean) intProp.get("hasMore"));
        Assert.assertFalse((Boolean) intProp.get("isFloat"));
        Assert.assertTrue((Boolean) intProp.get("isInteger"));
        Assert.assertTrue((Boolean) intProp.get("isNumeric"));
        Assert.assertSame((Integer) intProp.get("argumentValue"), intArgument);

        Double floatArgument = 3.14;
        HashMap<String, Object> floatProp = codegen.toCodegenPostgresqlDataTypeArgument(floatArgument, false);
        Assert.assertFalse((Boolean) floatProp.get("isString"));
        Assert.assertFalse((Boolean) floatProp.get("hasMore"));
        Assert.assertTrue((Boolean) floatProp.get("isFloat"));
        Assert.assertFalse((Boolean) floatProp.get("isInteger"));
        Assert.assertTrue((Boolean) floatProp.get("isNumeric"));
        Assert.assertSame((Double) floatProp.get("argumentValue"), floatArgument);
    }

    @Test
    public void testToCodegenPostgresqlDataTypeDefault() {
        final PostgresqlSchemaCodegen codegen = new PostgresqlSchemaCodegen();
        HashMap<String, Object> defaultMap = null;
        ArrayList<String> intFixture = new ArrayList<String>(Arrays.asList(
            "SmallInt", "INT", "bigint"
        ));
        for(String intType : intFixture) {
            defaultMap = codegen.toCodegenPostgresqlDataTypeDefault("150", intType);
            Assert.assertTrue((Boolean) defaultMap.get("isNumeric"));
            Assert.assertFalse((Boolean) defaultMap.get("isString"));
            Assert.assertFalse((Boolean) defaultMap.get("isKeyword"));
            Assert.assertSame(defaultMap.get("defaultValue"), "150");
        }
        defaultMap = codegen.toCodegenPostgresqlDataTypeDefault("SERIAL DEFAULT VALUE", "SMALLINT");
        Assert.assertFalse((Boolean) defaultMap.get("isNumeric"));
        Assert.assertFalse((Boolean) defaultMap.get("isString"));
        Assert.assertTrue((Boolean) defaultMap.get("isKeyword"));
        Assert.assertSame(defaultMap.get("defaultValue"), "SERIAL DEFAULT VALUE");

        ArrayList<String> dateFixture = new ArrayList<String>(Arrays.asList(
            "Timestamp"
        ));
        for(String dateType : dateFixture) {
            defaultMap = codegen.toCodegenPostgresqlDataTypeDefault("2018-08-12", dateType);
            Assert.assertFalse((Boolean) defaultMap.get("isNumeric"));
            Assert.assertTrue((Boolean) defaultMap.get("isString"));
            Assert.assertFalse((Boolean) defaultMap.get("isKeyword"));
            Assert.assertSame(defaultMap.get("defaultValue"), "2018-08-12");
        }
        defaultMap = codegen.toCodegenPostgresqlDataTypeDefault("CURRENT_TIMESTAMP", "Timestamp");
        Assert.assertFalse((Boolean) defaultMap.get("isNumeric"));
        Assert.assertFalse((Boolean) defaultMap.get("isString"));
        Assert.assertTrue((Boolean) defaultMap.get("isKeyword"));
        Assert.assertSame(defaultMap.get("defaultValue"), "CURRENT_TIMESTAMP");

        ArrayList<String> restFixture = new ArrayList<String>(Arrays.asList(
            "VARCHAR", "CHAR", "ENUM", "UNKNOWN"
        ));
        for(String restType : restFixture) {
            defaultMap = codegen.toCodegenPostgresqlDataTypeDefault("sometext", restType);
            Assert.assertFalse((Boolean) defaultMap.get("isNumeric"));
            Assert.assertTrue((Boolean) defaultMap.get("isString"));
            Assert.assertFalse((Boolean) defaultMap.get("isKeyword"));
            Assert.assertSame(defaultMap.get("defaultValue"), "sometext");
        }
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testToCodegenPostgresqlDataTypeDefaultWithExceptionalColumnType() {
        final PostgresqlSchemaCodegen codegen = new PostgresqlSchemaCodegen();
        HashMap<String, Object> defaultMap = null;
        ArrayList<String> specialFixture = new ArrayList<String>(Arrays.asList(
            "Blob", "TEXT", "GEOMETRY", "JSON"
        ));
        for(String specialType : specialFixture) {
            defaultMap = codegen.toCodegenPostgresqlDataTypeDefault("2018-08-12", specialType);
            Assert.assertNull(defaultMap);
        }
    }

    @Test
    public void testIsPostgresqlDataType() {
        final PostgresqlSchemaCodegen codegen = new PostgresqlSchemaCodegen();
        ArrayList<String> trueFixture = new ArrayList<String>(Arrays.asList(
            "INTEGER", "integer", "Integer", "VARCHAR", "varchar", "VarChar", "POINT", "Point", "point", "JSON", "json", "Json"
        ));
        ArrayList<String> falseFixture = new ArrayList<String>(Arrays.asList(
            "unknown", "HashMap", "HASHMAP", "hashmap"
        ));
        for(String trueValue : trueFixture) {
            Assert.assertTrue(codegen.isPostgresqlDataType(trueValue), "'" + trueValue + "' isn't PostgreSQL data type");
        }
        for(String falseValue : falseFixture) {
            Assert.assertFalse(codegen.isPostgresqlDataType(falseValue), "'" + falseValue + "' is PostgreSQL data type");
        }
    }

    @Test
    public void testToPostgresqlIdentifier() {
        final PostgresqlSchemaCodegen codegen = new PostgresqlSchemaCodegen();
        Assert.assertEquals(codegen.toPostgresqlIdentifier("table_name", "tbl_", ""), "table_name");
        Assert.assertEquals(codegen.toPostgresqlIdentifier("table_name   ", "tbl_", ""), "table_name");
        Assert.assertEquals(codegen.toPostgresqlIdentifier("12345678", "tbl_", ""), "tbl_12345678");
    }

    @Test(expectedExceptions = RuntimeException.class)
    public void testToPostgresqlIdentifierWithEmptyString() {
        final PostgresqlSchemaCodegen codegen = new PostgresqlSchemaCodegen();
        codegen.toPostgresqlIdentifier("   ", "tbl_", "");
    }

    @Test
    public void testEscapePostgresqlUnquotedIdentifier() {
        final PostgresqlSchemaCodegen codegen = new PostgresqlSchemaCodegen();
        Assert.assertEquals(codegen.escapePostgresqlUnquotedIdentifier("table1Z$_"), "table1Z$_");
        Assert.assertEquals(codegen.escapePostgresqlUnquotedIdentifier("table1Z$_!#%~&?()*+-./"), "table1Z$_");
        Assert.assertEquals(codegen.escapePostgresqlUnquotedIdentifier("table1Z$_русскийтекст"), "table1Z$_русскийтекст");
        Assert.assertEquals(codegen.escapePostgresqlQuotedIdentifier("table𐀀"), "table");
        Assert.assertEquals(codegen.escapePostgresqlQuotedIdentifier("table_name!'()�"), "table_name!'()�");
        Assert.assertEquals(codegen.escapePostgresqlQuotedIdentifier("table_name𐌅𐌌 "), "table_name");
    }

    @Test
    public void testEscapePostgresqlQuotedIdentifier() {
        final PostgresqlSchemaCodegen codegen = new PostgresqlSchemaCodegen();
        Assert.assertEquals(codegen.escapePostgresqlQuotedIdentifier("table"), "table");
        Assert.assertEquals(codegen.escapePostgresqlQuotedIdentifier("table𐀀"), "table");
        Assert.assertEquals(codegen.escapePostgresqlQuotedIdentifier("table_name!'()�"), "table_name!'()�");
        Assert.assertEquals(codegen.escapePostgresqlQuotedIdentifier("table_name𐌅𐌌 "), "table_name");
    }

    @Test
    public void testIsReservedWord() {
        final PostgresqlSchemaCodegen codegen = new PostgresqlSchemaCodegen();
        Set<String> reservedWords = codegen.reservedWords();
        ArrayList<String> trueFixture = new ArrayList<String>(Arrays.asList(
            "asc", "between", "boolean", /*"blob", "change",*/ "column", /*"day_hour",*/ "distinct", /*"enclosed",*/ "end", "except", /*"explain",*/ "float", "for", /*"function",*/ "grant",
            "grouping", /*"high_priority",*/ "group", /*"hour_minute",*/ /*"insensitive", "interval", "json_table", "keys", "kill", "leave", "left", "mediumblob",*/
            /*"modifies",*/ "not", "null", "numeric", /*"optimize",*/ /*"outer", "precision",*/ "primary", "references", /*"replace",*/ "select",/* "sql",*/ "then",
            "timestamp", /*"tinytext",*/ "unique",/* "unlock",*/ "varchar", /*"virtual",*/ "when", "where" /*"xor",*//* "year_month", "zerofill"*/, "none", "offset"
        ));
        ArrayList<String> falseFixture = new ArrayList<String>(Arrays.asList(
            "after", "charset", "cpu", "current", "delay_key_write", "format", "global", "host", "install", "json", "key_block_size", "local", "max_size",  "partial", "quarter",
            "relay", "second", "status",  "until", "variables", "without", "xml", "year"
        ));
        for(String trueValue : trueFixture) {
            Assert.assertTrue(reservedWords.contains(trueValue), "'" + trueValue + "' isn't PostgreSQL reserved word");
        }
        for(String falseValue : falseFixture) {
            Assert.assertFalse(reservedWords.contains(falseValue), "'" + falseValue + "' is PostgreSQL reserved word");
        }
    }

    @Test
    public void testSetDefaultDatabaseName() {
        final PostgresqlSchemaCodegen codegen = new PostgresqlSchemaCodegen();
        codegen.setDefaultDatabaseName("valid_db_name");
        Assert.assertSame(codegen.getDefaultDatabaseName(), "valid_db_name");
        codegen.setDefaultDatabaseName("12345");
        Assert.assertNotSame(codegen.getDefaultDatabaseName(), "12345");
    }

    @Test
    public void testGetDefaultDatabaseName() {
        final PostgresqlSchemaCodegen codegen = new PostgresqlSchemaCodegen();
        Assert.assertSame(codegen.getDefaultDatabaseName(), "");
    }

    @Test
    public void testSetJsonDataTypeEnabled() {
        final PostgresqlSchemaCodegen codegen = new PostgresqlSchemaCodegen();
        codegen.setJsonDataTypeEnabled(true);
        Assert.assertTrue(codegen.getJsonDataTypeEnabled());
        codegen.setJsonDataTypeEnabled(false);
        Assert.assertFalse(codegen.getJsonDataTypeEnabled());
    }

    @Test
    public void testGetJsonDataTypeEnabled() {
        final PostgresqlSchemaCodegen codegen = new PostgresqlSchemaCodegen();
        Assert.assertTrue(codegen.getJsonDataTypeEnabled());
        codegen.setJsonDataTypeEnabled(false);
        Assert.assertFalse(codegen.getJsonDataTypeEnabled());
    }

}
