package net.shawfire.plf;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ParseUtils {

    public static String[] getMostFrequent(final Map<String, Integer> keyCounts, final int limit) {
        List<String> keys = keyCounts.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .map(Map.Entry::getKey)
                .limit(limit)
                .collect(Collectors.toList());
        String[] keyArray = keys.toArray(new String[keys.size()]);
        return keyArray;
    }

    public static Map<String, Integer> sortByValue(final Map<String, Integer> keyCounts, final int limit) {
        return keyCounts.entrySet()
                .stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(limit)
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue, (e1, e2) -> e1, LinkedHashMap::new));
    }

    public static void addItemToCount(final Map<String, Integer> countMap, final String field) {
        int count = 1;
        if (countMap.containsKey(field)) {
            count += countMap.get(field);
            countMap.replace(field, count);
        }
        else {
            countMap.put(field, count);
        }
    }
}
