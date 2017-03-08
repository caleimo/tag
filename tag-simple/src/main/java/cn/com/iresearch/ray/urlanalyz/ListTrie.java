package cn.com.iresearch.ray.urlanalyz;

import java.util.List;


public class ListTrie extends Trie {
	public void insert(String key, List value)
    {
        char chars[] = key.toCharArray();
        Trie.TrieNode now = root;
        char arr$[] = chars;
        int len$ = arr$.length;
        for(int i$ = 0; i$ < len$; i$++)
        {
            char c = arr$[i$];
            now = now.nextNBuild(c);
        }

        if(now.getValue() == null)
            now.setValue(value);
        else
            ((List)now.getValue()).addAll(value);
    }

    public void insert(String x0, Object x1)
    {
        insert(x0, (List)x1);
    }
}
