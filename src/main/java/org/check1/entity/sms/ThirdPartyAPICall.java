package org.check1.entity.sendsmsentities;

import lombok.Data;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Data
public class ThirdPartyAPICall {

    private String deliverychannel="sms";
    private Channels channels;
    private List<Destination> destination = new ArrayList<>();

    public ThirdPartyAPICall(String message, List<String> phoneNumbers)
    {
        this.channels = new Channels(message);
        this.destination.add(new Destination(phoneNumbers));
    }

    public void setChannels(String message)
    {
        this.channels = new Channels(message);
    }
    public void setDestination(List<String> phoneNumbers)
    {
        this.destination.add(new Destination(phoneNumbers));
    }

    @Data
    public static class Channels{
        private Sms sms;
        public Channels(String message)
        {
            this.sms=new Sms(message);
        }

        @Data
        public static class Sms{
            private String text;
            public Sms(String message)
            {
                this.text=message;
            }
        }
    }

    @Data
    public static class Destination{
        private List<String> msisdn = new ArrayList<>();
        String correlationId = UUID.randomUUID().toString();
        public Destination(List<String> phoneNumbers)
        {
            msisdn.addAll(phoneNumbers);
        }
    }
}
