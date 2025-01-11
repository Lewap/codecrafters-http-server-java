package request;

import java.util.List;

public class Request {

    private final List<String> requestLines;
    private final String[] args;

    public Request ( List<String> requestLines, String[] args ) {
        this.requestLines = requestLines;
        this.args = args;
    }

    public String[] getArgs () {
        return this.args;
    }

    public String getRootOfTheEndpoint () {

        String result = null;
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

        String[] result = null;
        String[] VERBLineParsed;

        for (String requestLine : this.requestLines) {
            if ( requestLine.startsWith("GET") ) {
                VERBLineParsed = requestLine.split(" ");
                result = VERBLineParsed;
            }
        }

        return result;

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

}
