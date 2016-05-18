/*
 Licensed to Diennea S.r.l. under one
 or more contributor license agreements. See the NOTICE file
 distributed with this work for additional information
 regarding copyright ownership. Diennea S.r.l. licenses this file
 to you under the Apache License, Version 2.0 (the
 "License"); you may not use this file except in compliance
 with the License.  You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing,
 software distributed under the License is distributed on an
 "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 KIND, either express or implied.  See the License for the
 specific language governing permissions and limitations
 under the License.

 */
package herddb.sql.functions;

import herddb.model.StatementExecutionException;
import herddb.model.Tuple;
import herddb.sql.AggregatedColumnCalculator;
import java.time.Clock;
import java.time.Instant;
import java.util.function.Function;
import net.sf.jsqlparser.expression.Expression;
import net.sf.jsqlparser.expression.LongValue;
import net.sf.jsqlparser.expression.StringValue;
import net.sf.jsqlparser.schema.Column;

/**
 * SQL SUM
 *
 * @author enrico.olivelli
 */
public class SumColumnCalculator extends AbstractSingleExpressionArgumentColumnCalculator {

    public SumColumnCalculator(String fieldName, Expression expression) throws StatementExecutionException {
        super(fieldName, expression);
    }

    long result;

    @Override
    public String getFieldName() {
        return fieldName;
    }

    @Override
    public void consume(Tuple tuple) {
        Comparable value = valueExtractor.apply(tuple);
        if (value != null) {
            result += ((Number) value).longValue();
        }
    }

    @Override
    public Object getValue() {
        return result;
    }
}