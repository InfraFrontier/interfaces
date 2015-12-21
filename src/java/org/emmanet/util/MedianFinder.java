

package org.emmanet.util;

/*
 * #%L
 * InfraFrontier
 * $Id:$
 * $HeadURL:$
 * %%
 * Copyright (C) 2015 EMBL-European Bioinformatics Institute
 * %%
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * #L%
 */

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

/**
 *
 * @author phil
 * *This class will find the median of a list of Doubles.
 */
public class MedianFinder {
    
    public static Double find(ArrayList<Double> data) {
Double result;
System.out.println("DATA SIZE IS:::" + data.size());
//if(data.get(0) == null || data.get(1) == null || data.get(2) == null || data.get(3) == null){
    //System.out.println("data.get(0)=" + data.get(0));

//}
Collections.sort(data);
System.out.println("Begin output of values to be calculated::");
for (Iterator it = data.listIterator(); it.hasNext();) {
    Object o = it.next();
    System.out.println(o.toString());
}
System.out.println("Finish output of values to be calculated::");
if (data.size() % 2 == 1) {
// If the number of entries in the list is not even.

// Get the middle value.
// You must floor the result of the division to drop the
// remainder.
result = data.get( (int) Math.floor(data.size()/2) );

} else  if(( data.size()/2  != 0)) {
// If the number of entries in the list are even.

// Get the middle two values and average them.
 
Double lowerMiddle = data.get( data.size()/2 );
Double upperMiddle = data.get( data.size()/2 - 1 );
result = (lowerMiddle + upperMiddle) / 2;
}
else {
    result=0.0;
}
System.out.println("MEDIAN RESULT RETURNED");
System.out.println(result);
System.out.println("END OF MEDIAN RESULT RETURNED");
return result;
    }

}
