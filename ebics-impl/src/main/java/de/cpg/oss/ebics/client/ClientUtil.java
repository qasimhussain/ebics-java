package de.cpg.oss.ebics.client;

import de.cpg.oss.ebics.api.EbicsSession;
import de.cpg.oss.ebics.api.exception.EbicsException;
import de.cpg.oss.ebics.utils.*;
import de.cpg.oss.ebics.xml.EbicsResponseElement;
import de.cpg.oss.ebics.xml.ResponseElement;
import de.cpg.oss.ebics.xml.ResponseOrderDataElement;
import org.apache.http.HttpEntity;
import org.ebics.h004.EbicsRequest;

import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;

abstract class ClientUtil {

    interface ResponseElementParser<T extends ResponseElement<?>> {
        T parse(InputStream responseDataXml) throws EbicsException;
    }

    interface ResponseOrderDataElementParser<T extends ResponseOrderDataElement<?>> {
        T parse(InputStream orderDataXml) throws EbicsException;
    }

    static EbicsResponseElement requestExchange(final EbicsSession session, final EbicsRequest request)
            throws EbicsException {
        return requestExchange(session, request, EbicsResponseElement::parse);
    }

    static <T, O extends ResponseElement<T>> O requestExchange(
            final EbicsSession session,
            final EbicsRequest request,
            final ResponseElementParser<O> responseElementParser) throws EbicsException {
        return requestExchange(session, EbicsRequest.class, request, responseElementParser,
                Optional.ofNullable(request.getHeader().getStatic().getOrderDetails())
                        .map(orderDetails -> orderDetails.getOrderType().getValue())
                        .orElse("Ebics"));
    }

    static <I, T, O extends ResponseElement<T>> O requestExchange(
            final EbicsSession session,
            final Class<I> requestClass, final I request,
            final ResponseElementParser<O> responseElementParser,
            final String baseElementName) throws EbicsException {
        final byte[] xml = IOUtil.read(XmlUtil.prettyPrint(requestClass, request));
        session.getTraceManager().trace(IOUtil.wrap(xml), baseElementName.concat("Request"), session.getUser());

        final HttpEntity httpEntity = HttpUtil.sendAndReceive(
                session.getBank(),
                IOUtil.wrap(XmlUtil.validate(xml)),
                session.getMessageProvider());
        final O response;
        try {
            response = responseElementParser.parse(httpEntity.getContent());
        } catch (final IOException e) {
            throw new EbicsException(e);
        }

        session.getTraceManager().trace(response.getResponseClass(), response.getResponse(),
                baseElementName.concat("Response"), session.getUser());
        response.report(session.getMessageProvider());
        return response;
    }

    static <T, O extends ResponseOrderDataElement<T>, I extends ResponseElement> O orderDataElement(
            final EbicsSession session,
            final I responseElement,
            final ResponseOrderDataElementParser<O> responseOrderDataElementParser,
            final String baseElementName) throws EbicsException {
        final byte[] orderDataXml = IOUtil.read(ZipUtil.uncompress(CryptoUtil.decryptAES(
                IOUtil.wrap(responseElement.getOrderData()),
                CryptoUtil.decryptRSA(responseElement.getTransactionKey(), session.getUserEncryptionKey()))));
        try {
            final O responseOrderDataElement = responseOrderDataElementParser.parse(IOUtil.wrap(orderDataXml));
            session.getTraceManager().trace(responseOrderDataElement.getResponseOrderDataClass(),
                    responseOrderDataElement.getResponseOrderData(), baseElementName.concat("ResponseOrderData"),
                    session.getUser());
            return responseOrderDataElement;
        } catch (final EbicsException e) {
            session.getTraceManager().trace(
                    IOUtil.wrap(orderDataXml),
                    baseElementName.concat("ResponseOrderData"), session.getUser());
            throw e;
        }
    }
}
