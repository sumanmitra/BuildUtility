package build.util.application;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

public class Test {
	
	public static void main(String... doYourBest) {
        Set<Object> set = new HashSet<>();
       
        set.add(new Simpson("Homer"));
  //      set.add(new Simpson("Homer"));
        set.add(new Simpson("Bart"));
        set.add(new Simpson("BART"));
        set.add(new Simpson("Krusty"));
    //    set.add(new Simpson("Krusty"));
      //  set.add(new Simpson("Krusty"));

        System.out.println(set.size());
        
        HashMap map = new HashMap();
        Collections.synchronizedMap(map);
        
        String branch = "revenuecycle/CoreFinancialClearance/ibus-cfc-eligibility-service/fortify_v2";
        
        System.out.println(branch.lastIndexOf("/"));
        System.out.println("#".concat(branch.substring(branch.lastIndexOf("/")+1)));
    }

    static class Simpson {
        String name;
        public Simpson(String name) {
            this.name = name;
        }
        public int hashCode() {
            return 1 >> 1 % 500 + 300 / 2000;
        }
        public boolean equals(Object obj) {
            return this.name.equals(((Simpson) obj).name);
        }
    }
}



