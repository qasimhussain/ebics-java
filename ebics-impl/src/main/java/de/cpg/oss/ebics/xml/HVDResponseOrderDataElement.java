package de.cpg.oss.ebics.xml;

import de.cpg.oss.ebics.api.DetailedVEUOrder;
import de.cpg.oss.ebics.api.EbicsConfiguration;
import de.cpg.oss.ebics.api.SignatureVersion;
import de.cpg.oss.ebics.api.VEUOrder;
import de.cpg.oss.ebics.utils.XmlUtil;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.ebics.h004.HVDResponseOrderDataType;

import java.io.InputStream;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class HVDResponseOrderDataElement implements ResponseOrderDataElement<HVDResponseOrderDataType> {

    @Getter
    private final HVDResponseOrderDataType responseOrderData;

    public static HVDResponseOrderDataElement parse(final InputStream orderDataXml) {
        return new HVDResponseOrderDataElement(XmlUtil.parse(HVDResponseOrderDataType.class, orderDataXml));
    }

    public DetailedVEUOrder detailedVEUOrder(final EbicsConfiguration configuration,
                                             final VEUOrder veuOrder) {
        return DetailedVEUOrder.builder()
                .order(veuOrder)
                .dataDigest(responseOrderData.getDataDigest().getValue())
                .dataSignatureVersion(SignatureVersion.ofRaw(responseOrderData.getDataDigest().getSignatureVersion()))
                .summary(new String(responseOrderData.getDisplayFile(), configuration.getVeuDisplayFileCharset()))
                .build();
    }

    public boolean isOrderDetailsAvailable() {
        return responseOrderData.isOrderDetailsAvailable();
    }

    @Override
    public Class<HVDResponseOrderDataType> getResponseOrderDataClass() {
        return HVDResponseOrderDataType.class;
    }
}
