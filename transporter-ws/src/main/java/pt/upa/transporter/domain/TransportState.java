package pt.upa.transporter.domain;
//estados possiveis das viagens
public enum TransportState {

    REJECTED,
    ACCEPTED,
    HEADING,
    ONGOING,
    COMPLETED;

    public String value() {
        return name();
    }

    public static TransportState fromValue(String v) {
        return valueOf(v);
    }
	
}
