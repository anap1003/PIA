/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package parsers;

import db.Festival;

/**
 *
 * @author KoRiSnIk
 */
public class CSVParser {
    public boolean parseGeneralInfo(Festival f, String[] lines, int startIndex, int endIndex) {
        String[] line1 = lines[0].split("\\s*,\\s*");
        String[] line2 = lines[1].split("\\s*,\\s*");
        boolean ok = line1.length == 4 && line2.length == 4;
        f.setName(line2[0].substring(1, line2[0].length()-2));
        f.setLocation(line2[1].substring(1, line2[1].length()-2));
        
        return ok;
    }
    
    public boolean isEmptyLine(String[] lines, int index) {
        return lines[index].trim().isEmpty();
    }
}
