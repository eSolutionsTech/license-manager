/*
 * RSAKeyPairGenerator.java from LicenseManager modified Thursday, January 24, 2013 16:37:10 CST (-0600).
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

import java.io.File;
import java.io.IOException;
import java.security.InvalidParameterException;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;

import ro.esolutions.licensing.exception.AlgorithmNotSupportedException;
import ro.esolutions.licensing.exception.InappropriateKeyException;
import ro.esolutions.licensing.exception.InappropriateKeySpecificationException;
import ro.esolutions.licensing.exception.RSA2048NotSupportedException;

/**
 * The generator one should use to create public/private key pairs for use with
 * the application.
 *
 * @author Nick Williams
 * @version 1.0.0
 * @since 1.0.0
 */
public final class RSAKeyPairGenerator implements RSAKeyPairGeneratorInterface {

    /**
     * Generates a key pair with RSA 2048-bit security.
     *
     * @return a public/private key pair.
     * @throws RSA2048NotSupportedException if RSA or 2048-bit encryption are not supported.
     */
    @Override
    public KeyPair generateKeyPair() throws RSA2048NotSupportedException {
        KeyPairGenerator keyGenerator;

        try {
            keyGenerator = KeyPairGenerator.getInstance(KeyFileUtilities.keyAlgorithm);
        } catch (final NoSuchAlgorithmException e) {
            throw new RSA2048NotSupportedException("RSA keys are not supported on your system. Contact your system administrator for assistance.", e);
        }

        try {
            keyGenerator.initialize(2048);
        } catch (final InvalidParameterException e) {
            throw new RSA2048NotSupportedException("RSA is supported on your system, but 2048-bit keys are not. Contact your system administrator for assistance.", e);
        }

        return keyGenerator.generateKeyPair();
    }

    /**
     * Saves the key pair specified to output files specified, encrypting both with the specified password.
     *
     * @param keyPair               The key pair to save to the files specified
     * @param privateOutputFileName The name of the file to save the encrypted private key to
     * @param publicOutputFileName  The name of the file to save the encrypted public key to
     * @param password              The password to encrypt both keys with
     * @throws IOException                            if an error occurs while writing to the files.
     * @throws AlgorithmNotSupportedException         If the encryption algorithm is not supported
     * @throws InappropriateKeyException              If the public or private keys are invalid
     * @throws InappropriateKeySpecificationException If the public or private keys are invalid
     */
    @Override
    public void saveKeyPairToFiles(final KeyPair keyPair,final String privateOutputFileName,final String publicOutputFileName,
                                   final char[] password)
            throws IOException, AlgorithmNotSupportedException, InappropriateKeyException,
            InappropriateKeySpecificationException {
        this.saveKeyPairToFiles(keyPair, privateOutputFileName, publicOutputFileName, password, password);
    }

    /**
     * Saves the key pair specified to output files specified, encrypting each with their specified passwords.
     *
     * @param keyPair               The key pair to save to the files specified
     * @param privateOutputFileName The name of the file to save the encrypted private key to
     * @param publicOutputFileName  The name of the file to save the encrypted public key to
     * @param privatePassword       The password to encrypt the private key with
     * @param publicPassword        The password to encrypt the public key with
     * @throws IOException                            if an error occurs while writing to the files.
     * @throws AlgorithmNotSupportedException         If the encryption algorithm is not supported
     * @throws InappropriateKeyException              If the public or private keys are invalid
     * @throws InappropriateKeySpecificationException If the public or private keys are invalid
     */
    @Override
    public void saveKeyPairToFiles(final KeyPair keyPair,final String privateOutputFileName,final String publicOutputFileName,
                                   final char[] privatePassword,final char[] publicPassword)
            throws IOException, AlgorithmNotSupportedException, InappropriateKeyException,
            InappropriateKeySpecificationException {
        final PrivateKey privateKey = keyPair.getPrivate();
        final PublicKey publicKey = keyPair.getPublic();

        KeyFileUtilities.writeEncryptedPrivateKey(privateKey, new File(privateOutputFileName), privatePassword);
        KeyFileUtilities.writeEncryptedPublicKey(publicKey, new File(publicOutputFileName), publicPassword);
    }

    /**
     * Saves the public and private keys specified to the respective
     * {@link RSAKeyPairGeneratorInterface.GeneratedClassDescriptor#getJavaFileContents() javaFileContents} fields in
     * the provided {@link RSAKeyPairGeneratorInterface.GeneratedClassDescriptor}s, encrypting both with the specified
     * password.
     *
     * @param keyPair            The key pair to save
     * @param privateKeyProvider An object describing the {@link PrivateKeyDataProvider} class to generate, and into
     *                           which the generated code will be saved
     * @param publicKeyProvider  An object describing the {@link PublicKeyDataProvider} class to generate, and into
     *                           which the generated code will be saved
     * @param password           The password to encrypt the keys with
     * @throws AlgorithmNotSupportedException         If the encryption algorithm is not supported
     * @throws InappropriateKeyException              If the public or private keys are invalid
     * @throws InappropriateKeySpecificationException If the public or private keys are invalid
     */
    @Override
    public void saveKeyPairToProviders(final KeyPair keyPair,final GeneratedClassDescriptor privateKeyProvider,
                                       final GeneratedClassDescriptor publicKeyProvider,final char[] password)
            throws AlgorithmNotSupportedException, InappropriateKeyException, InappropriateKeySpecificationException {
        this.saveKeyPairToProviders(keyPair, privateKeyProvider, publicKeyProvider, password, password);
    }

    /**
     * Saves the public and private keys specified to the respective
     * {@link RSAKeyPairGeneratorInterface.GeneratedClassDescriptor#getJavaFileContents() javaFileContents} fields in
     * the provided {@link RSAKeyPairGeneratorInterface.GeneratedClassDescriptor}s, encrypting each with their
     * respective passwords.
     *
     * @param keyPair            The key pair to save
     * @param privateKeyProvider An object describing the {@link PrivateKeyDataProvider} class to generate, and into
     *                           which the generated code will be saved
     * @param publicKeyProvider  An object describing the {@link PublicKeyDataProvider} class to generate, and into
     *                           which the generated code will be saved
     * @param privatePassword    The password to encrypt the private key with
     * @param publicPassword     The password to encrypt the public key with
     * @throws AlgorithmNotSupportedException         If the encryption algorithm is not supported
     * @throws InappropriateKeyException              If the public or private keys are invalid
     * @throws InappropriateKeySpecificationException If the public or private keys are invalid
     */
    @Override
    public void saveKeyPairToProviders(final KeyPair keyPair,final GeneratedClassDescriptor privateKeyProvider,
                                       final GeneratedClassDescriptor publicKeyProvider,final char[] privatePassword,
                                       final char[] publicPassword)
            throws AlgorithmNotSupportedException, InappropriateKeyException, InappropriateKeySpecificationException {
        if (keyPair == null)
            throw new IllegalArgumentException("Parameter keyPair cannot be null.");

        if (privateKeyProvider == null)
            throw new IllegalArgumentException("Parameter privateKeyProvider cannot be null.");

        if (publicKeyProvider == null)
            throw new IllegalArgumentException("Parameter publicKeyProvider cannot be null.");

        if (passwordInValid(privatePassword))
            throw new IllegalArgumentException("Parameter privatePassword cannot be null or zero-length.");

        if (passwordInValid(publicPassword))
            throw new IllegalArgumentException("Parameter publicPassword cannot be null or zero-length.");

        final byte[] privateKey = KeyFileUtilities.writeEncryptedPrivateKey(keyPair.getPrivate(), privatePassword);
        final byte[] publicKey = KeyFileUtilities.writeEncryptedPublicKey(keyPair.getPublic(), publicPassword);

        final String privateKeyCode = this.arrayToCodeString(this.byteArrayToIntArray(privateKey), "byte");
        final String publicKeyCode = this.arrayToCodeString(this.byteArrayToIntArray(publicKey), "byte");

        privateKeyProvider.setJavaFileContents(this.generateJavaCode(
                privateKeyProvider.getPackageName(),
                privateKeyProvider.getClassName(),
                "PrivateKeyDataProvider",
                new String[]{
                        "ro.esolutions.licensing.encryption.PrivateKeyDataProvider",
                        "ro.esolutions.licensing.exception.KeyNotFoundException"
                },
                "public byte[] getEncryptedPrivateKeyData() throws KeyNotFoundException",
                privateKeyCode
        ));

        publicKeyProvider.setJavaFileContents(this.generateJavaCode(
                publicKeyProvider.getPackageName(),
                publicKeyProvider.getClassName(),
                "PublicKeyDataProvider",
                new String[]{
                        "ro.esolutions.licensing.encryption.PublicKeyDataProvider",
                        "ro.esolutions.licensing.exception.KeyNotFoundException"
                },
                "public byte[] getEncryptedPublicKeyData() throws KeyNotFoundException",
                publicKeyCode
        ));
    }

    /**
     * Saves the password specified to the
     * {@link RSAKeyPairGeneratorInterface.GeneratedClassDescriptor#getJavaFileContents() javaFileContents} field in
     * the provided {@link RSAKeyPairGeneratorInterface.GeneratedClassDescriptor}.
     *
     * @param password         The password to save to the specified Java class
     * @param passwordProvider An object describing the {@link PasswordProvider} class to generate, and into which the
     *                         generated code will be saved
     */
    @Override
    public void savePasswordToProvider(final char[] password,final GeneratedClassDescriptor passwordProvider) {
        if (passwordInValid(password))
            throw new IllegalArgumentException("Parameter password cannot be null or zero-length.");

        if (passwordProvider == null)
            throw new IllegalArgumentException("Parameter passwordProvider cannot be null.");

        final String passwordCode = this.arrayToCodeString(this.charArrayToIntArray(password), "char");

        passwordProvider.setJavaFileContents(this.generateJavaCode(
                passwordProvider.getPackageName(),
                passwordProvider.getClassName(),
                "PasswordProvider",
                new String[]{"ro.esolutions.licensing.encryption.PasswordProvider"},
                "public char[] getPassword()",
                passwordCode
        ));
    }

    private boolean passwordInValid(final char[] password) {
        return password == null || password.length == 0;
    }

    /**
     * Generates a final, compilable Java class implementing the specified interface with a single, one-statement
     * method that returns a simple value.
     *
     * @param packageName     The package name the class should be contained in, or {@code null} if no package
     * @param className       The name of the class to create
     * @param interfaceName   The interface this class should implement, or null if no interface
     * @param imports         An array of classes to import at the top of the Java code
     * @param methodSignature The signature of the sole method in the class
     * @param returnValue     The statement that the method should return (will be prepended with "return ")
     * @return the Java code for the specified class.
     */
    protected String generateJavaCode(final String packageName,final String className,final String interfaceName,
                                      final String[] imports,final String methodSignature,final String returnValue) {
        final StringBuilder stringBuilder = new StringBuilder();

        if (packageName != null && packageName.trim().length() > 0)
            stringBuilder.append("package ").append(packageName.trim()).append(";\r\n\r\n");

        if (imports != null && imports.length > 0) {
            for (String importClass : imports)
                stringBuilder.append("import ").append(importClass.trim()).append(";\r\n");
            stringBuilder.append("\r\n");
        }

        final boolean hasInterface = interfaceName != null && interfaceName.trim().length() > 0;

        stringBuilder.append("public final class ").append(className.trim());
        if (hasInterface)
            stringBuilder.append(" implements ").append(interfaceName.trim());
        stringBuilder.append("\r\n");
        stringBuilder.append("{\r\n");
        if (hasInterface)
            stringBuilder.append("\t@Override\r\n");
        stringBuilder.append("\t").append(methodSignature.trim()).append("\r\n");
        stringBuilder.append("\t{\r\n");

        stringBuilder.append("\t\treturn ").append(returnValue).append(";\r\n");

        stringBuilder.append("\t}\r\n");

        stringBuilder.append("}");

        return stringBuilder.toString();
    }

    /**
     * Takes an array of integer-representable primitives ({@code byte}, {@code char}, {@code short}, {@code int})
     * and returns a Java code array-literal instantiation of the array, with values in hexadecimal literal format.
     * It is the user's responsibility to ensure that the values contained in the array can fit within the smaller
     * precision of the array type, if applicable.
     *
     * @param values The array of values to include in the array code
     * @param type   The data type ({@code byte}, {@code char}, {@code short}, {@code int}) of the array to return
     * @return the Java code representation of this array.
     */
    protected String arrayToCodeString(final int[] values,final String type) {
        final StringBuilder stringBuilder = new StringBuilder("new ").append(type).append("[] {\r\n\t\t\t\t");
        int i = 0, j = 1;
        for (int value : values) {
            if (i++ > 0)
                stringBuilder.append(", ");
            if (j++ > 8) {
                j = 2;
                stringBuilder.append("\r\n\t\t\t\t");
            }
            stringBuilder.append("0x");
            stringBuilder.append(String.format("%08x", value).toUpperCase());
        }
        stringBuilder.append("\r\n\t\t}");
        return stringBuilder.toString();
    }

    /**
     * Converts a {@code byte} array to an {@code int} array.
     *
     * @param array The {@code byte} array to convert
     * @return the converted array as an {@code int} array.
     */
    protected int[] byteArrayToIntArray(final byte[] array) {
        final int[] a = new int[array.length];
        int i = 0;
        for (byte b : array)
            a[i++] = b;
        return a;
    }

    /**
     * Converts a {@code char} array to an {@code int} array.
     *
     * @param array The {@code char} array to convert
     * @return the converted array as an {@code int} array.
     */
    protected int[] charArrayToIntArray(final char[] array) {
        final int[] a = new int[array.length];
        int i = 0;
        for (char c : array)
            a[i++] = c;
        return a;
    }
}
