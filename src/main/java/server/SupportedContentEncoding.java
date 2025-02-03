package server;

public enum SupportedContentEncoding {

    GZIP("gzip");

    private final String value;

    SupportedContentEncoding(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public static SupportedContentEncoding findByValue(String value) {
        SupportedContentEncoding result = null;
        for (SupportedContentEncoding encoding : values()) {
            if (encoding.getValue().equalsIgnoreCase(value)) {
                result = encoding;
                break;
            }
        }
        return result;
    }

    public static boolean isSupported (String[] values) {

        boolean result = false;

        for (String value : values) {
            if ( findByValue(value)!=null ) {
                result = true;
                break;
            }
        }

        return result;
    }

}
