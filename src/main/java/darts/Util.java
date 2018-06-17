package darts;

import java.io.BufferedReader;
import java.io.FileReader;
import java.util.*;

public class Util {

    private DoubleArrayTrie dat = null;
    private List<String> words = null;
    public Util(){
        init();
    }
    public Util(String dictFile){
        init();
        loadDict(dictFile);
    }

    public DoubleArrayTrie getDat() {
        return dat;
    }

    public List<String> getWords() {
        return words;
    }

    private void init(){
        dat = new DoubleArrayTrie();
        words = new ArrayList<String>();
    }

    public void reLoadDict(String dictFile){
        init();
        loadDict(dictFile);
    }

    public void loadDict(String dictFile){
        try {
            BufferedReader reader = new BufferedReader(new FileReader(dictFile));
            String line;

            Set<Character> charset = new HashSet<Character>();
            while ((line = reader.readLine()) != null) {
                words.add(line);
                // 制作一份码表debug
                for (char c : line.toCharArray()) {
                    charset.add(c);
                }
            }
            reader.close();
            Collections.sort(words);
            System.out.println("dict size：" + words.size());
            System.out.println("error[0: no error]: " + dat.build(words));
        } catch (Exception e){
            System.out.println("bulid dict error!");
            e.printStackTrace();
        }
    }

    public boolean existWord(String word){
        return dat.exactMatchSearch(word) != -1;
    }

    public List<List<Integer>> getStartAndEnd( String str) {
        List<Integer> startlist = new ArrayList<Integer>();
        List<Integer> endlist = new ArrayList<Integer>();
        for (int i = 0; i < str.length();) {
            List<Integer> integerList = dat.commonPrefixSearch(str.substring(i));
            int maxIndex = -1;
            int max = 0;
            if (integerList != null){
                for (int index : integerList) {
                    if (max < words.get(index).length()){
                        max = words.get(index).length();
                        maxIndex = index;
                    }
                }
            }
            if(max != 0) {
                startlist.add(i);
                int len = words.get(maxIndex).length();
                endlist.add(i + len - 1);
                i += len;
            } else {
                i++;
            }
        }
        List<List<Integer>>  list = new ArrayList<List<Integer>>();
        list.add(startlist);
        list.add(endlist);
        return list;
    }
    public List<List<Integer>> getOverlapStartAndEnd( String str) {
        List<Integer> startlist = new ArrayList<Integer>();
        List<Integer> endlist = new ArrayList<Integer>();
        for (int i = 0; i < str.length(); i++) {
            List<Integer> integerList = dat.commonPrefixSearch(str.substring(i));
            int maxIndex = -1;
            int max = 0;
            if (integerList != null){
                for (int index : integerList) {
                    if (max < words.get(index).length()){
                        max = words.get(index).length();
                        maxIndex = index;
                    }
                }
            }
            if(max != 0) {
                startlist.add(i);
                int len = words.get(maxIndex).length();
                endlist.add(i + len - 1);
            }
        }
        List<List<Integer>>  list = new ArrayList<List<Integer>>();
        list.add(startlist);
        list.add(endlist);
        return list;
    }
    public List<List<Integer>> getAllStartAndEnd( String str) {
        List<Integer> startlist = new ArrayList<Integer>();
        List<Integer> endlist = new ArrayList<Integer>();
        for (int i = 0; i < str.length(); i++) {
            List<Integer> integerList = dat.commonPrefixSearch(str.substring(i));
            if (integerList != null){
                for (int index : integerList) {
                    startlist.add(i);
                    int len = words.get(index).length();
                    endlist.add(i + len - 1);
                }
            }
        }
        List<List<Integer>>  list = new ArrayList<List<Integer>>();
        list.add(startlist);
        list.add(endlist);
        return list;
    }

    public List<String> beginMaxMatch(String str){
        List<String> list = new ArrayList<>();
        for (int i = 0; i < str.length();){
            List<Integer> integerList = dat.commonPrefixSearch(str.substring(i));
            int max = 1;
            if (integerList != null){
                for (int index : integerList) {
                    if (max < words.get(index).length()){
                        max = words.get(index).length();
                    }
                }
            }
            list.add(str.substring(i, i+max));
            i += max;
        }
        return list;
    }
    public List<String> endMaxMatch(String str){
        List<String> list = new ArrayList<>();
        for (int i = str.length(); i > 0;) {
            int start = i-1;
            for (int j = 0; j < i; j++) {
                start = j;
                if (dat.exactMatchSearch(str.substring(j, i)) != -1) {
                    break;
                }
            }
            list.add(0, str.substring(start, i));
            i = start;
        }
        return list;
    }

    public List<String> cutSingleSeq(String str){
        List<String> b = beginMaxMatch(str);
        List<String> e = endMaxMatch(str);
        if(b.size() <= e.size())
            return b;
        return e;
    }
    public String singleSeqLabel(String str){
        List<String> wordList = cutSingleSeq(str);
        StringBuilder sb = new StringBuilder();
        for(String word : wordList){
            if (word.length() == 1){
                sb.append("s");
            } else {
                sb.append("b");
                for (int i = 1; i < word.length()-1; i++){
                    sb.append("m");
                }
                sb.append("e");
            }
        }
        return sb.toString();
    }
}
