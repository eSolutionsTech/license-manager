/*
 * KeyFileUtilities.java from LicenseManager modified Tuesday, February 21, 2012 10:59:34 CST (-0600).
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

package ro.esolutions.licensing.encryption;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

import ro.esolutions.licensing.exception.AlgorithmNotSupportedException;
import ro.esolutions.licensing.exception.InappropriateKeySpecificationException;

/**
 * A class of utility methods for reading and writing private and public keys
 * to files.
 *
 * @author Nicholas Williamo
 * @version 1.0.0
 * @since 1.0.0
 */
public class KeyFileUtilities {
    public static final String KEY_ALGORITHM = "RSA";

    protected static void writeEncryptedPrivateKey(final PrivateKey privateKey,final File file, char[] passPhrase)
            throws IOException {

        FileUtils.writeByteArrayToFile(file, KeyFileUtilities.writeEncryptedPrivateKey(privateKey, passPhrase));
    }

    protected static void writeEncryptedPublicKey(final PublicKey publicKey,final File file,final char[] passPhrase)
            throws IOException {
        FileUtils.writeByteArrayToFile(file, KeyFileUtilities.writeEncryptedPublicKey(publicKey, passPhrase));
    }

    protected static PrivateKey readEncryptedPrivateKey(final File file,final char[] passPhrase) throws IOException {
        return KeyFileUtilities.readEncryptedPrivateKey(FileUtils.readFileToByteArray(file), passPhrase);
    }

    protected static PublicKey readEncryptedPublicKey(final File file,final char[] passPhrase) throws IOException {
        return KeyFileUtilities.readEncryptedPublicKey(FileUtils.readFileToByteArray(file), passPhrase);
    }

    protected static byte[] writeEncryptedPrivateKey(final PrivateKey privateKey,final char[] passPhrase) {
        final PKCS8EncodedKeySpec pkcs8EncodedKeySpec = new PKCS8EncodedKeySpec(privateKey.getEncoded());
        return Encryptor.encryptRaw(pkcs8EncodedKeySpec.getEncoded(), passPhrase);
    }

    protected static byte[] writeEncryptedPublicKey(final PublicKey publicKey,final char[] passPhrase) {
        final X509EncodedKeySpec x509EncodedKeySpec = new X509EncodedKeySpec(publicKey.getEncoded());
        return Encryptor.encryptRaw(x509EncodedKeySpec.getEncoded(), passPhrase);
    }

    public static PrivateKey readEncryptedPrivateKey(final byte[] fileContents,final char[] passPhrase) {
        final PKCS8EncodedKeySpec privateKeySpec = new PKCS8EncodedKeySpec(Encryptor.decryptRaw(fileContents, passPhrase));

        try {
            return KeyFactory.getInstance(KEY_ALGORITHM).generatePrivate(privateKeySpec);
        } catch (final NoSuchAlgorithmException e) {
            throw new AlgorithmNotSupportedException(KEY_ALGORITHM, e);
        } catch (final InvalidKeySpecException e) {
            throw new InappropriateKeySpecificationException(e);
        }
    }

    public static PublicKey readEncryptedPublicKey(final byte[] fileContents,final char[] passPhrase) {
        final X509EncodedKeySpec publicKeySpec = new X509EncodedKeySpec(Encryptor.decryptRaw(fileContents, passPhrase));

        try {
            return KeyFactory.getInstance(KEY_ALGORITHM).generatePublic(publicKeySpec);
        } catch (final NoSuchAlgorithmException e) {
            throw new AlgorithmNotSupportedException(KEY_ALGORITHM, e);
        } catch (final InvalidKeySpecException e) {
            throw new InappropriateKeySpecificationException(e);
        }
    }

    private KeyFileUtilities() {
        throw new RuntimeException("This class cannot be instantiated.");
    }
}
