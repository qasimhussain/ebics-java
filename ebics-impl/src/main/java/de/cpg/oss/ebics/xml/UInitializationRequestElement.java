package de.cpg.oss.ebics.xml;

import de.cpg.oss.ebics.api.EbicsSession;
import de.cpg.oss.ebics.api.OrderType;
import lombok.Builder;
import lombok.NonNull;
import org.ebics.h004.*;

import static de.cpg.oss.ebics.xml.EbicsXmlFactory.*;

@Builder
public class UInitializationRequestElement implements EbicsRequestElement {

    @NonNull
    private final OrderType orderType;
    @NonNull
    private final byte[] digest;
    @NonNull
    private final byte[] nonce;
    private final int numSegments;
    private final String fileFormat;
    private final boolean test;
    private final boolean ebcdic;

    @Override
    public EbicsRequest createForSigning(final EbicsSession session) {
        final StaticHeaderOrderDetailsType orderDetails;
        if (orderType.equals(OrderType.FUL)) {
            FULOrderParamsType.Builder<Void> fULOrderParamsBuilder = FULOrderParamsType.builder()
                    .withFileFormat(FileFormatType.builder()
                            .withCountryCode(session.getConfiguration().getLocale().getCountry().toUpperCase())
                            .withValue(fileFormat)
                            .build());
            if (test) {
                fULOrderParamsBuilder = fULOrderParamsBuilder.addParameters(stringParameter("TEST", "TRUE"));
            }
            if (ebcdic) {
                fULOrderParamsBuilder = fULOrderParamsBuilder.addParameters(stringParameter("EBCDIC", "TRUE"));

            }
            orderDetails = orderDetails(
                    OrderAttributeType.DZHNN,
                    orderType,
                    OBJECT_FACTORY.createFULOrderParams(fULOrderParamsBuilder.build()));
        } else {
            orderDetails = orderDetails(OrderAttributeType.OZHNN, orderType);
        }

        return request(
                session.getConfiguration(),
                header(
                        mutableHeader(TransactionPhaseType.INITIALISATION),
                        staticHeader(session, nonce, numSegments, orderDetails)),
                body(dataTransferRequestWithDigest(session, digest, nonce)));
    }
}
