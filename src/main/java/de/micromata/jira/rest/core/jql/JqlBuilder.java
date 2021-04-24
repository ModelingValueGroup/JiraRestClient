/*
 * Copyright 2013 the original author or authors.
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package de.micromata.jira.rest.core.jql;

/**
 * JQL builder
 * <p/>
 * <p>JQL condition = ( field + operator + operand ) + JqlKeyword + ...
 * <p>Example: ( PROJECT = DEMO ) + AND ...
 *
 * @author Christian Schulze
 * @author Vitali Filippow
 */
public class JqlBuilder {
    private final StringBuilder jql = new StringBuilder();

    public JqlKeyword addCondition(EField field, EOperator operator, String... operand) {
        JqlKeyword jqlKeyword = new JqlKeyword();

        if (field != null) {
            jql.append(field).append(" ");
        }
        if (operator != null) {
            jql.append(operator).append(" ");
        }
        if (operand != null) {
            boolean isMulti = operand.length > 1;
            if (isMulti) {
                jql.append("(");
            }
            String del = "";
            for (String s : operand) {
                jql.append(del);
                del = ",";
                boolean hasSpaces = s.contains(" ");
                if (hasSpaces) {
                    jql.append("\"");
                }
                jql.append(s);
                if (hasSpaces) {
                    jql.append("\"");
                }
            }
            if (isMulti) {
                jql.append(")");
            }
            jql.append(" ");
        }
        return jqlKeyword;
    }

    public void clear() {
        jql.setLength(0);
    }

    public class JqlKeyword {

        public JqlBuilder and() {
            jql.append(EKeyword.AND).append(" ");
            return JqlBuilder.this;
        }

        public JqlBuilder or() {
            jql.append(EKeyword.OR).append(" ");
            return JqlBuilder.this;
        }

        public String orderBy(SortOrder order, EField... fields) {
            if (fields != null && order != null && fields.length != 0) {
                jql.append(EKeyword.ORDER_BY).append(" ");
                jql.append(fields[0]);

                for (int i = 1; i < fields.length; i++) {
                    jql.append(", ");
                    jql.append(fields[i]);
                }

                jql.append(" ").append(order);
            }
            return build();
        }

        /**
         * Return the request String and clear the buffer.
         *
         * @return
         */
        public String build() {
            String request = jql.toString();
            clear();
            return request;
        }
    }
}
