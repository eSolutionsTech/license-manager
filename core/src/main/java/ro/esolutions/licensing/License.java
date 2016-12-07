/*
 * License.java from LicenseManager modified Monday, April 8, 2013 12:10:38 CDT (-0500).
 *
 * Copyright 2010-2013 the original author or authors.
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
import com.google.common.base.Strings;
import org.apache.commons.lang.SerializationUtils;
import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.HashCodeBuilder;
import ro.esolutions.licensing.immutable.ImmutableLinkedHashSet;

import java.io.Serializable;
import java.time.Instant;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

public final class License implements Serializable, Cloneable {
    private static final long serialVersionUID = 1L;

    private final String productKey;
    private final String holder;
    private final String issuer;
    private final String subject;
    private final Instant issueDate;
    private final Instant goodAfterDate;
    private final Instant goodBeforeDate;
    private final int seats;
    private final ImmutableLinkedHashSet<Feature> features;

    private License(final License.Builder builder) {
        this.productKey = Strings.nullToEmpty(builder.productKey);
        this.holder = Strings.nullToEmpty(builder.holder);
        this.issuer = Strings.nullToEmpty(builder.issuer);
        this.subject = Strings.nullToEmpty(builder.subject);
        this.issueDate = builder.issueDate;
        this.goodAfterDate = builder.goodAfterDate;
        this.goodBeforeDate = builder.goodBeforeDate;
        this.seats = builder.seats;
        this.features = new ImmutableLinkedHashSet<>(builder.features);
    }

    public final byte[] serialize() {
        return SerializationUtils.serialize(this);
    }

    static License deserialize(byte[] data) {
        return (License) SerializationUtils.deserialize(data);
    }

    public final String getProductKey() {
        return this.productKey;
    }

    public final String getIssuer() {
        return this.issuer;
    }

    public final String getHolder() {
        return this.holder;
    }

    public final String getSubject() {
        return this.subject;
    }

    public final Instant getIssueDate() {
        return this.issueDate;
    }

    public final Instant getGoodAfterDate() {
        return this.goodAfterDate;
    }

    public final Instant getGoodBeforeDate() {
        return this.goodBeforeDate;
    }

    public final int getSeats() {
        return this.seats;
    }

    public final ImmutableLinkedHashSet<Feature> getFeatures() {
        return this.features.clone();
    }

    public final boolean hasLicenseForFeature(final Feature feature) {
        return hasLicenseForFeature(feature.getName());
    }

    public final boolean hasLicenseForFeature(final String featureName) {
        return this.features.stream().filter(f -> Objects.equals(f.getName(), featureName))
                .findAny()
                .map(feature -> feature.getGoodBeforeDate() == null || feature.getGoodBeforeDate().isAfter(Instant.now()))
                .orElse(false);
    }

    public final boolean hasLicenseForAnyFeature(final Feature... features) {
        return Arrays.stream(features)
                .map(Feature::getName)
                .anyMatch(this::hasLicenseForFeature);
    }

    public final boolean hasLicenseForAnyFeature(final String... featureNames) {
        return Arrays.stream(featureNames)
                .anyMatch(this::hasLicenseForFeature);
    }

    public final boolean hasLicenseForAllFeatures(final Feature... features) {
        return Arrays.stream(features)
                .map(Feature::getName)
                .allMatch(this::hasLicenseForFeature);
    }

    public final boolean hasLicenseForAllFeatures(final String... featureNames) {
        return Arrays.stream(featureNames)
                .allMatch(this::hasLicenseForFeature);
    }

    @Override
    public final boolean equals(final Object object) {
        return EqualsBuilder.reflectionEquals(this, object);
    }

    @Override
    public final int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    @Override
    public final String toString() {
        return MoreObjects.toStringHelper(License.class)
                .add("productKey", productKey)
                .add("holder", holder)
                .add("issuer", issuer)
                .add("subject", subject)
                .add("issueDate", issueDate)
                .add("validFrom", goodAfterDate)
                .add("goodBeforeDate", goodBeforeDate)
                .add("seats", seats)
                .add("features", features)
                .toString();
    }

    @Override
    @SuppressWarnings("CloneDoesntCallSuperClone")
    public final License clone() {
        final License.Builder builder = new License.Builder()
                .withProductKey(this.productKey)
                .withHolder(this.holder)
                .withIssuer(this.issuer)
                .withSubject(this.subject)
                .withIssueDate(this.issueDate)
                .withGoodAfter(this.goodAfterDate)
                .withGoodBefore(this.goodBeforeDate)
                .withSeats(this.seats);

        features.forEach(builder::withFeature);

        return builder.build();
    }

    public static final class Builder {
        private String productKey;
        private String holder;
        private String issuer;
        private String subject;
        private Instant issueDate = Instant.now();
        private Instant goodAfterDate = Instant.MIN;
        private Instant goodBeforeDate = Instant.MAX;
        private int seats = Integer.MAX_VALUE;
        private Set<Feature> features = new LinkedHashSet<>();

        public Builder withProductKey(final String productKey) {
            this.productKey = productKey;
            return this;
        }

        public Builder withIssuer(final String issuer) {
            this.issuer = issuer;
            return this;
        }

        public Builder withHolder(final String holder) {
            this.holder = holder;
            return this;
        }

        public Builder withSubject(final String subject) {
            this.subject = subject;
            return this;
        }

        public Builder withIssueDate(final Instant issueDate) {
            this.issueDate = issueDate;
            return this;
        }

        public Builder withGoodAfter(final Instant goodAfterDate) {
            this.goodAfterDate = goodAfterDate;
            return this;
        }

        public Builder withGoodBefore(final Instant goodBeforeDate) {
            this.goodBeforeDate = goodBeforeDate;
            return this;
        }

        public Builder withSeats(final int seats) {
            this.seats = seats;
            return this;
        }

        public Builder withFeature(final String featureName) {
            this.features.add(Feature.of(featureName).build());
            return this;
        }

        public Builder withFeature(final Feature feature) {
            this.features.add(feature);
            return this;
        }

        public License build() {
            return new License(this);
        }
    }
}
