/*
 * Copyright (C) 2015 Sebastian Daschner, sebastian-daschner.com
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.sebastian_daschner.jaxrs_analyzer.model.elements;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * Represents an element on the runtime stack.
 *
 * @author Sebastian Daschner
 */
public class Element {

    /**
     * Unmodifiable placeholder for an empty element.
     */
    public static final Element EMPTY = new UnmodifiableElement(Object.class.getName());

    private final Set<Object> possibleValues;
    private final String type;

    public Element(final String type, final Object... values) {
        Objects.requireNonNull(type);

        this.type = type;
        possibleValues = new HashSet<>();

        // allow null as vararg argument
        if (values == null)
            possibleValues.add(null);
        else
            Collections.addAll(possibleValues, values);
    }

    /**
     * Merges the other element into this element.
     *
     * @param element The element to merge
     * @return This element (needed as BinaryOperator)
     */
    public Element merge(final Element element) {
        possibleValues.addAll(element.possibleValues);
        return this;
    }

    public Set<Object> getPossibleValues() {
        return possibleValues;
    }

    public String getType() {
        return type;
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        final Element element = (Element) o;

        if (!possibleValues.equals(element.possibleValues)) return false;
        return type.equals(element.type);
    }

    @Override
    public int hashCode() {
        int result = possibleValues.hashCode();
        result = 31 * result + type.hashCode();
        return result;
    }

    @Override
    public String toString() {
        return "Element{" +
                "possibleValues=" + possibleValues +
                ", type='" + type + '\'' +
                '}';
    }

    /**
     * Unmodifiable representation of a stack element.
     */
    private static class UnmodifiableElement extends Element {

        public UnmodifiableElement(final String type) {
            super(type);
        }

        @Override
        public Set<Object> getPossibleValues() {
            return Collections.unmodifiableSet(super.getPossibleValues());
        }

    }

}
