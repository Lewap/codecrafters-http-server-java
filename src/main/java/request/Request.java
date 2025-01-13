package request;

import java.util.List;

public class Request {

    private final List<String> requestLines;
    private final String[] args;
    private String body;

    public Request ( List<String> requestLines, String[] args ) {
        this.requestLines = requestLines;
        this.args = args;
    }

    public String[] getArgs () {
        return this.args;
    }

    public String getRootOfTheEndpoint () {

        String result;
        String[] VERBLineParsed = getVERBLineParsed();
        String uri = VERBLineParsed[VERBLineParsed.length-2];
        String[] uriParsed = uri.split("/");

        if (uriParsed.length == 0) {
            result = "/";
        } else {
            result = uriParsed[1];
        }

        return result;
    }

    public String[] getEndpointParsed () {

        String[] VERBLineParsed = getVERBLineParsed();
        String uri = VERBLineParsed[VERBLineParsed.length-2];

        return uri.split("/");

    }

    public String[] getVERBLineParsed () {

        return this.requestLines.get(0).split(" ");

    }

    public String getVERB () {

        return getVERBLineParsed()[0];

    }

    public String[] getUserAgentParsed () {

        String[] result = null;
        String[] UserAgentLineParsed;

        for (String requestLine : this.requestLines) {
            if ( requestLine.startsWith("User-Agent:") ) {
                UserAgentLineParsed = requestLine.split(" ");
                result = UserAgentLineParsed;
            }
        }
        return result;
    }

    public int getContentLength () {

        int result = -1;
        String[] ContentLengthLineParsed = null;

        for (String requestLine : this.requestLines) {
            if ( requestLine.startsWith("Content-Length:") ) {
                ContentLengthLineParsed = requestLine.split(" ");
                result = Integer.parseInt(ContentLengthLineParsed[1]);
            }
        }

        return result;

    }

    public void setBody (String body) {
        this.body = body;
    }

    public String getBody () {
        return this.body;
    }

}
