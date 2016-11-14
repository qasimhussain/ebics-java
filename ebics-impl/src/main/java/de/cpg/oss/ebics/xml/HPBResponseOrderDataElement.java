/*
 * Copyright (c) 1990-2012 kopiLeft Development SARL, Bizerte, Tunisia
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License version 2.1 as published by the Free Software Foundation.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA 02111-1307 USA
 *
 * $Id$
 */

package de.cpg.oss.ebics.xml;

import de.cpg.oss.ebics.api.AuthenticationVersion;
import de.cpg.oss.ebics.api.EbicsAuthenticationKey;
import de.cpg.oss.ebics.api.EbicsEncryptionKey;
import de.cpg.oss.ebics.api.EncryptionVersion;
import de.cpg.oss.ebics.api.exception.EbicsException;
import de.cpg.oss.ebics.io.ContentFactory;
import de.cpg.oss.ebics.utils.KeyUtil;
import org.ebics.h004.HPBResponseOrderDataType;
import org.w3.xmldsig.RSAKeyValue;

import java.security.interfaces.RSAPublicKey;

/**
 * The <code>HPBResponseOrderDataElement</code> contains the public bank
 * keys in encrypted mode. The user should decrypt with his encryption
 * key to have the bank public keys.
 *
 * @author hachani
 */
public class HPBResponseOrderDataElement {

    private final ContentFactory contentFactory;

    private HPBResponseOrderDataType response;

    /**
     * Creates a new <code>HPBResponseOrderDataElement</code> from a given
     * content factory.
     *
     * @param factory the content factory.
     */
    public HPBResponseOrderDataElement(final ContentFactory factory) {
        this.contentFactory = factory;
    }

    public EbicsAuthenticationKey getBankAuthenticationKey() throws EbicsException {
        final RSAPublicKey publicKey = getBankAuthenticationPublicKey();
        return EbicsAuthenticationKey.builder()
                .publicKey(publicKey)
                .digest(KeyUtil.getKeyDigest(publicKey))
                .creationTime(response.getAuthenticationPubKeyInfo().getPubKeyValue().getTimeStamp())
                .version(AuthenticationVersion.valueOf(response.getAuthenticationPubKeyInfo().getAuthenticationVersion()))
                .build();
    }

    public EbicsEncryptionKey getBankEncryptionKey() throws EbicsException {
        final RSAPublicKey publicKey = getBankEncryptionPublicKey();
        return EbicsEncryptionKey.builder()
                .publicKey(publicKey)
                .digest(KeyUtil.getKeyDigest(publicKey))
                .creationTime(response.getEncryptionPubKeyInfo().getPubKeyValue().getTimeStamp())
                .version(EncryptionVersion.valueOf(response.getEncryptionPubKeyInfo().getEncryptionVersion()))
                .build();
    }

    public HPBResponseOrderDataType build() throws EbicsException {
        response = XmlUtils.parse(HPBResponseOrderDataType.class, contentFactory.getContent());
        return response;
    }

    private RSAPublicKey getBankAuthenticationPublicKey() {
        final RSAKeyValue rsaKey = response.getAuthenticationPubKeyInfo().getPubKeyValue().getRSAKeyValue();
        return KeyUtil.getPublicKey(rsaKey.getModulus(), rsaKey.getExponent());
    }

    private RSAPublicKey getBankEncryptionPublicKey() {
        final RSAKeyValue rsaKey = response.getEncryptionPubKeyInfo().getPubKeyValue().getRSAKeyValue();
        return KeyUtil.getPublicKey(rsaKey.getModulus(), rsaKey.getExponent());
    }
}
