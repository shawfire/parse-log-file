package net.shawfire.plf;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class IPAddressParser {

    private static Logger logger = LoggerFactory.getLogger(IPAddressParser.class);

    private static final int IP_ADDRESS_FIELD = 1;

    private Map<String, Integer> ipAddressCount = new HashMap<>();

    public Map<String, Integer> getIpAddressCount() {
        return ipAddressCount;
    }

    public void addIfUrl(String field, int count) {
        if (count == IP_ADDRESS_FIELD) {
            ParseUtils.addItemToCount(ipAddressCount, field);
        }
    }

    int getNumberOfIPs() {
        int uniqueCount = ipAddressCount.size();
        logger.info("Number of unique IP addresses: {}",  uniqueCount );
        return uniqueCount;
    }

    String[] getMostActiveIPs(int limit) {
        String[] mostActiveIPs = ParseUtils.getMostFrequent(ipAddressCount, limit);
        logger.info("The top {} most active IP addresses: {}",  limit, mostActiveIPs );
        return mostActiveIPs;
    }

    Map<String, Integer> getMostActiveIPCounts(int limit) {
        Map<String, Integer> mostActiveIPCounts = ParseUtils.sortByValue(ipAddressCount, limit);
        return mostActiveIPCounts;
    }

}
