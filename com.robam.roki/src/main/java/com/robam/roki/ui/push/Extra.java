package com.robam.roki.ui.push;

public class Extra {
    public String display_type;
    public ExtraDTO extra;
    public BodyDTO body;
    public String msg_id;

    public static class ExtraDTO {
        public String type;
        public String id;
    }

    public static class BodyDTO {
        public String after_open;
        public String img;
        public String ticker;
        public String title;
        public String text;
    }


}
