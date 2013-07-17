package com.suitedslime.efficientcraft.core.helper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.suitedslime.efficientcraft.util.IdMetaPair;

public abstract class ECHelper {

    private static Map<IdMetaPair, List<String>> tooltipMap = new HashMap<IdMetaPair, List<String>>();

    public static void registerTooltip(int id, int meta, String line) {
        IdMetaPair pair = new IdMetaPair(id, meta);

        if (tooltipMap.get(pair) == null) {
            List<String> temp = new ArrayList<String>();
            temp.add(line);
            tooltipMap.put(pair, temp);
        } else {
            tooltipMap.get(pair).add(line);
        }
    }

    public static Map<IdMetaPair, List<String>> getTooltipMap() {
        return tooltipMap;
    }
}
