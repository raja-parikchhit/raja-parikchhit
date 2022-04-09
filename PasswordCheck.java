import org.junit.Assert;

import java.util.HashMap;

//mingbo.qin@aqr.com
public class PasswordCheck {
    public static void main(String[] args) {
        PasswordCheck passwordCheck = new PasswordCheck();
/*        System.out.println("abc --> " + passwordCheck.goodOrBad("abc"));
        System.out.println("xxx --> " + passwordCheck.goodOrBad("xxx"));
        System.out.println("axxxb --> " + passwordCheck.goodOrBad("axxxb"));
        System.out.println("xyxyxy --> " + passwordCheck.goodOrBad("xyxyxy"));
        System.out.println("xyxyexy --> " + passwordCheck.goodOrBad("xyxyexy"));
  */

        //optimized
        System.out.println("abc --> " + passwordCheck.goodOrBadSimplified("abc"));
        System.out.println("xxx --> " + passwordCheck.goodOrBadSimplified("xxx"));
        System.out.println("axxxb --> " + passwordCheck.goodOrBadSimplified("axxxb"));
        System.out.println("xyxyxy --> " + passwordCheck.goodOrBadSimplified("xyxyxy"));
        System.out.println("xyxyexy --> " + passwordCheck.goodOrBadSimplified("xyxyexy"));
        System.out.println("abcdefabcdefabcdef --> " + passwordCheck.goodOrBadSimplified("abcdefabcdefabcdef"));
        System.out.println("xyxyzxyzxyz --> " + passwordCheck.goodOrBadSimplified("xyxyzxyzxyz"));

        /*
        System.out.println("abc --> " + passwordCheck.goodOrBad3("abc"));
        System.out.println("xxx --> " + passwordCheck.goodOrBad3("xxx"));
        System.out.println("axxxb --> " + passwordCheck.goodOrBad3("axxxb"));
        System.out.println("xyxyxy --> " + passwordCheck.goodOrBad3("xyxyxy"));
        System.out.println("xyxyexy --> " + passwordCheck.goodOrBad3("xyxyexy"));
        System.out.println("xyxyzaaa --> " + passwordCheck.goodOrBad3("xyxyzaaa"));
        System.out.println("xyxyzxyzxyz --> " + passwordCheck.goodOrBad3("xyxyzxyzxyz"));
*/
    }

    public String goodOrBadSimplified(String password) {
        //System.out.println("=====================================================");
        Assert.assertNotNull(password, "Password can't be null");
        int pwdLength = password.length();
        if (pwdLength >= 3) {
            int maxWordSize = pwdLength/3; //length of biggest sequence that can be repeated 3 times in passwor. eg, abcd in abcdabcdabcd.
            for (int wordSize = 1; wordSize <= maxWordSize; wordSize++) {
                //System.out.print("Checking for consecutive characters of length " + wordSize);
                for (int i=0; i <= pwdLength - 3*wordSize ; i++) {
                    String first = password.substring(i, i+wordSize);
                    String second = password.substring(i + wordSize, i+ 2*wordSize);
                    if (first.equals(second)) {
                        String third = password.substring(i + 2*wordSize, i + 3*wordSize);
                        if (first.equals(third)) {
                            //System.out.println(" -- " + first + " is repeated.");
                            return "bad";
                        }
                    }
                }
               // System.out.println(" -- clean");
            }
        }
        return "good";
    }

    public String goodOrBad(String password) {
        Assert.assertNotNull(password, "Password can't be null");
        HashMap<Character, Integer> charIndexMap = new HashMap<>();
        if (password.length() > 2) {
            String intermediateString = "";
            String holdString = "";
            boolean hold = false;
            int matchCount = 1;
            int matchIndex = -1;
            for (String s : password.split("")) {
                int i = intermediateString.lastIndexOf(s);
                if (i >= 0) {
                    if (!hold) {
                        matchIndex = i;
                    }
                    hold =true;
                    holdString = holdString.concat(s);
                    if (holdString.equals(intermediateString.substring(matchIndex))) {
                        if(++matchCount == 3) {
                            return "bad";
                        }
                        intermediateString.concat(holdString);
                        holdString = "";
                    }
                } else {
                    intermediateString =  intermediateString.concat(s);
                    hold = false;
                }
            }
        }
        return "good";
    }

    public String goodOrBad3(String password) {
        Assert.assertNotNull(password, "Password can't be null");
        HashMap<Character, Integer> charIndexMap = new HashMap<>();
        int currentIndex = -1;
        if (password.length() > 2) {
            for (Character s : password.toCharArray()) {
                ++currentIndex;
                Integer i = charIndexMap.get(s);
                if (i != null) {
                    //tentative match found at index i
                    int wordSize = currentIndex - i; //wordsize of a tentative match
                    if (password.length() - i < 3*(wordSize)) { // remaining string must be at least 3 times word size
                        return "good";
                    }
                    String first = password.substring(i, i+wordSize);
                    String second = password.substring(i+wordSize, i + 2*wordSize);
                    if (first.equals(second)) {
                        String thrid = password.substring(i + 2*wordSize, i+ 3*wordSize);
                        if(first.equals(thrid)) {
                            return "bad";
                        }
                    }
                }
                charIndexMap.put(s, currentIndex);
            }
        }
        return "good";
    }
}
