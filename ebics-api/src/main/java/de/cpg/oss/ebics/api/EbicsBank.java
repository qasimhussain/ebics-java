package de.cpg.oss.ebics.api;

import javaslang.control.Either;
import lombok.*;
import lombok.experimental.Wither;

import java.net.URI;
import java.util.Collection;
import java.util.stream.Collectors;

@Value
@Wither
@Builder
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class EbicsBank implements Identifiable {

    private static final long serialVersionUID = 4L;

    @NonNull
    private final URI uri;

    private final EbicsAuthenticationKey authenticationKey;
    private final EbicsEncryptionKey encryptionKey;

    @NonNull
    private final String hostId;
    private final String name;

    private final Collection<String> supportedEbicsVersions;
    private final Collection<String> supportedOrderTypes;

    @Override
    public String getId() {
        return getHostId();
    }

    public Collection<Either<EbicsVersion, String>> getSupportedEbicsVersions() {
        return supportedEbicsVersions.stream()
                .map(EbicsVersion::ofRaw)
                .collect(Collectors.toList());
    }

    public Collection<Either<OrderType, String>> getSupportedOrderTypes() {
        return supportedOrderTypes.stream()
                .map(OrderType::ofRaw)
                .collect(Collectors.toList());
    }

    // We all love JPA, don't we?
    private EbicsBank() {
        this(URI.create(""), null, null, "", null, null, null);
    }
}
