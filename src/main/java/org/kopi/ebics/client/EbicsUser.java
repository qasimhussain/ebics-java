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

package org.kopi.ebics.client;

import lombok.Builder;
import lombok.Value;
import lombok.experimental.Wither;
import org.kopi.ebics.interfaces.Identifiable;
import org.kopi.ebics.interfaces.PasswordCallback;

import java.security.KeyPair;


/**
 * Things an EBICS user must be able to perform.
 *
 * @author Hachani
 */
@Value
@Builder
@Wither
public class EbicsUser implements Identifiable {

    private static final long serialVersionUID = 1L;

    private final KeyPair a005Key;
    private final KeyPair e002Key;
    private final KeyPair x002Key;

    private final String securityMedium;
    private final String userId;
    private final String name;

    private final boolean initializedINI;
    private final boolean initializedHIA;

    private final transient EbicsPartner partner;
    private final transient PasswordCallback passwordCallback;

    @Override
    public String getId() {
        return getUserId();
    }
}