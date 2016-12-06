/*
 * DefaultLicenseValidator.java from LicenseManager modified Tuesday, February 21, 2012 10:59:34 CST (-0600).
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

import java.time.Instant;
import java.time.format.DateTimeFormatter;

import ro.esolutions.licensing.exception.ExpiredLicenseException;
import ro.esolutions.licensing.exception.InvalidLicenseException;

public class DefaultLicenseValidator implements LicenseValidator {

    @Override
    public void validateLicense(final License license) throws InvalidLicenseException {
        final Instant time = Instant.now();
        if (license.getGoodAfterDate().isAfter(time))
            throw new InvalidLicenseException("The " + this.getLicenseDescription(license) +
                    " does not take effect until " + this.getFormattedDate(license.getGoodAfterDate()) + ".");
        if (license.getGoodBeforeDate().isBefore(time))
            throw new ExpiredLicenseException("The " + this.getLicenseDescription(license) +
                    " expired on " + this.getFormattedDate(license.getGoodAfterDate()) + ".");
    }

    private String getLicenseDescription(final License license) {
        return license.getSubject() + " license for " + license.getHolder();
    }

    private String getFormattedDate(final Instant time) {
        return DateTimeFormatter.ISO_INSTANT.format(time);
    }
}
