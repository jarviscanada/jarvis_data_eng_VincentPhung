package ca.jrvs.apps.grep;
public interface RegexExc {

    public boolean matchJpeg(String filename);
    //return true if the file extension is jpeg or jpg (case insensitive)

    public boolean matchIp(String ip);
    //return true if ip is valid, IP range is from 0.0.0.0 to 999.999.999.999

    public boolean isEmptyLine(String line);
    //return true if the line is empty (e.g empty, white space, tabs, etc...)

}
