package de.cpg.oss.ebics.api;

import javaslang.collection.Stream;
import javaslang.control.Either;
import lombok.Getter;

public enum OrderType {
    // User key management
    INI(Transmission.UPLOAD, "Send password initialisation", Presence.MANDATORY),
    HIA(Transmission.UPLOAD, "Transmission of the subscriber key for identification and authentication and encryption within the framework of subscriber initialisation", Presence.MANDATORY),
    HPB(Transmission.DOWNLOAD, "Transfer the public bank key", Presence.MANDATORY),
    HPD(Transmission.DOWNLOAD, "Return bank parameters", Presence.MANDATORY),
    HSA(Transmission.UPLOAD, "Transmission of the subscriber key for identification and authentication and encryption within the framework of subscriber initialisation for subscribers that have remote access data transmission via FTAM"),
    HAA(Transmission.DOWNLOAD, "Download retrievable order types"),
    HKD(Transmission.DOWNLOAD, "Download customer’s customer and subscriber data"),
    HTD(Transmission.DOWNLOAD, "Download subscriber’s customer and subscriber data"),
    HAC(Transmission.DOWNLOAD, "Download customer acknowledgement (XML-format)", Presence.MANDATORY),
    HCA(Transmission.UPLOAD, "Send amendment of the subscriber key for identification and authentication and encryption", Presence.MANDATORY),
    HCS(Transmission.UPLOAD, "Transmission of the subscriber key for ES, identification and authentication and encryption", Presence.MANDATORY),
    HEV(Transmission.DOWNLOAD, "Download supported EBICS versions", Presence.MANDATORY),
    SPR(Transmission.UPLOAD, "Suspension of access authorisation", Presence.MANDATORY),

    FUL(Transmission.UPLOAD, "Upload file with any format"),
    FDL(Transmission.DOWNLOAD, "Download file with any format"),

    // distributed electronic signature
    HVU(Transmission.DOWNLOAD, "Download VEU overview", Presence.CONDITIONAL),
    HVZ(Transmission.DOWNLOAD, "Download VEU overview with additional information", Presence.CONDITIONAL),
    HVD(Transmission.DOWNLOAD, "Retrieve VEU state", Presence.CONDITIONAL),
    HVT(Transmission.DOWNLOAD, "Retrieve VEU transaction details", Presence.CONDITIONAL),
    HVE(Transmission.UPLOAD, "Add VEU signature", Presence.CONDITIONAL),
    HVS(Transmission.UPLOAD, "VEU cancellation", Presence.CONDITIONAL),

    // Order types with standardised format
    AEA(Transmission.UPLOAD, "Upload free text message (export letters of credit)"),
    AIA(Transmission.UPLOAD, "Upload import letters of credit"),
    AID(Transmission.UPLOAD, "Upload import letters of credit documentation recording"),
    AKA(Transmission.DOWNLOAD, "Download import letters of credit"),
    AKD(Transmission.DOWNLOAD, "Download import letters of credit, invoicing"),
    AZM(Transmission.UPLOAD, "Upload foreign payment transaction in magnetic tape format (variable record length)"),
    AZV(Transmission.UPLOAD, "Upload foreign payment transaction in diskette format"),
    AZ2(Transmission.UPLOAD, "Upload foreign payment transaction in magnetic tape format (record length field 2 bytes)"),
    AZ4(Transmission.UPLOAD, "Upload foreign payment transaction in magnetic tape format (record length field 4 bytes)"),
    B1H(Transmission.UPLOAD, "Presentation of a debit transfer at the merchant’s bank"),
    B1K(Transmission.UPLOAD, "Presentation of a debit transfer at the buyer’s bank"),
    BCH(Transmission.UPLOAD, "Presentation of a credit transfer at the merchant’s bank"),
    BCK(Transmission.UPLOAD, "Presentation of a credit transfer at the buyer’s bank"),
    BDH(Transmission.UPLOAD, "Presentation of dispute initiated credit transfer at the merchant’s bank"),
    BDK(Transmission.UPLOAD, "Presentation of dispute initiated credit transfer at the buyer’s bank"),
    CBC(Transmission.DOWNLOAD, "Download payment status report for direct debit via XML container"),
    CCC(Transmission.UPLOAD, "Upload credit transfer initiation via XML container"),
    CCS(Transmission.UPLOAD, "Upload SEPA credit transfers from Service Data Processing Centre"),
    CCT(Transmission.UPLOAD, "Upload Credit Transfer Initiation (DK/EPC specification of the SEPA credit transfer)"),
    CCU(Transmission.UPLOAD, "Upload Credit Transfer Initiation with urgent payments (non-SEPA)"),
    CCX(Transmission.UPLOAD, "Order type for the identification of SEPA credit transfers submitted by CCS if the VEU is used in the SRZ process (only for internal use on the EBICS server)" +
            "VEU = Distributed Electronic Signature SRZ = Service Data Processing Centre"),
    CDB(Transmission.UPLOAD, "Upload direct debit initiation (SEPA business to business (B2B) direct debit)"),
    CDC(Transmission.UPLOAD, "Upload direct debit initiation via XML container (SEPA core direct debit)"),
    CDD(Transmission.UPLOAD, "Upload direct debit initiation (SEPA core direct debit)"),
    CDS(Transmission.UPLOAD, "Upload SEPA core direct debits from Service Data Processing Centre"),
    CDX(Transmission.UPLOAD, "Order type for the identification of SEPA core direct debits submitted by CDS if the VEU is used in the SRZ process (only for internal use on the EBICS server)" +
            "VEU = Distributed Electronic Signature SRZ = Service Data Processing Centre"),
    CDZ(Transmission.DOWNLOAD, "Download payment status report for direct debit"),
    CK7(Transmission.UPLOAD, "Upload SEPA Cards Clearing Reversal via XML- container"),
    CK8(Transmission.UPLOAD, "Upload SEPA Cards Clearing direct debit initiation via XML-Container"),
    CRC(Transmission.DOWNLOAD, "Download payment status report for credit transfer via XML container"),
    CRZ(Transmission.DOWNLOAD, "Download payment status report for credit transfer"),
    CXN(Transmission.UPLOAD, "Forward SEPA Cards Clearing (SCC) transactions initiated by a Service Data Processing Centre via order types CK7 and CK8 to the account servicing institution"),
    CXS(Transmission.UPLOAD, "Forward SEPA Credit Transfers and Direct Debits initiated by a Service Data Processing Centre via order types CCS, CDS resp. C2S to the account servicing institution"),
    CX7(Transmission.UPLOAD, "Upload SEPA Cards Clearing reversal"),
    CX8(Transmission.UPLOAD, "Upload SEPA Cards Clearing direct debit initiation"),
    C2C(Transmission.UPLOAD, "Upload direct debit initiation via XML container (SEPA business to business (B2B) direct debit)"),
    C2S(Transmission.UPLOAD, "Upload SEPA direct debits (B2B) from Service Data Processing Centre"),
    C2X(Transmission.UPLOAD, "Order type for the identification of SEPA direct debits (B2B) submitted by C2S if the VEU is used in the SRZ process (only for internal use on the EBICS server)" +
            "VEU = Distributed Electronic Signature SRZ = Service Data Processing Centre"),
    C52(Transmission.DOWNLOAD, "Download bank to customer account report"),
    C53(Transmission.DOWNLOAD, "Download bank to customer statement report"),
    C54(Transmission.DOWNLOAD, "Download bank to customer debit credit notification"),
    C7X(Transmission.UPLOAD, "Order type for the identification of SEPA Card Clearing (SCC) reversal submitted by CK7 or CX7 if the VEU is used in the SRZ process (only for internal use on the EBICS server)" +
            "VEU = Distributed Electronic Signature SRZ = Service Data Processing Centre"),
    C8X(Transmission.UPLOAD, "Order type for the identification of SEPA Card Clearing direct debit initiation submitted by CK8 or CX8 if the VEU is used in the SRZ process (only for internal use on the EBICS server)" +
            "VEU = Distributed Electronic Signature SRZ = Service Data Processing Centre"),
    DDG(Transmission.DOWNLOAD, "Download foreign exchange confirmation"),
    DHB(Transmission.UPLOAD, "Upload foreign exchange confirmation"),
    DTI(Transmission.DOWNLOAD, "Download IZV file"),
    EAB(Transmission.DOWNLOAD, "Download export letters of credit"),
    EAD(Transmission.DOWNLOAD, "Download export letters of credit, invoicing"),
    FTB(Transmission.BOTH, "Upload or download any file"),
    FTD(Transmission.BOTH, "Upload or download free text file"),
    GBZ(Transmission.UPLOAD, "Submitting of BZAHL files (Cash card transaction data)"),
    GFB(Transmission.DOWNLOAD, "Download guarantee consecutive messages (Query to extend or pay, claim for payment information, settlement of claim for payment and/or charges)"),
    GFK(Transmission.UPLOAD, "Upload guarantee consecutive messages (Response to extend or pay query, request for reduction or release)"),
    GUB(Transmission.DOWNLOAD, "Download guarantee messages (Issuance, amendment, free format, advice of reduction or release)"),
    GUK(Transmission.UPLOAD, "Upload guarantee messages (Issuance, amendment, free format)"),
    IDD(Transmission.UPLOAD, "Upload international debit entries"),
    IIB(Transmission.DOWNLOAD, "Download import collections"),
    IIK(Transmission.UPLOAD, "Upload import collections"),
    INT(Transmission.UPLOAD, "Upload international payment transaction"),
    RFT(Transmission.UPLOAD, "Upload request for transfer"),
    STA(Transmission.DOWNLOAD, "Download SWIFT daily accounts"),
    TST(Transmission.BOTH, "Upload or download ASCII test file"),
    VMK(Transmission.DOWNLOAD, "Download short-term acknowledgement slips"),
    WPA(Transmission.DOWNLOAD, "Download bought/sold notes"),
    WPC(Transmission.DOWNLOAD, "Download bond confirmation slips"),

    // Order types without standardised format
    BKA(Transmission.DOWNLOAD, "Order type for electronic account statements"),
    CD1(Transmission.UPLOAD, "Upload direct debit initiation (SEPA core direct debit with local instrument = COR1)"),
    CZ3(Transmission.UPLOAD, "Request credit transfers by sending an xml- container with pain.013 messages"),
    CZ4(Transmission.UPLOAD, "Reject requested credit transfers by sending an xml-container with pain.014 messages"),
    C07(Transmission.UPLOAD, "Customer Payment Reversal with Direct Debit BusinessTransaction"),
    C1C(Transmission.UPLOAD, "Upload direct debit initiation via XML container (SEPA core direct debit with local instrument = COR1)"),
    C1S(Transmission.UPLOAD, "Upload direct debit initiation (SEPA core direct debit with local instrument = COR1) from Service Data Processing Centre"),
    C1X(Transmission.UPLOAD, "Order type for the identification of SEPA core direct debits (with with local instrument = COR1) submitted by C1S if the VEU is used in the SRZ process (only for internal use on the EBICS server)" +
            "VEU = Distributed Electronic Signature SRZ = Service Data Processing Centre"),
    C29(Transmission.DOWNLOAD, "Resolution of Investigation"),
    C55(Transmission.UPLOAD, "Customer Payment Cancellation Request"),
    C86(Transmission.DOWNLOAD, "Bank Services Billing Statement"),
    DKI(Transmission.DOWNLOAD, "Download foreign exchange rate information (Euro)"),
    DMI(Transmission.DOWNLOAD, "Download foreign exchange market information"),
    DSW(Transmission.DOWNLOAD, "Download foreign exchange swap information"),
    DTE(Transmission.UPLOAD, "Upload rush order (IZV in DTAUS0 format)"),
    EEA(Transmission.DOWNLOAD, "Download EDIFACT ASCII"),
    EEZ(Transmission.DOWNLOAD, "Download EDIFACT EBCDIC"),
    EIB(Transmission.DOWNLOAD, "Download implementation display (export collection) bank to customer"),
    EIK(Transmission.UPLOAD, "Upload export collections"),
    ESA(Transmission.UPLOAD, "Upload EDIFACT ASCII"),
    ESG(Transmission.DOWNLOAD, "Download ESG file for electronic second signature"),
    ESP(Transmission.UPLOAD, "Upload ESP file for electronic second signature"),
    ESR(Transmission.UPLOAD, "Submission of EDIFACT debit entries"),
    EUE(Transmission.UPLOAD, "Upload same-day cross-border Euro express payment"),
    GRC(Transmission.DOWNLOAD, "Download of response files (Processing result of cash card transaction data)"),
    IBI(Transmission.DOWNLOAD, "Download response to information request"),
    IBK(Transmission.DOWNLOAD, "Download institution acknowledgement file complete file"),
    IBU(Transmission.DOWNLOAD, "Download institution acknowledgement file daily update"),
    IBW(Transmission.DOWNLOAD, "Download institution acknowledgement file complete file, other file"),
    IKI(Transmission.UPLOAD, "Upload information request"),
    IKK(Transmission.UPLOAD, "Upload institution accounts. Complete file limited to 170 MB"),
    IKU(Transmission.UPLOAD, "Upload institution accounts daily update"),
    IKW(Transmission.UPLOAD, "Upload institution accounts complete file other file"),
    KTH(Transmission.UPLOAD, "KTOHIN. Automated process for changing account numbers and bank sort codes"),
    KTR(Transmission.DOWNLOAD, "KTORUECK. Automated process for changing account numbers and bank sort codes"),
    UPD(Transmission.DOWNLOAD, "Download updates"),

    // Reserved order types for inter-organisation payment traffic/file exchange
    FIN(Transmission.UPLOAD, "Upload EDIFACT-FINPAY"),
    IZS(Transmission.DOWNLOAD, "Information from central offices"),
    SSP(Transmission.DOWNLOAD, "EC card suspension file"),

    // Order types for a communication with the Deutsche Bundesbank
    QC1(Transmission.UPLOAD, "INPUT CREDIT FILE (ICF)"),
    QB1(Transmission.UPLOAD, "BILATERAL INPUT CREDIT FILE (BCF)"),
    QD5(Transmission.UPLOAD, "INPUT CORE DEBIT FILE (CORE IDF)"),
    QD6(Transmission.UPLOAD, "INPUT B2B DEBIT FILE (B2B IDF)"),
    QK1(Transmission.UPLOAD, "SCC INPUT DEBIT FILE (SCC IDF)"),
    QB2(Transmission.DOWNLOAD, "BILATERAL SETTLED CREDIT FILE (BCF)"),
    QC2(Transmission.DOWNLOAD, "CREDIT VALIDATION FILE (CVF)"),
    QC3(Transmission.DOWNLOAD, "SETTLED CREDIT FILE (SCF)"),
    QK2(Transmission.DOWNLOAD, "SCC DEBIT VALIDATION FILE (SCC DVF)"),
    QK3(Transmission.DOWNLOAD, "SCC DEBIT NOTIFICATION FILE (SCC DNF)"),
    QK4(Transmission.DOWNLOAD, "SCC SETTLED DEBIT FILE (SCC SDF)"),
    QR1(Transmission.DOWNLOAD, "DAILY RECONCILIATION REPORT FOR CREDIT Transfers (DRC)"),
    QR5(Transmission.DOWNLOAD, "DAILY RECONCILIATION REPORT FOR SCC (DRR SCC)"),
    QD7(Transmission.DOWNLOAD, "CORE DEBIT VALIDATION FILE (DVF)"),
    QD8(Transmission.DOWNLOAD, "CORE DEBIT NOTIFICATION FILE (DNF)"),
    QD9(Transmission.DOWNLOAD, "SETTLED CORE DEBIT FILE (SDF)"),
    QDA(Transmission.DOWNLOAD, "B2B DEBIT VALIDATION FILE (DVF)"),
    QDB(Transmission.DOWNLOAD, "B2B DEBIT NOTIFICATION FILE (DNF)"),
    QDC(Transmission.DOWNLOAD, "SETTLED B2B DEBIT FILE (SDF)"),
    QR3(Transmission.DOWNLOAD, "DAILY RECONCILIATION REPORT FOR CORE DIRECT DEBITS (DRD CORE)"),
    QR4(Transmission.DOWNLOAD, "DAILY RECONCILIATION REPORT FOR B2B DIRECT DEBITS (DRD B2B)"),
    QSD(Transmission.DOWNLOAD, "SEPA-Clearer Directory"),
    QS1(Transmission.UPLOAD, "SVV BSE INPUT DEBIT FILE"),
    QS2(Transmission.UPLOAD, "SVV ISE INPUT DEBIT FILE"),
    QS3(Transmission.UPLOAD, "SVV ISR INPUT DEBIT FILE"),
    QS4(Transmission.DOWNLOAD, "SVV BSE DEBIT VALIDATION FILE"),
    QS5(Transmission.DOWNLOAD, "SVV BSE DEBIT NOTIFICATION FILE"),
    QS6(Transmission.DOWNLOAD, "SVV BSE SETTLED DEBIT FILE"),
    QS7(Transmission.DOWNLOAD, "SVV ISE DEBIT VALIDATION FILE"),
    QS8(Transmission.DOWNLOAD, "SVV ISE DEBIT NOTIFICATION FILE"),
    QS9(Transmission.DOWNLOAD, "SVV ISR SETTLED DEBIT FILE"),
    QSA(Transmission.DOWNLOAD, "SVV ISR DEBIT VALIDATION FILE"),
    QR6(Transmission.DOWNLOAD, "DAILY RECONCILIATION REPORT FOR SVV BSE (DRD BSE)"),
    QR7(Transmission.DOWNLOAD, "DAILY RECONCILIATION REPORT FOR SVV ISE (DRD ISE)"),
    QR8(Transmission.DOWNLOAD, "DAILY RECONCILIATION REPORT FOR SVV ISE (DRD ISR)"),
    QEA(Transmission.UPLOAD, "LB file, direct debits and payments from credit institutions arising from the paperless cheque collection procedure"),
    QI3(Transmission.UPLOAD, "IB file; cost rates ISE, from credit institutions"),
    QE4(Transmission.DOWNLOAD, "LB file; direct debits and payments out of the paperless cheque collection procedure to credit institutions"),
    QI2(Transmission.DOWNLOAD, "IB file; ISE clearing data records, to credit institutions"),
    QM3(Transmission.DOWNLOAD, "M3 message: Notification of a non-processable file or submission outside of the time frame (IB file)"),
    QM6(Transmission.DOWNLOAD, "M6 message: List of files processed in the morning windows or additional notification of ISE clearing data records without a corresponding image"),
    QM7(Transmission.DOWNLOAD, "M7 message: Notification of payments which have not been executed or have been cancelled"),
    QM8(Transmission.DOWNLOAD, "M8 message: Notification of non-processable data records"),
    QM9(Transmission.DOWNLOAD, "M9 message: Notification of processed payments and delivered files; simultaneously serves as final notification that RPS has been completed"),
    QE3(Transmission.BOTH, "LB file; direct debits and payments arising from the paperless cheque collection procedure, bank file"),
    QI1(Transmission.BOTH, "IB file; ISE clearing data records, bank file"),
    QG1(Transmission.UPLOAD, "GT file; Prior1 credit transfers from banks"),
    QG2(Transmission.UPLOAD, "GT file; Prior1 credit transfers from banks"),
    QDT(Transmission.UPLOAD, "DT file; Prior1 international credit transfers (in euro) from banks"),
    QWT(Transmission.UPLOAD, "WT file; Prior1 international credit transfers (in foreign currency) from banks"),
    QG3(Transmission.DOWNLOAD, "GT file, Prior1 credit transfers to banks"),
    QG4(Transmission.DOWNLOAD, "GT file; Prior1 credit transfers to banks"),
    QWA(Transmission.DOWNLOAD, "Settlement of foreign currency payments (WA files)"),
    QMH(Transmission.DOWNLOAD, "M6 message: free text message or information file to banks"),
    QMA(Transmission.UPLOAD, "MA file; request for transaction volume and account balance during the day"),
    QMU(Transmission.DOWNLOAD, "Information on transaction volume and account balance during the day"),
    QMK(Transmission.DOWNLOAD, "MK file, customer statement message"),
    QMN(Transmission.DOWNLOAD, "M3 file, notification on a not processible MA file"),
    JAA(Transmission.UPLOAD, "Single ICF"),
    JBA(Transmission.UPLOAD, "Batch ICF"),
    JCA(Transmission.UPLOAD, "Single IDF"),
    JDA(Transmission.UPLOAD, "Batch IDF"),
    JEA(Transmission.UPLOAD, "Single IDF B2B"),
    JFA(Transmission.UPLOAD, "Batch IDF B2B"),
    JAB(Transmission.DOWNLOAD, "Single CVF"),
    JBB(Transmission.DOWNLOAD, "Batch CVF"),
    JAC(Transmission.DOWNLOAD, "Single SCF"),
    JBC(Transmission.DOWNLOAD, "Batch SCF"),
    JAD(Transmission.DOWNLOAD, "Single CCF"),
    JBD(Transmission.DOWNLOAD, "Batch CCF"),
    JAG(Transmission.DOWNLOAD, "Single PCF"),
    JBG(Transmission.DOWNLOAD, "Batch PCF"),
    JA1(Transmission.DOWNLOAD, "Daily Reconciliation Report (DRR)"),
    JA2(Transmission.DOWNLOAD, "Monthly Statistical Report (MSR)"),
    JA3(Transmission.DOWNLOAD, "Routing Table File (RTF)"),
    JA4(Transmission.DOWNLOAD, "Cycle Reconciliation Report (CRR)"),
    JA5(Transmission.DOWNLOAD, "Pre settlement Report"),
    JA6(Transmission.DOWNLOAD, "Related to a Monthly Advice Report"),
    JCB(Transmission.DOWNLOAD, "Single DVF"),
    JDB(Transmission.DOWNLOAD, "Batch DVF"),
    JCC(Transmission.DOWNLOAD, "Single SDF"),
    JDC(Transmission.DOWNLOAD, "Batch SDF"),
    JCD(Transmission.DOWNLOAD, "Single CDF"),
    JDD(Transmission.DOWNLOAD, "Batch CDF"),
    JCE(Transmission.DOWNLOAD, "Single DNF"),
    JDE(Transmission.DOWNLOAD, "Batch DNF"),
    JCF(Transmission.DOWNLOAD, "Single RSF"),
    JDF(Transmission.DOWNLOAD, "Batch RSF"),
    JC1(Transmission.DOWNLOAD, "Daily Reconciliation Report (DRR)"),
    JC2(Transmission.DOWNLOAD, "Monthly Statistical Report (MSR)"),
    JC3(Transmission.DOWNLOAD, "Routing Table File (RTF)"),
    JC5(Transmission.DOWNLOAD, "Pre Settlement Report (PSR)"),
    JC6(Transmission.DOWNLOAD, "Related to a Monthly Advice Report"),
    JGB(Transmission.DOWNLOAD, "Single DVF SCC"),
    JHB(Transmission.DOWNLOAD, "Batch DVF SCC"),
    JGC(Transmission.DOWNLOAD, "Single SDF SCC"),
    JHC(Transmission.DOWNLOAD, "Batch SDF SCC"),
    JGD(Transmission.DOWNLOAD, "Single CDF SCC"),
    JHD(Transmission.DOWNLOAD, "Batch CDF SCC"),
    JGE(Transmission.DOWNLOAD, "Single DNF SCC"),
    JHE(Transmission.DOWNLOAD, "Batch DNF SCC"),
    JGF(Transmission.DOWNLOAD, "Single RSF SCC"),
    JHF(Transmission.DOWNLOAD, "Batch RSF SCC"),
    JG1(Transmission.DOWNLOAD, "Daily Reconciliation Report (DRR)"),
    JG2(Transmission.DOWNLOAD, "Monthly Statistical Report (MSR)"),
    JG3(Transmission.DOWNLOAD, "Routing Table File (RTF)"),
    JG5(Transmission.DOWNLOAD, "Pre Settlement Report (PSR)"),
    JEB(Transmission.DOWNLOAD, "Single DVF B2B"),
    JFB(Transmission.DOWNLOAD, "Batch DVF B2B"),
    JEC(Transmission.DOWNLOAD, "Single SDF B2B"),
    JFC(Transmission.DOWNLOAD, "Batch SDF B2B"),
    JED(Transmission.DOWNLOAD, "Single CDF B2B"),
    JFD(Transmission.DOWNLOAD, "Batch CDF B2B"),
    JEE(Transmission.DOWNLOAD, "Single DNF B2B"),
    JFE(Transmission.DOWNLOAD, "Batch DNF B2B"),
    JEF(Transmission.DOWNLOAD, "Single RSF B2B"),
    JFF(Transmission.DOWNLOAD, "Batch RSF B2B"),
    JE1(Transmission.DOWNLOAD, "Daily Reconciliation Report (DRR)"),
    JE2(Transmission.DOWNLOAD, "Monthly Statistical Report (MSR)"),
    JE3(Transmission.DOWNLOAD, "Routing Table File (RTF)"),
    JE5(Transmission.DOWNLOAD, "Pre Settlement Report (PSR)"),
    JE6(Transmission.DOWNLOAD, "Related to a Monthly Advice Report");

    OrderType(final Transmission transmission, final String description, final Presence presence) {
        this.transmission = transmission;
        this.description = description;
        this.presence = presence;
    }

    OrderType(final Transmission transmission, final String description) {
        this(transmission, description, Presence.OPTIONAL);
    }

    public enum Transmission {
        DOWNLOAD,
        UPLOAD,
        BOTH
    }

    public boolean isMandatory(final boolean germanBank) {
        return Presence.MANDATORY.equals(presence) || (Presence.CONDITIONAL.equals(presence) && germanBank);
    }

    public static Either<OrderType, String> ofRaw(final String rawValue) {
        return Stream.of(values()).find(orderType -> orderType.name().equals(rawValue))
                .map(Either::<OrderType, String>left)
                .getOrElse(Either.right(rawValue));
    }

    @Getter
    private final Transmission transmission;
    @Getter
    private final String description;
    private final Presence presence;

    private enum Presence {
        MANDATORY,
        OPTIONAL,
        CONDITIONAL
    }
}
