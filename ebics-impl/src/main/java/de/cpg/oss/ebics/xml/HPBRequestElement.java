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

import de.cpg.oss.ebics.api.exception.EbicsException;
import de.cpg.oss.ebics.session.EbicsSession;
import org.ebics.h004.EbicsNoPubKeyDigestsRequest;
import org.w3.xmldsig.SignatureType;

/**
 * The <code>HPBRequestElement</code> is the element to be sent when
 * a HPB request is needed to retrieve the bank public keys
 *
 * @author hachani
 */
public class HPBRequestElement {

    private final EbicsSession session;

    /**
     * Constructs a new HPB Request element.
     *
     * @param session the current ebics session.
     */
    public HPBRequestElement(final EbicsSession session) {
        this.session = session;
    }

    public String getName() {
        return "HPBRequest.xml";
    }

    public EbicsNoPubKeyDigestsRequest build() throws EbicsException {
        final NoPubKeyDigestsRequestElement noPubKeyDigestsRequest = new NoPubKeyDigestsRequestElement(session);
        final EbicsNoPubKeyDigestsRequest request = noPubKeyDigestsRequest.build();

        final SignedInfoElement signedInfo = new SignedInfoElement(
                session.getUser(),
                XmlUtils.digest(EbicsNoPubKeyDigestsRequest.class, request));
        final SignatureType signatureType = signedInfo.build();
        request.setAuthSignature(signatureType);

        final byte[] signature = XmlUtils.sign(EbicsNoPubKeyDigestsRequest.class, request, session.getUser());
        request.getAuthSignature().getSignatureValue().setValue(signature);

        return request;
    }
}