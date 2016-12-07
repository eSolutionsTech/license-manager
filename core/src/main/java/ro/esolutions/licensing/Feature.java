/*
 * Feature.java from LicenseManager modified Wednesday, November 30, 2016 14:14:50 EET (+0200).
 *
 * Copyright 2010-2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ro.esolutions.licensing;

import com.google.common.base.MoreObjects;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;

import java.io.Serializable;
import java.time.Instant;

public class Feature implements Cloneable, Serializable {
    private static final long serialVersionUID = 1L;

    private final String name;
    private final int seats;
    private final Instant goodBeforeDate;

    private Feature(final Builder builder) {
        this.name = builder.name;
        this.seats = builder.seats;
        this.goodBeforeDate = builder.goodBeforeDate;
    }

    public static Feature.Builder of(final String name) {
        return new Feature.Builder(name);
    }

    public int getSeats() {
        return seats;
    }

    public final String getName() {
        return name;
    }

    public final Instant getGoodBeforeDate() {
        return goodBeforeDate;
    }

    /**
     * Deserializes a string representation of a feature into a feature.
     *
     * @param input The string representation of a feature, generated with {@link #toString()}.
     * @return the unserialized feature.
     */
    static Feature fromString(final String input) {
        if (input == null)
            throw new IllegalArgumentException("The input argument did not contain exactly two parts.");

        final String[] parts = input.split("" + (char) 0x1F);
        if (parts.length != 3)
            throw new IllegalArgumentException("The input argument did not contain exactly two parts.");

        return Feature.of(parts[0])
                .seats(Integer.parseInt(parts[1]))
                .goodBeforeDate(Instant.parse(parts[2]))
                .build();
    }

    /**
     * Indicates whether these features are the same feature. Important note: Two features <b>can</b> be the same
     * feature (equal) and have different expiration dates.
     *
     * @param object The feature to check for equality against
     * @return {@code true} if the features are the same, {@code false} otherwise.
     */
    @Override
    public final boolean equals(final Object object) {
        return EqualsBuilder.reflectionEquals(this, object);
    }

    @Override
    public final int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public final String toString() {
        return MoreObjects.toStringHelper(this)
                .add("name", name)
                .add("seats", seats)
                .add("goodBeforeDate", goodBeforeDate)
                .toString();
    }

    @Override
    @SuppressWarnings("CloneDoesntCallSuperClone")
    public Feature clone() {
        return Feature.of(this.name)
                .seats(this.seats)
                .goodBeforeDate(this.goodBeforeDate)
                .build();
    }

    public static class Builder {
        private String name;
        private int seats;
        private Instant goodBeforeDate;

        private Builder(final String name) {
            this.name = name;
        }

        public Builder seats(final int seats) {
            this.seats = seats;
            return this;
        }

        public Builder goodBeforeDate(final Instant goodBeforeDate) {
            this.goodBeforeDate = goodBeforeDate;
            return this;
        }

        public Feature build() {
            return new Feature(this);
        }
    }
}
