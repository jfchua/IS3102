package application.enumeration;

import com.fasterxml.jackson.annotation.JsonProperty;

public enum Subscription {
	@JsonProperty("COMMONINFRA")
	COMMONINFRA,
	@JsonProperty("PROPERTY")
	PROPERTY,
	@JsonProperty("EVENT")
	EVENT,
	@JsonProperty("FINANCE")
	FINANCE,
	@JsonProperty("TICKETING")
	TICKETING,
	@JsonProperty("BI")
	BI;

}
