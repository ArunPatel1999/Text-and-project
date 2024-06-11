package org.arun;

import lombok.experimental.UtilityClass;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class CollectionsMethods {

    public static void main(String[] args) {
        System.out.println("Create List 100 Word Ram Unmutable =>"+ Collections.nCopies(2, "Ram"));

        List<Integer> accurnce = Arrays.asList(1,2,3,1,46,257,75,3,7,85);
        System.out.println("Accurence of 3 in list is => "+Collections.frequency( accurnce, 3));
        System.out.println("All element accured in list => "+ accurnce.stream()
                .collect(Collectors.toMap(x -> x,
                        y -> Collections.frequency(accurnce, y),
                        (old, newr) -> newr)));

        List<Integer> accurnce1 = Arrays.asList(1, 2, 3, 4, 5);
        System.out.println("both list contain commona element =>"+(!Collections.disjoint(accurnce1, accurnce)));

        Collections.rotate(accurnce1, 2);
        System.out.println("rotate Right => "+accurnce1);

        Collections.rotate(accurnce1, -2);
        System.out.println("rotate left => "+accurnce1);

    }

}
