package request;

import java.util.Map;
import org.springframework.http.HttpStatus;

public class Response {

    private final String DEFAULT_HTTP_PROTOCOL = "HTTP/1.1";

    public Response(HttpStatus startLineStatusCode) {
        this.startLineProtocol = DEFAULT_HTTP_PROTOCOL;
        this.startLineStatusCode = startLineStatusCode;
    }

    public Response(HttpStatus startLineStatusCode, String startLineStatusText) {
        this.startLineProtocol = DEFAULT_HTTP_PROTOCOL;
        this.startLineStatusCode = startLineStatusCode;
        this.startLineStatusText = startLineStatusText;
    }

    public Response(HttpStatus startLineStatusCode, Map<String, String> headers, String body) {
        this.startLineProtocol = DEFAULT_HTTP_PROTOCOL;
        this.startLineStatusCode = startLineStatusCode;
        this.headers = headers;
        this.body = body;
    }

    public Response(HttpStatus startLineStatusCode, Map<String, String> headers, byte[] body) {
        this.startLineProtocol = DEFAULT_HTTP_PROTOCOL;
        this.startLineStatusCode = startLineStatusCode;
        this.headers = headers;
        this.bodyAsByteArray = body;
    }

    private String startLineProtocol;
    private HttpStatus startLineStatusCode;
    private String startLineStatusText;

    private Map<String, String> headers;
    private String body;
    private byte[] bodyAsByteArray;

    public byte[] getBodyAsByteArray () {
        return bodyAsByteArray;
    }

    public void setBody (String body) {
        this.body = body;
    }

    @Override
    public String toString () {

        String startLineStatusString = this.startLineStatusCode.value() + " " + this.startLineStatusCode.getReasonPhrase();

        String startLineString = this.startLineProtocol + " "
                + startLineStatusString;

        if ( startLineStatusText != null ) {
            startLineString += " " + this.startLineStatusText;
        }

        StringBuilder headersStringBuilder = new StringBuilder();
        if (this.headers != null) {
            for (Map.Entry<String, String> entry : this.headers.entrySet()) {
                headersStringBuilder.append(entry.getKey())
                        .append(": ")
                        .append(entry.getValue())
                        .append("\r\n");
            }
        }

        String headersString = headersStringBuilder.toString();

        String result = startLineString + "\r\n";

        if ( headersString != null ) {
            result += headersString;
        }

        result += "\r\n";

        if ( this.body != null ) {
            result += this.body;
        }

        return result;
    }

}
