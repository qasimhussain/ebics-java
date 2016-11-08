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

import org.ebics.h004.EbicsRequest;
import org.ebics.h004.EbicsResponse;
import org.kopi.ebics.exception.EbicsException;
import org.kopi.ebics.interfaces.ContentFactory;
import org.kopi.ebics.io.ByteArrayContentFactory;
import org.kopi.ebics.io.Joiner;
import org.kopi.ebics.messages.Messages;
import org.kopi.ebics.session.EbicsSession;
import org.kopi.ebics.session.OrderType;
import org.kopi.ebics.utils.Constants;
import org.kopi.ebics.utils.Utils;
import org.kopi.ebics.xml.*;

import java.io.IOException;
import java.io.OutputStream;
import java.time.LocalDate;

import static org.kopi.ebics.xml.DefaultEbicsRootElement.generateName;


/**
 * Handling of file transfers.
 * Files can be transferred to and fetched from the bank.
 * Every transfer may be performed in a recoverable way.
 * For convenience and performance reasons there are also
 * methods that do the whole transfer in one method call.
 * To use the recoverable transfer mode, you may set a working
 * directory for temporarily created files.
 * <p>
 * <p> EBICS specification 2.4.2 - 6.2 Encryption at application level
 * <p>
 * <p>In the event of an upload transaction, a random symmetrical key is generated in the
 * customer system that is used exclusively within the framework of this transaction both for
 * encryption of the ES’s and for encryption of the order data. This key is encrypted
 * asymmetrically with the financial institution’s public encryption key and is transmitted by the
 * customer system to the bank system during the initialization phase of the transaction.
 * <p>
 * <p>Analogously, in the case of a download transaction a random symmetrical key is generated
 * in the bank system that is used for encryption of the order data that is to be downloaded and
 * for encryption of the bank-technical signature that has been provided by the financial
 * institution. This key is asymmetrically encrypted and is transmitted by the bank system to the
 * customer system during the initialization phase of the transaction. The asymmetrical
 * encryption takes place with the technical subscriber’s public encryption key if the
 * transaction’s EBICS messages are sent by a technical subscriber. Otherwise the
 * asymmetrical encryption takes place with the public encryption key of the non-technical
 * subscriber, i.e. the submitter of the order.
 *
 * @author Hachani
 */
class FileTransfer {

    private final EbicsSession session;

    /**
     * Constructs a new FileTransfer session
     *
     * @param session the user session
     */
    FileTransfer(final EbicsSession session) {
        this.session = session;
    }

    /**
     * Initiates a file transfer to the bank.
     *
     * @param content   The bytes you want to send.
     * @param orderType As which order type
     */
    void sendFile(final byte[] content, final OrderType orderType) throws EbicsException {
        final HttpRequestSender sender = new HttpRequestSender(session);
        final UInitializationRequestElement initializer = new UInitializationRequestElement(session, orderType, content);
        final EbicsRequest request = initializer.build();
        final byte[] xml = XmlUtils.prettyPrint(EbicsRequest.class, request);
        session.getConfiguration().getTraceManager().trace(xml, initializer.getName());
        XmlUtils.validate(xml);
        final int httpCode = sender.send(new ByteArrayContentFactory(xml));
        Utils.checkHttpCode(httpCode);
        final EbicsResponseElement response = new EbicsResponseElement(sender.getResponseBody(),
                orderType,
                generateName(orderType));
        final EbicsResponse ebicsResponse = response.build();
        session.getConfiguration().getTraceManager().trace(EbicsResponse.class, ebicsResponse);
        response.report();
        final TransferState state = new TransferState(initializer.getSegmentNumber(), response.getTransactionId());

        while (state.hasNext()) {
            final int segmentNumber;

            segmentNumber = state.next();
            sendFile(initializer.getContent(segmentNumber),
                    segmentNumber,
                    state.isLastSegment(),
                    state.getTransactionId(),
                    orderType);
        }
    }

    /**
     * Sends a segment to the ebics bank server.
     *
     * @param factory       the content factory that contain the segment data.
     * @param segmentNumber the segment number
     * @param lastSegment   is it the last segment?
     * @param transactionId the transaction Id
     * @param orderType     the order type
     * @throws EbicsException
     */
    private void sendFile(final ContentFactory factory,
                          final int segmentNumber,
                          final boolean lastSegment,
                          final byte[] transactionId,
                          final OrderType orderType) throws EbicsException {
        final TransferResponseElement response;

        session.getConfiguration().getLogger().info(Messages.getString("upload.segment",
                Constants.APPLICATION_BUNDLE_NAME,
                segmentNumber));
        final UTransferRequestElement uploader = new UTransferRequestElement(session,
                orderType,
                segmentNumber,
                lastSegment,
                transactionId,
                factory);
        final HttpRequestSender sender = new HttpRequestSender(session);
        final EbicsRequest ebicsRequest = uploader.build();
        final byte[] xml = XmlUtils.prettyPrint(EbicsRequest.class, ebicsRequest);
        session.getConfiguration().getTraceManager().trace(xml, uploader.getName());
        XmlUtils.validate(xml);
        final int httpCode = sender.send(new ByteArrayContentFactory(xml));
        Utils.checkHttpCode(httpCode);
        response = new TransferResponseElement(sender.getResponseBody(), generateName(orderType));
        final EbicsResponse ebicsResponse = response.build();
        session.getConfiguration().getTraceManager().trace(EbicsResponse.class, ebicsResponse);
        response.report();
    }

    /**
     * Fetches a file of the given order type from the bank.
     * You may give an optional start and end date.
     * This type of transfer will run until everything is processed.
     * No transaction recovery is possible.
     *
     * @param orderType type of file to fetch
     * @param start     optional begin of fetch term
     * @param end       optional end of fetch term
     * @param dest      where to put the data
     * @throws IOException    communication error
     * @throws EbicsException server generated error
     */
    void fetchFile(final OrderType orderType,
                   final LocalDate start,
                   final LocalDate end,
                   final OutputStream dest)
            throws IOException, EbicsException {
        final HttpRequestSender sender = new HttpRequestSender(session);
        final DInitializationRequestElement initializer = new DInitializationRequestElement(session,
                orderType,
                start,
                end);
        final EbicsRequest request = initializer.build();
        final byte[] xml = XmlUtils.prettyPrint(EbicsRequest.class, request);
        session.getConfiguration().getTraceManager().trace(xml, initializer.getName());
        XmlUtils.validate(xml);
        int httpCode = sender.send(new ByteArrayContentFactory(xml));
        Utils.checkHttpCode(httpCode);
        final DInitializationResponseElement response = new DInitializationResponseElement(sender.getResponseBody(), orderType, generateName(orderType));
        final EbicsResponse ebicsResponse = response.build();
        session.getConfiguration().getTraceManager().trace(EbicsResponse.class, ebicsResponse);
        response.report();
        final TransferState state = new TransferState(response.getSegmentsNumber(), response.getTransactionId());
        state.setSegmentNumber(response.getSegmentNumber());
        final Joiner joiner = new Joiner(session.getUser());
        joiner.append(response.getOrderData());
        while (state.hasNext()) {
            final int segmentNumber;

            segmentNumber = state.next();
            fetchFile(orderType,
                    segmentNumber,
                    state.isLastSegment(),
                    state.getTransactionId(),
                    joiner);
        }

        joiner.writeTo(dest, response.getTransactionKey());
        final ReceiptRequestElement receipt = new ReceiptRequestElement(session, state.getTransactionId(), generateName(orderType));
        final EbicsRequest ebicsRequest = receipt.build();
        final byte[] receiptXml = XmlUtils.prettyPrint(EbicsRequest.class, ebicsRequest);
        XmlUtils.validate(receiptXml);
        session.getConfiguration().getTraceManager().trace(receiptXml, receipt.getName());
        httpCode = sender.send(new ByteArrayContentFactory(receiptXml));
        Utils.checkHttpCode(httpCode);
        final ReceiptResponseElement receiptResponse = new ReceiptResponseElement(sender.getResponseBody(), generateName(orderType));
        final EbicsResponse ebicsReceiptResponse = receiptResponse.build();
        session.getConfiguration().getTraceManager().trace(EbicsResponse.class, ebicsReceiptResponse);
        receiptResponse.report();
    }

    /**
     * Fetches a given portion of a file.
     *
     * @param orderType     the order type
     * @param segmentNumber the segment number
     * @param lastSegment   is it the last segment?
     * @param transactionId the transaction ID
     * @param joiner        the portions joiner
     * @throws IOException    communication error
     * @throws EbicsException server generated error
     */
    private void fetchFile(final OrderType orderType,
                           final int segmentNumber,
                           final boolean lastSegment,
                           final byte[] transactionId,
                           final Joiner joiner)
            throws IOException, EbicsException {
        final DTransferResponseElement response;
        final int httpCode;

        final HttpRequestSender sender = new HttpRequestSender(session);
        final DTransferRequestElement downloader = new DTransferRequestElement(session,
                orderType,
                segmentNumber,
                lastSegment,
                transactionId);
        final EbicsRequest ebicsRequest = downloader.build();
        final byte[] xml = XmlUtils.prettyPrint(EbicsRequest.class, ebicsRequest);
        session.getConfiguration().getTraceManager().trace(xml, downloader.getName());
        XmlUtils.validate(xml);
        httpCode = sender.send(new ByteArrayContentFactory(xml));
        Utils.checkHttpCode(httpCode);
        response = new DTransferResponseElement(sender.getResponseBody(),
                orderType,
                generateName(orderType));
        final EbicsResponse ebicsResponse = response.build();
        session.getConfiguration().getTraceManager().trace(EbicsResponse.class, ebicsResponse);
        response.report();
        joiner.append(response.getOrderData());
    }
}
